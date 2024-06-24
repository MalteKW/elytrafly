@file:Suppress("unused")

package de.maxbossing.elytrafly.module.zones

import de.maxbossing.elytrafly.main.ElytraFly
import de.maxbossing.elytrafly.main.cBase
import de.maxbossing.elytrafly.data.Permissions
import de.maxbossing.elytrafly.data.Zone
import de.maxbossing.elytrafly.main.mn
import de.maxbossing.elytrafly.module.settings.VanillaSettingsProvider
import de.maxbossing.mxpaper.MXColors
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.extensions.bukkit.isInArea
import de.maxbossing.mxpaper.extensions.bukkit.plus
import de.maxbossing.mxpaper.extensions.pluginKey
import de.maxbossing.mxpaper.items.flags
import de.maxbossing.mxpaper.items.itemStack
import de.maxbossing.mxpaper.items.meta
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.persistence.PersistentDataType

object ZoneManager {

    private val config = ElytraFly.config

    val ELYTRA_KEY = pluginKey("elytra")

    /** Saved chestplates for players that are flying */
    var chestPlates = mutableMapOf<Player, ItemStack?>()

    /** The Boosts that A Player has used in his flight */
    var usedBoosts = mutableMapOf<Player, Int>()

    /** The [Zone] List */
    var zones: List<Zone>
        get() = ElytraFly.config.zones
        set(value) {
            ElytraFly.config.zones = value
        }

    /** The elytra that is used */
    var elytra: ItemStack

    /** The boost firework used for boosting players */
    lateinit var boostFirework: ItemStack
        private set

    /** Players that are currently delayed from using boosts */
    var boostDelays = mutableListOf<Player>()

    init {
        rebuildBoostFirework() // Create Boost Firework
        elytra = buildElytra() // Create Elytra
        ZoneListener // Init listeners
    }

    /**
     * Gives a Player a One-time Elytra
     *
     * Also sends messages if the Player can boost etc
     *
     * @param player The Player to give the elytra to
     */
    fun giveElytra(player: Player) {
        if (player.inventory.chestplate?.itemMeta?.persistentDataContainer?.has(ZoneManager.ELYTRA_KEY) == true) return

        chestPlates[player] = player.inventory.chestplate // save chestplate

        player.inventory.chestplate = elytra // give elytra

        player.sendMessage(cmp("You got an Elytra!", cBase))

        if (VanillaSettingsProvider.allowBoosts(player))
            player.sendMessage(
                cmp("Press ", cBase) +
                    Component.keybind().keybind("key.swapOffhand").build().color(MXColors.CORNFLOWERBLUE) +
                    cmp(" to boost!")
            )
    }

    /**
     * Removes a One-time Elytra from a [Player]
     *
     * Also resets delays etc
     *
     * @param player The Player to remove the Elytra from
     */
    fun removeElytra(player: Player) {
        if (player.inventory.chestplate?.itemMeta?.persistentDataContainer?.has(ZoneManager.ELYTRA_KEY) == false || player.inventory.chestplate == null) return

        player.inventory.chestplate = chestPlates[player]

        chestPlates.remove(player) // remove chestplate
        boostDelays.remove(player) // remove delays
        usedBoosts.remove(player) // Remove used boosts for this flight

    }

    /** Sets the [boostFirework] according to configuration */
    fun rebuildBoostFirework() {
        val bc = config.elytraConfig.boostConfig
        boostFirework = itemStack(Material.FIREWORK_ROCKET) {
            meta<FireworkMeta> {

                power = bc.boostStrength

                flags(ItemFlag.HIDE_ATTRIBUTES)
                flags(ItemFlag.HIDE_ENCHANTS)

                if (bc.boostDesign.fades.isNotEmpty())
                    this.addEffects(
                        FireworkEffect.builder()
                            .flicker(
                                bc.boostDesign.flicker
                            )
                            .trail(
                                bc.boostDesign.trail
                            )
                            .withColor(
                                bc.boostDesign
                                    .colors
                                    .map {
                                        Color.fromRGB(it)
                                    }
                            )
                            .withFade(
                                bc.boostDesign
                                    .fades
                                    .map {
                                        Color.fromRGB(it)
                                    }
                            )
                            .build()
                    )
                else {
                    this.addEffects(
                        FireworkEffect.builder()
                            .flicker(
                                bc.boostDesign.flicker
                            )
                            .trail(
                                bc.boostDesign.trail
                            )
                            .withColor(
                                bc.boostDesign
                                    .colors
                                    .map {
                                        Color.fromRGB(it)
                                    }
                            )
                            .build()
                    )

                }
            }
        }
    }

