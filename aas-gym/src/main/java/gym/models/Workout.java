package gym.models;

import javax.json.JsonObjectBuilder;

public interface Workout {
	

	String getDescription();

	String getInicio();
	
	int getId();

	void setDescription(String workoutDescription);
	
	void setInicio(String workoutInicio);
	//void toggle();
	
	JsonObjectBuilder getJsonObjectBuilder();
}
