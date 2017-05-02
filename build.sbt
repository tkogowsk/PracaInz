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
  "org.apache.poi" % "poi" % "3.10-FINAL",
  "org.apache.poi" % "poi-scratchpad" % "3.10-FINAL",
  "org.apache.poi" % "poi-ooxml" % "3.10-FINAL",
// WebJars (i.e. client-side) dependencies
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.webjars" % "jquery" % "1.11.3",
  "org.webjars" % "bootstrap" % "3.3.6" exclude("org.webjars", "jquery"),
  "org.webjars.bower" % "angular" % "1.6.1",
  "org.webjars.bower" % "angular-route" % "1.6.1" exclude("org.webjars.bower", "angularjs"),
  "org.webjars.bower" % "angular-animate" % "1.6.1" exclude("org.webjars.bower", "angularjs"),
  "org.webjars.bower" % "angular-ui-router" % "0.3.2" exclude("org.webjars.bower", "angularjs"),
  "org.webjars.bower" % "angular-resource" % "1.6.1" exclude("org.webjars.bower", "angularjs"),
  "org.webjars" % "angular-ui-bootstrap" % "2.2.0" exclude("org.webjars", "angularjs")
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"  