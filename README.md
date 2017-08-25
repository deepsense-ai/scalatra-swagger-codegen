# scalatra-swagger-codegen
Forked scalatra swagger codegen. Patched for our needs and remade into SBT plugin.

In order to use it in your project add following lines to you `plugins.sbt` file:

addSbtPlugin("ai.deepsense" %% "scalatra-swagger-codegen" % "1.23")
```
and in your module sbt file:
```
enablePlugins(ScalatraSwaggerCodegenPlugin)
 
swaggerSpecPath := "path/to/swagger.json"
generatedCodePackage := "your.package.name"
```
It will generate relevant source code in your managed sources directory.
