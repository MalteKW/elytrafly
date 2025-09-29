package de.maxbossing.elytrafly.main

import de.maxbossing.elytrafly.commands.ElytraFlyCommands
import de.maxbossing.elytrafly.data.Config
import de.maxbossing.elytrafly.data.loadConfig
import de.maxbossing.elytrafly.metrics.Metrics
import de.maxbossing.elytrafly.module.placeholders.PlaceholderAPIExpansionPack
import de.maxbossing.elytrafly.module.settings.LuckpermsSettingsProvider
import de.maxbossing.elytrafly.module.settings.SettingsProvider
import de.maxbossing.elytrafly.module.settings.VanillaSettingsProvider
import de.maxbossing.elytrafly.module.updater.UpdateManager
import de.maxbossing.elytrafly.module.zones.ZoneManager
import de.maxbossing.elytrafly.utils.debug
import de.maxbossing.mxpaper.MXColors
import de.maxbossing.mxpaper.extensions.pluginManager
import de.maxbossing.mxpaper.main.MXPaper
import de.maxbossing.mxpaper.main.prefix
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import io.github.rysefoxx.inventory.plugin.pagination.InventoryManager
import net.kyori.adventure.text.minimessage.MiniMessage

class ElytraFly : MXPaper() {
    companion object {
        // Instance
        lateinit var elytrafly: ElytraFly

        // Config
        lateinit var config: Config

        // SettingsProvider
        lateinit var settingsProvider: SettingsProvider
    }

    // Inventory API
    var invManager = InventoryManager(this)

    override fun load() {
        // Commands
        CommandAPI.onLoad(CommandAPIBukkitConfig(this))

        // Set instance
        elytrafly = this

        logger.info("ElytraFly loaded!")
    }

    override fun startup() {

        // Load config
        Companion.config = loadConfig(); debug("Loaded config")

        // Set prefix
        prefix = mn.deserialize(Companion.config.prefix); debug("Deserialized Prefix")

        // Commands
        CommandAPI.onEnable(); debug("CommandAPI Enabled")

        // Inventories
        invManager.invoke(); debug("Inventory API enabled")

        // Set SettingsProvider
        settingsProvider = if (pluginManager.isPluginEnabled("LuckPerms")) {
            LuckpermsSettingsProvider
        } else {
            VanillaSettingsProvider
        }

        if (pluginManager.isPluginEnabled("PlaceholderAPI")) {
            PlaceholderAPIExpansionPack
            debug("PlaceholderAPI Expansion registered")
        }

        // This manages the whole elytra system
        ZoneManager; debug("ZoneManager started")

        // Admin GUI
        ElytraFlyCommands; debug("Commands Loaded")

        // bStats Log
        // We don't want fake data
        if (!Companion.config.debug)
            Metrics(this, 20247)


        // Update Manager
        //UpdateManager; debug("UpdateManager Loaded!")

        logger.info("ElytraFly enabled!")
    }

    override fun shutdown() {
        // Save config
        de.maxbossing.elytrafly.data.saveConfig(); debug("Config Saved")

        logger.info("ElytraFly disabled!")
    }

}

val elytrafly by lazy { ElytraFly.elytrafly }

val mn = MiniMessage.miniMessage()

val cBase = MXColors.GRAY

val cAccent = MXColors.CORNFLOWERBLUE