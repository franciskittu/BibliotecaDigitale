package it.biblio.model;

public interface Privilegi {
	
	long getProgressivo();
	
	Utente getUtente();
	void setUtente(Utente U);
	
	Ruolo getRuolo();
	void setRuolo(Ruolo R);

}
