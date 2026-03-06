package net.tclproject.immersivecavegen.world.biomes.caves;

import net.minecraft.world.gen.feature.*;
import net.minecraft.world.*;
import java.util.*;
import net.tclproject.immersivecavegen.*;
import net.minecraft.init.*;
import net.tclproject.immersivecavegen.world.*;

public final class GenerateJungleCaves extends WorldGenerator
{
    public boolean func_76484_a(final World world, final Random random, final int x, final int y, final int z) {
        switch (CavesDecorator.weightedChoice(WGConfig.jcaveslist)) {
            case 1: {
                CavesDecorator.generateGlowcaps(world, random, x, y, z);
                generateLiliesJungle(world, random, x, y, z);
                return true;
            }
            case 2: {
                CavesDecorator.generateVines(world, random, x, y, z);
                generateLiliesJungle(world, random, x, y, z);
                return true;
            }
            case 3: {
                if (world.func_147439_a(x, y + 1, z).func_149721_r()) {
                    world.func_147465_d(x, y, z, Blocks.field_150321_G, 0, 3);
                }
                generateLiliesJungle(world, random, x, y, z);
                return true;
            }
            case 4: {
                CavesDecorator.generateMushrooms(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z));
                generateLiliesJungle(world, random, x, y, z);
                return true;
            }
            case 5: {
                CavesDecorator.generateSmallMushrooms(world, random, x, y, z);
                generateLiliesJungle(world, random, x, y, z);
                return true;
            }
            default: {
                new GenerateStoneStalactite().generate(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z), 8);
                generateLiliesJungle(world, random, x, y, z);
                return true;
            }
        }
    }

    public static void generateLiliesJungle(final World world, final Random random, final int x, final int y, final int z) {
        if (WGConfig.liliesChance > random.nextFloat()) {
            if (WGConfig.glowLilies == 1 || WGConfig.glowLilies == 2) {
                CavesDecorator.generateGlowLily(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z));
            }
            else if (WGConfig.glowLilies == 3) {
                CavesDecorator.generateLily(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z));
            }
        }
    }
}
