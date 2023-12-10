package de.maxbossing.elytrafly.module.settings

import de.maxbossing.elytrafly.ElytraFly
import de.maxbossing.elytrafly.data.Permissions
import org.bukkit.entity.Player

object VanillaSettingsProvider : SettingsProvider {
    private val config
        get() = ElytraFly.config

    /** If a Player is allowed to boost himself while flying */
    override fun allowBoosts(player: Player): Boolean {
        // Boosts disabled per config
        if (!config.elytraConfig.boostConfig.boost)
            return false

        // Player is not allowed to boost
        if (!player.hasPermission(Permissions.BOOST))
            return false

        // Max boosts is set to 0
        if (config.elytraConfig.boostConfig.maxBoosts == 0)
            if (!player.hasPermission(Permissions.BOOST_MAX_BYPASS))
                return false

        return true
    }

    /** How many boosts a Player has */
    override fun maxBoosts(player: Player): Int {
        if (player.hasPermission(Permissions.BOOST_MAX_BYPASS)) return -1
        return config.elytraConfig.boostConfig.maxBoosts
    }

    /** How long the delay between boosts for a Player is */
    override fun boostDelay(player: Player): Int {
        if (player.hasPermission(Permissions.DELAY_BYPASS)) return -1
        return config.elytraConfig.boostConfig.boostDelay
    }
}