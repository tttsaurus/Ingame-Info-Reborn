package com.tttsaurus.ingameinfo.common.impl.network.common;

import com.tttsaurus.ingameinfo.common.impl.network.IgiNetwork;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RespondNbtPacket implements IMessage
{
    public static final String RESPONSE_KEY_PROTOCOL = "IgiNetResponseKey";

    public static void attachRespondKeyToNbt(NBTTagCompound nbt, String key)
    {
        nbt.setString(RESPONSE_KEY_PROTOCOL, key);
    }

    private NBTTagCompound nbt;

    public RespondNbtPacket() { }

    public RespondNbtPacket(NBTTagCompound nbt)
    {
        this.nbt = nbt;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        nbt = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, nbt);
    }

    public static class Handler implements IMessageHandler<RespondNbtPacket, IMessage>
    {
        @Override
        public IMessage onMessage(RespondNbtPacket message, MessageContext ctx)
        {
            if (!ctx.side.isClient()) return null;

            if (message.nbt.hasKey(RESPONSE_KEY_PROTOCOL))
                IgiNetwork.pushNbtResponse(message.nbt.getString(RESPONSE_KEY_PROTOCOL), message.nbt);

            return null;
        }
    }
}
