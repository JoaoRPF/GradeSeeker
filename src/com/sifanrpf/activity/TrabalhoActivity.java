package com.sifanrpf.activity;

import java.util.ArrayList;
import java.util.List;

import com.sifanrpf.database.TrabalhoDataSource;
import com.sifanrpf.gradeseeker.InputFilterMaxMin;
import com.sifanrpf.controlanota.R;
import com.sifanrpf.gradeseeker.Trabalho;

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

public class TrabalhoActivity extends ActionBarActivity implements OnItemSelectedListener {
	
	private TrabalhoDataSource trabalhoDataSource;
	private String nomeDisciplina;
	private Spinner spNumeroTrabalhos;
	private ListView listViewPercs;
	
	private int numeroTrabalhos = 1;
	private int percentagemTotal;
	private float cotacaoInicial;
	int flagCotacoes = 0;
	float[] percsTemporarias = new float[10];
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trabalho_activity);
		trabalhoDataSource = new TrabalhoDataSource(this);
		trabalhoDataSource.open();
		retiraExtrasIntent();
		
		final TextView nomeDisciplinaText = (TextView) findViewById(R.id.nomeDisciplina);
		nomeDisciplinaText.setText(nomeDisciplina);
		
		spNumeroTrabalhos = (Spinner) findViewById(R.id.spinnerNumeroTrabalhos);
		spNumeroTrabalhos.setOnItemSelectedListener(this);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				 															R.array.anos, 
				 															android.R.layout.simple_spinner_dropdown_item);
		spNumeroTrabalhos.setAdapter(adapter);
		
		final EditText percentagemTrabalho = (EditText) findViewById(R.id.percentagemTrabalho);
		percentagemTrabalho.setFilters(new InputFilter[]{ new InputFilterMaxMin("1", "100")});
		
		percentagemTrabalho.setOnFocusChangeListener(new View.OnFocusChangeListener() {
	        @Override
	        public void onFocusChange(View v, boolean hasFocus) {
	            if (!hasFocus) {
	                hideKeyboard(v);
	            }
	        }
	    });
		
		List<String> trabalhosCriadosStr = devolveStringsParaListView();
		
		final ArrayAdapter<String> adapterListaTrabalhos = new ArrayAdapter<String>(this,
																android.R.layout.simple_list_item_1, 
																trabalhosCriadosStr);
		
		listViewPercs = (ListView) findViewById(R.id.listPercentagemCadaTrabalho);
		listViewPercs.setAdapter(adapterListaTrabalhos);
		
		//ESCUTAR A LISTVIEW USANDO FLAG CRIADA
		listViewPercs.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
				AlertDialog.Builder requestPercentagem = new AlertDialog.Builder(TrabalhoActivity.this);
				requestPercentagem.setMessage("Valor do Trabalho " + (position+1) + " (%)");
				
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
						int numeroTrabalho = position + 1;
						percsTemporarias[numeroTrabalho] = inputPercentagemInt;
						flagCotacoes = 1;
						Toast.makeText(getBaseContext(), "Trabalho " + numeroTrabalho + " tem o valor de " + inputPercentagemInt + "%"
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
		
		final Button button = (Button) findViewById(R.id.gravarTrabalho);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String percentagemTrabalhoStr = percentagemTrabalho.getText().toString();
				if(!percentagemTrabalhoStr.equals("")){
					if(verificaPercentagens()){
						percentagemTotal = Integer.parseInt(percentagemTrabalhoStr);
						criaTrabalho();
						if(flagCotacoes==0)
							gravaCotacaoTrabalhos();
						if(flagCotacoes==1)
							gravaNovaPercentagem();
						Intent intent = new Intent(getBaseContext(), DisciplinaActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("NomeDisciplina", nomeDisciplina);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					else{
						Toast.makeText(getBaseContext(), "O conjunto dos Trabalhos/Projectos tem que valer 100%"
	        					, Toast.LENGTH_LONG).show();
					}
				}
				else{
					Toast.makeText(getBaseContext(), "Por favor, insira a % que os Trabalhos/Projectos valem."
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
		String numeroTrabalhosString = parent.getItemAtPosition(pos).toString();
		numeroTrabalhos = Integer.parseInt(numeroTrabalhosString);
		cotacaoInicial = (float) (100.0/numeroTrabalhos);
		
		for (int i=1; i<=numeroTrabalhos; i++){
			percsTemporarias[i] = (float) (100/numeroTrabalhos);
		}
		
		flagCotacoes = 0;
		
		final ArrayAdapter<String> adapterListaTrabalhos = new ArrayAdapter<String>(this,
													android.R.layout.simple_list_item_1, 
													devolveStringsParaListView());

		listViewPercs = (ListView) findViewById(R.id.listPercentagemCadaTrabalho);
		listViewPercs.setAdapter(adapterListaTrabalhos);
		
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		//NAO FAZ NADA
	}
	
	public void criaTrabalho(){
		Trabalho trabalho = trabalhoDataSource.createTrabalho(nomeDisciplina, numeroTrabalhos, percentagemTotal);
	}
	
	public void gravaCotacaoTrabalhos(){
		if(flagCotacoes==0){
			float[] cotacoes = new float[numeroTrabalhos];
			for (int i=0; i<numeroTrabalhos; i++){
				cotacoes[i] = cotacaoInicial;
			}
			trabalhoDataSource.gravaCotacaoTrabalhos(nomeDisciplina, numeroTrabalhos, cotacoes);
		}
	}
	
	public List<String> devolveStringsParaListView(){
		List<String> trabalhosStr = new ArrayList<String>();
		for (int i=1; i<=numeroTrabalhos; i++){
			trabalhosStr.add("Trabalho "+i);
		}
		return trabalhosStr;
	}
	
	public boolean verificaPercentagens(){
		float total = 0;
		for (int i=1; i<=numeroTrabalhos; i++){
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
		trabalhoDataSource.alteraCotacaoTrabalho(nomeDisciplina, numeroTrabalhos, percsTemporarias);
	}
	
	public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
