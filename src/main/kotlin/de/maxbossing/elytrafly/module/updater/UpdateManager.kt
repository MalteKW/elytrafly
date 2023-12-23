package de.maxbossing.elytrafly.module.updater

import de.maxbossing.elytrafly.ElytraFly
import de.maxbossing.elytrafly.cAccent
import de.maxbossing.elytrafly.data.Permissions
import de.maxbossing.elytrafly.elytrafly
import de.maxbossing.elytrafly.httpClient
import de.maxbossing.mxpaper.MXColors
import de.maxbossing.mxpaper.event.listen
import de.maxbossing.mxpaper.event.register
import de.maxbossing.mxpaper.extensions.bukkit.addUrl
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.extensions.bukkit.plus
import de.maxbossing.mxpaper.main.prefix
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.bukkit.event.player.PlayerJoinEvent

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

    private val versionURL = "https://raw.githubusercontent.com/maxbossing/elytrafly/master/version.txt"

    @JvmName("getRemoveVersionFromWeb")
    private fun getRemoteVersion(): Int = runBlocking {
        val response = httpClient.get(versionURL)

        if (response.status != HttpStatusCode.OK)
            return@runBlocking -1

        val text = response.bodyAsText().replace(" ", "").replace("\n", "")

        if (text.toIntOrNull() == null)
            return@runBlocking -1

        text.toInt()
    }

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