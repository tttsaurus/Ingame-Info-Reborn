If you like this project, don't forget to give it a star⭐!

Currently WIP

## Overview
This is a library mod that helps you to create in-game overlaid or focused gui with ease.

## Todo List / Features
- Introduce a custom gui container (✔)
- Maintain a list of custom gui containers (✔)
- A gui container can be ingame-overlaid/focused (✔)
- Introduce feature-rich gui layout (✔)
  - Pivot (✔)
  - Alignment (✔)
  - Padding (✔)
  - Horizontal Group (✔)
  - Vertical Group (✔)
  - Nesting Groups (✔)
- Add controls like text, button, input field, etc.
  - Text (✔)
  - Anim Text (mostly done)
  - Button (✔)
  - Checkbox
  - Input Field
  - Image
  - GIF
  - Slide Bar
  - Progress Bar
  - Draggable
- Introduce modular animation options for controls
- Add crt support (runtime gui setup)

## Quick Start
- Go to [GitHub Actions](https://github.com/tttsaurus/Ingame-Info-Reborn/actions)
- Click on a workflow
- Scroll down to the bottom and download the `Artifacts`
- Unzip and `ingameinfo-[version].jar` is the mod file

## How to use
API is WIP but should look like
```java
GUILayout.startVertical();
GUILayout.text("Hello World");
GUILayout.text("FPS: {0}", fpsDelegate);
GUILayout.endVertical();
```
Crt API is also WIP but should look similar to java code.

## Credits
- Created using [GregTechCEu's Buildscripts](https://github.com/GregTechCEu/Buildscripts)
- Inspired by [InGame-Info-XML](https://github.com/Lunatrius/InGame-Info-XML)
