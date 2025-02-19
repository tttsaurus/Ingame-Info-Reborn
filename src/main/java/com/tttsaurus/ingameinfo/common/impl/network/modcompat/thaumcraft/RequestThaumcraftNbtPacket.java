package com.tttsaurus.ingameinfo.common.impl.network.modcompat.thaumcraft;

import com.tttsaurus.ingameinfo.common.impl.network.IgiNetwork;
import com.tttsaurus.ingameinfo.common.impl.network.common.RespondNbtPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import thaumcraft.api.aura.AuraHelper;

public class RequestThaumcraftNbtPacket implements IMessage
{
    public static final String RESPONSE_KEY = "ThaumcraftNbt";

    public RequestThaumcraftNbtPacket() { }

    @Override
    public void fromBytes(ByteBuf buf) { }

    @Override
    public void toBytes(ByteBuf buf) { }

    public static class Handler implements IMessageHandler<RequestThaumcraftNbtPacket, IMessage>
    {
        @Override
        public IMessage onMessage(RequestThaumcraftNbtPacket message, MessageContext ctx)
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
                nbt.setFloat("LocalVis", AuraHelper.getVis(player.world, player.getPosition()));
                nbt.setFloat("LocalFlux", AuraHelper.getFlux(player.world, player.getPosition()));
                nbt.setInteger("LocalAuraBase", AuraHelper.getAuraBase(player.world, player.getPosition()));
                IgiNetwork.NETWORK.sendTo(new RespondNbtPacket(nbt), player);
            });

            return null;
        }
    }
}
