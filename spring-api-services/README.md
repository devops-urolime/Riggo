# Resouce Server based on model presented on the repo and auth0

##Changes
Database integration (PGSQL) for load data (load is core of the business). Note that said it is under review and exact model might change. The idea of this version is to model get by extSysId and external extSysId. Please look at the postman folder for the test scripts.( auth tab bearer token). It is far from perfect but has the essential pieces like domain/models , services, basic validation, data wrappers or repoitories and exception and configuration and associated dependency changes.
Ismaiel's changes to Auth0 api  controller  been moved to RefAPIController so that does not get lost in development as it could well be useful to new entrants to the team if need be. 

TBD:
Caching Redis. The configuration is already in by again needs to downgraded to a lower version for now.

#Build tools
Choose Maven or Gradle

##Installation of Gradle
Always use gradle wrapper.

#Tools 
Pick your poison IntelliJ ide is really good (read expensive!). Anything else that works for you will do.

#Note
Do not forget to insert data for testing. The database starter is blank database. You need to have PGSQL setup 11.x recommended.  And yes logging is enables to please do look at it for info. For example, the database is not hit again and again for data on subsequent API  queries.



#Docker 

This project is docker-ized based off Alpine is has no java runtime it pull the JVM over AZUL

It installs bash for scripting and testing . 

Inspect the image with the -exec on docker.

It takes lot of time to build. One time effort. 

 
