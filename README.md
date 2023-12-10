# ElytraFly

> Simple One-time-use elytra Plugin offering multiple elytra zones

> [!TIP]  
> [Get this Plugin on Modrint](https://modrinth.com/plugin/elytrafly/)

## Table of contents

* [Setup](#setup)
* [Zones](#zones)
    * [Creating Zones](#creating-zones)
    * [Deleting Zones](#deleting-zones)
    * [Restricting Zones](#restricting-zones)
* [Configuration](#configuration)
* [Boost Designs](#boost-designs)
    * [Colors](#colors)
* [Permissions](#permissions)
* [Plugin Integrations](#integrations)
    * [Luckperms Integration](#luckperms)
    * [PlaceholderAPI Integration](#placeholderapi)
* [License](#license)

## Setup

Download the Plugin, put it in `plugins/` and restart the server

## Zones

> [!NOTE]
> Zone define the areas in which Players get the elytras.

### Creating Zones

To create a Zone, open the GUI with `/elytrafly` and click on `New Zone`. Follow the Instructions in the Chat to create
the Zone

![Video of a zone being added](https://raw.githubusercontent.com/maxbossing/ElytraFly/master/assets/zone_creation.webp)

### Deleting Zones

To delete a Zone, simply right-click the Zone in the Zone GUI, opened with `/elytrafly`, to delete it.

> [!CAUTION]  
> Deleted zones are not recoverable!

![Video of a zone being deleted](https://raw.githubusercontent.com/maxbossing/ElytraFly/master/assets/zone_delete.webp)

### Restricting zones

Zones can be restricted to specific players/permission groups

* Every Zone has a unique Permission attached to it specifying what players are allowed to use the zone
* The permission is constructed from `elytrafly.zone.` and the unique name of the zone, lowercase and with whitespaces
  replaced with `-`
    * Example: Zone `Test Zone` has the permission `elytrafly.zone.test-zone`

> [!NOTE]  
> ElytraFly only enforces the Permissions when the `restricted` flag of the zone is set to true  
> This can be done in the Zone GUI

## Configuration

> [!WARNING]  
> Do **not** edit the config while the server is running!
> The plugin will simply overwrite the config on shutdown!

Everything there is to configure about this Plugin is done in the GUI opened with `/elytrafly`

To open the Settings, click on the Workbench in the lower left corner of the GUI

![Video of the Settings Menu](https://raw.githubusercontent.com/maxbossing/ElytraFly/master/assets/settings_overview.webp)

| Setting        | Description                                                                                                            | 
|----------------|------------------------------------------------------------------------------------------------------------------------|
| Boost          | If boost is activated, Player with the `elytrafly.boost` permission can use the `F` key to boost themselves in mid-air |
| Boost Strength | Boost Strength defines how long the Boost will last. every level is approximately half a second of boost               |
| Max Boosts     | How many boosts a Player can use per flight. -1 for infinite, 0 for none                                               |
| Boost Delay    | How many seconds player have to wait until they can boost again                                                        |
| Prefix         | Changes the Prefix of the Plugin                                                                                       |

![Video showing the Name of the Elytra being changed](https://raw.githubusercontent.com/maxbossing/ElytraFly/master/assets/elytra_name_change.webp)

## Boost Designs

ElytraFly offers an extensive Design builder to customize the boosts to your liking. The Builder offers functionality to
change colors, add fades, flickers and trails to your boosts.

![Video showing the Design builder](https://raw.githubusercontent.com/maxbossing/ElytraFly/master/assets/design_overview.webp)

### Colors

You can easily define the colors of your elytra just by clicking items!

![Video showing how easily colors are changed](https://raw.githubusercontent.com/maxbossing/ElytraFly/master/assets/color_builder.webp)

## Permissions

| Permission                 | Description                                                                                                               |
|----------------------------|---------------------------------------------------------------------------------------------------------------------------|
| `elytrafly.gui`            | Allows to access the GUI and other admin functionality                                                                    |
| `elytrafly.boost`          | Allows Players to boost themselves in mid-air using their off-hand swap key                                               |
| `elytrafly.boost.bypass`   | Allows Players to bypass the boost limit                                                                                  |
| `elytrafly.delay.bypass`   | Allows Players to bypass the delay for boosts                                                                             |
| `elytrafly.zone.bypass`    | Allows Players to use **all** zones, even if they are restricted                                                          |
| `elytrafly.zone.ZONE_NAME` | Allows players to use a zone if it's restricted (The zones name is fully lowercase and Whitespaces are translated to `-`) |

## Integrations

Elytrafly offers first-class Support for multiple popular plugins, like [Luckperms](https://luckperms.net)
or [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI)

### Luckperms

You can use Luckperms [Meta Variables](https://luckperms.net/wiki/Prefixes,-Suffixes-&-Meta) to fine-tune specific
Settings of Elytrafly per player. Refer to the [wiki](https://luckperms.net/wiki/Prefixes,-Suffixes-&-Meta) for a guide
how to use them

| Variable name           | Description                                               |
|-------------------------|-----------------------------------------------------------|
| `elytrafly.max-boosts`  | How many boosts a Player/Group can use per Flight         |
| `elytrafly.boost-delay` | How long the delay between boosts is for the Player/Group |

### PlaceholderAPI

ElytraFly offers an expansion for [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI) to display
additional information to Players

> [!NOTE]  
> You do not need to worry about enabling the Placeholders, ElytraFly will register them automatically when
> PlaceholderAPI is being detected on the Server

| Placeholder    | Description                                                                           |
|----------------|---------------------------------------------------------------------------------------|
| `zones_count`  | The total of zones set on the server                                                  |
| `current_zone` | The name of the zone in which the Player is standing (null if not standing in a zone) |
| `allow_boosts` | If the Player is allowed to boost himself                                             |
| `max_boosts`   | The maximum boosts the Player is allowed to use                                       |
| `delay`        | The delay that is set for the Player between boosts                                   |

## License

This Repository is licensed under `GPL v3`