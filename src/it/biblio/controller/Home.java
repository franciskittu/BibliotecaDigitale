package it.biblio.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.biblio.framework.data.DataLayerException;
import it.biblio.framework.result.TemplateManagerException;

@WebServlet(name="Home", urlPatterns={"/index.html", "/Home"})
public class Home extends BibliotecaBaseController {

	@Override
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			action_result(request,response);
		} catch (TemplateManagerException | DataLayerException ex) {
			request.setAttribute("exception", ex);
            action_error(request, response);
		}

	}

}
