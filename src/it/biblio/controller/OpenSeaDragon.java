package it.biblio.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.biblio.data.model.BibliotecaDataLayer;
import it.biblio.data.model.Pagina;
import it.biblio.framework.data.DataLayerException;
import it.biblio.framework.result.FailureResult;
import it.biblio.framework.result.StreamResult;
import it.biblio.framework.utility.ControllerException;
import it.biblio.framework.utility.SecurityLayer;

@WebServlet("/OpenSeaDragon")
public class OpenSeaDragon extends BibliotecaBaseController {

	private static final long serialVersionUID = -3999174379443972231L;

	@Override
	protected void action_error(HttpServletRequest request, HttpServletResponse response){
		StreamResult sr = new StreamResult(getServletContext());
		try {
			sr.activate(new File(getServletContext().getRealPath(File.separator)+File.separator+"template/img/error_osd.jpeg"), request, response);
		} catch (IOException e) {
			request.setAttribute("message", "Errore grave nel reperire le immagini delle opere");
			(new FailureResult(getServletContext())).activate((String) request.getAttribute("message"), request,
					response);
		}
	}
	
	private void action_download(HttpServletRequest request, HttpServletResponse response, long imgid)
			throws IOException, DataLayerException, ControllerException {
		StreamResult sr = new StreamResult(getServletContext());

		BibliotecaDataLayer datalayer = (BibliotecaDataLayer) request.getAttribute("datalayer");

		Pagina P = datalayer.getPagina(imgid);
		if (P != null) {
			sr.activate(new File(P.getPathImmagine()), request, response);
		} else {
			throw new ControllerException("Non è stata reperita alcuna pagina");
		}
	}
	
	@Override
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try{
			long imgid = SecurityLayer.checkNumeric(request.getParameter("imgid"));
			action_download(request,response,imgid);
		}catch (NumberFormatException ex) {
            action_error(request, response);
		} catch (IOException e) {
			action_error(request,response);
		} catch (DataLayerException e) {
			action_error(request,response);
		} catch (ControllerException e) {
			action_error(request,response);
		}
		
	}

}
