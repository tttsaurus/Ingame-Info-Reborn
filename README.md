If you like this project, don't forget to give it a star⭐!

Currently WIP

Suggestions/PRs are welcome

## Overview
This is a library mod that helps you to create in-game overlaid (or focused) gui with ease.

## Todo List / Features
- Introduce a custom gui container (✔)
- Maintain a list of custom gui containers (✔)
- A gui container can be ingame-overlaid/focused (✔)
- Introduce feature-rich gui layout
  - Pivot (✔)
  - Alignment (✔)
  - Padding (✔)
  - Horizontal Group (stack elements horizontally) (✔)
  - Vertical Group (stack elements vertically) (✔)
  - Sized Group (✔)
  - Nesting Groups (group in group) (✔)
  - Adaptive Group (fit elements into it adaptively)
  - Foldout Group
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
- A gui layout builder (partially done)
- Introduce modular animation options for controls
- Add crt support (runtime gui setup)

## Quick Start
- Go to [GitHub Actions](https://github.com/tttsaurus/Ingame-Info-Reborn/actions)
- Click on the latest workflow
- Scroll down to the bottom and download the `Artifacts`
- Unzip and `ingameinfo-[version].jar` is the mod file

## How to use
Here's an easy example.
```java
GuiLayoutBuilder builder = IgiGui.getBuilder();
builder
    .setDebug(true)
    .startHorizontalGroup()
    .addElement(new Text("test1", 1f, Color.GRAY.getRGB()))
    .startVerticalGroup()
    .addElement(new Text("test2", 1f, Color.GRAY.getRGB()))
    .addElement(new Text("test3", 1f, Color.GRAY.getRGB()))
    .endGroup()
    .endGroup();
IgiGui.openGui(builder);
```
The default alignment is the top-left corner.
![image](https://github.com/user-attachments/assets/0bbd66db-4ee0-4ef9-a47d-0021ffc3be1b)

Crt API is still WIP but should look similar to java code.

## Credits
- Created using [GregTechCEu's Buildscripts](https://github.com/GregTechCEu/Buildscripts)
- Inspired by [InGame-Info-XML](https://github.com/Lunatrius/InGame-Info-XML)
