package net.tclproject.immersivecavegen.world;

import net.minecraft.world.*;
import cpw.mods.fml.common.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import net.tclproject.immersivecavegen.blocks.*;
import java.util.*;

public class GenerateStoneStalactite
{
    public final Block blockId;
    public static List blockWhiteList;

    public GenerateStoneStalactite() {
        this(BlockInit.stoneStalactiteBlock);
    }

    public GenerateStoneStalactite(final Block toGen) {
        this.blockId = toGen;
    }

    public void generate(final World world, final Random random, final int x, final int y, final int z, final int distance, final int maxLength) {
        boolean deepslateBelow = false;
        boolean deepslateAbove = false;
        if (Loader.isModLoaded("etfuturum")) {
            deepslateBelow = CavesDecorator.isBlockDeepslate(world, x, y - distance, z);
            deepslateAbove = CavesDecorator.isBlockDeepslate(world, x, y + 1, z);
        }
        if (distance <= 1) {
            if (!world.func_147437_c(x, y + 1, z) && !(world.func_147439_a(x, y - 1, z) instanceof BlockLiquid)) {
                if (deepslateAbove && deepslateBelow) {
                    world.func_147465_d(x, y, z, BlockInit.deepslateStalactiteBlock, 0, 3);
                    return;
                }
                world.func_147465_d(x, y, z, this.blockId, 0, 3);
            }
        }
        else {
            int j = 0;
            int topY = y;
            int botY = y - distance + 1;
            if (world.func_147439_a(x, botY - 1, z).func_149721_r() && random.nextInt(10) > 7) {
                if (deepslateBelow) {
                    world.func_147465_d(x, botY, z, BlockInit.deepslateStalactiteBlock, CavesDecorator.randomChoice(9, 10), 3);
                    return;
                }
                world.func_147465_d(x, botY, z, this.blockId, CavesDecorator.randomChoice(9, 10), 3);
                if (this.blockId == BlockInit.sandStalactiteBlock) {
                    if (world.func_147439_a(x, botY - 1, z) == Blocks.field_150348_b) {
                        world.func_147465_d(x, botY - 1, z, Blocks.field_150322_A, 0, 2);
                    }
                    CavesDecorator.convertToSandType(world, random, x, botY, z);
                }
                else if (this.blockId == BlockInit.netherStalactiteBlock) {
                    if (world.func_147439_a(x, botY - 1, z) == Blocks.field_150348_b || world.func_147439_a(x, botY - 1, z) == Blocks.field_150322_A) {
                        world.func_147465_d(x, botY - 1, z, BlockInit.scorchedStone, 0, 2);
                    }
                    CavesDecorator.convertToLavaType(world, random, x, botY, z);
                }
            }
            else if (world.func_147439_a(x, y + 1, z).func_149721_r() && random.nextInt(10) > 7) {
                if (deepslateAbove) {
                    world.func_147465_d(x, topY, z, BlockInit.deepslateStalactiteBlock, CavesDecorator.randomChoice(1, 2), 3);
                    return;
                }
                world.func_147465_d(x, topY, z, this.blockId, CavesDecorator.randomChoice(1, 2), 3);
                if (this.blockId == BlockInit.sandStalactiteBlock) {
                    CavesDecorator.convertToSandType(world, random, x, topY, z);
                }
                else if (this.blockId == BlockInit.netherStalactiteBlock) {
                    CavesDecorator.convertToLavaType(world, random, x, topY, z);
                }
            }
            else {
                boolean gen = false;
                boolean genStala = false;
                if (world.func_147439_a(x, y + 1, z).func_149721_r()) {
                    if (deepslateAbove) {
                        world.func_147465_d(x, y, z, BlockInit.deepslateStalactiteBlock, 3, 3);
                    }
                    else {
                        world.func_147465_d(x, y, z, this.blockId, 3, 3);
                    }
                    genStala = true;
                }
                if (!world.func_147439_a(x, botY, z).func_149688_o().func_76224_d() && deepslateBelow) {
                    final int aux = CavesDecorator.randomChoice(-1, 8);
                    if (aux != -1) {
                        world.func_147465_d(x, botY, z, BlockInit.deepslateStalactiteBlock, aux, 3);
                        gen = true;
                    }
                }
                else if (!world.func_147439_a(x, botY, z).func_149688_o().func_76224_d() && GenerateStoneStalactite.blockWhiteList.contains(world.func_147439_a(x, botY - 1, z))) {
                    final int aux = CavesDecorator.randomChoice(-1, 8);
                    if (aux != -1) {
                        world.func_147465_d(x, botY, z, this.blockId, aux, 3);
                        gen = true;
                        if (this.blockId == BlockInit.sandStalactiteBlock) {
                            if (world.func_147439_a(x, botY - 1, z) == Blocks.field_150348_b) {
                                world.func_147465_d(x, botY - 1, z, Blocks.field_150322_A, 0, 2);
                            }
                            CavesDecorator.convertToSandType(world, random, x, botY, z);
                        }
                        else if (this.blockId == BlockInit.netherStalactiteBlock) {
                            if (world.func_147439_a(x, botY - 1, z) == Blocks.field_150348_b || world.func_147439_a(x, botY - 1, z) == Blocks.field_150322_A) {
                                world.func_147465_d(x, botY - 1, z, BlockInit.scorchedStone, 0, 2);
                            }
                            CavesDecorator.convertToLavaType(world, random, x, botY, z);
                        }
                    }
                }
                if (genStala) {
                    while (true) {
                        final int bottomMetadata = world.func_72805_g(x, botY, z);
                        if (topY < botY || j >= distance) {
                            return;
                        }
                        if (bottomMetadata == 8 && topY - 1 == botY) {
                            world.func_147465_d(x, topY, z, deepslateAbove ? BlockInit.deepslateStalactiteBlock : this.blockId, CavesDecorator.randomChoice(4, 5, 7, 6), 3);
                            break;
                        }
                        --topY;
                        ++j;
                        final int aux = random.nextInt(4);
                        if (world.func_147439_a(x, topY, z) instanceof BlockLiquid || world.func_147439_a(x, topY, z) instanceof BlockStaticLiquid) {
                            world.func_147465_d(x, topY + 1, z, deepslateAbove ? BlockInit.deepslateStalactiteBlock : this.blockId, CavesDecorator.randomChoice(11), 3);
                            return;
                        }
                        if (aux != 2 && !world.func_147439_a(x, topY, z).func_149721_r()) {
                            if (!world.func_147437_c(x, topY, z)) {
                                continue;
                            }
                            world.func_147465_d(x, topY, z, deepslateAbove ? BlockInit.deepslateStalactiteBlock : this.blockId, CavesDecorator.randomChoice(4, 5), 3);
                        }
                        else {
                            if (world.func_147437_c(x, topY, z)) {
                                world.func_147465_d(x, topY, z, deepslateAbove ? BlockInit.deepslateStalactiteBlock : this.blockId, CavesDecorator.randomChoice(11), 3);
                                break;
                            }
                            break;
                        }
                    }
                }
                if (gen) {
                    while (true) {
                        final int topMetadata = world.func_72805_g(x, y, z);
                        if (topY < botY || j >= distance) {
                            return;
                        }
                        if (topMetadata == 3 && botY + 1 == y) {
                            world.func_147465_d(x, botY, z, deepslateBelow ? BlockInit.deepslateStalactiteBlock : this.blockId, CavesDecorator.randomChoice(4, 5, 6, 7), 3);
                            break;
                        }
                        ++j;
                        ++botY;
                        if (world.func_147439_a(x, botY, z) instanceof BlockBaseStalactite) {
                            if (world.func_72805_g(x, botY, z) == 4 || world.func_72805_g(x, botY, z) == 5) {
                                world.func_147465_d(x, botY - 1, z, deepslateBelow ? BlockInit.deepslateStalactiteBlock : this.blockId, CavesDecorator.randomChoice(4, 5), 3);
                                break;
                            }
                            if (world.func_72805_g(x, botY, z) == 11) {
                                world.func_147465_d(x, botY - 1, z, deepslateBelow ? BlockInit.deepslateStalactiteBlock : this.blockId, CavesDecorator.randomChoice(12), 3);
                                break;
                            }
                        }
                        final int aux = random.nextInt(3);
                        if (aux != 2) {
                            if (!world.func_147437_c(x, botY, z)) {
                                continue;
                            }
                            world.func_147465_d(x, botY, z, deepslateBelow ? BlockInit.deepslateStalactiteBlock : this.blockId, CavesDecorator.randomChoice(4, 5), 3);
                        }
                        else {
                            if (world.func_147437_c(x, botY, z)) {
                                world.func_147465_d(x, botY, z, deepslateBelow ? BlockInit.deepslateStalactiteBlock : this.blockId, CavesDecorator.randomChoice(12), 3);
                                break;
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    static {
        GenerateStoneStalactite.blockWhiteList = new ArrayList();
    }
}
