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
Here's an easy example.
```java
public class TestView extends View
{
    @Override
    public void init(GuiLayout guiLayout)
    {
        guiLayout
                .startGroup(new HorizontalGroup(), "\"padding\" : {\"top\" : 10, \"left\" : 10}")
                .addElement(new Text(), "\"uid\" : \"AAA\", \"scale\" : 2.0f, \"backgroundStyle\" : \"roundedBoxWithOutline\"")
                .endGroup();
    }
}

public class TestViewModel extends ViewModel<TestView>
{
    @Reactive(targetUid = "AAA", property = "text", initiativeSync = true)
    public ReactiveObject<String> testString = new ReactiveObject<String>(){};

    @Override
    public void start()
    {
        testString.set("New Test");
    }
}

MvvmRegistry.register("test", TestViewModel.class);
IgiGuiManager.openGui("test");
```
The default alignment and pivot are the top-left corner.
![Snipaste_2024-12-21_19-01-57](https://github.com/user-attachments/assets/b6b23e72-5081-45d7-97d9-b811c8336141)

Crt API is still WIP but should look similar to java code.

## Credits
- Created using [GregTechCEu's Buildscripts](https://github.com/GregTechCEu/Buildscripts)
- Inspired by [InGame-Info-XML](https://github.com/Lunatrius/InGame-Info-XML)
