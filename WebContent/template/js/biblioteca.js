var vettore_ausiliario=[];
var ruolo = [];
var id_opera;
var pagine_opera = [];

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
			            	for(var i = 0 ; i<data.ruoli.length; i++){
			            		if (data.ruoli[i].nome_ruolo == ""){
			            			data.ruoli[i].nome_ruolo = "Utente base";
			            		}
			            	}	
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
	var idRuoloUtente=0;
	var tab = document.getElementById('gestioneRuolo');
	var tbody = tab.tBodies[0];
	var tr = tbody.getElementsByTagName('TR');
	//se la tabella è piena viene azzerata
	if (tr.length>0){
		while (tr[0]){
			tr[0].remove();
		}	
	}
	var id_utente = data.getElementsByTagName('TD')[1].textContent;
	// scorro l'array dei ruoli
	for (var i = 0; i<ruolo.ruoli.length; i++){
		var numeroUtenteArray=0;
		var riga = document.createElement('TR');
		
		if (ruolo.utenti[i].id == id_utente){
			numeroUtenteArray = i;
			document.getElementById("nomeUtenteRuolo").innerHTML= ruolo.utenti[i].nome + " " + ruolo.utenti[i].cognome ;
			//scorro l'oggetto i che contiene informazioni sui ruoli e stampo nella tabella 
			//il ruolo dell'utente selezionato con nome descrizione e id 
				
			idRuoloUtente=ruolo.ruoli[i].id_ruolo;
			
			}
		}
		//stampo i restanti ruoli
		for (var i = 0; i<ruolo.ruoli_db.length; i++) {
			var riga = document.createElement('TR');
			if(idRuoloUtente == ruolo.ruoli_db[i].id_ruolo){
				for (j in ruolo.ruoli[i]){	
					var colonna = document.createElement('TD');
					colonna.textContent = ruolo.ruoli_db[i][j];
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
				riga.className = "riga_tabella";
				tbody.appendChild(riga);
			}
			else{
				for (j in ruolo.ruoli_db[i]){
					var colonna = document.createElement('TD');
					colonna.textContent = ruolo.ruoli_db[i][j];
					riga.appendChild(colonna);	
				}
			var td = document.createElement('TD');
			var applica = document.createElement('button');
			applica.className="btn btn-primary btn-lg";
			applica.id = ruolo.ruoli_db[i].nome_ruolo; 
			applica.onclick = function(){
				self = this;
				$.ajax({
			         url: 'Aggiorna',
			         type: 'GET',
			         data: 'tipoAggiornamento=aggiorna_privilegi_utente&nome_ruolo='+self.id+'&id_utente='+id_utente,
			             success: function(response) {
			            	 if(eval(response)){
			            		 for (var i=0; i <ruolo.ruoli_db.length; i++){
			            			 if (ruolo.ruoli_db[i].nome_ruolo == self.id){
			            				 ruolo.ruoli[numeroUtenteArray].id_ruolo = ruolo.ruoli_db[i].id_ruolo;
			            				 ruolo.ruoli[numeroUtenteArray].nome_ruolo = ruolo.ruoli_db[i].nome_ruolo;
			            				 ruolo.ruoli[numeroUtenteArray].descrizione_ruolo = ruolo.ruoli_db[i].descrizione_ruolo;
			            			 }
			            		 }
			            		 gestioneRuoloUtente(data);

			            		 listaUtenti();
			            	 }
			            	 else 
			            		 alert("");
			             }
				});
			}
			var testo=document.createTextNode("Applica");
			applica.appendChild(testo);
			td.appendChild(applica);
			riga.appendChild(td);
			riga.className = "riga_tabella";
			tbody.appendChild(riga);
			}
		}
		
				 
	
	document.getElementById("gestioneRuoloUtenti").style.visible="yes";
	document.getElementById("gestioneRuoloUtenti").style.display="block";	
}

function inserisciOpera(){
	document.getElementById("inserisciOpera").style.visible="yes";
    document.getElementById("inserisciOpera").style.display="block";
}

