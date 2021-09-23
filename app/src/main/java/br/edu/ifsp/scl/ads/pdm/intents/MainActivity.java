package br.edu.ifsp.scl.ads.pdm.intents;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import br.edu.ifsp.scl.ads.pdm.intents.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private ActivityResultLauncher<Intent> outraActivityResultLauncher;
    public static final String PARAMETRO = "PARAMETRO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        getSupportActionBar().setTitle("Tratando Intents");
        getSupportActionBar().setSubtitle("Principais tipos");

        outraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        activityMainBinding.retornoTv.setText(result.getData().getStringExtra(PARAMETRO));
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
                return true;
            case R.id.callMi:
                // Fazer uma chamada
                return true;
            case R.id.dialMi:
                // Abrir discador
                return true;
            case R.id.pickMi:
                // Pegar uma imagem
                return true;
            case R.id.chooserMi:
                // Abrir lista de aplicativos
                return true;
            default: return false;
        }
    }
}