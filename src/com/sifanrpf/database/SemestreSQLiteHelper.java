package com.sifanrpf.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SemestreSQLiteHelper extends SQLiteOpenHelper{
	
	public static final String TABLE_SEMESTRES = "Semestres";
	public static final String COLUNA_ID = "_id";
	public static final String COLUNA_SEMESTRE = "Semestre";
	public static final String COLUNA_ANO = "Ano"; 
	public static final String COLUNA_SEM = "Sem"; 

	private static final String DATABASE_NAME = "semestres.db";
	private static final int DATABASE_VERSION = 6;
	
	//INSTRUCAO SQL PARA A CRIACAO DE UMA BASE DE DADOS
	
	private static final String DATABASE_CREATE = "create table " +
												  TABLE_SEMESTRES + 
												  "(" + 
												  COLUNA_ID + 
												  " integer primary key autoincrement, " + 
												  COLUNA_ANO + 
												  " integer not null, " + 
												  COLUNA_SEM + 
												  " integer not null);";
	
	public SemestreSQLiteHelper(Context context){
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
		
		bd.execSQL("DROP TABLE IF EXISTS " + TABLE_SEMESTRES);
		onCreate(bd);
	}	
}
