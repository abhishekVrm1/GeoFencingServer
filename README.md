# Geofencing Server

This is a Spring Boot application that provides geofencing capabilities based on the origin country of incoming requests.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java 8 or higher installed on your system.
- Maven installed on your system.
- MySQL database server running with an empty database named `geofencing_db`.

## Getting Started

Follow these steps to get the project up and running:

1. Clone the repository to your local machine:

	git clone <repository-url>

## Configuration

### Database Configuration

The application uses a MySQL database. You can configure the database properties in the `application.properties` file located in the `src/main/resources` directory. Here are the relevant properties:

- `spring.datasource.url`: The JDBC URL of your MySQL database.
- `spring.datasource.username`: The username for connecting to the database.
- `spring.datasource.password`: The password for connecting to the database.

Make sure to update these properties with your database details.

### IPINFO Configuration

The application uses the IPINFO service to retrieve information about the origin country of incoming requests. You can configure the IPINFO properties in the `application.properties` file as well:

- `ipinfo.token`: Your IPINFO API token.
- `ipinfo.url`: The URL of the IPINFO service.

Update these properties with your IPINFO API token and service URL.

## Running the Application

To run the application, follow these steps:

1. Build the project using Maven:

   mvn clean install
   
2. Run the project with below command:

   mvn spring-boot:run
   
The application will start and be accessible at http://localhost:8080/geofencing.


## Testing

You can test the application by using the provided endpoints:

- Use a tool like Postman or your web browser to access the application's endpoints as described in your project requirements.
