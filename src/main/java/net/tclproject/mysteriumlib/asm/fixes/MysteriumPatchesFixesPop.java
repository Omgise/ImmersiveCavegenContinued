package net.tclproject.mysteriumlib.asm.fixes;

import java.util.*;
import net.tclproject.immersivecavegen.*;
import net.minecraft.block.material.*;
import net.tclproject.mysteriumlib.asm.annotations.*;
import net.minecraft.world.chunk.*;
import net.minecraft.block.*;
import net.minecraftforge.common.*;
import cpw.mods.fml.common.eventhandler.*;
import net.minecraft.world.biome.*;
import net.minecraft.init.*;
import net.tclproject.immersivecavegen.world.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.*;
import net.minecraftforge.event.terraingen.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.structure.*;
import cpw.mods.fml.common.*;

public class MysteriumPatchesFixesPop
{
    public static Random lakeRand;

    @Fix(returnSetting = EnumReturnSetting.ON_TRUE, booleanAlwaysReturned = false)
    public static boolean generate(final WorldGenLiquids l, final World world, final Random random, final int x, final int y, final int z) {
        for (final String str : WGConfig.dimblacklist) {
            if (String.valueOf(world.field_73011_w.field_76574_g).equalsIgnoreCase(str)) {
                return false;
            }
        }
        final boolean type = l.field_150521_a.func_149688_o() == Material.field_151586_h;
        return (WGConfig.disableSourceWater && type && (!WGConfig.disableSourceUnderground || y < 64)) || (WGConfig.disableSourceLava && !type && (!WGConfig.disableSourceUnderground || y < 64));
    }

