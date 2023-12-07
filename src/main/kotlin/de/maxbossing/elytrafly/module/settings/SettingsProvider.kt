package de.maxbossing.elytrafly.module.settings

import org.bukkit.entity.Player

interface SettingsProvider {

    fun allowBoosts(player: Player): Boolean

    fun maxBoosts(player: Player): Int

    fun boostDelay(player: Player): Int


}