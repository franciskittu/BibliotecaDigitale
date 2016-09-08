package it.biblio.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.biblio.data.model.BibliotecaDataLayer;
import it.biblio.data.model.Privilegi;
import it.biblio.framework.data.DataLayerException;
import it.biblio.framework.result.TemplateManagerException;
import it.biblio.framework.result.TemplateResult;
import it.biblio.framework.utility.ControllerException;
import it.biblio.framework.utility.SecurityLayer;

/**
 * Servlet che gestisce le richieste di aggiornamento delle entità
 * presenti nella base di dati.
 * 
 * @author Marco D'Ettorre
 * @author Francesco Proietti
 */
@WebServlet(name="Aggiorna", urlPatterns={"/Aggiorna"})
public class Aggiorna extends BibliotecaBaseController {

	/**
	 * Funzione che aggiorna i privilegi utente rimuovendo quelli già presenti 
	 * per inserire quelli ricevuti dalla richiesta.
	 * 
	 * @param request servlet request
	 * @param response servlet response
	 * @throws TemplateManagerException errore nella logica del template manager 
	 */
	private void action_aggiorna_privilegi_utente(HttpServletRequest request, HttpServletResponse response) throws ControllerException, TemplateManagerException{
		
		try{
			long id_utente = SecurityLayer.checkNumeric(request.getParameter("id_utente"));
			if(request.getParameter("nome_ruolo")== null){
				throw new ControllerException("campo nome ruolo non corretto!");
			}
			Boolean successo = true;
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");
			
			datalayer.rimuoviPrivilegiUtente(id_utente);
			
			Privilegi P = datalayer.creaPrivilegio();
			P.setRuolo(datalayer.getRuoloByNome(request.getParameter("nome_ruolo")));
			P.setUtente(datalayer.getUtente(id_utente));
			
			P = datalayer.aggiungiPrivilegi(P);
			if(P == null){
				successo = false;
			}
			
			request.setAttribute("risultato", successo.toString());
			request.setAttribute("outline_tpl", "");
			
			TemplateResult tr = new TemplateResult(getServletContext());
			tr.activate("controlloAjax.ftl.json", request, response);
		}catch(DataLayerException ex){
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		}catch(NumberFormatException ex){
			throw new ControllerException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		if(request.getParameter("tipoAggiornamento") != null){
			try{
				switch(request.getParameter("tipoAggiornamento")){
				case "aggiorna_privilegi_utente": action_aggiorna_privilegi_utente(request,response);
					break;
				default: action_aggiorna_privilegi_utente(request,response);
				}
			}catch(ControllerException | TemplateManagerException ex){
				request.setAttribute("message", ex.getMessage());
				action_error(request, response);
			}
		}

	}

}
