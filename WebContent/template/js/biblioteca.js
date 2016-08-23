var admin=false;

////////////////////////////////////////////////////////////
/* 	LISTA OPERE IN PUBBLICAZIONE ACQUISIZIONI PER ADMIN   */
////////////////////////////////////////////////////////////

function opereInPubblicazioneAcquisizioni(){
	$.ajax({
        url: 'Ricerca',
        dataType: "json",
        type: 'GET',
        data: 'tipoRicerca=opereInPubblicazioneAcquisizioni',
            success: function(data) {
    	 		if (data==""){
                    document.getElementById("listaopere").style.display="none";
    	 			document.getElementById("erroreRicerca").style.display="inline";
        	 		window.location.hash='#erroreRicerca';
    	 		}
    	 		else {         	 			
    	 			var k = 0;
    	 			var i = 0;
     	 			pages=[];

        	 			while(i<data.length) {
                    	 var temp = [];
                    	 var cont=0;
                    	 for(j=i; j < i+3 && j < data.length; j++){
                    		 temp.push({riga_tabella:cont++,id: data[j].id, titolo: data[j].titolo,  descrizione:data[j].descrizione, numero_pagine:data[j].numero_pagine,editore:data[j].editore,anno:data[j].anno});                             
                    	 }
                    	 pages[k++] = temp;
                    	 i = i+3;
        	 			}
                   admin= true;
                   init();
     	 			document.getElementById("erroreRicerca").style.display="none";
	                document.getElementById("listaopere").style.display="inline";
	                window.location.hash='#listaopere';
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
 	                    document.getElementById("listaopere").style.visible="none";
 	                   document.getElementById("listaopere").style.display="none";
         	 			document.getElementById("erroreRicerca").style.visible="visible";
             	 		window.location.hash='#erroreRicerca';
         	 		}
         	 		else {         	 			
         	 			var k = 0;
         	 			var i = 0;
         	 			var cont=0;
         	 			pages=[];
	         	 			while(i<data.length) {
	                     	 var temp = [];
	                     	 
	                     	 for(j=i; j < i+3 && j < data.length; j++){
	                     		 temp.push({riga_tabella:cont++, id: data[j].id, titolo: data[j].titolo,  descrizione:data[j].descrizione, numero_pagine:data[j].numero_pagine,editore:data[j].editore,anno:data[j].anno});                             
	                     	 }
	                     	 pages[k++] = temp;
	                     	 i = i+3;
	         	 			}
	                    admin= true;
	                    init();
	      	 			document.getElementById("erroreRicerca").style.visible="none";
	      	 			document.getElementById("erroreRicerca").style.display="none";
		                document.getElementById("listaopere").style.visible="yes";
		                window.location.hash='#listaopere';
         	 		}
	             }
	});
}
$('.riga_tabella').click( function() {
    window.location = $(this).find('a').attr('href');
});

///////////////////
/* 	PAGINATORE   */
///////////////////
var pages = [];

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
	row.id="riga"+datarow.id;
	row.className = "riga_tabella";
	var i;
	//e inseriamo tante celle quanti sono gli elementi della datarow
	//and add as many cells as the datarow elements are
	for(i in datarow) {
		var cell = document.createElement('td');
		cell.textContent = datarow[i];
		row.appendChild(cell);
	}
	//se sono admin inserisco il bottone rimuovi nella tabella 
	if (admin){
		var rimuovi = document.createElement('button');
		rimuovi.className="btn btn-danger btn-lg";
		rimuovi.id=datarow.id;
		var testo=document.createTextNode("rimuovi");
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
		cell.appendChild(rimuovi)
		row.appendChild(cell);
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
	updateTable("tableopere",data);
	//aggiornamento dei link di paginazione
	//update the pager
	updatePager("paging",page);
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

///////////////////////////
/* 	CONTROLLA USERNAME   */
///////////////////////////
   

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
            
            
            function controllaNumeroPagina(obj){
            	var id = document.getElementById("opera").value;
                $.ajax({
                url: 'UploadImmagine',
                type: 'POST',
                data : {numeroAJAX:obj.value, operaAJAX:id },
                //data: 'numeroAJAX='+obj.value+'&operaAJAX='+id,
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
            
(function($) {
    "use strict"; // Start of use strict

    // jQuery for page scrolling feature - requires jQuery Easing plugin
    $('.page-scroll a').bind('click', function(event) {
        var $anchor = $(this);
        $('html, body').stop().animate({
            scrollTop: ($($anchor.attr('href')).offset().top - 50)
        }, 1250, 'easeInOutExpo');
        event.preventDefault();
    });

    // Highlight the top nav as scrolling occurs
    $('body').scrollspy({
        target: '.navbar-fixed-top',
        offset: 51
    });

    // Closes the Responsive Menu on Menu Item Click
    $('.navbar-collapse ul li a:not(.dropdown-toggle)').click(function() {
        $('.navbar-toggle:visible').click();
    });

    // Offset for Main Navigation
    $('#mainNav').affix({
        offset: {
            top: 100
        }
    })

    // Floating label headings for the contact form
    $(function() {
        $("body").on("input propertychange", ".floating-label-form-group", function(e) {
            $(this).toggleClass("floating-label-form-group-with-value", !!$(e.target).val());
        }).on("focus", ".floating-label-form-group", function() {
            $(this).addClass("floating-label-form-group-with-focus");
        }).on("blur", ".floating-label-form-group", function() {
            $(this).removeClass("floating-label-form-group-with-focus");
        });
    });

})(jQuery); // End of use strict
