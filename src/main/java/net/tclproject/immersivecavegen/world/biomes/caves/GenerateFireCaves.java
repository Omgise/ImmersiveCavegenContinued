package net.tclproject.immersivecavegen.world.biomes.caves;

import net.minecraft.world.gen.feature.*;
import net.minecraft.world.*;
import java.util.*;
import net.tclproject.immersivecavegen.*;
import net.tclproject.immersivecavegen.world.*;

public class GenerateFireCaves extends WorldGenerator
{
    public boolean func_76484_a(final World world, final Random random, final int x, final int y, final int z) {
        switch (CavesDecorator.weightedChoice(WGConfig.fcaveslist)) {
            case 1: {
                new GenerateNetherStalactites().generate(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z), 8);
                generateLiliesOther(world, random, x, y, z);
                return true;
            }
            case 2: {
                CavesDecorator.generateLavashrooms(world, random, x, y, z);
                generateLiliesOther(world, random, x, y, z);
                return true;
            }
            case 3: {
                CavesDecorator.generateScorchedLavaStone(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z));
                generateLiliesOther(world, random, x, y, z);
                return true;
            }
            case 4: {
                CavesDecorator.generateSkulls(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z));
                generateLiliesOther(world, random, x, y, z);
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public static void generateLiliesOther(final World world, final Random random, final int x, final int y, final int z) {
        if (WGConfig.liliesChance > random.nextFloat()) {
            if (WGConfig.glowLilies == 2) {
                CavesDecorator.generateGlowLily(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z));
            }
            else if (WGConfig.glowLilies == 3 || WGConfig.glowLilies == 1) {
                CavesDecorator.generateLily(world, random, x, y, z, CavesDecorator.getNumEmptyBlocks(world, x, y, z));
            }
        }
    }
}
