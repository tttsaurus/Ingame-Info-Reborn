If you like this project, don't forget to give it a star⭐!

Currently WIP

Suggestions/PRs are welcome

## Overview
This is a library mod that helps you to create in-game overlaid (or focused) gui with ease.

## Todo List / Features
- Approximate Model-View-ViewModel pattern (✔)
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
  - Sliding Text (✔)
  - Anim Text (✔)
  - Simple Button (✔)
  - Checkbox
  - Input Field
  - Image
  - Url Image (✔)
  - GIF
  - Slide Bar
  - Progress Bar (✔)
  - Draggable
- A gui layout builder (✔)
- Introduce modular animation options for controls (WIP)
- Add crt support (✔)
- Ingame spotify support (✔)

## About Spotify Overlay
Also under early stage of development

Version Requirement: >= v1.0.0-b2
- It depends on the Spotify Web API, so it only works when you are connected to the Internet
- You have to register your own spotify app first and then input the client id & secret to the config file to allow the Web API to function ([About Spotify App](https://developer.spotify.com/documentation/web-api/concepts/apps). Btw, Redirect URI should be set to http://localhost:8888 for this mod to listen)
- Input the command `#spotify-oauth` to the chat to authorize your spotify app
- After you finished the authentication, input the command `#spotify-gui true` while you're listening to sth on Spotify to open the gui overlay. `#spotify-gui false` for closing ofc
- You can always run `#spotify-gui true` again to refresh
- Album image loading could be slow
- Theoretically, you no longer need to run `#spotify-oauth` for the next launch and so on, but you should run `#spotify-oauth` to refresh if you encountered any issues

Additionally
- You can edit ./config/ingameinfo/spotify.ixml to create whatever layout you want
- Logic and layout definition are completely separated

## Latest Build
In case you want to use the latest action build
- Go to [GitHub Actions](https://github.com/tttsaurus/Ingame-Info-Reborn/actions)
- Click on the latest workflow
- Scroll down to the bottom and download the `Artifacts`
- Unzip and `ingameinfo-[version].jar` is the mod file

## How to use
API is changing frequently for the current stage.
Here's an easy example under v1.0.0-b1.

`./config/ingameinfo/test.ixml`
```xml
<HorizontalGroup>
    <Text uid = "AAA" scale = 2.0f>
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
    @Reactive(targetUid = "AAA", property = "text", initiativeSync = true)
    public ReactiveObject<String> testString = new ReactiveObject<String>(){};

    @Override
    public void start()
    {
        EventCenter.igiGuiFpsEvent.addListener((fps) ->
        {
            testString.set("GUI FPS: " + fps);
        });
    }
}
```
```java
@SubscribeEvent
public static void onMvvmRegister(MvvmRegisterEvent event)
{
    MvvmRegistry.register("test", TestViewModel.class);
}
```
```java
IgiGuiManager.openGui("test");
```
The default alignment and pivot are the top-left corner.
![Snipaste_2024-12-26_14-29-07](https://github.com/user-attachments/assets/5e04ff27-718f-4633-824a-f0f7e001829d)

Crt version is as follows (install ProbeZS and ZS IntelliSense for more api details)
```zenscript
#loader preinit

import mods.ingameinfo.mvvm.ViewModel;
import mods.ingameinfo.mvvm.View;
import mods.ingameinfo.Types;
import mods.ingameinfo.event.EventCenter;

View.setIxmlFileName("test");

ViewModel.addReactiveObject("testString", Types.String, "AAA", "text", true);

ViewModel.setStartAction(function()
{
    EventCenter.addIgiGuiFpsEventListener(function(fps as int)
    {
        ViewModel.getReactiveObject("testString").set("GUI FPS: " ~ fps);
    });
});
```

## Credits
- Created using [GregTechCEu's Buildscripts](https://github.com/GregTechCEu/Buildscripts)
- Inspired by [InGame-Info-XML](https://github.com/Lunatrius/InGame-Info-XML)
