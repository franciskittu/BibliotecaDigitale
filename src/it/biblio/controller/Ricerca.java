package it.biblio.controller;

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
import it.biblio.framework.result.TemplateManagerException;
import it.biblio.framework.result.TemplateResult;
import it.biblio.framework.utility.SecurityLayer;

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
	 * @param request
	 * @param response
	 * @throws TemplateManagerException
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
			request.setAttribute("contentType", "text/json");
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
	private void action_ricerca_utenti_ajax(HttpServletRequest request, HttpServletResponse response)
			throws TemplateManagerException{
		try{
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");
			
			List<Utente> utenti = datalayer.getTuttiGliUtenti();
			List<Ruolo> ruoli = datalayer.getListaRuoliUtente(datalayer.getUtenteByUsername((String) request.getAttribute("nomeutente")) );
			Ruolo ruolo = ruoli.get(0);
			ruoli = datalayer.getTuttiIRuoli();
			
			request.setAttribute("utenti", utenti);
			request.setAttribute("ruolo", ruolo);
			request.setAttribute("ruoli", ruoli);
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
			request.setAttribute("pagine", pagine);
			request.setAttribute("outline_tpl", "");
			request.setAttribute("contentType", "text/json");
			TemplateResult tr = new TemplateResult(getServletContext());
			tr.activate("queryPagine.ftl.json", request, response);
		} catch(DataLayerException ex){
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
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
