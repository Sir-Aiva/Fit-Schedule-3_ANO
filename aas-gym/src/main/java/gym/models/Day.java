package gym.models;

import java.util.List;

import javax.json.JsonObjectBuilder;

public interface Day {

	String getData();

	List<Workout> getWorkouts();
	
	int getId();
	
	void setData(String name);

	void removeWorkout(String workoutId);

	boolean hasWorkout(String workoutId);

	Workout getWorkout(String workoutId);

	Workout addWorkout(String description, String status);
	
	JsonObjectBuilder getJsonObjectBuilder();
}
