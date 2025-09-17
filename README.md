# WorldOptimiser

**Automatic World Optimization for ReplayMod Compatibility**

A Minecraft Forge mod that automatically optimizes worlds when mod changes are detected, specifically designed to solve ReplayMod block ID mismatch issues.

## 🎯 Purpose

This mod was created to solve a critical issue with [ReforgedPlay Mod](https://www.curseforge.com/minecraft/mc-mods/reforgedplay-mod) (version 0.3.1) where adding or removing mods that contain blocks, models, or textures causes ReplayMod to display incorrect textures and models in replay recordings.

### The Problem
- **ReplayMod works perfectly** when a world is generated with a fixed set of mods
- **Issues arise** when you add or remove mods containing blocks, models, or textures
- This changes the reference ID list, causing ReplayMod to jumble block IDs
- **Previous manual fix**: Download world → Open in singleplayer → Manually optimize → Re-upload
- **This was tedious and risky** for server environments where mods need to be changed on-the-fly

### The Solution
WorldOptimiser automatically detects mod list changes and triggers the world optimization process that you would normally do manually through the "Optimize World" button in singleplayer. This recalibrates the ID mappings and fixes ReplayMod compatibility issues.

## ✨ Features

- **Automatic Detection**: Monitors for mod additions, removals, and version changes
- **Smart Optimization**: Only runs when actual mod changes are detected
- **Server-Side**: Works on both dedicated servers and integrated servers
- **Non-Intrusive**: Runs seamlessly in the background during world startup
- **Replay-Safe**: Ensures your ReplayMod recordings display correctly after mod changes

## 🔧 How It Works

1. **Startup Check**: When a world loads, the mod reads the saved mod list from `level.dat`
2. **Comparison**: Compares the saved mod list with currently loaded mods
3. **Detection**: Identifies any additions, removals, or version changes
4. **Optimization**: Automatically runs the world optimization process if changes are detected
5. **Completion**: World starts normally with updated ID mappings

## 📋 Requirements

- **Minecraft**: 1.20.1
- **Forge**: 47.4.0 or higher
- **Java**: 17+

## 📦 Installation

1. Download the latest release from the releases page
2. Place `worldoptimiser-1.0.jar` in your `mods` folder
3. Start your server/world - the mod will automatically handle optimization when needed

## 🎮 Usage

No configuration required! The mod works automatically:

- Install the mod
- Add/remove other mods as needed
- Start your world - optimization happens automatically if mod changes are detected
- Your ReplayMod recordings will now display correctly

## 📝 Log Output

When the mod detects changes, you'll see messages like:
```
[Server thread/WARN] [worldoptimiser]: Detected new mods world will be optimised on startup
[Server thread/INFO] [worldoptimiser/worldupgrader]: World will be optimised it may take a while, please wait...
[Server thread/INFO] [worldoptimiser/worldupgrader]: World optimisation complete
```

## 🐛 Known Issues

- World optimization can take some time for large worlds
- The process blocks world startup until completion (this is necessary for proper ID remapping)

## 👨‍💻 Created For

This mod was specifically created for **[@protagnst](https://youtube.com/@protagnst)** to solve ReplayMod compatibility issues in their content creation workflow.

## 💼 Custom Development

Need a custom mod solution for your specific use case? I'm available for hire for custom Minecraft mod development and similar technical solutions.

**Contact**: Available for custom development work - reach out if you have similar issues that need solving!

## 📄 Technical Details

- **Mod ID**: `worldoptimiser`
- **Version**: 1.0
- **Author**: Pelotrio
- **License**: All Rights Reserved

### Compatibility Note
This issue was specific to ReforgedPlay Mod v0.3.1 on Forge 1.20.1. Previous versions (Forge 1.16.5 and Fabric versions) handled ID changes automatically, but this functionality was lost in the 1.20.1 Forge version.

## 🔧 For Developers

The mod hooks into Forge's mod loading events and world startup to:
- Detect mod list changes via `ModMismatchEvent`
- Read/compare mod lists stored in world NBT data
- Trigger Minecraft's built-in `WorldUpgrader` when needed
- Ensure compatibility with ReplayMod's block ID system

---

*This mod solves a specific technical issue with ReplayMod compatibility. If you're experiencing similar block ID or texture mapping issues after mod changes, this tool automates the manual optimization process that was previously required.*