function listaPagineOpera(){
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
        			console.log(data);
                    if (eval(data)){
                    	$("#status").remove();
                        document.getElementById("paginacheck").innerHTML ="<div id=\"status\"><span style=\"top:20px\" class=\"glyphicon glyphicon-ok form-control-feedback\" aria-hidden=\"true\"></span><span id=\"inputSuccess2Status\" class=\"sr-only\">(success)</span></div>";
                    }
                    else {
                    	document.getElementById("paginacheck").innerHTML ="<div id=\"status\"><span style=\"top:20px\" class=\"glyphicon glyphicon-remove form-control-feedback\" aria-hidden=\"true\"></span><span id=\"inputError2Status\" class=\"sr-only\">(error)</span></div><p class=\"help-block text-danger\"><ul role=\"alert\"><li>Pagina gia' presente, scegline un'altra</li></ul></p>";
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
	var testo = document.getElementById("testoTrascrizione").value;
	console.log(document.getElementById("editor_tei").id_della_pagina.value); 
	console.log(testo);
	debugger;
	$.ajax({
	    url: 'Trascrivi',
	    type: 'GET',
	    data : "id_pagina="+obj.id+"&testo="+testo,//anche tutte le input della form 
	    //data: 'numeroAJAX='+obj.value+'&operaAJAX='+id,
	        success: function(data) {
	                    if (eval(data)){
	                        alert("Trascrizione salvata correttamente");
	                    }
	                    else {
	                        $("#status").remove();
	                        alert("ok");
	                        
	                    }
	        }
	    });
}

function trascrizionePagina(idOpera){
	//id_opera = idOpera;
	$.ajax({
		url: 'Ricerca',
        dataType: "json",
        type: 'GET',
        data: 'tipoRicerca=pagine_opera&id_opera='+idOpera,
        success: function(data) {
        	console.log(data);
        	pagine_opera = data;
            if (data.length > 0){
	            	if (data[0].trascrizione != ''){
	            		document.getElementById("testoTrascrizione").innerHTML = data[0].trascrizione;
	            	}
	            	document.getElementById("numeroPaginaDaTrascrivere").innerHTML = data[0].numero;
	            	openseadragon(data[0].id);
	            	document.getElementById("editor_tei").id_della_pagina.value = data[0].id;
	            	document.getElementById("openseadragon").style.display="block";
	            	document.getElementById("openseadragon").style.visibility="visible";
            	
            }
            else {
                alert("Non ci sono pagine da trascrivere!!");
                
            }
        }
        
	});
}

function gestioneTrascrizione(numero_pagina_selezionata){
	for (var i = 0 ; i<pagine_opera.length; i++){
		if ( pagine_opera[i].numero == numero_pagina_selezionata ){
			if (pagine_opera[i].trascrizione != ''){
				document.getElementById("testoTrascrizione").innerHTML = pagine_opera[i].trascrizione;
			}
			document.getElementById("editor_tei").id_della_pagina.value = pagine_opera[i].id;
			document.getElementById("numeroPaginaDaTrascrivere").innerHTML = pagine_opera[i].numero;
			document.getElementById("openseadragon1").remove();
			var div_creato = document.createElement("DIV");
			div_creato.setAttribute("style","width: 50%; height: 500px; float:left;");
			div_creato.id = "openseadragon1";	
			var body = document.getElementById("viewer_img");
			body.appendChild(div_creato);
			openseadragon(pagine_opera[i].id);
		}
	}
}

function stampaErrore(data){
	document.innerHTML(data);
}

//OPENSEADRAGON 
function openseadragon(idImmagine){
	var viewer = OpenSeadragon({
	    id: "openseadragon1",
	    prefixUrl: "template/js/openseadragon/images/",
	    tileSources: {
	        type: "image",
	        url: "Download?imgid="+idImmagine
	    }
	});
}
function scegliPagina(){
	document.getElementById("inserimentoNumeroPagina").addEventListener("keyup", function(event) {
	event.preventDefault();
	if (event.keyCode == 13) {
		var pagina_selezionata = document.getElementById('inserimentoNumeroPagina').value;
		gestioneTrascrizione(pagina_selezionata);
	}
	});
}

function previous(){
	var pagina = parseInt(document.getElementById('numeroPaginaDaTrascrivere').textContent)-1;
	gestioneTrascrizione(pagina);
}

function next(){
	var pagina = parseInt(document.getElementById('numeroPaginaDaTrascrivere').textContent)+1;
	gestioneTrascrizione(pagina);
}