name := "scala-openliberty-mandelbrot"

version := "0.1"

scalaVersion := "2.12.9"

libraryDependencies += "org.slf4j"               % "slf4j-api"     % "1.7.25"
libraryDependencies += "org.slf4j"               % "slf4j-log4j12" % "1.7.26"
libraryDependencies += "org.apache.commons"      % "commons-math3" % "3.6.1"
libraryDependencies += "net.liftweb"             %% "lift-json"    % "3.3.0"
libraryDependencies += "io.openliberty.features" % "cdi-2.0"       % "19.0.0.10"  % "provided"
libraryDependencies += "io.openliberty.features" % "jsonp-1.1"     % "19.0.0.10"  % "provided"
libraryDependencies += "io.openliberty.features" % "mpMetrics-2.0" % "19.0.0.10"  % "provided"
libraryDependencies += "io.openliberty.features" % "mpHealth-2.0"  % "19.0.0.10"  % "provided"

// https://github.com/sbt/sbt/issues/3618 describes fix to download javax.ws.rs-api correctly, as below
libraryDependencies += "jakarta.ws.rs"           % "jakarta.ws.rs-api" % "2.1.5"      % "provided"
libraryDependencies += "io.openliberty.features" % "jaxrs-2.1"         % "19.0.0.10"  % "provided" exclude("javax.ws.rs", "javax.ws.rs-api")
libraryDependencies += "io.openliberty.features" % "mpRestClient-1.3"  % "19.0.0.10"  % "provided" exclude("javax.ws.rs", "javax.ws.rs-api")
libraryDependencies += "io.openliberty.features" % "mpOpenAPI-1.1"     % "19.0.0.10"  % "provided" exclude("javax.ws.rs", "javax.ws.rs-api")

libraryDependencies += "org.scalatest"           %% "scalatest"        % "3.0.8"      % "test"

enablePlugins(DockerPlugin,JettyPlugin)

dockerfile in docker := {
  // The package task generates a WAR file
  val configSource: File = new File( "src/main/config/")
  val warFile: File = new File( "target/scala-2.12/scala-openliberty-mandelbrot_2.12-0.1.war")
  val configPath = "/config/"
  val appPath = "/config/apps/"
  val keysPath = "/opt/ol/wlp/output/defaultServer/resources/security/"
  val key1 = new File( "src/main/resources/key.p12")
  val key2 = new File( "src/main/resources/ltpa.keys")

  new Dockerfile {
    from("open-liberty")
    add(key1, keysPath)
    add(key2, keysPath)
    add(configSource, configPath)
    add(warFile, appPath)
    expose( 9080, 9443 )
  }
}

imageNames in docker := Seq(
  ImageName(
    namespace = Some("krzsam"),
    repository = "examples",
    tag = Some( s"${name.value}-${version.value}")
  )
)
