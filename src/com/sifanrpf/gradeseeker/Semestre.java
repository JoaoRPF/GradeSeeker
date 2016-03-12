package com.sifanrpf.gradeseeker;

public class Semestre {
	
	private long id;
	private int ano;
	private int sem;
	
	public Semestre(int ano, int sem){
		this.setAno(ano);
		this.setSem(sem);
	}
	
	public Semestre(){}

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
