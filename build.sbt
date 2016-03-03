import com.lihaoyi.workbench.Plugin._

enablePlugins(ScalaJSPlugin)

libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.4.5"

workbenchSettings

name := "BuckBoost Converter"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

bootSnippet := "iPES.BuckBoost.BuckBoost().main();"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)

scalaJSStage in Global := FastOptStage