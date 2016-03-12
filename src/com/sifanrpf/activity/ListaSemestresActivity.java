package com.sifanrpf.activity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.sifanrpf.database.SemestreDataSource;
import com.sifanrpf.gradeseeker.Disciplina;
import com.sifanrpf.controlanota.R;
import com.sifanrpf.gradeseeker.Semestre;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListaSemestresActivity extends ActionBarActivity{
	
	private SemestreDataSource semestreDataSource;
	private ListView listView;
	private String selectedSemestre;
	private Semestre semestre;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_semestres_activity);
		semestreDataSource = new SemestreDataSource(this);
		semestreDataSource.open();
		List<Semestre> allSemestresExistentes = semestreDataSource.getAllSemestres();
		List<String> allSemestresString = converteSemestreParaString(allSemestresExistentes);
		
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
												android.R.layout.simple_list_item_1, 
												allSemestresString);
		
		final ArrayAdapter<Semestre> adapterSemestre = new ArrayAdapter<Semestre>(this,
														android.R.layout.simple_list_item_1,
														allSemestresExistentes);
		
		listView = (ListView) findViewById(R.id.listagemTodosSemestres);
		listView.setAdapter(adapter);
		adapter.sort(new Comparator<String>() {
		    @Override
		    public int compare(String lhs, String rhs) {
		        return lhs.compareTo(rhs);    
		    }
		});
		
		adapterSemestre.sort(new Comparator<Semestre>() {
			@Override
			public int compare(Semestre lhs, Semestre rhs) {
				if(lhs.getAno() < rhs.getAno())
					return -1; //DEVOLVE O DA ESQUERDA (lhs)
				
				if(lhs.getAno() > rhs.getAno())
					return 1; //DEVOLVE O DA DIREITA (rhs)
				
				if(lhs.getAno() == rhs.getAno()){
					if(lhs.getSem() < rhs.getSem()){
						return -1;
					}
					else
						return 1;
				}
				return 0;
			}
			
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				semestre = (Semestre) adapterSemestre.getItem(position);
				Intent intent = new Intent(getBaseContext(), MenuDisciplinasActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("Ano", semestre.getAno());
				bundle.putInt("Sem", semestre.getSem());
				intent.putExtras(bundle);	
				startActivity(intent);
			}
			
		});
	}
	
	public List<String> converteSemestreParaString(List<Semestre> listaSemestre){
		List<String> listaString = new ArrayList<String>();
		for(Semestre sem : listaSemestre){
			String str = sem.getAno() + "º Ano " + sem.getSem() + "º Semestre";
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
