package it.biblio.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.biblio.data.model.BibliotecaDataLayer;
import it.biblio.data.model.Opera;
import it.biblio.data.model.Pagina;
import it.biblio.framework.data.DataLayerException;

@WebServlet(name="Trascrivi", urlPatterns={"/Trascrivi"})
public class Trascrivi extends BibliotecaBaseController {

	/**
	 * 
	 * @param request
	 * @param response
	 * @param p
	 * @return
	 */
	private String input_to_tei(HttpServletRequest request, HttpServletResponse response, Pagina p){
		try{
			Opera o = p.getOpera();
			String titolo = p.getNumero();
			String editore= o.getEditore();
			String descrizione= o.getDescrizione();
			String testo= request.getParameter("testo");testo.split("\n");
			String intestazione = "<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>"
					+ "<?xml-model href=\\\"http://www.tei-c.org/release/xml/tei/custom/schema/relaxng/tei_lite.rng\\\" type=\\\"application/xml\\\" schematypens=\\\"http://relaxng.org/ns/structure/1.0\\\"?>"
					+ "<?xml-model href=\\\"http://www.tei-c.org/release/xml/tei/custom/schema/relaxng/tei_lite.rng\\\" type=\\\"application/xml\\\""
					+ "schematypens=\\\"http://purl.oclc.org/dsdl/schematron\\\"?>"
					+ "<TEI xmlns=\\\"http://www.tei-c.org/ns/1.0\\\">";
			String header = "<teiHeader><fileDesc><titleStmt><title>"+
					titolo+"</title></titleStmt><publicationStmt><p>"+
					editore+"</p></publicationStmt><sourceDesc><p>"+
					descrizione+"</p></sourceDesc></fileDesc></teiHeader>";
			String body = "<text><body><p>"+testo+"</p></body></text></TEI>";
			return intestazione + header + body;
		}catch(DataLayerException ex){
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void action_memorizza_file(HttpServletRequest request, HttpServletResponse response, Pagina p) throws FileNotFoundException, IOException{
		final String path = getServletContext().getInitParameter("system.directory_trascrizioni");
		
		try(PrintWriter out = new PrintWriter(new File(path + File.separator + p.getOpera().getTitolo() + p.getNumero()+".xml"))){
			out.println(input_to_tei(request, response, p));
		} catch (DataLayerException ex) {
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		}
	}
	
	@Override
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try{
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");
			Pagina p = datalayer.getPagina(Long.parseLong(request.getParameter("id_pagina")));
			
		} catch(DataLayerException ex){
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		}

	}

}
