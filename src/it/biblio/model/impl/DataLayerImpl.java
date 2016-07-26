package it.biblio.model.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

import it.biblio.model.DataLayer;
import it.biblio.model.Opera;
import it.biblio.model.Privilegi;
import it.biblio.model.Ruolo;
import it.biblio.model.Utente;

public class DataLayerImpl implements DataLayer {

	private final PreparedStatement aUtente, gUtente,gUtenteUsername;
	private final PreparedStatement aRuolo, gRuolo;
	private final PreparedStatement gPrivilegi, aPrivilegi, rPrivilegiUtente;
	private final PreparedStatement gOpera, aOpera;
	
	public DataLayerImpl(Connection c) throws SQLException {
		aUtente = c.prepareStatement("INSERT INTO Utente(username,password,email,nome,cognome) VALUES (?,?,?,?,?) RETURNING ID");
		gUtente = c.prepareStatement("SELECT * FROM Utente WHERE id = ?");
		gUtenteUsername = c.prepareStatement("SELECT * FROM Utente WHERE username = ?");
		aRuolo = c.prepareStatement("INSERT INTO Ruolo(nome, descrizione) VALUES (?,?) RETURNING progressivo");
		gRuolo = c.prepareStatement("SELECT * FROM Ruolo WHERE id = ?");
		gPrivilegi = c.prepareStatement("SELECT * FROM Privilegi WHERE id = ?");
		aPrivilegi = c.prepareStatement("INSERT INTO Privilegi(utente,ruolo) VALUES(?,?) RETURNING ID");
		rPrivilegiUtente = c.prepareStatement("DELETE FROM privilegi WHERE utente = ?");
		gOpera = c.prepareStatement("SELECT * FROM Opera WHERE id = ?");
		aOpera = c.prepareStatement("INSERT INTO Opera(titolo,lingua,anno,editore,descrizione,pubblicata) VALUES(?,?,?,?,?,?) RETURNING ID");
		
		
	}

	@Override
	public Utente creaUtente() {
		return new UtenteImpl(this);
	}

	@Override
	public Utente aggiungiUtente(Utente U) {
		UtenteImpl U1 = (UtenteImpl) U;
		ResultSet chiave = null;
		try {
			aUtente.setString(1, U1.getUsername());
			aUtente.setString(2, U1.getPassword());
			aUtente.setString(3, U1.getEmail());
			aUtente.setString(4, U1.getNome());
			aUtente.setString(5, U1.getCognome());
			chiave = aUtente.executeQuery();
			if (chiave.next()) {
				return getUtente(chiave.getLong("id"));
			}
		} catch (SQLException ex) {
			System.out.println(ex.getSQLState()+ex.getMessage());
		} finally {
			try {
				chiave.close();
			} catch (SQLException ex) {
				//
			}
		}
		return null;
	}

	@Override
	public Utente getUtente(long id) {
		Utente ris = null;
		ResultSet rs = null;
		try {
			gUtente.setLong(1, id);
			rs = gUtente.executeQuery();
			if (rs.next()) {
				ris = new UtenteImpl(this, rs);
			}
		} catch (SQLException ex) {
			java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				rs.close();
			} catch (SQLException ex) {
				java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return ris;
	}

	@Override
	public Ruolo creaRuolo() {
		return new RuoloImpl(this);
	}

	@Override
	public Ruolo getRuolo(long progressivo) {
		Ruolo ris = null;
		ResultSet rs = null;
		try {
			gRuolo.setLong(1, progressivo);
			rs = gRuolo.executeQuery();
			if(rs.next()){
				ris = new RuoloImpl(this, rs);
			}
		} catch (SQLException e) {
			java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, e);
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return ris;
	}

	@Override
	public Ruolo aggiungiRuolo(Ruolo R) {
		RuoloImpl RI = (RuoloImpl) R;
		ResultSet chiave = null;
		try{
			aRuolo.setString(1, RI.getNome());
			aRuolo.setString(2, RI.getDescrizione());
			chiave = aRuolo.executeQuery();
			if(chiave.next()){
				return getRuolo(chiave.getLong("progressivo"));
			}
			
		} catch(SQLException ex){
			java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				chiave.close();
			} catch (SQLException e) {
				java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return null;
	}

	@Override
	public Utente getUtenteByUsername(String username) {
		Utente ris = null;
		ResultSet rs = null;
		try {
			gUtenteUsername.setString(1, username);
			rs = gUtenteUsername.executeQuery();
			if (rs.next()) {
				ris = new UtenteImpl(this, rs);
			}
		} catch (SQLException ex) {
			java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				rs.close();
			} catch (SQLException ex) {
				java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return ris;
	}

	@Override
	public Privilegi creaPrivilegio() {
		return new PrivilegiImpl(this);
	}

	@Override
	public Privilegi getPrivilegi(long progressivo) {
		Opera ris = null;
		ResultSet rs = null;
		try{
			gPrivilegi.setLong(1, progressivo);
			rs = gPrivilegi.executeQuery();
			if(rs.next()){
				ris = new OperaImpl(this,rs);
			}
		} catch(SQLException ex){
			java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
		} finally{
			try {
				rs.close();
			} catch (SQLException e) {
				java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return null;
	}

	@Override
	public Privilegi aggiungiPrivilegi(Privilegi P) {
		Privilegi PI = (PrivilegiImpl) P;
		ResultSet chiave = null;
		try{
			aPrivilegi.setLong(1, PI.getUtente().getID());
			aPrivilegi.setLong(2, PI.getRuolo().getID());
			chiave = aPrivilegi.executeQuery();
			if(chiave.next()){
				return getPrivilegi(chiave.getLong("progressivo"));
			}
		} catch(SQLException ex){
			java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
		}finally{
			try{
				chiave.close();
			}catch(SQLException ex){
				java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return null;
	}

	@Override
	public Privilegi rimuoviPrivilegiUtente(long id_utente) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Opera creaOpera() {
		return new OperaImpl(this);
		
	}

	@Override
	public Opera getOpera(long id) {
		return null;
	}

	@Override
	public Opera aggiungiOpera(Opera O) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Opera> getOpereByQuery(Opera P) {
		// TODO Auto-generated method stub
		return null;
	}

}
