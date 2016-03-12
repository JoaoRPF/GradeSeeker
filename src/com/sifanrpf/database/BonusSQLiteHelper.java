package com.sifanrpf.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BonusSQLiteHelper extends SQLiteOpenHelper{

	public static final String TABLE_BONUS = "Bonus";
	public static final String COLUNA_ID = "_id";
	public static final String COLUNA_NOME = "Nome";
	public static final String COLUNA_TIPO = "Tipo";
	public static final String COLUNA_PERCENTAGEM = "Percentagem";
	public static final String COLUNA_VALOR = "Valor";
	
	private static final String DATABASE_NAME = "bonus.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "CREATE TABLE " +
												  TABLE_BONUS + 
												  "(" + 
												  COLUNA_ID + 
												  " integer primary key autoincrement, " + 
												  COLUNA_NOME + 
												  " text unique not null, " +
												  COLUNA_TIPO + 
												  " text not null, " + 
												  COLUNA_PERCENTAGEM + 
												  " integer, " +
												  COLUNA_VALOR +
												  " real);";
	
	public BonusSQLiteHelper(Context context){
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
		
		bd.execSQL("DROP TABLE IF EXISTS " + TABLE_BONUS);
		onCreate(bd);
	}

}
