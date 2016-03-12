package com.sifanrpf.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.sifanrpf.database.DisciplinaDataSource;
import com.sifanrpf.gradeseeker.Disciplina;
import com.sifanrpf.controlanota.R;

public class MenuDisciplinasActivity extends ActionBarActivity {
	
	private DisciplinaDataSource disciplinaDataSource;
	private ListView listView;
	private Disciplina disciplina;
	
	private int ano;
	private int sem;
	private String nomeDisciplina;
	private int creditos;
	private String professor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_disciplinas);
		disciplinaDataSource = new DisciplinaDataSource(this);
		disciplinaDataSource.open();
		retiraExtrasIntent();
		
		final TextView nomeSemestre = (TextView) findViewById(R.id.menu_disciplinas_nome_semestre);
		nomeSemestre.setText("Disciplinas do " + ano + "ºAno, " + sem + "ºSem");
		
		List<String> disciplinasCriadasStr = getDisciplinasName();
		
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
										android.R.layout.simple_list_item_1, 
										disciplinasCriadasStr);
		
		final Button botaoCriarRemoverDisciplina= (Button) findViewById(R.id.botao_criar_remover_disciplina);
		
		botaoCriarRemoverDisciplina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(getBaseContext(), CriarDisciplinasActivity.class);
            	Bundle bundle = new Bundle();
				bundle.putInt("Ano", ano);
				bundle.putInt("Sem", sem);
				intent.putExtras(bundle);	
            	startActivity(intent);
            }
		});
		
		listView = (ListView) findViewById(R.id.listaDisciplinasDoSemestre);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				Intent intent = new Intent(getBaseContext(), DisciplinaActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("Ano", ano);
				bundle.putInt("Sem", sem);
				bundle.putString("NomeDisciplina", adapter.getItem(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	
	public void retiraExtrasIntent(){
		if(getIntent().getExtras().isEmpty())
			return;
		Bundle bundle = getIntent().getExtras();
		ano = bundle.getInt("Ano");
		sem = bundle.getInt("Sem");
	}
	
	public List<String> getDisciplinasName(){
		
		List<String> discName = new ArrayList<String>();
		List<Disciplina> disc = disciplinaDataSource.getDisciplinasBySemestre(ano, sem);
		for (Disciplina d : disc){
			discName.add(d.getNome());
		}
		return discName;
	}
	
	@Override
	public void onBackPressed() {
	    finish();
	    Intent intent = new Intent(getBaseContext(), ListaSemestresActivity.class);
	    Bundle bundle = new Bundle();
		bundle.putInt("Ano", ano);
		bundle.putInt("Sem", sem);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
}
