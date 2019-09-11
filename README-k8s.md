## Deploying Docker image on Kubernetes

### Deploying Calculation Service

#### Creating Deployment

To create the deployment and verify the application was deployed:
```
$ kubectl apply -f ./calculation-deployment.yaml
deployment.apps/calculation-depl created

$ kubectl get deployments -o wide
NAME               READY   UP-TO-DATE   AVAILABLE   AGE     CONTAINERS         IMAGES                                             SELECTOR
calculation-depl   3/3     3            3           2m34s   calculation-depl   krzsam/examples:scala-openliberty-mandelbrot-0.1   app=calculation-depl

$ kubectl get pods -o wide
NAME                               READY   STATUS    RESTARTS   AGE     IP          NODE               NOMINATED NODE   READINESS GATES
calculation-depl-599c7f898-5cqh7   1/1     Running   0          2m57s   10.36.0.1   ip-172-31-27-88    <none>           <none>
calculation-depl-599c7f898-7v8cg   1/1     Running   0          2m57s   10.44.0.1   ip-172-31-23-111   <none>           <none>
calculation-depl-599c7f898-9x8j5   1/1     Running   0          2m57s   10.44.0.2   ip-172-31-23-111   <none>           <none>

$ curl 10.36.0.1:9080/mandelbrot/iteration/info
This is Iteration Service

$ curl "10.44.0.2:9080/mandelbrot/iteration/iterate?posx=1&posy=2&datax=0.1&datay=0.1&iterations=256"
{"posX":1,"posY":2,"c":{"real":0.09362728698078197,"imaginary":0.12303975734127459},"iterations":0}
```

#### Creating Service

To expose the deployment as a service and verify it:
```
$ kubectl apply -f ./calculation-service.yaml
service/calculation-svc created

$ kubectl get services -o wide
NAME              TYPE           CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE   SELECTOR
calculation-svc   LoadBalancer   10.106.77.222   <pending>     9080:30196/TCP   72s   app=calculation-depl
kubernetes        ClusterIP      10.96.0.1       <none>        443/TCP          67d   <none>

$ curl 10.106.77.222:9080/mandelbrot/iteration/info
This is Iteration Serviceadmin

$ curl "10.106.77.222:9080/mandelbrot/iteration/iterate?posx=1&posy=2&datax=0.1&datay=0.1&iterations=256"
{"posX":1,"posY":2,"c":{"real":0.09362728698078197,"imaginary":0.12303975734127459},"iterations":0}
```

### Deploying Image Generation Service

#### Creating Deployment

To create the deployment and verify the application was deployed:
```
$ kubectl apply -f ./image-deployment.yaml
deployment.apps/image-depl created

$ kubectl get deployments -o wide
NAME               READY   UP-TO-DATE   AVAILABLE   AGE   CONTAINERS         IMAGES                                             SELECTOR
calculation-depl   3/3     3            3           30m   calculation-depl   krzsam/examples:scala-openliberty-mandelbrot-0.1   app=calculation-depl
image-depl         1/1     1            1           17s   image-depl         krzsam/examples:scala-openliberty-mandelbrot-0.1   app=image-depl

$ kubectl get pods -o wide
NAME                               READY   STATUS    RESTARTS   AGE   IP          NODE               NOMINATED NODE   READINESS GATES
calculation-depl-599c7f898-5cqh7   1/1     Running   0          31m   10.36.0.1   ip-172-31-27-88    <none>           <none>
calculation-depl-599c7f898-7v8cg   1/1     Running   0          31m   10.44.0.1   ip-172-31-23-111   <none>           <none>
calculation-depl-599c7f898-9x8j5   1/1     Running   0          31m   10.44.0.2   ip-172-31-23-111   <none>           <none>
image-depl-849874f46-5sssg         1/1     Running   0          38s   10.36.0.2   ip-172-31-27-88    <none>           <none>

$ curl 10.36.0.2:9080/mandelbrot/image/info
This is Image Generation Service
```

#### Creating Service

To expose the deployment as a service and verify it:
```
$ kubectl apply -f ./image-service.yaml
service/image-svc created

$ kubectl get services -o wide
NAME              TYPE           CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE   SELECTOR
calculation-svc   LoadBalancer   10.106.77.222   <pending>     9080:30196/TCP   27m   app=calculation-depl
image-svc         LoadBalancer   10.107.99.121   <pending>     9080:30004/TCP   15s   app=image-depl
kubernetes        ClusterIP      10.96.0.1       <none>        443/TCP          67d   <none>

$ curl 10.107.99.121:9080/mandelbrot/image/info
This is Image Generation Service

$ kubectl logs -f deployment/calculation-depl --all-containers
...
... 

$ kubectl logs -f deployment/image-depl --all-containers
...
...

$ curl "10.107.99.121:9080/mandelbrot/image/generate?tlx=-2.2&tly=1.2&brx=1.0&bry=-1.2&sx=640&sy=480&iterations=512" -o image-640-480.png
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100 34875    0 34875    0     0     94      0 --:--:--  0:06:09 --:--:-- 10227
```

#### Removing services and deployments

If necessary, all created deployments and services can be removed using the below command:
```
kubectl delete service/calculation-svc && kubectl delete deployment/calculation-depl && kubectl delete service/image-svc && kubectl delete deployment/image-depl
```
