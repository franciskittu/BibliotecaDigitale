package it.biblio.model;

public interface DataLayer {

	Utente creaUtente();
	
	Utente getUtente(long id);
	
	Utente aggiungiUtente(Utente U);
	
	Ruolo creaRuolo();
	
	Ruolo getRuolo(long progressivo);
	
	Ruolo aggiungiRuolo(Ruolo R);
}
