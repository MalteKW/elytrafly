import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.6.0"

    id("io.papermc.paperweight.userdev") version "1.5.9"
    id("xyz.jpenilla.run-paper") version "2.2.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "de.maxbossing"
version = 17

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/groups/public/")

    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") // PlaceholderAPI
}

dependencies {
    paperweight.paperDevBundle("1.20.1-R0.1-SNAPSHOT")

    implementation("de.maxbossing", "mxpaper", "2.0.0")

    compileOnly("dev.jorel","commandapi-bukkit-core", "9.2.0")
    compileOnly("dev.jorel", "commandapi-bukkit-kotlin", "9.2.0")

    implementation("io.github.rysefoxx.inventory", "RyseInventory-Plugin", "1.6.5")

    compileOnly("net.luckperms", "api", "5.4") // Optional Luckperms API for metadata
    compileOnly("me.clip", "placeholderapi", "2.11.5") // Optional Placeholder API for Placeholders
}

kotlin {
    jvmToolchain(17)
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    runServer {
        minecraftVersion("1.20.1")
    }
}

bukkit {
    name = "ElytraFly"
    website = "https://maxbossing.de"
    author = "Max Bossing <info@maxbossing.de>"
    prefix = "ElytraFly"

    // version is set automatic from project version

    main = "de.maxbossing.elytrafly.ElytraFly"

    softDepend = listOf(
        "LuckPerms",
        "PlaceholderAPI"
    )

    libraries = listOf(
        "org.jetbrains.kotlin:kotlin-stdlib:1.9.20",
        "dev.jorel:commandapi-bukkit-shade:9.2.0",
        "dev.jorel:commandapi-bukkit-kotlin:9.2.0",
        "io.github.rysefoxx.inventory:RyseInventory-Plugin:1.6.5",
        "de.maxbossing:mxpaper:2.0.0"
    )

    apiVersion = "1.20"
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
}