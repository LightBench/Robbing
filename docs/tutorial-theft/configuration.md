# Configuration

Open your plugin folder, and nextly Robber folder. Here you can find the file `config.yml`, open it with a texteditor (like notepad).

## Config Example

Inside `config.yml` search the section `robbing`, it will looks like this:

```yaml
rob:
  # Whether the rob feature should be enabled or not.
  enabled: true
  # Enable alert to send to the robbed target.
  alert: true
  # Whitelist and blacklist notes:
  # blacklist or/and the whitelist lists can be filled with the item name like in the
  # suggested examples, you can find easily item names online, suggested website: https://minecraftitemids.com/.
  # If you want to add a robbing custom item just add it as "robbing:" and the
  # custom item name, for example:
  #   items:
  #     - "robbing:handcuffs"
  #     - "robbing:safe"

  # Blacklist, if enabled all the items in the list will not be robbable.
  blacklist:
    enabled: false
    items:
      - diamond
      - diamond_block
  # Whitelist, if enabled only the items in the list will be robbable.
  whitelist:
    enabled: false
    items:
      - gold_ingot
  # Max distance in blocks during robbing between the robber and the robbed.
  # Beyond this distance, the robbed player's inventory will be closed.
  max-distance: 5
  # Cooldown setting (set timeout to 0 to disable).
  cooldown: 120
  # If enabled, sneak right-click to rob, otherwise just right-click.
  sneak-to-rob: true
  # After a robbery, the target will be blinded.
  # This option simulates a physical aggression to enhance realism.
  blindness-after-robbing:
    enabled: false
    # Blindness duration in seconds.
    duration: 5
  # Whether players can rob NPCs or not.
  NPC-rob: false
  # Catching a robber while robbing.
  # If enabled, this feature will slow down the robber if the robbed player shift left-clicks on them.
  catch-robber:
    enabled: true
    # Deny jumping after being caught.
    can-jump: false
    # Slow effect power after being caught.
    slow-power: 5
    # Duration of the caught effects (in seconds).
    duration: 20
```

Description of all Robbing settings in the config.

## enabled

**Type:** `boolean`

**Default:** `true`

**Usage:**

If `true` the robbing will be enabled in your server.

Set `false` to disable this mechanic.

## alert

**Type:** `boolean`

**Default:** `true`

**Usage:**

If `true` the target of the theft will receive an alert.

Set `false` to disable theft alerts.

## blacklist.enabled

**Type:** `boolean`

**Default:** `false`

**Usage:**

If `true` the blacklist will e enabled.

## blacklist.items

**Type:** `list`

**Default:**

```yaml
    - diamond
    - diamond_block
```

**Usage:**

Add the items you want to make immune to theft in this list.

## whitelist.enabled

**Type:** `boolean`

**Default:** `false`

**Usage:**

If `true` the whitelist will e enabled.

## whitelist.items

**Type:** `list`

**Default:**

```yaml
    - gold_ingot
```

**Usage:**

Add the items you want to make immune to theft in this list.

## max\_distance

**Type:** `integer`

**Default:** `5`

**Usage:**

Distance beyond which the attempted theft, once this distance is exceeded, the victim's inventory will be closed to the thief.

## cooldown

**Type:** `integer`

**Default:** `120`

**Usage:**

Interval in **seconds** between one theft and another, to disable it, set the parameter to 0.

## sneak-to-rob

**Type:** `boolean`

**Default:** `true`

**Usage:**

If `true` the thief have to shift + right click to start a rob, if `false` just right click will be enough.

## blindness-after-robbing.enabled

**Type:** `boolean`

**Default:** `false`

**Usage:**

If `true`, the victim of a theft will receive temporary blindness.

## blindness-after-robbing.duration

**Type:** `integer`

**Default:** `5`

**Usage:** to operate it is necessary to have blindness\_after\_robbing enabled.

Interval between one theft and another, to disable it, set the parameter to 0.

### catch-robber.enabled

**Type:** `boolean`

**Default:** `true`

**Usage:**

If `true` the target of a rob will be able to catch his robber.

Set `false` to disable this mechanic.

### catch-robber.can-jump

**Type:** `boolean`

**Default:** `false`

**Usage:** to operate it is necessary to have catch-robber enabled.

If `true` jump will be denied to the caught robber.

Set `false` to disable this mechanic.

### catch-robber.slow-power

**Type:** `integer`

**Default:** `5`

**Usage:**  to operate it is necessary to have catch-robber enabled.

Power of slowness to apply to caught robber.

### catch-robber.duration

**Type:** `integer`

**Default:** `20`

**Usage:**  to operate it is necessary to have catch-robber enabled.

Sets how long the slowness on the caught thief should last
