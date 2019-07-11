# Database Migrations - FlywayDB

## Installation Instructions
Step 1: Get it .
Visit https://flywaydb.org/download/ and download
Ensure you get Flyway Community Edition 5.2.4 by Boxfuse


## Configuration
Configuration for your local can be achieved either by configuration file or environment variables.


### Configuration file
Open folder conf of your flyway installation. 
Enter values into configuration file.

```
flyway.url=JDBC your database url here 
flyway.user=your DB user
flyway.password=your password
```

### Environment Variables
FLYWAY_URL
FLYWAY_USER
FLYWAY_PASSWORD


## Execution 
```
flyway -X migrate  
```

