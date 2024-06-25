# Configuration

Open your plugin folder, and nextly Robber folder. Here you can find the file `config.yml`, open it with a texteditor (like notepad).

## Config Example

Inside `config.yml` search the section `handcuffing`, it will looks like this:

```yaml
handcuffing:
  # Whether the handcuffing feature should be enabled or not.
  enabled: true
  # Set a cooldown before using handcuffs again (in seconds).
  cooldown: 5
  # Crafting of custom items related to the current feature.
  enable-crafting:
    handcuffs: true
    handcuffs_key: true
  # Make it possible to escape from handcuffing.
  # Handcuffed players have the "delay-handcuffing" time to escape from the handcuffer within the "distance" block.
  # to avoid being handcuffed.
  escape:
    enabled: false
    # Delay handcuffing action after clicking (in seconds).
    # This option gives the player the possibility to escape.
    delay-handcuffing: 2
    # Distance to reach from the handcuffer to escape handcuffing.
    distance: 3
  # Permitted commands while handcuffed.
  permitted-commands:
    - login
    - register
  # Enable right-clicking on a handcuffed player to make them follow the sender.
  kidnap:
    enabled: true
```

Description of all handcuffing settings in the config.

## enabled

**Type:** `boolean`

**Default:** `true`

**Usage:**

If `true` the handcuffs will be enabled in your server.

Set `false` to disable this mechanic.

## cooldown

**Type:** `integer`

**Default:** `5`

**Usage:**

Interval in **seconds** between one handcuffs and another, to disable it, set the parameter to 0.

## escape.enabled

**Type:** `boolean`

**Default:** `false`

**Usage:**

If `true` it make possible to escape from handcuffing. \
Handcuffed players have the "escape.delay-handcuffing" time to escape from the handcuffer within the "distance" block to avoid being handcuffed.

Set to `true` to enable handcuffed escape feature.

## escape.delay-handcuffing

**Type:** `int`

**Default:** `2`

**Usage:**

Delay handcuffing action after clicking (in seconds). \
This option gives the player the possibility to escape.

## escape.distance

**Type:** `int`

**Default:** 3

**Usage:**

Distance (in blocks) to reach from the handcuffer to escape handcuffing.

```
permitted-commands
```

**Type:** `list`

**Default:**

```yaml
    - login
    - register
```

**Usage:**

Permitted commands while handcuffed.

## kidnap.enabled

**Type:** `boolean`

**Default:** `true`

**Usage:**

If `true` right clicking on a handcuffed player will make he follow you.

Set `false` to disable handcuffed following.

