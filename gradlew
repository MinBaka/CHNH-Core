plugins {
    id 'java-library'
    id 'eclipse'
    id 'idea'
    id 'maven-publish'
    // 建议使用 7.0.145 或更高版本以获得更好的 1.21.1 支持
    id 'net.neoforged.gradle.userdev' version '7.0.145'
}

// 建议将 group 改为您的名字，体现归属权喵
group = 'com.minbaka.chnhcore'
version = mod_version

base {
    archivesName = 'chnh-core'
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

// 如果您之前删除了这个文件，可以先把这行注释掉喵
// minecraft.accessTransformers.file rootProject.file('src/main/resources/META-INF/accesstransformer.cfg')

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
    implementation "net.neoforged:neoforge:${neo_version}"

    // 记得保留咱们之前的 ApricityUI 依赖喵！
    implementation files('libs/ApricityUI-neoforge-1.21.1-1.1.0.jar')
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
    filesMatching(['META-INF/mods.toml', 'neoforge.mods.toml']) {
        expand replaceProperties
    }
}

publishing {
    publications {
        register('mavenJava', MavenPublication) {
            from components.java
        }
    }
}