package it.biblio.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import it.biblio.data.impl.BibliotecaDataLayerPgsqlImpl;
import it.biblio.data.model.BibliotecaDataLayer;
import it.biblio.framework.result.*;

/**
 * servlet astratta che permette di inizializzare la connessione al DB.
 * Implementata da tutte le servlet che vogliono accedere al DB.
 * 
 * @author francesco
 *
 */
public abstract class BibliotecaBaseController extends HttpServlet {

	private static final long serialVersionUID = 8021980007988958087L;
	
	@Resource(name = "jdbc/bibliodb")
	private DataSource ds;
	
	
	
	protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException;
	
	protected void action_result(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException{
		TemplateResult res = new TemplateResult(getServletContext());
		request.setAttribute("strip_slashes", new SplitSlashesFmkExt() );
		res.activate("", request, response);
	}
	private void processBaseRequest(HttpServletRequest request, HttpServletResponse response){
		try(BibliotecaDataLayer datalayer = new BibliotecaDataLayerPgsqlImpl(ds)){
			datalayer.init();
			request.setAttribute("datalayer", datalayer);
			processRequest(request, response);
		}catch(Exception ex){
			ex.printStackTrace();
			(new FailureResult(getServletContext())).activate(
					(ex.getMessage() != null || ex.getCause() != null) ? ex.getMessage() : ex.getCause().getMessage() , request, response);
		}
	}
	
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
        processBaseRequest(request, response);
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
        processBaseRequest(request, response);
    }

}
