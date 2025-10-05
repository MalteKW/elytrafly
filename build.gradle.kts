import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"

    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

group = "de.maxbossing"
version = 20

repositories {
    mavenCentral()
    maven("https://repo.networkmanager.xyz/repository/maven-public/")

    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") // PlaceholderAPI
}

dependencies {
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")

    paperLibrary("de.maxbossing", "mxpaper", "3.0.0")

    paperLibrary("dev.jorel", "commandapi-bukkit-shade-mojang-mapped", "10.1.2")
    paperLibrary("dev.jorel", "commandapi-bukkit-kotlin", "10.1.2")

    paperLibrary("io.github.rysefoxx.inventory", "RyseInventory-Plugin", "1.6.13")

    compileOnly("net.luckperms", "api", "5.4") // Optional Luckperms API for metadata
    compileOnly("me.clip", "placeholderapi", "2.11.5") // Optional Placeholder API for Placeholders
}

kotlin {
    jvmToolchain(21)
}

tasks {
    runServer {
        minecraftVersion("1.21.8")
    }
}

paper {
    name = "ElytraFly"
    website = "https://maxbossing.de"
    author = "Max Bossing <info@maxbossing.de>"
    prefix = "ElytraFly"

    apiVersion = "1.20"
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD

    foliaSupported = true

    generateLibrariesJson = true

    main = "de.maxbossing.elytrafly.main.ElytraFly"
    loader = "de.maxbossing.elytrafly.main.ElytraFlyLoader"


    serverDependencies {
        register("LuckPerms") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            joinClasspath = true
            required = false
        }
        register("PlaceholderAPI") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            joinClasspath = true
            required = false
        }
    }


}