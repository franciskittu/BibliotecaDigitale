package it.biblio.framework.data;

/**
 * Fornire le funzioni di chiusura da usare 
 * per chiduere il DB. Viene gestita anche la 
 * chiusura automatica in blocchi try-with-resources
 * grazie all'ereditarietà di AutoCloseable. * 
 * 
 * @author Marco D'Ettorre
 * @author Francesco Proietti
 */
public interface DataLayer extends AutoCloseable{

	/**
	 * Inizializza la connessione al DB
	 * 
	 * @throws DataLayerException
	 */
	void init() throws DataLayerException;
	
	/**
	 * chiude correttamente la connessione al DB
	 * 
	 * @throws DataLayerException
	 */
	void destroy() throws DataLayerException;
}
