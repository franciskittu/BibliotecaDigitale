var vettore_ausiliario=[];
var ruolo = [];

/*VISTA DEFAULT PER L'UTENTE ADMIN*/
function vistaDefaultAdmin(){
	document.getElementById("admindefault").style.display="block";
    document.getElementById("admindefault").style.visibility="visible";
}

/*LISTA DI TUTTI GLI UTENTI PER L'ADMIN*/
function listaUtenti(){
		$.ajax({ 
				url: 'Ricerca',
		        dataType: "json",
		        type: 'GET',
		        data: 'tipoRicerca=utenti',
		            success: function(data) {
		            		ruolo = data;
	    	 				sezione=4;
	    	 				admin= true;
	    	 				vettore_ausiliario = data.ruoli;
	    	 				paginatore(data.utenti);
		    	 			init();
			                document.getElementById("utenti").style.visibility="yes";
    					    document.getElementById("utenti").style.display="block";
		            },
					error: function(error){
						console.log(error);
					}
		});
}

function gestioneRuoloUtente(data){
	var j = 0;
	var tab = document.getElementById('gestioneRuolo');
	var tbody = tab.tBodies[0];
	
	var id_utente = data.getElementsByTagName('TD')[1].textContent;
	for (var i = 0; i<ruolo.ruoli.length; i++){
		var riga = document.createElement('TR');
		if (ruolo.ruoli[i].nome_ruolo == ""){
			ruolo.ruoli[i].nome_ruolo = "Utente base";
		}
		if (ruolo.utenti[i].id == id_utente){
			document.getElementById("nomeUtenteRuolo").innerHTML= ruolo.utenti[i].nome + " " + ruolo.utenti[i].cognome ;
			for (j in ruolo.ruoli[i]){	
				var colonna = document.createElement('TD');
				colonna.textContent = ruolo.ruoli[i][j];
				riga.appendChild(colonna);	
			}
			var td = document.createElement('TD');
			var applica = document.createElement('button');
			applica.className="btn btn-lg";
			applica.disabled = "disabled";
			applica.id=id_utente;
			var testo=document.createTextNode("Applica");
			applica.appendChild(testo);
			td.appendChild(applica);
			riga.appendChild(td);
		}
		else {	
			for (j in ruolo.ruoli_db[i]){
				var colonna = document.createElement('TD');
				colonna.textContent = ruolo.ruoli_db[i][j];
				riga.appendChild(colonna);	
			}
			var td = document.createElement('TD');
			var applica = document.createElement('button');
			applica.className="btn btn-success btn-lg";
			applica.id = ruolo.ruoli_db[i].nome_ruolo; 
			applica.onclick = function(){
				self = this;
				console.log(self.id);
				$.ajax({
			         url: 'Aggiorna',
			         type: 'GET',
			         data: 'tipoAggiornamento=aggiorna_privilegi_utente&nome_ruolo='+self.id+'&id_utente='+id_utente,
			             success: function(data) {
			            	 if(eval(data)){
			            		 console.log(data);
			            		 
			            	 }
			            	 else 
			            		 alert("Ci dispiace! A causa di un problema non è stato possibile rimuovere l'opera");
			             }
				});
			}
			var testo=document.createTextNode("Applica");
			applica.appendChild(testo);
			td.appendChild(applica);
			riga.appendChild(td);
		}
		riga.className = "riga_tabella";
		tbody.appendChild(riga);	
	}	 
	
	document.getElementById("gestioneRuoloUtenti").style.visible="yes";
	document.getElementById("gestioneRuoloUtenti").style.display="block";	
}

function inserisciOpera(){
	document.getElementById("inserisciOpera").style.visible="yes";
    document.getElementById("inserisciOpera").style.display="block";
}

function listaPagineOpera(){
        admin= true;
        init();
        document.getElementById("pagine_opera").style.visible="yes";
        document.getElementById("pagine_opera").style.display="block";
        window.location.hash='#pagine_opera';
		
}

