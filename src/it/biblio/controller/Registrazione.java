package it.biblio.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import it.biblio.data.impl.BibliotecaDataLayerPgsqlImpl;
import it.biblio.data.model.BibliotecaDataLayer;
import it.biblio.data.model.Utente;
import it.biblio.framework.data.DataLayerException;
import it.biblio.framework.result.FailureResult;
import it.biblio.framework.result.TemplateManagerException;
import it.biblio.framework.result.TemplateResult;
import it.biblio.framework.utility.ControllerException;
import it.biblio.framework.utility.SecurityLayer;

@WebServlet(name="Registrazione", urlPatterns={"/Registrazione"})
public class Registrazione extends BibliotecaBaseController {

	/**
	 * Metodo per il controllo della correttezza dei parametri della form di
	 * registrazione.
	 * 
	 * @param servlet
	 *            request per i parametri
	 * @return true se il controllo passa, false altrimenti
	 */
	private Boolean checkParametriRegistrazione(HttpServletRequest request) {
		// da implementare in futuro
		return true;
	}

	/**
	 * Azione intrapresa per il controllo del campo username durante la
	 * compilazione della form. Il risultato del controllo sarà inviato sotto
	 * forma di stringa in formato JSON.
	 * 
	 * @param request
	 * @param response
	 * @throws TemplateManagerException
	 */
	private void action_ajax(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
		try {
			Map<String, Object> template_data = new HashMap<String, Object>();

			String ris = checkUsername((BibliotecaDataLayer) request.getAttribute("datalayer"),
					request.getParameter("usernameAjax"));
			template_data.put("outline_tpl", "");
			template_data.put("risultato", ris);
			/* chiama il template per l'oggetto JSON */
			TemplateResult tr = new TemplateResult(getServletContext());
			tr.activate("controlloAjax.ftl.json", template_data, response);
		} catch (DataLayerException ex) {
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		}
	}

	private void action_registration(HttpServletRequest request, HttpServletResponse response)
			throws ControllerException, IOException {
		try {
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");

			/* prende i campi della form */
			String cognome = request.getParameter("cognome");
			String nome = request.getParameter("nome");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String username = request.getParameter("username");

			Utente U = datalayer.creaUtente();
			U.setCognome(SecurityLayer.addSlashes(cognome));
			U.setEmail(SecurityLayer.addSlashes(email));
			U.setNome(SecurityLayer.addSlashes(nome));
			U.setPassword(SecurityLayer.criptaPassword(SecurityLayer.addSlashes(password),
					SecurityLayer.addSlashes(username)));
			U.setUsername(SecurityLayer.addSlashes(username));

			Utente ris = datalayer.aggiungiUtente(U);// inserimento nel
														// DB
			/* crea sessione */
			if (ris != null) {
				SecurityLayer.createSession(request, ris.getUsername(), ris.getID(), null);// i
																							// ruoli
																							// vengono
																							// aggiunti
																							// in
																							// seguito
																							// dall'admin
			} else {
				throw new ControllerException("inserimento utente nel DB non avvenuto correttamente!");
			}

			/* homepage dopo login */
			response.sendRedirect("Home");// mando un redirect poichè è necessario ricaricare la pagina
		} catch (DataLayerException ex) {
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		}
	}

	/**
	 * Metodo usato per chiamata AJAX per la verifica di username duplicati
	 * 
	 * @param datalayer
	 *            DAO
	 * @param campousername
	 *            valore username appena inserito
	 * @return stringa "false" se lo username non è presente nel DB, "true"
	 *         altrimenti
	 * @throws DataLayerException
	 */
	private String checkUsername(BibliotecaDataLayer datalayer, String username) throws DataLayerException {

		if (datalayer.getUtenteByUsername(username) == null) {
			return "false";
		}
		return "true";
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			/* gestione richiesta AJAX */
			if (request.getParameter("usernameAjax") != null) {
				action_ajax(request, response);
			}
			/* gestione richiesta registrazione */
			else {
				/*
				 * il check dei campi è fatto tramite javascript, da
				 * implementare qui nella prossima revisione
				 */
				if (!checkParametriRegistrazione(request)) {
					throw new ControllerException("Controllo parametri fallito!");
				}
				action_registration(request, response);

			}

		} catch (IOException | ControllerException | TemplateManagerException ex) {
			request.setAttribute("exception", ex);
			action_error(request, response);
		}

	}

}
