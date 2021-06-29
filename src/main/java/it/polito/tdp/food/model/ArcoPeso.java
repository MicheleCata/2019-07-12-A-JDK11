package it.polito.tdp.food.model;

public class ArcoPeso {
	
	private Food f;
	private Double peso;
	
	public ArcoPeso(Food f, Double peso) {
		super();
		this.f = f;
		this.peso = peso;
	}

	public Food getF() {
		return f;
	}

	public void setF(Food f) {
		this.f = f;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
	public String toString() {
		return this.f+" "+ this.peso;
	}
	
	
	

}
