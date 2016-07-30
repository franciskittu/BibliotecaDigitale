package it.biblio.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import it.biblio.framework.result.FailureResult;
import it.biblio.model.Opera;
import it.biblio.model.impl.DataLayerImpl;

public class Ricerca extends HttpServlet {

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		DataSource ds = (DataSource) getServletContext().getAttribute("datasource");
		/*try {
			Connection connection = ds.getConnection();
			
			DataLayerImpl datalayer = new DataLayerImpl(connection);
*/
			String anno = request.getParameter("anno");
			String editore = request.getParameter("editore");
			String autore = request.getParameter("autore");
			String titolo = request.getParameter("titolo");
			String pubblicata = request.getParameter("pubblicata");
			String lingua = request.getParameter("lingua");
/*
			if (anno != null && editore != null && autore != null && titolo != null && pubblicata != null) {
				List<Opera> opere = new ArrayList<Opera>();
				Opera O = datalayer.creaOpera();
				O.setAnno(anno);
				O.setEditore(editore);
				O.setTitolo(titolo);
				O.setPubblicata((pubblicata.equals("true")) ? true : false);
				O.setLingua(lingua);
				opere = datalayer.getOpereByQuery(O);

			response.sendRedirect("Visualizza?richiesta=ricerca");
			} else {
				throw new Exception("campi ricerca non validi!");
			}
		} catch (Exception e) {
			FailureResult res = new FailureResult(getServletContext());
			res.activate(e.getMessage(), request, response);
		}
		*/
			response.sendRedirect("Visualizza?richiesta=ricerca&anno="+anno+"&editore="+editore+"&autore="+autore+"&lingua="+lingua+"&titolo="+titolo+"&pubblicata="+pubblicata);
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
