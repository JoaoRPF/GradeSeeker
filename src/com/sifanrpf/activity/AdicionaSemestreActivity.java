package com.sifanrpf.activity;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.sifanrpf.controlanota.R;
import com.sifanrpf.gradeseeker.Semestre;
import com.sifanrpf.database.SemestreDataSource;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdicionaSemestreActivity extends ActionBarActivity implements OnItemSelectedListener {

    private String anoString;
	private Integer ano;
	private String semestreString;
	private Integer semestre;
	private Spinner spinnerAnos;
	private RadioButton radioButtonSemestre1, radioButtonSemestre2;
	private SemestreDataSource semestreDataSource;
	private Semestre newSemestre;
	private List<Semestre> allSemestresExistentes = new ArrayList<Semestre>();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adiciona_semestre);
        semestreDataSource = new SemestreDataSource(this);
        semestreDataSource.open();
        allSemestresExistentes = semestreDataSource.getAllSemestres();
        
        spinnerAnos = (Spinner) findViewById(R.id.spinnerAnos);
        spinnerAnos.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        									 R.array.anos, 
        									 android.R.layout.simple_spinner_dropdown_item);
        spinnerAnos.setAdapter(adapter);
        
        radioButtonSemestre1 = (RadioButton) findViewById(R.id.semestre1);
        radioButtonSemestre2 = (RadioButton) findViewById(R.id.semestre2);
        
        Button setaDireita = (Button) findViewById(R.id.setaSeguinte);
        
        setaDireita.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	boolean semestreSelecionado = trataRadioButtonSemestres();
            	if(semestreSelecionado){
            		if(verificaSemestreValido(allSemestresExistentes)){
            			//Toast.makeText(getBaseContext(), "OK!!!!", Toast.LENGTH_LONG).show();
            			newSemestre = semestreDataSource.createSemestre(ano, semestre);
            			Intent intent = new Intent(getBaseContext(), ListaSemestresActivity.class);
            			startActivity(intent);
            		}
            		else{
            			Toast.makeText(getBaseContext(), "Este semestre já foi criado."
            					+ " Por favor edite-o ou crie um novo.", Toast.LENGTH_LONG).show();
            		}
            	}
            	else
            		return;
            }
        });
        
    }


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		anoString = parent.getItemAtPosition(position).toString();
		ano = Integer.parseInt(anoString);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		//NAO FAZ NADA
	}
	
	public boolean trataRadioButtonSemestres(){
		if(radioButtonSemestre1.isChecked())
			semestre = 1;
		if(radioButtonSemestre2.isChecked())
			semestre = 2;
		
		if(!radioButtonSemestre1.isChecked() && !radioButtonSemestre2.isChecked()){
			Toast.makeText(getBaseContext(), "Por favor, selecione um semestre", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	public boolean verificaSemestreValido(List<Semestre> allSemestresExistentes){
		for(Semestre s : allSemestresExistentes){
			if (ano == s.getAno() && semestre == s.getSem()){
				return false;
			}
		}
		return true;	
	}
	
	@Override
	protected void onResume(){
		semestreDataSource.open();
		super.onResume();
	}
	
	@Override
	protected void onPause(){
		semestreDataSource.close();
		super.onPause();
	}
	
	@Override
	public void onBackPressed() {
	    finish();
	    Intent intent = new Intent(getBaseContext(), MainActivity.class);
		startActivity(intent);
	}
}
