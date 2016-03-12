package com.sifanrpf.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TrabalhoSQLiteHelper extends SQLiteOpenHelper{
	public static final String TABLE_TRABALHO_TEORICO = "Trabalhos";
	public static final String COLUNA_ID = "_id";
	public static final String COLUNA_NOME = "Nome";
	public static final String COLUNA_QUANTIDADE = "Quantidade";
	public static final String COLUNA_TRABALHO1 = "Trabalho1";
	public static final String COLUNA_TRABALHO2 = "Trabalho2";
	public static final String COLUNA_TRABALHO3 = "Trabalho3";
	public static final String COLUNA_TRABALHO4 = "Trabalho4";
	public static final String COLUNA_TRABALHO5 = "Trabalho5";
	public static final String COLUNA_TRABALHO6 = "Trabalho6";
	public static final String COLUNA_TRABALHO7 = "Trabalho7";
	public static final String COLUNA_TRABALHO8 = "Trabalho8";
	public static final String COLUNA_TRABALHO9 = "Trabalho9";
	public static final String COLUNA_TRABALHO1_PERC = "Trabalho1PERC";
	public static final String COLUNA_TRABALHO2_PERC = "Trabalho2PERC";
	public static final String COLUNA_TRABALHO3_PERC = "Trabalho3PERC";
	public static final String COLUNA_TRABALHO4_PERC = "Trabalho4PERC";
	public static final String COLUNA_TRABALHO5_PERC = "Trabalho5PERC";
	public static final String COLUNA_TRABALHO6_PERC = "Trabalho6PERC";
	public static final String COLUNA_TRABALHO7_PERC = "Trabalho7PERC";
	public static final String COLUNA_TRABALHO8_PERC = "Trabalho8PERC";
	public static final String COLUNA_TRABALHO9_PERC = "Trabalho9PERC";
	public static final String COLUNA_PERC_TOTAL = "Percentagem_Total";
	public static final String COLUNA_MEDIA = "Media";
	
	private static final String DATABASE_NAME = "Trabalho.db";
	private static final int DATABASE_VERSION = 3;
	
	private static final String DATABASE_CREATE = "CREATE TABLE " +
												  TABLE_TRABALHO_TEORICO + 
												  "(" + 
												  COLUNA_ID + 
												  " integer primary key autoincrement, " + 
												  COLUNA_NOME + 
												  " text unique not null, " +
												  COLUNA_QUANTIDADE + 
												  " integer not null, " + 
												  COLUNA_TRABALHO1 + 
												  " real, " +
												  COLUNA_TRABALHO2 + 
												  " real, " +
												  COLUNA_TRABALHO3 + 
												  " real, " + 
												  COLUNA_TRABALHO4 + 
												  " real, " +
												  COLUNA_TRABALHO5 + 
												  " real, " +
												  COLUNA_TRABALHO6 + 
												  " real, " + 
												  COLUNA_TRABALHO7 + 
												  " real, " +
												  COLUNA_TRABALHO8 + 
												  " real, " +
												  COLUNA_TRABALHO9 + 
												  " real, " +
												  COLUNA_TRABALHO1_PERC + 
												  " real, " +
												  COLUNA_TRABALHO2_PERC + 
												  " real, " +
												  COLUNA_TRABALHO3_PERC + 
												  " real, " + 
												  COLUNA_TRABALHO4_PERC + 
												  " real, " +
												  COLUNA_TRABALHO5_PERC + 
												  " real, " +
												  COLUNA_TRABALHO6_PERC + 
												  " real, " + 
												  COLUNA_TRABALHO7_PERC + 
												  " real, " +
												  COLUNA_TRABALHO8_PERC + 
												  " real, " +
												  COLUNA_TRABALHO9_PERC +
												  " real, " +
												  COLUNA_PERC_TOTAL +
												  " integer not null, " +
												  COLUNA_MEDIA +
												  " real);";
	
	public TrabalhoSQLiteHelper(Context context){
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
		
		bd.execSQL("DROP TABLE IF EXISTS " + TABLE_TRABALHO_TEORICO);
		onCreate(bd);
	}
}
