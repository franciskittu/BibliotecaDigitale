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
                         console.log((data));
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

	                     document.getElementById("divlistaopere").style.display="inline";
	                     document.getElementById("divlistaopere").innerHTML = "<p>Go to page: <span id=\"paging\"><span class=\"pagerCurrent\">1</span><a class=\"pagerLink\" href=\"?page=2\">2</a><a class=\"pagerLink\" href=\"?page=3\">3</a></span></p><table border=\"1\" id=\"voti\"><tr><th>Number</th><th>Grade</th></tr><tr><td>124610</td><td>30</td></tr><tr><td>112233</td><td>28</td></tr></table>";
             }
         });
     };		


function controllausername(obj){
            //$("#username").keypress(function(){
            $.ajax({
            url: 'Registrazione',
            type: 'POST',
            data: 'usernameAjax='+obj.value,
                success: function(data) {
                            console.log((data));
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
                                console.log((data));
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
