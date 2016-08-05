package it.biblio.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import it.biblio.framework.result.FailureResult;
import it.biblio.framework.result.TemplateResult;
import it.biblio.model.Utente;
import it.biblio.model.impl.DataLayerImpl;
import it.biblio.utility.SecurityLayer;

public class Registrazione extends HttpServlet {
	
	
	@Resource(name = "jdbc/bibliodb") //richiamo alla resource-ref del deployment descriptor
	private DataSource ds;
	
	/**
	 * Metodo per il controllo della correttezza dei parametri
	 * della form di registrazione.
	 * 
	 * @param servlet request per i parametri
	 * @return true se il controllo passa, false altrimenti
	 */
	private Boolean checkParametriRegistrazione(HttpServletRequest request){
		//da implementare in futuro
		return true;
	}
	
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
            throws ServletException, IOException, SQLException {
		
		HttpSession s;
		try {
        	Connection connection = ds.getConnection();
			DataLayerImpl datalayer = new DataLayerImpl(connection);
			Map template_data = new HashMap();
		
			/*gestione richiesta AJAX*/
			if (request.getParameter("usernameAjax")!=null){
				String ris=checkUsername(datalayer, request.getParameter("usernameAjax"));
				template_data.put("outline_tpl", "");
				template_data.put("risultato", ris);
				/*chiama il template per l'oggetto JSON*/
				TemplateResult tr = new TemplateResult(getServletContext());
				tr.activate("controlloRegistrazione.ftl.json", template_data, response);
			}
			/*gestione richiesta registrazione*/
			else {
				/*il check dei campi è fatto tramite javascript, da implementare qui nella prossima revisione*/
				if(!checkParametriRegistrazione(request)){
					throw new Exception("Controllo parametri fallito!");
				}
				/*prende i campi della form*/
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
						
					Utente ris = datalayer.aggiungiUtente(U);//inserimento nel DB
					/*crea sessione*/
					if(ris != null){
						s = SecurityLayer.createSession(request, ris.getUsername(), ris.getID(), null);//i ruoli vengono aggiunti in seguito dall'admin
					}else{
						throw new Exception("inserimento utente nel DB non avvenuto correttamente!");
					}
					
					/*homepage dopo login*/
					response.sendRedirect("Visualizza?richiesta=login");
				}				
				
			}
			
			
		
        } catch (Exception e) {
			FailureResult res = new FailureResult(getServletContext());
            res.activate(e.getMessage(), request, response);
		}finally{
			ds.getConnection().close();
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
        try {
			processRequest(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
        try {
			processRequest(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
