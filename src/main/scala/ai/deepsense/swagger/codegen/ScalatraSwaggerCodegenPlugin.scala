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
 *
 */

package ai.deepsense.swagger.codegen

import ai.deepsense.swagger.CodegenRunner

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
    lazy val generatedApiPackage: SettingKey[String] = settingKey[String](
      "Package of the generated API code (default value from 'generatedCodePackage')")
  }

  import autoImport._

  override lazy val projectSettings = Seq(

    generatedApiPackage := generatedCodePackage.value,

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
            generatedApiPackage.value,
            generatedCodePackage.value)
          files.asScala.toSet
        }
      cachedFun(Set(file(swaggerSpecPath.value))).toSeq
      // Regenerate only if swagger spec file changed
    }.taskValue
  )

}
