package com.tttsaurus.ingameinfo.common.impl.network.tps;

import com.tttsaurus.ingameinfo.common.impl.network.IgiNetwork;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RequestTpsMsptPacket implements IMessage
{
    public RequestTpsMsptPacket() { }

    @Override
    public void fromBytes(ByteBuf buf) { }

    @Override
    public void toBytes(ByteBuf buf) { }

    public static class Handler implements IMessageHandler<RequestTpsMsptPacket, IMessage>
    {
        @Override
        public IMessage onMessage(RequestTpsMsptPacket message, MessageContext ctx)
        {
            if (!ctx.side.isServer()) return null;

            MinecraftServer server = ctx.getServerHandler().player.getServerWorld().getMinecraftServer();
            if (server == null) return null;
            if (!server.isServerRunning()) return null;

            EntityPlayerMP player = ctx.getServerHandler().player;
            server.addScheduledTask(() ->
            {
                long[] tickTimes = server.tickTimeArray;
                double averageTickTime = 0d;

                for (long tickTime : tickTimes)
                    averageTickTime += tickTime / 1.0E6d;
                averageTickTime /= tickTimes.length;

                int tps = (int)(Math.min(1000d / averageTickTime, 20d));

                IgiNetwork.NETWORK.sendTo(new RespondTpsMsptPacket(tps, averageTickTime), player);
            });

            return null;
        }
    }
}
