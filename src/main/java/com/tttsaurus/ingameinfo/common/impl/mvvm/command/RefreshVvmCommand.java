package com.tttsaurus.ingameinfo.common.impl.mvvm.command;

import com.tttsaurus.ingameinfo.common.core.IgiRuntimeLocator;
import com.tttsaurus.ingameinfo.common.core.gui.IgiGuiContainer;
import com.tttsaurus.ingameinfo.common.impl.gui.DefaultLifecycleHolder;
import com.tttsaurus.ingameinfo.common.core.InternalMethods;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import java.util.Map;

@SuppressWarnings("all")
public class RefreshVvmCommand extends CommandBase
{
    @Override
    public String getName()
    {
        return "igirefresh";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "/igirefresh <mvvm>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length == 1)
        {
            Map<String, IgiGuiContainer> map = InternalMethods.instance.GuiLifecycleProvider$openedGuiMap$getter.invoke(IgiRuntimeLocator.get().lifecycleHolder.getLifecycleProvider());
            if (map.containsKey(args[0]))
            {
                map.get(args[0]).refreshVvm(IgiRuntimeLocator.get().mvvmRegistry);
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Refreshed In-Game Info GUI " + TextFormatting.AQUA + args[0] + TextFormatting.RESET));
            }
            else
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("In-Game Info GUI " + TextFormatting.AQUA + args[0] + TextFormatting.RESET + " isn't instantiated"));
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }
}
