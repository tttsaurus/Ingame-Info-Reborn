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
  - Simple Button (✔)
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

## How to use
<details>
<summary>Click to expand</summary>

Here's an easy example (requirement: version >= 1.0.0-b5).

`./config/ingameinfo/test.ixml`
```xml
<HorizontalGroup>
    <Text uid = "myUid">
</Group>
```
`TestView.java`
```java
public class TestView extends View
{
    @Override
    public String getIxmlFileName() { return "test"; }
}
```
`TestViewModel.java`
```java
public class TestViewModel extends ViewModel<TestView>
{
    @Reactive(targetUid = "myUid", property = "text", initiativeSync = true)
    public ReactiveObject<String> myUidText = new ReactiveObject<String>(){};

    @Override
    public void start()
    {
        EventCenter.igiGuiFpsEvent.addListener((fixedFps, renderFps) ->
        {
            myUidText.set("Fixed FPS: " + fixedFps + ", Render FPS: " + renderFps);
        });
    }
}
```
```java
@SubscribeEvent
public static void onMvvmRegister(MvvmRegisterEvent event)
{
    MvvmRegistry.autoRegister("test", TestViewModel.class);
}
```
```java
IgiGuiManager.openGui("test");
```
The default alignment and pivot are the top-left corner.
![Snipaste_2025-01-22_00-20-57](https://github.com/user-attachments/assets/ee9818ca-eee7-4ff2-9825-00a8cd3c1cc4)


Zenscript version is as follows (install ProbeZS and ZS IntelliSense for more api details)
```zenscript
#loader preinit

import mods.ingameinfo.mvvm.ViewModel;
import mods.ingameinfo.mvvm.View;
import mods.ingameinfo.mvvm.Mvvm;
import mods.ingameinfo.Types;
import mods.ingameinfo.igievent.EventCenter;

Mvvm.define("test");

View.setIxmlFileName("test");

var myUidText = ViewModel.registerReactiveObject("myUidText", Types.String, "myUid", "text", true);

ViewModel.setStartAction(function()
{
    EventCenter.addIgiGuiFpsEventListener(function(fixedFps as int, renderFps as int)
    {
        myUidText.set("Fixed FPS: " ~ fixedFps ~ ", Render FPS: " ~ renderFps);
    });
});
```
```zenscript
import mods.ingameinfo.gui.IgiGuiManager;
import mods.ingameinfo.event.IgiGuiInitEvent;

events.onIgiGuiInit(function(event as IgiGuiInitEvent)
{
    IgiGuiManager.openGui("test");
});
```

Extra java example:
- [TemplateView](https://github.com/tttsaurus/Ingame-Info-Reborn/blob/master/src/main/java/com/tttsaurus/ingameinfo/common/impl/mvvm/TemplateView.java)
- [TemplateViewModel](https://github.com/tttsaurus/Ingame-Info-Reborn/blob/master/src/main/java/com/tttsaurus/ingameinfo/common/impl/mvvm/TemplateViewModel.java)

Extra Tips:
- Go to `./logs/latest.log`
- Find
  ```
  [17:03:34] [Client thread/INFO] [ingameinfo]: Registered serviceable elements: 
  [17:03:34] [Client thread/INFO] [ingameinfo]:   - UrlImage
  [17:03:34] [Client thread/INFO] [ingameinfo]:   - SlidingText
  [17:03:34] [Client thread/INFO] [ingameinfo]:   - Text
  [17:03:34] [Client thread/INFO] [ingameinfo]:   - VerticalGroup
  [17:03:34] [Client thread/INFO] [ingameinfo]:   - AnimText
  [17:03:34] [Client thread/INFO] [ingameinfo]:   - SimpleButton
  [17:03:34] [Client thread/INFO] [ingameinfo]:   - EmptyBlock
  [17:03:34] [Client thread/INFO] [ingameinfo]:   - SizedGroup
  [17:03:34] [Client thread/INFO] [ingameinfo]:   - HorizontalGroup
  [17:03:34] [Client thread/INFO] [ingameinfo]:   - ProgressBar
  [17:03:34] [Client thread/INFO] [ingameinfo]:
  [17:03:34] [Client thread/INFO] [ingameinfo]: Notice:
  [17:03:34] [Client thread/INFO] [ingameinfo]: 1. Elements marked with * below are unserviceable in ixml.
  [17:03:34] [Client thread/INFO] [ingameinfo]: 2. You can access style properties from parent elements.
  [17:03:34] [Client thread/INFO] [ingameinfo]:
  [17:03:34] [Client thread/INFO] [ingameinfo]: Element type: Sized* extends Element*
  [17:03:34] [Client thread/INFO] [ingameinfo]: - With style properties:
  [17:03:34] [Client thread/INFO] [ingameinfo]:   - [float] width (with deserializer: BuiltinTypesDeserializer)
  [17:03:34] [Client thread/INFO] [ingameinfo]:     - Setter callback pre: nonNegativeFloatValidation
  [17:03:34] [Client thread/INFO] [ingameinfo]:     - Setter callback post: requestReCalc
  [17:03:34] [Client thread/INFO] [ingameinfo]:   - [float] height (with deserializer: BuiltinTypesDeserializer)
  [17:03:34] [Client thread/INFO] [ingameinfo]:     - Setter callback pre: nonNegativeFloatValidation
  [17:03:34] [Client thread/INFO] [ingameinfo]:     - Setter callback post: requestReCalc
  ...
  ```
- Then you'll know what style properties each element has
- As a result, you can write your `ixml` files with ease

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
