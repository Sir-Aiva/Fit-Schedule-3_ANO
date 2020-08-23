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
<script src="https://code.jquery.com/jquery-1.10.2.js"
	type="text/javascript"></script>
<script src="js/app-ajax.js" type="text/javascript"></script>
<table class = "table table-striped table-bordered" id="datatable">
			<thead>
				<tr class = "thead-dark">
					<th>ID</th>
					<th>Inicio</th>
					<th>Description</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<%! int i = 0; %>
				<c:forEach items="${workoutList}" var="workoutList">
					<tr>
						<td>${workoutList.id}</td>
						<td>${workoutList.inicio}:00</td>
						<td>${workoutList.description}</td>
						<th><button class="btn btn-danger" onclick = deleteWork()>Apagar</button></th>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<script>

			var urlWork = $(location).attr('href');
			
			
			function deleteWork(){
				$.ajax({
					url: urlWork,
					method: 'DELETE',
					success: function(){
						alert('Registo Eliminado');
						window.location.href = "http://localhost:8080/tasklists/days/";
			
					},
					error: function(error){
					alert(error);
					}
				})
			}
			
			</script>	

</body>
</html>
 
</body>
</html>