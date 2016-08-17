/*

WEB EGINEERING COURSE - University of L'Aquila 

This example shows a simple technique for client-side table pagination.
Table data is loaded using two access functions that can be rewritten
to use any data source (e.g., AJAX calls, ecc.)

See the course homepage: http://www.di.univaq.it/gdellape/students.php

*/

//crea una table row a partire dai dati nell'array (un elemento per cella)
//creates a table row from the array data (an array element, a cell)
function makeRow(datarow) {
	//creiamo la riga
	//create the row
	var row = document.createElement('tr');
    var cellstylecallback = getCellStyleCallback();
	var i;
	//e inseriamo tante celle quanti sono gli elementi della datarow
	//and add as many cells as the datarow elements are
	for(i in datarow) {
		var cell = document.createElement('td');
		cell.textContent = datarow[i];
		if (typeof(cellstylecallback)=="function") cellstylecallback(cell.style, datarow,i);		
		row.appendChild(cell);
	}
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
	//qui sotto conserverà il valore passato durante la chiamata a makePageLink
	//note: thanks to the closure, the page variable used in the created function
	//is set to the page value passed to makePageLink
	link.onclick=function(){switchPage(page);}
	//l'href del link è nullo
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
	updateTable("voti",data);
	//aggiornamento dei link di paginazione
	//update the pager
	updatePager("paging",page);
}

//inizializzazione dello script
//script initialization
function init() {
	switchPage(1);
}

window.onload=init;