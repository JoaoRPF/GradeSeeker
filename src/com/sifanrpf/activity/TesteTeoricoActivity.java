package com.sifanrpf.activity;

import java.util.ArrayList;
import java.util.List;

import com.sifanrpf.database.DisciplinaDataSource;
import com.sifanrpf.database.TesteTeoricoDataSource;
import com.sifanrpf.gradeseeker.InputFilterMaxMin;
import com.sifanrpf.controlanota.R;
import com.sifanrpf.gradeseeker.TesteTeorico;

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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class TesteTeoricoActivity extends ActionBarActivity implements OnItemSelectedListener {

	//private DisciplinaDataSource disciplinaDataSource;
	private TesteTeoricoDataSource testeTeoricoDataSource;
	private String nomeDisciplina;
	private Spinner spNumeroTestes;
	private ListView listViewPercs;
	
	private int numeroTestes = 1;
	private int percentagemTotal;
	private float cotacaoInicial;
	int flagCotacoes = 0;
	float[] percsTemporarias = new float[10];
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teste_teorico);
		testeTeoricoDataSource = new TesteTeoricoDataSource(this);
		testeTeoricoDataSource.open();
		retiraExtrasIntent();
		
		final TextView nomeDisciplinaText = (TextView) findViewById(R.id.nomeDisciplina);
		nomeDisciplinaText.setText(nomeDisciplina);
		
		spNumeroTestes = (Spinner) findViewById(R.id.spinnerNumeroTestesTeoricos);
		spNumeroTestes.setOnItemSelectedListener(this);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				 															R.array.anos, 
				 															android.R.layout.simple_spinner_dropdown_item);
		spNumeroTestes.setAdapter(adapter);
		
		final EditText percentagemTesteTeorico = (EditText) findViewById(R.id.percentagemTesteTeorico);
		percentagemTesteTeorico.setFilters(new InputFilter[]{ new InputFilterMaxMin("1", "100")});
		
		percentagemTesteTeorico.setOnFocusChangeListener(new View.OnFocusChangeListener() {
	        @Override
	        public void onFocusChange(View v, boolean hasFocus) {
	            if (!hasFocus) {
	                hideKeyboard(v);
	            }
	        }
	    });
		
		List<String> testesCriadosStr = devolveStringsParaListView();
		
		final ArrayAdapter<String> adapterListaTestes = new ArrayAdapter<String>(this,
																android.R.layout.simple_list_item_1, 
																testesCriadosStr);
		
		listViewPercs = (ListView) findViewById(R.id.listPercentagemCadaTeste);
		listViewPercs.setAdapter(adapterListaTestes);
		
		//ESCUTAR A LISTVIEW USANDO FLAG CRIADA
		listViewPercs.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
				AlertDialog.Builder requestPercentagem = new AlertDialog.Builder(TesteTeoricoActivity.this);
				requestPercentagem.setMessage("Valor do Teste " + (position+1) + " (%)");
				
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
						int numeroTeste = position + 1;
						percsTemporarias[numeroTeste] = inputPercentagemInt;
						flagCotacoes = 1;
						Toast.makeText(getBaseContext(), "Teste " + numeroTeste + " tem o valor de " + inputPercentagemInt + "%"
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
		
		final Button button = (Button) findViewById(R.id.gravarTesteTeorico);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String percentagemTesteStr = percentagemTesteTeorico.getText().toString();
				if(!percentagemTesteStr.equals("")){
					if(verificaPercentagens()){
						percentagemTotal = Integer.parseInt(percentagemTesteStr);
						criaTesteTeorico();
						if(flagCotacoes==0)
							gravaCotacaoTestes();
						if(flagCotacoes==1)
							gravaNovaPercentagem();
						Intent intent = new Intent(getBaseContext(), DisciplinaActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("NomeDisciplina", nomeDisciplina);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					else{
						Toast.makeText(getBaseContext(), "O conjunto dos Testes Teóricos tem que valer 100%"
	        					, Toast.LENGTH_LONG).show();
					}
				}
				else{
					Toast.makeText(getBaseContext(), "Por favor, insira a % que os Testes Teóricos valem."
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
		String numeroTestesString = parent.getItemAtPosition(pos).toString();
		numeroTestes = Integer.parseInt(numeroTestesString);
		cotacaoInicial = (float) (100.0/numeroTestes);
		
		for (int i=1; i<=numeroTestes; i++){
			percsTemporarias[i] = (float) (100/numeroTestes);
		}
		
		flagCotacoes = 0;
		
		final ArrayAdapter<String> adapterListaTestes = new ArrayAdapter<String>(this,
													android.R.layout.simple_list_item_1, 
													devolveStringsParaListView());

		listViewPercs = (ListView) findViewById(R.id.listPercentagemCadaTeste);
		listViewPercs.setAdapter(adapterListaTestes);
		
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		//NAO FAZ NADA
	}
	
	public void criaTesteTeorico(){
		TesteTeorico teste = testeTeoricoDataSource.createTesteTeorico(nomeDisciplina, numeroTestes, percentagemTotal);
	}
	
	public void gravaCotacaoTestes(){
		if(flagCotacoes==0){
			float[] cotacoes = new float[numeroTestes];
			for (int i=0; i<numeroTestes; i++){
				cotacoes[i] = cotacaoInicial;
			}
			testeTeoricoDataSource.gravaCotacaoTestes(nomeDisciplina, numeroTestes, cotacoes);
		}
	}
	
	public List<String> devolveStringsParaListView(){
		List<String> testesStr = new ArrayList<String>();
		for (int i=1; i<=numeroTestes; i++){
			testesStr.add("Teste "+i);
		}
		return testesStr;
	}
	
	public boolean verificaPercentagens(){
		float total = 0;
		for (int i=1; i<=numeroTestes; i++){
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
		testeTeoricoDataSource.alteraCotacaoTeste(nomeDisciplina, numeroTestes, percsTemporarias);
	}
	
	public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
	
	
}
