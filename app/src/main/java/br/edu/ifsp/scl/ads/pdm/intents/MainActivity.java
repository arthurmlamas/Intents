package br.edu.ifsp.scl.ads.pdm.intents;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.edu.ifsp.scl.ads.pdm.intents.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private ActivityResultLauncher<Intent> outraActivityResultLauncher;
    private ActivityResultLauncher<String> requisicaoPermissaoActivityResultLauncher;
    private ActivityResultLauncher<Intent> selecionarImagemActivityResultLauncher;
    private ActivityResultLauncher<Intent> escolherActivityResultLauncher;
    public static final String PARAMETRO = "PARAMETRO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle("Tratando Intents");
        getSupportActionBar().setSubtitle("Principais tipos");

        outraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        activityMainBinding.retornoTv.setText(result.getData() != null ? result.getData().getStringExtra(PARAMETRO) : null);
                    }
                });

        requisicaoPermissaoActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                concedida -> {
                    if (!concedida) {
                        requisitarPermissaoLigacao();
                    } else {
                        chamarTelefone();
                    }
                }
        );

        selecionarImagemActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::vizualizarImagem
        );

        escolherActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::vizualizarImagem
        );

    }

    private void vizualizarImagem(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent vizualizarImagemIntent = new Intent(Intent.ACTION_VIEW);
            vizualizarImagemIntent.setData(Objects.requireNonNull(result.getData()).getData());
            startActivity(vizualizarImagemIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.outraActivityMi:
                Intent outraActivityIntent = new Intent(this, OutraActivity.class);
                outraActivityIntent.putExtra(PARAMETRO, activityMainBinding.parametroEt.getText().toString());
                outraActivityResultLauncher.launch(outraActivityIntent);
                return true;
            case R.id.viewMi:
                // Abrir um navegador
                String url = activityMainBinding.parametroEt.getText().toString().toLowerCase(Locale.ROOT);
                Pattern pattern = Pattern.compile("http[s]?", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(url);
                if (!matcher.find()) {
                    url = "http://" + url;
                }
                Intent siteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(siteIntent);
                return true;
            case R.id.callMi:
                // Fazer uma chamada
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        requisitarPermissaoLigacao();
                    } else {
                        chamarTelefone();
                    }
                } else {
                    chamarTelefone();
                }
                return true;
            case R.id.dialMi:
                // Abrir discador
                discarTelefone();
                return true;
            case R.id.pickMi:
                // Pegar uma imagem
                selecionarImagemActivityResultLauncher.launch(prepararImagemIntent());
                return true;
            case R.id.chooserMi:
                // Abrir lista de aplicativos
                Intent escolherActivityIntent = new Intent(Intent.ACTION_CHOOSER);
                escolherActivityIntent.putExtra(Intent.EXTRA_INTENT, prepararImagemIntent());
                escolherActivityIntent.putExtra(Intent.EXTRA_TITLE, "Escolha um aplicativo");
                escolherActivityResultLauncher.launch(escolherActivityIntent);
                return true;
            default: return false;
        }
    }

    private void requisitarPermissaoLigacao() {
        requisicaoPermissaoActivityResultLauncher.launch(Manifest.permission.CALL_PHONE);
    }

    private void chamarTelefone() {
        Intent chamarIntent = new Intent();
        chamarIntent.setAction(Intent.ACTION_CALL);
        chamarIntent.setData(Uri.parse("tel: " + activityMainBinding.parametroEt.getText()));
        startActivity(chamarIntent);
    }

    private void discarTelefone() {
        Intent discadorIntent = new Intent(Intent.ACTION_DIAL);
        discadorIntent.setData(Uri.parse("tel: " + activityMainBinding.parametroEt.getText()));
        startActivity(discadorIntent);
    }

    private Intent prepararImagemIntent() {
        Intent pegarImagemIntent = new Intent(Intent.ACTION_PICK);
        String diretorio = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        pegarImagemIntent.setDataAndType(Uri.parse(diretorio), "image/*");
        return pegarImagemIntent;
    }
}