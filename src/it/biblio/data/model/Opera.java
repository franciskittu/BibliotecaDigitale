package it.biblio.data.model;

import it.biblio.framework.data.DataLayerException;

/**
* Interfaccia del Plain Old Java Object della Entità Utente.
* Contiene metodi get e set per ogni attributo dell'entità.
* Transfer Object del pattern DAO
*  
* @author francesco
*
*/
public interface Opera {

	long getID();
	
	String getTitolo();
	void setTitolo(String titolo);
	
	String getLingua();
	void setLingua(String lingua);
	
	String getAnno();
	void setAnno(String aaaa);
	
	String getEditore();
	void setEditore(String editore);
	
	String getDescrizione();
	void setDescrizione(String descrizione);
	
	Boolean getPubblicata();
	void setPubblicata(Boolean b);
	
	Utente getTrascrittore() throws DataLayerException;
	void setTrascrittore(Utente T);
	
	Utente getAcquisitore() throws DataLayerException;
	void setAcquisitore(Utente A);
	
	Integer getNumeroPagine();
	void setNumeroPagine(Integer numero_pagine);
}
