package com.sifanrpf.gradeseeker;

public class Disciplina {

	private long id;
	private int ano;
	private int sem;
	private float creditos;
	private String nome;
	private String professor;
	private int perc;
	
	public Disciplina(){}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public float getCreditos() {
		return creditos;
	}
	public void setCreditos(float creditos) {
		this.creditos = creditos;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getProfessor() {
		return professor;
	}
	public void setProfessor(String professor) {
		this.professor = professor;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public int getSem() {
		return sem;
	}

	public void setSem(int sem) {
		this.sem = sem;
	}
	
	public int getPerc(){
		return perc;
	}
	
	public void setPerc(int perc){
		this.perc=perc;
	}
	
	
}
