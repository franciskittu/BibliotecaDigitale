package it.biblio.model.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.biblio.model.DataLayer;
import it.biblio.model.Utente;

public class DataLayerImpl implements DataLayer {

	private final PreparedStatement aUtente, gUtente;

	public DataLayerImpl(Connection c) throws SQLException {
		aUtente = c.prepareStatement("INSERT INTO Utente(username,password,email,nome,cognome) VALUES (?,?,?,?,?) RETURNING ID");
		gUtente = c.prepareStatement("SELECT * FROM Utente WHERE id = ?");
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
			aUtente.setString(3, U1.getPassword());
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

		} finally {
			try {
				rs.close();
			} catch (SQLException ex) {

			}
		}
		return ris;
	}

}
