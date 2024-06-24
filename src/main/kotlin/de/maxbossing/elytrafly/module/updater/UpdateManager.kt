package de.maxbossing.elytrafly.module.updater

import de.maxbossing.elytrafly.main.ElytraFly
import de.maxbossing.elytrafly.main.cAccent
import de.maxbossing.elytrafly.data.Permissions
import de.maxbossing.elytrafly.main.elytrafly
import de.maxbossing.mxpaper.MXColors
import de.maxbossing.mxpaper.event.listen
import de.maxbossing.mxpaper.event.register
import de.maxbossing.mxpaper.extensions.bukkit.addUrl
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.extensions.bukkit.plus
import de.maxbossing.mxpaper.main.prefix
import org.bukkit.event.player.PlayerJoinEvent
import java.net.URI
import java.nio.charset.Charset

object UpdateManager {

    private var remoteVersion = -1

    private val announcementListener = listen<PlayerJoinEvent>(register = false) {
        if (!it.player.hasPermission(Permissions.GUI) && !it.player.isOp) return@listen
        if (!ElytraFly.config.updateConfig.announceUpdate) return@listen

        it.player.sendMessage(
            prefix +
                cmp(
                    "ElytraFly is outdated!",
                    color = MXColors.RED
                )
        )
        it.player.sendMessage(
            prefix +
                cmp(
                    "Download the newest version at ",
                    color = MXColors.RED
                ) +
                cmp(
                    "https://modrinth.com/plugin/elytrafly",
                    color = cAccent,
                ).addUrl("https://modrinth.com/plugin/elytrafly")
        )
    }

    private const val VERSION_URL = "https://api.exobyte.dev/version?application=elytrafly"

    @JvmName("getRemoveVersionFromWeb")
    private fun getRemoteVersion(): Int =
        URI(VERSION_URL).toURL().readText(Charset.defaultCharset()).filter { it.isDigit() }.toIntOrNull() ?: -1


    fun needsUpdate(): Boolean =
        if (remoteVersion == -1) false else elytrafly.pluginMeta.version.toInt() < remoteVersion

    init {
        remoteVersion = getRemoteVersion()

        if (needsUpdate()) {
            if (ElytraFly.config.updateConfig.announceUpdate) {
                elytrafly.logger.info("ElytraFly is outdated!")
                elytrafly.logger.info("Download the newest version at https://modrinth.com/plugin/elytrafly")
            }
            announcementListener.register()
        }
    }
}