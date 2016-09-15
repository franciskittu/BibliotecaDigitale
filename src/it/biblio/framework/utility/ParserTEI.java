package it.biblio.framework.utility;

import java.io.IOException;

public final class ParserTEI {

	/**
	 * 
	 * @param doc
	 * @param path_doc
	 * @return
	 * @throws IOException
	 */
	public static boolean valida(String path_doc) throws IOException{
		String comando = new String("bash -c \"teitotxt "+path_doc+"\"");
		Process proc = Runtime.getRuntime().exec(comando);
		int valore_di_ritorno;
		try {
			valore_di_ritorno = proc.waitFor();
		} catch (InterruptedException e) {
			return false;
		}
		if(valore_di_ritorno != 0){
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param doc
	 * @return
	 */
	public static String tei_to_txt(String doc){
		final String[] tags={"<?xml","<TEI","</TEI","<body","</body","<p","</p","<div","</div","<figure","</figure","<hi","</hi","<q","</q","<text","</text"};
		Boolean almeno_un_tag_presente = true;
		
		while(almeno_un_tag_presente == true){
			almeno_un_tag_presente = false;
			
			for(String tag : tags){
				if(doc.contains(tag)){
					almeno_un_tag_presente=true;
					int beginIndex = doc.indexOf(tag);
					int endIndex = doc.indexOf('>', beginIndex);
					String tag_intero = doc.substring(beginIndex, endIndex+1);
					doc = doc.replace(tag_intero, "");
				}
			}
			
		}
		//rimozione intestazione
		String tag_fine_header = "</teiHeader>";
		int inizioIntestazione = doc.indexOf("<teiHeader");
		int fineIntetazione = doc.indexOf(tag_fine_header, inizioIntestazione);
		String intestazione = doc.substring(inizioIntestazione, fineIntetazione+tag_fine_header.length());
		doc = doc.replace(intestazione, "");
		int occorrenza = 0;
		while(doc.indexOf(System.lineSeparator()) < occorrenza + 2){
			occorrenza = doc.indexOf(System.lineSeparator());
			doc = doc.substring(occorrenza+System.lineSeparator().length());
		}
		doc = doc.replaceAll(System.lineSeparator(), "<br />");
		return doc;
	}
}
