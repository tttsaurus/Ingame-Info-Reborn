package com.tttsaurus.ingameinfo;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.tttsaurus.ingameinfo.proxy.CommonProxy;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

@Mod(modid = Reference.MOD_ID,
     name = Reference.MOD_NAME,
     version = Reference.VERSION,
     acceptedMinecraftVersions = "[1.12.2]",
     dependencies = "required:cleanroom@[0.4.3-alpha,)")
public final class InGameInfoReborn
{
    /**
     * Will be injected by Forge.
     */
    @SidedProxy(
            clientSide = "com.tttsaurus.ingameinfo.proxy.ClientProxy",
            serverSide = "com.tttsaurus.ingameinfo.proxy.ServerProxy")
    private static CommonProxy proxy;

    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

    public static boolean crafttweakerLoaded;
    public static boolean bloodmagicLoaded;
    public static boolean sereneseasonsLoaded;
    public static boolean thaumcraftLoaded;
    public static boolean rftoolsdimLoaded;
    public static boolean deepresonanceLoaded;
    public static boolean toughasnailsLoaded;
    public static boolean simpledifficultyLoaded;
    public static boolean fluxloadingLoaded;

    /**
     * Will be initialized at Forge's <code>preInit</code>.
     */
    private static ASMDataTable asmDataTable = null;

    /**
     * @see #asmDataTable
     */
    @NonNull
    public static Optional<ASMDataTable> getAsmDataTable()
    {
        return Optional.ofNullable(asmDataTable);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        asmDataTable = event.getAsmData();
        proxy.preInit(event, LOGGER);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event, LOGGER);
    }
}
