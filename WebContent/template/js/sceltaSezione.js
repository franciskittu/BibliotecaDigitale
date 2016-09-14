/*
 SE SEZIONE = 0 VISTA DEFAULT 
			= 1 VISTA LISTA OPERE 
			= 2 VISTA LISTA OPERE DA PUBBLICARE ACQUISIZIONE E TRASCRIZIONE
			= 3 VISTA PAGINE DELL'OPERA
			= 4 VISTA UTENTI 
			= 5 TRASCRITTORE
			= 6 VISTA PAGINA TRASCRITTORE 
			= 7 INSERISCI OPERA
*/
var admin=false;
var sezione=0;

function scelta_sezione(caso){
		var sezioni= document.getElementsByTagName("SECTION");
		for (var i =0; i<sezioni.length; i++){
			sezioni[i].style.visibility="none";
			sezioni[i].style.display="none";
		}
		switch(caso) {
	    case 1:	 
	    	sezione=1;
	    	listaOpereAdmin();
	        break;
	    case 2:
	    	sezione=2;
	    	opereInPubblicazioneAcquisizioni();
	    	opereInPubblicazioneTrascrizioni();
	        break;
	    case 3: 
	    	sezione = 3;
	    	listaPagineOpera();
	    	break;
	    case 4: 
	    	sezione = 4;
	    	listaUtenti();
	    	break;
	    case 5:
	    	sezione = 5;
	    	listaOpereTrascrittore();
	    	listaOpereTrascrittoreDaTrascrivere();
	    	break;
	    case 6:
	    	sezione = 6;
	    	
	    	break;
	    case 7:
	    	sezione = 7;
	    	inserisciOpera();
	    	break;
	    case 8:
	    	sezione = 8;
	    	gestisciUtente();
	    	break;
	    	
	    default:
	    	sezione=0
	    	vistaDefaultAdmin();
		};
}

