package net.tclproject.immersivecavegen.items;

import net.minecraft.block.*;
import java.util.*;
import net.minecraft.util.*;
import cpw.mods.fml.relauncher.*;
import net.minecraft.item.*;

public class MultiItemBlock extends ItemBlock
{
    private final Block block;

    public MultiItemBlock(final Block block) {
        super(block);
        this.block = block;
        this.func_77627_a(true);
    }

    public MultiItemBlock(final Block block, final ArrayList names) {
        super(block);
        this.block = block;
        this.func_77627_a(true);
    }

    @SideOnly(Side.CLIENT)
    public IIcon func_77617_a(int damage) {
        if (damage > 26) {
            damage = 0;
        }
        return this.block.func_149691_a(0, damage);
    }

    public int func_77647_b(int damage) {
        if (damage > 26) {
            damage = 0;
        }
        return damage;
    }

    public String func_77667_c(final ItemStack itemstack) {
        return this.func_77658_a() + itemstack.func_77960_j();
    }
}
