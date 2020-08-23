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


@WebServlet("/days/*")
public class RESTAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String dayId, workoutId;
		DayController controller = new DayControllerClass();
		
        resp.setContentType("application/json");
        JsonObjectBuilder jsonObjectBuilder = null;
        
        String[] splits = req.getRequestURI().split("/");
        switch(splits.length) {
        case 3: // /lists
        	jsonObjectBuilder = controller.getDaysJsonObjectBuilder(); 
        	
        	JsonObject jObj = jsonObjectBuilder.build();
        	
        	JSONObject obj = new JSONObject(jObj.toString());
        	
        	JSONArray matches = obj.optJSONArray("gym");

        	System.out.println(matches);
        	
        	
        	for (int i = 0, len = matches.length(); i < len; i++) {
        	    JSONObject objWorkouts = matches.getJSONObject(i);
        	    //Remover as tasks
        	    objWorkouts.remove("workouts");
        	}
        	
        	System.out.println(matches);
        	
   	
        	List<DayClass> listas = new Gson().fromJson(matches.toString(), new TypeToken<List<DayClass>>() {}.getType());
            req.setAttribute("listas", listas);
    		
    		getServletConfig().getServletContext().getRequestDispatcher("/dates.jsp").forward(req,resp);
    		
            //jsonWriter.close();
            
            controller.close();
            
       	
        	
        	break;
        case 4: // /lists/<listId>
        	dayId = splits[3];
        	if(!controller.hasDayById(dayId)) {
        		jsonObjectBuilder = setError("There is no task list with id "+dayId+".");
        	}
        	else {
        		jsonObjectBuilder = controller.getDayById(dayId).getJsonObjectBuilder();
        		
        		
        		JsonObject jObj2 = jsonObjectBuilder.build();
        		System.out.println(jObj2.toString());
            
            	JSONObject obj2 = new JSONObject(jObj2.toString());
            	
            	JSONArray matches2 = obj2.optJSONArray("workouts");
            	System.out.println("matches2:"+matches2);
            	
            	List<WorkoutClass> listaTodosWorkouts = new Gson().fromJson(matches2.toString(), new TypeToken<List<WorkoutClass>>() {}.getType());
                req.setAttribute("listaTodosWorkouts", listaTodosWorkouts);
                
                getServletConfig().getServletContext().getRequestDispatcher("/datesWorkouts.jsp").forward(req,resp);

            	controller.close();
        		
        		
        	}
        	break;
        case 5: // /lists/<listId>/<workoutId>
        	dayId = splits[3];
        	workoutId = splits[4];
        	if(!controller.hasDayById(dayId)) {
        		jsonObjectBuilder = setError("There is no task list with id "+dayId+".");
        	}
        	else if (!controller.hasWorkout(dayId, workoutId)) {
        		jsonObjectBuilder = setError("There is no task with id "+workoutId+" in task list with id "+dayId+".");
        	}
        	else {
        		jsonObjectBuilder = controller.getDayById(dayId).getWorkout(workoutId).getJsonObjectBuilder();
        		
        		JsonObject jObj3 = jsonObjectBuilder.build();
        		System.out.println(jObj3.toString());
            
            	JSONObject obj3 = new JSONObject(jObj3.toString());
            	
            	JSONArray matches3 = new JSONArray();
            	matches3.put(obj3);
            	System.out.println("matches3: "+matches3);
   
            	List<WorkoutClass> workoutList = new Gson().fromJson(matches3.toString(), new TypeToken<List<WorkoutClass>>() {}.getType());
                req.setAttribute("workoutList", workoutList);
                
                getServletConfig().getServletContext().getRequestDispatcher("/workout.jsp").forward(req,resp);

            	controller.close();
        		
        		
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
		String dayId, workoutId, dayData, workoutDescription, workoutInicio;
		DayController controller = new DayControllerClass();
		
        resp.setContentType("application/json");
        JsonObjectBuilder jsonObjectBuilder = null;
     

        JsonReader jsonReader = Json.createReader(req.getReader());

    	JsonObject jsonObject = jsonReader.readObject();
    	jsonReader.close();

        String[] splits = req.getRequestURI().split("/");
        switch(splits.length) {
        case 3: // /lists
        	dayData = jsonObject.getString("data");
        	if(controller.hasDayByData(dayData)) {
        		jsonObjectBuilder = setError("There is already a task list with name "+dayData+".");
        	}
        	else {
        		jsonObjectBuilder = controller.createDay(dayData).getJsonObjectBuilder();
        	}
        	break;
        case 4: // /lists/<listId>
        	dayId = splits[3];
        	workoutDescription = jsonObject.getString("description");
        	workoutInicio = jsonObject.getString("inicio");
        	if(!controller.hasDayById(dayId)) {
        		jsonObjectBuilder = setError("No such task list.");
        	}
        	
        	else if(Integer.parseInt(workoutInicio) > 22 || Integer.parseInt(workoutInicio) < 10) {
        		jsonObjectBuilder = setError("Hora incorreta!, horário 10h até 22h.");
        	}
        	else {
        		jsonObjectBuilder = controller.createWorkout(dayId, workoutInicio, workoutDescription).getJsonObjectBuilder();
        	}
        	break;
        case 5: // /lists/<listId>/<workoutId>
        	dayId = splits[3];
        	workoutId = splits[4];
        	if(!controller.hasDayById(dayId)) {
        		jsonObjectBuilder = setError("There is no task list with id "+dayId+".");
        	}
        	else if (!controller.hasWorkout(dayId, workoutId)) {
        		jsonObjectBuilder = setError("There is no task with id "+workoutId+" in task list with id "+dayId+".");
			}
			
        	else {
        		jsonObjectBuilder = controller.changeWorkoutStatus(dayId, workoutId).getJsonObjectBuilder();
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
		String dayId, workoutId, dayData, workoutDescription;
		DayController controller = new DayControllerClass();
		
        resp.setContentType("application/json");
        JsonObjectBuilder jsonObjectBuilder = null;
        
        JsonReader jsonReader = Json.createReader(req.getReader());
    	JsonObject jsonObject = jsonReader.readObject();
    	jsonReader.close();
        
        String[] splits = req.getRequestURI().split("/");
        switch(splits.length) {
        case 4: // /lists/<listId>
        	dayId = splits[3];
        	dayData = jsonObject.getString("data");
        	if(!controller.hasDayById(dayId)) {
        		jsonObjectBuilder = setError("No such task list.");
        	}
        	else {
        		jsonObjectBuilder = controller.changeDayDataById(dayId, dayData).getJsonObjectBuilder();
        	}
        	break;
        case 5: // /lists/<listId>/<workoutId>
        	dayId = splits[3];
        	workoutId = splits[4];
        	workoutDescription = jsonObject.getString("description");
        	if(!controller.hasDayById(dayId)) {
        		jsonObjectBuilder = setError("There is no task list with id "+dayId+".");
        	}
        	else if (!controller.hasWorkout(dayId, workoutId)) {
        		jsonObjectBuilder = setError("There is no task with id "+workoutId+" in task list with id "+dayId+".");
        	}
        	else {
        		jsonObjectBuilder = controller.changeWorkoutDescription(dayId, workoutId, workoutDescription).getJsonObjectBuilder();
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
		String dayId, workoutId;
		DayController controller = new DayControllerClass();
		
        resp.setContentType("application/json");
        JsonObjectBuilder jsonObjectBuilder = null;
        
        String[] splits = req.getRequestURI().split("/");
        switch(splits.length) {
        case 4: // /lists/<listId>
        	dayId = splits[3];
        	if(!controller.hasDayById(dayId)) {
        		jsonObjectBuilder = setError("No such task list.");
        	}
        	else {
        		controller.deleteDay(dayId);
        		jsonObjectBuilder = setSuccess("Workout list removed successfully.");
        	}
        	break;
        case 5: // /lists/<listId>/<workoutId>
        	dayId = splits[3];
        	workoutId = splits[4];
        	if(!controller.hasDayById(dayId)) {
        		jsonObjectBuilder = setError("There is no task list with id "+dayId+".");
        	}
        	else if (!controller.hasWorkout(dayId, workoutId)) {
        		jsonObjectBuilder = setError("There is no task with id "+workoutId+" in task list with id "+dayId+".");
        	}
        	else {
        		controller.deleteWorkout(dayId, workoutId);
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

