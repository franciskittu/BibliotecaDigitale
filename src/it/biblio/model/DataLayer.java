package it.biblio.model;

import java.util.List;

public interface DataLayer {

	Utente creaUtente();
	
	Utente getUtente(long id);
	
	Utente aggiungiUtente(Utente U);
	
	Utente getUtenteByUsername(String username);
	
	Ruolo creaRuolo();
	
	Ruolo getRuolo(long progressivo);
	
	Ruolo aggiungiRuolo(Ruolo R);
	
	Privilegi creaPrivilegio();
	
	Privilegi getPrivilegi(long progressivo);
	
	Privilegi aggiungiPrivilegi(Privilegi P);
	
	Privilegi rimuoviPrivilegiUtente(long id_utente);

	Opera creaOpera();
	
	Opera getOpera(long id);
	
	Opera aggiungiOpera(Opera O);
	
	List<Opera> getOpereByQuery(Opera P);
}
