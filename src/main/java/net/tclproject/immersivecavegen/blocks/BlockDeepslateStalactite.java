package net.tclproject.immersivecavegen.blocks;

import net.minecraft.init.*;
import net.minecraft.item.*;
import java.util.*;
import ganymedes01.etfuturum.*;
import cpw.mods.fml.common.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.tclproject.immersivecavegen.entities.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;

public class BlockDeepslateStalactite extends BlockBaseStalactite
{
    public BlockDeepslateStalactite() {
        super(Item.func_150898_a(Blocks.field_150347_e));
        this.func_149663_c("deepslateStalactiteBlock");
        this.func_149658_d(":deepslateDecoration");
        this.func_149711_c(1.5f);
        this.func_149752_b(3.0f);
    }

    @Optional.Method(modid = "etfuturum")
    @Override
    public Item func_149650_a(final int metadata, final Random random, final int par3) {
        return Item.func_150898_a(ModBlocks.deepslate);
    }

    @Override
    public void func_149670_a(final World world, final int x, final int y, final int z, final Entity entity) {
        if (entity.func_70089_S() && !(entity instanceof ICaveEntity)) {
            entity.func_70097_a(DamageSource.field_76367_g, 2.5f);
        }
        if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).field_71075_bZ.field_75098_d || !((EntityPlayer)entity).field_71075_bZ.field_75100_b) {
            entity.field_70159_w *= 0.7;
            entity.field_70179_y *= 0.7;
        }
    }
}
