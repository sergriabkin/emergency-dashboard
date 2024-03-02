# Emergency Services Dashboard

This Java application, built using Spring Boot, offers real-time search capabilities and dashboard functionality for logging and querying emergency incidents. It integrates Elasticsearch for efficient data indexing and search optimization, utilizing Hibernate for data persistence.

## Features and Implementation Status

### General Setup
- [x] **Spring Boot Setup**: The application is set up with Spring Boot, providing a solid foundation for both the API and the interaction with Elasticsearch and the H2 database.
- [x] **ElasticSearch Integration**: Integrated with Elasticsearch to enable real-time search capabilities for emergency incidents.
- [x] **Data Persistence**: Implemented Hibernate for data persistence, with data synchronized with Elasticsearch for optimized searching.
- [x] **Unit Testing**: Developed unit tests covering core functionalities, including incident logging and searching.

### Incidents
- [x] **Create and Log Incidents**:
   - Implemented a RESTful API endpoint for logging emergency incidents.
   - Incident attributes include `incidentType`, `location` (latitude and longitude), `timestamp`, and `severityLevel`.
- [x] **Search Incidents**:
   - Created an API endpoint for conducting searches based on `incidentType`, `location`, and `timestamp`.
   - Enabled combination searches (e.g., all 'fire' incidents in a specific 'location').

### ElasticSearch
- [x] **Data Indexing**: Utilized Elasticsearch for indexing emergency incidents, optimizing the indexed data for efficient querying.
- [x] **Search Optimization**: Leveraged Elasticsearch's capabilities to ensure fast and accurate search queries.

### Optional Tasks
- [ ] **Real-Time Dashboard**:
   - WebSocket Integration: *Pending*.
   - UI Implementation: *Pending*. A simple UI using Thymeleaf to visualize real-time updates is planned but not yet implemented.

### Dockerization
- [x] **Docker Setup**: The application and Elasticsearch have been dockerized, enhancing portability and ease of deployment. Instructions for using Docker Compose are provided to run the application alongside Elasticsearch with minimal setup.


# Setup and Running the Project

## Prerequisites

Before you begin, ensure you have met the following requirements:
* You have installed the latest version of Java (Java 17 recommended).
* You have a Windows/Linux/Mac machine.
* You have Docker installed if you want to run Elasticsearch in a Docker container.

## Setup and Installation

### Running the Application Locally

1. **Clone the repository**

    ```bash
    git clone https://github.com/sergriabkin/emergency-dashboard.git
    ```

2. **Build the project**

   Using Maven, you can build the project using the following command:

   ```bash
   mvn clean install
   ```

3. **Run the Application**

   Run the application using Spring Boot Maven plugin:

   ```bash
   mvn spring-boot:run
   ```

### Setting up Elasticsearch with Docker (Optional)

This application uses Elasticsearch, so if you want to run it through Docker, follow these steps:

1. **Pull the Elasticsearch Docker Image**

   ```bash
   docker pull docker.elastic.co/elasticsearch/elasticsearch:7.10.0
   ```

2. **Run Elasticsearch Container**

   ```bash
   docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.10.0
   ```

   Ensure Elasticsearch is running and accessible at `http://localhost:9200`.


### Installing Lombok Plugin for IntelliJ IDEA

Lombok is a Java library that automatically plugs into your editor and build tools, spicing up your Java code. It allows you to reduce boilerplate code for model/data objects, e.g., it can generate getters and setters for those objects with annotations.

To use Lombok in IntelliJ IDEA, you need to install the Lombok plugin and enable annotation processing:

1. **Install the Lombok Plugin**

   - Open IntelliJ IDEA and navigate to `File` > `Settings` (on Windows and Linux) or `IntelliJ IDEA` > `Preferences` (on macOS).
   - Go to `Plugins` and click on the `Marketplace` tab.
   - Search for `Lombok Plugin` and click `Install`.
   - Restart IntelliJ IDEA to activate the plugin.

2. **Enable Annotation Processing**

   - Navigate back to `File` > `Settings` (or `IntelliJ IDEA` > `Preferences` on macOS), then to `Build, Execution, Deployment` > `Compiler` > `Annotation Processors`.
   - Check the box next to `Enable annotation processing`.
   - Click `Apply` and `OK` to save the changes.

With the Lombok plugin installed and annotation processing enabled, IntelliJ IDEA will recognize Lombok annotations in your project.

### Using the Application with Docker Compose

To run the application with its dependencies (e.g., Elasticsearch) using Docker Compose, follow these steps:

1. **Ensure Docker is Installed**

   - Before proceeding, make sure Docker and Docker Compose are installed on your system. You can download Docker Desktop from [Docker Hub](https://hub.docker.com/?overlay=onboarding).

2. **Define Your Docker Compose Configuration**

   - Ensure your `docker-compose.yml` file is correctly set up in your project's root directory, defining services for your application, Elasticsearch, and any other dependencies.

3. **Build and Run Your Services**

   - Open a terminal or command prompt.
   - Navigate to your project's root directory where the `docker-compose.yml` file is located.
   - Run the following command to build and start your services:

     ```bash
     docker-compose up --build
     ```

   This command builds the Docker image for your application (if necessary) and starts all the services defined in `docker-compose.yml`. The `--build` flag ensures that Docker Compose rebuilds the image if there have been changes since the last build.

4. **Accessing the Application**

   - With the services running, your application should be accessible at the configured port (e.g., `http://localhost:8080` for the application and `http://localhost:9200` for Elasticsearch).
   - Use your application as described in the "Using the Application" section of the README.

5. **Stopping the Services**

   - To stop and remove the containers, networks, and volumes created by `docker-compose up`, you can use the following command from the same directory:

     ```bash
     docker-compose down
     ```

## Using the Application

- To create an incident, send a POST request to `http://localhost:8080/incidents` with the following JSON body:

  ```json
  {
    "incidentType": "fire",
    "latitude": 113.0,
    "longitude": 114.0,
    "timestamp": "2024-03-01T11:52:16.441220",
    "severityLevel": "medium"
  }
  ```
- Access the H2 Database Console at `http://localhost:8080/h2-console`. Use the JDBC URL `jdbc:h2:mem:testdb`, with username `sa` and no password.
