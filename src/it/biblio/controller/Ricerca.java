package it.biblio.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.biblio.data.model.BibliotecaDataLayer;
import it.biblio.data.model.Opera;
import it.biblio.data.model.Pagina;
import it.biblio.data.model.Ruolo;
import it.biblio.data.model.Utente;
import it.biblio.framework.data.DataLayerException;
import it.biblio.framework.result.FailureResult;
import it.biblio.framework.result.SplitSlashesFmkExt;
import it.biblio.framework.result.TemplateManagerException;
import it.biblio.framework.result.TemplateResult;
import it.biblio.framework.utility.ParserTEI;
import it.biblio.framework.utility.SecurityLayer;

/**
 * Servlet che gestisce le richieste di selezione delle entit&agrave
 * presenti nella base di dati.
 * 
 * @author Marco D'Ettorre
 * @author Francesco Proietti
 */
@WebServlet(name="Ricerca", urlPatterns={"/Ricerca"})
public class Ricerca extends BibliotecaBaseController {

	/**
	 * campo per la serializzazione della classe.
	 * 
	 */
	private static final long serialVersionUID = 8446825403497077597L;

	@Override
	protected void action_error(HttpServletRequest request, HttpServletResponse response){
		try{
			(new TemplateResult(getServletContext())).activate("error.ftl.json", request,
					response);
			
		}catch(TemplateManagerException ex){
			request.setAttribute("exception", ex);
			(new FailureResult(getServletContext())).activate((Exception) request.getAttribute("exception"), request,
					response);
		}
	}
	/**
	 * Funzione di ricerca delle opere che restituisce un oggetto JSON.
	 * 
	 * @param request servlet request
	 * @param response servlet response
	 * @throws TemplateManagerException se occorre un errore nella logica del template manager
	 */
	private void action_ricerca_ajax(HttpServletRequest request, HttpServletResponse response)
			throws TemplateManagerException{
		try {
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");

			String anno = request.getParameter("anno");
			String editore = request.getParameter("editore");
			String autore = request.getParameter("autore");
			String titolo = request.getParameter("titolo");
			String immagini_pubblicate = request.getParameter("pubblicata");
			String lingua = request.getParameter("lingua");

			List<Opera> opere = new ArrayList<Opera>();
			Opera O = datalayer.creaOpera();
			O.setAutore(SecurityLayer.addSlashes(autore));
			O.setAnno(SecurityLayer.addSlashes(anno));
			O.setEditore(SecurityLayer.addSlashes(editore));
			O.setTitolo(SecurityLayer.addSlashes(titolo));
			O.setImmaginiPubblicate((immagini_pubblicate != null && immagini_pubblicate.equals("false")) ? false : true);
			O.setLingua(SecurityLayer.addSlashes(lingua));
			opere = datalayer.getOpereByQuery(O);

			request.setAttribute("opere", opere);
			request.setAttribute("outline_tpl", "");
			request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
			request.setAttribute("contentType", "text/json");
			TemplateResult tr = new TemplateResult(getServletContext());
			tr.activate("queryOpere.ftl.json", request, response);
		} catch (DataLayerException ex) {
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		}

	}
	
