package net.tclproject.mysteriumlib.network;

import net.minecraft.nbt.*;
import io.netty.buffer.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.network.simpleimpl.*;
import net.minecraft.init.*;

public class BlockWebDestroy implements IMessage
{
    private NBTTagCompound data;

    public BlockWebDestroy() {
    }

    public BlockWebDestroy(final int x, final int y, final int z) {
        (this.data = new NBTTagCompound()).func_74768_a("x", x);
        this.data.func_74768_a("y", y);
        this.data.func_74768_a("z", z);
    }

    public void fromBytes(final ByteBuf buffer) {
        this.data = ByteBufUtils.readTag(buffer);
    }

    public void toBytes(final ByteBuf buffer) {
        ByteBufUtils.writeTag(buffer, this.data);
    }

    public static class Handler<T extends BlockWebDestroy> implements IMessageHandler<T, BlockWebDestroy>
    {
        public BlockWebDestroy onMessage(final T message, final MessageContext ctx) {
            if (message instanceof BlockWebDestroy) {
                ctx.getServerHandler().field_147369_b.field_70170_p.func_147465_d(message.data.func_74762_e("x"), message.data.func_74762_e("y"), message.data.func_74762_e("z"), Blocks.field_150350_a, 0, 3);
            }
            return null;
        }
    }
}
