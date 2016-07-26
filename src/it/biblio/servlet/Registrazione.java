package it.biblio.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import it.biblio.framework.result.FailureResult;
import it.biblio.framework.result.TemplateResult;
import it.biblio.model.Utente;
import it.biblio.model.impl.DataLayerImpl;
import it.biblio.utility.SecurityLayer;

public class Registrazione extends HttpServlet {
	
	/**
	 * Metodo usato per chiamata AJAX per la verifica
	 * di username duplicati
	 * 
	 * @param datalayer DAO
	 * @param campousername valore username appena inserito
	 * @return stringa "false" se lo username non è presente nel DB, "true" altrimenti 
	 */
	private String checkUsername(DataLayerImpl datalayer, String campousername){
			if (datalayer.getUtenteByUsername(campousername)==null){
				return "false";
			}
			return "true";
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
		DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        
		try {
        	Connection connection = ds.getConnection();
			DataLayerImpl datalayer = new DataLayerImpl(connection);
			Map template_data = new HashMap();
			String html;
		
			/*gestione richiesta AJAX*/
			if (request.getParameter("usernameAjax")!=null){
				String ris=checkUsername(datalayer, request.getParameter("usernameAjax"));
				template_data.put("outline_tpl", "");
				template_data.put("risultato", ris);
				TemplateResult tr = new TemplateResult(getServletContext());
				html = "controlloRegistrazione.ftl.json";
			}
			/*gestione richiesta registrazione*/
			else {
				/*il check dei campi è fatto tramite javascript, da implementare qui nella prossima revisione*/
				String cognome = request.getParameter("cognome");
				String nome = request.getParameter("nome");
				String email = request.getParameter("email");
				String password = request.getParameter("password");
				String username = request.getParameter("username");
				
				if(username != null){
					Utente U = datalayer.creaUtente();
					U.setCognome(SecurityLayer.addSlashes(cognome));
					U.setEmail(SecurityLayer.addSlashes(email));
					U.setNome(SecurityLayer.addSlashes(nome));
					U.setPassword(SecurityLayer.criptaPassword(SecurityLayer.addSlashes(password), SecurityLayer.addSlashes(username)));
					U.setUsername(SecurityLayer.addSlashes(username));
						
					Utente ris = datalayer.aggiungiUtente(U);
					template_data.put("nome", SecurityLayer.stripSlashes(ris.getNome()));
				}
				
				
				html = "result.ftl.html";
				
				
			}
			
			/*chiama il template per il rendering corretto*/
			TemplateResult tr = new TemplateResult(getServletContext());
			tr.activate(html, template_data, response);
		
        } catch (SQLException e) {
			FailureResult res = new FailureResult(getServletContext());
            res.activate(e.getMessage(), request, response);
		}
        
	}
	
	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
