///////////////
/////ADMIN/////
///////////////
/*
 SE SEZIONE = 0 VISTA DEFAULT 
			= 1 VISTA LISTA OPERE 
			= 2 VISTA LISTA OPERE DA PUBBLICARE (ACQUISIZIONE)
			= 3 VISTA PAGINE DELL'OPERA
			= 4 
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
	        break;
	    case 3: 
	    	listaPagineOpera();
	    	break;
	    case 4: 
	    	listaUtenti();
	    	break;
	    case 5:
	    	sezione = 5;
	    	listaOpereTrascrittore();
	    	break;
	    case 6:
	    	sezione = 6;
	    	trascrizionePagina();
	    	break;
	    case 7:
	    	sezione = 7;
	    	inserisciOpera();
	    	break;
	    default:
	    	sezione=0
	    	vistaDefaultAdmin();
		};
}


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
		        data: 'tipoRicerca=listaUtenti',
		            success: function(data) {
		    	 		if (data==""){
		    	 			errore();
		    	 		}
		    	 		else {         	 			
		    	 			paginatore(data);
		    	 			admin= true;
		    	 			init();
			                document.getElementById("utenti").style.visible="yes";
    					    document.getElementById("utenti").style.display="block";
	
		    	 		}
		            }
		});
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
		         	 		if (data==""){
		         	 			errore();
		         	 		}
		         	 		else {      
		         	 			paginatore(data);
			                    admin= true;
			                    init();
				                document.getElementById("listaopere").style.visible="yes";
				                document.getElementById("listaopere").style.display="block";
				                window.location.hash='#listaopere';
		         	 		}
			             }
				});
}
function errore (){
		document.getElementById("erroreRicerca").style.visible="visible";
		document.getElementById("erroreRicerca").style.display="block";
		window.location.hash='#erroreRicerca';
}
function trascrizionePagina(){
		document.getElementById("trascrivipagina").style.display="block";
		document.getElementById("trascrivipagina").style.visibility="visible";
}

///////////////////
/* 	PAGINATORE   */
///////////////////
var pages = [];


function paginatore(data){
	var k = 0,i = 0,cont=1; 
	pages=[];
	while(i<data.length) {
	 	 var temp = [];
	 	 switch(sezione){
	 	 	case 1:
	 	 		for(j=i; j < i+3 && j < data.length; j++){
	 		 		 temp.push({riga_tabella:cont++, id: data[j].id, titolo: data[j].titolo,  descrizione:data[j].descrizione, numero_pagine:data[j].numero_pagine,editore:data[j].editore,anno:data[j].anno});                             
	 		 	}
	 	 		break;
	 	 	case 2:
	 	 		for(j=i; j < i+3 && j < data.length; j++){
	 		 		 temp.push({riga_tabella:cont++, id: data[j].id, titolo: data[j].titolo, editore:data[j].editore});                             
	 		 	}
	 	 		break;
	 	 	case 3:
	 	 		for(j=i; j < i+3 && j < data.length; j++){
	 		 		 temp.push({riga_tabella:cont++, numero_pagina: data[j].numero_pagina, opera: data[j].opera});                             
	 		 	}
	 		case 4:
	 	 		for(j=i; j < i+3 && j < data.length; j++){
	 		 		 temp.push({riga_tabella:cont++, username: data[j].username, nome: data[j].nome, cognome: data[j].cognome , email: data[j].email, ruolo: data[j].ruolo});                             
	 		 	}
	 		 	break;
	 		case 5:
	 			for(j=i; j < i+3 && j < data.length; j++){
	 		 		 temp.push({riga_tabella:cont++,id:data[j].id, titolo: data[j].titolo, descrizione: data[j].descrizione, numero_pagine: data[j].numero_pagine});
	 		 	}
	 			break;
	 	 }
	 	 
	 	 pages[k++] = temp;
	 	 i = i+3;
	}
}

//ritorna i dati di una pagina
//returns the data for a given page
function getPageData(page){
	return pages[page-1];		
}

//ritorna il numero totale di pagine
//returns the total number of pages
function getTotalPages(){
	return pages.length;
}

