package io.deepsense.swagger.codegen

import sbt.complete.DefaultParsers._
import sbt.{AutoPlugin, inputKey, settingKey, taskKey}

object ScalatraSwaggerCodegenPlugin extends AutoPlugin {

  object autoImport {
    lazy val swaggerSpecPath = settingKey[String]("Path to the swagger schema file")
    lazy val generatedCodeOutputDir = settingKey[String]("Path for the generated output")
    lazy val generatedCodePackage = settingKey[String]("Package of the generated scala code")
    lazy val runCodegen = taskKey[Unit]("Run codegen with specified swagger spec file, output dir and java package")
    lazy val runCodegenWithArgs = inputKey[Unit]("Run codegen with specified swagger spec file, output dir and java package as task arguments")
  }

  import autoImport._
  override lazy val projectSettings = Seq(
    runCodegen := {
      Scalatra2CodegenRunner.generate(swaggerSpecPath.value, generatedCodeOutputDir.value, generatedCodePackage.value)
    },
    runCodegenWithArgs := {
      val args = spaceDelimited("<arg>").parsed
      Scalatra2CodegenRunner.main(args.toArray)
    }
  )

}
