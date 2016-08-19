package it.biblio.data.model;

import java.util.List;

import it.biblio.framework.data.DataLayer;
import it.biblio.framework.data.DataLayerException;

public interface BibliotecaDataLayer extends DataLayer{
	/*Operazioni Utente*/
	Utente creaUtente() throws DataLayerException;
	
	Utente getUtente(long id) throws DataLayerException;
	
	Utente aggiungiUtente(Utente U)throws DataLayerException;
	
	Utente getUtenteByUsername(String username)throws DataLayerException;
	
	List<Utente> getTuttiGliUtenti() throws DataLayerException;
	
	/*Operazioni Ruolo*/
	Ruolo creaRuolo()throws DataLayerException;
	
	Ruolo getRuolo(long progressivo)throws DataLayerException;
	
	Ruolo getRuoloByNome(String nome)throws DataLayerException;
	
	List<Ruolo> getListaRuoliUtente(Utente U)throws DataLayerException;
	
	Ruolo aggiungiRuolo(Ruolo R)throws DataLayerException;
	
	List<Ruolo> getTuttiIRuoli() throws DataLayerException;
	
	/*Operazioni Privilegi*/
	Privilegi creaPrivilegio()throws DataLayerException;
	
	Privilegi getPrivilegi(long progressivo)throws DataLayerException;
	
	Privilegi aggiungiPrivilegi(Privilegi P)throws DataLayerException;
	
	Privilegi rimuoviPrivilegiUtente(long id_utente)throws DataLayerException;

	/*Operazioni Opera*/
	Opera creaOpera()throws DataLayerException;
	
	Opera getOpera(long id)throws DataLayerException;
	
	Opera aggiungiOpera(Opera O)throws DataLayerException;
	
	Opera aggiornaOpera(Opera O)throws DataLayerException;
	
	List<Opera> getOpereByQuery(Opera P)throws DataLayerException;
	
	List<Opera> getOpereDaTrascrivere()throws DataLayerException;
	
	List<Opera> getTutteLeOpere() throws DataLayerException;
	
	/*Operazioni Pagina*/
	
	Pagina creaPagina()throws DataLayerException;
	
	Pagina getPagina(long id)throws DataLayerException;
	
	Pagina aggiungiPagina(Pagina P)throws DataLayerException;
	
	List<Pagina> getPagineOpera(long id_opera)throws DataLayerException;
	
	/*Operazioni Commenta*/
	
	Commenta creaCommenta()throws DataLayerException;
	
	Commenta getCommenta(long progressivo)throws DataLayerException;
	
	Commenta aggiungiCommenta(Commenta C)throws DataLayerException;
	
}
