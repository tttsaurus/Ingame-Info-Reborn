## InGame Info Reborn <img src="logo.png" alt="InGame Info Reborn" width="160" align="right" style="margin-left: 16px; vertical-align: middle;"/>

![Platform](https://img.shields.io/badge/Platform-Cleanroom-brightgreen.svg)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/6f3ca5da261d410c8eb9479de9457372)](https://app.codacy.com/gh/tttsaurus/Ingame-Info-Reborn/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

This is a spiritual successor of [InGame-Info-XML](https://github.com/Lunatrius/InGame-Info-XML), **not a fork**.
It shares no code with the original project.
The original design focused on structured in-game HUDs and simple GUIs — reactive, declarative, and composable by design.
Over time, the scope has shifted to in-game editor oriented workflows.

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

My Detailed Todo List:
- https://trello.com/b/MTLHeyGn/ingameinfo

## Latest Build
In case you want to use the latest action build
- Go to [GitHub Actions](https://github.com/tttsaurus/Ingame-Info-Reborn/actions)
- Click on the latest workflow
- Scroll down to the bottom and download the `Artifacts`
- Unzip and `ingameinfo-[version].jar` is the mod file

## Credits
- Created using [CleanroomModTemplate](https://github.com/CleanroomMC/CleanroomModTemplate)
- Inspired by [InGame-Info-XML](https://github.com/Lunatrius/InGame-Info-XML)
- Bundled [Configurate](https://github.com/SpongePowered/Configurate) licensed under Apache-2.0
