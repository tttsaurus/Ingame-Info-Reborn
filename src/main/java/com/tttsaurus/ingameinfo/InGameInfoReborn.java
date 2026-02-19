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

@SuppressWarnings("FieldMayBeFinal")
@Mod(modid = Reference.MOD_ID,
     name = Reference.MOD_NAME,
     version = Reference.VERSION,
     acceptedMinecraftVersions = "[1.12.2]",
     dependencies = "required:cleanroom@[0.4.3-alpha,)")
public final class InGameInfoReborn
{
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

    /**
     * Will be injected by Forge.
     */
    @SidedProxy(
            clientSide = "com.tttsaurus.ingameinfo.proxy.ClientProxy",
            serverSide = "com.tttsaurus.ingameinfo.proxy.ServerProxy")
    private static CommonProxy proxy;

    //<editor-fold desc="isModLoaded">
    /**
     * Will be injected at <code>shouldApplyMixin</code> phase.
     */
    private static Boolean crafttweakerLoaded = null;

    /**
     * Will be injected at <code>shouldApplyMixin</code> phase.
     */
    private static Boolean bloodmagicLoaded = null;

    /**
     * Will be injected at <code>shouldApplyMixin</code> phase.
     */
    private static Boolean sereneseasonsLoaded = null;

    /**
     * Will be injected at <code>shouldApplyMixin</code> phase.
     */
    private static Boolean thaumcraftLoaded = null;

    /**
     * Will be injected at <code>shouldApplyMixin</code> phase.
     */
    private static Boolean rftoolsdimLoaded = null;

    /**
     * Will be injected at <code>shouldApplyMixin</code> phase.
     */
    private static Boolean deepresonanceLoaded = null;

    /**
     * Will be injected at <code>shouldApplyMixin</code> phase.
     */
    private static Boolean toughasnailsLoaded = null;

    /**
     * Will be injected at <code>shouldApplyMixin</code> phase.
     */
    private static Boolean simpledifficultyLoaded = null;

    /**
     * Will be injected at <code>shouldApplyMixin</code> phase.
     */
    private static Boolean fluxloadingLoaded = null;

    /**
     * Will be injected at <code>shouldApplyMixin</code> phase.
     */
    private static Boolean jeiLoaded = null;

    /**
     * The optional handle will only be empty at a very early stage.
     * You should access the method in the form of <code>is*Loaded().orElseThrow()</code>
     * and that is safe for most of the use cases.
     * <p>Note: cache the <code>is*Loaded</code> result in hotpaths to prevent <code>Optional</code> allocation overhead.</p>
     *
     * @see #crafttweakerLoaded
     */
    public static Optional<Boolean> isCraftTweakerLoaded()
    {
        return Optional.ofNullable(crafttweakerLoaded);
    }

    /**
     * The optional handle will only be empty at a very early stage.
     * You should access the method in the form of <code>is*Loaded().orElseThrow()</code>
     * and that is safe for most of the use cases.
     * <p>Note: cache the <code>is*Loaded</code> result in hotpaths to prevent <code>Optional</code> allocation overhead.</p>
     *
     * @see #bloodmagicLoaded
     */
    public static Optional<Boolean> isBloodMagicLoaded()
    {
        return Optional.ofNullable(bloodmagicLoaded);
    }

    /**
     * The optional handle will only be empty at a very early stage.
     * You should access the method in the form of <code>is*Loaded().orElseThrow()</code>
     * and that is safe for most of the use cases.
     * <p>Note: cache the <code>is*Loaded</code> result in hotpaths to prevent <code>Optional</code> allocation overhead.</p>
     *
     * @see #sereneseasonsLoaded
     */
    public static Optional<Boolean> isSereneSeasonsLoaded()
    {
        return Optional.ofNullable(sereneseasonsLoaded);
    }