    @Fix(returnSetting = EnumReturnSetting.ON_TRUE)
    public static boolean populate(final ChunkProviderGenerate g, final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
        int k = p_73153_2_ * 16;
        int l = p_73153_3_ * 16;
        boolean flag = false;
        final World world = (World)DimensionManager.getWorld(0);
        if (world == null) {
            return true;
        }
        for (final String str : WGConfig.dimblacklist) {
            if (String.valueOf(world.field_73011_w.field_76574_g).equalsIgnoreCase(str)) {
                return false;
            }
        }
        BlockFalling.field_149832_M = true;
        BiomeGenBase biomegenbase;
        try {
            biomegenbase = world.getBiomeGenForCoordsBody(k + 16, l + 16);
        }
        catch (Exception e2) {
            biomegenbase = null;
        }
        g.field_73220_k.setSeed(world.func_72905_C());
        final long i1 = g.field_73220_k.nextLong() / 2L * 2L + 1L;
        final long j1 = g.field_73220_k.nextLong() / 2L * 2L + 1L;
        g.field_73220_k.setSeed(p_73153_2_ * i1 + p_73153_3_ * j1 ^ world.func_72905_C());
        MysteriumPatchesFixesPop.lakeRand.setSeed(p_73153_2_ * i1 + p_73153_3_ * j1 ^ world.func_72905_C());
        MinecraftForge.EVENT_BUS.post((Event)new PopulateChunkEvent.Pre(p_73153_1_, world, g.field_73220_k, p_73153_2_, p_73153_3_, flag));
        if (world.func_72912_H().func_76089_r()) {
            try {
                if (WGConfig.enableMineshaftSpawn) {
                    g.field_73223_w.func_75051_a(world, g.field_73220_k, p_73153_2_, p_73153_3_);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            try {
                flag = (WGConfig.enableVillageSpawn && g.field_73224_v.func_75051_a(world, g.field_73220_k, p_73153_2_, p_73153_3_));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (WGConfig.enableStrongholdSpawn) {
                    g.field_73225_u.func_75051_a(world, g.field_73220_k, p_73153_2_, p_73153_3_);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!WGConfig.enableDesolateSpawn) {
                    g.field_73233_x.func_75051_a(world, g.field_73220_k, p_73153_2_, p_73153_3_);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (biomegenbase != BiomeGenBase.field_76769_d && biomegenbase != BiomeGenBase.field_76786_s && !flag && g.field_73220_k.nextInt(WGConfig.waterLakesChance) == 0 && TerrainGen.populate(p_73153_1_, world, g.field_73220_k, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.LAKE)) {
            int k2 = k + g.field_73220_k.nextInt(16) + 8;
            int l2 = g.field_73220_k.nextInt(256);
            int i2 = l + g.field_73220_k.nextInt(16) + 8;
            new WorldGenLakes(Blocks.field_150355_j).func_76484_a(world, g.field_73220_k, k2, l2, i2);
            if (MysteriumPatchesFixesPop.lakeRand.nextInt(WGConfig.waterLakesChance) == 0) {
                k2 = k + MysteriumPatchesFixesPop.lakeRand.nextInt(16) + 8;
                l2 = MysteriumPatchesFixesPop.lakeRand.nextInt(256);
                i2 = l + MysteriumPatchesFixesPop.lakeRand.nextInt(16) + 8;
                new WorldGenLakes(Blocks.field_150355_j).func_76484_a(world, MysteriumPatchesFixesPop.lakeRand, k2, l2, i2);
            }
            if (MysteriumPatchesFixesPop.lakeRand.nextInt(WGConfig.waterLakesChance) == 0) {
                k2 = k + MysteriumPatchesFixesPop.lakeRand.nextInt(16) + 8;
                l2 = MysteriumPatchesFixesPop.lakeRand.nextInt(256);
                i2 = l + MysteriumPatchesFixesPop.lakeRand.nextInt(16) + 8;
                new WorldGenLakes(Blocks.field_150355_j).func_76484_a(world, MysteriumPatchesFixesPop.lakeRand, k2, l2, i2);
            }
        }
        if (TerrainGen.populate(p_73153_1_, world, g.field_73220_k, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.LAVA) && !flag) {
            int k2 = k + g.field_73220_k.nextInt(16) + 8;
            int l2 = g.field_73220_k.nextInt(g.field_73220_k.nextInt(248) + 8);
            int i2 = l + g.field_73220_k.nextInt(16) + 8;
            if (l2 < 63 || g.field_73220_k.nextInt(WGConfig.lavaLakesChance) == 0) {
                new WorldGenLakes(Blocks.field_150353_l).func_76484_a(world, g.field_73220_k, k2, l2, i2);
            }
            else if (MysteriumPatchesFixesPop.lakeRand.nextInt(WGConfig.undergLavaLakesChance) == 0) {
                new WorldGenLakes(Blocks.field_150353_l).func_76484_a(world, MysteriumPatchesFixesPop.lakeRand, k2, l2, i2);
            }
            k2 = k + MysteriumPatchesFixesPop.lakeRand.nextInt(16) + 8;
            l2 = MysteriumPatchesFixesPop.lakeRand.nextInt(g.field_73220_k.nextInt(248) + 8);
            i2 = l + MysteriumPatchesFixesPop.lakeRand.nextInt(16) + 8;
            if (l2 < 63 || MysteriumPatchesFixesPop.lakeRand.nextInt(WGConfig.lavaLakesChance) == 0) {
                new WorldGenLakes(Blocks.field_150353_l).func_76484_a(world, MysteriumPatchesFixesPop.lakeRand, k2, l2, i2);
            }
            else if (MysteriumPatchesFixesPop.lakeRand.nextInt(WGConfig.undergLavaLakesChance) == 0) {
                new WorldGenLakes(Blocks.field_150353_l).func_76484_a(world, MysteriumPatchesFixesPop.lakeRand, k2, l2, i2);
            }
            k2 = k + MysteriumPatchesFixesPop.lakeRand.nextInt(16) + 8;
            l2 = MysteriumPatchesFixesPop.lakeRand.nextInt(g.field_73220_k.nextInt(248) + 8);
            i2 = l + MysteriumPatchesFixesPop.lakeRand.nextInt(16) + 8;
            if (l2 < 63 || MysteriumPatchesFixesPop.lakeRand.nextInt(WGConfig.lavaLakesChance) == 0) {
                new WorldGenLakes(Blocks.field_150353_l).func_76484_a(world, MysteriumPatchesFixesPop.lakeRand, k2, l2, i2);
            }
            else if (MysteriumPatchesFixesPop.lakeRand.nextInt(WGConfig.undergLavaLakesChance) == 0) {
                new WorldGenLakes(Blocks.field_150353_l).func_76484_a(world, MysteriumPatchesFixesPop.lakeRand, k2, l2, i2);
            }
        }
        if (TerrainGen.populate(p_73153_1_, world, g.field_73220_k, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.LAVA)) {
            int k2 = k + g.field_73220_k.nextInt(16) + 8;
            int l2 = g.field_73220_k.nextInt(g.field_73220_k.nextInt(248) + 8);
            int i2 = l + g.field_73220_k.nextInt(16) + 8;
            if (l2 < 63 || g.field_73220_k.nextInt(WGConfig.waterLakesChance) == 0) {
                new WorldGenLakesUnderground(Blocks.field_150353_l).func_76484_a(world, g.field_73220_k, k2, l2, i2);
            }
            else if (g.field_73220_k.nextInt(WGConfig.undergWaterLakesChance) == 0) {
                new WorldGenLakesUnderground(Blocks.field_150353_l).func_76484_a(world, g.field_73220_k, k2, l2, i2);
            }
            k2 = k + g.field_73220_k.nextInt(16) + 8;
            l2 = g.field_73220_k.nextInt(g.field_73220_k.nextInt(248) + 8);
            i2 = l + g.field_73220_k.nextInt(16) + 8;
            if (l2 < 63 || g.field_73220_k.nextInt(WGConfig.waterLakesChance) == 0) {
                new WorldGenLakesUnderground(Blocks.field_150353_l).func_76484_a(world, g.field_73220_k, k2, l2, i2);
            }
            else if (g.field_73220_k.nextInt(WGConfig.undergWaterLakesChance) == 0) {
                new WorldGenLakesUnderground(Blocks.field_150353_l).func_76484_a(world, g.field_73220_k, k2, l2, i2);
            }
            k2 = k + g.field_73220_k.nextInt(16) + 8;
            l2 = g.field_73220_k.nextInt(g.field_73220_k.nextInt(248) + 8);
            i2 = l + g.field_73220_k.nextInt(16) + 8;
            if (l2 < 63 || g.field_73220_k.nextInt(WGConfig.waterLakesChance) == 0) {
                new WorldGenLakesUnderground(Blocks.field_150353_l).func_76484_a(world, g.field_73220_k, k2, l2, i2);
            }
            else if (g.field_73220_k.nextInt(WGConfig.undergWaterLakesChance) == 0) {
                new WorldGenLakesUnderground(Blocks.field_150353_l).func_76484_a(world, g.field_73220_k, k2, l2, i2);
            }
        }
        int k2;
        int l2;
        int i2;
        boolean doGen;
        int j2;
        for (doGen = TerrainGen.populate(p_73153_1_, world, g.field_73220_k, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.DUNGEON), k2 = 0; doGen && k2 < 8; ++k2) {
            l2 = k + g.field_73220_k.nextInt(16) + 8;
            i2 = g.field_73220_k.nextInt(256);
            j2 = l + g.field_73220_k.nextInt(16) + 8;
            if (WGConfig.enableDungeonSpawn) {
                new WorldGenDungeons().func_76484_a(world, g.field_73220_k, l2, i2, j2);
            }
        }
        if (biomegenbase != null) {
            biomegenbase.func_76728_a(world, g.field_73220_k, k, l);
            if (TerrainGen.populate(p_73153_1_, world, g.field_73220_k, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.ANIMALS)) {
                SpawnerAnimals.func_77191_a(world, biomegenbase, k + 8, l + 8, 16, 16, g.field_73220_k);
            }
        }
        k += 8;
        l += 8;
        for (doGen = TerrainGen.populate(p_73153_1_, world, g.field_73220_k, p_73153_2_, p_73153_3_, flag, PopulateChunkEvent.Populate.EventType.ICE), k2 = 0; doGen && k2 < 16; ++k2) {
            for (int l2 = 0; l2 < 16; ++l2) {
                i2 = world.func_72874_g(k + k2, l + l2);
                if (world.func_72884_u(k2 + k, i2 - 1, l2 + l)) {
                    world.func_147465_d(k2 + k, i2 - 1, l2 + l, Blocks.field_150432_aD, 0, 2);
                }
                if (world.func_147478_e(k2 + k, i2, l2 + l, true)) {
                    world.func_147465_d(k2 + k, i2, l2 + l, Blocks.field_150431_aC, 0, 2);
                }
            }
        }
        MinecraftForge.EVENT_BUS.post((Event)new PopulateChunkEvent.Post(p_73153_1_, world, g.field_73220_k, p_73153_2_, p_73153_3_, flag));
        BlockFalling.field_149832_M = false;
        if (Loader.isModLoaded("immersivesnow")) {
            loadImmersiveSnowCompat(g, p_73153_1_, p_73153_2_, p_73153_3_);
        }
        return true;
    }

    @Fix(targetMethod = "<init>", insertOnExit = true)
    public static void Corridor(final StructureMineshaftPieces.Corridor r, final int p_i2035_1_, final Random p_i2035_2_, final StructureBoundingBox p_i2035_3_, final int p_i2035_4_) {
        if (!WGConfig.enableCaveSpiderSpawnerSpawn) {
            r.field_74956_b = false;
        }
        else if (WGConfig.enableCaveSpiderSpawnerSpawnAlways) {
            r.field_74956_b = true;
        }
    }

    @Fix(targetMethod = "<init>", insertOnExit = true)
    public static void ChunkProviderGenerate(final ChunkProviderGenerate gen, final World p_i2006_1_, final long p_i2006_2_, final boolean p_i2006_4_) {
        gen.field_73226_t = TerrainGen.getModdedMapGen(gen.field_73226_t, InitMapGenEvent.EventType.CAVE);
        gen.field_73225_u = (MapGenStronghold)TerrainGen.getModdedMapGen((MapGenBase)gen.field_73225_u, InitMapGenEvent.EventType.STRONGHOLD);
        gen.field_73224_v = (MapGenVillage)TerrainGen.getModdedMapGen((MapGenBase)gen.field_73224_v, InitMapGenEvent.EventType.VILLAGE);
        gen.field_73223_w = (MapGenMineshaft)TerrainGen.getModdedMapGen((MapGenBase)gen.field_73223_w, InitMapGenEvent.EventType.MINESHAFT);
        gen.field_73233_x = (MapGenScatteredFeature)TerrainGen.getModdedMapGen((MapGenBase)gen.field_73233_x, InitMapGenEvent.EventType.SCATTERED_FEATURE);
        gen.field_73232_y = TerrainGen.getModdedMapGen(gen.field_73232_y, InitMapGenEvent.EventType.RAVINE);
    }

    @Optional.Method(modid = "immersivesnow")
    public static void loadImmersiveSnowCompat(final ChunkProviderGenerate t, final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
        final boolean population = MysteriumPatchesFixesSnow.populate(t, p_73153_1_, p_73153_2_, p_73153_3_);
    }

    static {
        MysteriumPatchesFixesPop.lakeRand = new Random();
    }
}
