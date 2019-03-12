package com.example.maikon.app01application;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class ListarPessoasActivity extends AppCompatActivity {

    private ListView listView;
    private PessoaDao dao;
    private List<Pessoa> pessoasList;
    private List<Pessoa> pessoasFiltradas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pessoas);

        listView = (ListView) findViewById(R.id.ListaPessoas);
        dao = new PessoaDao(this);
        pessoasList = dao.getAll();
        pessoasFiltradas.addAll(pessoasList);

        ArrayAdapter<Pessoa> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pessoasFiltradas );

        listView.setAdapter(adaptador);
        registerForContextMenu(listView);  // quando o listview for precionado abrira o menu


    }

    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_principal, menu );

        //criar searceView para verifivar se algo foi digitado na barra de pesquisa
        SearchView sv = (SearchView)menu.findItem(R.id.app_bar_search).getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                procuraPessoaLista(s);
                return false;
            }
        });
        return true;

    }

    public void procuraPessoaLista(String nome){
        pessoasFiltradas.clear();
        for (Pessoa a : pessoasList){
            if(a.getNome().toLowerCase().contains(nome.toLowerCase())){
                pessoasFiltradas.add(a);
            }
        }
        listView.invalidateViews();
    }
    public void delete(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Pessoa pessoaExcluir = pessoasFiltradas.get(menuInfo.position);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atencao")
                .setMessage("Deseja excluir essa pessoa")
                .setNegativeButton("Nao",null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i ) {
                        pessoasFiltradas.remove(pessoaExcluir);
                        pessoasList.remove(pessoaExcluir);
                        dao.delete(pessoaExcluir);
                        listView.invalidateViews();
                    }
                }).create();
        dialog.show();
    }
    public void upDate(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Pessoa pessoaUpdate = pessoasFiltradas.get(menuInfo.position);
        Intent it = new Intent(ListarPessoasActivity.this, MainActivity.class);
        it.putExtra("pessoa", pessoaUpdate);
        startActivity(it);
    }

    @Override
    public void onResume(){
        super.onResume();
        pessoasList = dao.getAll();
        pessoasFiltradas.clear();
        pessoasFiltradas.addAll(pessoasList);
        listView.invalidateViews();
    }

    public void cadastrar(MenuItem item){
        Intent it = new Intent(ListarPessoasActivity.this, MainActivity.class);
        startActivity(it);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto, menu);
    }
}

