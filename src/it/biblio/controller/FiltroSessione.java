package it.biblio.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.biblio.framework.result.FailureResult;

/**
 * Servlet Filter implementation class FiltroSessione.
 * Protegge le servlet accessibili solo da utenti autorizzati.
 * 
 */
@WebFilter(description = "Protegge le servlet accessibili solo da utenti autorizzati", urlPatterns = { "/UploadImmagine", "/Trascrivi" })
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
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		List<String> ruoli = (List<String>) request.getAttribute("ruoli");
		Boolean ok = true;
		if(ruoli == null){
			ok = false;
		}
		switch(((HttpServletRequest)request).getServletPath()){
			case "/UploadImmagine": if(ok && (Boolean)request.getAttribute("acquisitore")){ok = true;}
				break;
			case "/Trascrivi": if(ok && (Boolean)request.getAttribute("trascrittore")){ok = true;}
				break;
			default: ok = true;
		}
		
		if(!ok){
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
