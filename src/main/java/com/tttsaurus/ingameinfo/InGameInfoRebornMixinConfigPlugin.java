package com.tttsaurus.ingameinfo;

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
        return switch (mixinClassName.split("\\.")[5])
        {
            case "hei" -> Loader.isModLoaded("jei");
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
