package gym.controllers;

import java.util.List;

import javax.json.JsonObjectBuilder;

import gym.models.Workout;
import gym.models.Day;

public interface DayController {

	List<Day> getDays();

	boolean hasWorkoutsLists();

	boolean hasDayByData(String dayData);

	Day createDay(String dayData);

	Day getDayByData(String dayData);
	
	Day getDayById(String dayId);

	Day changeDayDataByData(String dayData, String newDayData);
	
	Day changeDayDataById(String dayId, String newDayData);
	
	void deleteWorkout(String dayId, String workoutId);

	boolean hasWorkout(String dayId, String workoutId);

	Workout changeWorkoutDescription(String dayId, String workoutId, String taskDescription);

	Workout changeWorkoutStatus(String dayId, String workoutId);

	Workout createWorkout(String dayId, String status, String taskDescription);
	
	Workout getWorkout(String dayId, String workoutId);

	void deleteDay(String dayId);
	
	void close();

	boolean hasDayById(String dayId);
	
	JsonObjectBuilder getDaysJsonObjectBuilder();

}
