package com.sifanrpf.database;

import java.util.ArrayList;
import java.util.List;

import com.sifanrpf.gradeseeker.Semestre;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SemestreDataSource {
	private SQLiteDatabase database;
	private SemestreSQLiteHelper dbHelper;
	private String[] allColunas = {SemestreSQLiteHelper.COLUNA_ID, SemestreSQLiteHelper.COLUNA_ANO,
									SemestreSQLiteHelper.COLUNA_SEM};
	
	public SemestreDataSource(Context context){
		dbHelper = new SemestreSQLiteHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public Semestre createSemestre(int ano, int sem){
		ContentValues values = new ContentValues();
		values.put(SemestreSQLiteHelper.COLUNA_ANO, ano);
		values.put(SemestreSQLiteHelper.COLUNA_SEM, sem);
		long insertId = database.insert(SemestreSQLiteHelper.TABLE_SEMESTRES, null, values);
		
		Cursor cursor = database.query(SemestreSQLiteHelper.TABLE_SEMESTRES, 
										allColunas,
										SemestreSQLiteHelper.COLUNA_ID + " = " + insertId, null,null,null,null);
		cursor.moveToFirst();
		Semestre newSemestre = cursorToSemestre(cursor);
		cursor.close();
		return newSemestre;
	}
	
	public void deleteSemestre(Semestre semestre){
		long id = semestre.getId();
		System.out.println("Semestre " + semestre.getAno() + " ano " + semestre.getSem()
							+ "º sem" + "removido");
		database.delete(SemestreSQLiteHelper.TABLE_SEMESTRES, 
						SemestreSQLiteHelper.COLUNA_ID + " = " + id, null);
	}
	
	public List<Semestre> getAllSemestres(){
		
		List<Semestre> semestres = new ArrayList<Semestre>();
		Cursor cursor = database.query(SemestreSQLiteHelper.TABLE_SEMESTRES, 
										allColunas, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Semestre semestre = cursorToSemestre(cursor);
			semestres.add(semestre);
			cursor.moveToNext();
		}
		cursor.close();
		return semestres;
	}
	
	private Semestre cursorToSemestre(Cursor cursor){
		Semestre semestre = new Semestre();
		semestre.setId(cursor.getLong(0));
		semestre.setAno(cursor.getInt(1));
		semestre.setSem(cursor.getInt(2));
		return semestre;
	}	
}
