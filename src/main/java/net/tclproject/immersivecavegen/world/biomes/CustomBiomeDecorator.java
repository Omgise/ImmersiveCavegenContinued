package net.tclproject.immersivecavegen.world.biomes;

import net.minecraft.world.biome.*;
import net.minecraft.world.*;
import java.util.*;
import net.minecraft.init.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.block.*;

public class CustomBiomeDecorator extends BiomeDecorator
{
    public static WorldGenerator sand;
    public static WorldGenerator ice;
    public static WorldGenerator water;
    public static WorldGenerator lava;
    public static WorldGenerator obsidian;
    public static WorldGenerator netherrack;
    public static WorldGenerator stone;
    public static WorldGenerator dirt;

    public CustomBiomeDecorator(final BiomeGenBase biomeGenBase, final int trees, final int grass, final int flowers, final int plants) {
        this.field_76802_A = flowers;
        this.field_76805_H = 0;
        this.field_76801_G = 15;
        this.field_76808_K = false;
    }

    public CustomBiomeDecorator(final BiomeGenBase biomeGenBase, final int trees, final int grass, final int flowers) {
        this(biomeGenBase, trees, grass, flowers, 0);
    }

    public void func_150512_a(final World world, final Random random, final BiomeGenBase biome, final int x, final int z) {
        if (this.field_76815_a != null) {
            throw new RuntimeException("The biome is already being decorated! This is occuring at:" + x + "," + z);
        }
        super.func_150512_a(world, random, biome, x, z);
        this.field_76815_a = world;
        this.field_76813_b = random;
        this.field_76814_c = x;
        this.field_76811_d = z;
        this.field_76815_a = null;
        this.field_76813_b = null;
    }

    public void generateOre(final World world, final Random random, final int x, final int z, final int timesPerChunk, final WorldGenerator oreGen, final int minH, final int maxH) {
        for (int var5 = 0; var5 < timesPerChunk; ++var5) {
            final int var6 = x + random.nextInt(16);
            final int var7 = random.nextInt(maxH - minH) + minH;
            final int var8 = z + random.nextInt(16);
            oreGen.func_76484_a(world, random, var6, var7, var8);
        }
    }

    static {
        CustomBiomeDecorator.sand = (WorldGenerator)new WorldGenMinable((Block)Blocks.field_150354_m, 32);
        CustomBiomeDecorator.netherrack = (WorldGenerator)new WorldGenMinable(Blocks.field_150424_aL, 64);
        CustomBiomeDecorator.ice = (WorldGenerator)new WorldGenMinable(Blocks.field_150432_aD, 32);
        CustomBiomeDecorator.water = new MinableWorldGenerator(Blocks.field_150355_j, 4);
        CustomBiomeDecorator.lava = new MinableWorldGenerator(Blocks.field_150353_l, 8);
        CustomBiomeDecorator.dirt = new MinableWorldGenerator(Blocks.field_150346_d, 72, (Block)Blocks.field_150354_m);
        CustomBiomeDecorator.stone = new MinableWorldGenerator(Blocks.field_150348_b, 72, Blocks.field_150346_d);
        CustomBiomeDecorator.obsidian = (WorldGenerator)new WorldGenMinable(Blocks.field_150343_Z, 8);
    }
}
