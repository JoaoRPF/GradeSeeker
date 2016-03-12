package com.sifanrpf.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sifanrpf.database.DisciplinaDataSource;
import com.sifanrpf.gradeseeker.Disciplina;
import com.sifanrpf.controlanota.R;

public class CriarDisciplinasActivity extends ActionBarActivity{
	
	private DisciplinaDataSource disciplinaDataSource;
	private ListView listView;
	private Disciplina disciplina;
	private int ano;
	private int sem;
	private String nomeDisciplina;
	private float creditos;
	private String professor;
	List<String> nomesTemporarios = new ArrayList<String>();
	List<Float> creditosTemporarios = new ArrayList<Float>();
	EditText editNomeDisciplina = null;
	EditText editCreditos = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.criar_disciplinas);
		disciplinaDataSource = new DisciplinaDataSource(this);
		disciplinaDataSource.open();
		retiraExtrasIntent();
		
		List<Disciplina> disciplinasCriadas = disciplinaDataSource.getDisciplinasBySemestre(ano, sem);
		List<String> disciplinasCriadasStr = retiraDisciplinasByName(disciplinasCriadas);
			
				
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
										android.R.layout.simple_list_item_1, 
										disciplinasCriadasStr);
		
		final ArrayAdapter<Disciplina> adapterDisciplina = new ArrayAdapter<Disciplina>(this,
														android.R.layout.simple_list_item_1, 
														disciplinasCriadas);
		
		editNomeDisciplina = (EditText) findViewById(R.id.inputNomeDisciplina);
		editCreditos = (EditText) findViewById(R.id.inputCreditos);	
		
		listView = (ListView) findViewById(R.id.listaDisciplinasCriadas);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				trataListaDisciplinasCriadas(adapter, adapterDisciplina, position);
			}
		});
				
		final Button botaoAdicionar = (Button) findViewById(R.id.botaoAdd);
		final Button botaoCancelar = (Button) findViewById(R.id.botaoCancelar);
		final Button botaoConcluir = (Button) findViewById(R.id.botaoConcluir);
		
		editNomeDisciplina.requestFocus();
		
		botaoAdicionar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	trataCamposTexto();
            	trataBotaoAdicionar(adapter, adapterDisciplina);
            	InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        		mgr.hideSoftInputFromWindow(editCreditos.getWindowToken(), 0);
        		mgr.hideSoftInputFromInputMethod(editNomeDisciplina.getWindowToken(), 0);
            }
		});
		
		botaoCancelar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				trataBotaoCancelar();
			}
		});
		
		botaoConcluir.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v){
				trataBotaoConcluir();
			}
		});	
	}
	
	public void trataListaDisciplinasCriadas(final ArrayAdapter<String> adapter, final ArrayAdapter<Disciplina> adapterDisciplina, int position){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(CriarDisciplinasActivity.this);
		final String strDisciplina;
		strDisciplina = (String) adapter.getItem(position);
		builder.setMessage("Deseja eliminar a disciplina " + strDisciplina + "? Não é possível desfazer a operação!")
			.setCancelable(false)
			.setPositiveButton("Sim", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id){
					adapter.remove(strDisciplina);
					disciplinaDataSource.deleteDisciplinaByName(strDisciplina);
				}
			})
			.setNegativeButton("Não", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id){
					dialog.cancel();
				}
			});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void retiraExtrasIntent(){
		Bundle bundle = getIntent().getExtras();
		ano = bundle.getInt("Ano");
		sem = bundle.getInt("Sem");
	}
	
	public void trataCamposTexto(){
		nomeDisciplina = editNomeDisciplina.getText().toString();
		if(!editCreditos.getText().toString().equals(""))
			creditos = Float.parseFloat(editCreditos.getText().toString());
	}
	
	public void trataBotaoAdicionar(final ArrayAdapter<String> adapter, ArrayAdapter<Disciplina> adapterDisciplina){
		
		List<Disciplina> allDisciplinas = disciplinaDataSource.getAllDisciplinas();
		
        for(Disciplina d : allDisciplinas){
        	if (d.getNome().equals(nomeDisciplina) || (d.getNome().contains("Ano") 
        												&& d.getNome().contains("Sem")
        												&& d.getNome().contains(nomeDisciplina))){
        		long id = d.getId();
        		disciplinaDataSource.changeDisciplinaName(d, id, nomeDisciplina);
            	nomeDisciplina = nomeDisciplina + " " + ano + "º Ano" + " " + sem + "º Sem";
            }
        }
        
        for(int i=0; i<adapter.getCount(); i++){
        	if(nomeDisciplina.equals(adapter.getItem(i))){
        		return;
        	}
        }
        
        editNomeDisciplina.setText("");
        editCreditos.setText("");
        editNomeDisciplina.requestFocus();
        if(nomeDisciplina.equals("")){
        	Toast.makeText(getBaseContext(), "Por favor, insira um nome para a disciplina", Toast.LENGTH_LONG).show();
        	return;
        }
        
        if(creditos<=0){
        	Toast.makeText(getBaseContext(), "Número de Créditos Inválido", Toast.LENGTH_LONG).show();
        	return;
        }
            	
        adapter.add(nomeDisciplina);
        nomesTemporarios.add(nomeDisciplina);
        creditosTemporarios.add(creditos);
     }
	
	public void trataBotaoCancelar(){
		if (nomesTemporarios.isEmpty()){
			Intent intent = new Intent(getBaseContext(), MenuDisciplinasActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("Ano", ano);
			bundle.putInt("Sem", sem);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		else{
			AlertDialog.Builder builder = new AlertDialog.Builder(CriarDisciplinasActivity.this);
			builder.setMessage("Deseja cancelar e apagar as disciplinas criadas?")
				.setCancelable(false)
				.setPositiveButton("Sim", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int id){
						ProgressDialog dialogEspera = ProgressDialog.show(CriarDisciplinasActivity.this,"","Loading...", true);
						Intent intent = new Intent(getBaseContext(), MenuDisciplinasActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("Ano", ano);
						bundle.putInt("Sem", sem);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				})
				.setNegativeButton("Não", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int id){
						dialog.cancel();
					}
				});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
	
	public void trataBotaoConcluir(){
		int i=-1;
		for (String s : nomesTemporarios){
			i++;
			disciplinaDataSource.createDisciplina(ano, sem, creditosTemporarios.get(i), s, professor);
		}
		Intent intent = new Intent(getBaseContext(), MenuDisciplinasActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("Ano", ano);
		bundle.putInt("Sem", sem);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	public List<String> retiraDisciplinasByName(List<Disciplina> disciplinas){
		List <String> nomes = new ArrayList<String>();
		for (Disciplina d : disciplinas){
			nomes.add(d.getNome());
		}
		return nomes;
	}
	
	@Override
	protected void onResume(){
		disciplinaDataSource.open();
		super.onResume();
	}
	
	@Override
	protected void onPause(){
		disciplinaDataSource.close();
		super.onPause();
	}
	
	@Override
	public void onBackPressed() {
		trataBotaoCancelar();
	}
}
