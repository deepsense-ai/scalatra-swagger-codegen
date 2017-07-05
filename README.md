# scalatra-swagger-codegen
Forked scalatra swagger codegen. Patched for our needs and remade into SBT plugin.

In order to use it in your project add following lines to you `plugins.sbt` file:
```
resolvers += Resolver.url("Deepsense Ivy Releases", url(
    "http://artifactory.deepsense.codilime.com:8081/artifactory/deepsense-io-ivy"
))(Resolver.defaultIvyPatterns)
 
addSbtPlugin("io.deepsense" %% "scalatra-swagger-codegen" % "1.3")
```
and in your module sbt file:
```
enablePlugins(ScalatraSwaggerCodegenPlugin)
 
swaggerSpecPath := "neptune/src/main/resources/webapp/static/swagger.json"
generatedCodePackage := "io.deepsense.neptune"
```
It will generate relevant source code in your managed sources directory.
