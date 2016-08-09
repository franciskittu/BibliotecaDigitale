package it.biblio.servlet;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

import it.biblio.framework.result.FailureResult;
import it.biblio.framework.result.TemplateResult;
import it.biblio.model.Opera;
import it.biblio.model.Pagina;
import it.biblio.model.impl.DataLayerImpl;
import it.biblio.utility.*;

@WebServlet(description = "gestisce l'upload delle immaggini acquisite", urlPatterns = { "/UploadImmagine" })
@MultipartConfig
public class UploadImmagine extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(name = "jdbc/bibliodb")
	private DataSource ds;

	private String getFileName(final Part part) {
		final String partHeader = part.getHeader("content-disposition");

		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}

	/**
	 * Funzione che gestisce la richiesta AJAX
	 * 
	 * @param datalayer
	 * @param numero
	 * @param b
	 * @return
	 */
	private String checkNumeroPagina(DataLayerImpl datalayer, String numero, long opera) {
		List<Pagina> pagine = datalayer.getPagineOpera(opera);
		if (pagine != null) {
			for (Pagina pagina : pagine) {
				if (pagina.getNumero().equals(numero)) {
					return "false";
				}
			}
		}
		return "true";
	}

	private void gestisciUpload(DataLayerImpl datalayer, HttpServletRequest request, HttpServletResponse response) throws ErroreBiblioteca, IOException, ServletException {
		final long id_opera = Long.parseLong(request.getParameter("opera"));
		final Part filePart = request.getPart("fileToUpload");
		final String nomeFile = getFileName(filePart);
		final String path = getServletContext().getRealPath(File.separator) + "immagini-opere";
		final String numero = (String) request.getParameter("numero_pagina");

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new ErroreBiblioteca("Algoritmo per il message digest non valido!");
		}
		OutputStream out = null;
		InputStream in = null;
		try {

			String sdigest = "";
			out = new FileOutputStream(new File(path + File.separator + nomeFile));
			in = filePart.getInputStream();

			int read = 0;
			final byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
				md.update(bytes, 0, read);
			}
			
			//covertiamo il digest in una stringa
	        byte[] digest = md.digest();
	        for (byte b : digest) {
	            sdigest += String.valueOf(b);
	        }
	        
	        if(sdigest.equals("")){
	        	throw new ErroreBiblioteca("message digest non calcolato correttamente");
	        }
	        

			Opera O = datalayer.getOpera(id_opera);
			// se l'opera non ha ancora un acquisitore l'utente attuale gli sarà
			// attribuito
			if (O.getAcquisitore() == null) {
				O.setAcquisitore(datalayer.getUtente((Long)request.getAttribute("userid")));
				O = datalayer.aggiornaOpera(O);
			}
			Pagina P = datalayer.creaPagina();
			P.setOpera(O);
			P.setPathImmagine(getServletContext().getContextPath().substring(1) + File.separator +"immagini-opere" + File.separator + nomeFile);
			P.setUploadImmagine(new Timestamp(Calendar.getInstance().getTime().getTime()));
			P.setNumero(numero);
			datalayer.aggiungiPagina(P);

		} catch (Exception ex) {
			throw new ErroreBiblioteca(ex.getMessage());
		} finally {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}

		}
		response.sendRedirect("Visualizza?richiesta=upload");
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		
		try {
			/**
			 * Connessione al db
			 */
			Connection connection = ds.getConnection();
			/**
			 * Oggetto DAO
			 */
			DataLayerImpl datalayer = new DataLayerImpl(connection);

			if (request.getParameter("numeroAJAX") != null) {
				Map template_data = new HashMap();
				String pagina = request.getParameter("numeroAJAX");
				String opera = request.getParameter("operaAJAX");
				String ris = checkNumeroPagina(datalayer, pagina, Long.parseLong(opera));
				template_data.put("outline_tpl", "");
				template_data.put("risultato", ris);
				/* chiama il template per l'oggetto JSON */
				TemplateResult tr = new TemplateResult(getServletContext());
				tr.activate("controlloAjax.ftl.json", template_data, response);
			} else {
				gestisciUpload(datalayer, request, response);
				//new Visualizza(ds).processRequest(request, response);
			}
		} catch (SQLException e) {
			FailureResult res = new FailureResult(getServletContext());
			res.activate(e.getMessage(), request, response);
		} catch (ErroreBiblioteca e) {
			FailureResult res = new FailureResult(getServletContext());
			res.activate(e.getMessage(), request, response);
		}

		
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadImmagine() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
