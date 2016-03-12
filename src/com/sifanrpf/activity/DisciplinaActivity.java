package com.sifanrpf.activity;

import java.util.ArrayList;
import java.util.List;

import com.sifanrpf.database.BonusDataSource;
import com.sifanrpf.database.DisciplinaDataSource;
import com.sifanrpf.database.FichaDataSource;
import com.sifanrpf.database.MiniTesteDataSource;
import com.sifanrpf.database.MiniTesteSQLiteHelper;
import com.sifanrpf.database.TestePraticoDataSource;
import com.sifanrpf.database.TesteTeoricoDataSource;
import com.sifanrpf.database.TrabalhoDataSource;
import com.sifanrpf.gradeseeker.InputFilterMaxMin;
import com.sifanrpf.gradeseeker.InputFilterMaxMinFloat;
import com.sifanrpf.controlanota.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DisciplinaActivity extends ActionBarActivity {
	
	private DisciplinaDataSource disciplinaDataSource;
	private TesteTeoricoDataSource testeTeoricoDataSource;
	private TrabalhoDataSource trabalhoDataSource;
	private TestePraticoDataSource testePraticoDataSource;
	private MiniTesteDataSource miniTesteDataSource;
	private BonusDataSource bonusDataSource;
	private FichaDataSource fichaDataSource;
	
	private int ano=0;
	private int sem=0;
	private String nomeDisciplina;
	private float creditos;
	private ListView listViewAvaliacoes;
	private int notaAlteradaTemporaria;
	private int flagAlteracaoNumeros = 0;
	int numeroTestes;
	int numeroTrabalhos;
	int numeroTestesPraticos;
	int numeroMiniTestes;
	int numeroBonus;
	int numeroFichas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.disciplina);
		
		disciplinaDataSource = new DisciplinaDataSource(this);
		disciplinaDataSource.open();
		testeTeoricoDataSource = new TesteTeoricoDataSource(this);
		testeTeoricoDataSource.open();
		trabalhoDataSource = new TrabalhoDataSource(this);
		trabalhoDataSource.open();
		testePraticoDataSource = new TestePraticoDataSource(this);
		testePraticoDataSource.open();
		miniTesteDataSource = new MiniTesteDataSource(this);
		miniTesteDataSource.open();
		bonusDataSource = new BonusDataSource(this);
		bonusDataSource.open();
		fichaDataSource = new FichaDataSource(this);
		fichaDataSource.open();
		
		retiraExtrasIntent();
		creditos = disciplinaDataSource.getCreditosByName(nomeDisciplina);
		
		final TextView nomeDisciplinaText = (TextView) findViewById(R.id.nomeDisciplina);
		nomeDisciplinaText.setText(nomeDisciplina);
		
		final TextView numeroCreditosText = (TextView) findViewById(R.id.numeroCreditos);
		numeroCreditosText.setText("Créditos: "+ creditos);
		
		final Button alteraNumeroCreditosButton = (Button) findViewById(R.id.editarNumeroCreditos);
		alteraNumeroCreditosButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder requestCreditos = new AlertDialog.Builder(DisciplinaActivity.this);
				requestCreditos.setMessage("Insira número de créditos da cadeira '" + nomeDisciplina + "'");
				
				final EditText inputCreditosText = new EditText(getBaseContext());
				inputCreditosText.setTextColor(Color.BLACK);
				inputCreditosText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
				
				requestCreditos.setView(inputCreditosText);
				
				requestCreditos.setCancelable(true);
				
				requestCreditos.setPositiveButton("Gravar", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String inputCreditosStr = inputCreditosText.getText().toString();
						if(inputCreditosStr.contains(",")){
							String[] partes = inputCreditosStr.split(",");
							inputCreditosStr = partes[0] + "." + partes[1];
						}
						float inputCreditosInt = Float.parseFloat(inputCreditosStr); 
						gravaNovosCreditos(inputCreditosInt);
						numeroCreditosText.setText("Créditos: "+ creditos);
					}
				});
				
				requestCreditos.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				
				AlertDialog requestCreditosAlert = requestCreditos.create();
				requestCreditosAlert.show();
				requestCreditosAlert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				
			}
			
		});
		
		final Button criaAvaliacaoButton = (Button) findViewById(R.id.criarAvaliacao);
		criaAvaliacaoButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				final CharSequence[] hipoteses = {"Teste/ Exame Teórico",
												  "Trabalhos/ Projectos/ Relatórios",
												  "Teste/ Exame Prático",
												  "Mini Testes",
												  "Bónus",
												  "Fichas/ Exercicios"};
				
				AlertDialog.Builder requestFormaAvaliacao = new AlertDialog.Builder(DisciplinaActivity.this);
				requestFormaAvaliacao.setTitle("Escolha uma forma de avaliação");
				requestFormaAvaliacao.setItems(hipoteses, 
											   new DialogInterface.OnClickListener(){
											   		public void onClick(DialogInterface dialog, int index){
											   			Intent intent = null;
											   			Bundle bundle = new Bundle();
														bundle.putString("NomeDisciplina", nomeDisciplina);
											   			
														if (hipoteses[index].equals("Teste/ Exame Teórico"))
											   				intent = new Intent(getApplicationContext(), TesteTeoricoActivity.class);
														
														if (hipoteses[index].equals("Trabalhos/ Projectos/ Relatórios"))
															intent = new Intent(getApplicationContext(), TrabalhoActivity.class);
														
														if (hipoteses[index].equals("Teste/ Exame Prático"))
															intent = new Intent(getApplicationContext(), TestePraticoActivity.class);
														
														if(hipoteses[index].equals("Mini Testes"))
															intent = new Intent(getApplicationContext(), MiniTesteActivity.class);
														
														if(hipoteses[index].equals("Bónus"))
															intent = new Intent(getApplicationContext(), BonusActivity.class);
														
														if(hipoteses[index].equals("Fichas/ Exercicios"))
															intent = new Intent(getApplicationContext(), FichaActivity.class);
											   			
														intent.putExtras(bundle);
											   			startActivity(intent);
											   		}
												});
				AlertDialog requestAvaliacaoAlert = requestFormaAvaliacao.create();
				requestAvaliacaoAlert.show();
			}
		});
		
		numeroTestes = testeTeoricoDataSource.getNumeroTestesValidosByName(nomeDisciplina);
		numeroTrabalhos = trabalhoDataSource.getNumeroTrabalhosValidosByName(nomeDisciplina);
		numeroTestesPraticos = testePraticoDataSource.getNumeroTestesValidosByName(nomeDisciplina);
		numeroMiniTestes = miniTesteDataSource.getNumeroTestesValidosByName(nomeDisciplina);
		numeroBonus = bonusDataSource.getNumeroBonus(nomeDisciplina);
		numeroFichas = fichaDataSource.getNumeroFichasValidosByName(nomeDisciplina);
		
		final Button removeAvaliacaoButton = (Button) findViewById(R.id.removerAvaliacao);
		removeAvaliacaoButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				List<String> listItems = new ArrayList<String>();
				if(numeroTestes>0)
					listItems.add("Teste/ Exame Teórico");
				if(numeroTrabalhos>0)
					listItems.add("Trabalhos/ Projectos/ Relatórios");
				if(numeroTestesPraticos>0)
					listItems.add("Teste/ Exame Prático");
				if(numeroMiniTestes>0)
					listItems.add("Mini Testes");
				if(numeroBonus>0)
					listItems.add("Bónus");
				if(numeroFichas>0)
					listItems.add("Fichas/ Exercicios");
				
				final CharSequence[] hipoteses = listItems.toArray(new CharSequence[listItems.size()]);
								
				AlertDialog.Builder requestFormaAvaliacao = new AlertDialog.Builder(DisciplinaActivity.this);
				requestFormaAvaliacao.setTitle("Remova uma forma de avaliação");
				requestFormaAvaliacao.setItems(hipoteses, 
											   new DialogInterface.OnClickListener(){
											   		public void onClick(DialogInterface dialog, int index){
											   			flagAlteracaoNumeros = 1;
											   			Intent intent = null;
											   			Bundle bundle = new Bundle();
														bundle.putString("NomeDisciplina", nomeDisciplina);
											   			
														if (hipoteses[index].equals("Teste/ Exame Teórico"))
											   				testeTeoricoDataSource.removeTesteTeorico(nomeDisciplina);
														
														if (hipoteses[index].equals("Trabalhos/ Projectos/ Relatórios"))
															trabalhoDataSource.removeTrabalho(nomeDisciplina);
														
														if (hipoteses[index].equals("Teste/ Exame Prático"))
															testePraticoDataSource.removeTestePratico(nomeDisciplina);
														
														if(hipoteses[index].equals("Mini Testes"))
															miniTesteDataSource.removeMiniTeste(nomeDisciplina);
														
														if(hipoteses[index].equals("Bónus"))
															bonusDataSource.removeBonus(nomeDisciplina);
														
														if(hipoteses[index].equals("Fichas/ Exercicios"))
															fichaDataSource.removeFicha(nomeDisciplina);
														
														Intent intent2 = new Intent(getBaseContext(), DisciplinaActivity.class);
														Bundle bundle2 = new Bundle();
														bundle2.putString("NomeDisciplina", nomeDisciplina);
														intent2.putExtras(bundle);
														startActivity(intent2);
														
											   		}
												});
				AlertDialog requestAvaliacaoAlert = requestFormaAvaliacao.create();
				requestAvaliacaoAlert.show();
			}
		});
		
		if(flagAlteracaoNumeros==1){
			numeroTestes = testeTeoricoDataSource.getNumeroTestesValidosByName(nomeDisciplina);
			numeroTrabalhos = trabalhoDataSource.getNumeroTrabalhosValidosByName(nomeDisciplina);
			numeroTestesPraticos = testePraticoDataSource.getNumeroTestesValidosByName(nomeDisciplina);
			numeroMiniTestes = miniTesteDataSource.getNumeroTestesValidosByName(nomeDisciplina);
			numeroBonus = bonusDataSource.getNumeroBonus(nomeDisciplina);
			numeroFichas = fichaDataSource.getNumeroFichasValidosByName(nomeDisciplina);
			flagAlteracaoNumeros = 0;
		}
		
		List<String> testesTeoricosStr = devolveStringsParaTestesTeoricosListView(numeroTestes);
		List<String> trabalhosStr = devolveStringsParaTrabalhosListView(numeroTrabalhos);
		List<String> testesPraticosStr = devolveStringsParaTestesPraticosListView(numeroTestesPraticos);
		List<String> miniTestesStr = devolveStringsParaMiniTestesListView(numeroMiniTestes);
		List<String> bonusStr = devolveStringsParaBonusListView(numeroBonus);
		List<String> fichasStr = devolveStringsParaFichasListView(numeroFichas);
		
		List<String> avaliacoesStr = new ArrayList<String>();
		avaliacoesStr.addAll(testesTeoricosStr);
		avaliacoesStr.addAll(trabalhosStr);
		avaliacoesStr.addAll(testesPraticosStr);
		avaliacoesStr.addAll(miniTestesStr);
		avaliacoesStr.addAll(bonusStr);
		avaliacoesStr.addAll(fichasStr);
		
		final ArrayAdapter<String> adapterLista = new ArrayAdapter<String>(this,
																android.R.layout.simple_list_item_1, 
																avaliacoesStr);
		
		listViewAvaliacoes = (ListView) findViewById(R.id.avaliacoes);
		listViewAvaliacoes.setAdapter(adapterLista);
		
		listViewAvaliacoes.setOnScrollListener(new OnScrollListener(){
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int fim = view.getLastVisiblePosition();
				for(int i = firstVisibleItem; i < visibleItemCount; i++){
					View v = view.getChildAt(i);
					String str = splitByIgualDireita(adapterLista.getItem(fim-visibleItemCount+1+i));
					if(str.contains("valores")){
						String[] partes = str.split("valores");
						str = partes[0];
					}
					float num = Float.parseFloat(str);
					if(num >= 9.5)
						v.setBackgroundColor(Color.parseColor("#00FF22"));
					else
						v.setBackgroundColor(Color.parseColor("#FF0022"));
				}
			}
		});
		
		listViewAvaliacoes.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				
				final int positionFinal = position;
				
				if (adapterLista.getItem(position).contains("Média"))
					return;
				
				AlertDialog.Builder requestNota = new AlertDialog.Builder(DisciplinaActivity.this);
				if(adapterLista.getItem(position).contains("Bónus"))
					requestNota.setMessage("Bónus");
				else
					requestNota.setMessage("Nota " + splitByIgual(adapterLista.getItem(position)));
				
				final EditText inputNotaText = new EditText(getBaseContext());
				inputNotaText.setFilters(new InputFilter[]{ new InputFilterMaxMinFloat("0.0", "20.0")});
				inputNotaText.setTextColor(Color.BLACK);
				inputNotaText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
				
				requestNota.setView(inputNotaText);
				
				requestNota.setCancelable(false);
				
				requestNota.setPositiveButton("Gravar", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						String inputNotaStr = inputNotaText.getText().toString();
						if(inputNotaStr.contains(",")){
							String[] partes = inputNotaStr.split(",");
							inputNotaStr = partes[0] + "." + partes[1];
						}
						float inputNotaFloat = Float.parseFloat(inputNotaStr);
						String nomeAvaliacao = adapterLista.getItem(positionFinal);
						gravaNota(nomeAvaliacao, inputNotaFloat, numeroTestes);
							
						float nota = 0;
						if(nomeAvaliacao.contains("Teste") && !nomeAvaliacao.contains("Prático") && !nomeAvaliacao.contains("Mini"))
							nota = getNotaTesteTeorico(nomeAvaliacao);
						if(nomeAvaliacao.contains("Trabalho"))
							nota = getNotaTrabalho(nomeAvaliacao);
						if(nomeAvaliacao.contains("Prático"))
							nota = getNotaTestePratico(nomeAvaliacao); 
						if(nomeAvaliacao.contains("Mini"))
							nota = getNotaMiniTeste(nomeAvaliacao);
						if(nomeAvaliacao.contains("Bónus"))
							nota = getBonus();
						if(nomeAvaliacao.contains("Ficha"))
							nota = getNotaFicha(nomeAvaliacao);
							
						
						adapterLista.remove(nomeAvaliacao);
						if (nomeAvaliacao.contains("=")){
							String str = splitByIgual(nomeAvaliacao);
							if(str.contains("Bónus")){
								str = str + " = " + nota + " valores";
							}
							else
								str = str + " = " + nota;
							adapterLista.insert(str, positionFinal);
						}
						else
							adapterLista.insert(nomeAvaliacao + " = " + nota, positionFinal);
						
						for (int i=0; i<adapterLista.getCount(); i++){
							if(adapterLista.getItem(i).contains("Média de Testes") && 
									!adapterLista.getItem(i).contains("Práticos") && !adapterLista.getItem(i).contains("Mini")){
								adapterLista.remove(adapterLista.getItem(i));
								float mediaTestes = getMediaTestes();
								adapterLista.insert("Média de Testes = " + mediaTestes, i);
							}
							if(adapterLista.getItem(i).contains("Média de Trabalhos")){
								adapterLista.remove(adapterLista.getItem(i));
								float mediaTrabalhos = getMediaTrabalhos();
								adapterLista.insert("Média de Trabalhos = " + mediaTrabalhos, i);
							}
							if(adapterLista.getItem(i).contains("Média de Testes Práticos")){
								adapterLista.remove(adapterLista.getItem(i));
								float mediaTestesPraticos = getMediaTestesPraticos();
								adapterLista.insert("Média de Testes Práticos = " + mediaTestesPraticos, i);
							}
							if(adapterLista.getItem(i).contains("Média de Mini Testes")){
								adapterLista.remove(adapterLista.getItem(i));
								float mediaMiniTestes = getMediaMiniTestes();
								adapterLista.insert("Média de Mini Testes = " + mediaMiniTestes, i);
							}
							if(adapterLista.getItem(i).contains("Média de Fichas")){
								adapterLista.remove(adapterLista.getItem(i));
								float mediaFichas = getMediaFichas();
								adapterLista.insert("Média de Fichas/Exercicios = " + mediaFichas, i);
							}
							
						}
						
						alteraMediaGraficamente();
					}
				});
				
				requestNota.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				
				AlertDialog requestPercAlert = requestNota.create();
				requestPercAlert.show();
				requestPercAlert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			}
		});
		
		alteraMediaGraficamente();
		
		
	}
	
	public void retiraExtrasIntent(){
		Bundle bundle = getIntent().getExtras();
		if(bundle.size()==3){
			ano = bundle.getInt("Ano");
			sem = bundle.getInt("Sem");
		}
		nomeDisciplina = bundle.getString("NomeDisciplina");
	}
	
	public void gravaNovosCreditos(float inputCreditosInt){
		disciplinaDataSource.changeCreditosByName(inputCreditosInt, nomeDisciplina);
		creditos = inputCreditosInt;
	}
	
	public List<String> devolveStringsParaTestesTeoricosListView(int numeroTestes){
		List<String> testesStr = new ArrayList<String>();
		float nota = 0;
		float media = 0;
		for (int i=1; i<=numeroTestes; i++){
			nota = getNotaTesteTeorico("Teste "+i);
			testesStr.add("Teste "+i + " = " + nota);			
		}
		if (numeroTestes >= 2){
			media = getMediaTestes();
			testesStr.add("Média de Testes = " + media);
		}
		return testesStr;
	}
	
	public List<String> devolveStringsParaTrabalhosListView (int numeroTrabalhos){
		List<String> trabalhosStr = new ArrayList<String>();
		float nota = 0;
		float media = 0;
		for (int i=1; i<=numeroTrabalhos; i++){
			nota = getNotaTrabalho("Trabalho "+i);
			trabalhosStr.add("Trabalho "+i + " = " + nota);
		}
		if(numeroTrabalhos >= 2){
			media = getMediaTrabalhos();
			trabalhosStr.add("Média de Trabalhos = " + media);
		}
		return trabalhosStr;
	}
	
	public List<String> devolveStringsParaTestesPraticosListView (int numeroTestesPraticos){
		List<String> testesPraticosStr = new ArrayList<String>();
		float nota = 0;
		float media = 0;
		for (int i=1; i<=numeroTestesPraticos; i++){
			nota = getNotaTestePratico("Teste Prático "+i);
			testesPraticosStr.add("Teste Prático "+i + " = " + nota);
		}
		if(numeroTestesPraticos >= 2){
			media = getMediaTestesPraticos();
			testesPraticosStr.add("Média de Testes Práticos = " + media);
		}
		return testesPraticosStr;
	}
	
	public List<String> devolveStringsParaMiniTestesListView (int numeroMiniTestes){
		List<String> miniTestesStr = new ArrayList<String>();
		float nota = 0;
		float media = 0 ;
		for (int i=1; i<=numeroMiniTestes; i++){
			nota = getNotaMiniTeste("Mini Teste "+i);
			miniTestesStr.add("Mini Teste "+i + " = " + nota);
		}
		if(numeroMiniTestes >= 2){
			media = getMediaMiniTestes();
			miniTestesStr.add("Média de Mini Testes = " + media);
		}
		return miniTestesStr;
	}
	
	public List<String> devolveStringsParaBonusListView(int numeroBonus){
		List<String> bonusStr = new ArrayList<String>();
		float bonus;
		if(numeroBonus > 0){
			bonus = getBonus();
			bonusStr.add("Bónus = " + bonus + " valores");
		}
		return bonusStr;
	}
	
	public List<String> devolveStringsParaFichasListView(int numeroFichas){
		List<String> fichasStr = new ArrayList<String>();
		float nota = 0;
		float media = 0;
		for (int i=1; i<=numeroFichas; i++){
			nota = getNotaFicha("Ficha/Exercicio "+i);
			fichasStr.add("Ficha/Exercicio "+i + " = " + nota);
		}
		if(numeroFichas >= 2){
			media = getMediaFichas();
			fichasStr.add("Média de Fichas/Exercicios = " + media);
		}
		return fichasStr;
	}
	
	public void gravaNota(String nomeAvaliacao, float inputNota, int numAvaliacoes){
		if (nomeAvaliacao.contains("Teste") && !nomeAvaliacao.contains("Prático") && !nomeAvaliacao.contains("Mini")){
			testeTeoricoDataSource.gravaNota(nomeDisciplina, nomeAvaliacao, inputNota, numAvaliacoes);
		}
		if (nomeAvaliacao.contains("Trabalho")){
			trabalhoDataSource.gravaNota(nomeDisciplina, nomeAvaliacao, inputNota, numAvaliacoes);
		}
		if (nomeAvaliacao.contains("Prático")){
			testePraticoDataSource.gravaNota(nomeDisciplina, nomeAvaliacao, inputNota, numAvaliacoes);
		}
		if (nomeAvaliacao.contains("Mini")){
			miniTesteDataSource.gravaNota(nomeDisciplina, nomeAvaliacao, inputNota, numAvaliacoes);
		}
		if (nomeAvaliacao.contains("Bónus")){
			bonusDataSource.gravaBonus(nomeDisciplina, "Valor", 0, inputNota);
		}
		if (nomeAvaliacao.contains("Ficha")){
			fichaDataSource.gravaNota(nomeDisciplina, nomeAvaliacao, inputNota, numAvaliacoes);
		}
	}
	
	public float getNotaTesteTeorico(String nomeAvaliacao){
		float nota = testeTeoricoDataSource.getNota(nomeDisciplina, nomeAvaliacao);
		return nota; 
	}
	
	public float getNotaTrabalho(String nomeAvaliacao){
		float nota = trabalhoDataSource.getNota(nomeDisciplina, nomeAvaliacao);
		return nota;
	}
	
	public float getNotaTestePratico(String nomeAvaliacao){
		float nota = testePraticoDataSource.getNota(nomeDisciplina, nomeAvaliacao);
		return nota;
	}
	
	public float getNotaMiniTeste(String nomeAvaliacao){
		float nota = miniTesteDataSource.getNota(nomeDisciplina, nomeAvaliacao);
		return nota;
	}
	
	public float getBonus(){
		String[] bonusStr = bonusDataSource.getBonus(nomeDisciplina);
		float bonus = Float.parseFloat(bonusStr[0]);
		return bonus;
	}
	
	public float getNotaFicha(String nomeAvaliacao){
		float nota = fichaDataSource.getNota(nomeDisciplina, nomeAvaliacao);
		return nota;
	}
	
	public float getMediaTestes(){
		float media = testeTeoricoDataSource.getMedia(nomeDisciplina);
		return media;
	}
	
	public float getMediaTrabalhos(){
		float media = trabalhoDataSource.getMedia(nomeDisciplina);
		return media;
	}
	
	public float getMediaTestesPraticos(){
		float media = testePraticoDataSource.getMedia(nomeDisciplina);
		return media;
	}
	
	public float getMediaMiniTestes(){
		float media = miniTesteDataSource.getMedia(nomeDisciplina);
		return media;
	}
	
	public float getMediaFichas(){
		float media = fichaDataSource.getMedia(nomeDisciplina);
		return media;
	}
	
	public String splitByIgual(String str){
		String[] partes = str.split("=");
		return partes[0];
	}
	
	public String splitByIgualDireita (String str){
		String[] partes = str.split("=");
//		if (partes[0].equals("Bónus")){
//			return "999"; //APENAS PARA FICAR VERDE
//		}
		return partes[1];
	}
	
	public boolean checkMaisDe10(String nomeAvaliacao, float nota){
		if (nota >= 9.5 || nomeAvaliacao.contains("Bónus")){
			return true;
		}
		else
			return false;
	}
	
	public int[] getPercentagens(){
		int[] percentagens = new int[5];
		int i = 0;
		while(true){
			int percTesteTeorico = testeTeoricoDataSource.getPercentagem(nomeDisciplina);
			if(percTesteTeorico!=0){
				percentagens[i]=percTesteTeorico;
				i++;
			}
			int percTrabalho = trabalhoDataSource.getPercentagem(nomeDisciplina);
			if(percTrabalho!=0){
				percentagens[i]=percTrabalho;
				i++;
			}
			int percTestePratico = testePraticoDataSource.getPercentagem(nomeDisciplina);
			if(percTestePratico!=0){
				percentagens[i]=percTestePratico;
				i++;
			}
			
			int percMiniTeste = miniTesteDataSource.getPercentagem(nomeDisciplina);
			if(percMiniTeste!=0){
				percentagens[i]=percMiniTeste;
				i++;
			}
			
			int percFicha = fichaDataSource.getPercentagem(nomeDisciplina);
			if(percFicha!=0){
				percentagens[i]=percFicha;
			}
			break;
		}
		return percentagens;
	}
	
	public float calculaMedia(){
		float media = 0;
		
		float testeTeorico = testeTeoricoDataSource.getMedia(nomeDisciplina);
		float percTT = testeTeoricoDataSource.getPercentagem(nomeDisciplina);
		
		float trabalho = trabalhoDataSource.getMedia(nomeDisciplina);
		float percTrab = trabalhoDataSource.getPercentagem(nomeDisciplina);
		
		float testePratico = testePraticoDataSource.getMedia(nomeDisciplina);
		float percTP = testePraticoDataSource.getPercentagem(nomeDisciplina);
		
		float miniTeste = miniTesteDataSource.getMedia(nomeDisciplina);
		float percMT = miniTesteDataSource.getPercentagem(nomeDisciplina);
		
		float ficha = fichaDataSource.getMedia(nomeDisciplina);
		float percF = fichaDataSource.getPercentagem(nomeDisciplina);
		
		media = testeTeorico * percTT 
				+ trabalho * percTrab
				+ testePratico * percTP
				+ miniTeste * percMT
				+ ficha * percF;
		
		media = media/(float) 100;
		if(media > 20){
			media = (float) 20;
		}
		
		return media;
	}
	
	public void alteraMediaGraficamente(){
		final TextView mediaDisciplinaText = (TextView) findViewById(R.id.mediaDisciplina);
		//nomeDisciplinaText.setText(nomeDisciplina);
		int[] percentagens = getPercentagens();
		int total = 0;
		if (percentagens != null){
			int contador = 0;
			for(int i : percentagens){
				total = total + percentagens[contador];
				contador = contador+1;
			}
		}
		if (total == 100){
			float media = calculaMedia();
			String[] bonusStr2 = bonusDataSource.getBonus(nomeDisciplina);
			float bonus = 0;
			if (bonusStr2 !=null)
				bonus = Float.parseFloat(bonusStr2[0]);
			media = media + bonus;
			if(media > 20){
				media = (float) 20;
			}
			int mediaInt = Math.round(media);
			if(media >= 9.5)
				mediaDisciplinaText.setTextColor(Color.parseColor("#00FF44"));
			if(media < 9.5)
				mediaDisciplinaText.setTextColor(Color.parseColor("#FF0044"));
			
			String resultado = "Média = " + media + " (" + mediaInt + ")";
			SpannableString spanString = new SpannableString(resultado);
			spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
			mediaDisciplinaText.setText(spanString);
			mediaDisciplinaText.setTextSize(18);
			return;
		}
		
		mediaDisciplinaText.setTextSize(10);
		if (total == 0)
			mediaDisciplinaText.setText("! Não está inserida qualquer forma de avaliação da cadeira '" + nomeDisciplina + "'");
		if (total < 100 && total>0)
			mediaDisciplinaText.setText("! Apenas " + total + "% da avaliação da cadeira '" + nomeDisciplina + "' está inserida");
		if (total > 100)
			mediaDisciplinaText.setText("! Ultrapassou os 100% nos critérios de avaliação!" ); 
	}
	
	@Override
	public void onBackPressed() {
	    finish();
	    Intent intent = new Intent();
	    if(ano == 0 || sem == 0)
	    	intent = new Intent(getBaseContext(), ListaDisciplinasActivity.class);
	    else{
	    	intent = new Intent(getBaseContext(), MenuDisciplinasActivity.class);
	    	Bundle bundle = new Bundle();
			bundle.putInt("Ano", ano);
			bundle.putInt("Sem", sem);
			intent.putExtras(bundle);
	    }
		startActivity(intent);
	}
}
