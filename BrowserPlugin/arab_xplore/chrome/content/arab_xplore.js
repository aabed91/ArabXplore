var textHighlighter = function () {
	var xmlhttp = null;
	if (window.XMLHttpRequest) {
  		xmlhttp = new XMLHttpRequest();
  		if ( typeof xmlhttp.overrideMimeType != 'undefined') { 
    			xmlhttp.overrideMimeType('text/xml'); 
  		}
	} else if (window.ActiveXObject) {
  		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	} else {
  		alert('Perhaps your browser does not support xmlhttprequests?');
	}

	xmlhttp.open('GET', "http://localhost:8080/SimpleService/rest/annotate", true);
	xmlhttp.send(null);
	xmlhttp.onreadystatechange = function() {
  		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			var jsonResult = xmlhttp.responseText;
			window.alert(jsonResult);
					
	  	} else {
   		// wait for the call to complete
					
  		}
	}

	//window.alert("Abbas");
};