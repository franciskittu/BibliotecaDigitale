package it.biblio.servlet;

import java.io.IOException;
import java.sql.*;
import java.util.logging.*;

import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

import it.biblio.framework.result.HTMLResult;

public class EsempioDS extends HttpServlet {

	
	private void action_query(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HTMLResult result = new HTMLResult(getServletContext());
        result.setTitle("Query executor with JDBC connection pooling");
        result.appendToBody("<form method='get' action='EsempioDS'>");
        result.appendToBody("<p>Write an SQL query: <input type='text' name='query'/>");
        result.appendToBody("<input type='submit' name='submit' value='execute'/></p>");
        result.appendToBody("</form>");
        result.activate(request, response);
    }
	
	private void action_error(HttpServletRequest request, HttpServletResponse response) {
        //assumiamo che l'eccezione sia passata tramite gli attributi della request
        //we assume that the exception has been passed using the request attributes
        Exception exception = (Exception) request.getAttribute("exception");
        String message;
        if (exception != null && exception.getMessage() != null) {
            message = exception.getMessage();
        } else {
            message = "Unknown error";
        }
        HTMLResult result = new HTMLResult(getServletContext());
        result.setTitle("ERROR");
        result.setBody("<p>" + message + "</p>");
        try {
            result.activate(request, response);
        } catch (IOException ex) {
            //if error page cannot be sent, try a standard HTTP error message
            //se non possiamo inviare la pagina di errore, proviamo un messaggio di errore HTTP standard
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
            } catch (IOException ex1) {
                //if ALSO this error status cannot be notified, write to the server log
                //se ANCHE questo stato di errore non può essere notificato, scriviamo sul log del server
                Logger.getLogger(EsempioDS.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
	
	private void action_results(HttpServletRequest request, HttpServletResponse response, String query) throws SQLException, IOException, NamingException {
        int i;
        Statement s = null;
        ResultSet rs = null;
        Connection connection = null;

        HTMLResult result = new HTMLResult(getServletContext());
        result.setTitle("Query executor results");
        result.appendToBody("<p><b>Query:</b> " + query + "</p>");
        try {
        	DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
            connection = ds.getConnection();

            //eseguiamo la query
            //query execution
            s = connection.createStatement();
            rs = s.executeQuery(query);

            //in questo esempio, non conosciamo la struttura
            //dei record risultanti dalla query, quindi la esploriamo
            //utilizzando i metadati associati al ResultSet
            //in this particular example, we don't know the structure of the
            //returned records, so we use the ResultSet metadata to explore them
            ResultSetMetaData rsm = rs.getMetaData();

            result.appendToBody("<table border = '1'>");
            result.appendToBody("<tr>");
            for (i = 1; i <= rsm.getColumnCount(); ++i) {
                String cname = rsm.getColumnName(i);
                String clabel = rsm.getColumnLabel(i);
                String qcname = (rsm.getTableName(i).isEmpty()) ? cname : (rsm.getTableName(i) + "." + cname);
                String header = (cname.equals(clabel)) ? qcname : (clabel + " (" + qcname + ")");
                result.appendToBody("<th>" + header + "</th>");
            }
            result.appendToBody("</tr>");

            //iteriamo sulle righe del risultato
            //iterate on the result rows
            while (rs.next()) {
                result.appendToBody("<tr>");
                //in questo caso non preleviamo le colonne
                //usando i loro nomi, ma le leggiamo tutte
                //tramite l'indice
                //in this case we address the columns using their index
                //otherwise we could use their name
                for (i = 1; i <= rsm.getColumnCount(); ++i) {
                    result.appendToBody("<td>" + (rs.getObject(i) != null ? rs.getObject(i).toString() : "<i>null</i>") + "</td>");
                }
                result.appendToBody("</tr>");
            }
            result.appendToBody("</table>");
            result.activate(request, response);
        } finally {
            //alla fine chiudiamo tutte le risorse, in ogni caso
            //finally, we close all the opened resources
            try {
                rs.close();
                s.close();
            } catch (Exception ex) {
                //ignoriamo ulteriori errori in fase di chiusura
                //ignore further exceptions when closing 
            }
            try {
                connection.close();
            } catch (Exception ex) {
                //ignoriamo ulteriori errori in fase di chiusura
                //ignore further exceptions when closing 
            }
        }
    }
	 protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException {

	        String query = request.getParameter("query");
	        try {
	            if (query != null && !query.isEmpty()) {
	                action_results(request, response, query);
	            } else {
	                action_query(request, response);
	            }
	        } catch (IOException ex) {
	            request.setAttribute("exception", ex);
	            action_error(request, response);
	        } catch (SQLException ex) {
	            request.setAttribute("exception", ex);
	            action_error(request, response);
	        } catch (NamingException ex) {
	            request.setAttribute("exception", ex);
	            action_error(request, response);
	        }
	    }
	
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
