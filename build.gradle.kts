import org.gradle.internal.classpath.Instrumented.systemProperty

plugins {
    id("java-library")
    id("net.neoforged.moddev") version "2.0.142"
}

val modId: String = project.properties["mod_id"].toString()
val modName: String = project.properties["mod_name"].toString()
val modVersion: String = project.properties["mod_version"].toString()
val modLicense: String = project.properties["mod_license"].toString()
val minecraftVersion: String = project.properties["minecraft_version"].toString()
val minecraftVersionRange: String = project.properties["minecraft_version_range"].toString()
val neoVersion: String = project.properties["neo_version"].toString()
val neoforgeVersionRange: String = project.properties["neoforge_version_range"].toString()

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.BIN
}

version = modVersion
group = "com.thesilentnights.$modId"

base {
    archivesName = "${modId}-neoforge-${minecraftVersion}"
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

sourceSets {
    named("main") {
        resources {
            srcDir("src/generated/resources")
            exclude("**/*.bbmodel")
            exclude("src/generated/**/.cache")
        }
    }
}

neoForge {
    version = neoVersion

    runs {
        create("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        create("gameTestServer") {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        create("data") {
            clientData()
            programArguments.addAll(listOf(
                "--mod", modId, "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            ))
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
}

configurations {
    val localRuntime = maybeCreate("localRuntime")
    named("runtimeClasspath") {
        extendsFrom(localRuntime)
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {

    val sqliteDep = dependencies.create("org.xerial:sqlite-jdbc") as ExternalModuleDependency
    sqliteDep.version {
        strictly("[3.53.0.0]")
        prefer("3.53.0.0")
    }
    runtimeOnly(sqliteDep)
    jarJar(sqliteDep)
}

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
        "minecraft_version" to minecraftVersion,
        "minecraft_version_range" to minecraftVersionRange,
        "neo_version" to neoVersion,
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_license" to modLicense,
        "mod_version" to modVersion,
        "neoforge_version_range" to neoforgeVersionRange,
    )
    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

sourceSets.main.get().resources.srcDir(generateModMetadata)

neoForge.ideSyncTask(generateModMetadata)
