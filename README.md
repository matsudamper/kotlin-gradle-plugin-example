# Overview
KotlinでGradleのプラグインを作成し、タスクを作成するサンプル。  

# Directory
IntelliJ等で、以下のディレクトリを別々に開くと良い。  
- plugin
  - plugin本体が入っている
- test
  - pluginを使用するコードが入っている

# Detail
## Plugin Main
### File
注目すべき点
build.gradle.kts
```kotlin
plugins {
    // 現状ではGradle Kotlin DSLのKotlinは1.5.21に依存していると警告が出るので、1.5.21にしている。
    id("org.jetbrains.kotlin.jvm") version "1.5.21"

    // Kotlinで開発する上で便利なDSLを提供してくれる
    `kotlin-dsl`

    // Gradleのプラグインを作成するのに必要
    `java-gradle-plugin`

    // Mavenに配布するのに使用する
    `maven-publish`
}

group = "net.matsudamper"

gradlePlugin {
    (plugins) {
        // package名が net.matsudamper.kotlin_gradle_plugin_example
        // ファイル名が ExampleGradle.gradle.kts というファイルという場合の例
        "net.matsudamper.kotlin_gradle_plugin_example.ExampleGradle" {
            // 以下のように参照できる
            // id("kotlin-gradle-plugin-example") version "1.0"
            id = "kotlin-gradle-plugin-example"
            version = "1.0"

            // "${package名}.${ファイル名の先頭}Plugin" を付ける
            // (<name>.gradle.kts -> <name>Plugin)
            implementationClass = "net.matsudamper.kotlin_gradle_plugin_example.ExampleGradlePlugin"
        }
    }
}
```

ExampleGradle.gradle.kts
pluginを定義し、applyを呼ぶ。無名クラスは使用不可だった。
```kotlin
package net.matsudamper.kotlin_gradle_plugin_example

import org.gradle.api.Project

class MyPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            // exampleTaskというタスクを作成し、タスクを実行するとOKとプリントする。
            tasks.create("exampleTask") {
                doLast {
                    println("OK")
                }
            }
        }
    }
}
apply<MyPlugin>()
```

### Publish
ローカルに配布する。  
```
./gradlew publishToMavenLocal
```
`~/.m2/repository/` の中を見てみる。グループにすると2つのものが見つかる

1つはPluginファイル  
kotlin-gradle-plugin-example/kotlin-gradle-plugin-example.gradle.plugin/
  - 1.0/kotlin-gradle-plugin-example.gradle.plugin-1.0.pom
  - maven-metadata-local.xml

`maven-metadata-local.xml`  
実体ファイルへの参照しか無い。  
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>kotlin-gradle-plugin-example</groupId>
  <artifactId>kotlin-gradle-plugin-example.gradle.plugin</artifactId>
  <version>1.0</version>
  <packaging>pom</packaging>
  <dependencies>
    <dependency>
      <groupId>net.matsudamper</groupId>
      <artifactId>kotlin-gradle-plugin-example</artifactId>
      <version>1.0</version>
    </dependency>
  </dependencies>
</project>
```

もう1つは実体  
net/matsudamper/kotlin-gradle-plugin-example/
  - 1.0/
    - kotlin-gradle-plugin-example-1.0.jar
    - kotlin-gradle-plugin-example-1.0.module
    - kotlin-gradle-plugin-example-1.0.pom
  - maven-metadata-local.xml

### Description
build.gradle.ktsのimplementationClassに"${package名}.${ファイル名の先頭}Plugin" を付けた理由。  

`java-gradle-plugin` を使用してjarファイルを作成した場合、jar(zip)をunzipしてみるとPluginが末尾に付いている。その為、末尾にPluginを付けた。末尾にPluginが付く理由は不明。  

## User
使用する側は以下のように追加するだけ。  
```kotlin
plugins {
    id("kotlin-gradle-plugin-example") version "1.0"
}
```

これでタスクが実行できる。  
```
./gradlew exampleTask
```
