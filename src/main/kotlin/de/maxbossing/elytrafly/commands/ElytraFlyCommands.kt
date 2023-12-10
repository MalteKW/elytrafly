@file:Suppress("unused")

package de.maxbossing.elytrafly.commands

import de.maxbossing.elytrafly.cBase
import de.maxbossing.elytrafly.data.Permissions
import de.maxbossing.elytrafly.data.saveConfig
import de.maxbossing.elytrafly.gui.ZoneGUI
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.extensions.bukkit.plus
import de.maxbossing.mxpaper.main.prefix
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor

object ElytraFlyCommands {
    val elytraFlyCommand = commandTree("elytrafly") {
        withPermission(Permissions.GUI)
        playerExecutor { player, _ ->
            ZoneGUI(player)
        }
        literalArgument("save") {
            anyExecutor { sender, _ ->
                saveConfig()
                sender.sendMessage(prefix + cmp("Config Saved", cBase))
            }
        }
    }
}