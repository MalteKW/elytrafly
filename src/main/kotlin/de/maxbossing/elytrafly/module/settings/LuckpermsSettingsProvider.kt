package de.maxbossing.elytrafly.module.settings

import de.maxbossing.elytrafly.data.Permissions
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import net.luckperms.api.LuckPermsProvider
import org.bukkit.entity.Player

object LuckpermsSettingsProvider: SettingsProvider {

    private var API = LuckPermsProvider.get()

    override fun allowBoosts(player: Player): Boolean = VanillaSettingsProvider.allowBoosts(player)


    override fun boostDelay(player: Player): Int {
        if (player.hasPermission(Permissions.BOOST_MAX_BYPASS)) return -1

        val metavalue = API.getPlayerAdapter(Player::class.java).getMetaData(player).getMetaValue("elytrafly.boost-delay")

        return if (metavalue == null)
            VanillaSettingsProvider.boostDelay(player)
        else
            metavalue.toIntOrNull()?:VanillaSettingsProvider.boostDelay(player)
    }

    override fun maxBoosts(player: Player): Int {
        if (player.hasPermission(Permissions.BOOST_MAX_BYPASS)) return -1

        val metavalue = API.getPlayerAdapter(Player::class.java).getMetaData(player).getMetaValue("elytrafly.max-boosts")

        return if (metavalue == null)
            VanillaSettingsProvider.maxBoosts(player)
        else
            metavalue.toIntOrNull()?:VanillaSettingsProvider.maxBoosts(player)

    }
}