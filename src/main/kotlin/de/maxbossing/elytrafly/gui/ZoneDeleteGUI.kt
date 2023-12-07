package de.maxbossing.elytrafly.gui

import de.maxbossing.elytrafly.*
import de.maxbossing.elytrafly.data.Zone
import de.maxbossing.elytrafly.module.zones.ZoneManager
import de.maxbossing.elytrafly.utils.skullTexture
import de.maxbossing.mxpaper.MXColors
import de.maxbossing.mxpaper.MXHeads
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.extensions.bukkit.plus
import de.maxbossing.mxpaper.items.itemStack
import de.maxbossing.mxpaper.items.meta
import de.maxbossing.mxpaper.items.setLore
import de.maxbossing.mxpaper.main.prefix
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem
import io.github.rysefoxx.inventory.plugin.content.InventoryContents
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.SkullMeta
import kotlin.random.Random

class ZoneDeleteGUI(val player: Player, val zone: Zone) {
    var zones: List<Zone>
        get() = ElytraFly.config.zones
        set(value) {
            ElytraFly.config.zones = value
        }



    fun infoItem(): IntelligentItem {
        fun pseudoRandomMaterial(zone: Zone) = Material.entries.filter { it.name.endsWith("ARMOR_TRIM_SMITHING_TEMPLATE") }.random(Random(zone.hashCode()))
        fun xyzString(location: Location): String = "${location.x} ${location.y} ${location.z}"

        return IntelligentItem.of(
            itemStack(pseudoRandomMaterial(zone = zone)) {
                meta {
                    displayName(cmp(zone.name, cAccent))

                    addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS)

                    setLore {
                        lorelist += cmp("World: ", cBase) + cmp(zone.loc1.world.name, cAccent)
                        lorelist += cmp("loc 1: ", cBase) + cmp(xyzString(zone.loc1), cAccent)
                        lorelist += cmp("loc 2: ", cBase) + cmp(xyzString(zone.loc2), cAccent)
                    }
                }
            }
        ) {}
    }

    fun abortButton(): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.PLAYER_HEAD) {
                meta<SkullMeta> {
                    displayName(cmp("Abort", cBase))
                    skullTexture(MXHeads.X_RED)
                }
            }

        ) {
            ZoneGUI(player)
        }
    }

    fun confirmButton(): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.PLAYER_HEAD) {
                meta<SkullMeta> {
                    displayName(cmp("Confirm", cBase))
                    skullTexture(MXHeads.CHECKMARK_GREEN)
                }
            }
        ) {
            ZoneManager.delZone(zone.name)
            player.sendMessage(prefix + cmp("Zone deleted", cBase))
            ZoneGUI(player)
        }
    }


    val gui = RyseInventory.builder()
        .title(cmp("ElytraFly >> ", cBase) + cmp("Delete", MXColors.INDIANRED))
        .rows(3)
        .provider(object : InventoryProvider {
            override fun init(player: Player, contents: InventoryContents) {
                contents.fill(itemStack(Material.GRAY_STAINED_GLASS_PANE) { meta { displayName(cmp("")) } })

                //  0  1  2  3  4  5  6  7  8
                //  9 10 11 12 13 14 15 16 17
                // 18 19 20 21 22 23 24 25 26


                // Abort Button
                contents.set(11, abortButton())

                // Info Item
                contents.set(13, infoItem())

                // Confirm Button
                contents.set(15, confirmButton())
            }
        }).build(elytrafly)

    init {
        gui.open(player)
    }

}