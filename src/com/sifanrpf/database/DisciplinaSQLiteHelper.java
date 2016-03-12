package com.sifanrpf.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DisciplinaSQLiteHelper extends SQLiteOpenHelper{
	
	public static final String TABLE_DISCIPLINAS = "Disciplinas";
	public static final String COLUNA_ID = "_id";
	public static final String COLUNA_ANO = "Ano"; 
	public static final String COLUNA_SEM = "Sem";
	public static final String COLUNA_CREDITOS = "Creditos";
	public static final String COLUNA_NOME = "NomeDisciplina";
	public static final String COLUNA_PROFESSOR = "Professor";
	public static final String COLUNA_PERC = "Percentagem";
	
	private static final String DATABASE_NAME = "disciplinas.db";
	private static final int DATABASE_VERSION = 7;
	
	private static final String DATABASE_CREATE = "CREATE TABLE " +
												  TABLE_DISCIPLINAS + 
												  "(" + 
												  COLUNA_ID + 
												  " integer primary key autoincrement, " + 
												  COLUNA_ANO + 
												  " integer not null, " + 
												  COLUNA_SEM + 
												  " integer not null, " +
												  COLUNA_CREDITOS + 
												  " real not null, " +
												  COLUNA_NOME + 
												  " text not null, " +
												  COLUNA_PERC +
												  " integer not null, " +
												  COLUNA_PROFESSOR +
												  " text);";
	
	public DisciplinaSQLiteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database){
		database.execSQL(DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase bd, int oldVersion, int newVersion){
		Log.w(SemestreSQLiteHelper.class.getName(),
			  "Upgrading database from version " + oldVersion + "to" + newVersion + ", which will destroy all old data");
		
		bd.execSQL("DROP TABLE IF EXISTS " + TABLE_DISCIPLINAS);
		onCreate(bd);
	}	
}
