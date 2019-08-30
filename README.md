# scala-openliberty-mandelbrot
Example Scala application to implement MicroProfile microservices

## Application Structure

#### */config* directory

The contents of the directory are result of reverse-engineering of contents of the resulting Docker image as created by an example project
[GitHub: guide-getting-started](https://github.com/OpenLiberty/guide-getting-started)

* /config/apps
* /config/dropins
* /config/workarea
* /config/bootstrap.properties

## Preparing Docker image

start your local Docker machine

run 
```
sbt> package
sbt> docker
```


```
docker exec -it 713912a6e13d /bin/bash

docker run --name mandelbrot-app -d -p 9080:9080 -p 9443:9443 krzsam/examples:scala-openliberty-mandelbrot-0.1
docker container logs -f mandelbrot-app 
docker container stop mandelbrot-app && docker container rm mandelbrot-app
```

logs: target/liberty/wlp/usr/servers/GettingStartedServer/logs

## Running Docker image on Kubernetes
 

## Links
* Akka
* Microservice frameworks
    * [OpenLiberty](https://openliberty.io/)
        * [Microprofile Specification](https://microprofile.io/)
* [Docker Multistage Build](https://docs.docker.com/develop/develop-images/multistage-build/)
* [sbt-assembly](https://github.com/sbt/sbt-assembly) -- not needed
* [sbt-docker](https://github.com/marcuslonnberg/sbt-docker)]
* [xsbt-web-plugin](https://github.com/earldouglas/xsbt-web-plugin)
* [GitHub: guide-getting-started](https://github.com/OpenLiberty/guide-getting-started)