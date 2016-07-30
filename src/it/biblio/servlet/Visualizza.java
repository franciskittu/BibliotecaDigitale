package it.biblio.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import it.biblio.framework.result.TemplateResult;
import it.biblio.model.Opera;
import it.biblio.model.Utente;
import it.biblio.model.impl.DataLayerImpl;
import it.biblio.utility.SecurityLayer;

public class Visualizza extends HttpServlet {

	private void gestisciRicerca(HttpServletRequest request, DataLayerImpl datalayer,
			Map<String, Object> template_data) {
		String anno = request.getParameter("anno");
		String editore = request.getParameter("editore");
		String autore = request.getParameter("autore");
		String titolo = request.getParameter("titolo");
		String pubblicata = request.getParameter("pubblicata");
		String lingua = request.getParameter("lingua");

		List<Opera> opere = new ArrayList<Opera>();
		Opera O = datalayer.creaOpera();
		O.setAnno(anno);
		O.setEditore(editore);
		O.setTitolo(titolo);
		O.setPubblicata((pubblicata.equals("true")) ? true : false);
		O.setLingua(lingua);
		opere = datalayer.getOpereByQuery(O);

		template_data.put("opere", opere);
	}

	private void gestisciLogin(HttpServletRequest request, Map<String, Object> template_data, HttpSession s) {
		if (request.getParameter("errore") != null) {
			if (request.getParameter("errore").equals("login")) {
				template_data.put("errore", "login");// da concordare con il
														// template freemarker
			}
		} else {

			List<String> ruoli = (List<String>) s.getAttribute("ruoli");
			if (ruoli != null) {
				template_data.put("acquisitore", ruoli.contains("acquisitore"));
				template_data.put("trascrittore", ruoli.contains("trascrittore"));
				template_data.put("admin", ruoli.contains("admin"));
				template_data.put("revisore_acquisizioni", ruoli.contains("revisore acquisizioni"));
				template_data.put("revisore_trascrizioni", ruoli.contains("revisore trascrizioni"));
				template_data.put("nomutente", s.getAttribute("username"));
			}

			template_data.put("loggato", true);

		}
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		HttpSession s = SecurityLayer.checkSession(request);

		DataSource ds = (DataSource) getServletContext().getAttribute("datasource");

		Connection connection = ds.getConnection();
		DataLayerImpl datalayer = new DataLayerImpl(connection);

		Map<String, Object> template_data = new HashMap<String, Object>();
		if (!request.getParameterNames().hasMoreElements()) {
			template_data.put("ricerca_tpl", "ricerca.ftl.html");
			template_data.put("registrazione_tpl", "registrazione.ftl.html");
		} else if (request.getParameter("richiesta").equals("login")) {
			gestisciLogin(request, template_data, s);
			template_data.put("ricerca_tpl", "ricerca.ftl.html");
		} else if (request.getParameter("richiesta").equals("ricerca")) {
			gestisciRicerca(request, datalayer, template_data);
			template_data.put("ricerca_tpl", "ricerca.ftl.html");
			template_data.put("ricerca", true);
			template_data.put("listaopere_tpl", "listaopere.ftl.html");
			if (s != null) {
				gestisciLogin(request, template_data, s);
			} else {
				template_data.put("acquisitore", false);
				template_data.put("trascrittore", false);
				template_data.put("admin", false);
				template_data.put("revisore_acquisizioni", false);
				template_data.put("revisore_trascrizioni", false);
				template_data.put("registrazione_tpl", "registrazione.ftl.html");
			}
		}

		TemplateResult tr = new TemplateResult(getServletContext());
		tr.activate("", template_data, response);
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on
	// the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}