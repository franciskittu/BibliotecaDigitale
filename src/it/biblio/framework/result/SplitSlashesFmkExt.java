package it.biblio.framework.result;

import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import it.biblio.framework.security.SecurityLayer;

/**
 * Classe che definisce una funzione richiamabile all'interno
 * del template di Freemarker.
 * 
 * @author francesco
 *
 */
public class SplitSlashesFmkExt implements TemplateMethodModelEx {

	@Override
	public Object exec(List arg0) throws TemplateModelException {
		//la lista contiene i parametri passati alla funzione nel template
		if(!arg0.isEmpty()){
			return SecurityLayer.stripSlashes(arg0.get(0).toString());
		}else{
			return "";
		}
		
	}

}
