name := "Inz"

version := "1.0"

lazy val `inz` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "com.chuusai" %% "shapeless" % "2.3.1",
  "io.underscore" %% "slickless" % "0.3.0",
  "ai.x" %% "play-json-extensions" % "0.8.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.h2database" % "h2" % "1.4.187",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc4",
  // WebJars (i.e. client-side) dependencies
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.webjars" % "jquery" % "1.11.3",
  "org.webjars" % "bootstrap" % "3.3.6" exclude("org.webjars", "jquery"),
  "org.webjars" % "angularjs" % "1.5.5" exclude("org.webjars", "jquery"),
  "org.webjars.bower" % "angular-route" % "1.6.1" exclude("org.webjars.bower", "angularjs"),
  "org.webjars.bower" % "angular-ui-router" % "0.3.2" exclude("org.webjars.bower", "angularjs"),
  "org.webjars.bower" % "angular-resource" % "1.5.5" exclude("org.webjars.bower", "angularjs"),
  "org.webjars" % "angular-ui-bootstrap" % "1.3.2" exclude("org.webjars", "jquery")
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"  