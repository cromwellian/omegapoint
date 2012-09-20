import sbt._
import Keys._
import samskivert.ProjectBuilder
import ProguardPlugin._

object TriplePlayBuild extends Build {
  val builder = new ProjectBuilder("pom.xml") {
    override val globalSettings = Seq(
      crossPaths    := false,
      scalaVersion  := "2.9.1",
      javacOptions  ++= Seq("-Xlint", "-Xlint:-serial", "-source", "1.6", "-target", "1.6"),
      scalacOptions ++= Seq("-unchecked", "-deprecation"),
      javaOptions   ++= Seq("-ea"),
      fork in Compile := true
    )
    override def projectSettings (name :String) = name match {
      case "core" => seq(
        // no scala-library dependency here
        autoScalaLibrary := false,
        // include source in our jar for GWT
        unmanagedResourceDirectories in Compile <+= baseDirectory / "src/main/java",
        // disable doc publishing, TODO: reenable when scaladoc doesn't choke on our code
        publishArtifact in (Compile, packageDoc) := false,
        // wire junit into SBT
        libraryDependencies ++= Seq(
	        "com.novocode" % "junit-interface" % "0.7" % "test->default"
        )
      )
      case "tools" => proguardSettings ++ seq(
        mainClass in (Compile, run) := Some("tripleplay.tools.FramePacker"),
        proguardOptions += keepMain("tripleplay.tools.FramePacker"),
        proguardOptions += "-dontnote scala.Enumeration"
      )
      case "demo-core" => Seq(
        // copy resources from playn/tests/resources
        unmanagedResourceDirectories in Compile <+= baseDirectory / "src/main/java",
        excludeFilter in unmanagedResources ~= { _ || "*.java" },
        publish := false,
        publishLocal := false
      )
      case "demo-java" => LWJGLPlugin.lwjglSettings ++ Seq(
        publish := false,
        publishLocal := false
      )
      case _ => Nil
    }
  }

  lazy val core = builder("core")
  lazy val ios = builder("ios")
  lazy val tools = builder("tools")
  lazy val demoCore = builder("demo-core")
  lazy val demoJava = builder("demo-java")

  // one giant fruit roll-up to bring them all together
  lazy val tripleplay = Project("tripleplay", file(".")) aggregate(
    core, ios, tools, demoCore, demoJava)
}
