package net.tclproject.immersivecavegen.misc;

import net.minecraft.init.*;
import cpw.mods.fml.common.registry.*;
import net.minecraft.item.crafting.*;
import net.tclproject.immersivecavegen.items.*;
import net.tclproject.immersivecavegen.blocks.*;
import net.minecraft.item.*;
import net.minecraftforge.oredict.*;

public final class CraftInit
{
    public static final void init() {
        GameRegistry.addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack(Blocks.field_150321_G, 4), new Object[] { "S S", " B ", "S S", 'S', Items.field_151007_F, 'B', Items.field_151123_aH }));
        GameRegistry.addRecipe((IRecipe)new ShapedOreRecipe(new ItemStack(ItemInit.itemStalactiteDagger), new Object[] { "S", "W", 'S', BlockInit.stoneStalactiteBlock, 'W', Items.field_151055_y }));
        GameRegistry.addRecipe((IRecipe)new ShapelessOreRecipe(new ItemStack(Item.func_150898_a(BlockInit.ceilingVine)), new Object[] { new ItemStack(Blocks.field_150395_bd), Items.field_151055_y }));
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                GameRegistry.addRecipe((IRecipe)new ShapelessOreRecipe(new ItemStack(ItemInit.glowSoup, 1, 0), new Object[] { new ItemStack(BlockInit.cavePlantBlock, 2, 2 + i), new ItemStack(BlockInit.cavePlantBlock, 2, 2 + j), Items.field_151054_z }));
            }
        }
    }
}
