package net.tclproject.immersivecavegen.items;

import net.minecraft.item.*;
import cpw.mods.fml.common.registry.*;
import net.tclproject.immersivecavegen.misc.*;
import net.minecraftforge.common.util.*;

public final class ItemInit
{
    public static Item itemTabPlaceholder;
    public static Item glowSoup;
    public static Item itemStalactiteDagger;
    public static Item itemStalagmiteDagger;
    public static Item itemSpawnSpSm;
    public static Item itemSpawnSp;
    public static Item itemSpawnSpL;
    public static Item itemSpawnGlSl;
    public static Item.ToolMaterial stalactiteMat;
    public static Item.ToolMaterial stalagmiteMat;

    public static final void init() {
        GameRegistry.registerItem(ItemInit.itemTabPlaceholder = new ItemTabPlaceholder().func_77655_b("ItemTabPlaceholder").func_111206_d("immersivecavegen:tab_icon"), "ItemTabPlaceholder");
        GameRegistry.registerItem(ItemInit.itemStalactiteDagger = new ItemStalactiteDagger(ItemInit.stalactiteMat).func_77655_b("ItemStalactiteDagger").func_111206_d("immersivecavegen:stalactite_dagger").func_77637_a(GamemodeTab.tabCaves), "ItemStalactiteDagger");
        GameRegistry.registerItem(ItemInit.glowSoup = new GlowSoup(8).func_77655_b("glowSoup").func_111206_d("immersivecavegen:glow_soup").func_77637_a(GamemodeTab.tabCaves), "ItemGlowSoup");
        GameRegistry.registerItem(ItemInit.itemSpawnSpSm = new SpawnEggBrownSpiderSmall().func_77655_b("SpawnSmallBrownSpider").func_111206_d("immersivecavegen:spawn_egg_brownspidersmall").func_77637_a(GamemodeTab.tabCaves), "SpawnSmallBrownSpider");
        GameRegistry.registerItem(ItemInit.itemSpawnSp = new SpawnEggBrownSpider().func_77655_b("SpawnBrownSpider").func_111206_d("immersivecavegen:spawn_egg_brownspider").func_77637_a(GamemodeTab.tabCaves), "SpawnBrownSpider");
        GameRegistry.registerItem(ItemInit.itemSpawnSpL = new SpawnEggBrownSpiderLarge().func_77655_b("SpawnLargeBrownSpider").func_111206_d("immersivecavegen:spawn_egg_brownspiderlarge").func_77637_a(GamemodeTab.tabCaves), "SpawnLargeBrownSpider");
        GameRegistry.registerItem(ItemInit.itemSpawnGlSl = new SpawnEggGlowSlime().func_77655_b("SpawnGlowSlime").func_111206_d("immersivecavegen:spawn_egg_glowslime").func_77637_a(GamemodeTab.tabCaves), "SpawnGlowSlime");
    }

    static {
        ItemInit.stalactiteMat = EnumHelper.addToolMaterial("Stalactite", 0, 89, 4.0f, 1.3f, 16);
        ItemInit.stalagmiteMat = EnumHelper.addToolMaterial("Stalagmite", 0, 112, 4.0f, 1.5f, 10);
    }
}
