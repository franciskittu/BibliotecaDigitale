package it.biblio.model.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.biblio.model.Opera;
import it.biblio.model.Utente;

public class OperaImpl implements Opera{

	private long ID, id_trascrittore, id_acquisitore;
	private Integer numeropagine;
	private Boolean pubblicata;
	private String titolo,descrizione, lingua, anno, editore;
	private Utente trascrittore, acquisitore;
	
	/**
	 * Oggetto DAO per l'accesso al DB.
	 */
	private DataLayerImpl datalayer;
	
	/**
	 * Costruttore base che genera un oggetto "vuoto".
	 * @param datalayer DAO
	 */
	public OperaImpl(DataLayerImpl datalayer){
		titolo = descrizione = editore = lingua = anno = "";
		pubblicata = false;
		ID = id_acquisitore= id_trascrittore = numeropagine = 0;
		this.datalayer = datalayer;
	}
	
	/**
	 * Costruttore helper che costruisce l'oggetto dai risultati di una query
	 * 
	 * @param datalayer DAO
	 * @param dati record del DB contenente gli attributi dell'oggetto.
	 * @throws SQLException
	 */
	public OperaImpl(DataLayerImpl datalayer, ResultSet dati) throws SQLException{
		pubblicata = dati.getBoolean("pubblicata");
		ID = dati.getLong("ID");
		titolo = dati.getString("titolo");
		descrizione = dati.getString("descrizione");
		editore = dati.getString("editore");
		lingua = dati.getString("lingua");
		anno = dati.getString("anno");
		id_trascrittore = dati.getLong("trascrittore");
		id_acquisitore = dati.getLong("acquisitore");
		numeropagine = dati.getInt("numero_pagine");
		this.datalayer = datalayer;
	}
	
	@Override
	public long getID() {
		return ID;
	}

	@Override
	public String getTitolo() {
		return titolo;
	}

	@Override
	public void setTitolo(String titolo) {
		this.titolo = titolo;
		
	}

	@Override
	public String getLingua() {
		return lingua;
	}

	@Override
	public void setLingua(String lingua) {
		this.lingua = lingua;
		
	}

	@Override
	public String getAnno() {
		return anno;
	}

	@Override
	public void setAnno(String aaaa) {
		this.anno = anno;
	}

	@Override
	public String getEditore() {
		return editore;
	}

	@Override
	public void setEditore(String editore) {
		this.editore = editore;
	}

	@Override
	public String getDescrizione() {
		return descrizione;
	}

	@Override
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
		
	}

	@Override
	public Boolean getPubblicata() {
		return pubblicata;
	}

	@Override
	public void setPubblicata(Boolean b) {
		this.pubblicata = b;
	}
	
	@Override
	public Utente getTrascrittore() {
		if(this.trascrittore == null){
			this.trascrittore = datalayer.getUtente(this.id_trascrittore);
		}
		return this.trascrittore;
	}

	@Override
	public void setTrascrittore(Utente T) {
		this.trascrittore = T;
		
	}

	@Override
	public Utente getAcquisitore() {
		if(this.acquisitore == null){
			this.acquisitore = datalayer.getUtente(this.id_acquisitore);
		}
		return this.acquisitore;
	}

	@Override
	public void setAcquisitore(Utente A) {
		this.acquisitore = A;
		
	}

	@Override
	public Integer getNumeroPagine() {
		return this.numeropagine;
	}

	@Override
	public void setNumeroPagine(Integer numeropagine) {
		this.numeropagine = numeropagine;
		
	}

}
