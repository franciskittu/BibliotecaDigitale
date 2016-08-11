package it.biblio.framework.data;

/**
 * 
 * @author francesco
 *
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