    /**
     * Build the Elytra for One-time-use according to configuration
     *
     * @return The Elytra
     */
    fun buildElytra(): ItemStack {
        return itemStack(Material.ELYTRA) {
            meta {
                displayName(
                    mn.deserialize(
                        ElytraFly
                            .config
                            .elytraConfig
                            .name
                    )
                )

                persistentDataContainer.set(ELYTRA_KEY, PersistentDataType.BYTE, 0xff.toByte())

                isUnbreakable = true

                addEnchant(
                    Enchantment.POWER,
                    1,
                    true
                )

                addEnchant(
                    Enchantment.BINDING_CURSE,
                    1,
                    true
                )

                addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                addItemFlags(ItemFlag.HIDE_ENCHANTS)
            }
        }
    }

    /**
     * Adds a [Zone] to the Zone List
     *
     * @param name The [Zone] Name
     * @param loc1 The first corner of the [Zone]
     * @param loc2 The opposite corner of the [Zone]
     * @return false if zone already exists
     */
    fun addZone(name: String, loc1: Location, loc2: Location): Boolean = addZone(Zone(name, true, false, loc1, loc2))

    /**
     * Adds a [Zone] to the Zone List
     *
     * @param zone The [Zone] to add
     * @return false if zone already exists
     */
    fun addZone(zone: Zone): Boolean {
        if (zones.contains(zone))
            return false
        zones += zone
        return true
    }

    /**
     * Deletes a [Zone] from the Zone List
     *
     * @param name the Name of the zon to be deleted
     * @return false if the zone was not found
     */
    fun delZone(name: String): Boolean {
        zones.forEach {
            if (it.name == name) {
                zones -= it
                return true
            }
        }
        return false
    }

    fun isInZone(player: Player): Zone? {
        for (zone in zones) {
            if (isActive(zone.name) == false)
                continue

            if (isRestricted(zone.name) == true)
                if (!player.hasPermission(Permissions.ZONE_BYPASS) && !player.hasPermission(
                        Permissions.zoneRestriction(
                            zone.name
                        )
                    )
                )
                    continue

            if (player.isInArea(zone.loc1, zone.loc2))
                return zone
        }
        return null
    }

    fun activateZone(name: String): Boolean {
        zones.forEach {
            if (it.name == name) {
                if (it.active)
                    return false

                it.active = true

                return true
            }
        }
        return false
    }

    fun deactivateZone(name: String): Boolean {
        zones.forEach {
            if (it.name == name) {
                if (!it.active)
                    return false

                it.active = false
                return true
            }
        }
        return false
    }

    fun isActive(name: String): Boolean? {
        zones.forEach {
            if (it.name == name) {
                return it.active
            }
        }
        return null
    }

    fun isRestricted(name: String): Boolean? {
        zones.forEach {
            if (it.name == name) {
                return it.restricted
            }
        }
        return null
    }

    fun restrictZone(name: String): Boolean {
        zones.forEach {
            if (it.name == name) {
                if (it.restricted)
                    return false

                it.restricted = true
                return true
            }
        }
        return false
    }

    fun unrestrictZone(name: String): Boolean {
        zones.forEach {
            if (it.name == name) {
                if (!it.restricted)
                    return false

                it.restricted = false
                return true
            }
        }
        return false
    }

    fun alreadyExists(name: String): Boolean {
        for (zone in zones)
            if (zone.name == name)
                return true
        return false
    }

}