//crea una table row a partire dai dati nell'array (un elemento per cella)
//creates a table row from the array data (an array element, a cell)
function makeRow(datarow) {
	//creiamo la riga
	//create the row
	var row = document.createElement('tr');
	row.className = "riga_tabella";
	var i;
	//e inseriamo tante celle quanti sono gli elementi della datarow
	//and add as many cells as the datarow elements are
	var cont=0;
	for(i in datarow) {
		var cell = document.createElement('td');
		cell.textContent = datarow[i];
		row.appendChild(cell);
		row.id = "riga"+datarow.id;
	}
	//se sono admin inserisco il bottone rimuovi nella tabella 
	if (admin){
		switch (sezione){
			case 1:
				row.onclick = function(){
					self = this;
					$.ajax({
				        url: 'SelezionaOpera',
				        type: 'GET',
				        data: 'id_opera='+self.id,
				            success: function(data) {
				            	sezione = 3;
				            	if (data==""){
				        			errore();
				        		}
				            	else {
				            		paginatore(data);
				            		scelta_sezione(3);
				            	}
				            }
					});
				}
				var rimuovi = document.createElement('button');
				rimuovi.className="btn btn-danger btn-lg";
				rimuovi.id=datarow.id;
				var testo=document.createTextNode("Rimuovi");
				rimuovi.appendChild(testo);
				rimuovi.onclick= function(){
					self=this;
					$.ajax({
				         url: 'Rimuovi',
				         type: 'GET',
				         data: 'id_opera='+this.id,
				             success: function(data) {
				            	 if(eval(data)){
				            		 for(i=0; i< pages.length; i++){
				            			 for(j=0; j<pages[i].length; j++){
					            			 if(pages[i][j].id==self.id){
					            				 pages[i].splice(j,1);
					            			 }
				            			 }
				            		 }
				            		 document.getElementById("riga"+self.id).remove();
				            	 }
				            	 else 
				            		 alert("Ci dispiace! A causa di un problema non è stato possibile rimuovere l'opera");
				             }
					});
				};
				var cell = document.createElement('td');
				cell.appendChild(rimuovi);
				row.appendChild(cell);
				break;
			case 2:
				var rimuovi = document.createElement('button');
				rimuovi.className="btn btn-danger btn-lg";
				rimuovi.id=datarow.id;
				var testo=document.createTextNode("Rimuovi");
				rimuovi.appendChild(testo);
				rimuovi.onclick= function(){
					self=this;
					$.ajax({
				         url: 'Rimuovi',
				         type: 'GET',
				         data: 'id_opera='+this.id,
				             success: function(data) {
				            	 if(eval(data)){
				            		 for(i=0; i< pages.length; i++){
				            			 for(j=0; j<pages[i].length; j++){
					            			 if(pages[i][j].id==self.id){
					            				 pages[i].splice(j,1);
					            			 }
				            			 }
				            		 }
				            		 document.getElementById("riga"+self.id).remove();
				            	 }
				            	 else 
				            		 alert("Ci dispiace! A causa di un problema non è stato possibile rimuovere l'opera");
				             }
					});
				};
				var pubblica = document.createElement('button');
				pubblica.className="btn btn-primary btn-lg";
				pubblica.id=datarow.id;
				var testo=document.createTextNode("Pubblica");
				pubblica.appendChild(testo);
				var cell = document.createElement('td');
				cell.appendChild(rimuovi);
				var cell1 = document.createElement('td');
				cell1.appendChild(pubblica);
				row.appendChild(cell);
				row.appendChild(cell1);
				break;
			case 3:
				alert("caso 3");
				break;
			case 4: 
				alert("caso 4");
				break;
			
		}
	}
	else if(sezione == 5){	
			row.onclick = function(){
				self=this;
				var id = self.id ;
				id = id.slice (4, id.length);
				$.ajax({
			         url: 'Ricerca',
			         type: 'GET',
			         data: 'pagine_opera='+id,
			             success: function(data) {
			            	 if(eval(data)){
			            		 scelta_sezione(6);
			            	 }
			            	 else 
			            		 alert("Ci dispiace! A causa di un problema non è stato possibile selezionare l'opera");
			             }
				});
			};
	}
	//console.log(row);
	return row;				
}

