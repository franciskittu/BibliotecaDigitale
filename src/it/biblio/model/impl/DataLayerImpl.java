package it.biblio.model.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import it.biblio.model.Commenta;
import it.biblio.model.DataLayer;
import it.biblio.model.Opera;
import it.biblio.model.Pagina;
import it.biblio.model.Privilegi;
import it.biblio.model.Ruolo;
import it.biblio.model.Utente;

public class DataLayerImpl implements DataLayer {

	private final PreparedStatement aUtente, gUtente,gUtenteUsername;
	private final PreparedStatement aRuolo, gRuolo, gRuoloNome,gRuoliUtente;
	private final PreparedStatement gPrivilegi, aPrivilegi, rPrivilegiUtente;
	private final PreparedStatement gOpera, aOpera, aggiornaOpera,gOpereInTrascrizioneByUtente;
	private final PreparedStatement gPagina, aPagina, gPagineOpera;
	private final PreparedStatement gCommenta, aCommenta;
	
	private Statement gOpereByQuery;
	
	public DataLayerImpl(Connection c) throws SQLException {
		aUtente = c.prepareStatement("INSERT INTO Utente(username,password,email,nome,cognome) VALUES (?,?,?,?,?) RETURNING ID");
		gUtente = c.prepareStatement("SELECT * FROM Utente WHERE id = ?");
		gUtenteUsername = c.prepareStatement("SELECT * FROM Utente WHERE username = ?");
		aRuolo = c.prepareStatement("INSERT INTO Ruolo(nome, descrizione) VALUES (?,?) RETURNING progressivo");
		gRuolo = c.prepareStatement("SELECT * FROM Ruolo WHERE id = ?");
		gRuoloNome = c.prepareStatement("SELECT * FROM Ruolo WHERE nome = ?");
		gRuoliUtente = c.prepareStatement("SELECT Ruolo.* FROM Ruolo,Utente,Privilegi WHERE username = ? AND Utente.id = Privilegi.utente AND Privilegi.ruolo = Ruolo.id");
		gPrivilegi = c.prepareStatement("SELECT * FROM Privilegi WHERE id = ?");
		aPrivilegi = c.prepareStatement("INSERT INTO Privilegi(utente,ruolo) VALUES(?,?) RETURNING ID");
		rPrivilegiUtente = c.prepareStatement("DELETE FROM privilegi WHERE utente = ?");
		gOpera = c.prepareStatement("SELECT * FROM Opera WHERE id = ?");
		aOpera = c.prepareStatement("INSERT INTO Opera(titolo,lingua,anno,editore,descrizione,pubblicata) VALUES(?,?,?,?,?,?) RETURNING ID");
		aggiornaOpera = c.prepareStatement("UPDATE Opera SET titolo = ?, lingua = ?, anno = ?, editore = ?, descrizione = ?, pubblicata = ?");
		gPagina = c.prepareStatement("SELECT * FROM Pagina WHERE id = ?");
		aPagina = c.prepareStatement("INSERT INTO Pagina(numero,path_immagine,upload_immagine,immagine_validata,"
				+ "path_trascrizione,ultima_modifica_trascrizione,trascrizione_validata,opera) VALUES(?,?,?,?,?,?,?,?,?) RETURNING ID");
		gCommenta = c.prepareStatement("SELECT * FROM Commenta WHERE progressivo = ?");
		aCommenta = c.prepareStatement("");
		gOpereByQuery = c.createStatement();
		gOpereInTrascrizioneByUtente = c.prepareStatement("SELECT DISTINCT Opera.* FROM Opera,Pagina WHERE Pagina.utente= ? AND Opera.id = Pagina.opera");
		gPagineOpera = c.prepareStatement("SELECT * FROM Pagina WHERE opera = ?");
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
		Privilegi ris = null;
		ResultSet rs = null;
		try{
			gPrivilegi.setLong(1, progressivo);
			rs = gPrivilegi.executeQuery();
			if(rs.next()){
				ris = new PrivilegiImpl(this,rs);
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
		return ris;
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
		Opera ris = null;
		ResultSet rs = null;
		try{
			gOpera.setLong(1,id);
			rs = gOpera.executeQuery();
			if(rs.next()){
				ris = new OperaImpl(this, rs);
			}
		} catch (SQLException ex){
			java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
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
	public Opera aggiungiOpera(Opera O) {
		OperaImpl OI = (OperaImpl) O;
		ResultSet chiave = null;
		try{
			aOpera.setString(1, OI.getTitolo());
			aOpera.setString(2, OI.getLingua());
			aOpera.setString(3, OI.getAnno());
			aOpera.setString(4, OI.getEditore());
			aOpera.setString(5, OI.getDescrizione());
			aOpera.setBoolean(6, OI.getPubblicata());
			chiave = aOpera.executeQuery();
			if(chiave.next()){
				return getOpera(chiave.getLong("ID"));
			}
		} catch(SQLException ex){
			java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	
	@Override
	public List<Opera> getOpereByQuery(Opera O) {
		Opera OI = (OperaImpl) O; 
		List<Opera> ris = new ArrayList<Opera>();
		ResultSet rs = null;
		try{
			String query = "SELECT * FROM Opera WHERE pubblicata = "+OI.getPubblicata().toString();
			if(O.getID() != 0){
				query = query + " AND id = "+O.getID();
			}
			if(!O.getAnno().equals("")){
				query = query + " AND anno = '"+O.getAnno()+"'";
			}
			if(!O.getLingua().equals("")){
				query = query + " AND lingua = '"+O.getLingua()+"'";
			}
			if(!O.getEditore().equals("")){
				query = query + " AND editore LIKE '%"+O.getEditore()+"%'";
			}
			if(!O.getTitolo().equals("")){
				query = query + " AND titolo LIKE '%"+O.getTitolo()+"%'";
			}
			
			rs = gOpereByQuery.executeQuery(query);
			while(rs.next()){
				ris.add(new OperaImpl(this, rs));
			}
		} catch (SQLException ex){
			java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
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
	public Pagina creaPagina() {
		return new PaginaImpl(this);
	}

	@Override
	public Pagina getPagina(long id) {
		Pagina ris = null;
		ResultSet rs = null;
		try{
			gPagina.setLong(1,id);
			rs = gPagina.executeQuery();
			if(rs.next()){
				ris = new PaginaImpl(this, rs);
			}
		} catch (SQLException ex){
			java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
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
	public Pagina aggiungiPagina(Pagina P) {
		PaginaImpl PI = (PaginaImpl) P;
		ResultSet chiave = null;
		try{
			aPagina.setString(1, PI.getNumero());
			aPagina.setString(2,PI.getPathImmagine());
			aPagina.setTimestamp(3, PI.getUploadImmagine());
			aPagina.setBoolean(4, PI.getImmagineValidata());
			aPagina.setString(5, PI.getPathTrascrizione());
			aPagina.setTimestamp(6, PI.getUltimaModificaTrascrizione());
			aPagina.setBoolean(7, PI.getTrascrizioneValidata());
			aPagina.setLong(8, PI.getOpera().getID());
			chiave = aPagina.executeQuery();
			if(chiave.next()){
				return getPagina(chiave.getLong("ID"));
			}
		} catch(SQLException ex){
			java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
		} finally{
			try {
			chiave.close();
			} catch (SQLException e) {
				java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return null;
	}

	@Override
	public Commenta creaCommenta() {
		return new CommentaImpl(this);
	}

	@Override
	public Commenta getCommenta(long progressivo) {
		Commenta ris = null;
		ResultSet rs = null;
		try{
			gCommenta.setLong(1,progressivo);
			rs = gCommenta.executeQuery();
			if(rs.next()){
				ris = new CommentaImpl(this, rs);
			}
		} catch (SQLException ex){
			java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
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
	public Commenta aggiungiCommenta(Commenta C) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Opera aggiornaOpera(Opera O) {
		try{
			aggiornaOpera.setString(1, O.getTitolo());
			aggiornaOpera.setString(2, O.getLingua());
			aggiornaOpera.setString(3, O.getAnno());
			aggiornaOpera.setString(4, O.getEditore());
			aggiornaOpera.setString(5, O.getDescrizione());
			aggiornaOpera.setBoolean(6, O.getPubblicata());
			if(aggiornaOpera.executeUpdate() == 1){
				return getOpera(O.getID());
			}
		} catch(SQLException ex){
			java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@Override
	public Ruolo getRuoloByNome(String nome) {
		Ruolo ris = null;
		ResultSet rs = null;
		try{
			gRuoloNome.setString(1, nome);
			rs = gRuoloNome.executeQuery();
			if(rs.next()){
				ris = new RuoloImpl(this, rs);
			}
		} catch(SQLException ex){
			java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return null;
	}

	@Override
	public List<Ruolo> getListaRuoliUtente(Utente U) {
		List<Ruolo> ris = new ArrayList<Ruolo>();
		ResultSet rs = null;
		try{
			this.gRuoliUtente.setString(1, U.getUsername());
			rs = gRuoliUtente.executeQuery();
			while(rs.next()){
				ris.add(new RuoloImpl(this,rs));
			}
		}catch(SQLException ex){
			java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return ris;
	}

	@Override
	public List<Opera> getOpereInTrascrizioneByUtente(Utente U) {
		List<Opera> ris = new ArrayList<Opera>();
		ResultSet rs = null;
		try{
			this.gOpereInTrascrizioneByUtente.setLong(1, U.getID());
			this.gOpereInTrascrizioneByUtente.executeQuery();
			while(rs.next()){
				ris.add(new OperaImpl(this,rs));
			}
		}catch(SQLException ex){
			java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, ex);
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				java.util.logging.Logger.getLogger(DataLayerImpl.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return ris;
	}

	@Override
	public List<Pagina> getPagineOpera(long id_opera) {
		List<Pagina> ris = new ArrayList<Pagina>();
		ResultSet rs = null;
		try{
			this.gPagineOpera.setLong(1, id_opera);
			this.gPagineOpera.executeQuery();
			while(rs.next()){
				ris.add(new PaginaImpl(this,rs));
			}
		}catch(SQLException ex){
			//
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return null;
	}

}
