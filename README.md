# ElytraFly
> Simple One-time-use elytra Plugin offering multiple elytra zones 

> [!TIP]  
> [Get this Plugin on Modrint](https://modrinth.com/plugin/elytrafly/)

## Table of contents
* [Setup](#setup)
* [Zones](#zones)
  * [Creating Zones](#creating-zones)
  * [Deleting Zones](#deleting-zones)
* [Configuration](#configuration)
  * [Boost](#boost)
  * [Boost strength](#boost-strength)
  * [Max Boosts](#max-boosts)
  * [Boost delay](#boost-delay)
  * [Prefix](#prefix)
  * [Elytra Name](#elytra-name)
* [Luckperms Integration](#luckperms)
* [Boost Designs](#boost-designs)
  * [Colors](#colors)
* [Permissions](#permissions)

## Setup
Download the Plugin, put it in `plugins/` and restart the server

## Zones
> [!INFO]
> Zone define the areas in which Players get the elytras.

### Creating Zones
To create a Zone, open the GUI with `/elytrafly` and click on `New Zone`. Follow the Instructions in the Chat to create the Zone

![Video of a zone being added](https://raw.githubusercontent.com/maxbossing/ElytraFly/master/assets/zone_creation.webp)

### Deleting Zones
To delete a Zone, simply right-click the Zone in the Zone GUI, opened with `/elytrafly`, to delete it.

> [!CAUTION]  
> Deleted zones are not recoverable!

![Video of a zone being deleted](https://raw.githubusercontent.com/maxbossing/ElytraFly/master/assets/zone_delete.webp)

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

## Luckperms
Elytrafly offers first-class Support for Luckperms to fine-tune player-specific settings like max boosts, delays, etc...

This is done using so-called [Meta Variables](https://luckperms.net/wiki/Prefixes,-Suffixes-&-Meta). refer to the [wiki](https://luckperms.net/wiki/Prefixes,-Suffixes-&-Meta) for a guide how to use them

| Variable name           | Description                                               |
|-------------------------|-----------------------------------------------------------|
| `elytrafly.max-boosts`  | How many boosts a Player/Group can use per Flight         |
| `elytrafly.boost.delay` | How long the delay between boosts is for the Player/Group |

## Boost Designs
ElytraFly offers an extensive Design builder to customize the boosts to your liking. The Builder offers functionality to change colors, add fades, flickers and trails to your boosts.

![Video showing the Design builder](https://raw.githubusercontent.com/maxbossing/ElytraFly/master/assets/design_overview.webp)

### Colors
You can easily define the colors of your elytra just by clicking items!

![Video showing how easily colors are changed](https://raw.githubusercontent.com/maxbossing/ElytraFly/master/assets/color_builder.webp)

## Permissions
| Permission               | Description                                                                 |
|--------------------------|-----------------------------------------------------------------------------|
| `elytrafly.gui`          | Allows to access the GUI and other admin functionality                      |
| `elytrafly.boost`        | Allows Players to boost themselver in mid-air using their off-hand swap key |
| `elytrafly.boost.bypass` | Allows Players to bypass the boost limit                                    |
| `elytrafly.delay.bypass` | Allows Players to bypass the delay for boosts                               |

## License
This Repository is licensed under `GPL v3`