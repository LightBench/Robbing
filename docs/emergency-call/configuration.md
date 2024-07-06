# Configuration

Open your plugin folder, and nextly Robber folder. Here you can find the file `config.yml`, open it with a texteditor (like notepad).

## Config Example

Inside `config.yml` search the section `lockpick`, it will looks like this:

```yaml
# When a player uses /911, every other player with robbing.emergency.alert will receive a notification.
emergencycall:
  # Whether the emergency call feature should be enabled or not.
  enabled: false
  # Cooldown for /911 (in seconds), set to 0 to disable.
  cooldown: 10
```

Description of all handcuffing settings in the config.

## enabled

**Type:** `boolean`

**Default:** `false`

**Usage:**

If `true` the emergency call feature will be enabled.

## cooldown

**Type:** `int`

**Default:** `10`

**Usage:**

Cooldown to use /911 (in seconds), set to 0 to disable.
