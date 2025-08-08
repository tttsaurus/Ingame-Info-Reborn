<div align="center">
  <img src="logo.png" alt="InGame Info Reborn" width="300"/>
  <h1>InGame Info Reborn</h1>
  <p>"In-game HUDs and GUIs done with structure — reactive, declarative, composable, and built to handle the rest."</p>
</div>
<br>

[![Versions](https://img.shields.io/curseforge/game-versions/1171541?logo=curseforge&label=Game%20Version)](https://www.curseforge.com/minecraft/mc-mods/ingame-info-reborn)
[![Downloads](https://img.shields.io/curseforge/dt/1171541?logo=curseforge&label=Downloads)](https://www.curseforge.com/minecraft/mc-mods/ingame-info-reborn)
[![Downloads](https://img.shields.io/modrinth/dt/ingame-info-reborn?logo=modrinth&label=Downloads)](https://modrinth.com/mod/ingame-info-reborn)
[![CodeFactor](https://www.codefactor.io/repository/github/tttsaurus/ingame-info-reborn/badge)](https://www.codefactor.io/repository/github/tttsaurus/ingame-info-reborn)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/6f3ca5da261d410c8eb9479de9457372)](https://app.codacy.com/gh/tttsaurus/Ingame-Info-Reborn/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

This is a _Work-In-Progress_ spiritual successor of [InGame-Info-XML](https://github.com/Lunatrius/InGame-Info-XML), **not a fork**.
If you like this project, don't forget to give it a star⭐!

Suggestions/PRs are welcome

## Overview
This is a library mod that helps you to create in-game overlaid (or focused) GUI with ease.

![Snipaste_2025-01-12_12-53-07](https://github.com/user-attachments/assets/581f0727-bba8-4ff5-9780-8fdbfaf587fd)
(Nothing will pop up with the default configuration!)

### Architectural Modules Chart

<details>
<summary>Click to Expand</summary>

| Module                                   | Role                                                             | Status         |
|------------------------------------------|------------------------------------------------------------------|----------------|
| **MVVM Base**                            | Separates logic (ViewModel) from rendering (View)                | ✅ Done         |
| **XAML-Style DSL For View**              | Declarative XAML-style layout to build static UI trees           | ✅ Done         |
| **Reactive Binding**                     | View reacts to changes in ViewModel automatically                | ✅ Done         |
| **Compose (Injected via Slot)**          | Immediate-mode UI embedded in ViewModel                          | ⚠️ Partially   |
| **Snapshot Diffing**                     | Virtual tree diffing for Compose-based UI                        | ✅ Done         |
| **Shared Context**                       | Shared runtime context between ViewModel and Compose blocks      | ✅ Done         |
| **DOM-Like Event System**                | Input propagation and event capturing/bubbling                   | ✅ Done         |
| **Interactable Control**                 | Captures input, intercepts propagation (works with Event System) | ✅ Done         |
| **Fixed / Render Update**                | Dual update loop for logic vs render                             | ✅ Done         |
| **Annotation Driven Auto-Interpolation** | Utility for smooth interpolation during render updates           | ✅ Done         |
| **Render Op Queue**                      | Abstract draw commands for controls                              | ✅ Done         |
| **UI Decoration**                        | Draw custom visuals on existing controls                         | ✅ Mostly Done  |
| **Transition API**                       | Externally trigger view transitions                              | 🚧 Planned     |
| **Theme Manager**                        | Global theme system (colors, font scale, etc)                    | ✅ Mostly Done  |
| **Modal Layer**                          | Stack-based modal / dialog system                                | 🚧 Planned     |

</details>

### Example
```xml
<VerticalGroup>
    <Text uid = "fps">
    ...
</Group>
```
```java
// snippet from View and ViewModel classes
@Reactive(targetUid = "fps", property = "text", initiativeSync = true)
public ReactiveObject<String> fpsText = new ReactiveObject<>(){};

EventCenter.gameFpsEvent.addListener((fps) ->
{
    fpsText.set("FPS: " + fps);
});
```
```java
// registration entry point
@SubscribeEvent
public static void onIgiRuntimeEntryPoint(IgiRuntimeEntryPointEvent event)
{
    event.runtime.initPhase
            .registerMvvm("example", ExampleViewModel.class)
            .openGuiOnStartup("example");
}
```

## Wiki
- [WIP Wiki](https://tttsaurus.github.io/Ingame-Info-Reborn-Wiki/)

## Implementation Todo List / Overview
<details>
<summary>Click to Expand</summary>

_**Currently working on the architecture.**_<br>
_**Not adding controls or QoL updates.**_

My Detailed Todo List:
- https://trello.com/b/MTLHeyGn/ingameinfo

Implementation Overview:
- Add framebuffer to the GUI rendering life cycle (✔)
- Introduce a custom GUI container (✔)
- Maintain a list of GUI containers so that GUIs can stack together (✔)
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
