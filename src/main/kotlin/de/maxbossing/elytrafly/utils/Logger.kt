package de.maxbossing.elytrafly.utils

import de.maxbossing.elytrafly.main.ElytraFly
import de.maxbossing.elytrafly.main.elytrafly

fun debug(vararg msg: String) {
    if (ElytraFly.config.debug)
        msg.forEach { elytrafly.logger.info("[DEBUG] $it") }
}