If you like this project, don't forget to give it a star⭐!

Currently WIP

Suggestions/PRs are welcome

## Overview
This is a library mod that helps you to create in-game overlaid (or focused) gui with ease.

## Todo List / Features
- Approximate Model-View-ViewModel pattern (high priority)
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
- A gui layout builder (✔)
- Introduce modular animation options for controls
- Add crt support (runtime gui setup)

## Quick Start
- Go to [GitHub Actions](https://github.com/tttsaurus/Ingame-Info-Reborn/actions)
- Click on the latest workflow
- Scroll down to the bottom and download the `Artifacts`
- Unzip and `ingameinfo-[version].jar` is the mod file

## How to use
API is changing frequently for the current stage.
Here's an easy example of how to set up a `View`.
```java
GuiLayout builder = IgiGui.getBuilder();
builder
        .setDebug(true)
        .setHeldItemWhitelist(true)
        .addHeldItemWhitelist(new ItemStack(Items.APPLE))
        .startHorizontalGroup("\"padding\" : {\"top\" : 10, \"left\" : 10}")
        .addElement(new Text(), "\"text\" : \"Test1\", \"scale\" : 2.0f, \"color\" : " + Color.GREEN.getRGB() + ", \"alignment\" : BOTTOM_LEFT, \"pivot\" : BOTTOM_LEFT, \"backgroundStyle\" : \"roundedBoxWithOutline\"")
        .startVerticalGroup()
        .addElement(new Text(), "\"text\" : \"Test2\", \"scale\" : 2.0f")
        .addElement(new Text(), "\"text\" : \"Test3\"")
        .endGroup()
        .endGroup();
```
The default alignment and pivot are the top-left corner.
![Snipaste_2024-12-18_18-21-48](https://github.com/user-attachments/assets/dcaadd00-8ac0-48b3-915d-1183a53ef113)

Crt API is still WIP but should look similar to java code.

## Credits
- Created using [GregTechCEu's Buildscripts](https://github.com/GregTechCEu/Buildscripts)
- Inspired by [InGame-Info-XML](https://github.com/Lunatrius/InGame-Info-XML)
