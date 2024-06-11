import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    mavenLocal()
    mavenCentral()

    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/groups/public/")
    }
    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    implementation(files(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar")))))
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    implementation("fr.minuskube.inv:smart-invs:1.2.7")
    implementation("me.pustinek:interactivemessenger:1.2.2")
    compileOnly("org.spigotmc:spigot-api:1.16.1-R0.1-SNAPSHOT")
}

group = "me.pustinek"
version = "2.1.2"
description = "ItemFilter"
java.sourceCompatibility = JavaVersion.VERSION_16

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.named<ShadowJar>("shadowJar") {
    archiveFileName.set("ItemFilter.jar")

    configurations = listOf(project.configurations.runtimeClasspath.get())
    relocate("me.pustinek.interactivemessenger", "me.pustinek.itemfilter.relocations.interactivemessenger")
    relocate("fr.minuskube.inv", "me.pustinek.itemfilter.relocations.smartinventory")
    // relocate config updater
    relocate("com.tchristofferson.configupdater", "me.pustinek.itemfilter.relocations.configupdater")

    // Include only necessary dependencies
    dependencies {
        include(dependency("fr.minuskube.inv:smart-invs"))
        include(dependency("com.tchristofferson:config-updater"))
        include(dependency("me.pustinek:interactivemessenger"))
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks.named<ShadowJar>("shadowJar").get().archiveFile)
        }
    }
}

defaultTasks("clean", "build")
