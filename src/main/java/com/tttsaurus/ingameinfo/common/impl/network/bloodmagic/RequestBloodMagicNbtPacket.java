package com.tttsaurus.ingameinfo.common.impl.network.bloodmagic;

import WayofTime.bloodmagic.core.data.SoulNetwork;
import WayofTime.bloodmagic.util.helper.NetworkHelper;
import com.tttsaurus.ingameinfo.common.impl.network.IgiNetwork;
import com.tttsaurus.ingameinfo.common.impl.network.common.RespondNbtPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RequestBloodMagicNbtPacket implements IMessage
{
    public static final String RESPONSE_KEY = "BloodMagicNbt";

    public RequestBloodMagicNbtPacket() { }

    @Override
    public void fromBytes(ByteBuf buf) { }

    @Override
    public void toBytes(ByteBuf buf) { }

    public static class Handler implements IMessageHandler<RequestBloodMagicNbtPacket, IMessage>
    {
        @Override
        public IMessage onMessage(RequestBloodMagicNbtPacket message, MessageContext ctx)
        {
            if (!ctx.side.isServer()) return null;

            MinecraftServer server = ctx.getServerHandler().player.getServerWorld().getMinecraftServer();
            if (server == null) return null;
            if (!server.isServerRunning()) return null;

            NBTTagCompound nbt = new NBTTagCompound();
            RespondNbtPacket.attachRespondKeyToNbt(nbt, RESPONSE_KEY);
            EntityPlayerMP player = ctx.getServerHandler().player;
            server.addScheduledTask(() ->
            {
                SoulNetwork soulNetwork = NetworkHelper.getSoulNetwork(player);
                nbt.setInteger("CurrentEssence", soulNetwork.getCurrentEssence());
                nbt.setInteger("OrbTier", soulNetwork.getOrbTier());
                IgiNetwork.NETWORK.sendTo(new RespondNbtPacket(nbt), player);
            });

            return null;
        }
    }
}
