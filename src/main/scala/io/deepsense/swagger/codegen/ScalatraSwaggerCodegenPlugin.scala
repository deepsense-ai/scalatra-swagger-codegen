package io.deepsense.swagger.codegen

import io.deepsense.swagger.CodegenRunner

import scala.collection.JavaConverters._
import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin
import sbt.{AutoPlugin, Compile, Def, File, FileFunction, FilesInfo, settingKey}

object ScalatraSwaggerCodegenPlugin extends AutoPlugin {

  // http://stackoverflow.com/questions/24724406/how-to-generate-sources-in-an-sbt-plugin
  override def requires = JvmPlugin

  object autoImport {
    lazy val swaggerSpecPath: SettingKey[String] = settingKey[String]("Path to the swagger schema file")
    lazy val swaggerCodegen: SettingKey[String] = settingKey[String]("Generated language")
    lazy val generatedCodePackage: SettingKey[String] = settingKey[String]("Package of the generated scala code")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    sourceGenerators in Compile += Def.task {
      val outputDir = (sourceManaged in Compile).value

      val cachedFun = FileFunction.cached(
        streams.value.cacheDirectory / "swagger-generated-cache",
        inStyle = FilesInfo.hash,
        outStyle = FilesInfo.hash) { (_: Set[File]) =>
          val files = CodegenRunner.generate(
            swaggerSpecPath.value,
            outputDir.getAbsolutePath + "/swagger-generated",
            swaggerCodegen.value,
            generatedCodePackage.value)
          files.asScala.toSet
        }
      cachedFun(Set(file(swaggerSpecPath.value))).toSeq
      // Regenerate only if swagger spec file changed
    }.taskValue
  )

}
