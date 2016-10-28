package io.deepsense.swagger.codegen

import scala.collection.JavaConverters._

import sbt.Keys._
import sbt.complete.DefaultParsers._
import sbt.plugins.JvmPlugin
import sbt.{AutoPlugin, Compile, Def, inputKey, settingKey}

object ScalatraSwaggerCodegenPlugin extends AutoPlugin {

  // http://stackoverflow.com/questions/24724406/how-to-generate-sources-in-an-sbt-plugin
  override def requires = JvmPlugin

  object autoImport {
    lazy val swaggerSpecPath = settingKey[String]("Path to the swagger schema file")
    lazy val generatedCodePackage = settingKey[String]("Package of the generated scala code")
    lazy val runCodegenWithArgs = inputKey[Unit]("Run codegen with specified swagger spec file, output dir and java package as task arguments")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    sourceGenerators in Compile += Def.task {
      val outputDir = (sourceManaged in Compile).value

      val files = Scalatra2CodegenRunner.generate(
        swaggerSpecPath.value,
        outputDir.getAbsolutePath + "/swagger-generated",
        generatedCodePackage.value
      )

      files.asScala.toSeq
    }.taskValue,
    runCodegenWithArgs := {
      val args = spaceDelimited("<arg>").parsed
      Scalatra2CodegenRunner.main(args.toArray)
    }
  )

}
