/**
 * Copyright 2017 deepsense.ai (CodiLime, Inc)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

organization := "ai.deepsense"
name := "scalatra-swagger-codegen"

version := "1.24"

scalaVersion := "2.10.6"

sbtPlugin := true

isSnapshot := false

val swaggerParserVersion = "1.0.18"
val swaggerCoreVersion = "1.5.8"

libraryDependencies += "io.swagger" % "swagger-codegen" % "2.2.0"
libraryDependencies += "io.swagger" % "swagger-parser" % swaggerParserVersion
libraryDependencies += "io.swagger" % "swagger-compat-spec-parser" % swaggerParserVersion
libraryDependencies += "io.swagger" % "swagger-core" % swaggerCoreVersion

publishMavenStyle := true
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
publishArtifact in Test := false
pomIncludeRepository := { _ => false }
pomExtra := (
  <url>https://github.com/deepsense-ai/scalatra-swagger-codegen</url>
    <licenses>
      <license>
        <name>Apache 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0</url>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:deepsense-ai/scalatra-swagger-codegen.git</url>
      <connection>scm:git:git@github.com:deepsense-ai/scalatra-swagger-codegen.git</connection>
    </scm>
    <developers>
      <developer>
        <name>Deepsense</name>
        <email>contact@deepsense.ai</email>
        <url>http://deepsense.ai/</url>
        <organization>Deepsense.ai</organization>
        <organizationUrl>http://deepsense.ai/</organizationUrl>
      </developer>
    </developers>)
