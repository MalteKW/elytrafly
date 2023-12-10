package de.maxbossing.elytrafly.module.placeholders

import de.maxbossing.elytrafly.ElytraFly
import de.maxbossing.elytrafly.data.Zone
import de.maxbossing.elytrafly.elytrafly
import de.maxbossing.elytrafly.module.zones.ZoneManager
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

object PlaceholderAPIExpansionPack: PlaceholderExpansion() {
    override fun getIdentifier(): String = "elytrafly"

    override fun getAuthor(): String = "Max Bossing <info@maxbossing.de>"

    override fun getVersion(): String = elytrafly.pluginMeta.version // Don't care about version, this is published directly with the plugin

    override fun persist(): Boolean  = true // We don't want PlaceholderAPI to reload our Plugin

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        return when (params) {
            "zones_count" -> "${ZoneManager.zones.size}"

            "current_zone" -> {
                player?:return null
                player.player?:return null

                (ZoneManager.isInZone(player.player as Player)?:return null).name
            }

            "max_boosts" -> {
                val boosts = ElytraFly.settingsProvider.maxBoosts(((player?:return null).player?:return null))

                return when (boosts) {
                    (-1) -> "infinite"
                    (0) -> "none"
                    else -> boosts.toString()
                }
            }

            "delay" -> {
                val delay = ElytraFly.settingsProvider.boostDelay(((player?:return null).player?:return null))

                return when (delay) {
                    (-1) -> "none"
                    else -> delay.toString()
                }
            }

            "allow_boost" -> {
                ElytraFly.settingsProvider.allowBoosts(((player?:return null).player?:return null)).toString()
            }
            else -> null
        }
    }

    init {
        this.register()
    }

}