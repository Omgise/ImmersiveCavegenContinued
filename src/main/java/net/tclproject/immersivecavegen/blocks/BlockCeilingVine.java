package net.tclproject.immersivecavegen.blocks;

import net.tclproject.immersivecavegen.misc.*;
import net.minecraft.world.*;
import net.minecraft.block.*;

public class BlockCeilingVine extends BlockVine
{
    public BlockCeilingVine() {
        this.func_149647_a(GamemodeTab.tabCaves);
    }

    public boolean func_149707_d(final World p_149707_1_, final int p_149707_2_, final int p_149707_3_, final int p_149707_4_, final int p_149707_5_) {
        switch (p_149707_5_) {
            case 1: {
                return this.func_150093_a(p_149707_1_.func_147439_a(p_149707_2_, p_149707_3_ + 1, p_149707_4_));
            }
            case 2: {
                return this.func_150093_a(p_149707_1_.func_147439_a(p_149707_2_, p_149707_3_, p_149707_4_ + 1));
            }
            case 3: {
                return this.func_150093_a(p_149707_1_.func_147439_a(p_149707_2_, p_149707_3_, p_149707_4_ - 1));
            }
            case 4: {
                return this.func_150093_a(p_149707_1_.func_147439_a(p_149707_2_ + 1, p_149707_3_, p_149707_4_));
            }
            case 5: {
                return this.func_150093_a(p_149707_1_.func_147439_a(p_149707_2_ - 1, p_149707_3_, p_149707_4_));
            }
            case 0: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public boolean func_150093_a(final Block p_150093_1_) {
        return p_150093_1_.func_149686_d() && p_150093_1_.func_149688_o().func_76230_c();
    }
}
