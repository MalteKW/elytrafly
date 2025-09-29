package de.maxbossing.elytrafly.gui.design

import de.maxbossing.elytrafly.main.ElytraFly
import de.maxbossing.elytrafly.main.cAccent
import de.maxbossing.elytrafly.main.cBase
import de.maxbossing.elytrafly.main.elytrafly
import de.maxbossing.elytrafly.gui.SettingsGUI
import de.maxbossing.elytrafly.module.zones.ZoneManager
import de.maxbossing.elytrafly.utils.skullTexture
import de.maxbossing.mxpaper.MXColors
import de.maxbossing.mxpaper.MXHeads
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.extensions.bukkit.plus
import de.maxbossing.mxpaper.items.flag
import de.maxbossing.mxpaper.items.itemStack
import de.maxbossing.mxpaper.items.meta
import de.maxbossing.mxpaper.items.setLore
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem
import io.github.rysefoxx.inventory.plugin.content.InventoryContents
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta

class BoostDesign(val player: Player) {

    var flicker = false
    var trail = false

    var colors: MutableList<Color> = mutableListOf<Color>()
    var fades: MutableList<Color> = mutableListOf<Color>()

    fun flickerButton(): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.NETHER_STAR) {
                meta {
                    displayName(cmp("Flicker", cBase))
                    setLore {
                        lorelist += if (flicker) cmp("> Active", cAccent, underlined = true) else cmp(
                            "Active",
                            cBase,
                            underlined = false
                        )
                        lorelist += if (!flicker) cmp("> Not Active", cAccent, underlined = true) else cmp(
                            "Not Active",
                            cBase,
                            underlined = false
                        )
                    }
                }
            }
        ) {
            flicker = !flicker

            it.currentItem!!.meta {
                setLore {
                    lorelist += if (flicker) cmp("> Active", cAccent, underlined = true) else cmp(
                        "Active",
                        cBase,
                        underlined = false
                    )
                    lorelist += if (!flicker) cmp("> Not Active", cAccent, underlined = true) else cmp(
                        "Not Active",
                        cBase,
                        underlined = false
                    )
                }
            }
        }
    }

    fun trailButton(): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.LEAD) {
                meta {
                    displayName(cmp("Trail", cBase))
                    setLore {
                        lorelist += if (trail) cmp("> Active", cAccent, underlined = true) else cmp(
                            "Active",
                            cBase,
                            underlined = false
                        )
                        lorelist += if (!trail) cmp("> Not Active", cAccent, underlined = true) else cmp(
                            "Not Active",
                            cBase,
                            underlined = false
                        )
                    }
                }
            }
        ) {
            trail = !trail

            it.currentItem!!.meta {
                setLore {
                    lorelist += if (trail) cmp("> Active", cAccent, underlined = true) else cmp(
                        "Active",
                        cBase,
                        underlined = false
                    )
                    lorelist += if (!trail) cmp("> Not Active", cAccent, underlined = true) else cmp(
                        "Not Active",
                        cBase,
                        underlined = false
                    )
                }
            }
        }
    }

    fun abortButton(): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.PLAYER_HEAD) {
                meta<SkullMeta> {
                    skullTexture(MXHeads.X_RED)
                    displayName(cmp("Abort Design", MXColors.RED))
                }
            }
        ) {
            SettingsGUI(player)
        }
    }

    fun saveButton(): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.PLAYER_HEAD) {
                meta<SkullMeta> {
                    skullTexture(MXHeads.CHECKMARK_GREEN)
                    displayName(cmp("Save Design", MXColors.GREEN))
                }
            }
        ) {
            ElytraFly.config.elytraConfig.boostConfig.boostDesign.apply {
                this.colors = this@BoostDesign.colors.map { it.asRGB() }
                this.fades = this@BoostDesign.fades.map { it.asRGB() }
                this.flicker = this@BoostDesign.flicker
                this.trail = this@BoostDesign.trail
            }
            ZoneManager.rebuildBoostFirework()
            SettingsGUI(player)
        }
    }

    fun colorButton(index: Int, fade: Boolean): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.LEATHER_CHESTPLATE) {
                meta<LeatherArmorMeta> {
                    displayName(cmp("${if (!fade) "Main" else "Fade"} Color ${index + 1}", cAccent))

                    flag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                    flag(ItemFlag.HIDE_ATTRIBUTES)
                    flag(ItemFlag.HIDE_DYE)

                    setLore {
                        if (!fade) {
                            lorelist += cmp("This is one of the main colors of the Boost Design", cBase)
                            lorelist += cmp("This will define the colors of the big explosion of the Firework", cBase)
                        } else {
                            lorelist += cmp("This is one of the fade colors of the Boost Design", cBase)
                            lorelist += cmp("This will define the colors the big explosion will fade into", cBase)
                        }

                        lorelist += cmp("", cBase)
                        lorelist += cmp("Click to change", cBase)
                    }
                    this.setColor(if (fade) fades[index] else colors[index])

                }
            }
        ) {
            ColorInput(player, gui, if (fade) fades[index] else colors[index]) { color ->
                if (fade) {
                    fades[index] = color
                } else {
                    colors[index] = color
                }
            }
        }
    }


    var gui = RyseInventory.builder()
        .title(cmp("ElytraFly > ", cBase) + cmp("Design", cAccent))
        .rows(5)
        .provider(object : InventoryProvider {
            override fun init(player: Player, contents: InventoryContents) {
                contents.fill(itemStack(Material.GRAY_STAINED_GLASS_PANE) { meta { displayName(cmp("")) } })
                contents.fillBorders(itemStack(Material.BLACK_STAINED_GLASS_PANE) { meta { displayName(cmp("")) } })

                //  0  1  2  3  4  5  6  7  8
                //  9 10 11 12 13 14 15 16 17
                // 18 19 20 21 22 23 24 25 26
                // 27 28 29 30 31 32 33 34 35
                // 36 37 38 39 40 41 42 43 44

                // 12 - Abort Design
                contents.set(12, abortButton())

                // 14 - Save Design
                contents.set(14, saveButton())

                // 30 - Trail
                contents.set(30, trailButton())

                // 32 - Flicker
                contents.set(32, flickerButton())

                // 10, 19, 28 - Colors
                contents.set(10, colorButton(0, false))
                contents.set(19, colorButton(1, false))
                contents.set(28, colorButton(2, false))

                // 16, 25, 34 - Fades
                contents.set(16, colorButton(0, true))
                contents.set(25, colorButton(1, true))
                contents.set(34, colorButton(2, true))

            }
        })


        .build(elytrafly)


    init {
        // Currently we only allow 3 colors/fades, because we'd have to reimagine the system to display and change them, as GUIs would'nt fit them nicely
        // So this just takes the first three designs from the config or adds needed ones to get to three
        // If you need more colors, directly edit the config

        val configcolors = ElytraFly.config.elytraConfig.boostConfig.boostDesign.colors.map { Color.fromRGB(it) }
        val configfades = ElytraFly.config.elytraConfig.boostConfig.boostDesign.fades.map { Color.fromRGB(it) }

        colors = if (configcolors.size < 3) {
            val needed = List<Color>(3 - configcolors.size) { Color.WHITE }
            (configcolors + needed).toMutableList()
        } else {
            configcolors.take(3).toMutableList()
        }

        fades = if (configfades.size < 3) {
            val needed = List<Color>(3 - configfades.size) { Color.WHITE }
            (configfades + needed).toMutableList()
        } else {
            configfades.take(3).toMutableList()
        }


        gui.open(player)
    }
}