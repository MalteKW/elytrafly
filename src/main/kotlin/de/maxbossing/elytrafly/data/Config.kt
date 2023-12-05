package de.maxbossing.elytrafly.data

import de.maxbossing.elytrafly.ElytraFly
import de.maxbossing.elytrafly.elytrafly
import de.maxbossing.mxpaper.extensions.kotlin.createIfNotExists
import de.maxbossing.mxpaper.serialization.LocationSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.FireworkEffect.Type
import org.bukkit.Location
import java.io.File
import java.lang.IllegalArgumentException
import kotlin.io.path.Path

@Serializable
data class Config(
    val prefix: String,
    val debug: Boolean,
    var elytraConfig: ElytraConfig,
    var zones: List<Zone>
)

@Serializable
data class ElytraConfig(
    // Name of the Elytra, serialized with MiniMessage
    var name: String,
    var boostConfig: BoostConfig
)

@Serializable
data class BoostConfig(
    var boost: Boolean,
    var maxBoosts: Int = -1,
    var boostStrength: Int,
    var boostDelay: Int,
    var boostDesign: BoostDesign
)

@Serializable
data class BoostDesign(
    var flicker: Boolean,
    var trail: Boolean,
    var colors: List<Int>,
    var fades: List<Int>,
)

@Serializable
data class Zone(
    var name: String,
    var loc1: @Serializable(with = LocationSerializer::class) Location,
    var loc2: @Serializable(with = LocationSerializer::class) Location
)

fun loadConfig(): Config {

    val configFile = File(elytrafly.dataFolder, "config.json")

    if (!configFile.exists()) {
        configFile.createIfNotExists()
        configFile.writeText(
            Json.encodeToString(
                Config.serializer(),

                Config(
                    prefix = "<gray>[<gold>ElytraFly<gray>]<reset> ",
                    debug = false,

                    elytraConfig = ElytraConfig(
                        name = "<rainbow>Elytra</rainbow>",

                        boostConfig = BoostConfig(
                            boost = true,
                            maxBoosts = -1,
                            boostStrength = 1,
                            boostDelay = 2,
                            boostDesign = BoostDesign(
                                flicker = true,
                                trail = true,
                                colors = listOf(
                                    Color.WHITE.asRGB()
                                ),
                                fades = listOf(
                                    Color.GRAY.asRGB(),
                                    Color.SILVER.asRGB()
                                )
                            )
                        )

                    ),

                    zones = listOf<Zone>()
                )

            )
        )
    }

    val json: Config =  Json.decodeFromString(configFile.readText())
    if (json.elytraConfig.boostConfig.boostDesign.colors.isEmpty()) {
        throw IllegalArgumentException("Colors cannot be empty!")
    }
    return json
}

fun saveConfig() {
    File(elytrafly.dataFolder, "config.json").writeText(Json.encodeToString(Config.serializer(), ElytraFly.config))
}