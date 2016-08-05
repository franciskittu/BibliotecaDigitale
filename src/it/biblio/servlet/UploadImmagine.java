package it.biblio.servlet;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import it.biblio.model.Pagina;
import it.biblio.model.impl.DataLayerImpl;

/**
 * Servlet implementation class Upload
 */
@WebServlet(description = "gestisce l'upload delle immaggini acquisite", urlPatterns = { "/UploadImmagine" })
@MultipartConfig
public class UploadImmagine extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Resource(name = "jdbc/bibliodb")
	private DataSource ds;
	
	private String getFileName(final Part part) {
	    final String partHeader = part.getHeader("content-disposition");
	    
	    for (String content : part.getHeader("content-disposition").split(";")) {
	        if (content.trim().startsWith("filename")) {
	            return content.substring(
	                    content.indexOf('=') + 1).trim().replace("\"", "");
	        }
	    }
	    return null;
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		final long id_opera = Long.getLong(request.getParameter("opera"));
		final Part filePart = request.getPart("fileToUpload");
		final String nomeFile = getFileName(filePart);
		final String path = getServletContext().getContextPath()+File.separator+"immagini-opere";
		final String numero = (String) request.getParameter("numero_pagina");
		
		
		OutputStream out = null;
		InputStream in = null;
		try{
			/**
			 * Connessione al db
			 */
			Connection connection = ds.getConnection();
			/**
			 * Oggetto DAO
			 */
			DataLayerImpl datalayer = new DataLayerImpl(connection);
			
			out = new FileOutputStream(new File(path+File.separator+nomeFile));
			in = filePart.getInputStream();
			
			int read = 0;
			final byte[] bytes = new byte[1024];
			
			while((read = in.read(bytes)) != -1){
				out.write(bytes,0,read);
			}
			
			Pagina P = datalayer.creaPagina();
			P.setOpera(datalayer.getOpera(id_opera));
			P.setPathImmagine(path+File.separator+nomeFile);
			P.setUploadImmagine(new Timestamp(Calendar.getInstance().getTime().getTime()));
			P.setNumero(numero);
			datalayer.aggiungiPagina(P);
		}catch(FileNotFoundException ex){
			//failure
		}catch(SQLException ex){
			//
		}finally{
			if(out!=null){
				out.close();
			}
			if(in!=null){
				in.close();
			}
			
			response.sendRedirect("Visualizza?richiesta=upload");
		}
		
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadImmagine() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletInfo()
	 */
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null; 
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
