package it.biblio.framework.security;

public class ErroreBiblioteca extends Exception {

	public ErroreBiblioteca(String err){
		super(err);
	}
	public ErroreBiblioteca(){
		super("errore sconosciuto");
	}
}
