## Preparing Docker image

To prepare Docker image, Docker runtime needs to be installed and available on the development machine.
For the purpose of this application, [Docker Toolbox](https://docs.docker.com/toolbox/overview/) was used.

To create an image,  the below commands need to be invoked in Sbt (via the Sbt Console for example). The structure of the Docker image being
created is defined in *build.sbt* file and uses *sbt-docker* plugin for this purpose.
```
sbt> package
...

sbt> docker
...
```

The above will create an image in the local Docker repository:

```
$ docker image ls
REPOSITORY                                     TAG                                 IMAGE ID            CREATED             SIZE
krzsam/examples                                scala-openliberty-mandelbrot-0.1    cb4118270d42        17 seconds ago      478MB
...
```

The below commands can be used to test the image and verify it was created correctly and the Open Liberty server is starting up
and deploying the application.

```
$ docker run --name mandelbrot-app -d -p 9080:9080 -p 9443:9443 krzsam/examples:scala-openliberty-mandelbrot-0.1

$ docker container logs -f mandelbrot-app
Launching defaultServer (Open Liberty 19.0.0.10/wlp-1.0.33.cl191020191002-0300) on IBM J9 VM, version 8.0.5.41 - pxa6480sr5fp41-20190919_01(SR5 FP41) (en_US)
...
[AUDIT   ] CWWKZ0001I: Application scala-openliberty-mandelbrot_2.12-0.1 started in 96.400 seconds.
```

The application deployment can be verified using the below commands

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

$ docker container stop mandelbrot-app && docker container rm mandelbrot-app
```

#### Push image to Docker repository

Once the image is verified, it can be pushed to the main DockerHub repository.

```
$ docker login
Login with your Docker ID to push and pull images from Docker Hub. If you don't have a Docker ID, head over to https://hub.docker.com to create one.
Username (krzsam):
Password:
Login Succeeded

$ docker push krzsam/examples:scala-openliberty-mandelbrot-0.1
The push refers to repository [docker.io/krzsam/examples]
e2c341adac91: Pushed                                                                                                                                                              
f2b30661a5dc: Pushed                                                                                                                                                              
3848d16e19e4: Pushed                                                                                                                                                              
92aa46819f88: Pushed                                                                                                                                                              
4152b4bb288c: Mounted from library/open-liberty                                                                                                                                   
2e71b918bc44: Mounted from library/open-liberty                                                                                                                                   
b92f66a7b3b6: Mounted from library/open-liberty                                                                                                                                   
d1ca4bf1e9a7: Mounted from library/open-liberty                                                                                                                                   
592742b2d637: Mounted from library/open-liberty                                                                                                                                   
97ef137a0aff: Mounted from library/open-liberty                                                                                                                                   
9acfe225486b: Mounted from library/open-liberty                                                                                                                                   
90109bbe5b76: Mounted from library/open-liberty                                                                                                                                   
cb81b9d8a6c9: Mounted from library/open-liberty                                                                                                                                   
ea69392465ad: Mounted from library/open-liberty                                                                                                                                   
scala-openliberty-mandelbrot-0.1: digest: sha256:c75c3c0f87e929075be7a2b85e749a282f062d04a2fc5a1907ba28e311117ea0 size: 3246
```
