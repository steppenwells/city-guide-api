organization := "com.gu"

name := "cityguideApi"

version := "1.0"

scalaVersion := "2.8.1"

// include web plugin settings in this project
seq(webSettings :_*)

resolvers ++= Seq(
  "Sonatype OSS" at "http://oss.sonatype.org/content/repositories/releases/",
  "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
  "Guardian Github Releases" at "http://guardian.github.com/maven/repo-releases"
)

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.6.1",
  "org.slf4j" % "slf4j-simple" % "1.6.1",
  "javax.servlet" % "servlet-api" % "2.5" % "provided->default",
  "commons-codec" % "commons-codec" % "1.5",
  "org.scalatra" %% "scalatra" % "2.0.0",
  "com.gu.openplatform" %% "content-api-client" % "1.11",
  "net.liftweb" %% "lift-json" % "2.3"
)

// and use this version of jetty for jetty run
libraryDependencies += "org.eclipse.jetty" % "jetty-webapp" % "7.3.1.v20110307" % "container"


port in container.Configuration := 9080

