package net.tclproject.immersivecavegen.misc;

import net.minecraft.creativetab.*;
import net.tclproject.immersivecavegen.items.*;
import net.minecraft.item.*;

public final class GamemodeTab extends CreativeTabs
{
    public static CreativeTabs tabCaves;

    public GamemodeTab(final String lable) {
        super(lable);
    }

    public Item func_78016_d() {
        return new ItemStack(ItemInit.itemTabPlaceholder).func_77973_b();
    }

    static {
        GamemodeTab.tabCaves = new GamemodeTab("caves");
    }
}
