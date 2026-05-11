plugins {
    id 'java-library'
    id 'eclipse'
    id 'idea'
    id 'maven-publish'
    id 'net.neoforged.gradle.userdev' version '7.0.80'
}

version = '1.0.0'
group = 'com.sighs.baeffect'

base {
    archivesName = 'baeffect'
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

minecraft.accessTransformers.file rootProject.file('src/main/resources/META-INF/accesstransformer.cfg')

runs {
    configureEach {
        systemProperty 'forge.logging.markers', 'REGISTRIES'
        systemProperty 'forge.logging.console.level', 'debug'
        modSource project.sourceSets.main
    }
    client {
        systemProperty 'forge.enabledGameTestNamespaces', project.mod_id
    }
    server {
        systemProperty 'forge.enabledGameTestNamespaces', project.mod_id
        programArgument '--nogui'
    }
}

sourceSets.main.resources { srcDir 'src/generated/resources' }

dependencies {
    implementation 'net.neoforged:neoforge:21.1.116'
}

tasks.withType(ProcessResources).configureEach {
    var replaceProperties = [
            minecraft_version   : minecraft_version,
            minecraft_version_range: minecraft_version_range,
            neo_version         : neo_version,
            neo_version_range   : neo_version_range,
            loader_version_range: loader_version_range,
            mod_id              : mod_id,
            mod_name            : mod_name,
            mod_license         : mod_license,
            mod_version         : mod_version,
            mod_authors         : mod_authors,
            mod_description     : mod_description
    ]
    inputs.properties replaceProperties
    filesMatching(['META-INF/mods.toml']) {
        expand replaceProperties
    }
}

publishing {
    publications {
        register('mavenJava', MavenPublication) {
            from components.java
        }
    }
    repositories {
        maven {
            url "https://your.maven.repo"
        }
    }
}