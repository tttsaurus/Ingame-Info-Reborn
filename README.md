<div align="center">
  <img src="logo.png" alt="Your Image" width="300"/>
  <h1>InGame Info Reborn</h1>
</div>

This is a spiritual successor of [InGame-Info-XML](https://github.com/Lunatrius/InGame-Info-XML), **not a fork**. Moreover, this is growing into a general-purpose HUD/GUI lib for both java and zenscript users.

If you like this project, don't forget to give it a star⭐!

Suggestions/PRs are welcome

## Overview
This is a library mod that helps you to create in-game overlaid (or focused) GUI with ease.

![Snipaste_2025-01-12_12-53-07](https://github.com/user-attachments/assets/581f0727-bba8-4ff5-9780-8fdbfaf587fd)
(Nothing will pop up with the default configuration!)

## Wiki
- [Wiki Link](https://tttsaurus.github.io/Ingame-Info-Reborn-Wiki/)

## Todo List / Features
<details>
<summary>Click to expand</summary>

My Detailed Todo List:
- https://trello.com/b/MTLHeyGn/ingameinfo

Feature Overview:
- Approximate Model-View-ViewModel pattern (✔)
- Add framebuffer to the GUI rendering life cycle (✔)
- GUI theme manager (✔)
- Introduce a custom GUI container (✔)
- Maintain a list of custom GUI containers so that GUIs can stack together (✔)
- A GUI container can be ingame-overlaid/focused (runtime switchable) (✔)
- Introduce feature-rich GUI layout
  - Pivot (✔)
  - Alignment (✔)
  - Padding (✔)
  - Horizontal Group (stack elements horizontally) (✔)
  - Vertical Group (stack elements vertically) (✔)
  - Sized Group (✔)
  - Nesting Groups (group in group) (✔)
  - Adaptive Group (fit elements into it adaptively)
  - Foldout Group
  - Draggable Group
- Add controls like text, button, input field, etc.
  - Text (✔)
  - Sliding Text (✔)
  - Anim Text (✔)
  - Button (✔)
  - Checkbox
  - Input Field
  - Image (✔)
  - Url Image (✔)
  - GIF
  - Slide Bar
  - Progress Bar (✔)
  - Item (✔)
- Introduce modular animation options for controls
- Add CrT/Zenscript support (✔)
- Ingame spotify support (go to wiki for details) (✔)

</details>

## Latest Build
In case you want to use the latest action build
- Go to [GitHub Actions](https://github.com/tttsaurus/Ingame-Info-Reborn/actions)
- Click on the latest workflow
- Scroll down to the bottom and download the `Artifacts`
- Unzip and `ingameinfo-[version].jar` is the mod file

## Credits
- Created using [GregTechCEu's Buildscripts](https://github.com/GregTechCEu/Buildscripts)
- Inspired by [InGame-Info-XML](https://github.com/Lunatrius/InGame-Info-XML)
- Bundled [Configurate](https://github.com/SpongePowered/Configurate) licensed under Apache-2.0
