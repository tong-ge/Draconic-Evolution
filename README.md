Draconic-Evolution- Nomi-CEu Fork
==================

Draconic Evolution adds powerful new tools, Items, weapons and Armor. The premise is the enigmatic ore - Draconium. As you progress you find it has many uses from the mundane such as machines and items, to the near-mystical at the top with Awakened Draconium to make extremely powerful armor and tools.

This is made for nomi-ceu, and requires GT as a dependency. However, custom GT blocks, with names "draconium" and "awakened_draconium", are required for this to work properly.

## Changes from DE 1.12.2:

You can now use GT blocks, and Draconic Evolution Blocks, in the Energy Storage Core, and DE Reactor!

There's also a destruct button on the energy core! It will appear instead of the build guide button, when the core is valid. It destroys instantly (unlike the builder)! 

Useless Chaotic Tier upgrade key fusion recipes, with no catalyst or output, have been removed.

Note: if players de-activate and then reactivate their old core twice, the Draconic Evolution blocks will turn into their GT counterparts. Also, DE Awakened Draconium put into the reactor automatically transforms into GT Awakened Draconium.

Main Internal Change:
Moves EnergyCoreStructure from String to IBlockState[], which also adds a fallback block. Also, InvisECoreBlock now stores metadata.

## License / Use in Modpacks
This mod is licensed under the [**Don't Be a Jerk License**](https://github.com/brandon3055/Draconic-Evolution/blob/master/LICENSE) created by CoFH.
I herby grant permission to use this mod in any mod pack without the need to request permission from myself the owner (brandon3055) and the forker (IntegerLimit).

### TODO:
* Make config for builder and destroy. Config controls if it will be instant or at a speed of 1 block/t.
* Destroy Core sound is defeaning at t8. Make it one sound if instant, same with builder when that instant is implemented.
* Maybe later, chaotic tier upgrades for tools and armor will be implemented.

## Credits:

brandon3055 has done 99% of the work here. 

[TechLord22](https://github.com/TechLord22) for the huge help in making these changes.

Thank you!
