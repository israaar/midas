# Midas: Middleware Library for Mobile Ad-Hoc Networking using Heterogenous Media

This project is a middleware library for mobile ad-hoc networking that supports using heterogeneous media for communication.

## Building a jar
Run the `build_jar.sh` script to build on Linux/Mac

## Using jar in project
1. Add the midas.jar that was created under the "Building a jar" section.
2. Access any class files and implementations by using the `import midas.*`

## Docker Message Broadcaster example:
1. Build the image
```
./build_docker.sh
```
2. In one terminal run docker compose:
```
docker-compose up
```
This will show all messages that are broadcasted to these containers
3. In another terminal window run the following:
```
docker run -a stdin -a stdout -it --name server_4000 -p 4000:4000 --network host --rm -e PORTS="4000 4001 4002" midas_broadcast:latest
```
This window will allow you to send messages to the other instances.
All text typed in through here will appear in the output in the first terminal window.