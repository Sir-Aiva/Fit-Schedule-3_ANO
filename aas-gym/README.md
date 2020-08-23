# REST Tutorial: Task List Manager
This project supports a tutorial on REST APIs using JBOSS/WildFly.

The project implements a task list manager business logic, exposing a REST API.

The business objects, i.e., Tasks and TaskList, persist in a relational database through the java persistence api, using Hibernate.

## REST API
The actions supported by the REST API are the follwing:

	/lists/
	    GET - List task lists.
	    POST - Create a new task list.
	/lists/list_id/
	    GET - Get task list, including tasks.
	    PUT - Change task list name.
	    POST - Create task in task list.
	    DELETE - Delete task list, including tasks.
	/lists/list_id/task_id/
	    GET - Show a task.
	    POST - Toggle task status.
	    PUT - Change task description.
	    DELETE - Remove task.

### Sample session

	curl -H "Accept: application/json" localhost:8080/tasklists/lists/
	curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d '{"name": "test list 1"}' localhost:8080/tasklists/lists/
	curl -H "Accept: application/json" localhost:8080/tasklists/lists/1
	curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" -d '{"description": "task sample 1"}' localhost:8080/tasklists/lists/1
	curl -H "Accept: application/json" localhost:8080/tasklists/lists/1/1
	curl -X PUT -H "Content-Type: application/json" -H "Accept: application/json" -d '{"name": "new name"}' localhost:8080/tasklists/lists/1
	curl -X PUT -H "Content-Type: application/json" -H "Accept: application/json" -d '{"description": "new description"}' localhost:8080/tasklists/lists/1/1
	curl -X PUT -H "Content-Type: application/json" -H "Accept: application/json" -d '{"description": "new description"}' localhost:8080/tasklists/lists/1/1
	curl -X DELETE -H "Accept: application/json" localhost:8080/tasklists/lists/1/1
	curl -X DELETE -H "Accept: application/json" localhost:8080/tasklists/lists/1
	curl -H "Accept: application/json" localhost:8080/tasklists/lists/
	

## Stack

- Java version "11.0.4" 2019-07-16 LTS
- WildFly 18.0
- Maven 3.6.X
- Hibernate 5.4.X
- H2 Database


## Build and deployment
To build the application

	mvn package

The Maven action will produce a `tasklists.war` file in the `target` directory.

### Deployment using the WildFly Maven plugin
With a running WildFly instance:

	mvn wildfly:deploy

### Deployment using the WildFly scanner
Copy the `war` file resulting from the build process into the `deployments` directory in the proper execution mode directory, e.g., `standalone`. 

E.g., in a default Linux instalation, running with the `standalone` mode

	cp tasklists.war /opt/jboss/wildfly/standalone/deployments
	
## License
This project is distributed following the terms of the 3-Clause BSD License. See the LICENSE file for details.

