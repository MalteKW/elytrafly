package de.maxbossing.elytrafly.gui

import de.maxbossing.elytrafly.*
import de.maxbossing.elytrafly.data.Zone
import de.maxbossing.elytrafly.module.zones.ZoneManager
import de.maxbossing.elytrafly.utils.skullTexture
import de.maxbossing.mxpaper.MXColors
import de.maxbossing.mxpaper.MXHeads
import de.maxbossing.mxpaper.chat.input.awaitChatInput
import de.maxbossing.mxpaper.event.SingleListener
import de.maxbossing.mxpaper.event.listen
import de.maxbossing.mxpaper.event.unregister
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.extensions.bukkit.plus
import de.maxbossing.mxpaper.items.*
import de.maxbossing.mxpaper.main.prefix
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem
import io.github.rysefoxx.inventory.plugin.content.InventoryContents
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider
import io.github.rysefoxx.inventory.plugin.pagination.Pagination
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.SkullMeta
import kotlin.random.Random

class ZoneGUI(val player: Player) {

    var zones: List<Zone>
        get() = ElytraFly.config.zones
        set(value) {
            ElytraFly.config.zones = value
        }


    fun zoneButton(zone: Zone): IntelligentItem {
        fun pseudoRandomMaterial(zone: Zone) = Material.entries.filter { it.name.endsWith("ARMOR_TRIM_SMITHING_TEMPLATE") }.random(Random(zone.hashCode()))

        fun xyzString(location: Location): String = "${location.x} ${location.y} ${location.z}"

        return IntelligentItem.of(
            itemStack(pseudoRandomMaterial(zone)) {
                meta {
                    displayName(cmp(zone.name, cAccent))
                    flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ITEM_SPECIFICS)
                    setLore {
                        lorelist += cmp("Active: ", cBase) + if (ZoneManager.isActive(zone.name)) cmp("yes", MXColors.GREEN) else cmp("no", MXColors.RED)
                        lorelist += cmp("")
                        lorelist += cmp("World: ", cBase) + cmp(zone.loc1.world.name, cAccent)
                        lorelist += cmp("loc 1: ", cBase) + cmp(xyzString(zone.loc1), cAccent)
                        lorelist += cmp("loc 2: ", cBase) + cmp(xyzString(zone.loc2), cAccent)
                        lorelist += cmp("")
                        lorelist += cmp("Left-Click  ∙  ", cBase) + cmp("Toggle", cAccent)
                        lorelist += cmp("Right-Click  ∙  ", cBase) + cmp("Delete", bold = true, color = MXColors.INDIANRED)
                    }
                }
            }
        ) {
            if (it.isRightClick)
                ZoneDeleteGUI(player, zone)
            if (it.isLeftClick)
                if (zone.active)
                    ZoneManager.deactivateZone(zone.name)
                else
                    ZoneManager.activateZone(zone.name)

            it.currentItem!!.meta {
                setLore {
                    lorelist += cmp("Active: ", cBase) + if (ZoneManager.isActive(zone.name)) cmp("yes", MXColors.GREEN) else cmp("no", MXColors.RED)
                    lorelist += cmp("")
                    lorelist += cmp("World: ", cBase) + cmp(zone.loc1.world.name, cAccent)
                    lorelist += cmp("loc 1: ", cBase) + cmp(xyzString(zone.loc1), cAccent)
                    lorelist += cmp("loc 2: ", cBase) + cmp(xyzString(zone.loc2), cAccent)
                    lorelist += cmp("")
                    lorelist += cmp("Left-Click  ∙  ", cBase) + cmp("Toggle", cAccent)
                    lorelist += cmp("Right-Click  ∙  ", cBase) + cmp("Delete", bold = true, color = MXColors.INDIANRED)
                }
            }
        }
    }

    fun createButton(): IntelligentItem {

        fun awaitBlockInput(callback: (Location) -> Unit) {
            var listener: SingleListener<PlayerInteractEvent>?= null

            fun receive(loc: Location) {
                listener!!.unregister()
                callback(loc)
            }

            player.sendMessage(prefix + cmp("Click on a block to set a location", cBase))

            listener = listen<PlayerInteractEvent> { event ->
                if (event.player == player) {
                    val clickedBlock = event.clickedBlock
                    event.isCancelled = true
                    if (clickedBlock != null) {
                        receive(clickedBlock.location)
                    } else {
                        awaitBlockInput(callback)
                    }
                }
            }
        }


        return IntelligentItem.of(
            itemStack(Material.SUNFLOWER) {
                meta {
                    displayName(cmp("New Zone", cBase))
                }
            }
        ) {
            player.closeInventory()

            player.awaitChatInput(
                question = prefix + cmp("Input the name of the Zone...", cBase),
            ){ it1 ->
                if (it1.input == null)
                    return@awaitChatInput
                else {
                    awaitBlockInput { it2 ->
                        awaitBlockInput { it3 ->
                            ZoneManager.addZone(Zone(PlainTextComponentSerializer.plainText().serialize(it1.input!!), true, it2, it3))
                            player.sendMessage(prefix + cmp("Zone added!", cBase))
                        }
                    }
                }
            }
        }
    }

    fun settingsButton(): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.CRAFTING_TABLE) {
                meta {
                    displayName(cmp("Settings", cBase))
                }
            }
        ) {
            SettingsGUI(player)
        }
    }

    var gui = RyseInventory
        .builder()
        .title(cmp("ElytraFly >> ", cBase) + cmp("Zones", cAccent))
        .rows(4)
        .provider(object : InventoryProvider {
            override fun init(player: Player, contents: InventoryContents) {
                contents.fillBorders(itemStack(Material.BLACK_STAINED_GLASS_PANE) { meta { displayName(cmp("")) } })


                val pagination: Pagination = contents.pagination()

                pagination.iterator(
                    SlotIterator.builder()
                    .startPosition(1, 1)
                    .endPosition(2, 7)
                    .type(SlotIterator.SlotIteratorType.HORIZONTAL)
                    .build())


                val headLeft = itemStack(Material.PLAYER_HEAD) {
                    amount = if (pagination.isFirst) 1 else pagination.page() -1
                    meta<SkullMeta> {
                        skullTexture(MXHeads.ARROW_LEFT_WHITE)
                        displayName(if (pagination.isFirst) cmp("«", strikethrough = true, bold = true, color = cBase) else cmp("«", strikethrough = false, bold = true, color = cBase))

                    }
                }

                contents.set(3, 3, IntelligentItem.of(headLeft) {
                    if (pagination.isFirst) return@of
                    pagination.inventory().open(player, pagination.previous().page())
                })


                val headRight = itemStack(Material.PLAYER_HEAD) {
                    amount = if (pagination.isLast) 1 else pagination.page()
                    meta<SkullMeta> {
                        skullTexture(MXHeads.ARROW_RIGHT_WHITE)
                        displayName(if (pagination.isLast) cmp("»", strikethrough = true, bold = true, color = cBase) else cmp("»", strikethrough = false, bold = true, color = cBase))
                    }

                }


                contents.set(3, 5, IntelligentItem.of(headRight) {
                    if (pagination.isLast) return@of
                    pagination.inventory().open(player, pagination.next().page())
                })

                zones.forEach {
                    pagination.addItem(zoneButton(it))
                }

                // Create new Zone
                contents.set(35, createButton())

                // Settings
                contents.set(27, settingsButton())

            }
        }).build(elytrafly)

    init {
        gui.open(player)
    }
}