    /**
     * The optional handle will only be empty at a very early stage.
     * You should access the method in the form of <code>is*Loaded().orElseThrow()</code>
     * and that is safe for most of the use cases.
     * <p>Note: cache the <code>is*Loaded</code> result in hotpaths to prevent <code>Optional</code> allocation overhead.</p>
     *
     * @see #thaumcraftLoaded
     */
    public static Optional<Boolean> isThaumcraftLoaded()
    {
        return Optional.ofNullable(thaumcraftLoaded);
    }

    /**
     * The optional handle will only be empty at a very early stage.
     * You should access the method in the form of <code>is*Loaded().orElseThrow()</code>
     * and that is safe for most of the use cases.
     * <p>Note: cache the <code>is*Loaded</code> result in hotpaths to prevent <code>Optional</code> allocation overhead.</p>
     *
     * @see #rftoolsdimLoaded
     */
    public static Optional<Boolean> isRfToolsDimLoaded()
    {
        return Optional.ofNullable(rftoolsdimLoaded);
    }

    /**
     * The optional handle will only be empty at a very early stage.
     * You should access the method in the form of <code>is*Loaded().orElseThrow()</code>
     * and that is safe for most of the use cases.
     * <p>Note: cache the <code>is*Loaded</code> result in hotpaths to prevent <code>Optional</code> allocation overhead.</p>
     *
     * @see #deepresonanceLoaded
     */
    public static Optional<Boolean> isDeepResonanceLoaded()
    {
        return Optional.ofNullable(deepresonanceLoaded);
    }

    /**
     * The optional handle will only be empty at a very early stage.
     * You should access the method in the form of <code>is*Loaded().orElseThrow()</code>
     * and that is safe for most of the use cases.
     * <p>Note: cache the <code>is*Loaded</code> result in hotpaths to prevent <code>Optional</code> allocation overhead.</p>
     *
     * @see #toughasnailsLoaded
     */
    public static Optional<Boolean> isToughAsNailsLoaded()
    {
        return Optional.ofNullable(toughasnailsLoaded);
    }

    /**
     * The optional handle will only be empty at a very early stage.
     * You should access the method in the form of <code>is*Loaded().orElseThrow()</code>
     * and that is safe for most of the use cases.
     * <p>Note: cache the <code>is*Loaded</code> result in hotpaths to prevent <code>Optional</code> allocation overhead.</p>
     *
     * @see #simpledifficultyLoaded
     */
    public static Optional<Boolean> isSimpleDifficultyLoaded()
    {
        return Optional.ofNullable(simpledifficultyLoaded);
    }

    /**
     * The optional handle will only be empty at a very early stage.
     * You should access the method in the form of <code>is*Loaded().orElseThrow()</code>
     * and that is safe for most of the use cases.
     * <p>Note: cache the <code>is*Loaded</code> result in hotpaths to prevent <code>Optional</code> allocation overhead.</p>
     *
     * @see #fluxloadingLoaded
     */
    public static Optional<Boolean> isFluxLoadingLoaded()
    {
        return Optional.ofNullable(fluxloadingLoaded);
    }

    /**
     * The optional handle will only be empty at a very early stage.
     * You should access the method in the form of <code>is*Loaded().orElseThrow()</code>
     * and that is safe for most of the use cases.
     * <p>Note: cache the <code>is*Loaded</code> result in hotpaths to prevent <code>Optional</code> allocation overhead.</p>
     *
     * @see #jeiLoaded
     */
    public static Optional<Boolean> isJeiLoaded()
    {
        return Optional.ofNullable(jeiLoaded);
    }
    //</editor-fold>

    /**
     * Will be initialized at Forge's <code>preInit</code>.
     */
    private static ASMDataTable asmDataTable = null;

    /**
     * The optional handle will only be empty before Forge's <code>preInit</code> stage.
     * You should access the method in the form of <code>getAsmDataTable().orElseThrow()</code>
     * and that is safe for most of the use cases (after Forge's <code>preInit</code>).
     * <p>Note: cache the result in hotpaths to prevent <code>Optional</code> allocation overhead.</p>
     *
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
