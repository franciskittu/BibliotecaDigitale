
/*

WEB EGINEERING COURSE - University of L'Aquila 

Data access functions for simple pager - local version
data to be paged is contained in local javascript structures

See the course homepage: http://www.di.univaq.it/gdellape/students.php

*/

//database
var pages = [
	[{matricola:"124610",voto:30},{matricola:"112233",voto:28}],
	[{matricola:"555555",voto:19}],
	[{matricola:"556677",voto:19},{matricola:"667788",voto:27},{matricola:"888888",voto:21}]
];

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

//ritorna la funzione callback usata per dare stile alle celle create dal paginatore in base ai dati contenuti
//returns a callback function used to style the cells created by the pager (in this case, looking at the cell contents)
function getCellStyleCallback() {
 return function cellStyleForData(cellstyle,datarow,index) {
	//se i dati riportano un voto minore di venti, tutte le celle della riga saranno rosse
	//if the grade is less than 20, all the riw cells are red
	if (datarow.voto < 20) cellstyle.backgroundColor = "red";
	else if (datarow.voto < 25) cellstyle.backgroundColor = "yellow";
 }
}

//------------------------------------------------------------------------------------