/* 	LISTA OPERE IN PUBBLICAZIONE ACQUISIZIONI PER ADMIN   */
function opereInPubblicazioneAcquisizioni(){
			$.ajax({
		        url: 'Ricerca',
		        dataType: "json",
		        type: 'GET',
		        data: 'tipoRicerca=opereInPubblicazioneAcquisizioni',
		            success: function(data) {
		    	 		if (data==""){
		    	 			errore();
		    	 		}
		    	 		else {         	 			
		    	 			paginatore(data);
		    	 			admin= true;
		    	 			init();
			                document.getElementById("tableOpereInPubbAcqu").style.display="block";
			                document.getElementById("tableOpereInPubbAcqu").style.visibility="visible";
			                
		    	 		}
		            }
			});
}



///////////////////////
/* 	LISTA OPERE ADMIN   */
//////////////////////
function listaOpereAdmin() {
				$.ajax({
			         url: 'Ricerca',
			         dataType: "json",
			         type: 'GET',
			         data: 'tipoRicerca=tutteleopere',
			             success: function(data) {
		         	 			paginatore(data);
			                    admin= true;
			                    init();
				                document.getElementById("listaopere").style.visible="yes";
				                document.getElementById("listaopere").style.display="block";
				                window.location.hash='#listaopere';
			             }
				});
}
function errore (){
		document.getElementById("erroreRicerca").style.visible="visible";
		document.getElementById("erroreRicerca").style.display="block";
		window.location.hash='#erroreRicerca';
}
function trascrizionePagina(){
		document.getElementById("openseadragon").style.display="block";
		document.getElementById("openseadragon").style.visibility="visible";
		document.getElementById("editortei").style.display="block";
		document.getElementById("editortei").style.visibility="visible";
}


///////////////////
/* 	RICERCA   */
///////////////////
function ricerca(){
	var titolo,autore,editore,anno_pubblicazione,lingua,pubblicata;
	titolo=document.getElementById("form_ricerca").titolo.value;
	autore=document.getElementById("form_ricerca").autore.value;
	editore=document.getElementById("form_ricerca").editore.value;
	anno_pubblicazione=document.getElementById("form_ricerca").annoPubblicazione.value;
	lingua=document.getElementById("form_ricerca").lingua.value;
	pubblicata=document.getElementById("form_ricerca").pubblicata.value;
	 $.ajax({
         url: 'Ricerca',
         dataType: "json",
         type: 'GET',
         data: 'titolo='+titolo+"&autore="+autore+"&editore="+editore+"&anno="+anno_pubblicazione+"&immagini_pubblicate="+pubblicata+"&lingua="+lingua,
             success: function(data) {
            	 		if (data==""){
    	                    document.getElementById("listaopere").style.display="none";
            	 			document.getElementById("erroreRicerca").style.display="inline";
                	 		window.location.hash='#erroreRicerca';
            	 		}
            	 		else {
            	 			var k = 0;
            	 			var i = 0;
            	 			pages = [];
            	 			while(i<data.length) {
                        	 var temp = [];
                        	 
                        	 for(j=i; j < i+3 && j < data.length; j++){
                        		 temp.push({id: data[j].id, titolo: data[j].titolo,  descrizione:data[j].descrizione, numero_pagine:data[j].numero_pagine });
                        	 }
                        	 pages[k++] = temp;
                        	 i = i+3;
                         }
                         
                        init();
         	 			document.getElementById("erroreRicerca").style.display="none";
	                    document.getElementById("listaopere").style.display="inline";
	                    window.location.hash='#listaopere';
            	 		}
             },
             error: function(){
            	 		
             }
             
         });
     };		

//////////////////////////////////////////
/* 	CONTROLLA USERNAME REGISTRAZIONE   */
/////////////////////////////////////////
   

