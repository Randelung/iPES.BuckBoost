import com.lihaoyi.workbench.Plugin._

enablePlugins(ScalaJSPlugin)

workbenchSettings

name := "Example"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.5"

libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.4.5"

libraryDependencies += "be.doeraene" %%% "scalajs-jquery" % "0.8.0"

bootSnippet := "iPES.BuckBoost.BuckBoost().main();"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)

scalaJSStage in Global := FastOptStage