# Everlast — Minecraft Plugin

Everlast is a Minecraft plugin that lets players protect up to three items from being lost on death. Using a unique essence system, players can forge an Everlasting Token and apply it to eligible items through an anvil, ensuring their most valuable possessions stay with them when they die. This plugin adds a new layer of progression and item security to your server, with restrictions to keep gameplay balanced.

## Install

1. Download the `Everlast.jar` file from the release page or the marketplace.
2. Stop your Minecraft server (Paper 1.21 or later).
3. Place the `Everlast.jar` file into the `plugins` folder of your server directory.
4. Restart your server.

## Use

### Getting Started

To protect your first item, you'll need to create a Player Essence, forge an Everlasting Token, and apply it via an anvil.

#### 1. Create a Player Essence

- Hold an empty glass bottle in your main hand.
- Look straight down (pitch angle ≥ 70°).
- Right-click while looking down.
- This consumes **5 XP levels** and gives you a glowing white "Player Essence" bottle bound to you.

#### 2. Forge an Everlasting Token

Craft an Everlasting Token in a 3×3 crafting grid with these exact materials:
- **4** Totems of Undying
- **2** Nether Stars
- **3** Player Essences (all must belong to the same player, and you must be that player)

The result is a glowing "Everlasting Token" that is bound to the player who forged it.

#### 3. Apply the Token to an Item

- Place your eligible item (weapon, tool, armor, etc.) in the first slot of an anvil.
- Place your Everlasting Token in the second slot.
- The anvil will show a protected result for **5 XP levels**.
- Take the result to make the item "Everlasting" — it stays with you when you die.

### Managing Your Protected Items

Use the `/everlast` command:

- `/everlast info` — Shows how many items you currently have protected (out of 3).
- `/everlast unprotect` — Removes protection from the item in your main hand (only if you own it).

### Important Rules

- Each player can protect a **maximum of 3 items**.
- **Shulker boxes** cannot be protected.
- Protected items return to your inventory after death instead of dropping on the ground.
- Essences, tokens, and already-protected items cannot be protected again.
- Only the owner of an essence/token can use them for crafting or anvil application.
- You must have at least 5 XP levels to create an essence.
- Anvil application costs 5 XP levels (in addition to the essence creation cost).

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/39c2b371-afc2-4113-ae35-bd5c69fe5049?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=39c2b371-afc2-4113-ae35-bd5c69fe5049
