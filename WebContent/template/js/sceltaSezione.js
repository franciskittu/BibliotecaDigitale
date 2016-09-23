/*
 SE SEZIONE = 0 VISTA DEFAULT 
			= 1 VISTA LISTA OPERE 
			= 2 VISTA LISTA OPERE DA PUBBLICARE ACQUISIZIONE E TRASCRIZIONE
			= 3 VISTA PAGINE DELL'OPERA
			= 4 VISTA UTENTI 
			= 5 TRASCRITTORE
			= 6 VISTA PAGINA TRASCRITTORE 
			= 7 INSERISCI OPERA
			= 8 GESTIONE UTENTE 
			= 9 REVISORE ACQUISIZIONI
*/
var admin=false;
var sezione=0;

function scelta_sezione(caso){
		if (document.getElementById("error2")!= null){
		document.getElementById("error2").style.visibility="none";
		document.getElementById("error2").style.display="none";
		}
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
	    case "trascrittore":
	    	sezione = "trascrittore";
	    	listaOpereTrascrittore();
	    	listaOpereTrascrittoreDaTrascrivere();
	    	break;
	    case "editor_trascrittore":
	    	sezione = "editor_trascrittore";
	    	
	    	break;
	    case 7:
	    	sezione = 7;
	    	inserisciOpera();
	    	break;
	    case 8:
	    	sezione = 8;
	    	gestisciUtente();
	    	break;
	    case "revisore_acquisizioni":
	    	sezione = "revisore_acquisizioni";
	    	revisoreAcquisizioni();
	    	break;
	    case "convalida_revisore_acquisizioni":
	    	sezione = "convalida_revisore_acquisizioni";
	    	break;
	    case "revisore_trascrizione":
	    	sezione = "revisore_trascrizione";
	    	revisione_trascrizione();
	    	break;
	    case "pagine_con_trascrizioni_da_convalidare":
	    	sezione = "pagine_con_trascrizioni_da_convalidare";
	    	break;
	    case "ricerca":
	    	sezione = "ricerca";
	    	break;
	    default:
	    	sezione=0
	    	vistaDefaultAdmin();
		};
}

