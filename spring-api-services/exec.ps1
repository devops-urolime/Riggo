docker build -t auth0-spring-security-mvc-api .
docker run -p 8088:8088 -it \
       -v "$(pwd)":/usr/src/app -v ~/.m2:/root/.m2 \
       auth0-spring-security-mvc-api
