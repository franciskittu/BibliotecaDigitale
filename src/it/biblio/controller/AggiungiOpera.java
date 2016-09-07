package it.biblio.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.biblio.data.model.BibliotecaDataLayer;
import it.biblio.data.model.Opera;
import it.biblio.framework.data.DataLayerException;
import it.biblio.framework.result.TemplateManagerException;
import it.biblio.framework.utility.ControllerException;
import it.biblio.framework.utility.SecurityLayer;

@WebServlet(name="AggiungiOpera", urlPatterns={"/AggiungiOpera"})
public class AggiungiOpera extends BibliotecaBaseController {

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws TemplateManagerException 
	 */
	private void action_aggiungi_opera(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException{
		try{
			BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");
			
			Opera O = datalayer.creaOpera();
			O.setAnno(SecurityLayer.addSlashes(request.getParameter("anno")));
			O.setLingua(SecurityLayer.addSlashes(request.getParameter("lingua")));
			O.setTitolo(SecurityLayer.addSlashes(request.getParameter("titolo")));
			O.setEditore(SecurityLayer.addSlashes(request.getParameter("editore")));
			O.setAutore(SecurityLayer.addSlashes(request.getParameter("autore")));
			O.setNumeroPagine(Integer.parseInt(request.getParameter("numeroPagine")));
			O.setImmaginiPubblicate(false);
			O.setTrascrizioniPubblicate(false);
			
			O = datalayer.aggiungiOpera(O);
			if(O == null){
				throw new DataLayerException("Errore nell'aggiungere l'opera nel database");
			}
			else{
				request.setAttribute("messaggio", "Inserimento dell'opera avvenuto correttamente;)");
				action_result(request, response);
			}
		}
		catch(DataLayerException ex){
			request.setAttribute("message", "Data access exception: " + ex.getMessage());
			action_error(request, response);
		}
	}
	
	
	@Override
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try{
			if(request.getParameter("anno") != null && request.getParameter("titolo") != null 
				&& request.getParameter("lingua") != null && request.getParameter("autore") != null
				&& request.getParameter("editore") != null ){
				
				SecurityLayer.checkNumeric(request.getParameter("numeroPagine"));
				action_aggiungi_opera(request, response);
			}else{
				throw new ControllerException("parametri non validi!");
			}
			
		}catch(ControllerException | NumberFormatException | TemplateManagerException ex){
			request.setAttribute("exception", ex);
			action_error(request, response);
		}

	}

}
