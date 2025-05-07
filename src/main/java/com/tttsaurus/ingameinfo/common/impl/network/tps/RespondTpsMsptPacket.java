package com.tttsaurus.ingameinfo.common.impl.network.tps;

import com.tttsaurus.ingameinfo.common.core.function.IAction_2Param;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RespondTpsMsptPacket implements IMessage
{
    public static IAction_2Param<Integer, Double> callback;

    private int tps;
    private double mspt;

    public RespondTpsMsptPacket() { }

    public RespondTpsMsptPacket(int tps, double mspt)
    {
        this.tps = tps;
        this.mspt = mspt;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        tps = buf.readInt();
        mspt = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(tps);
        buf.writeDouble(mspt);
    }

    public static class Handler implements IMessageHandler<RespondTpsMsptPacket, IMessage>
    {
        @Override
        public IMessage onMessage(RespondTpsMsptPacket message, MessageContext ctx)
        {
            if (!ctx.side.isClient()) return null;

            callback.invoke(message.tps, message.mspt);

            return null;
        }
    }
}
