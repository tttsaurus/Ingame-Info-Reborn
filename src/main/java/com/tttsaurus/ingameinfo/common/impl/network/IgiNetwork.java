package com.tttsaurus.ingameinfo.common.impl.network;

import com.tttsaurus.ingameinfo.Reference;
import com.tttsaurus.ingameinfo.common.core.function.IAction_1Param;
import com.tttsaurus.ingameinfo.common.core.function.IAction_2Param;
import com.tttsaurus.ingameinfo.common.impl.network.common.RespondNbtPacket;
import com.tttsaurus.ingameinfo.common.impl.network.modcompat.bloodmagic.RequestBloodMagicNbtPacket;
import com.tttsaurus.ingameinfo.common.impl.network.modcompat.thaumcraft.RequestThaumcraftNbtPacket;
import com.tttsaurus.ingameinfo.common.impl.network.tps.RequestTpsMsptPacket;
import com.tttsaurus.ingameinfo.common.impl.network.tps.RespondTpsMsptPacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class IgiNetwork
{
    // key: response key
    private static final Map<String, NBTTagCompound> cachedNbtResponses = new HashMap<>();
    private static final Map<String, IAction_1Param<NBTTagCompound>> nbtResponseConsumers = new HashMap<>();

    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

    public static void pushNbtResponse(String responseKey, NBTTagCompound nbt)
    {
        cachedNbtResponses.put(responseKey, nbt);
        IAction_1Param<NBTTagCompound> consumer = nbtResponseConsumers.get(responseKey);
        if (consumer != null) consumer.invoke(nbt);
    }
    @Nullable
    public static NBTTagCompound popNbtResponse(String responseKey)
    {
        NBTTagCompound nbt = cachedNbtResponses.get(responseKey);
        if (nbt == null) return null;
        cachedNbtResponses.remove(responseKey);
        return nbt;
    }
    protected static void addNbtResponseConsumer(String responseKey, IAction_1Param<NBTTagCompound> consumer)
    {
        nbtResponseConsumers.put(responseKey, consumer);
    }

    public static void requestTpsMspt(IAction_2Param<Integer, Double> callback)
    {
        RespondTpsMsptPacket.callback = callback;
        NETWORK.sendToServer(new RequestTpsMsptPacket());
    }
    public static void requestBloodMagicNbt(IAction_1Param<NBTTagCompound> callback)
    {
        addNbtResponseConsumer(RequestBloodMagicNbtPacket.RESPONSE_KEY, callback);
        NETWORK.sendToServer(new RequestBloodMagicNbtPacket());
    }
    public static void requestThaumcraftNbt(IAction_1Param<NBTTagCompound> callback)
    {
        addNbtResponseConsumer(RequestThaumcraftNbtPacket.RESPONSE_KEY, callback);
        NETWORK.sendToServer(new RequestThaumcraftNbtPacket());
    }

    public static void init()
    {
        int index = 0;

        NETWORK.registerMessage(RespondNbtPacket.Handler.class, RespondNbtPacket.class, index++, Side.CLIENT);

        NETWORK.registerMessage(RequestTpsMsptPacket.Handler.class, RequestTpsMsptPacket.class, index++, Side.SERVER);
        NETWORK.registerMessage(RespondTpsMsptPacket.Handler.class, RespondTpsMsptPacket.class, index++, Side.CLIENT);

        NETWORK.registerMessage(RequestBloodMagicNbtPacket.Handler.class, RequestBloodMagicNbtPacket.class, index++, Side.SERVER);

        NETWORK.registerMessage(RequestThaumcraftNbtPacket.Handler.class, RequestThaumcraftNbtPacket.class, index++, Side.SERVER);
    }
}
