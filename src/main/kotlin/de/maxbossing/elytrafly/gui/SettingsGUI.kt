package de.maxbossing.elytrafly.gui

import de.maxbossing.elytrafly.main.ElytraFly
import de.maxbossing.elytrafly.main.cAccent
import de.maxbossing.elytrafly.main.cBase
import de.maxbossing.elytrafly.main.elytrafly
import de.maxbossing.elytrafly.gui.design.BoostDesign
import de.maxbossing.elytrafly.utils.skullTexture
import de.maxbossing.mxpaper.MXHeads
import de.maxbossing.mxpaper.await.awaitChatInput
import de.maxbossing.mxpaper.extensions.bukkit.cmp
import de.maxbossing.mxpaper.extensions.bukkit.plus
import de.maxbossing.mxpaper.extensions.deserialized
import de.maxbossing.mxpaper.items.itemStack
import de.maxbossing.mxpaper.items.meta
import de.maxbossing.mxpaper.items.setLore
import de.maxbossing.mxpaper.main.cError
import de.maxbossing.mxpaper.main.prefix
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem
import io.github.rysefoxx.inventory.plugin.content.InventoryContents
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.SkullMeta
import kotlin.time.Duration.Companion.minutes

class SettingsGUI(val player: Player) {

    var config
        get() = ElytraFly.config
        set(value) {
            ElytraFly.config = value
        }

