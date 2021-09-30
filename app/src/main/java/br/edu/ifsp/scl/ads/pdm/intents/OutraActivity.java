package br.edu.ifsp.scl.ads.pdm.intents;

import static br.edu.ifsp.scl.ads.pdm.intents.MainActivity.PARAMETRO;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Objects;

import br.edu.ifsp.scl.ads.pdm.intents.databinding.ActivityOutraBinding;

public class OutraActivity extends AppCompatActivity {
    private ActivityOutraBinding activityOutraBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityOutraBinding = ActivityOutraBinding.inflate(getLayoutInflater());
        setContentView(activityOutraBinding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).setTitle("Outra Activity");
        getSupportActionBar().setSubtitle("Recebe e retorna o valor");

        activityOutraBinding.recebidoTv.setText(getIntent().getStringExtra(PARAMETRO));

        activityOutraBinding.retornarBt.setOnClickListener(view -> {
                    Intent retornoIntent = new Intent();
                    retornoIntent.putExtra(PARAMETRO, activityOutraBinding.retornoEt.getText().toString());
                    setResult(RESULT_OK, retornoIntent);
                    finish();
                });
    }
}