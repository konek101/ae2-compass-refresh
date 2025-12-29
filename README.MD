# AE2 Compass Refresh

A Forge 1.20.1 mixin mod that adds refresh behavior to the AE2 Meteorite Compass.

## Features

- **Shift + Right-click** with Meteorite Compass triggers a rescan of nearby meteorite data
- Server-side only operation
- Configurable scan radius (0-3 chunks)
- Configurable cooldown system
- Chat feedback messages
- Tooltip explaining the feature

## Configuration

The mod provides a server-side configuration file (`config/compassrefresh-server.toml`) with the following options:

- `scanRadius` (0-3, default: 1) - Chunk radius to scan around the player
- `cooldownTicks` (0-1200, default: 40) - Cooldown between uses in ticks (20 ticks = 1 second)
- `onlyLoadedChunks` (boolean, default: true) - Only scan loaded chunks to prevent chunk loading
- `showChatFeedback` (boolean, default: true) - Show chat feedback messages when rescanning

## Implementation Details

### Mixin Injection

The mod uses a mixin to inject into `appeng.items.misc.MeteoriteCompassItem`:
- Intercepts the `use()` method to detect shift + right-click
- Adds tooltip via `appendHoverText()` injection

### Rescan Logic

When triggered, the mod:
1. Checks if the player is in alternate use mode (shift) using `InteractionUtil.isInAlternateUseMode(player)`
2. Validates cooldown per player
3. Iterates through chunks in the configured radius
4. For each loaded chunk, calls `ServerCompassService.updateArea((ServerLevel) level, chunk)` to refresh meteorite data
5. Sends chat feedback to the player

## Building

This mod requires access to Maven repositories that may not be available in all environments. The build dependencies are:

- Minecraft 1.20.1
- Forge 47.2.0
- Applied Energistics 2 (15.2.1)
- SpongePowered Mixin

To build:
```bash
./gradlew build
```

The compiled mod JAR will be in `build/libs/`.

## Installation

1. Install Minecraft 1.20.1
2. Install Forge 47.2.0 or newer
3. Install Applied Energistics 2 (15.2.1 or newer)
4. Place the mod JAR in your `mods` folder

## Usage

1. Craft or obtain a Meteorite Compass from Applied Energistics 2
2. Hold the compass and **Shift + Right-click** in the air
3. The mod will rescan nearby chunks for meteorite data
4. You'll receive a chat message indicating how many chunks were scanned

## License

MIT License

