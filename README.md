# BuckBoost Demo Project

## Introduction

This is a basic demo project designed to showcase what Scala.js can do for the replacement of the many Java applets
of the ETHZ iPES webpage. In this project the Java applet
[BuckBoost](http://www.ipes.ethz.ch/ipes/dcdc/e_BuckBoost.html) was reasonably closely recreated in Scala.js to showcase
the improvements to be gained by switching from many standalone Java applets to one singular, central library.

## Running the demo

To see the demo, clone the repo, install [SBT](http://www.scala-sbt.org/), and run sbt in the root directory of the
project. Then type ```fastOptJS``` into SBT will download all necessary dependencies and run a webserver, so the
generated webpage/JavaScript documents will be available under a
[localhost link](localhost:12345/target/scala-2.11/classes/index-dev.html).

## Viewing the Scaladoc

To see the Scaladoc, open target/scala-2.11/api/index.html. To regenerate it in case I forget, type ```doc``` into the SBT
console.