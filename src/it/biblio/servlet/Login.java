package it.biblio.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import it.biblio.framework.result.FailureResult;
import it.biblio.framework.result.TemplateResult;
import it.biblio.model.Ruolo;
import it.biblio.model.Utente;
import it.biblio.model.impl.DataLayerImpl;
import it.biblio.utility.SecurityLayer;

/**
 * Servlet implementation class Login
 */
@WebServlet(description = "Verifica credenziali e reindirizzamento alla homepage utente", urlPatterns = { "/Login" })
public class Login extends HttpServlet {


	 @Resource(name = "jdbc/bibliodb") //reference to the resource-ref in the deployment descriptor
	 private DataSource ds;
	 
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			/**
			 * ottenimento connessione dal pool di connessioni
			 */
			Connection connection = ds.getConnection();
			
			/**
			 * DAO per effettuare operazioni sul DB
			 */
			DataLayerImpl datalayer = new DataLayerImpl(connection);

			Utente U = datalayer.getUtenteByUsername(request.getParameter("username"));

			if (U != null) {
				if (U.getPassword()
						.equals(SecurityLayer.criptaPassword(SecurityLayer.addSlashes(request.getParameter("password")),
								SecurityLayer.addSlashes(request.getParameter("username"))))) {
					List<Ruolo> ruoli_utente = datalayer.getListaRuoliUtente(U);
					List<String> nomi_ruoli_utente = new ArrayList<String>();
					for(Ruolo ruolo : ruoli_utente){
						nomi_ruoli_utente.add(ruolo.getNome());
					}
					
					HttpSession s = SecurityLayer.createSession(request, U.getUsername(), U.getID(), nomi_ruoli_utente);
					response.sendRedirect("Visualizza?richiesta=login");
					/*request.setAttribute("richiesta","login");
					new Visualizza(ds).processRequest(request, response);*/
					
				} else {
					response.sendRedirect("Visualizza?errore=login");
					/*request.setAttribute("errore", "login");
					new Visualizza(ds).processRequest(request, response);*/
				}
			} else {
				response.sendRedirect("Visualizza?errore=login");
				/*request.setAttribute("errore", "login");
				new Visualizza(ds).processRequest(request, response);*/
			}

		} catch (SQLException e) {
			FailureResult res = new FailureResult(getServletContext());
			res.activate(e.getMessage(), request, response);
		}

	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on
	// the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

}
