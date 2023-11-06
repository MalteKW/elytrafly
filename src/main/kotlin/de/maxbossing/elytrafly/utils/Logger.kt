package de.maxbossing.elytrafly.utils

import de.maxbossing.elytrafly.ElytraFly
import de.maxbossing.elytrafly.elytrafly

fun debug(vararg msg: String) {
    if (ElytraFly.config.debug)
        msg.forEach { elytrafly.logger.info("[DEBUG] $it") }
}