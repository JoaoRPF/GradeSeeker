package com.sifanrpf.database;

import com.sifanrpf.gradeseeker.Bonus;
import com.sifanrpf.gradeseeker.TesteTeorico;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BonusDataSource {
	
	private SQLiteDatabase database;
	private BonusSQLiteHelper dbHelper;
	
	public BonusDataSource(Context context){
		dbHelper = new BonusSQLiteHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public Bonus createBonus(String nome, String tipo, int percentagem, float valor){
		ContentValues values = new ContentValues();
		values.put(BonusSQLiteHelper.COLUNA_NOME, nome);
		values.put(BonusSQLiteHelper.COLUNA_TIPO, tipo);
		values.put(BonusSQLiteHelper.COLUNA_PERCENTAGEM, percentagem);
		values.put(BonusSQLiteHelper.COLUNA_VALOR, valor);
		
		int criado = database.update(BonusSQLiteHelper.TABLE_BONUS, 
				values, 
				BonusSQLiteHelper.COLUNA_NOME + " = '" + nome + "'", null);
		
		if(criado != 0)
			database.delete(BonusSQLiteHelper.TABLE_BONUS, 
						BonusSQLiteHelper.COLUNA_NOME + " = '" + nome + "'", null);
		
		long insertId = database.insert(BonusSQLiteHelper.TABLE_BONUS, null, values);
		
		Cursor cursor = database.query(BonusSQLiteHelper.TABLE_BONUS, 
									null,
									BonusSQLiteHelper.COLUNA_ID + " = " + insertId, null,null,null,null);
		cursor.moveToFirst();
		Bonus newBonus = cursorToBonus(cursor);
		cursor.close();
		return newBonus;
	}
	
	public Bonus cursorToBonus(Cursor cursor){
		Bonus bonus = new Bonus();
		bonus.setId(cursor.getLong(0));
		bonus.setTipo(cursor.getString(2));
		bonus.setPercentagem(cursor.getInt(3));
		bonus.setValor(cursor.getFloat(4));
		return bonus;
	}
	
	public void gravaBonus(String nomeDisciplina, String tipo, int percentagem, float valor){
		ContentValues values = new ContentValues();
		values.put(BonusSQLiteHelper.COLUNA_NOME, nomeDisciplina);
		values.put(BonusSQLiteHelper.COLUNA_TIPO, tipo);
		values.put(BonusSQLiteHelper.COLUNA_PERCENTAGEM, percentagem);
		values.put(BonusSQLiteHelper.COLUNA_VALOR, valor);
				
		database.update(BonusSQLiteHelper.TABLE_BONUS, 
				values, 
				BonusSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
	}
	
	public String[] getBonus(String nomeDisciplina){
		String[] bonusStr = new String[1];
		int bonusPerc = 0;
		float bonusValor = 0;
		Cursor cursor = database.query(BonusSQLiteHelper.TABLE_BONUS, 
								null, (BonusSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);
		if (cursor.isAfterLast())
			return null;
		cursor.moveToFirst();
		Bonus bonus = cursorToBonus(cursor);
		
		if(bonus.getTipo().equals("Percentagem")){
			bonusPerc = bonus.getPercentagem();
			bonusStr[0] = String.valueOf(bonusPerc);
		}
		if(bonus.getTipo().equals("Valor")){
			bonusValor = bonus.getValor();
			bonusStr[0] = Float.toString(bonusValor);
		}
		return bonusStr;
	}
	
	public int getNumeroBonus(String nomeDisciplina){
		Cursor cursor = database.query(BonusSQLiteHelper.TABLE_BONUS, 
					null, (BonusSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);
		return cursor.getCount();
	}
	
	public void removeBonus(String nomeDisciplina){
		database.delete(BonusSQLiteHelper.TABLE_BONUS, 
						BonusSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'", null);
	}
	
}
