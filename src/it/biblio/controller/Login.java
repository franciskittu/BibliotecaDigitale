package it.biblio.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import it.biblio.data.model.*;
import it.biblio.framework.data.DataLayerException;
import it.biblio.framework.result.FailureResult;
import it.biblio.framework.result.SplitSlashesFmkExt;
import it.biblio.framework.result.TemplateManagerException;
import it.biblio.framework.result.TemplateResult;
import it.biblio.framework.security.SecurityLayer;

/**
 * Servlet implementation class Login
 * 
 * @author francesco
 */
@WebServlet(description = "Verifica credenziali e reindirizzamento alla homepage utente", urlPatterns = { "/Login" })
public class Login extends BibliotecaBaseController {


	private void action_error(HttpServletRequest request, HttpServletResponse response) {
		if (request.getAttribute("exception") != null) {
			(new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request,
					response);
		} else {
			(new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request,
					response);
		}
	}
	
	private void action_login(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
		try {
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");
			Utente U = datalayer.getUtenteByUsername(SecurityLayer.addSlashes(request.getParameter("username")));

			if (U != null) {
				if (U.getPassword()
						.equals(SecurityLayer.criptaPassword(SecurityLayer.addSlashes(request.getParameter("password")),
								SecurityLayer.addSlashes(request.getParameter("username"))))) {
					// credenziali ok
					List<Ruolo> ruoli_utente = datalayer.getListaRuoliUtente(U);
					List<String> nomi_ruoli_utente = new ArrayList<String>();
					for (Ruolo ruolo : ruoli_utente) {
						nomi_ruoli_utente.add(ruolo.getNome());
					}
					HttpSession s = SecurityLayer.createSession(request, U.getUsername(), U.getID(), nomi_ruoli_utente);
				} else {
					// display dell'errore nella form di login
					request.setAttribute("errore_login", true);
				}
			} else {
				// display dell'errore nella form di login
				request.setAttribute("errore_login", true);
			}
			
			// fine processamento
			action_result(request,response);
			
		} catch (DataLayerException ex) {
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request,response);
		}
	}

	@Override
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		try {
			action_login(request, response);
		} catch (TemplateManagerException ex) {
			request.setAttribute("exception", ex);
            action_error(request, response);
		}

	}

}
