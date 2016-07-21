package it.biblio.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
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

public class addUtente extends HttpServlet {

	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
        try {
        	Connection connection = ds.getConnection();
			DataLayerImpl datalayer = new DataLayerImpl(connection);
			
			Utente U = datalayer.creaUtente();
			U.setCognome("Proietti");
			U.setEmail("franciskittu@gmail.com");
			U.setNome("Francesco");
			U.setPassword("ciaomamma");
			U.setUsername("franciskittu");
			
			Utente ris = datalayer.aggiungiUtente(U);
			
			Map template_data = new HashMap();
			template_data.put("nome", ris.getNome());
			template_data.put("outline_template", "");
			
			String html = "result.ftl.html";
			
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
