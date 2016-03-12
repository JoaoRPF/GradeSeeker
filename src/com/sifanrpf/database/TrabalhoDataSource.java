package com.sifanrpf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sifanrpf.gradeseeker.TesteTeorico;
import com.sifanrpf.gradeseeker.Trabalho;

public class TrabalhoDataSource {
	private SQLiteDatabase database;
	private TrabalhoSQLiteHelper dbHelper;
	
	
	public TrabalhoDataSource(Context context){
		dbHelper = new TrabalhoSQLiteHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public Trabalho createTrabalho(String nome, int quantidade, int perc){
		ContentValues values = new ContentValues();
		values.put(TrabalhoSQLiteHelper.COLUNA_NOME, nome);
		values.put(TrabalhoSQLiteHelper.COLUNA_QUANTIDADE, quantidade);
		values.put(TrabalhoSQLiteHelper.COLUNA_PERC_TOTAL, perc);
		values.put(TrabalhoSQLiteHelper.COLUNA_MEDIA, 0);
		
		int criado = database.update(TrabalhoSQLiteHelper.TABLE_TRABALHO_TEORICO, 
				values, 
				TrabalhoSQLiteHelper.COLUNA_NOME + " = '" + nome + "'", null);
		
		if(criado != 0)
			database.delete(TrabalhoSQLiteHelper.TABLE_TRABALHO_TEORICO, 
						TrabalhoSQLiteHelper.COLUNA_NOME + " = '" + nome + "'", null);
		
		long insertId = database.insert(TrabalhoSQLiteHelper.TABLE_TRABALHO_TEORICO, null, values);
		
		Cursor cursor = database.query(TrabalhoSQLiteHelper.TABLE_TRABALHO_TEORICO, 
									null,
									TrabalhoSQLiteHelper.COLUNA_ID + " = " + insertId, null,null,null,null);
		cursor.moveToFirst();
		Trabalho newTrabalho = cursorToTrabalho(cursor);
		cursor.close();
		return newTrabalho;
	}
	
	public Trabalho cursorToTrabalho(Cursor cursor){
		Trabalho trabalho = new Trabalho();
		trabalho.setId(cursor.getLong(0));
		trabalho.setQuantidade(cursor.getInt(2));
		trabalho.setTrabalho1(cursor.getFloat(3));
		trabalho.setTrabalho2(cursor.getFloat(4));
		trabalho.setTrabalho3(cursor.getFloat(5));
		trabalho.setTrabalho4(cursor.getFloat(6));
		trabalho.setTrabalho5(cursor.getFloat(7));
		trabalho.setTrabalho6(cursor.getFloat(8));
		trabalho.setTrabalho7(cursor.getFloat(9));
		trabalho.setTrabalho8(cursor.getFloat(10));
		trabalho.setTrabalho9(cursor.getFloat(11));
		trabalho.setTrabalho1_perc(cursor.getFloat(12));
		trabalho.setTrabalho2_perc(cursor.getFloat(13));
		trabalho.setTrabalho3_perc(cursor.getFloat(14));
		trabalho.setTrabalho4_perc(cursor.getFloat(15));
		trabalho.setTrabalho5_perc(cursor.getFloat(16));
		trabalho.setTrabalho6_perc(cursor.getFloat(17));
		trabalho.setTrabalho7_perc(cursor.getFloat(18));
		trabalho.setTrabalho8_perc(cursor.getFloat(19));
		trabalho.setTrabalho9_perc(cursor.getFloat(20));
		trabalho.setPercentagem_total(cursor.getInt(21));
		trabalho.setMedia(cursor.getFloat(22));
		return trabalho;
	}
	
	public void gravaCotacaoTrabalhos(String nomeDisciplina, int numTrabalhos, float[] cotacoes){
		ContentValues cv = new ContentValues();
		for(int i=1; i<=numTrabalhos; i++){
			if (i==1){
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO1_PERC, cotacoes[i-1]);
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO1, 0);
			}
			if (i==2){
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO2_PERC, cotacoes[i-1]);
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO2, 0);
			}
			if (i==3){
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO3_PERC, cotacoes[i-1]);
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO3, 0);
			}
			if (i==4){
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO4_PERC, cotacoes[i-1]);
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO4, 0);
			}
			if (i==5){
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO5_PERC, cotacoes[i-1]);
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO5, 0);
			}
			if (i==6){
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO6_PERC, cotacoes[i-1]);
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO6, 0);
			}
			if (i==7){
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO7_PERC, cotacoes[i-1]);
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO7, 0);
			}
			if (i==8){
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO8_PERC, cotacoes[i-1]);
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO8, 0);
			}
			if (i==9){
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO9_PERC, cotacoes[i-1]);
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO9, 0);
			}
		}
		
		database.update(TrabalhoSQLiteHelper.TABLE_TRABALHO_TEORICO, 
						cv, 
						TrabalhoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
		
		alteraMedia(nomeDisciplina, numTrabalhos);
	}
	
	public void alteraCotacaoTrabalho(String nomeDisciplina, int numTrabalhos, float[] cotacoes){
		ContentValues cv = new ContentValues();
		for(int i=1; i<=numTrabalhos; i++){
			if (i==1)
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO1_PERC, cotacoes[i]);
			if (i==2)
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO2_PERC, cotacoes[i]);
			if (i==3)
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO3_PERC, cotacoes[i]);
			if (i==4)
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO4_PERC, cotacoes[i]);
			if (i==5)
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO5_PERC, cotacoes[i]);
			if (i==6)
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO6_PERC, cotacoes[i]);
			if (i==7)
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO7_PERC, cotacoes[i]);
			if (i==8)
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO8_PERC, cotacoes[i]);
			if (i==9)
				cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO9_PERC, cotacoes[i]);
		}
		
		database.update(TrabalhoSQLiteHelper.TABLE_TRABALHO_TEORICO, 
				cv, 
				TrabalhoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
		
		alteraMedia(nomeDisciplina, numTrabalhos);
		
	}
	
	public int getNumeroTrabalhosValidosByName(String nomeDisciplina){
		Cursor cursor = database.query(TrabalhoSQLiteHelper.TABLE_TRABALHO_TEORICO, 
									null, (TrabalhoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);
		if (cursor.isAfterLast())
				return 0;
		cursor.moveToFirst();
		Trabalho trabalho = cursorToTrabalho(cursor);
		int numeroTrabalhos = trabalho.getQuantidade();
		return numeroTrabalhos;	
	}
	
	public void gravaNota(String nomeDisciplina, String nomeAvaliacao, float inputNota, int numTrabalhos){
		ContentValues cv = new ContentValues();
		if(nomeAvaliacao.contains("o 1"))
			cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO1, inputNota);
		if(nomeAvaliacao.contains("o 2"))
			cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO2, inputNota);
		if(nomeAvaliacao.contains("o 3"))
			cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO3, inputNota);
		if(nomeAvaliacao.contains("o 4"))
			cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO4, inputNota);
		if(nomeAvaliacao.contains("o 5"))
			cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO5, inputNota);
		if(nomeAvaliacao.contains("o 6"))
			cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO6, inputNota);
		if(nomeAvaliacao.contains("o 7"))
			cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO7, inputNota);
		if(nomeAvaliacao.contains("o 8"))
			cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO8, inputNota);
		if(nomeAvaliacao.contains("o 9"))
			cv.put(TrabalhoSQLiteHelper.COLUNA_TRABALHO9, inputNota);
		
		database.update(TrabalhoSQLiteHelper.TABLE_TRABALHO_TEORICO, 
				cv, 
				TrabalhoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
		
		alteraMedia(nomeDisciplina, numTrabalhos);
	}
	
	public void alteraMedia(String nomeDisciplina, int numeroTrabalhos){
		ContentValues cv = new ContentValues();
		Cursor cursor = database.query(TrabalhoSQLiteHelper.TABLE_TRABALHO_TEORICO, 
							null, (TrabalhoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);
		cursor.moveToFirst();
		Trabalho trabalho = cursorToTrabalho(cursor);
		float media = 0;
		try{
			media = trabalho.getTrabalho1_perc() * trabalho.getTrabalho1();
			
			media += trabalho.getTrabalho2_perc() * trabalho.getTrabalho2();
			
			media += trabalho.getTrabalho3_perc() * trabalho.getTrabalho3();
			
			media += trabalho.getTrabalho4_perc() * trabalho.getTrabalho4();
			
			media += trabalho.getTrabalho5_perc() * trabalho.getTrabalho5();
			
			media += trabalho.getTrabalho6_perc() * trabalho.getTrabalho6();
			
			media += trabalho.getTrabalho7_perc() * trabalho.getTrabalho7();
			
			media += trabalho.getTrabalho8_perc() * trabalho.getTrabalho8();
			
			media += trabalho.getTrabalho9_perc() * trabalho.getTrabalho9();
		}
		catch(Exception e){
			
		}
		
		media = media / (float) 100;
		
		cv.put(TrabalhoSQLiteHelper.COLUNA_MEDIA, media);
		
		database.update(TrabalhoSQLiteHelper.TABLE_TRABALHO_TEORICO, 
				cv, 
				TrabalhoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
		
	}
	
	public float getNota(String nomeDisciplina, String nomeAvaliacao){
		float nota = 0;
		Cursor cursor = database.query(TrabalhoSQLiteHelper.TABLE_TRABALHO_TEORICO, 
							null, (TrabalhoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);
		if (cursor.isAfterLast())
			return 0;
		cursor.moveToFirst();
		Trabalho trabalho = cursorToTrabalho(cursor);
		
		if(nomeAvaliacao.contains("o 1"))
			nota = trabalho.getTrabalho1();
		if(nomeAvaliacao.contains("o 2"))
			nota = trabalho.getTrabalho2();
		if(nomeAvaliacao.contains("o 3"))
			nota = trabalho.getTrabalho3();
		if(nomeAvaliacao.contains("o 4"))
			nota = trabalho.getTrabalho4();
		if(nomeAvaliacao.contains("o 5"))
			nota = trabalho.getTrabalho5();
		if(nomeAvaliacao.contains("o 6"))
			nota = trabalho.getTrabalho6();
		if(nomeAvaliacao.contains("o 7"))
			nota = trabalho.getTrabalho7();
		if(nomeAvaliacao.contains("o 8"))
			nota = trabalho.getTrabalho8();
		if(nomeAvaliacao.contains("o 9"))
			nota = trabalho.getTrabalho9();
		
		return nota;
	}

	public float getMedia(String nomeDisciplina) {
		float media = 0;
		Cursor cursor = database.query(TrabalhoSQLiteHelper.TABLE_TRABALHO_TEORICO, 
								null, (TrabalhoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);
		
		if (cursor.isAfterLast())
			return 0;
		cursor.moveToFirst();
		Trabalho trabalho = cursorToTrabalho(cursor);
		media = trabalho.getMedia();
		
		return media;
	}
	
	public int getPercentagem(String nomeDisciplina){
		int perc = 0;
		Cursor cursor = database.query(TrabalhoSQLiteHelper.TABLE_TRABALHO_TEORICO, 
								null, (TrabalhoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);

		if (cursor.isAfterLast())
			return 0;
		cursor.moveToFirst();
		Trabalho teste = cursorToTrabalho(cursor);
		perc = teste.getPercentagem_total();
	
		return perc;	
	}
	
	public void removeTrabalho(String nomeDisciplina){
		database.delete(TrabalhoSQLiteHelper.TABLE_TRABALHO_TEORICO, 
						TrabalhoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
	}
}
