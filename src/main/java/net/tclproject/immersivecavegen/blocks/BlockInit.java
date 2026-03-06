package net.tclproject.immersivecavegen.blocks;

import cpw.mods.fml.common.registry.*;
import net.minecraft.block.*;
import net.tclproject.immersivecavegen.misc.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.tclproject.immersivecavegen.items.*;
import net.minecraft.block.material.*;

public class BlockInit
{
    public static Block cavePlantBlock;
    public static Block iceStalactiteBlock;
    public static Block stoneStalactiteBlock;
    public static Block netherStalactiteBlock;
    public static Block deepslateStalactiteBlock;
    public static Block scorchedStone;
    public static Block scorchedLavaStone;
    public static Block sandStalactiteBlock;
    public static Block glowLily;
    public static Block glowLilyBlue;
    public static Block ceilingVine;
    public static Block mushroomBlockBlue;
    public static Block mushroomBlockGreen;

    public static void init() {
        BlockInit.glowLily = GameRegistry.registerBlock((Block)new BlockGlowlily(), "GlowLily").func_149658_d("immersivecavegen:glowlily");
        BlockInit.glowLilyBlue = GameRegistry.registerBlock((Block)new BlockGlowlily(), "GlowLilyBlue").func_149658_d("immersivecavegen:glowlily2");
        BlockInit.ceilingVine = GameRegistry.registerBlock((Block)new BlockCeilingVine(), "CeilingVine").func_149663_c("ceilingVine").func_149711_c(0.2f).func_149672_a(Block.field_149779_h).func_149658_d("immersivecavegen:greenvine");
        BlockInit.scorchedStone = GameRegistry.registerBlock((Block)new BlockStone(), "ScorchedStone").func_149663_c("scorchedStoneBlock").func_149647_a(GamemodeTab.tabCaves).func_149711_c(1.5f).func_149752_b(10.0f).func_149672_a(Block.field_149780_i).func_149658_d("immersivecavegen:scorched_stone");
        BlockInit.scorchedLavaStone = GameRegistry.registerBlock((Block)new ScorchedLavaStone(), "ScorchedLavaStone");
        BlockInit.stoneStalactiteBlock = GameRegistry.registerBlock((Block)new BlockStoneStalactite(), (Class)ItemStalactite.class, "StoneStalactite");
        BlockInit.netherStalactiteBlock = GameRegistry.registerBlock((Block)new BlockNetherStalactite(), (Class)ItemStalactite.class, "NetherStalactite");
        BlockInit.deepslateStalactiteBlock = GameRegistry.registerBlock((Block)new BlockDeepslateStalactite(), (Class)ItemStalactite.class, "DeepslateStalactite");
        BlockInit.sandStalactiteBlock = GameRegistry.registerBlock(new BlockBaseStalactite(Item.func_150898_a(Blocks.field_150322_A)).func_149663_c("sandStalactiteBlock").func_149658_d(":sandDecoration"), (Class)ItemStalactite.class, "SandstoneSalactite");
        BlockInit.iceStalactiteBlock = GameRegistry.registerBlock((Block)new BlockIceStalactite(), (Class)MultiItemBlock.class, "iceStalactite");
        BlockInit.cavePlantBlock = GameRegistry.registerBlock(new BlockCavePlant().func_149715_a(6.0f), (Class)MultiItemBlock.class, "cavePlant");
        BlockInit.mushroomBlockBlue = GameRegistry.registerBlock((Block)new BlockHugeGlowingMushroom(Material.field_151575_d, 0), "glowMushroomBlue").func_149647_a(GamemodeTab.tabCaves);
        BlockInit.mushroomBlockGreen = GameRegistry.registerBlock((Block)new BlockHugeGlowingMushroom2(Material.field_151575_d, 1), "glowMushroomGreen").func_149647_a(GamemodeTab.tabCaves);
    }
}