	/**
	 * Funzione di ricerca degli utenti che restituisce un oggetto JSON.
	 * 
	 * @param request servlet request
	 * @param response servlet response
	 * @throws TemplateManagerException se occorre un errore nella logica del template manager
	 */
	private void action_ricerca_utenti_ajax(HttpServletRequest request, HttpServletResponse response)
			throws TemplateManagerException{
		try{
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");
			
			List<Ruolo> ruoli = new ArrayList<>();
			List<Utente> utenti = datalayer.getTuttiGliUtenti();
			for(Utente utente : utenti){
				List<Ruolo> ruoli_utente = datalayer.getListaRuoliUtente(utente);
				if(!ruoli_utente.isEmpty())
					ruoli.add(ruoli_utente.get(0));
				else{
					Ruolo ruolo = datalayer.creaRuolo();
					ruolo.setNome("utente base");
					ruolo.setDescrizione("utente che può solo vedere le opere!");
					ruoli.add(ruolo);
				}
			}
			
			List<Ruolo> ruoli_db = datalayer.getTuttiIRuoli();
	
			request.setAttribute("utenti", utenti);
			request.setAttribute("ruoli", ruoli);
			request.setAttribute("ruoli_db", ruoli_db);
			request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
			request.setAttribute("outline_tpl", "");
			request.setAttribute("contentType", "text/json");
			TemplateResult tr = new TemplateResult(getServletContext());
			tr.activate("queryUtenti.ftl.json", request, response);
		} catch (DataLayerException ex) {
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		}
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws TemplateManagerException
	 */
	private void action_tutteleopere_ajax(HttpServletRequest request, HttpServletResponse response)
			throws TemplateManagerException {
		try{
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");
			
			List<Opera> opere = datalayer.getTutteLeOpere();
			request.setAttribute("opere", opere);
			request.setAttribute("outline_tpl", "");
			request.setAttribute("contentType", "text/json");
			request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
			TemplateResult tr = new TemplateResult(getServletContext());
			tr.activate("queryOpere.ftl.json", request, response);
		} catch (DataLayerException ex) {
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws TemplateManagerException
	 */
	private void action_opere_in_pubblicazione_acquisizioni_ajax(HttpServletRequest request, HttpServletResponse response)
			throws TemplateManagerException {
		try{
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");
			
			List<Opera> opere = datalayer.getOpereInPubblicazioneAcquisizioni();
			request.setAttribute("opere", opere);
			request.setAttribute("outline_tpl", "");
			request.setAttribute("contentType", "text/json");
			request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
			TemplateResult tr = new TemplateResult(getServletContext());
			tr.activate("queryOpere.ftl.json", request, response);
		} catch (DataLayerException ex) {
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		}
	}
	

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws TemplateManagerException
	 */
	private void action_opere_in_trascrizione(HttpServletRequest request, HttpServletResponse response)
			throws TemplateManagerException {
		try{
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");
			
			Utente U = datalayer.getUtenteByUsername((String) request.getAttribute("nomeutente"));
			Opera O = datalayer.creaOpera();
			O.setImmaginiPubblicate(true);
			O.setTrascrizioniPubblicate(false);
			O.setTrascrittore(U);
			List<Opera> opere = datalayer.getOpereByQuery(O);
			request.setAttribute("opere", opere);
			request.setAttribute("outline_tpl", "");
			request.setAttribute("contentType", "text/json");
			request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
			TemplateResult tr = new TemplateResult(getServletContext());
			tr.activate("queryOpere.ftl.json", request, response);
		}
			catch(DataLayerException ex) {
				request.setAttribute("message", "Data access exception: " + ex.getMessage());
				action_error(request, response);
			}
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws TemplateManagerException
	 */
	private void action_opere_da_trascrivere(HttpServletRequest request, HttpServletResponse response)
			throws TemplateManagerException {
		try{
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");
			
			List<Opera> opere = datalayer.getOpereDaTrascrivere();
			request.setAttribute("opere", opere);
			request.setAttribute("outline_tpl", "");
			request.setAttribute("contentType", "text/json");
			request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
			TemplateResult tr = new TemplateResult(getServletContext());
			tr.activate("queryOpere.ftl.json", request, response);
		}
			catch(DataLayerException ex) {
				request.setAttribute("message", "Data access exception: " + ex.getMessage());
				action_error(request, response);
			}
	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws TemplateManagerException
	 */
	private void action_opere_in_pubblicazione_trascrizioni_ajax(HttpServletRequest request, HttpServletResponse response)
			throws TemplateManagerException {
		try{
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");
			
			List<Opera> opere = datalayer.getOpereInPubblicazioneTrascrizioni();
			request.setAttribute("opere", opere);
			request.setAttribute("outline_tpl", "");
			request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
			request.setAttribute("contentType", "text/json");
			TemplateResult tr = new TemplateResult(getServletContext());
			tr.activate("queryOpere.ftl.json", request, response);
		} catch (DataLayerException ex) {
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		}
	}
	
	private void action_pagine_opera(HttpServletRequest request, HttpServletResponse response)
			throws TemplateManagerException {
		try{
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");
			
			long id_opera = Long.parseLong(request.getParameter("id_opera"));
			List<Pagina> pagine = datalayer.getPagineOpera(id_opera);
			//leggi il file trascrizione in caso di utente trascrittore o revisore.
			for(Pagina p : pagine){
				if(! p.getPathTrascrizione().equals("") 
						&& ( (Boolean)request.getAttribute("trascrittore") == true || (Boolean) request.getAttribute("revisore_trascrizioni") == true )){
					
					BufferedReader in = new BufferedReader(new FileReader(p.getPathTrascrizione()));
					String testo, riga;
					testo = riga = "";
					while( (riga = in.readLine()) != null){
						testo += riga + System.lineSeparator();
					}
					in.close();
					if((Boolean) request.getAttribute("revisore_trascrizioni")==true){
						p.setPathTrascrizione(testo);
					}else{
						/*int beginIndex = testo.indexOf("<body>")-6;
						int endIndex = testo.indexOf("</body>");
						p.setPathTrascrizione(testo.substring(beginIndex, endIndex));*/
						testo = ParserTEI.tei_to_txt(testo);
						p.setPathTrascrizione(testo);
					}
					
				}
			}
			request.setAttribute("pagine", pagine);
			request.setAttribute("outline_tpl", "");
			request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
			request.setAttribute("contentType", "text/json");
			TemplateResult tr = new TemplateResult(getServletContext());
			tr.activate("queryPagine.ftl.json", request, response);
		} catch(DataLayerException ex){
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		} catch (MalformedURLException e) {
			request.setAttribute("message", "Il path è sbagliato, probabilmente devi aggiustare i path nel DB con quelli del tuo sistema.");
			action_error(request,response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unused")
	private void action_ricerca(HttpServletRequest request, HttpServletResponse response)
			throws TemplateManagerException {
		try {
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");

			String anno = request.getParameter("anno");
			String editore = request.getParameter("editore");
			String autore = request.getParameter("autore");
			String titolo = request.getParameter("titolo");
			String immagini_pubblicate = request.getParameter("pubblicata");
			String lingua = request.getParameter("lingua");

			List<Opera> opere = new ArrayList<Opera>();
			Opera O = datalayer.creaOpera();
			O.setAnno(SecurityLayer.addSlashes(anno));
			O.setEditore(SecurityLayer.addSlashes(editore));
			O.setTitolo(SecurityLayer.addSlashes(titolo));
			O.setImmaginiPubblicate((immagini_pubblicate.equals("true")) ? true : false);
			O.setLingua(SecurityLayer.addSlashes(lingua));
			opere = datalayer.getOpereByQuery(O);

			request.setAttribute("ricerca", true);
			request.setAttribute("opere", opere);
			request.setAttribute("strip_slashes", new SplitSlashesFmkExt());

			action_result(request, response);
		} catch (DataLayerException ex) {
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		}

	}

	@Override
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		try {
			if(request.getParameter("tipoRicerca") == null){
				action_ricerca_ajax(request,response);
			}
			else{
				switch(request.getParameter("tipoRicerca")){
				case "utenti": action_ricerca_utenti_ajax(request,response);
					break;
				case "tutteleopere": action_tutteleopere_ajax(request,response);
					break;
				case "opereInPubblicazioneAcquisizioni": action_opere_in_pubblicazione_acquisizioni_ajax(request, response);
					break;
				case "opereInPubblicazioneTrascrizioni": action_opere_in_pubblicazione_trascrizioni_ajax(request, response);
					break;
				case "opere_in_trascrizione": action_opere_in_trascrizione(request,response);
					break;
				case "opere_da_trascrivere": action_opere_da_trascrivere(request,response);
					break;
				case "pagine_opera": action_pagine_opera(request,response);
					break;
				default: action_ricerca_ajax(request,response);
					break;
				}
			}
		} catch (TemplateManagerException ex) {
			request.setAttribute("exception", ex);
			action_error(request, response);
		}

	}

}
