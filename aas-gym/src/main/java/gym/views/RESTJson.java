package gym.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import gym.controllers.DayController;
import gym.controllers.DayControllerClass;
import gym.models.WorkoutClass;
import gym.models.DayClass;

import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/json/*")
public class RESTJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String taskListId, taskId;
		DayController controller = new DayControllerClass();
		
        resp.setContentType("application/json");
        JsonObjectBuilder jsonObjectBuilder = null;
        
        String[] splits = req.getRequestURI().split("/");
        switch(splits.length) {
        case 3: // /lists
        	jsonObjectBuilder = controller.getDaysJsonObjectBuilder(); 
        	                 	
        	break;
        case 4: // /lists/<listId>
        	taskListId = splits[3];
        	if(!controller.hasDayById(taskListId)) {
        		jsonObjectBuilder = setError("There is no task list with id "+taskListId+".");
        	}
        	else {
        		jsonObjectBuilder = controller.getDayById(taskListId).getJsonObjectBuilder();

        	}
        	break;
        case 5: // /lists/<listId>/<taskId>
        	taskListId = splits[3];
        	taskId = splits[4];
        	if(!controller.hasDayById(taskListId)) {
        		jsonObjectBuilder = setError("There is no task list with id "+taskListId+".");
        	}
        	else if (!controller.hasWorkout(taskListId, taskId)) {
        		jsonObjectBuilder = setError("There is no task with id "+taskId+" in task list with id "+taskListId+".");
        	}
        	else {
        		jsonObjectBuilder = controller.getDayById(taskListId).getWorkout(taskId).getJsonObjectBuilder();
              		
        		
        	}
        	break;
        default:
        	jsonObjectBuilder = setError(resp, "Unsupported operation.");
        }
        
        JsonWriter jsonWriter = Json.createWriter(resp.getWriter());
        jsonWriter.writeObject(jsonObjectBuilder.build());
        
        jsonWriter.close();
        
        controller.close();
  
    }
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String taskListId, taskId, taskListData, taskDescription, taskStatus;
		DayController controller = new DayControllerClass();
		
        resp.setContentType("application/json");
        JsonObjectBuilder jsonObjectBuilder = null;
     

        JsonReader jsonReader = Json.createReader(req.getReader());

    	JsonObject jsonObject = jsonReader.readObject();
    	jsonReader.close();

        String[] splits = req.getRequestURI().split("/");
        switch(splits.length) {
        case 3: // /lists
        	taskListData = jsonObject.getString("data");
        	if(controller.hasDayByData(taskListData)) {
        		jsonObjectBuilder = setError("There is already a task list with name "+taskListData+".");
        	}
        	else {
        		jsonObjectBuilder = controller.createDay(taskListData).getJsonObjectBuilder();
        	}
        	break;
        case 4: // /lists/<listId>
        	taskListId = splits[3];
        	taskDescription = jsonObject.getString("description");
        	taskStatus = jsonObject.getString("inicio");
        	if(!controller.hasDayById(taskListId)) {
        		jsonObjectBuilder = setError("No such task list.");
        	}
        	else {
        		jsonObjectBuilder = controller.createWorkout(taskListId, taskStatus, taskDescription).getJsonObjectBuilder();
        	}
        	break;
        case 5: // /lists/<listId>/<taskId>
        	taskListId = splits[3];
        	taskId = splits[4];
        	if(!controller.hasDayById(taskListId)) {
        		jsonObjectBuilder = setError("There is no task list with id "+taskListId+".");
        	}
        	else if (!controller.hasWorkout(taskListId, taskId)) {
        		jsonObjectBuilder = setError("There is no task with id "+taskId+" in task list with id "+taskListId+".");
        	}
        	else {
        		jsonObjectBuilder = controller.changeWorkoutStatus(taskListId, taskId).getJsonObjectBuilder();
        	}
        	break;
        default:
        	jsonObjectBuilder = setError(resp, "Unsupported operation.");
        }
        
        
        
        JsonWriter jsonWriter = Json.createWriter(resp.getWriter());
        jsonWriter.writeObject(jsonObjectBuilder.build());
        jsonWriter.close();
        
        controller.close();
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String taskListId, taskId, taskListData, taskDescription;
		DayController controller = new DayControllerClass();
		
        resp.setContentType("application/json");
        JsonObjectBuilder jsonObjectBuilder = null;
        
        JsonReader jsonReader = Json.createReader(req.getReader());
    	JsonObject jsonObject = jsonReader.readObject();
    	jsonReader.close();
        
        String[] splits = req.getRequestURI().split("/");
        switch(splits.length) {
        case 4: // /lists/<listId>
        	taskListId = splits[3];
        	taskListData = jsonObject.getString("data");
        	if(!controller.hasDayById(taskListId)) {
        		jsonObjectBuilder = setError("No such task list.");
        	}
        	else {
        		jsonObjectBuilder = controller.changeDayDataById(taskListId, taskListData).getJsonObjectBuilder();
        	}
        	break;
        case 5: // /lists/<listId>/<taskId>
        	taskListId = splits[3];
        	taskId = splits[4];
        	taskDescription = jsonObject.getString("description");
        	if(!controller.hasDayById(taskListId)) {
        		jsonObjectBuilder = setError("There is no task list with id "+taskListId+".");
        	}
        	else if (!controller.hasWorkout(taskListId, taskId)) {
        		jsonObjectBuilder = setError("There is no task with id "+taskId+" in task list with id "+taskListId+".");
        	}
        	else {
        		jsonObjectBuilder = controller.changeWorkoutDescription(taskListId, taskId, taskDescription).getJsonObjectBuilder();
        	}
        	break;
        default:
        	jsonObjectBuilder = setError(resp, "Unsupported operation.");
        }
        
        JsonWriter jsonWriter = Json.createWriter(resp.getWriter());
        jsonWriter.writeObject(jsonObjectBuilder.build());
        jsonWriter.close();
        
        controller.close();
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String taskListId, taskId;
		DayController controller = new DayControllerClass();
		
        resp.setContentType("application/json");
        JsonObjectBuilder jsonObjectBuilder = null;
        
        String[] splits = req.getRequestURI().split("/");
        switch(splits.length) {
        case 4: // /lists/<listId>
        	taskListId = splits[3];
        	if(!controller.hasDayById(taskListId)) {
        		jsonObjectBuilder = setError("No such task list.");
        	}
        	else {
        		controller.deleteDay(taskListId);
        		jsonObjectBuilder = setSuccess("Workout list removed successfully.");
        	}
        	break;
        case 5: // /lists/<listId>/<taskId>
        	taskListId = splits[3];
        	taskId = splits[4];
        	if(!controller.hasDayById(taskListId)) {
        		jsonObjectBuilder = setError("There is no task list with id "+taskListId+".");
        	}
        	else if (!controller.hasWorkout(taskListId, taskId)) {
        		jsonObjectBuilder = setError("There is no task with id "+taskId+" in task list with id "+taskListId+".");
        	}
        	else {
        		controller.deleteWorkout(taskListId, taskId);
        		jsonObjectBuilder = setSuccess("Workout removed successfully.");
        	}
        	break;
        default:
        	jsonObjectBuilder = setError(resp, "Unsupported operation.");
        }
        
        JsonWriter jsonWriter = Json.createWriter(resp.getWriter());
        jsonWriter.writeObject(jsonObjectBuilder.build());
        jsonWriter.close();
        
        controller.close();
	}
	
	private static JsonObjectBuilder setMessage(String key, String message) {
		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		jsonObjectBuilder.add(key, message);
		return jsonObjectBuilder;
	}
	
	private static JsonObjectBuilder setSuccess(String message) {
		return setMessage("message", message);
	}
	
	private static JsonObjectBuilder setError(String message) {
		return setMessage("error", message);
	}
	
	private static JsonObjectBuilder setError(HttpServletResponse resp, String message) {
		resp.setStatus(400);
		return setError(message);
	}
}

