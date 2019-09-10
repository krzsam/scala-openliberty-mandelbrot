
#### */config* directory

The contents of the directory are result of reverse-engineering of contents of the resulting Docker image as created by an example project
[GitHub: guide-getting-started](https://github.com/OpenLiberty/guide-getting-started)

* /config/apps
* /config/dropins
* /config/workarea
* /config/bootstrap.properties
* /src/main/resources/key.p12 - taken when generated in running Docker container
* /src/main/resources/ltpa.keys - taken when generated in running Docker container

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

Testing services
```
$ curl 192.168.99.100:9080/mandelbrot/system/info
Mandelbrot Example Application using Open Liberty Microservices

$ curl 192.168.99.100:9080/mandelbrot/iteration/info
This is Iteration Service

$ curl 192.168.99.100:9080/mandelbrot/image/info
This is Image Generation Service

$ curl "192.168.99.100:9080/mandelbrot/iteration/iterate?posx=1&posy=2&datax=0.5&datay=0.5&iterations=256"
{"posX":1,"posY":2,"c":{"real":9.486587524414062,"imaginary":9.328857421875},"iterations":250}

$ curl "192.168.99.100:9080/mandelbrot/iteration/iterate?posx=1&posy=2&datax=0.1&datay=0.1&iterations=256"
{"posX":1,"posY":2,"c":{"real":0.09362728698078197,"imaginary":0.12303975734127459},"iterations":0}

curl 192.168.99.100:9080/mandelbrot/iteration/iterate?iterations=100
```

```
C:\Users\Krzysiek>docker cp 196af7c5d7f9:/opt/ol/wlp/output/defaultServer/resources/security/key.p12 .
C:\Users\Krzysiek>docker cp 196af7c5d7f9:/opt/ol/wlp/output/defaultServer/resources/security/ltpa.keys .
```

#### Push image to Docker repository

```
$ docker login
Login with your Docker ID to push and pull images from Docker Hub. If you don't have a Docker ID, head over to https://hub.docker.com to create one.
Username (krzsam):
Password:
Login Succeeded

$ docker push krzsam/examples:scala-openliberty-mandelbrot-0.1
The push refers to repository [docker.io/krzsam/examples]
2ae092193fc8: Pushed                                                                                             
ec1187582bee: Layer already exists                                                                               
ff0f5a9985e3: Layer already exists                                                                               
faf91c7e588b: Layer already exists                                                                               
503dcaf8ca5d: Layer already exists                                                                               
9d77f4ac3e27: Layer already exists                                                                               
32ee5abc03f2: Layer already exists                                                                               
afd7e213fcb2: Layer already exists                                                                               
b220d99add99: Layer already exists                                                                               
4ce08090970e: Layer already exists                                                                               
e79142719515: Layer already exists                                                                               
aeda103e78c9: Layer already exists                                                                               
2558e637fbff: Layer already exists                                                                               
f749b9b0fb21: Layer already exists                                                                               
scala-openliberty-mandelbrot-0.1: digest: sha256:24b9ee9188c7bf176a5ff0a1e2e16e9955b7129c1be6a352b843cc8d238abb29 size: 3246
```