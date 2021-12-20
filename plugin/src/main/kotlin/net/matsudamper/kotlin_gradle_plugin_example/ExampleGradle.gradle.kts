package net.matsudamper.kotlin_gradle_plugin_example

import org.gradle.api.Project

class MyPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            tasks.create("exampleTask") {
                doLast {
                    println("OK")
                }
            }
        }
    }
}

apply<MyPlugin>()
