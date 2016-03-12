package com.sifanrpf.gradeseeker;

public class Bonus {
	private long id;
	private String nomeDisciplina;
	private String tipo;
	private int percentagem;
	private float valor;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNomeDisciplina() {
		return nomeDisciplina;
	}
	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public int getPercentagem() {
		return percentagem;
	}
	public void setPercentagem(int percentagem) {
		this.percentagem = percentagem;
	}
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
}
