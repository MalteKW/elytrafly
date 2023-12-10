package de.maxbossing.elytrafly.data

object Permissions {
    private const val BASE = "elytrafly"

    const val BOOST = "$BASE.boost"
    const val BOOST_MAX_BYPASS = "$BOOST.bypass"

    const val DELAY_BYPASS = "$BASE.delay.bypass"

    const val GUI = "$BASE.gui"

    const val ZONE_BYPASS_ALL = "$BASE.zone.bypass"

    fun zoneBypass(name: String) = "$BASE.zone.bypass.${name.replace(" ", "-")}"

    fun zoneRestriction(name: String) = "$BASE.zone.${name.replace(" ", "-")}"
}