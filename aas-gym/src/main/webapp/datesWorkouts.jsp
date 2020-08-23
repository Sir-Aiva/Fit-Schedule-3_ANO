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
					<th>Start</th>
					<th>Description</th>>
				</tr>
			</thead>
			<tbody>
			    <%! int i = 0; %>				
				<c:forEach items="${listaTodosWorkouts}" var="listaTodosWorkouts">			
					<tr>
						<td>${listaTodosWorkouts.id}</td>
						<td>${listaTodosWorkouts.inicio}:00</td>	
						<td><a href="./${listaTodosWorkouts.id}/"/>${listaTodosWorkouts.description}</td>
						
					</tr>
				</c:forEach>
			</tbody>
		</table>
			<p>Marcar Workout</p>
			<form name="input"  id="form1"  enctype="text/plain">
		    <select class="btn btn-secondary dropdown-toggle" name="hora" id="hora">
		    <option value="10">10</option>
		    <option value="11">11</option>
		    <option value="12">12</option>
		    <option value="13">13</option>
		    <option value="14">14</option>
		    <option value="15">15</option>
		    <option value="16">16</option>
		    <option value="17">17</option>
		    <option value="18">18</option>
		    <option value="19">19</option>
		    <option value="20">20</option>
		    <option value="21">21</option>
		    <option value="22">22</option>
		  </select>
		  
		      <select class="btn btn-secondary dropdown-toggle" name="aula" id="aula">
		    <option value="Treino Livre">Treino Livre</option>
		    <option value="Aula de Pilates">Aula de Pilates</option>
		    <option value="Treino Personalizado">Treino Personalizado</option>
		    <option value="Aula de Spin">Aula de Spin</option>
		    <option value="Body Combat">Body Combat</option>
		    <option value="Body Pump">Body Pump</option>
		    <option value="Jump Fit">Jump Fit</option>
		    <option value="Zumba">Zumba</option>
		    <option value="Yoga">Yoga</option>
		  </select>

		    <input type="submit" onclick="send()">
		</form>
		
		<script src="https://code.jquery.com/jquery-1.10.2.js"
	type="text/javascript"></script>
<script src="js/app-ajax.js" type="text/javascript"></script>

<script>
    function send() {
        var pacote = {
            inicio: $("#hora").val(),
            description: $("#aula").val(),
        }

        var link = $(location).attr('pathname')+$(location).attr('hash');
       

        $.ajax({
            url: link,
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
	
</body>
</html>
</body>
</html>