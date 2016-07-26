package it.biblio.model.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.biblio.model.Opera;

public class OperaImpl implements Opera{

	private long ID;
	private Boolean pubblicata;
	private String titolo,descrizione, lingua, anno, editore;
	
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
		ID = 0;
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

}
