# ElytraFly
> Simple One-time-use elytra Plugin offering multiple elytra zones 

## Setup
Download the Plugin, put it in `plugins/` and restart the server

## Define Zones
Zone define the areas in which Players get the elytras. To create a Zone, open the GUI with `/elytrafly` and click on `New Zone`. Follow the Instructions in the Chat to create the Zone

## Delete Zones
To delete a Zone, simply rightclick the Zone in the Zone GUI, opened with `/elytrafly`, to delete it.

## Configuration
> **Note:** Do not edit the config while the plugin is running, it will simply overwrite it!

The Configuration is saved in `plugins/elytrafly/config.json`

`debug` - Only for development purposes.

`prefix` - The Message prefix of the Plugin, supports MiniMessage.

`zones` - A list of serialized Zones (**Do NOT touch this, this is serialized in bytes!**)


### `elytraConfig`
`name` - The display name of the Elytra players recieve, supports MiniMessage.

`boost` - If activated, players with the `elytrafly.boost` permission can boost themselves in mid-air with `F`.

`boostStrength` - How Strong the boost is.

`boostDelay` - How many Seconds between usage of the boost

### `boostConfig`
`flicker` - If true, the trail of the boost will flicker

`trail` - If true, the Boost will leave a trail

`colors` - A list of RGB Values packed as Ints ([RGB Converter](https://www.shodor.org/~efarrow/trunk/html/rgbint.html)), that will color the explosion of the boost

`fades` - A list of RGB Values packed as Ints ([RGB Converter](https://www.shodor.org/~efarrow/trunk/html/rgbint.html)), the colors in which the explosion of the boost will fade




## To-Do
* [ ] GUI builder for the Boost Design
* [ ] Settings for Boost, Boost Strength

## License
This Repository is licensed under `GPL v3`