package com.sifanrpf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sifanrpf.gradeseeker.Ficha;
import com.sifanrpf.gradeseeker.TesteTeorico;

public class FichaDataSource {
	private SQLiteDatabase database;
	private FichaSQLiteHelper dbHelper;
	
	
	public FichaDataSource(Context context){
		dbHelper = new FichaSQLiteHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public Ficha createFicha(String nome, int quantidade, int perc){
		ContentValues values = new ContentValues();
		values.put(FichaSQLiteHelper.COLUNA_NOME, nome);
		values.put(FichaSQLiteHelper.COLUNA_QUANTIDADE, quantidade);
		values.put(FichaSQLiteHelper.COLUNA_PERC_TOTAL, perc);
		values.put(FichaSQLiteHelper.COLUNA_MEDIA, 0);
		
		int criado = database.update(FichaSQLiteHelper.TABLE_FICHA_TEORICO, 
				values, 
				FichaSQLiteHelper.COLUNA_NOME + " = '" + nome + "'", null);
		
		if(criado != 0)
			database.delete(FichaSQLiteHelper.TABLE_FICHA_TEORICO, 
						FichaSQLiteHelper.COLUNA_NOME + " = '" + nome + "'", null);
		
		long insertId = database.insert(FichaSQLiteHelper.TABLE_FICHA_TEORICO, null, values);
		
		Cursor cursor = database.query(FichaSQLiteHelper.TABLE_FICHA_TEORICO, 
									null,
									FichaSQLiteHelper.COLUNA_ID + " = " + insertId, null,null,null,null);
		cursor.moveToFirst();
		Ficha newFicha = cursorToFicha(cursor);
		cursor.close();
		return newFicha;
	}
	
	public Ficha cursorToFicha(Cursor cursor){
		Ficha ficha = new Ficha();
		ficha.setId(cursor.getLong(0));
		ficha.setQuantidade(cursor.getInt(2));
		ficha.setFicha1(cursor.getFloat(3));
		ficha.setFicha2(cursor.getFloat(4));
		ficha.setFicha3(cursor.getFloat(5));
		ficha.setFicha4(cursor.getFloat(6));
		ficha.setFicha5(cursor.getFloat(7));
		ficha.setFicha6(cursor.getFloat(8));
		ficha.setFicha7(cursor.getFloat(9));
		ficha.setFicha8(cursor.getFloat(10));
		ficha.setFicha9(cursor.getFloat(11));
		ficha.setFicha1_perc(cursor.getFloat(12));
		ficha.setFicha2_perc(cursor.getFloat(13));
		ficha.setFicha3_perc(cursor.getFloat(14));
		ficha.setFicha4_perc(cursor.getFloat(15));
		ficha.setFicha5_perc(cursor.getFloat(16));
		ficha.setFicha6_perc(cursor.getFloat(17));
		ficha.setFicha7_perc(cursor.getFloat(18));
		ficha.setFicha8_perc(cursor.getFloat(19));
		ficha.setFicha9_perc(cursor.getFloat(20));
		ficha.setPercentagem_total(cursor.getInt(21));
		ficha.setMedia(cursor.getFloat(22));
		return ficha;
	}
	
	public void gravaCotacaoFichas(String nomeDisciplina, int numFichas, float[] cotacoes){
		ContentValues cv = new ContentValues();
		for(int i=1; i<=numFichas; i++){
			if (i==1){
				cv.put(FichaSQLiteHelper.COLUNA_FICHA1_PERC, cotacoes[i-1]);
				cv.put(FichaSQLiteHelper.COLUNA_FICHA1, 0);
			}
			if (i==2){
				cv.put(FichaSQLiteHelper.COLUNA_FICHA2_PERC, cotacoes[i-1]);
				cv.put(FichaSQLiteHelper.COLUNA_FICHA2, 0);
			}
			if (i==3){
				cv.put(FichaSQLiteHelper.COLUNA_FICHA3_PERC, cotacoes[i-1]);
				cv.put(FichaSQLiteHelper.COLUNA_FICHA3, 0);
			}
			if (i==4){
				cv.put(FichaSQLiteHelper.COLUNA_FICHA4_PERC, cotacoes[i-1]);
				cv.put(FichaSQLiteHelper.COLUNA_FICHA4, 0);
			}
			if (i==5){
				cv.put(FichaSQLiteHelper.COLUNA_FICHA5_PERC, cotacoes[i-1]);
				cv.put(FichaSQLiteHelper.COLUNA_FICHA5, 0);
			}
			if (i==6){
				cv.put(FichaSQLiteHelper.COLUNA_FICHA6_PERC, cotacoes[i-1]);
				cv.put(FichaSQLiteHelper.COLUNA_FICHA6, 0);
			}
			if (i==7){
				cv.put(FichaSQLiteHelper.COLUNA_FICHA7_PERC, cotacoes[i-1]);
				cv.put(FichaSQLiteHelper.COLUNA_FICHA7, 0);
			}
			if (i==8){
				cv.put(FichaSQLiteHelper.COLUNA_FICHA8_PERC, cotacoes[i-1]);
				cv.put(FichaSQLiteHelper.COLUNA_FICHA8, 0);
			}
			if (i==9){
				cv.put(FichaSQLiteHelper.COLUNA_FICHA9_PERC, cotacoes[i-1]);
				cv.put(FichaSQLiteHelper.COLUNA_FICHA9, 0);
			}
		}
		
		database.update(FichaSQLiteHelper.TABLE_FICHA_TEORICO, 
						cv, 
						FichaSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
		
		alteraMedia(nomeDisciplina, numFichas);
	}
	
	public void alteraCotacaoFicha(String nomeDisciplina, int numFichas, float[] cotacoes){
		ContentValues cv = new ContentValues();
		for(int i=1; i<=numFichas; i++){
			if (i==1)
				cv.put(FichaSQLiteHelper.COLUNA_FICHA1_PERC, cotacoes[i]);
			if (i==2)
				cv.put(FichaSQLiteHelper.COLUNA_FICHA2_PERC, cotacoes[i]);
			if (i==3)
				cv.put(FichaSQLiteHelper.COLUNA_FICHA3_PERC, cotacoes[i]);
			if (i==4)
				cv.put(FichaSQLiteHelper.COLUNA_FICHA4_PERC, cotacoes[i]);
			if (i==5)
				cv.put(FichaSQLiteHelper.COLUNA_FICHA5_PERC, cotacoes[i]);
			if (i==6)
				cv.put(FichaSQLiteHelper.COLUNA_FICHA6_PERC, cotacoes[i]);
			if (i==7)
				cv.put(FichaSQLiteHelper.COLUNA_FICHA7_PERC, cotacoes[i]);
			if (i==8)
				cv.put(FichaSQLiteHelper.COLUNA_FICHA8_PERC, cotacoes[i]);
			if (i==9)
				cv.put(FichaSQLiteHelper.COLUNA_FICHA9_PERC, cotacoes[i]);
		}
		
		database.update(FichaSQLiteHelper.TABLE_FICHA_TEORICO, 
				cv, 
				FichaSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
		
		alteraMedia(nomeDisciplina, numFichas);
		
	}
	
	public int getNumeroFichasValidosByName(String nomeDisciplina){
		Cursor cursor = database.query(FichaSQLiteHelper.TABLE_FICHA_TEORICO, 
									null, (FichaSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);
		if (cursor.isAfterLast())
				return 0;
		cursor.moveToFirst();
		Ficha ficha = cursorToFicha(cursor);
		int numeroFichas = ficha.getQuantidade();
		return numeroFichas;	
	}
	
	public void gravaNota(String nomeDisciplina, String nomeAvaliacao, float inputNota, int numFichas){
		ContentValues cv = new ContentValues();
		if(nomeAvaliacao.contains("o 1"))
			cv.put(FichaSQLiteHelper.COLUNA_FICHA1, inputNota);
		if(nomeAvaliacao.contains("o 2"))
			cv.put(FichaSQLiteHelper.COLUNA_FICHA2, inputNota);
		if(nomeAvaliacao.contains("o 3"))
			cv.put(FichaSQLiteHelper.COLUNA_FICHA3, inputNota);
		if(nomeAvaliacao.contains("o 4"))
			cv.put(FichaSQLiteHelper.COLUNA_FICHA4, inputNota);
		if(nomeAvaliacao.contains("o 5"))
			cv.put(FichaSQLiteHelper.COLUNA_FICHA5, inputNota);
		if(nomeAvaliacao.contains("o 6"))
			cv.put(FichaSQLiteHelper.COLUNA_FICHA6, inputNota);
		if(nomeAvaliacao.contains("o 7"))
			cv.put(FichaSQLiteHelper.COLUNA_FICHA7, inputNota);
		if(nomeAvaliacao.contains("o 8"))
			cv.put(FichaSQLiteHelper.COLUNA_FICHA8, inputNota);
		if(nomeAvaliacao.contains("o 9"))
			cv.put(FichaSQLiteHelper.COLUNA_FICHA9, inputNota);
		
		database.update(FichaSQLiteHelper.TABLE_FICHA_TEORICO, 
				cv, 
				FichaSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
		
		alteraMedia(nomeDisciplina, numFichas);
	}
	
	public void alteraMedia(String nomeDisciplina, int numeroFichas){
		ContentValues cv = new ContentValues();
		Cursor cursor = database.query(FichaSQLiteHelper.TABLE_FICHA_TEORICO, 
							null, (FichaSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);
		cursor.moveToFirst();
		Ficha ficha = cursorToFicha(cursor);
		float media = 0;
		try{
			media = ficha.getFicha1_perc() * ficha.getFicha1();
			
			media += ficha.getFicha2_perc() * ficha.getFicha2();
			
			media += ficha.getFicha3_perc() * ficha.getFicha3();
			
			media += ficha.getFicha4_perc() * ficha.getFicha4();
			
			media += ficha.getFicha5_perc() * ficha.getFicha5();
			
			media += ficha.getFicha6_perc() * ficha.getFicha6();
			
			media += ficha.getFicha7_perc() * ficha.getFicha7();
			
			media += ficha.getFicha8_perc() * ficha.getFicha8();
			
			media += ficha.getFicha9_perc() * ficha.getFicha9();
		}
		catch(Exception e){
			
		}
		
		media = media / (float) 100;
		
		cv.put(FichaSQLiteHelper.COLUNA_MEDIA, media);
		
		database.update(FichaSQLiteHelper.TABLE_FICHA_TEORICO, 
				cv, 
				FichaSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
		
	}
	
	public float getNota(String nomeDisciplina, String nomeAvaliacao){
		float nota = 0;
		Cursor cursor = database.query(FichaSQLiteHelper.TABLE_FICHA_TEORICO, 
							null, (FichaSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);
		if (cursor.isAfterLast())
			return 0;
		cursor.moveToFirst();
		Ficha ficha = cursorToFicha(cursor);
		
		if(nomeAvaliacao.contains("o 1"))
			nota = ficha.getFicha1();
		if(nomeAvaliacao.contains("o 2"))
			nota = ficha.getFicha2();
		if(nomeAvaliacao.contains("o 3"))
			nota = ficha.getFicha3();
		if(nomeAvaliacao.contains("o 4"))
			nota = ficha.getFicha4();
		if(nomeAvaliacao.contains("o 5"))
			nota = ficha.getFicha5();
		if(nomeAvaliacao.contains("o 6"))
			nota = ficha.getFicha6();
		if(nomeAvaliacao.contains("o 7"))
			nota = ficha.getFicha7();
		if(nomeAvaliacao.contains("o 8"))
			nota = ficha.getFicha8();
		if(nomeAvaliacao.contains("o 9"))
			nota = ficha.getFicha9();
		
		return nota;
	}

	public float getMedia(String nomeDisciplina) {
		float media = 0;
		Cursor cursor = database.query(FichaSQLiteHelper.TABLE_FICHA_TEORICO, 
								null, (FichaSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);
		
		if (cursor.isAfterLast())
			return 0;
		cursor.moveToFirst();
		Ficha ficha = cursorToFicha(cursor);
		media = ficha.getMedia();
		
		return media;
	}
	
	public int getPercentagem(String nomeDisciplina){
		int perc = 0;
		Cursor cursor = database.query(FichaSQLiteHelper.TABLE_FICHA_TEORICO, 
								null, (FichaSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);

		if (cursor.isAfterLast())
			return 0;
		cursor.moveToFirst();
		Ficha teste = cursorToFicha(cursor);
		perc = teste.getPercentagem_total();
	
		return perc;	
	}
	
	public void removeFicha(String nomeDisciplina){
		database.delete(FichaSQLiteHelper.TABLE_FICHA_TEORICO, 
						FichaSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
	}
}
