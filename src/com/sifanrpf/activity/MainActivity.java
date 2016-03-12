package com.sifanrpf.activity;

import com.sifanrpf.controlanota.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        trataBotoesActividadePrincipal();
    }
    
    public void trataBotoesActividadePrincipal(){
    	
    	final Button botaoAdicionarSemestre = (Button) findViewById(R.id.botao_adicionar_semestre);
        final Button botaoListaSemestres = (Button) findViewById(R.id.botao_lista_semestres);
        final Button botaoListaDisciplinas = (Button) findViewById(R.id.botao_lista_disciplinas);
    	
    	botaoAdicionarSemestre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdicionaSemestreActivity.class);
                startActivity(intent);
            }
        });
        
        botaoListaSemestres.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListaSemestresActivity.class);
                startActivity(intent);
            }
        });
        
        botaoListaDisciplinas.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v){
        		Intent intent = new Intent(getApplicationContext(), ListaDisciplinasActivity.class);
        		startActivity(intent);
        	}
		});
    }
    
    @Override
    public void onBackPressed(){
      Intent intent = new Intent(Intent.ACTION_MAIN);
      intent.addCategory(Intent.CATEGORY_HOME);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    }
}
