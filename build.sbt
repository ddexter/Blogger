name := "Blogger"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.commonjava.googlecode.markdown4j" % "markdown4j" % "2.2-cj-1.0",
  "org.testng" % "testng" % "6.1.1" % "test",
  "org.postgresql" % "postgresql" % "9.4-1205-jdbc42"
)
