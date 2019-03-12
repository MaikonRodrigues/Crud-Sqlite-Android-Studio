package com.example.maikon.app01application;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;


public class MainActivity extends AppCompatActivity {

    EditText nome, cpf, telefone;
    Button btnCriar;
    PessoaDao dao;
    Pessoa pessoaUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = (EditText) findViewById(R.id.editNome);
        cpf = (EditText) findViewById(R.id.editCpf);
        telefone = (EditText) findViewById(R.id.editTelefone);

        btnCriar = (Button) findViewById(R.id.btnCriar);

        dao = new PessoaDao(this);

        Intent it = getIntent();
        if (it.hasExtra("pessoa")){
            pessoaUpdate = (Pessoa) it.getSerializableExtra("pessoa");
            nome.setText(pessoaUpdate.getNome().toString());
            cpf.setText(pessoaUpdate.getCpf().toString());
            telefone.setText(pessoaUpdate.getTelefone().toString());
        }

    }



    public void salvar(View view) {

        if (pessoaUpdate == null) {

            Pessoa pessoa = new Pessoa();
            pessoa.setNome(nome.getText().toString());
            pessoa.setCpf(cpf.getText().toString());
            pessoa.setTelefone(telefone.getText().toString());
            long id = dao.inserirPessoa(pessoa);

            Toast toast = Toast.makeText(this, "Insercao concluida com id: " + id, LENGTH_SHORT);
            toast.show();
            nome.setText("");
            cpf.setText("");
            telefone.setText("");

            Intent it = new Intent(MainActivity.this, ListarPessoasActivity.class);
            startActivity(it);

        }else{

            pessoaUpdate.setNome(nome.getText().toString());
            pessoaUpdate.setCpf(cpf.getText().toString());
            pessoaUpdate.setTelefone(telefone.getText().toString());
            dao.upDate(pessoaUpdate);
            Toast toast = Toast.makeText(this, "Atualizacao concluida ", LENGTH_SHORT);
            toast.show();

            Intent it = new Intent(MainActivity.this, ListarPessoasActivity.class);
            startActivity(it);

        }
    }
}



