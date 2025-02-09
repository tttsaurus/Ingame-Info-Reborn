package com.tttsaurus.ingameinfo;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import com.tttsaurus.ingameinfo.proxy.CommonProxy;

@Mod(modid = Tags.MODID,
     name = Tags.MODNAME,
     version = Tags.VERSION,
     dependencies = "")
public final class InGameInfoReborn
{
    @SidedProxy(
            clientSide = "com.tttsaurus.ingameinfo.proxy.ClientProxy",
            serverSide = "com.tttsaurus.ingameinfo.proxy.ServerProxy")
    private static CommonProxy proxy;

    public static boolean crafttweakerLoaded;
    public static boolean bloodmagicLoaded;
    public static boolean sereneseasonsLoaded;

    public static Logger logger;
    public static ASMDataTable asmDataTable;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        asmDataTable = event.getAsmData();
        proxy.preInit(event, logger);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event, logger);
        logger.info("In-Game Info Reborn initialized.");
    }
}
