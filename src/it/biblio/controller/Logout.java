package it.biblio.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.biblio.framework.utility.SecurityLayer;

/**
 * Con questa server si effettua il logout invalidando la sessione esistente.
 * 
 * @author francesco
 *
 */
@WebServlet(name="Logout", urlPatterns={"/Logout"})
public class Logout extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1503993411303022678L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		SecurityLayer.disposeSession(request);
		response.sendRedirect("Home");
		 
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
