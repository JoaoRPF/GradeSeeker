package com.sifanrpf.database;

import com.sifanrpf.gradeseeker.Disciplina;
import com.sifanrpf.gradeseeker.TesteTeorico;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TesteTeoricoDataSource {
	private SQLiteDatabase database;
	private TesteTeoricoSQLiteHelper dbHelper;
	/*private String[] allColunas =  {TesteTeoricoSQLiteHelper.COLUNA_ID, 
									TesteTeoricoSQLiteHelper.COLUNA_NOME,
									TesteTeoricoSQLiteHelper.COLUNA_QUANTIDADE,
									TesteTeoricoSQLiteHelper.COLUNA_TESTE1,
									TesteTeoricoSQLiteHelper.COLUNA_TESTE2,
									TesteTeoricoSQLiteHelper.COLUNA_TESTE3,
									};*/
	
	public TesteTeoricoDataSource(Context context){
		dbHelper = new TesteTeoricoSQLiteHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public TesteTeorico createTesteTeorico(String nome, int quantidade, int perc){
		ContentValues values = new ContentValues();
		values.put(TesteTeoricoSQLiteHelper.COLUNA_NOME, nome);
		values.put(TesteTeoricoSQLiteHelper.COLUNA_QUANTIDADE, quantidade);
		values.put(TesteTeoricoSQLiteHelper.COLUNA_PERC_TOTAL, perc);
		values.put(TesteTeoricoSQLiteHelper.COLUNA_MEDIA, 0);
		
		int criado = database.update(TesteTeoricoSQLiteHelper.TABLE_TESTE_TEORICO, 
				values, 
				TesteTeoricoSQLiteHelper.COLUNA_NOME + " = '" + nome + "'", null);
		
		if(criado != 0)
			database.delete(TesteTeoricoSQLiteHelper.TABLE_TESTE_TEORICO, 
						TesteTeoricoSQLiteHelper.COLUNA_NOME + " = '" + nome + "'", null);
		
		long insertId = database.insert(TesteTeoricoSQLiteHelper.TABLE_TESTE_TEORICO, null, values);
		
		Cursor cursor = database.query(TesteTeoricoSQLiteHelper.TABLE_TESTE_TEORICO, 
									null,
									TesteTeoricoSQLiteHelper.COLUNA_ID + " = " + insertId, null,null,null,null);
		cursor.moveToFirst();
		TesteTeorico newTesteTeorico = cursorToTesteTeorico(cursor);
		cursor.close();
		return newTesteTeorico;
	}
	
	public void removeTesteTeorico(String nomeDisciplina){
		database.delete(TesteTeoricoSQLiteHelper.TABLE_TESTE_TEORICO, 
						TesteTeoricoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
	}
	
	public TesteTeorico cursorToTesteTeorico(Cursor cursor){
		TesteTeorico teste = new TesteTeorico();
		teste.setId(cursor.getLong(0));
		teste.setQuantidade(cursor.getInt(2));
		teste.setTeste1(cursor.getFloat(3));
		teste.setTeste2(cursor.getFloat(4));
		teste.setTeste3(cursor.getFloat(5));
		teste.setTeste4(cursor.getFloat(6));
		teste.setTeste5(cursor.getFloat(7));
		teste.setTeste6(cursor.getFloat(8));
		teste.setTeste7(cursor.getFloat(9));
		teste.setTeste8(cursor.getFloat(10));
		teste.setTeste9(cursor.getFloat(11));
		teste.setTeste1_perc(cursor.getFloat(12));
		teste.setTeste2_perc(cursor.getFloat(13));
		teste.setTeste3_perc(cursor.getFloat(14));
		teste.setTeste4_perc(cursor.getFloat(15));
		teste.setTeste5_perc(cursor.getFloat(16));
		teste.setTeste6_perc(cursor.getFloat(17));
		teste.setTeste7_perc(cursor.getFloat(18));
		teste.setTeste8_perc(cursor.getFloat(19));
		teste.setTeste9_perc(cursor.getFloat(20));
		teste.setPercentagem_total(cursor.getInt(21));
		teste.setMedia(cursor.getFloat(22));
		return teste;
	}
	
	public void gravaCotacaoTestes(String nomeDisciplina, int numTestes, float[] cotacoes){
		ContentValues cv = new ContentValues();
		for(int i=1; i<=numTestes; i++){
			if (i==1){
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE1_PERC, cotacoes[i-1]);
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE1, 0);
			}
			if (i==2){
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE2_PERC, cotacoes[i-1]);
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE2, 0);
			}
			if (i==3){
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE3_PERC, cotacoes[i-1]);
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE3, 0);
			}
			if (i==4){
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE4_PERC, cotacoes[i-1]);
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE4, 0);
			}
			if (i==5){
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE5_PERC, cotacoes[i-1]);
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE5, 0);
			}
			if (i==6){
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE6_PERC, cotacoes[i-1]);
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE6, 0);
			}
			if (i==7){
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE7_PERC, cotacoes[i-1]);
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE7, 0);
			}
			if (i==8){
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE8_PERC, cotacoes[i-1]);
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE8, 0);
			}
			if (i==9){
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE9_PERC, cotacoes[i-1]);
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE9, 0);
			}
		}
		
		database.update(TesteTeoricoSQLiteHelper.TABLE_TESTE_TEORICO, 
						cv, 
						TesteTeoricoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
		
		alteraMedia(nomeDisciplina, numTestes);
	}
	
	public void alteraCotacaoTeste(String nomeDisciplina, int numTestes, float[] cotacoes){
		ContentValues cv = new ContentValues();
		for(int i=1; i<=numTestes; i++){
			if (i==1)
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE1_PERC, cotacoes[i]);
			if (i==2)
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE2_PERC, cotacoes[i]);
			if (i==3)
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE3_PERC, cotacoes[i]);
			if (i==4)
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE4_PERC, cotacoes[i]);
			if (i==5)
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE5_PERC, cotacoes[i]);
			if (i==6)
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE6_PERC, cotacoes[i]);
			if (i==7)
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE7_PERC, cotacoes[i]);
			if (i==8)
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE8_PERC, cotacoes[i]);
			if (i==9)
				cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE9_PERC, cotacoes[i]);
		}
		
		database.update(TesteTeoricoSQLiteHelper.TABLE_TESTE_TEORICO, 
				cv, 
				TesteTeoricoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
		
		alteraMedia(nomeDisciplina, numTestes);
		
	}
	
	public int getNumeroTestesValidosByName(String nomeDisciplina){
		Cursor cursor = database.query(TesteTeoricoSQLiteHelper.TABLE_TESTE_TEORICO, 
									null, (TesteTeoricoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);
		if (cursor.isAfterLast())
				return 0;
		cursor.moveToFirst();
		TesteTeorico teste = cursorToTesteTeorico(cursor);
		int numeroTestes = teste.getQuantidade();
		return numeroTestes;	
	}
	
	public void gravaNota(String nomeDisciplina, String nomeAvaliacao, float inputNota, int numTestes){
		ContentValues cv = new ContentValues();
		if(nomeAvaliacao.contains("e 1"))
			cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE1, inputNota);
		if(nomeAvaliacao.contains("e 2"))
			cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE2, inputNota);
		if(nomeAvaliacao.contains("e 3"))
			cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE3, inputNota);
		if(nomeAvaliacao.contains("e 4"))
			cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE4, inputNota);
		if(nomeAvaliacao.contains("e 5"))
			cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE5, inputNota);
		if(nomeAvaliacao.contains("e 6"))
			cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE6, inputNota);
		if(nomeAvaliacao.contains("e 7"))
			cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE7, inputNota);
		if(nomeAvaliacao.contains("e 8"))
			cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE8, inputNota);
		if(nomeAvaliacao.contains("e 9"))
			cv.put(TesteTeoricoSQLiteHelper.COLUNA_TESTE9, inputNota);
		
		database.update(TesteTeoricoSQLiteHelper.TABLE_TESTE_TEORICO, 
				cv, 
				TesteTeoricoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
		
		alteraMedia(nomeDisciplina, numTestes);
	}
	
	public void alteraMedia(String nomeDisciplina, int numeroTestes){
		ContentValues cv = new ContentValues();
		Cursor cursor = database.query(TesteTeoricoSQLiteHelper.TABLE_TESTE_TEORICO, 
							null, (TesteTeoricoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);
		cursor.moveToFirst();
		TesteTeorico teste = cursorToTesteTeorico(cursor);
		float media = 0;
		try{
			media = teste.getTeste1_perc() * teste.getTeste1();
			
			media += teste.getTeste2_perc() * teste.getTeste2();
			
			media += teste.getTeste3_perc() * teste.getTeste3();
			
			media += teste.getTeste4_perc() * teste.getTeste4();
			
			media += teste.getTeste5_perc() * teste.getTeste5();
			
			media += teste.getTeste6_perc() * teste.getTeste6();
			
			media += teste.getTeste7_perc() * teste.getTeste7();
			
			media += teste.getTeste8_perc() * teste.getTeste8();
			
			media += teste.getTeste9_perc() * teste.getTeste9();
		}
		catch(Exception e){
			
		}
		
		media = media / (float) 100;
		
		cv.put(TesteTeoricoSQLiteHelper.COLUNA_MEDIA, media);
		
		database.update(TesteTeoricoSQLiteHelper.TABLE_TESTE_TEORICO, 
				cv, 
				TesteTeoricoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
		
	}
	
	public float getNota(String nomeDisciplina, String nomeAvaliacao){
		float nota = 0;
		Cursor cursor = database.query(TesteTeoricoSQLiteHelper.TABLE_TESTE_TEORICO, 
							null, (TesteTeoricoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);
		if (cursor.isAfterLast())
			return 0;
		cursor.moveToFirst();
		TesteTeorico teste = cursorToTesteTeorico(cursor);
		
		if(nomeAvaliacao.contains("e 1"))
			nota = teste.getTeste1();
		if(nomeAvaliacao.contains("e 2"))
			nota = teste.getTeste2();
		if(nomeAvaliacao.contains("e 3"))
			nota = teste.getTeste3();
		if(nomeAvaliacao.contains("e 4"))
			nota = teste.getTeste4();
		if(nomeAvaliacao.contains("e 5"))
			nota = teste.getTeste5();
		if(nomeAvaliacao.contains("e 6"))
			nota = teste.getTeste6();
		if(nomeAvaliacao.contains("e 7"))
			nota = teste.getTeste7();
		if(nomeAvaliacao.contains("e 8"))
			nota = teste.getTeste8();
		if(nomeAvaliacao.contains("e 9"))
			nota = teste.getTeste9();
		
		return nota;
	}

	public float getMedia(String nomeDisciplina) {
		float media = 0;
		Cursor cursor = database.query(TesteTeoricoSQLiteHelper.TABLE_TESTE_TEORICO, 
								null, (TesteTeoricoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);
		
		if (cursor.isAfterLast())
			return 0;
		cursor.moveToFirst();
		TesteTeorico teste = cursorToTesteTeorico(cursor);
		media = teste.getMedia();
		
		return media;
	}
	
	public int getPercentagem(String nomeDisciplina){
		int perc = 0;
		Cursor cursor = database.query(TesteTeoricoSQLiteHelper.TABLE_TESTE_TEORICO, 
								null, (TesteTeoricoSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);

		if (cursor.isAfterLast())
			return 0;
		cursor.moveToFirst();
		TesteTeorico teste = cursorToTesteTeorico(cursor);
		perc = teste.getPercentagem_total();
	
		return perc;	
	}
}







