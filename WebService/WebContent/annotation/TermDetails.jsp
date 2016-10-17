<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script>
function buildNetwork(url, term){
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

	xmlhttp.open('GET', "http://localhost:8080/SimpleService/rest/network?url="+url+"&term="+term, true);
	xmlhttp.send(null);
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
			var jsonResult = xmlhttp.responseText;
		    
			var params = [
			              'height='+screen.height,
			              'width='+screen.width,
			              'fullscreen=yes' // only works in IE, but here for completeness
			          ].join(',');
			var w = window.open("http://localhost:8080/SimpleService/network/network.jsp", "popup_window", params);
			w.myVariable = jsonResult;
			w.moveTo(0, 0);
		} else {
		 // wait for the call to complete
		

		}
	}
}


</script>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title>Insert title here</title>
</head>
<body>
<%
String term = request.getParameter("term");
String definition = request.getParameter("definition");
String dbpediaUrl = request.getParameter("url");
String baseUrl = request.getParameter("baseURL");
%>
<table width="460" border="1" align="center">
  <tr>
    <td colspan="2"><%=term%></td>
  </tr>
  <tr>
    <td width="120">Definition</td>
    <td width="324"><%=definition%></td>
  </tr>
  <tr>
    <td>Detailes</td>
    <td></td>
  </tr>
  <tr>
    <td>DBpedia URL</td>
    <td><%=dbpediaUrl%></td>
  </tr>
  <tr>
    <td>Related</td>
    <td>&lt;%=related_terms%&gt;</td>
  </tr>
</table>
<p><a href="javascript:void(0);" onclick="buildNetwork('<%=baseUrl%>','<%=term%>')">Show relations network</a></p>
</body>
</html>