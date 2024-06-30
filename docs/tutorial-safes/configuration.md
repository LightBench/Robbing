# Configuration

Open your plugin folder, and nextly Robber folder. Here you can find the file `config.yml`, open it with a texteditor (like notepad).

## Config Example

Inside `config.yml` search the section `lockpick`, it will looks like this:

<pre class="language-yaml"><code class="lang-yaml">safe:
  only_owner_can_break: false
  # Set a max number of safes that a player can lock.
  limit-locked-safes:
    enabled: false
    max-safes: 5
<strong>lockpicking:
</strong>  # Whether the lock picking feature should be enabled or not.
  enabled: true
</code></pre>

Description of all handcuffing settings in the config.

## safes.only\_owner\_can\_break

**Type:** `boolean`

**Default:** `false`

**Usage:**

If `true` only who locked a safe can break it.

## safes.limit-locked-safes.enabled

**Type:** `boolean`

**Default:** `false`

**Usage:**

If `true` the amount of safes lockable by a Player will be limited.

## safes.limit-locked-safes.max-safes

**Type:** `int`

**Default:** `5`

**Usage:**

the amount of safes that a player can have locked at the same time.\
Note: the `safes.limit-locked-safes.enabled` must be `true` to use this feature.

## lockpicking.enabled

**Type:** `boolean`

**Default:** `true`

**Usage:**

If `true` the lockpicking feature will be enabled.
