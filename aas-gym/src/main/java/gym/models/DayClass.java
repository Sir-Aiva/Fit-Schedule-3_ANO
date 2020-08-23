package gym.models;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="day")
public class DayClass implements Day {
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	
	private int id;
	private String data;
	
	@ElementCollection
	@OneToMany(
		    orphanRemoval = true,
		    cascade = CascadeType.ALL,
		    targetEntity = WorkoutClass.class,
		    fetch = FetchType.EAGER)
	private List<Workout> workouts;
	
	public DayClass() {
		super();
	}
	
	public DayClass(String data) {
		this.data = data;
		this.workouts = new ArrayList<Workout>();
	}

	public int getId() {
		return id;
	}

	public String getData() {
		return this.data;
	}

	public List<Workout> getWorkouts() {
		return this.workouts;
	}
	
	public void setData(String data) {
		this.data = data;		
	}
	
	public boolean hasWorkout(String taskId) {
		return this.workouts.contains(new WorkoutClass(taskId));
	}
	
	public void removeWorkout(String taskId) {
		this.workouts.remove(new WorkoutClass(taskId));
	}
	
	public Workout addWorkout(String description, String inicio) {
		int sum = 0;
		for(Workout workout : this.getWorkouts()) {
			if(workout.getInicio().equals(inicio)) {
				sum++;
			}
		}
		if(sum < 3) {   //número de pessoas autorizadas a frequentar o ginásio em simultâneo
			Workout workout = new WorkoutClass(description, inicio);
			this.workouts.add(workout);
			return workout;
		}else {
			System.out.println("At {} the gym has full capacity!".format(inicio));
			return null;
		}		
		//Workout workout = new WorkoutClass(description,status);
		//this.workouts.add(workout);
		//return workout;
	}
	
	public Workout getWorkout(String taskId) {
		return this.workouts.get(this.workouts.indexOf(new WorkoutClass(taskId)));
	}
	
	public JsonObjectBuilder getJsonObjectBuilder() {
		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		jsonObjectBuilder.add("id", this.getId());
        jsonObjectBuilder.add("data", this.getData());
        
        
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for(Workout workout : this.getWorkouts()) {
        	jsonArrayBuilder.add(workout.getJsonObjectBuilder());
        }
        jsonObjectBuilder.add("workouts", jsonArrayBuilder);
        return jsonObjectBuilder;
	}
}
