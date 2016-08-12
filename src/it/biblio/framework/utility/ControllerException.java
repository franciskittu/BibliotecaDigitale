package it.biblio.framework.utility;

public class ControllerException extends Exception {

	public ControllerException(String err){
		super(err);
	}
	public ControllerException(){
		super("errore sconosciuto");
	}
}
