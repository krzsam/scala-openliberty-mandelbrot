name := "scala-openliberty-mandelbrot"

version := "0.1"

scalaVersion := "2.12.9"

libraryDependencies += "org.slf4j"               % "slf4j-api"     % "1.7.25"
libraryDependencies += "org.slf4j"               % "slf4j-log4j12" % "1.7.26"
libraryDependencies += "org.apache.commons"      % "commons-math3" % "3.6.1"
libraryDependencies += "io.openliberty.features" % "cdi-2.0"       % "19.0.0.8"  % "provided"
libraryDependencies += "io.openliberty.features" % "jsonp-1.1"     % "19.0.0.8"  % "provided"
libraryDependencies += "io.openliberty.features" % "mpMetrics-2.0" % "19.0.0.8"  % "provided"
libraryDependencies += "io.openliberty.features" % "mpHealth-2.0"  % "19.0.0.8"  % "provided"
// https://github.com/sbt/sbt/issues/3618 describes fix to download javax.ws.rs-api correctly, as below
//libraryDependencies += "javax.ws.rs"             % "javax.ws.rs-api" % "2.1.1"   % "provided" artifacts( Artifact("javax.ws.rs-api", "jar", "jar"))
libraryDependencies += "jakarta.ws.rs"             % "jakarta.ws.rs-api" % "2.1.5"   % "provided"
libraryDependencies += "io.openliberty.features" % "jaxrs-2.1"     % "19.0.0.8"  % "provided" exclude("javax.ws.rs", "javax.ws.rs-api")

// for sbt-assembly
resolvers += Resolver.url("https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/").
  withPatterns( Patterns( "https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/com.eed3si9n/[module]/scala_2.12/sbt_1.0/[revision]/ivys/ivy.xml" ) )

enablePlugins(DockerPlugin,JettyPlugin)

dockerfile in docker := {
  // The assembly task generates a fat JAR file
  val configSource: File = new File( "src/main/config/")
  val warFile: File = new File( "target/scala-2.12/scala-openliberty-mandelbrot_2.12-0.1.war")
  val configPath = "/config/"
  val appPath = "/config/apps/"

  new Dockerfile {
    from("open-liberty")
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
