package de.maxbossing.elytrafly.gui.design

import de.maxbossing.elytrafly.main.cAccent
import de.maxbossing.elytrafly.main.cBase
import de.maxbossing.elytrafly.main.elytrafly
import de.maxbossing.mxpaper.MXColors
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.extensions.bukkit.plus
import de.maxbossing.mxpaper.extensions.fancy
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

class ColorInput(
    val player: Player,
    val inv: RyseInventory,
    var currentColor: Color = Color.WHITE,
    val callback: (Color) -> Unit
) {


    fun chestplate(): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.LEATHER_CHESTPLATE) {
                meta<LeatherArmorMeta> {

                    flag(ItemFlag.HIDE_DYE)
                    flag(ItemFlag.HIDE_ITEM_SPECIFICS)
                    flag(ItemFlag.HIDE_ATTRIBUTES)

                    displayName(cmp("Click to finish", MXColors.GREEN))

                    setLore {
                        lorelist += cmp("")
                        lorelist += cmp("Right-click - Reset Color", cBase)
                    }

                    this.setColor(currentColor)
                }
            }
        ) {
            if (it.isRightClick) {
                currentColor = Color.WHITE

                it.currentItem!!.meta<LeatherArmorMeta> {
                    this.setColor(currentColor)
                }

                val slots = listOf(10, 11, 12, 14, 15, 16)

                for (i in slots) {
                    it.inventory.getItem(i)!!.meta {
                        when (i) {
                            10, 11 -> displayName(
                                cmp(
                                    "${RGBColor.RED.name.fancy} : ",
                                    cBase
                                ) + cmp(currentColor.red.toString(), cAccent)
                            )

                            12, 14 -> displayName(
                                cmp(
                                    "${RGBColor.GREEN.name.fancy} : ",
                                    cBase
                                ) + cmp(currentColor.green.toString(), cAccent)
                            )

                            15, 16 -> displayName(
                                cmp(
                                    "${RGBColor.BLUE.name.fancy} : ",
                                    cBase
                                ) + cmp(currentColor.blue.toString(), cAccent)
                            )
                        }
                        setLore {
                            lorelist += (cmp("Right-Click • ", cBase) + cmp(" +1"))
                            lorelist += (cmp("Shift-Right-Click • ", cBase) + cmp("+10"))
                            lorelist += (cmp(
                                "                                  ",
                                underlined = true,
                                color = cBase,
                                bold = true
                            ))
                            lorelist += cmp("")
                            lorelist += (cmp("Left-Click • ", cBase) + cmp(" -1"))
                            lorelist += (cmp("Shift-Left-Click • ", cBase) + cmp("-10"))
                        }
                    }
                }

            } else {
                inv.open(player)
                callback(currentColor)
            }

        }
    }

    enum class RGBColor(val material: Material) {
        RED(Material.RED_CONCRETE),
        GREEN(Material.LIME_CONCRETE),
        BLUE(Material.BLUE_CONCRETE)
    }

    fun colorItem(color: RGBColor): IntelligentItem {
        return IntelligentItem.of(
            itemStack(color.material) {
                meta {
                    displayName(
                        cmp(
                            "${color.name.fancy} : ",
                            cAccent
                        ) + cmp(
                            (if (color == RGBColor.BLUE) currentColor.blue else if (color == RGBColor.GREEN) currentColor.green else currentColor.red).toString(),
                            cAccent
                        )
                    )

                    setLore {
                        lorelist += (cmp("Right-Click • ", cBase) + cmp(" +1"))
                        lorelist += (cmp("Shift-Right-Click • ", cBase) + cmp("+10"))
                        lorelist += (cmp(
                            "                                  ",
                            underlined = true,
                            color = cBase,
                            bold = true
                        ))
                        lorelist += cmp("")
                        lorelist += (cmp("Left-Click • ", cBase) + cmp(" -1"))
                        lorelist += (cmp("Shift-Left-Click • ", cBase) + cmp("-10"))
                    }
                }
            }
        ) {
            fun much(): Int {
                return if (it.isLeftClick) {
                    if (it.isShiftClick)
                        -10
                    else
                        -1
                } else {
                    if (it.isShiftClick)
                        10
                    else
                        1
                }
            }

            when (color) {
                RGBColor.BLUE -> {
                    if (currentColor.blue + much() > 255 || currentColor.blue + much() < 0) return@of
                    currentColor = currentColor.setBlue(currentColor.blue + much())

                }

                RGBColor.RED -> {
                    if (currentColor.red + much() > 255 || currentColor.red + much() < 0) return@of
                    currentColor = currentColor.setRed(currentColor.red + much())

                }

                RGBColor.GREEN -> {
                    if (currentColor.green + much() > 255 || currentColor.green + much() < 0) return@of
                    currentColor = currentColor.setGreen(currentColor.green + much())
                }
            }


            it.inventory.getItem(13)!!.meta<LeatherArmorMeta> {
                setColor(currentColor)
            }

            val slots = when (color) {
                RGBColor.RED -> listOf(10, 11)
                RGBColor.GREEN -> listOf(12, 14)
                RGBColor.BLUE -> listOf(15, 16)

            }

            for (i in slots) {
                it.inventory.getItem(i)!!.meta {
                    displayName(
                        cmp(
                            "${color.name.fancy} : ",
                            cBase
                        ) + cmp(
                            (if (color == RGBColor.BLUE) currentColor.blue else if (color == RGBColor.GREEN) currentColor.green else currentColor.red).toString(),
                            cAccent
                        )
                    )
                }
            }
        }
    }

    val gui = RyseInventory.builder()
        .rows(3)
        .title(cmp("Color Selector", cAccent))
        .provider(object : InventoryProvider {
            override fun init(player: Player, contents: InventoryContents) {
                contents.fill(itemStack(Material.GRAY_STAINED_GLASS_PANE) { meta { displayName(cmp("")) } })

                //  0  1  2  3  4  5  6  7  8
                //  9 10 11 12 13 14 15 16 17
                // 18 19 20 21 22 23 24 25 26

                contents.set(13, chestplate())

                contents.set(10, colorItem(RGBColor.RED))
                contents.set(11, colorItem(RGBColor.RED))
                contents.set(12, colorItem(RGBColor.GREEN))
                contents.set(14, colorItem(RGBColor.GREEN))
                contents.set(15, colorItem(RGBColor.BLUE))
                contents.set(16, colorItem(RGBColor.BLUE))
            }
        })
        .build(elytrafly)

    init {
        gui.open(player)
    }
}