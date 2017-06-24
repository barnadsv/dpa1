package br.unisinos.barnadsv.dpa_1_app;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        String nomeCompleto = intent.getStringExtra("nome_completo");
        String dataNascimento = intent.getStringExtra("data_nascimento");
        String sexo = intent.getStringExtra("sexo");
        String profissao = intent.getStringExtra("profissao");
        String estadoCivil = intent.getStringExtra("estado_civil");

        Resources res = getResources();

        TextView tvNomeCompleto = (TextView) findViewById(R.id.tvNomeCompleto);
        tvNomeCompleto.setText(res.getString(R.string.nomeCompleto, nomeCompleto));

        TextView tvDataNascimento = (TextView) findViewById(R.id.tvDataNascimento);
        tvDataNascimento.setText(res.getString(R.string.dataNascimento, dataNascimento));

        TextView tvSexo = (TextView) findViewById(R.id.tvSexo);
        tvSexo.setText(res.getString(R.string.sexo, sexo));

        TextView tvProfissao = (TextView) findViewById(R.id.tvProfissao);
        tvProfissao.setText(res.getString(R.string.profissao, profissao));

        TextView tvEstadoCivil = (TextView) findViewById(R.id.tvEstadoCivil);
        tvEstadoCivil.setText(res.getString(R.string.estadoCivil, estadoCivil));

        Button btRetornar = (Button) findViewById(R.id.btRetornar);
        btRetornar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
            startActivity(intent);
            }
        });
    }
}
