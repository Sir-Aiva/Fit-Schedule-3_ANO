<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="javax.json.JsonObject" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/bs4/dt-1.10.20/datatables.min.css"/>
</head>
<body>

<!DOCTYPE html>
<html>
<body>
<table class = "table table-striped table-bordered" id="datatable">
			<thead>
				<tr class = "thead-dark">
					<th>ID</th>
					<th>Date</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<%! int i = 0; %>
				<c:forEach items="${listas}" var="lista">
					<tr>
						<td>${lista.id}</td>
						<td><a href="/tasklists/days/${lista.id}/">${lista.data}</td>
						<th><button class="btn btn-danger" onclick = deleteData(${lista.id})>Apagar</button></th>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div>
		
<script src="https://code.jquery.com/jquery-1.10.2.js"
	type="text/javascript"></script>
<script src="js/app-ajax.js" type="text/javascript"></script>

<script>
    function send() {
        var pacote = {
            data: $("#date").val()
        }

        $.ajax({
            url: '/tasklists/days/',
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
            	alert('Enviado!');
            	alert(JSON.stringify(pacote));
            },
            data: JSON.stringify(pacote)
                        
        });
        location.reload(true)
                
    }

    
    
</script>

<script>
function deleteData(id){
	$.ajax({
		url: 'http://localhost:8080/tasklists/days/'+id,
		method: 'DELETE',
		success: function(){
			alert('Registo Eliminado');
			location.reload();

		},
		error: function(error){
		alert(error);
		}
	})
}
</script>
		<p>Nova Data:</p>
		<form name="input" action="/tasklists/days/" id="form1"  enctype="text/plain">
		    <input type="date" id="date">
		    <input type="submit" onclick="send()">
		</form>

</body>
</html>
 
</body>
</html>