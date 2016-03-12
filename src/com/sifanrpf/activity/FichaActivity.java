package com.sifanrpf.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.sifanrpf.database.FichaDataSource;
import com.sifanrpf.gradeseeker.Ficha;
import com.sifanrpf.gradeseeker.InputFilterMaxMin;
import com.sifanrpf.controlanota.R;

public class FichaActivity extends ActionBarActivity implements OnItemSelectedListener {
	
	private FichaDataSource fichaDataSource;
	private String nomeDisciplina;
	private Spinner spNumeroFichas;
	private ListView listViewPercs;
	
	private int numeroFichas = 1;
	private int percentagemTotal;
	private float cotacaoInicial;
	int flagCotacoes = 0;
	float[] percsTemporarias = new float[10];
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ficha_activity);
		fichaDataSource = new FichaDataSource(this);
		fichaDataSource.open();
		retiraExtrasIntent();
		
		final TextView nomeDisciplinaText = (TextView) findViewById(R.id.nomeDisciplina);
		nomeDisciplinaText.setText(nomeDisciplina);
		
		spNumeroFichas = (Spinner) findViewById(R.id.spinnerNumeroFichas);
		spNumeroFichas.setOnItemSelectedListener(this);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				 															R.array.anos, 
				 															android.R.layout.simple_spinner_dropdown_item);
		spNumeroFichas.setAdapter(adapter);
		
		final EditText percentagemFicha = (EditText) findViewById(R.id.percentagemFicha);
		percentagemFicha.setFilters(new InputFilter[]{ new InputFilterMaxMin("1", "100")});
		
		percentagemFicha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
	        @Override
	        public void onFocusChange(View v, boolean hasFocus) {
	            if (!hasFocus) {
	                hideKeyboard(v);
	            }
	        }
	    });
		
		List<String> fichasCriadosStr = devolveStringsParaListView();
		
		final ArrayAdapter<String> adapterListaFichas = new ArrayAdapter<String>(this,
																android.R.layout.simple_list_item_1, 
																fichasCriadosStr);
		
		listViewPercs = (ListView) findViewById(R.id.listPercentagemCadaFicha);
		listViewPercs.setAdapter(adapterListaFichas);
		
		//ESCUTAR A LISTVIEW USANDO FLAG CRIADA
		listViewPercs.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
				AlertDialog.Builder requestPercentagem = new AlertDialog.Builder(FichaActivity.this);
				requestPercentagem.setMessage("Valor do Ficha " + (position+1) + " (%)");
				
				final EditText inputPercentagemText = new EditText(getBaseContext());
				inputPercentagemText.setTextColor(Color.BLACK);
				inputPercentagemText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
				
				requestPercentagem.setView(inputPercentagemText);
				
				requestPercentagem.setCancelable(false);
				
				requestPercentagem.setPositiveButton("Gravar", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String inputPercentagemStr = inputPercentagemText.getText().toString();
						int inputPercentagemInt = Integer.parseInt(inputPercentagemStr);
						int numeroFicha = position + 1;
						percsTemporarias[numeroFicha] = inputPercentagemInt;
						flagCotacoes = 1;
						Toast.makeText(getBaseContext(), "Ficha " + numeroFicha + " tem o valor de " + inputPercentagemInt + "%"
	        					, Toast.LENGTH_LONG).show();
					}
				});
				
				requestPercentagem.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				
				AlertDialog requestPercAlert = requestPercentagem.create();
				requestPercAlert.show();
				requestPercAlert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			}
		});
		
		final Button button = (Button) findViewById(R.id.gravarFicha);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String percentagemFichaStr = percentagemFicha.getText().toString();
				if(!percentagemFichaStr.equals("")){
					if(verificaPercentagens()){
						percentagemTotal = Integer.parseInt(percentagemFichaStr);
						criaFicha();
						if(flagCotacoes==0)
							gravaCotacaoFichas();
						if(flagCotacoes==1)
							gravaNovaPercentagem();
						Intent intent = new Intent(getBaseContext(), DisciplinaActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("NomeDisciplina", nomeDisciplina);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					else{
						Toast.makeText(getBaseContext(), "O conjunto das Fichas/Exercicios tem que valer 100%"
	        					, Toast.LENGTH_LONG).show();
					}
				}
				else{
					Toast.makeText(getBaseContext(), "Por favor, insira a % que as Fichas/Exercicios valem."
        					, Toast.LENGTH_LONG).show();
				}
			}
		});		
		
	}
	
	public void retiraExtrasIntent(){
		Bundle bundle = getIntent().getExtras();
		nomeDisciplina = bundle.getString("NomeDisciplina");
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
		String numeroFichasString = parent.getItemAtPosition(pos).toString();
		numeroFichas = Integer.parseInt(numeroFichasString);
		cotacaoInicial = (float) (100.0/numeroFichas);
		
		for (int i=1; i<=numeroFichas; i++){
			percsTemporarias[i] = (float) (100/numeroFichas);
		}
		
		flagCotacoes = 0;
		
		final ArrayAdapter<String> adapterListaFichas = new ArrayAdapter<String>(this,
													android.R.layout.simple_list_item_1, 
													devolveStringsParaListView());

		listViewPercs = (ListView) findViewById(R.id.listPercentagemCadaFicha);
		listViewPercs.setAdapter(adapterListaFichas);
		
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		//NAO FAZ NADA
	}
	
	public void criaFicha(){
		Ficha ficha = fichaDataSource.createFicha(nomeDisciplina, numeroFichas, percentagemTotal);
	}
	
	public void gravaCotacaoFichas(){
		if(flagCotacoes==0){
			float[] cotacoes = new float[numeroFichas];
			for (int i=0; i<numeroFichas; i++){
				cotacoes[i] = cotacaoInicial;
			}
			fichaDataSource.gravaCotacaoFichas(nomeDisciplina, numeroFichas, cotacoes);
		}
	}
	
	public List<String> devolveStringsParaListView(){
		List<String> fichasStr = new ArrayList<String>();
		for (int i=1; i<=numeroFichas; i++){
			fichasStr.add("Ficha "+i);
		}
		return fichasStr;
	}
	
	public boolean verificaPercentagens(){
		float total = 0;
		for (int i=1; i<=numeroFichas; i++){
			total = total + percsTemporarias[i];
		}
		if ((float)total > 98.9)
			return true;
		if ((float)total < 100.9)
			return true;
		else
			return false;
	}
	
	public void gravaNovaPercentagem(){
		fichaDataSource.alteraCotacaoFicha(nomeDisciplina, numeroFichas, percsTemporarias);
	}
	
	public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
