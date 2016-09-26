
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
	 		 		 temp.push({riga_tabella:cont++, id:data[j].id, numero_pagina: data[j].numero, immagine_validata: data[j].immagine_validata});                             
	 		 	}
	 	 		break;
	 		case 4:
	 	 		for(j=i; j < i+3 && j < data.length; j++){
	 		 		 temp.push({riga_tabella:cont++, id: data[j].id, nomeutente: data[j].nomeutente, nome: data[j].nome, cognome: data[j].cognome , email: data[j].email,ruolo : vettore_ausiliario[j].nome_ruolo});
	 		 	}
	 		 	break;
	 		case "trascrittore":
	 			for(j=i; j < i+3 && j < data.length; j++){
	 		 		 temp.push({riga_tabella:cont++,id:data[j].id, titolo: data[j].titolo, descrizione: data[j].descrizione, numero_pagine: data[j].numero_pagine});
	 		 	}
	 			break;
	 		case "revisore_acquisizioni":
	 			for(j=i; j < i+3 && j < data.length; j++){
	 		 		 temp.push({riga_tabella:cont++,id:data[j].id, titolo: data[j].titolo, descrizione: data[j].descrizione, numero_pagine: data[j].numero_pagine});
	 		 	}
	 			break;
	 		case "revisore_trascrizione":
	 			for(j=i; j < i+3 && j < data.length; j++){
	 		 		 temp.push({riga_tabella:cont++,id:data[j].id, titolo: data[j].titolo, descrizione: data[j].descrizione, numero_pagine: data[j].numero_pagine});
	 		 	}
	 			break;
	 		case "ricerca":
	 			 for(j=i; j < i+3 && j < data.length; j++){
            		 temp.push({id: data[j].id, titolo: data[j].titolo,  descrizione:data[j].descrizione, numero_pagine:data[j].numero_pagine });
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
	var cont=0;
	var titolo_opera=[];
	//e inseriamo tante celle quanti sono gli elementi della datarow
	//and add as many cells as the datarow elements are
	for(i in datarow) {	
		if (i == "titolo"){
			titolo_opera[i]=datarow[i];
		}
		var cell = document.createElement('td');
		cell.textContent = datarow[i];
		row.appendChild(cell);
	}
	row.id = "riga"+datarow.id;
	//se sono admin inserisco il bottone rimuovi nella tabella 
	if (admin){
		switch (sezione){
			case 1:
				row.onclick = function(){
					
				}
				var visualizza = document.createElement('button');
				visualizza.className = "btn btn-primary btn-lg";
				var testoVisualizza= document.createTextNode("Visualizza");
				visualizza.appendChild(testoVisualizza);
				visualizza.id="visu"+datarow.id;
				visualizza.onclick = function(){
					self=this;
					var id = self.id;
					id = id.slice(4,id.length);	
					$.ajax({
				         url: 'Ricerca',
				         dataType: 'json',
				         type: 'GET',
				         data: 'tipoRicerca=pagine_opera&id_opera='+id,
				             success: function(data) {
				            	 if(data.length==0){
				            		alert("Non ci sono pagine per quest'opera!");
				            	 }
				            	 else{
				            		 document.getElementById("titoloListaPagineOpera").innerHTML=titolo_opera.titolo;
				            		 admin= true;
				            		 vettore_ausiliario = data;
				            		 scelta_sezione(3);
				            		 
				            	 }
				             },
				             error: function(data){
				            	 	console.log(data);
				            	 	alert("ERRORE GRAVE!!! Contattare l'amministratore.")
							}
					});
				}
				var cell1 = document.createElement('td');
				cell1.appendChild(visualizza);
				row.appendChild(cell1);
				
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
				             },
				             error: function(data){
				            	 	console.log(data);
				            	 	alert("ERRORE GRAVE!!! Contattare l'amministratore.")
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
				             },
				             error: function(data){
				            	 	console.log(data);
				            	 	alert("ERRORE GRAVE!!! Contattare l'amministratore.")
							}
					});
				};
				var pubblica = document.createElement('button');
				pubblica.className="btn btn-primary btn-lg";
				pubblica.id=datarow.id;
				pubblica.onclick = function(){
					self=this;
					if(table_opere_acquisizione == true){
					$.ajax({
				         url: 'Aggiorna',
				         type: 'GET',
				         data: 'tipoAggiornamento=pubblicazione_acquisizioni&id_opera='+self.id,
				             success: function(data) {
				            	 if(eval(data)){
				            		 alert("Opera pubblicata!!")
				            	 }
				             },
				            error: function(data){
				            	 	console.log(data);
				            	 	alert("ERRORE GRAVE!!! Contattare l'amministratore.")
							}
					});
					}
					else{
						$.ajax({
					         url: 'Aggiorna',
					         type: 'GET',
					         data: 'tipoAggiornamento=pubblicazione_trascrizioni&id_opera='+self.id,
					             success: function(data) {
					            	 if(eval(data)){
					            		 alert("Opera pubblicata!!")
					            	 }
					             },
					             error: function(data){
					            	 	console.log(data);
					            	 	alert("ERRORE GRAVE!!! Contattare l'amministratore.")
								}
						});
					}
				};
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
				         data: 'id_pagina='+self.id,
				             success: function(data) {
				            	 alert("Pagina rimossa correttamente");
				             }
					});
				};
				row.appendChild(rimuovi);
				break;
			case 4: 
				var rimuovi = document.createElement('button');
				rimuovi.className="btn btn-danger btn-lg";
				var testo=document.createTextNode("Rimuovi");
				rimuovi.id=datarow.id;
				rimuovi.appendChild(testo);
				rimuovi.onclick= function(){
					self=this;
					$.ajax({
				         url: 'Rimuovi',
				         type: 'GET',
				         data: 'id_utente='+self.id,
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
				             },
							error: function(data){
			            	 	console.log(data);
			            	 	alert("ERRORE GRAVE!!! Contattare l'amministratore.")
							}
					});
				};
				row.onclick = function(){
					gestioneRuoloUtente(this);
				}
				var cell = document.createElement('td');
				cell.appendChild(rimuovi);
				row.appendChild(cell);
				break;
			
		}
	}
	else if(sezione == "trascrittore"){
			row.onclick = function(){
				self=this;
				var id = self.id ;
				id = id.slice (4, id.length);
				$.ajax({
			         url: 'Ricerca',
			         dataType: 'json',
			         type: 'GET',
			         data: 'tipoRicerca=pagine_opera&id_opera='+id,
			             success: function(data) {
			            	 if(data.length > 0){
			            		 	scelta_sezione("editor_trascrittore");
				         	    	trascrizionePagina(id);
				         	    	
			            	 }
			            	 else 
				            		 alert("L'opera non ha ancora nessuna pagina con immagine validata!");
			             },
			             error: function(data){
			            	 	console.log(data);
			            	 	alert("ERRORE GRAVE!!! Contattare l'amministratore.")
							}
				});
			};
	}
	else if(sezione == "revisore_acquisizioni"){	
		row.onclick = function(){
			self=this;
			var id = self.id ;
			id = id.slice (4, id.length);
			$.ajax({
		         url: 'Ricerca',
		         dataType: 'json',
		         type: 'GET',
		         data: 'tipoRicerca=pagine_opera&id_opera='+id,
		             success: function(data) {
		            	 if(data.length > 0){
		            		 	scelta_sezione("convalida_revisore_acquisizioni");
			         	    	convalidaPagina(data);	
		            	 }
		            	 else 
			            		 alert("L'opera non ha ancora nessuna pagina con immagine validata!");
		             },
		             error: function(data){
		            	 	console.log(data);
		            	 	alert("ERRORE GRAVE!!! Contattare l'amministratore.")
						}
			});
		};
	}
	else if(sezione == "revisore_trascrizione"){
		row.onclick = function(){
			self=this;
			var id = self.id ;
			id = id.slice (4, id.length);
			$.ajax({
		         url: 'Ricerca',
		         dataType: 'json',
		         type: 'GET',
		         data: 'tipoRicerca=pagine_opera&id_opera='+id,
		             success: function(data) {
		            	 console.log(data);
		            	 if(data.length > 0){
		            		 	scelta_sezione("pagine_con_trascrizioni_da_convalidare");
		            		 	pagine_con_trascrizioni_da_convalidare(data);
		            	 }
		            	 else 
			            		 alert("L'opera non ha ancora nessuna pagina con trascrizione validata!");
		             },
		             error: function(data){
		            	 	console.log(data);
		            	 	alert("ERRORE GRAVE!!! Contattare l'amministratore.")
					}
			});
		};
	}
	else if (sezione == "ricerca"){
		row.onclick = function(){
			self=this;
			var id = self.id ;
			id = id.slice (4, id.length);
			$.ajax({
		         url: 'Ricerca',
		         dataType: 'json',
		         type: 'GET',
		         data: 'tipoRicerca=pagine_opera&id_opera='+id,
		             success: function(data) {
		            	 if(data.length > 0){
		            		 scelta_sezione("ricerca");
	            			 pagine_con_trascrizioni_da_convalidare(data);
	            			 document.getElementById("ul_navbar").style.display="none";
		            	 }
		            	 else{ 
			            	 alert("Impossibile visionare l'opera!");
		            	 }
		             },
					error: function(data){
						alert("ERRORE GRAVE!!! Contattare l'amministratore.")
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
	var skip = 0; 

	//preleviamo l'elemento DOM per la tabella
	//get the DOM table element
	var tab = document.getElementById(id);
	if (!id) return;
	//acquisiamo un riferimento al body della tabella (si veda il DOM HTML)
	//get a reference to the table body
	var tbody = tab.tBodies[0];
		
	//per evitare sfarfallii, sovrascriviamo le righe già presenti, 
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
			//se esiste già un' i-esima riga la sovrascriviamo con i nuovi dati
			//if the i-th row already exists, overwrite it with the new data
			tbody.replaceChild(newrow,tbody.rows[i+skip]);
		} else {
			//altrimenti aggiungiamo una riga
			//otherwise add a table row
			tbody.appendChild(newrow);
		}
	}
	//infine, cancelliamo tutte le righe che non ci servono più
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
			if(table_opere_acquisizione){
			updateTable("tableopereacquisizione",data);
			updatePager("paging1",page);
			}
			else{
			updateTable("tableOpereTrascrizione",data);
			updatePager("paging2",page);
			}
			break;
		case 3: 
			updateTable("listapagine",data);
			updatePager("paging3",page);
			break;
		case 4:
			updateTable("tableutenti",data);
			updatePager("paging4",page);
			break;
		
	}
	}
	else if (sezione=="trascrittore"){
		if(table_opere_in_trascrizione){
		updateTable("table_opere_in_trascrizione",data);
		updatePager("paging",page);
		}
		else{
		updateTable("table_opere_da_trascrivere",data);
		updatePager("paging1",page);
		}
	}
	else if (sezione == "revisore_acquisizioni"){
	updateTable("table_opere_da_convalidare",data);
	updatePager("paging2",page);
	}
	else if (sezione == "revisore_trascrizione"){
		updateTable("table_opere_da_convalidare_trascrittore",data);
		updatePager("paging3",page);
	}
	else if (sezione == "ricerca"){
		updateTable("tablericerca",data);
		updatePager("pagingricerca",page);
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
