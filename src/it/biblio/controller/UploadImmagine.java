package it.biblio.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import it.biblio.data.model.BibliotecaDataLayer;
import it.biblio.data.model.Opera;
import it.biblio.data.model.Pagina;
import it.biblio.framework.data.DataLayerException;
import it.biblio.framework.result.TemplateManagerException;
import it.biblio.framework.result.TemplateResult;
import it.biblio.framework.utility.ControllerException;

@WebServlet(name="UploadImmagine", description = "gestisce l'upload delle immaggini acquisite", urlPatterns = { "/UploadImmagine" })
@MultipartConfig
public class UploadImmagine extends BibliotecaBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2649569830853593682L;

	/**
	 * Ottiene il nome del file dal tipo Part
	 * 
	 * @param part File
	 * @return il nome del file se Part contiente il campo filename, <b>null</b> altrimenti
	 */
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
	 * @throws DataLayerException 
	 */
	private String checkNumeroPagina(BibliotecaDataLayer datalayer, String numero, long opera) throws DataLayerException {
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

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws TemplateManagerException 
	 */
	private void action_ajax(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
		try {
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");

			Map<String,Object> template_data = new HashMap<>();
			String pagina = request.getParameter("numeroAJAX");
			String opera = request.getParameter("operaAJAX");
			String ris = checkNumeroPagina(datalayer, pagina, Long.parseLong(opera));
			template_data.put("outline_tpl", "");
			template_data.put("risultato", ris);
			/* chiama il template per l'oggetto JSON */
			TemplateResult tr = new TemplateResult(getServletContext());
			tr.activate("controlloAjax.ftl.json", template_data, response);
		} catch (DataLayerException ex) {
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request,response);
		}
	}
	
	
	private void action_upload(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, ControllerException {
		BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");
		final long id_opera = Long.parseLong(request.getParameter("opera"));
		final Part filePart = request.getPart("fileToUpload");
		final String nomeFile = getFileName(filePart);
		final String path = getServletContext().getInitParameter("system.directory_immagini");
		final String numero = (String) request.getParameter("numero_pagina");
		
		
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new ControllerException("Algoritmo per il message digest non valido!");
		}
		
		try(OutputStream out = new FileOutputStream(new File(path + File.separator + nomeFile));
				InputStream in = filePart.getInputStream()) {

			String sdigest = "";

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
	        	throw new ControllerException("message digest non calcolato correttamente");
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
			P.setPathImmagine(path + File.separator + nomeFile);
			P.setUploadImmagine(new Timestamp(Calendar.getInstance().getTime().getTime()));
			P.setNumero(numero);
			P = datalayer.aggiungiPagina(P);
			//String url = "http://["+request.getLocalAddr()+"]:"+request.getLocalPort()+getServletContext().getContextPath() + File.separator + P.getPathImmagine();
			//System.out.println(url);
			//request.setAttribute("url", url);
			action_result(request,response);

		} catch (DataLayerException ex) {
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request,response);
		}
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		
		try {
			if (request.getParameter("numeroAJAX") != null) {
				action_ajax(request, response);
			} else {
				action_upload(request, response);
				
			}
		} catch (ControllerException | IOException | TemplateManagerException ex) {
			request.setAttribute("exception", ex);
            action_error(request, response);
		}

		
	}

}
