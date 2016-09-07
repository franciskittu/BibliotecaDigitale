package it.biblio.controller;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.biblio.data.model.BibliotecaDataLayer;
import it.biblio.data.model.Opera;
import it.biblio.data.model.Pagina;
import it.biblio.data.model.Utente;
import it.biblio.framework.data.DataLayerException;
import it.biblio.framework.result.TemplateManagerException;
import it.biblio.framework.result.TemplateResult;

@WebServlet(name="Rimuovi", urlPatterns={"/Rimuovi"})
public class Rimuovi extends BibliotecaBaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3263374695997268057L;

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws TemplateManagerException
	 */
	private void action_rimuovi_utente(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException{
		try{
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");
			Boolean successo = true;
			Utente U = datalayer.getUtente(Long.parseLong(request.getParameter("id_utente")));
			List<Utente> utenti = datalayer.getTuttiGliUtenti();
			if(datalayer.rimuoviUtente(U) == null){
				successo = false;
			}
			request.setAttribute("risultato", successo.toString());
			request.setAttribute("outline_tpl", "");
			
			TemplateResult tr = new TemplateResult(getServletContext());
			tr.activate("controlloAjax.ftl.json", request, response);
		}catch(DataLayerException ex){
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
	private void action_rimuovi_opera(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException{
		try{
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");
			Boolean successo = true;
			Opera O = datalayer.getOpera(Long.parseLong(request.getParameter("id_opera")));
			List<Pagina> pagine = datalayer.getPagineOpera(O.getID());
			for(Pagina p : pagine){
				if(datalayer.rimuoviPagina(p) == null){
					successo = false;
				}
			}
			if(datalayer.rimuoviOpera(O) == null){
				successo = false;
			}
			
			request.setAttribute("risultato", successo.toString());
			request.setAttribute("outline_tpl", "");
			
			TemplateResult tr = new TemplateResult(getServletContext());
			tr.activate("controlloAjax.ftl.json", request, response);
		} catch(DataLayerException ex){
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		}
	}
	
	
	@Override
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try{
			if(request.getParameter("id_opera") != null){
				action_rimuovi_opera(request,response);
			}else if(request.getParameter("id_utente") != null){
				action_rimuovi_utente(request,response);
			}
		}catch(TemplateManagerException ex){
			request.setAttribute("exception", ex);
			action_error(request, response);
		}

	}

}
