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
- [x] **Real-Time Dashboard**:
   - WebSocket Integration: *Done*.
   - UI Implementation: *Done*. A simple UI using Thymeleaf to visualize real-time updates is implemented.

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

**To run Elasticsearch locally (without Docker):**
1. Download the archived version from official website - https://www.elastic.co/downloads/past-releases/elasticsearch-7-17-17
2. Unpack it and run from terminal:
    ```bash
    cd {path_to_unarchived_folder}/bin/elasticsearch
   ```

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

- Access the H2 Database Console at `http://localhost:8080/h2-console`. Use the JDBC URL `jdbc:h2:mem:testdb`, with username `sa` and no password.

### Saving Incidents via UI

The application provides a user-friendly interface built with Thymeleaf for reporting emergency incidents. To report an incident, follow these steps:

1. **Open the Application**: Navigate to `http://localhost:8080` in your web browser. You will see a form for reporting new incidents.

2. **Fill Out the Incident Form**: Enter the details of the incident in the form. The fields include:
    - **Incident Type**: The type of incident (e.g., fire, medical, etc.).
    - **Latitude**: The latitude of the incident location (must be between -90.0 and 90.0).
    - **Longitude**: The longitude of the incident location (must be between -180.0 and 180.0).
    - **Severity Level**: The severity level of the incident (e.g., low, medium, high).

3. **Submit the Form**: After filling out the form with the incident details, click the "Report Incident" button to submit the form. The incident data will be saved to the system and broadcasted to all open clients via WebSocket.

### Checking Broadcasting Incidents

The application utilizes WebSockets to broadcast reported incidents to all clients currently connected to the application. To check the broadcasting functionality:

1. **Open Multiple Clients**: Open the application in multiple browser tabs or windows (`http://localhost:8080`).

2. **Report an Incident**: In one of the browser tabs/windows, fill out the incident report form and submit it by clicking the "Report Incident" button.

3. **Verify Broadcasting**: Upon submitting the form, all other open tabs/windows of the application should automatically display the reported incident in real-time. This confirms that the incident has been successfully broadcasted to all clients.

### Note on WebSockets

The WebSocket functionality ensures that all users of the application receive real-time updates of incidents without needing to refresh their browsers. This feature is particularly useful for emergency services dashboards, where timely information dissemination is crucial.

### Troubleshooting

- **WebSocket Connection Issues**: If you are experiencing issues with real-time updates not appearing in other clients, ensure that your browser supports WebSockets and that there are no network issues or browser extensions blocking WebSocket connections.
- **Form Submission Errors**: If the form submission fails, check that all input fields are correctly filled and that the latitude and longitude values are within their valid ranges. The application will validate the input and display error messages for any invalid fields.

### Using REST api
- To create an incident using application REST api, send a POST request to `http://localhost:8080/incidents` with the following JSON body:

  ```json
  {
    "incidentType": "fire",
    "latitude": -10.321,
    "longitude": 10.456,
    "timestamp": "2024-03-01T11:52:16.441220",
    "severityLevel": "medium"
  }
  ```
  
- To view all incidents, send a GET request to `http://localhost:8080/incidents`. It will show all incidents from DB. Here is some typical response:

  ```json
   [
  {
        "id": "ff8080818e03ef81018e03f2b30b0001",
        "incidentType": "fire",
        "latitude": 3.5,
        "longitude": 4.6,
        "timestamp": "2024-03-01T11:52:16.44122",
        "severityLevel": "medium"
    },
    {
        "id": "ff8080818e03ef81018e03f31aec0002",
        "incidentType": "medical",
        "latitude": 13.5,
        "longitude": 14.6,
        "timestamp": "2024-03-01T11:52:16.44122",
        "severityLevel": "medium"
    }
  ]
  ```
- To search incidents by incidentType, send a GET request to `http://localhost:8080/incidents/search/fire`. It will perform the search based on your query in "incidents" index of Elasticsearch. Here is some typical response:

  ```json
   [
  {
        "id": "ff8080818e03ef81018e03f2b30b0001",
        "incidentType": "fire",
        "latitude": -23.5,
        "longitude": 34.6,
        "timestamp": "2024-03-01T11:52:16.44122",
        "severityLevel": "medium"
    },
    {
        "id": "ff8080818e03ef81018e03f31aec0002",
        "incidentType": "fire",
        "latitude": 13.5,
        "longitude": 14.6,
        "timestamp": "2024-03-01T11:52:16.44122",
        "severityLevel": "medium"
    }
  ]
  ```
