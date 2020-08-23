package gym.models;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="workout")
public class WorkoutClass implements Workout {
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private int id;
	private String description;
	private String inicio; // "doing", "done"
	
	
	public WorkoutClass(String taskId) {
				
		this.id = Integer.parseInt(taskId);
	}
	
	public WorkoutClass(String description, String inicio) {
		this.description = description;
		this.inicio = inicio;
	}
	
	public WorkoutClass(String taskId, String inicio, String description) {
		this(description,inicio);
		
		this.id = Integer.parseInt(taskId);
	}

	public WorkoutClass() {
		super();
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return this.description;
	}

	public String getInicio() {
		return this.inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}
	
	public void setDescription(String taskDescription) {
		this.description = taskDescription;
	}
	
	//public void toggle() {
		//this.inicio = this.inicio == "done" ? "doing" : "done";		
	//}
	
	public JsonObjectBuilder getJsonObjectBuilder() {
		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		jsonObjectBuilder.add("id", this.getId());
		jsonObjectBuilder.add("inicio", this.getInicio());
		jsonObjectBuilder.add("description", this.getDescription());
		return jsonObjectBuilder;
	}
	

	public boolean equals(Object obj) {
		if(obj instanceof Workout) {
			return this.getId() == ((Workout)obj).getId();
		}
		return super.equals(obj);
	}	
}
