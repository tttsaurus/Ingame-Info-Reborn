package com.tttsaurus.ingameinfo.common.impl.network;

import com.tttsaurus.ingameinfo.Tags;
import com.tttsaurus.ingameinfo.common.api.function.IAction_2Param;
import com.tttsaurus.ingameinfo.common.impl.network.tps.RequestTpsMtpsPacket;
import com.tttsaurus.ingameinfo.common.impl.network.tps.RespondTpsMtpsPacket;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class IgiNetwork
{
    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MODID);

    public static void requestTpsMtps(IAction_2Param<Integer, Double> callback)
    {
        RespondTpsMtpsPacket.callback = callback;
        NETWORK.sendToServer(new RequestTpsMtpsPacket());
    }

    public static void init()
    {
        int index = 0;

        NETWORK.registerMessage(RequestTpsMtpsPacket.Handler.class, RequestTpsMtpsPacket.class, index++, Side.SERVER);
        NETWORK.registerMessage(RespondTpsMtpsPacket.Handler.class, RespondTpsMtpsPacket.class, index++, Side.CLIENT);
    }
}
