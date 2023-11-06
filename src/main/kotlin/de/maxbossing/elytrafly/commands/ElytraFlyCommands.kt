package de.maxbossing.elytrafly.commands

import de.maxbossing.elytrafly.gui.ZoneGUI
import de.maxbossing.elytrafly.data.Permissions
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.playerExecutor

object ElytraFlyCommands {
    val elytraFlyCommand = commandTree("elytrafly") {
        withPermission(Permissions.GUI)
        playerExecutor { player, _ ->
            ZoneGUI(player)
        }
    }
}