    fun backButton(): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.PLAYER_HEAD) {
                meta<SkullMeta> {
                    displayName(cmp("Back", cBase))
                    skullTexture(MXHeads.BACKWARDS_WHITE)
                }
            }

        ) {
            ZoneGUI(player)
        }
    }

    fun boostItem(): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.FIREWORK_ROCKET) {
                meta {
                    displayName(cmp("Boost"))
                    setLore {
                        lorelist += if (config.elytraConfig.boostConfig.boost) cmp(
                            "Active",
                            cAccent,
                            underlined = true
                        ) else cmp("Active", cBase)
                        lorelist += if (!config.elytraConfig.boostConfig.boost) cmp(
                            "Not Active",
                            cAccent,
                            underlined = true
                        ) else cmp("Not Active", cBase)
                    }
                }
            }

        ) {
            config.elytraConfig.boostConfig.boost = !config.elytraConfig.boostConfig.boost
            it.currentItem!!.meta {
                setLore {
                    lorelist += if (config.elytraConfig.boostConfig.boost) cmp(
                        "Active",
                        cAccent,
                        underlined = true
                    ) else cmp("Active", cBase)
                    lorelist += if (!config.elytraConfig.boostConfig.boost) cmp(
                        "Not Active",
                        cAccent,
                        underlined = true
                    ) else cmp("Not Active", cBase)
                }
            }
        }
    }

    fun boostStrengthItem(): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.FIREWORK_STAR) {
                meta {
                    displayName(cmp("Boost Strength"))
                    setLore {
                        lorelist += cmp(
                            "Current: ",
                            cBase
                        ) + cmp(config.elytraConfig.boostConfig.boostStrength.toString(), cAccent)
                        lorelist += cmp(" ")
                        lorelist += cmp("Right-Click • ", cBase) + cmp(" +1", cAccent)
                        lorelist += cmp("Left-Click • ", cBase) + cmp("-1", cAccent)
                    }
                }
            }
        ) {
            if (it.isLeftClick) {
                if (config.elytraConfig.boostConfig.boostStrength <= 1) return@of
                config.elytraConfig.boostConfig.boostStrength -= 1
            } else {
                config.elytraConfig.boostConfig.boostStrength += 1
            }
            it.currentItem!!.meta {
                setLore {
                    lorelist += cmp("Current: ", cBase) + cmp(
                        config.elytraConfig.boostConfig.boostStrength.toString(),
                        cAccent
                    )
                    lorelist += cmp(" ")
                    lorelist += cmp("Right-Click • ", cBase) + cmp(" +1", cAccent)
                    lorelist += cmp("Left-Click • ", cBase) + cmp("-1", cAccent)
                }
            }
        }
    }

    fun boostDelayItem(): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.CLOCK) {
                meta {
                    displayName(cmp("Boost Delay"))
                    setLore {
                        lorelist += cmp("Current: ", cBase) + cmp(
                            config.elytraConfig.boostConfig.boostDelay.toString(),
                            cAccent
                        )
                        lorelist += cmp(" ")
                        lorelist += cmp("Right-Click • ", cBase) + cmp(" +1", cAccent)
                        lorelist += cmp("Left-Click • ", cBase) + cmp("-1", cAccent)
                    }
                }
            }
        ) {
            if (it.isLeftClick) {
                if (config.elytraConfig.boostConfig.boostDelay <= 0) return@of
                config.elytraConfig.boostConfig.boostDelay -= 1
            } else {
                config.elytraConfig.boostConfig.boostDelay += 1
            }
            it.currentItem!!.meta {
                setLore {
                    lorelist += cmp("Current: ", cBase) + cmp(
                        config.elytraConfig.boostConfig.boostDelay.toString(),
                        cAccent
                    )
                    lorelist += cmp(" ")
                    lorelist += cmp("Right-Click • ", cBase) + cmp(" +1", cAccent)
                    lorelist += cmp("Left-Click • ", cBase) + cmp("-1", cAccent)
                }
            }
        }
    }

    fun boostDesignItem(): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.RAW_GOLD) {
                meta {
                    displayName(cmp("Boost Design"))
                    setLore {
                        lorelist += cmp("Click to open the design builder")
                    }
                }
            }
        ) {
            BoostDesign(player)
        }
    }

    fun prefixButton(): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.PAPER) {
                meta {
                    displayName(cmp("Prefix", cBase))

                    setLore {
                        lorelist += cmp("Click to set the Plugin prefix")
                        lorelist += cmp("Supports MiniMessage", cBase)
                        lorelist += cmp("Current Prefix: ", cBase) + prefix
                    }
                }
            }
        ) {
            it.whoClicked.closeInventory()
            (it.whoClicked as Player).awaitChatInput(
                initMessage = prefix + cmp("Input the new Prefix into the chat!"),
                maxTime = 2.minutes,
                onChat = { result ->
                    prefix = result.plus(" ").deserialized
                    it.whoClicked.sendMessage(prefix + cmp("Prefix set!", cBase))
                    it.currentItem!!.meta {
                        setLore {
                            lorelist += cmp("Click to set the Plugin prefix")
                            lorelist += cmp("Supports MiniMessage", cBase)
                            lorelist += cmp("Current Prefix: ", cBase) + prefix
                        }
                    }
                },
                onTimeout = {
                    it.whoClicked.sendMessage(prefix + cmp("You didn't specify a Prefix!", cError))
                },
                callback = { gui.open(it.whoClicked as Player)}
            )
        }
    }

    fun elytraNameDesign(): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.PAPER) {
                meta {
                    displayName(cmp("Elytra Name", cBase))

                    setLore {
                        lorelist += cmp("Click to set the Elytra Name")
                        lorelist += cmp("Supports MiniMessage", cBase)
                        lorelist += cmp("Current Name: ", cBase) + ElytraFly.config.elytraConfig.name.deserialized
                    }
                }
            }
        ) {
            it.whoClicked.closeInventory()
            (it.whoClicked as Player).awaitChatInput(
                initMessage = prefix + cmp("Input the elytra name into the chat!"),
                maxTime = 2.minutes,
                onChat = { result ->
                    ElytraFly.config.elytraConfig.name = result
                    it.whoClicked.sendMessage(prefix + cmp("Elytra Name set!", cBase))
                    it.currentItem!!.meta {
                        setLore {
                            lorelist += cmp("Click to set the Elytra Name")
                            lorelist += cmp("Supports MiniMessage", cBase)
                            lorelist += cmp("Current Name: ", cBase) + ElytraFly.config.elytraConfig.name.deserialized
                        }
                    }
                },
                onTimeout = {
                    it.whoClicked.sendMessage(prefix + cmp("You didn't specify an elytra name!", cError))
                },
                callback = {
                    gui.open(it.whoClicked as Player)
                }
            )
        }
    }

    fun maxUsesButton(): IntelligentItem {
        return IntelligentItem.of(
            itemStack(Material.GOLDEN_APPLE) {
                meta {
                    displayName(cmp("Max Boosts per flight", cBase))

                    setLore {
                        lorelist += cmp(
                            "Current: ",
                            cBase
                        ) + cmp(
                            if (config.elytraConfig.boostConfig.maxBoosts == -1) "Infinite" else config.elytraConfig.boostConfig.maxBoosts.toString(),
                            cAccent
                        )
                        lorelist += cmp(" ")
                        lorelist += cmp("Right-Click • ", cBase) + cmp(" +1", cAccent)
                        lorelist += cmp("Left-Click • ", cBase) + cmp("-1", cAccent)
                    }
                }
            }
        ) {
            if (it.isLeftClick) {
                if (config.elytraConfig.boostConfig.maxBoosts == -1) return@of
                config.elytraConfig.boostConfig.maxBoosts -= 1
            } else {
                config.elytraConfig.boostConfig.maxBoosts += 1
            }

            it.currentItem!!.meta {
                setLore {
                    lorelist += cmp(
                        "Current: ",
                        cBase
                    ) + cmp(
                        if (config.elytraConfig.boostConfig.maxBoosts == -1) "Infinite" else config.elytraConfig.boostConfig.maxBoosts.toString(),
                        cAccent
                    )
                    lorelist += cmp(" ")
                    lorelist += cmp("Right-Click • ", cBase) + cmp(" +1", cAccent)
                    lorelist += cmp("Left-Click • ", cBase) + cmp("-1", cAccent)
                }
            }
        }
    }

    private val gui = RyseInventory.builder()
        .title(cmp("ElytraFly >>", cBase) + cmp(" Settings", cAccent))
        .rows(4)
        .provider(object : InventoryProvider {
            override fun init(player: Player, contents: InventoryContents) {
                contents.fill(itemStack(Material.GRAY_STAINED_GLASS_PANE) { meta { displayName(cmp("")) } })
                contents.fillBorders(itemStack(Material.BLACK_STAINED_GLASS_PANE) { meta { displayName(cmp("")) } })

                //  0  1  2  3  4  5  6  7  8
                //  9 10 11 12 13 14 15 16 17
                // 18 19 20 21 22 23 24 25 26
                // 27 28 29 30 31 32 33 34 35


                // 0 - Back
                contents.set(0, backButton())

                // 10 - Boost
                contents.set(10, boostItem())

                // 12 - Boost Design
                contents.set(12, boostDesignItem())

                // 21 - Elytra Name
                contents.set(21, elytraNameDesign())

                // 19 - Boost Strength
                contents.set(19, boostStrengthItem())

                // 14 - Boost delay
                contents.set(14, boostDelayItem())

                // 23 - Prefix Setting
                contents.set(23, prefixButton())

                // 16 - Max Boosts Settings
                contents.set(16, maxUsesButton())

            }
        })
        .build(elytrafly)

    init {
        gui.open(player)
    }
}