---
description: Rob feature
---

# Rob

## Functionalities

Explanation of basics Robbing functionalities

### Rob

In robbing it is possible to rob other players by simply approaching their `shift + right clicking` on these. Their inventory will be opened to you and you can take their items. Beware, the poor victims will be `notified` that you are trying to rob them.

Is possible to give temporary blindness to a robbed player, check [blindness-after-robbing](configuration.md#blindness-after-robbing.enabled).

To set a cooldown between one theft and another read here [rob cooldown](configuration.md#cooldown).

![Image](https://media2.giphy.com/media/mjXEVL9BXqlK3BEMlT/giphy.gif?cid=790b761187d952c2ab9a6366c959592eec0f84bab051561a\&rid=giphy.gif)

### Escaping rob

There are basically two ways to escape an ongoing robbing, escape or catch the thief.

#### Escape

The robber will have to be stopped to rob you, to escape it will be enough to move away from him, for a configurable distance. Check [max distance](configuration.md#max\_distance) configuration for more info.

#### Catching

If you are reckless and do not want to escape in front of the danger, you will have the opportunity to catch the thief. To do this it will be sufficient to `shift + left click` on the thief to apply hard slow (configurable by default is slowness 5), and prohibition to jump. Check the [catching configuration](configuration.md#catch-robber.enabled).

### Limit Thefts

It is possible to set a whitelist and/or a blacklist the config file to deny/allow the rob of certain items from the config.

* [whitelist](configuration.md#whitelist.enabled)
* [blacklist](configuration.md#blacklist.enabled)
