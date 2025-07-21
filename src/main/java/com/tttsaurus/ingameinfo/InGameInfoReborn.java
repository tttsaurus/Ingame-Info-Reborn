package com.tttsaurus.ingameinfo;

import com.tttsaurus.ingameinfo.common.core.commonutils.TextFormattingV2;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.tttsaurus.ingameinfo.proxy.CommonProxy;

import java.util.List;

@Mod(modid = Tags.MODID,
     name = Tags.MODNAME,
     version = Tags.VERSION,
     acceptedMinecraftVersions = "[1.12.2]",
     dependencies = "required-after:mixinbooter@[10.0,)")
public final class InGameInfoReborn
{
    @SidedProxy(
            clientSide = "com.tttsaurus.ingameinfo.proxy.ClientProxy",
            serverSide = "com.tttsaurus.ingameinfo.proxy.ServerProxy")
    private static CommonProxy proxy;

    public static boolean crafttweakerLoaded;
    public static boolean bloodmagicLoaded;
    public static boolean sereneseasonsLoaded;
    public static boolean thaumcraftLoaded;
    public static boolean rftoolsdimLoaded;
    public static boolean deepresonanceLoaded;
    public static boolean toughasnailsLoaded;
    public static boolean simpledifficultyLoaded;
    public static boolean fluxloadingLoaded;

    public static final Logger LOGGER = LogManager.getLogger(Tags.MODNAME);

    public static ASMDataTable asmDataTable;
    private static Boolean isCleanroom = null;

    public static boolean isCleanroom()
    {
        if (isCleanroom == null)
        {
            try
            {
                Class.forName("com.cleanroommc.boot.Main");
                isCleanroom = true;
                return true;
            }
            catch (ClassNotFoundException e)
            {
                isCleanroom = false;
                return false;
            }
        }
        else
            return isCleanroom;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        asmDataTable = event.getAsmData();
        proxy.preInit(event, LOGGER);

        LOGGER.info("debug text formatting");
        List<TextFormattingV2.NestedToken> tokens = TextFormattingV2.tokenize("@green[green and @obf[more]]");
        for (TextFormattingV2.NestedToken token: tokens)
            LOGGER.info("\n" + token);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event, LOGGER);
        LOGGER.info("In-Game Info Reborn initialized.");
    }
}