Updating the README.md with detailed information about the advanced search functionalities, including location/timestamp precision, boosting, sorting, and filtering mechanisms:

---

## Advanced Incident Search Capabilities

The application supports advanced search queries against the "incidents" index in Elasticsearch. This enables users to perform detailed searches based on several parameters such as `incidentType`, geographical position (`latitude` and `longitude`), and `timestamp`. The search functionality incorporates features like precision handling, type boosting, and sorting to enhance the search results according to relevance and specificity.

### Search Query Parameters

- **`incidentType`**: Searches for incidents matching the specified type. The search is boosted for this field, giving higher relevance to matches on incident type.
- **Geographical Position (`latitude` and `longitude`)**: Performs a geospatial search to find incidents within a 10km radius from the specified point. This allows for pinpointing incidents based on location proximity.
- **`timestamp`**: Filters incidents that occurred within ±1 hour of the specified timestamp, providing a time-based precision in search results.

### Performing a Search

To search for incidents using these parameters, send a GET request to the endpoint with your query parameters. For example:

```
http://localhost:8080/incidents/search?latitude=10.31&longitude=10.41&incidentType=fire
```

This request searches the "incidents" index for fire incidents near the specified geographical location, considering the specified time window.

### Typical Response

The response will include a list of incidents that match the search criteria. Each incident in the response contains detailed information including `id`, `incidentType`, `latitude`, `longitude`, `timestamp`, and `severityLevel`. For example:

```json
[
  {
      "id": "ff8080818e0469a9018e0471616d0001",
      "incidentType": "fire",
      "latitude": 10.3,
      "longitude": 10.4,
      "timestamp": "2024-03-01T11:52:16.441",
      "severityLevel": "medium"
  },
  {
      "id": "ff8080818e0469a9018e0471729f0002",
      "incidentType": "fire",
      "latitude": 10.31,
      "longitude": 10.41,
      "timestamp": "2024-03-01T11:52:16.441",
      "severityLevel": "medium"
  },
  {
      "id": "ff8080818e0469a9018e047184a30003",
      "incidentType": "fire",
      "latitude": 10.311,
      "longitude": 10.411,
      "timestamp": "2024-03-01T11:52:16.441",
      "severityLevel": "medium"
  }
]
```

### Sorting and Relevance

Results are sorted by relevance (`_score`) in descending order, ensuring that the most pertinent incidents are listed first. The scoring mechanism takes into account the boosting of the `incidentType` field and the proximity for geospatial queries, among other factors.

### Note on Precision

- **Location**: The search considers incidents within a 10km radius of the provided geo-coordinates, enabling precise location-based filtering.
- **Timestamp**: The ±1 hour window around the provided timestamp helps in narrowing down incidents to a specific timeframe, increasing the temporal precision of the search.

This advanced search functionality empowers users to conduct comprehensive and refined searches on the incident data, leveraging Elasticsearch's powerful query and analytical capabilities.

## Directly Querying Elasticsearch

### Accessing Elasticsearch

To explore and query the incident data stored by the application, you can directly access Elasticsearch. This can be done using tools like Kibana, Postman, or even `curl` from the command line. Ensure you have the URL for the Elasticsearch instance, typically `http://localhost:9200` if running locally.

### Basic Queries

#### Viewing All Incidents

To retrieve all incidents logged in the system, you can execute a simple search query against the `incidents` index:

```bash
curl -X GET "localhost:9200/incidents/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
    "match_all": {}
  }
}
'
```

This query returns all documents within the `incidents` index, formatted for readability (`?pretty`).

#### Searching by Incident Type

To search for incidents of a specific type (e.g., `fire`), use a `match` query:

```bash
curl -X GET "localhost:9200/incidents/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
    "match": {
      "incidentType": "fire"
    }
  }
}
'
```

### Advanced Queries

#### Geospatial Searches

If you want to find incidents within a certain distance from a point, you can use a `geo_distance` query. For example, to find incidents within 10km of a specific location:

```bash
curl -X GET "localhost:9200/incidents/_search?pretty" -H 'Content-Type: application/json' -d'
{
  "query": {
    "bool": {
      "must": {
        "match_all": {}
      },
      "filter": {
        "geo_distance": {
          "distance": "10km",
          "location": {
            "lat": 40.7128,
            "lon": -74.0060
          }
        }
      }
    }
  }
}
'
```

### Tips

- **Index Name**: Ensure you're querying the correct index name (`incidents` in the examples).
- **Query Adjustments**: Adjust the query parameters based on your specific search criteria.