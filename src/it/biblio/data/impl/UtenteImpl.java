package it.biblio.data.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.biblio.data.model.*;
import it.biblio.framework.utility.SecurityLayer;

/**
 * Implementazionde del Plain Old Java Object relativo all'interfaccia Utente
 *  
 * @author francesco
 *
 */
public class UtenteImpl implements Utente {

	private long ID;
	private String username, password, nome, cognome, email;
	
	/**
	 * Oggetto DAO per l'accesso al DB.
	 */
	private BibliotecaDataLayerPgsqlImpl datalayer;
	
	/**
	 * Costruttore base che genera un oggetto "vuoto".
	 * @param datalayer
	 */
	public UtenteImpl(BibliotecaDataLayerPgsqlImpl datalayer){
		ID = 0;
		nome = cognome = username = password = email = "";
		this.datalayer = datalayer;
		
	}
	
	/**
	 * costruttore helper che costruisce l'oggetto dai risultati di una query
	 * 
	 * @param datalayer
	 * @param dati
	 * @throws SQLException
	 */
	public UtenteImpl(BibliotecaDataLayerPgsqlImpl datalayer, ResultSet dati) throws SQLException{
		ID = dati.getLong("ID");
		username = dati.getString("username");
		password = dati.getString("password");
		nome = dati.getString("nome");
		cognome = SecurityLayer.stripSlashes(dati.getString("cognome")) ;
		email = dati.getString("email");
		this.datalayer = datalayer;
	}
	
	@Override
	public long getID() {
		return ID;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
		
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
		
	}

	@Override
	public String getNome() {
		return nome;
	}

	@Override
	public void setNome(String nome) {
		this.nome = nome;
		
	}

	@Override
	public String getCognome() {
		return cognome;
	}

	@Override
	public void setCognome(String cognome) {
		this.cognome = cognome;
		
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
		
	}

}
