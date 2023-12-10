@file:Suppress("unused")

package de.maxbossing.elytrafly.module.zones

import de.maxbossing.elytrafly.ElytraFly
import de.maxbossing.elytrafly.data.Permissions
import de.maxbossing.mxpaper.event.listen
import de.maxbossing.mxpaper.runnables.taskRunLater
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent

object ZoneListener {

    private val settingsProvider = ElytraFly.settingsProvider

    /** Removes all Stuff from Players when leaving */
    private val leaveListener = listen<PlayerQuitEvent> {
        if (!ZoneManager.chestPlates.containsKey(it.player)) return@listen

        ZoneManager.removeElytra(it.player)
    }

    /** Gives the Elytra to players when in a zone */
    private val giveListener = listen<PlayerMoveEvent> {
        val player = it.player

        // No need to do it again if a Player already has a chestplate
        if (player.inventory.chestplate == ZoneManager.elytra) return@listen

        if (ZoneManager.isInZone(player) != null)
            ZoneManager.giveElytra(player)
    }

    /** Boosts players when they press `F`, or their offhandSwapKey */
    private val boostListener = listen<PlayerSwapHandItemsEvent> {
        if (!ElytraFly.config.elytraConfig.boostConfig.boost) return@listen
        if (it.player.inventory.chestplate != ZoneManager.elytra) return@listen
        if (!it.player.hasPermission(Permissions.BOOST)) return@listen
        if (!it.player.isGliding) return@listen
        if (ZoneManager.boostDelays.contains(it.player)) return@listen

        if (!settingsProvider.allowBoosts(it.player)) return@listen

        val maxBoosts = settingsProvider.maxBoosts(it.player)

        // Increment used boosts for this flight
        if (maxBoosts != -1) {
            if (ZoneManager.usedBoosts.getOrPut(it.player, { 1 }) <= maxBoosts) {
                ZoneManager.usedBoosts[it.player] = ZoneManager.usedBoosts[it.player]!! + 1
            } else {
                return@listen
            }
        }

        it.player.boostElytra(ZoneManager.boostFirework)

        if (it.player.hasPermission(Permissions.DELAY_BYPASS)) return@listen // No need to delay if bypass is set

        ZoneManager.boostDelays += it.player
        taskRunLater(
            (settingsProvider.boostDelay(it.player) * 20).toLong(),
            true
        ) {
            ZoneManager.boostDelays -= it.player
        }
    }

    /** Removes the Elytra and used boosts from a Player who landed */
    private val groundListener = listen<EntityToggleGlideEvent> {
        if (it.entity !is Player) return@listen
        if ((it.entity as Player).inventory.chestplate != ZoneManager.elytra) return@listen
        if (it.isGliding) return@listen

        ZoneManager.usedBoosts.remove(it.entity)

        if (ZoneManager.isInZone(it.entity as Player) != null)
            return@listen

        taskRunLater(
            (20).toLong(),
            true
        ) {
            ZoneManager.removeElytra(it.entity as Player)
        }
    }

    /** Cancels damage coming from the elytra and/or boosts */
    private val damageListener = listen<EntityDamageEvent> {
        if (it.entity !is Player) return@listen

        if ((it.entity as Player).inventory.chestplate != ZoneManager.elytra) return@listen

        if (it.cause != EntityDamageEvent.DamageCause.FALL && it.cause != EntityDamageEvent.DamageCause.FLY_INTO_WALL && it.cause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return@listen

        it.isCancelled = true
    }

    /** Removes the Elytra if going out of zone */
    private val moveListener = listen<PlayerMoveEvent> {
        if (it.player.inventory.chestplate != ZoneManager.elytra) return@listen

        if (it.player.isGliding) return@listen

        if (ZoneManager.isInZone(it.player) != null)
            return@listen

        taskRunLater(
            (20).toLong(),
            true
        ) {
            ZoneManager.removeElytra(it.player)
        }
    }


}