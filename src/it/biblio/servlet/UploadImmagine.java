package it.biblio.servlet;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class Upload
 */
@WebServlet(description = "gestisce l'upload delle immaggini acquisite", urlPatterns = { "/Upload" })
public class UploadImmagine extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
		final long id_opera = Long.getLong(request.getParameter("id_opera"));
		final Part filePart = request.getPart("fileToUpload");
		final String nomeFile = getFileName(filePart);
		final String path = "/tmp";
		
		OutputStream out = null;
		InputStream in = null;
		try{
			out = new FileOutputStream(new File(path+File.separator+nomeFile));
			in = filePart.getInputStream();
			
			int read = 0;
			final byte[] bytes = new byte[1024];
			
			while((read = in.read(bytes)) != -1){
				out.write(bytes,0,read);
			}
			
		}catch(FileNotFoundException ex){
			//failure
		}finally{
			if(out!=null){
				out.close();
			}
			if(in!=null){
				in.close();
			}
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
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
