package it.biblio.model;

/**
* Interfaccia del Plain Old Java Object della Entità Commenta.
* Contiene metodi get e set per ogni attributo dell'entità.
* Transfer Object del pattern DAO
*  
* @author francesco
*
*/
public interface Commenta {

	long getProgressivo();
	
	Utente getUtente();
	void setUtente(Utente U);
	
	Pagina getTrascrizione();
	void setTrascrizione(Pagina T);
	
	String getTitolo();
	void setTitolo(String titolo);
	
	String getCommento();
	void setCommento(String commento);
}
