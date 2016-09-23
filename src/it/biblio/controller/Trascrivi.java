package it.biblio.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.biblio.data.model.BibliotecaDataLayer;
import it.biblio.data.model.Opera;
import it.biblio.data.model.Pagina;
import it.biblio.framework.data.DataLayerException;
import it.biblio.framework.result.TemplateManagerException;
import it.biblio.framework.result.TemplateResult;
import it.biblio.framework.utility.SecurityLayer;

/**
 * Servlet per l'inserimento della trascrizione in formato TEI nel sistema.
 * 
 * @author Marco D'Ettorre
 * @author Francesco Proietti
 */
@WebServlet(name="Trascrivi", urlPatterns={"/Trascrivi"})
public class Trascrivi extends BibliotecaBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -910206917014384364L;

	/**
	 * funzione che trasforma la trascrizione della pagina in input in formato TEI.
	 * 
	 * @param request servlet request
	 * @param response servlet response
	 * @param p pagina di riferimento
	 * @return testo TEI da inserire nel file
	 */
	private String input_to_tei(HttpServletRequest request, HttpServletResponse response, Pagina p){
		try{
			Opera o = p.getOpera();
			String titolo = p.getNumero();
			String editore= o.getEditore();
			String descrizione= o.getDescrizione();
			String testo= request.getParameter("testo");testo.split("\n");
			String intestazione = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+System.lineSeparator()
					+ "<?xml-model href=\"http://www.tei-c.org/release/xml/tei/custom/schema/relaxng/tei_lite.rng\" type=\"application/xml\" schematypens=\"http://relaxng.org/ns/structure/1.0\"?>"+System.lineSeparator()
					+ "<?xml-model href=\"http://www.tei-c.org/release/xml/tei/custom/schema/relaxng/tei_lite.rng\" type=\"application/xml\""+System.lineSeparator()
					+ "schematypens=\"http://purl.oclc.org/dsdl/schematron\"?>"+System.lineSeparator()
					+ "<TEI xmlns=\"http://www.tei-c.org/ns/1.0\">";
			String header = "<teiHeader><fileDesc><titleStmt><title>"+
					titolo+"</title></titleStmt><publicationStmt><p>"+
					editore+"</p></publicationStmt><sourceDesc><p>"+
					descrizione+"</p></sourceDesc></fileDesc></teiHeader>";
			String body = "<text><body>"+SecurityLayer.addSlashes(testo)+"</body></text></TEI>";
			return intestazione + System.lineSeparator() + header + System.lineSeparator() + body;
		}catch(DataLayerException ex){
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		}
		return null;
	}
	
	
	/**
	 * Crea e inserisce il file TEI nella base di dati.
	 * 
	 * @param request servlet request
	 * @param response servlet response
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws TemplateManagerException 
	 */
	private void action_memorizza_file(HttpServletRequest request, HttpServletResponse response, Pagina p) throws FileNotFoundException, IOException, TemplateManagerException{
		final String path = getServletContext().getInitParameter("system.directory_trascrizioni");
		final String contenuto_file_tei = input_to_tei(request, response, p);
		Boolean successo = true;
		try(BufferedReader in = new BufferedReader(new StringReader(contenuto_file_tei));
				PrintWriter out = new PrintWriter(new BufferedWriter(
								new OutputStreamWriter(
										new FileOutputStream(path + File.separator + p.getOpera().getTitolo().replace(' ', '_') + p.getNumero()+".xml")
										,"UTF-8")
							)
						)
				){
			String s;
			while( (s=in.readLine()) != null){
				out.println(s);
			}
			
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");
			Opera O = datalayer.getOpera(p.getOpera().getID());
			// se l'opera non ha ancora un acquisitore l'utente attuale gli sarà
			// attribuito
			if (O.getTrascrittore() == null) {
				O.setTrascrittore(datalayer.getUtente((Long)request.getAttribute("userid")));
				datalayer.aggiornaOpera(O);
			}
			p.setPathTrascrizione(path + File.separator + p.getOpera().getTitolo().replace(' ', '_') + p.getNumero()+".xml");
			
			datalayer.aggiornaPagina(p);
			request.setAttribute("risultato", successo.toString());
			request.setAttribute("outline_tpl", "");
			
			TemplateResult tr = new TemplateResult(getServletContext());
			tr.activate("controlloAjax.ftl.json", request, response);
			
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
			action_memorizza_file(request,response, p);
		} catch(DataLayerException ex){
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