function controllausername(obj){
            //$("#username").keypress(function(){
            $.ajax({
            url: 'Registrazione',
            type: 'POST',
            data: 'usernameAjax='+obj.value,
                success: function(data) {
                            if (eval(data)){
                                
                                document.getElementById("usernamecheck").innerHTML ="<div id=\"status\"><span style=\"top:20px\" class=\"glyphicon glyphicon-remove form-control-feedback\" aria-hidden=\"true\"></span><span id=\"inputError2Status\" class=\"sr-only\">(error)</span></div><p class=\"help-block text-danger\"><ul role=\"alert\"><li>Username gia' presente, inserirne un altro</li></ul></p>";
                                
                            }
                            
                            else {
                                
                                $("#status").remove();
                                document.getElementById("usernamecheck").innerHTML ="<div id=\"status\"><span style=\"top:20px\" class=\"glyphicon glyphicon-ok form-control-feedback\" aria-hidden=\"true\"></span><span id=\"inputSuccess2Status\" class=\"sr-only\">(success)</span></div>";
                                
                            }
                }
            });
            };
            
///////////////////////////////////////////////////
/* 	CONTROLLA PAGINA GIA' INSERITA ACQUISIZIONE */
/////////////////////////////////////////////////
            
function controllaNumeroPagina(obj){
	var id = document.getElementById("opera").value;
    $.ajax({
    url: 'UploadImmagine',
    type: 'POST',
    data : {numeroAJAX:obj.value, operaAJAX:id },
    //data: 'numeroAJAX='+obj.value+'&operaAJAX='+id,
        success: function(data) {
                    if (eval(data)){
                        document.getElementById("paginacheck").innerHTML ="<div id=\"status\"><span style=\"top:20px\" class=\"glyphicon glyphicon-remove form-control-feedback\" aria-hidden=\"true\"></span><span id=\"inputError2Status\" class=\"sr-only\">(error)</span></div><p class=\"help-block text-danger\"><ul role=\"alert\"><li>Pagina gia' presente, scegline un'altra</li></ul></p>";
                    }
                    else {
                        $("#status").remove();
                        document.getElementById("paginacheck").innerHTML ="<div id=\"status\"><span style=\"top:20px\" class=\"glyphicon glyphicon-ok form-control-feedback\" aria-hidden=\"true\"></span><span id=\"inputSuccess2Status\" class=\"sr-only\">(success)</span></div>";
                        
                    }
        }
    });
}  

/*VISTA TRASCRITTORE*/
function listaOpereTrascrittore(){
	$.ajax({
        url: 'Ricerca',
        dataType: "json",
        type: 'GET',
        data: 'tipoRicerca=opere_in_trascrizione',
            success: function(data) {
    	 		if (data==""){
    	 			//errore();
    	 		}
    	 		else {
    	 			document.getElementById("lista_opere_in_trascrizione").style.display="block";
    	 			document.getElementById("lista_opere_in_trascrizione").style.visibility="visible";
    	 			paginatore(data);
    	 			init();
    	 		}
            }
	});
	$.ajax({
        url: 'Ricerca',
        dataType: "json",
        type: 'GET',
        data: 'tipoRicerca=opere_da_trascrivere',
            success: function(data) {
    	 		if (data==""){
    	 			//errore();
    	 		}
    	 		else {
    	 			document.getElementById("lista_opere_da_trascrivere").style.display="block";
    	 			document.getElementById("lista_opere_da_trascrivere").style.visibility="visible";
    	 			document.getElementById("ricerca").style.display="block";
    	 			document.getElementById("ricerca").style.visibility="visible";
    	 			paginatore(data);
    	 			init();
    	 		}
            }
	});
	
}

function invia_trascrizione(obj){
	console.log(document.getElementById("editor_tei").id_della_pagina.value);
	$.ajax({
	    url: 'Trascrivi',
	    type: 'GET',
	    data : "id_pagina="+obj.id,//anche tutte le input della form 
	    //data: 'numeroAJAX='+obj.value+'&operaAJAX='+id,
	        success: function(data) {
	                    if (eval(data)){
	                        alert("ok");
	                    }
	                    else {
	                        $("#status").remove();
	                        alert("ok");
	                        
	                    }
	        }
	    });
}
