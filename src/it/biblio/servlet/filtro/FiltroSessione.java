package it.biblio.servlet.filtro;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.biblio.framework.result.FailureResult;
import it.biblio.utility.ErroreBiblioteca;
import it.biblio.utility.SecurityLayer;

/**
 * Servlet Filter implementation class FiltroSessione
 */
@WebFilter(description = "Protegge le servlet accessibili solo da utenti autorizzati", urlPatterns = { "/UploadImmagine" })
public class FiltroSessione implements Filter {

	FilterConfig config;
	
    /**
     * Costruttore di default
     */
	public FiltroSessione(){
		
	}
	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		List<String> ruoli = (List<String>) request.getAttribute("ruoli");
		if(ruoli == null || !ruoli.contains("acquisitore")){
			FailureResult res = new FailureResult(this.config.getServletContext());
			res.activate("Accesso non autorizzato dal filtro", (HttpServletRequest)request, (HttpServletResponse)response);
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		this.config = fConfig;
	}

}
