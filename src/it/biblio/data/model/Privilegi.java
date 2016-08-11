package it.biblio.data.model;

import it.biblio.framework.data.DataLayerException;

public interface Privilegi {
	
	long getProgressivo() throws DataLayerException;
	
	Utente getUtente()throws DataLayerException;
	void setUtente(Utente U)throws DataLayerException;
	
	Ruolo getRuolo()throws DataLayerException;
	void setRuolo(Ruolo R)throws DataLayerException;

}
