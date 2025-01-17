package com.tttsaurus.ingameinfo.common.impl.network.tps;

import com.tttsaurus.ingameinfo.common.api.function.IAction_2Param;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RespondTpsMtpsPacket implements IMessage
{
    public static IAction_2Param<Integer, Double> callback;

    private int tps;
    private double mtps;

    public RespondTpsMtpsPacket() { }

    public RespondTpsMtpsPacket(int tps, double mtps)
    {
        this.tps = tps;
        this.mtps = mtps;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        tps = buf.readInt();
        mtps = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(tps);
        buf.writeDouble(mtps);
    }

    public static class Handler implements IMessageHandler<RespondTpsMtpsPacket, IMessage>
    {
        @Override
        public IMessage onMessage(RespondTpsMtpsPacket message, MessageContext ctx)
        {
            if (!ctx.side.isClient()) return null;

            callback.invoke(message.tps, message.mtps);

            return null;
        }
    }
}
