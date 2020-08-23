package gym.controllers;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import gym.models.Workout;
import gym.models.Day;
import gym.models.DayClass;

public class DayControllerClass implements DayController {
	private SessionFactory sessionFactory;
	
	public DayControllerClass() {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure()//"resource/hibernate.cfg.xml")
				.build();
        try {
        	this.sessionFactory = 
        			new MetadataSources(registry)
//        			.addAnnotatedClass(DayClass.class)
//        			.addAnnotatedClass(WorkoutClass.class)
        			.buildMetadata().buildSessionFactory();
        } catch(Exception e) {        	
        	e.printStackTrace();
        	StandardServiceRegistryBuilder.destroy(registry);
        	System.exit(1);
        }
	}
	
	@SuppressWarnings("unchecked")
	private List<Day> readDays() {
		Session session = sessionFactory.openSession();
		List<Day> daies = null;
		try {
			daies = session.createQuery("FROM gym.models.DayClass").list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return daies;
	}
	
	private Day readDayById(String dayId) {
		Day day = null;
		Session session = sessionFactory.openSession();
		try {			
			day = session.find(DayClass.class, Integer.parseInt(dayId));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return day;
	}
	
	private Day readDayByData(String data) {
		Session session = sessionFactory.openSession();
		Day day = null;
		try {
			Query query = session.createQuery("FROM gym.models.DayClass WHERE data=:data");
			query.setParameter("data", data);
			day = (Day)query.getResultList().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return day;
	}
	
	private void writeDay(Day day) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(day);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public void close() {
		sessionFactory.close();
	}
	
	public List<Day> getDays() {
		return this.readDays();
	}

	

	public boolean hasWorkoutsLists() {
		return !this.getDays().isEmpty();
	}

	public boolean hasDayByData(String taskListData) {
		return this.getDayByData(taskListData) != null;
	}
	
	public boolean hasDayById(String dayId) {
		return this.getDayById(dayId) != null;
	}

	public Day createDay(String taskListData) {
		Day day = new DayClass(taskListData);
		this.writeDay(day);
		return day;
	}

	public Day getDayByData(String taskListname) {
		return this.readDayByData(taskListname);
	}
	
	public Day getDayById(String dayId) {
		return this.readDayById(dayId);
	}
	
	public Day changeDayDataByData(String taskListData, String newDayData) {
		Day day = this.readDayByData(taskListData);
		day.setData(newDayData);
		this.writeDay(day);
		return day;
	}
	
	public Day changeDayDataById(String taskLisId, String newDayData) {
		Day day = this.readDayById(taskLisId);
		day.setData(newDayData);
		this.writeDay(day);
		return day;
	}
	
	public void deleteWorkout(String dayId, String taskId) {
		Day day = this.getDayById(dayId);
		day.removeWorkout(taskId);
		this.writeDay(day);
	}
	
	public boolean hasWorkout(String dayId, String taskId) {
		Day day = this.getDayById(dayId);
		return day.hasWorkout(taskId);
	}

	public Workout changeWorkoutDescription(String dayId, String taskId, String taskDescription) {
		Day day = this.getDayById(dayId);
		Workout workout = day.getWorkout(taskId);
		workout.setDescription(taskDescription);
		this.writeDay(day);
		return workout;
	}

	public Workout changeWorkoutStatus(String dayId, String taskId) {
		Day day = this.getDayById(dayId);
		Workout workout = day.getWorkout(taskId);
		//workout.toggle();
		this.writeDay(day);
		return workout;
	}

	public Workout createWorkout(String dayId, String taskStatus, String taskDescription) {
		Day day = this.getDayById(dayId);
		Workout workout = day.addWorkout(taskDescription, taskStatus);
		//day.addWorkout(taskStatus);
		this.writeDay(day);
		return workout;
	}

	public Workout getWorkout(String dayId, String taskId) {
		Day day = this.getDayById(dayId);
		Workout workout = day.getWorkout(taskId);
		return workout;
	}

	public void deleteDay(String dayId) {
		Day day = getDayById(dayId);
		Session session = sessionFactory.openSession();
		if (day != null) {
			try {
				session.getTransaction().begin();
				session.delete(day);
				session.getTransaction().commit();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				session.close();
			}
		}
	}
	
	public JsonObjectBuilder getDaysJsonObjectBuilder() {
		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		
		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
		for(Day day : this.getDays()) {
			jsonArrayBuilder.add(day.getJsonObjectBuilder());
		}        
        jsonObjectBuilder.add("gym", jsonArrayBuilder);
        return jsonObjectBuilder;
	}


}
