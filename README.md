# photo-app-users
This is a User Management Java Micro Service. Using this Micro Service you can perform CRUD on a users. In addition, it is configured to use Spring Security, thereby providing user login capability.

## Build
```
# Build and run tests
$ mvn clean package

# Build
$ mvn -DskipTests clean package
```

## Create Image
```
# Build and create local image. Typically used for local testing.
$ mvn -DskipTests clean package docker:build

# Build and Copy to docker hub
$ mvn -DskipTests clean package docker:build docker: publish
```

### Remove Images
```
# Remove this image
$ docker image rm users

# Remove all images
$ docker rmi $docker images -q)
```

## Run in Docker
```
# Run from maven
$ mvn docker:start

# Run from docker
$ cd ~/target/dockerfile
$ docker run -d users
```

## Access  Docker
```
# ssh
$ docker exec -it users bash

# logs
$ docker logs users
$ docker logs -f users
```

## Required to test:
It is best to run docker-compose in order to start the following list
1. Zuul - discovery service
1. Config Service
1. API Gateway
1. mySql
1. Rabbit MQ

## Test
Steps to test

1. Run docker-compose up 
1. Create User with Postman
    1. Action: POST
    1. URL: http://localhost:8011/users-ws/users
    1. Body:
    ```
    {
        "firstName": "mike",
        "lastName": "test",
        "email": "mike123@test.com",
        "password": "e410f515"
    }
    ```
3. Login in with Postman
    1. Action: POST
    1. URL: http://localhost:8011/users-ws/users/login
    1. Body:
    ```
    {
        "email": "mike123@test.com",
        "password": "e410f515"
    }
    ```
1. Perform Get with Postman
    1. Action: GET
    1. URL Ex: http://localhost:8011/users-ws/users/_requested_userId_
    1. URL Ex: http://localhost:8011/users-ws/users/53ad88ad-68f0-4e56-a331-15dff16bd395
    1. From the Login you will need to the auth token
    1. Headers:
        ```
        {
            "Authorization": Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1M2FkODhhZC02OGYwLTRlNTYtYTMzMS0xNWRmZjE2YmQzOTUiLCJleHAiOjE1NjU5OTE3MDN9.GhaYOh5spoSi97vWJFxRjMgaCFhks-1XQmtbhwBW8moJBB16TjzcDwIxfOMA5JtqH1E7woS6biiOPaVKUyoUfA
        }
        ```
