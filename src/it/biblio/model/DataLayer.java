package it.biblio.model;

public interface DataLayer {

	Utente creaUtente();
	
	Utente getUtente(long id);
	
	Utente aggiungiUtente(Utente U);
}
