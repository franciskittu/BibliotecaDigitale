package it.biblio.model;

/**
 * Interfaccia del Plain Old Java Object della Entità Ruolo.
 * Contiene metodi get e set per ogni attributo dell'entità.
 * Transfer Object del pattern DAO
 *  
 * @author francesco
 *
 */
public interface Ruolo {

	long getID();
	
	String getNome();
	void setNome(String Nome);
	
	String getDescrizione();
	void setDescrizione(String descrizione);
}
