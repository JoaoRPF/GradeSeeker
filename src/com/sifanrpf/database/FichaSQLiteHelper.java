package com.sifanrpf.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FichaSQLiteHelper extends SQLiteOpenHelper{
	public static final String TABLE_FICHA_TEORICO = "Fichas";
	public static final String COLUNA_ID = "_id";
	public static final String COLUNA_NOME = "Nome";
	public static final String COLUNA_QUANTIDADE = "Quantidade";
	public static final String COLUNA_FICHA1 = "Ficha1";
	public static final String COLUNA_FICHA2 = "Ficha2";
	public static final String COLUNA_FICHA3 = "Ficha3";
	public static final String COLUNA_FICHA4 = "Ficha4";
	public static final String COLUNA_FICHA5 = "Ficha5";
	public static final String COLUNA_FICHA6 = "Ficha6";
	public static final String COLUNA_FICHA7 = "Ficha7";
	public static final String COLUNA_FICHA8 = "Ficha8";
	public static final String COLUNA_FICHA9 = "Ficha9";
	public static final String COLUNA_FICHA1_PERC = "Ficha1PERC";
	public static final String COLUNA_FICHA2_PERC = "Ficha2PERC";
	public static final String COLUNA_FICHA3_PERC = "Ficha3PERC";
	public static final String COLUNA_FICHA4_PERC = "Ficha4PERC";
	public static final String COLUNA_FICHA5_PERC = "Ficha5PERC";
	public static final String COLUNA_FICHA6_PERC = "Ficha6PERC";
	public static final String COLUNA_FICHA7_PERC = "Ficha7PERC";
	public static final String COLUNA_FICHA8_PERC = "Ficha8PERC";
	public static final String COLUNA_FICHA9_PERC = "Ficha9PERC";
	public static final String COLUNA_PERC_TOTAL = "Percentagem_Total";
	public static final String COLUNA_MEDIA = "Media";
	
	private static final String DATABASE_NAME = "Ficha.db";
	private static final int DATABASE_VERSION = 3;
	
	private static final String DATABASE_CREATE = "CREATE TABLE " +
												  TABLE_FICHA_TEORICO + 
												  "(" + 
												  COLUNA_ID + 
												  " integer primary key autoincrement, " + 
												  COLUNA_NOME + 
												  " text unique not null, " +
												  COLUNA_QUANTIDADE + 
												  " integer not null, " + 
												  COLUNA_FICHA1 + 
												  " real, " +
												  COLUNA_FICHA2 + 
												  " real, " +
												  COLUNA_FICHA3 + 
												  " real, " + 
												  COLUNA_FICHA4 + 
												  " real, " +
												  COLUNA_FICHA5 + 
												  " real, " +
												  COLUNA_FICHA6 + 
												  " real, " + 
												  COLUNA_FICHA7 + 
												  " real, " +
												  COLUNA_FICHA8 + 
												  " real, " +
												  COLUNA_FICHA9 + 
												  " real, " +
												  COLUNA_FICHA1_PERC + 
												  " real, " +
												  COLUNA_FICHA2_PERC + 
												  " real, " +
												  COLUNA_FICHA3_PERC + 
												  " real, " + 
												  COLUNA_FICHA4_PERC + 
												  " real, " +
												  COLUNA_FICHA5_PERC + 
												  " real, " +
												  COLUNA_FICHA6_PERC + 
												  " real, " + 
												  COLUNA_FICHA7_PERC + 
												  " real, " +
												  COLUNA_FICHA8_PERC + 
												  " real, " +
												  COLUNA_FICHA9_PERC +
												  " real, " +
												  COLUNA_PERC_TOTAL +
												  " integer not null, " +
												  COLUNA_MEDIA +
												  " real);";
	
	public FichaSQLiteHelper(Context context){
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
		
		bd.execSQL("DROP TABLE IF EXISTS " + TABLE_FICHA_TEORICO);
		onCreate(bd);
	}

}
