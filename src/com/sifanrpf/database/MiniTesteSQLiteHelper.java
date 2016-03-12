package com.sifanrpf.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MiniTesteSQLiteHelper extends SQLiteOpenHelper{

	public static final String TABLE_MINI_TESTE = "Mini_Testes";
	public static final String COLUNA_ID = "_id";
	public static final String COLUNA_NOME = "Nome";
	public static final String COLUNA_QUANTIDADE = "Quantidade";
	public static final String COLUNA_TESTE1 = "Teste1";
	public static final String COLUNA_TESTE2 = "Teste2";
	public static final String COLUNA_TESTE3 = "Teste3";
	public static final String COLUNA_TESTE4 = "Teste4";
	public static final String COLUNA_TESTE5 = "Teste5";
	public static final String COLUNA_TESTE6 = "Teste6";
	public static final String COLUNA_TESTE7 = "Teste7";
	public static final String COLUNA_TESTE8 = "Teste8";
	public static final String COLUNA_TESTE9 = "Teste9";
	public static final String COLUNA_TESTE1_PERC = "Teste1PERC";
	public static final String COLUNA_TESTE2_PERC = "Teste2PERC";
	public static final String COLUNA_TESTE3_PERC = "Teste3PERC";
	public static final String COLUNA_TESTE4_PERC = "Teste4PERC";
	public static final String COLUNA_TESTE5_PERC = "Teste5PERC";
	public static final String COLUNA_TESTE6_PERC = "Teste6PERC";
	public static final String COLUNA_TESTE7_PERC = "Teste7PERC";
	public static final String COLUNA_TESTE8_PERC = "Teste8PERC";
	public static final String COLUNA_TESTE9_PERC = "Teste9PERC";
	public static final String COLUNA_PERC_TOTAL = "Percentagem_Total";
	public static final String COLUNA_MEDIA = "Media";
	
	private static final String DATABASE_NAME = "miniTeste.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "CREATE TABLE " +
												  TABLE_MINI_TESTE + 
												  "(" + 
												  COLUNA_ID + 
												  " integer primary key autoincrement, " + 
												  COLUNA_NOME + 
												  " text unique not null, " +
												  COLUNA_QUANTIDADE + 
												  " integer not null, " + 
												  COLUNA_TESTE1 + 
												  " real, " +
												  COLUNA_TESTE2 + 
												  " real, " +
												  COLUNA_TESTE3 + 
												  " real, " + 
												  COLUNA_TESTE4 + 
												  " real, " +
												  COLUNA_TESTE5 + 
												  " real, " +
												  COLUNA_TESTE6 + 
												  " real, " + 
												  COLUNA_TESTE7 + 
												  " real, " +
												  COLUNA_TESTE8 + 
												  " real, " +
												  COLUNA_TESTE9 + 
												  " real, " +
												  COLUNA_TESTE1_PERC + 
												  " real, " +
												  COLUNA_TESTE2_PERC + 
												  " real, " +
												  COLUNA_TESTE3_PERC + 
												  " real, " + 
												  COLUNA_TESTE4_PERC + 
												  " real, " +
												  COLUNA_TESTE5_PERC + 
												  " real, " +
												  COLUNA_TESTE6_PERC + 
												  " real, " + 
												  COLUNA_TESTE7_PERC + 
												  " real, " +
												  COLUNA_TESTE8_PERC + 
												  " real, " +
												  COLUNA_TESTE9_PERC +
												  " real, " +
												  COLUNA_PERC_TOTAL +
												  " integer not null, " +
												  COLUNA_MEDIA +
												  " real);";
	
	public MiniTesteSQLiteHelper(Context context){
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
		
		bd.execSQL("DROP TABLE IF EXISTS " + TABLE_MINI_TESTE);
		onCreate(bd);
	}
}
