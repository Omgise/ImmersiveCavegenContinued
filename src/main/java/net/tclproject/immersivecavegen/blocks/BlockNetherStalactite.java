package net.tclproject.immersivecavegen.blocks;

import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import java.util.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;

public class BlockNetherStalactite extends BlockBaseStalactite
{
    public BlockNetherStalactite() {
        super(Item.func_150898_a(Blocks.field_150347_e));
        this.func_149663_c("netherStalactiteBlock");
        this.func_149658_d(":netherDecoration");
    }

    @SideOnly(Side.CLIENT)
    public void func_149734_b(final World world, final int x, final int y, final int z, final Random random) {
        if (world.func_72805_g(x, y, z) < 4) {
            final boolean isWatered = world.func_147439_a(x, y + 2, z).func_149688_o().func_76224_d();
            for (int h = y; world.func_147439_a(x, h, z) == this; --h) {
                if (random.nextInt(5 + (isWatered ? 0 : 10)) == 0) {
                    final double d0 = x + random.nextFloat();
                    final double d2 = z + random.nextFloat();
                    final double d3 = h + 0.05 + (d0 - x) * (d2 - z);
                    world.func_72869_a("dripLava", d0, d3, d2, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @Override
    public void func_149670_a(final World world, final int x, final int y, final int z, final Entity entity) {
        if (entity.func_70089_S()) {
            entity.func_70097_a(DamageSource.field_76372_a, 1.0f);
            entity.func_70015_d(3);
        }
        if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).field_71075_bZ.field_75098_d || !((EntityPlayer)entity).field_71075_bZ.field_75100_b) {
            entity.field_70159_w *= 0.7;
            entity.field_70179_y *= 0.7;
        }
    }
}
