package de.maxbossing.elytrafly

import de.maxbossing.elytrafly.data.Permissions
import de.maxbossing.elytrafly.data.Zone
import de.maxbossing.mxpaper.event.listen
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.extensions.bukkit.isInArea
import de.maxbossing.mxpaper.extensions.bukkit.plus
import de.maxbossing.mxpaper.items.flags
import de.maxbossing.mxpaper.items.itemStack
import de.maxbossing.mxpaper.items.meta
import de.maxbossing.mxpaper.main.prefix
import de.maxbossing.mxpaper.runnables.taskRunLater
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkMeta

object ZoneManager {

    val config = ElytraFly.config

    var chestPlates = mutableMapOf<Player, ItemStack?>()

    var zones: List<Zone>
        get() = ElytraFly.config.zones
        set(value) {
            ElytraFly.config.zones = value
        }


    lateinit var elytra: ItemStack

    lateinit var boostFirework: ItemStack

    var boostDelays = mutableListOf<Player>()


    val leaveListener = listen<PlayerQuitEvent> {
        if (!chestPlates.containsKey(it.player))return@listen
        it.player.inventory.chestplate = chestPlates[it.player]
    }

    val giveListener = listen<PlayerMoveEvent> {
        val player = it.player

        if (player.inventory.chestplate == elytra)return@listen

        for (zone in zones) {
            if (player.isInArea(zone.loc1, zone.loc2)) {
                giveElytra(player)
                player.sendMessage(cmp("You got an Elytra!", cBase))
                if (player.hasPermission(Permissions.BOOST))
                    player.sendMessage(cmp("Press", cBase) + cmp(" F ", cAccent) + cmp("to boost!"))
            }
        }
    }

    val boostListener = listen<PlayerSwapHandItemsEvent> {
        if (!ElytraFly.config.elytraConfig.boostConfig.boost)return@listen
        if (it.player.inventory.chestplate != elytra)return@listen
        if (!it.player.hasPermission(Permissions.BOOST))return@listen
        if (!it.player.isGliding)return@listen
        if (boostDelays.contains(it.player))return@listen
        it.player.boostElytra(boostFirework)
        boostDelays += it.player
        taskRunLater((config.elytraConfig.boostConfig.boostDelay * 20).toLong(), true) { boostDelays -= it.player}
    }

    val groundListener = listen<EntityToggleGlideEvent>(priority = EventPriority.LOWEST) {
        if (it.entity !is Player)return@listen
        if ((it.entity as Player).inventory.chestplate != elytra)return@listen
        if (it.isGliding)return@listen
        for (zone in zones) {
            if ((it.entity as Player).isInArea(zone.loc1, zone.loc2)) {
                return@listen
            }
        }
        taskRunLater((20).toLong(), true){
            (it.entity as Player).inventory.chestplate = chestPlates[it.entity as Player]
            chestPlates[it.entity as Player] = null
        }
    }

    val damageListener = listen<EntityDamageEvent>(priority = EventPriority.HIGHEST) {
        if (it.entity !is Player)return@listen
        if ((it.entity as Player).inventory.chestplate != elytra)return@listen
        it.isCancelled = true
    }

    val moveListener = listen<PlayerMoveEvent> {
        if (it.player.inventory.chestplate != elytra)return@listen
        if (it.player.isGliding)return@listen
        for (zone in zones) {
            if (it.player.isInArea(zone.loc1, zone.loc2)) {
                return@listen
            }
        }
        taskRunLater((20).toLong(), true){
            it.player.inventory.chestplate = chestPlates[it.player]
        }
    }

    init {
        elytra = buildElytra()
        boostFirework = buildBoostFirework()
    }

    private fun giveElytra(player: Player) {
        if (player.inventory.chestplate == elytra)return

        chestPlates[player] = player.inventory.chestplate

        player.inventory.chestplate = elytra
        //TODO: Messages
    }

    fun buildBoostFirework(): ItemStack {
        val bc = config.elytraConfig.boostConfig
        return itemStack(Material.FIREWORK_ROCKET) {
            meta<FireworkMeta> {
                power = bc.boostStrength
                flags(ItemFlag.HIDE_ATTRIBUTES)
                flags(ItemFlag.HIDE_ENCHANTS)
                if (bc.boostDesign.fades.isNotEmpty())
                    this.addEffects(
                        FireworkEffect.builder()
                            .flicker(bc.boostDesign.flicker)
                            .trail(bc.boostDesign.trail)
                            .withColor(bc.boostDesign.colors.map { Color.fromRGB(it) })
                            .withFade(bc.boostDesign.fades.map { Color.fromRGB(it) })
                            .build()
                    )
                else {
                    this.addEffects(
                        FireworkEffect.builder()
                            .flicker(bc.boostDesign.flicker)
                            .trail(bc.boostDesign.trail)
                            .withColor(bc.boostDesign.colors.map { Color.fromRGB(it) })
                            .build()
                    )
                }
            }
        }
    }

    fun buildElytra(): ItemStack {
        return itemStack(Material.ELYTRA) {
            meta {
                displayName(mn.deserialize(ElytraFly.config.elytraConfig.name))

                isUnbreakable = true

                addEnchant(Enchantment.ARROW_DAMAGE, 1, true)
                addEnchant(Enchantment.BINDING_CURSE, 1, true)
                addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
            }
        }
    }

    fun addZone(name: String, loc1: Location, loc2: Location): Boolean {
        return addZone(Zone(name, loc1, loc2))
    }

    fun addZone(zone: Zone): Boolean {
        if (zones.contains(zone))
            return false
        zones += zone
        return true
    }

    fun delZone(name: String): Boolean {
        zones.forEach {
            if (it.name == name) {
                zones -= it
                return true
            }
        }
        return false
    }

}