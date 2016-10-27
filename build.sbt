name := "ScalatraSwaggerCodegen"

version := "1.0"

scalaVersion := "2.11.8"

val swaggerParserVersion = "1.0.18"
val swaggerCoreVersion = "1.5.8"

libraryDependencies += "io.swagger" % "swagger-codegen" % "2.1.6"
libraryDependencies += "io.swagger" % "swagger-parser" % swaggerParserVersion
libraryDependencies += "io.swagger" % "swagger-compat-spec-parser" % swaggerParserVersion
libraryDependencies += "io.swagger" % "swagger-core" % swaggerCoreVersion
