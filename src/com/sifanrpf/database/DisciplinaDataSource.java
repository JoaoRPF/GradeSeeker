package com.sifanrpf.database;

import java.util.ArrayList;
import java.util.List;

import com.sifanrpf.gradeseeker.Disciplina;
import com.sifanrpf.gradeseeker.Semestre;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DisciplinaDataSource {
	private SQLiteDatabase database;
	private DisciplinaSQLiteHelper dbHelper;
	private String[] allColunas =  {DisciplinaSQLiteHelper.COLUNA_ID, 
									DisciplinaSQLiteHelper.COLUNA_ANO,
									DisciplinaSQLiteHelper.COLUNA_SEM,
									DisciplinaSQLiteHelper.COLUNA_CREDITOS,
									DisciplinaSQLiteHelper.COLUNA_NOME,
									DisciplinaSQLiteHelper.COLUNA_PROFESSOR
									};
	
	public DisciplinaDataSource(Context context){
		dbHelper = new DisciplinaSQLiteHelper(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public Disciplina createDisciplina(int ano, int sem, float creditos, String nomeDisciplina, String professor){
		ContentValues values = new ContentValues();
		values.put(DisciplinaSQLiteHelper.COLUNA_ANO, ano);
		values.put(DisciplinaSQLiteHelper.COLUNA_SEM, sem);
		values.put(DisciplinaSQLiteHelper.COLUNA_CREDITOS, creditos);
		values.put(DisciplinaSQLiteHelper.COLUNA_NOME, nomeDisciplina);
		values.put(DisciplinaSQLiteHelper.COLUNA_PROFESSOR, professor);
		values.put(DisciplinaSQLiteHelper.COLUNA_PERC,0);
		
		long insertId = database.insert(DisciplinaSQLiteHelper.TABLE_DISCIPLINAS, null, values);
		
		Cursor cursor = database.query(DisciplinaSQLiteHelper.TABLE_DISCIPLINAS, 
										null,
										DisciplinaSQLiteHelper.COLUNA_ID + " = " + insertId, null,null,null,null);
		cursor.moveToFirst();
		Disciplina newDisciplina = cursorToDisciplina(cursor);
		cursor.close();
		return newDisciplina;
	}
	
	public void deleteDisciplina(Disciplina disciplina){
		long id = disciplina.getId();
		System.out.println("A cadeira: " + disciplina.getNome() + " do " + disciplina.getAno() + "º ano " + disciplina.getSem()
							+ "º sem" + " foi removida com sucesso");
		database.delete(DisciplinaSQLiteHelper.TABLE_DISCIPLINAS, 
						DisciplinaSQLiteHelper.COLUNA_ID + " = " + id, null);
	}
	
	public List<Disciplina> getAllDisciplinas(){
		
		List<Disciplina> disciplinas = new ArrayList<Disciplina>();
		Cursor cursor = database.query(DisciplinaSQLiteHelper.TABLE_DISCIPLINAS, 
										allColunas, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Disciplina disciplina = cursorToDisciplina(cursor);
			disciplinas.add(disciplina);
			cursor.moveToNext();
		}
		cursor.close();
		return disciplinas;
	}
	
	private Disciplina cursorToDisciplina(Cursor cursor){
		Disciplina disciplina = new Disciplina();
		disciplina.setId(cursor.getLong(0));
		disciplina.setAno(cursor.getInt(1));
		disciplina.setSem(cursor.getInt(2));
		disciplina.setCreditos(cursor.getFloat(3));
		disciplina.setNome(cursor.getString(4));
		disciplina.setProfessor(cursor.getString(5));
		//disciplina.setPerc(cursor.getInt(6));
		return disciplina;
	}
	
	public void deleteDisciplinaByName(String name){
		System.out.println("A cadeira: " + name + " foi removida com sucesso");
		database.delete(DisciplinaSQLiteHelper.TABLE_DISCIPLINAS,
						DisciplinaSQLiteHelper.COLUNA_NOME + " = '" + name + "'", null);
	}
	
	public void changeDisciplinaName(Disciplina disciplina, long id, String name){
		ContentValues cv = new ContentValues();
		String newName = name + " " + disciplina.getAno() + "º Ano" + " " + disciplina.getSem() + "º Sem";
		cv.put(DisciplinaSQLiteHelper.COLUNA_NOME, newName); 
		database.update(DisciplinaSQLiteHelper.TABLE_DISCIPLINAS, 
						cv, 
						DisciplinaSQLiteHelper.COLUNA_ID + " = " + id, null);
	}
	
	public List<Disciplina> getDisciplinasBySemestre(int ano, int sem){
		List<Disciplina> disciplinas = new ArrayList<Disciplina>();
		String[] colunas =  {DisciplinaSQLiteHelper.COLUNA_ANO,
							 DisciplinaSQLiteHelper.COLUNA_SEM};
		
		Cursor cursor = database.query(DisciplinaSQLiteHelper.TABLE_DISCIPLINAS, 
										null, (DisciplinaSQLiteHelper.COLUNA_ANO + " = " + ano + " AND " + 
										DisciplinaSQLiteHelper.COLUNA_SEM + " = " + sem), null, null, null, null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Disciplina disciplina = cursorToDisciplina(cursor);
			disciplinas.add(disciplina);
			cursor.moveToNext();
		}
		cursor.close();
		return disciplinas;
	}
	
	public float getCreditosByName(String nomeDisciplina){
		Cursor cursor = database.query(DisciplinaSQLiteHelper.TABLE_DISCIPLINAS, 
				null, (DisciplinaSQLiteHelper.COLUNA_NOME + " = '" + nomeDisciplina + "'"), null, null, null, null);
		cursor.moveToFirst();
		Disciplina disciplina = cursorToDisciplina(cursor);
		float creditos = disciplina.getCreditos();
		return creditos;	
	}
	
	public void changeCreditosByName(float cred, String nome){
		ContentValues cv = new ContentValues();
		cv.put(DisciplinaSQLiteHelper.COLUNA_CREDITOS, cred); 
		database.update(DisciplinaSQLiteHelper.TABLE_DISCIPLINAS, 
						cv, 
						DisciplinaSQLiteHelper.COLUNA_NOME + " = '" + nome + "'", null);
	}
}