//modifica il body di una tabella per ospitare le righe date
//se hascaption = true lascia inalterata la prima riga (intestazione)
//modifies the table body to contain the given rows
//if hascamtion=true leaves the first row unaltered (header)
function updateTable(id,data,hascaption) {

	//hascaption ha valore di default true
	//hascaption defaults to true
	if(typeof(hascaption)=='undefined') hascaption = true;
	//numero di righe da saltare all'inizio della tabella
	//number of rows skipped from the table head
	var skip = (hascaption)?1:0; 

	//preleviamo l'elemento DOM per la tabella
	//get the DOM table element
	var tab = document.getElementById(id);
	if (!id) return;
	//acquisiamo un riferimento al body della tabella (si veda il DOM HTML)
	//get a reference to the table body
	var tbody = tab.tBodies[0];
		
	//per evitare sfarfallii, sovrascriviamo le righe gi� presenti, 
	//quindi ne aggiungiamo altre o cancelliamo quelle inutili solo se necessario
	//to avoid flickering, overwrite the displayed rows
	//and add/remove rows only if strictly needed
	var oldrows = tbody.rows.length-skip;
	var i;
	//per ogni nuova riga i...
	//for any new row i...
	for(i = 0; i<data.length; ++i) {	
	//creazione delle righe HTML rappresentanti i dati
	//create a <tr> representing the data row
		var newrow = makeRow(data[i]);		
		if (i<oldrows) {
			//se esiste gi� un' i-esima riga la sovrascriviamo con i nuovi dati
			//if the i-th row already exists, overwrite it with the new data
			tbody.replaceChild(newrow,tbody.rows[i+skip]);
		} else {
			//altrimenti aggiungiamo una riga
			//otherwise add a table row
			tbody.appendChild(newrow);
		}
	}
	//infine, cancelliamo tutte le righe che non ci servono pi�
	//finally, delete all the unised extra rows
	for(i=data.length; i<oldrows; ++i) {
		tbody.removeChild(tbody.rows[data.length+skip]);
	}
}

//crea un link di paginazione, utilizzata da updatePager
//creates a pagination link, used by updatePager
function makePageLink(page) {
	var link = document.createElement("a");
	//nota: grazie all'effetto closure, la variabile page usata nella funzione anonima creata
	//qui sotto conserver� il valore passato durante la chiamata a makePageLink
	//note: thanks to the closure, the page variable used in the created function
	//is set to the page value passed to makePageLink
	link.onclick=function(){switchPage(page);}
	//l'href del link � nullo
	//the link href is null
	link.href="javascript:void(0)";
	link.textContent=page;
	link.className="pagerLink";
	
	return link;
}

//aggiorna i link di paginazione
//updates the pagination links
function updatePager(id,page) {

	//preleviamo l'elemento indicato, nel quale inseriremo il paginatore
	//get the given element, where the pager will be built
	var paging = document.getElementById(id);
	if (!paging) return;
	var i;
	
	//svuotiamo l'elemento
	//empty the element
	while(paging.hasChildNodes()) paging.removeChild(paging.firstChild);

	//inseriamo nell'elemento i link di paginazione alle pagine che precedono quella corrente
	//insert the pagination links for the previous pages
	for(i=1; i<page; ++i) {
		paging.appendChild(makePageLink(i));
	}	
	//la pagina corrente viene mostrata senza link
	//the current page is shown, but it is not a link
	var cur = document.createElement("span");
	cur.className="pagerCurrent";
	cur.textContent=page;
	paging.appendChild(cur);	
	//inseriamo nell'elemento i link di paginazione alle pagine che seguono quella corrente
	//insert the pagination links for the next pages
	for(i=page+1; i<=getTotalPages(); ++i) {
		paging.appendChild(makePageLink(i));
	}
}			

//cambia la pagina della tebella correntemente visualizzata
//chenges the table page currently displayed
function switchPage(page) {
	//acquisizione dei dati
	//load the data
	data = getPageData(page);
	//visualizzazione del nuovo set di righe
	//diaplay the rows
	if (admin){
	switch(sezione){
		case 1:
			updateTable("tableopere",data);
			updatePager("paging",page);
			break;
		case 2:
			updateTable("tableopereacquisizione",data);
			updatePager("paging1",page);
			//updateTable("tableOpereTrascrizione",data);
			//updatePager("paging2",page);
			break;
		case 3: 
			
			break;
		case 4:

			break;
		
	}
	}
	else if (sezione==5){
		updateTable("table_opere_in_trascrizione",data);
		updatePager("paging",page);
		updateTable("table_opere_da_trascrivere",data);
		updatePager("paging1",page);
	}
	else {
		updateTable("tableopere",data);
		updatePager("paging",page);
		
	}
	//aggiornamento dei link di paginazione
	//update the pager
	
}

//inizializzazione dello script
//script initialization
function init() {
	switchPage(1);
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
