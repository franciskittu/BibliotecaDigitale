package it.biblio.model.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.biblio.model.Privilegi;
import it.biblio.model.Ruolo;
import it.biblio.model.Utente;

public class PrivilegiImpl implements Privilegi {

	private long progressivo, id_utente, id_ruolo;
	private Utente utente;
	private Ruolo ruolo;
	
	private DataLayerImpl datalayer;
	
	public PrivilegiImpl(DataLayerImpl datalayer){
		progressivo = id_utente = id_ruolo = 0;
		this.datalayer = datalayer;
	}
	
	public PrivilegiImpl(DataLayerImpl datalayer, ResultSet dati) throws SQLException{
		progressivo = dati.getLong("progressivo");
		id_utente = dati.getLong("utente");
		id_ruolo = dati.getLong("ruolo");
		this.datalayer = datalayer;
		
	}
	
	@Override
	public long getProgressivo() {
		return progressivo;
	}

	/**
	 * Caricamento Lazy dell'oggetto
	 */
	@Override
	public Utente getUtente() {
		if(utente == null){
			utente = datalayer.getUtente(id_utente);
		}
		return utente;
	}

	@Override
	public void setUtente(Utente U) {
		utente = U;
	}

	@Override
	public Ruolo getRuolo() {
		if(ruolo == null){
			ruolo = datalayer.getRuolo(id_ruolo);
		}
		return ruolo;
	}

	@Override
	public void setRuolo(Ruolo R) {
		ruolo = R;

	}

}
