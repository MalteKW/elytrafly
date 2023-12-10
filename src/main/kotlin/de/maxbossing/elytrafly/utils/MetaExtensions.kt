package de.maxbossing.elytrafly.utils

import com.destroystokyo.paper.profile.ProfileProperty
import org.bukkit.Bukkit
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

/** Sets the texture of a [SkullMeta] to the given [base64] String */
fun SkullMeta.skullTexture(base64: String, uuid: UUID = UUID.randomUUID()): SkullMeta {
    val profile = Bukkit.createProfile(uuid)
    profile.setProperty(ProfileProperty("textures", base64))
    playerProfile = profile
    return this
}