package it.biblio.model;

import java.util.List;

public interface DataLayer {
	/*Operazioni Utente*/
	Utente creaUtente();
	
	Utente getUtente(long id);
	
	Utente aggiungiUtente(Utente U);
	
	Utente getUtenteByUsername(String username);
	
	/*Operazioni Ruolo*/
	Ruolo creaRuolo();
	
	Ruolo getRuolo(long progressivo);
	
	Ruolo aggiungiRuolo(Ruolo R);
	
	/*Operazioni Privilegi*/
	Privilegi creaPrivilegio();
	
	Privilegi getPrivilegi(long progressivo);
	
	Privilegi aggiungiPrivilegi(Privilegi P);
	
	Privilegi rimuoviPrivilegiUtente(long id_utente);

	/*Operazioni Opera*/
	Opera creaOpera();
	
	Opera getOpera(long id);
	
	Opera aggiungiOpera(Opera O);
	
	Opera aggiornaOpera(Opera O);
	
	List<Opera> getOpereByQuery(Opera P);
	
	/*Operazioni Pagina*/
	
	Pagina creaPagina();
	
	Pagina getPagina(long id);
	
	Pagina aggiungiPagina(Pagina P);
	
	/*Operazioni Commenta*/
	
	Commenta creaCommenta();
	
	Commenta getCommenta(long progressivo);
	
	Commenta aggiungiCommenta(Commenta C);
	
}
