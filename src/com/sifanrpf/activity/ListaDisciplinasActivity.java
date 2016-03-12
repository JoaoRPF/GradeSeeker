package com.sifanrpf.activity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.sifanrpf.database.DisciplinaDataSource;
import com.sifanrpf.gradeseeker.Disciplina;
import com.sifanrpf.controlanota.R;

public class ListaDisciplinasActivity extends ActionBarActivity{
	
	private DisciplinaDataSource disciplinaDataSource;
	private ListView listView;
	private Disciplina disciplina;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_disciplinas_activity);
		disciplinaDataSource = new DisciplinaDataSource(this);
		disciplinaDataSource.open();
		List<Disciplina> allDisciplinasExistentes = disciplinaDataSource.getAllDisciplinas();
		List<String> allDisciplinasString = converteDisciplinaParaString(allDisciplinasExistentes);
		
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
												android.R.layout.simple_list_item_1, 
												allDisciplinasString);
		
		final ArrayAdapter<Disciplina> adapterDisciplina = new ArrayAdapter<Disciplina>(this,
														android.R.layout.simple_list_item_1,
														allDisciplinasExistentes);
		
		listView = (ListView) findViewById(R.id.listagemTodasDisciplinas);
		listView.setAdapter(adapter);
		adapter.sort(new Comparator<String>() {
		    @Override
		    public int compare(String lhs, String rhs) {
		        return lhs.compareTo(rhs);    
		    }
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				Intent intent = new Intent(getBaseContext(), DisciplinaActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("NomeDisciplina", adapter.getItem(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	
	public List<String> converteDisciplinaParaString(List<Disciplina> listaDisciplina){
		List<String> listaString = new ArrayList<String>();
		for(Disciplina d : listaDisciplina){
			String str = d.getNome();
			listaString.add(str);
		}
		return listaString;
	}
	
	@Override
	public void onBackPressed() {
	    finish();
	    Intent intent = new Intent(getBaseContext(), MainActivity.class);
		startActivity(intent);
	}

}
