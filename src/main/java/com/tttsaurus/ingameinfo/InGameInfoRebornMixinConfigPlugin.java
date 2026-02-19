package com.tttsaurus.ingameinfo;

import com.tttsaurus.ingameinfo.common.core.InternalMethods;
import net.minecraftforge.fml.common.Loader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class InGameInfoRebornMixinConfigPlugin implements IMixinConfigPlugin
{
    @Override
    public void onLoad(String s)
    {

    }

    @Override
    public String getRefMapperConfig()
    {
        return null;
    }

    /**
     * An example of mod mixin
     * The {@link org.spongepowered.asm.mixin.MixinEnvironment.Phase#MOD} allow the mixins being processed after modlist building
     * Which allow calling {@link Loader#isModLoaded(String)}
     * @param targetClassName Not important unless you are writing multi-target mixin
     * @param mixinClassName The full mixin class name. Filtering with group name is the easiest solution here.
     * @return If the mixin should apply
     */
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
    {
        //<editor-fold desc="fetch dep existence">
        InternalMethods.InGameInfoReborn$crafttweakerLoaded$setter(Loader.isModLoaded("crafttweaker"));
        InternalMethods.InGameInfoReborn$bloodmagicLoaded$setter(Loader.isModLoaded("bloodmagic"));
        InternalMethods.InGameInfoReborn$sereneseasonsLoaded$setter(Loader.isModLoaded("sereneseasons"));
        InternalMethods.InGameInfoReborn$thaumcraftLoaded$setter(Loader.isModLoaded("thaumcraft"));
        InternalMethods.InGameInfoReborn$rftoolsdimLoaded$setter(Loader.isModLoaded("rftoolsdim"));
        InternalMethods.InGameInfoReborn$deepresonanceLoaded$setter(Loader.isModLoaded("deepresonance"));
        InternalMethods.InGameInfoReborn$toughasnailsLoaded$setter(Loader.isModLoaded("toughasnails"));
        InternalMethods.InGameInfoReborn$simpledifficultyLoaded$setter(Loader.isModLoaded("simpledifficulty"));
        InternalMethods.InGameInfoReborn$fluxloadingLoaded$setter(Loader.isModLoaded("fluxloading"));
        InternalMethods.InGameInfoReborn$jeiLoaded$setter(Loader.isModLoaded("jei"));

        InGameInfoReborn.LOGGER.info("Fetched the existence of optional dependent mods.");
        //</editor-fold>

        return switch (mixinClassName.split("\\.")[5])
        {
            case "hei" -> InGameInfoReborn.isJeiLoaded().orElseThrow();
            default -> true;
        };
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1)
    {

    }

    @Override
    public List<String> getMixins()
    {
        return null;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo)
    {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo)
    {

    }
}
