package net.tclproject.mysteriumlib.asm.fixes;

import java.util.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.structure.*;
import net.tclproject.mysteriumlib.asm.annotations.*;

public class MysteriumPatchesFixesB
{
    private static final Random caveRNG;
    private static final Random largeCaveRNG;
    private static final Random spawnRNG;
    private static long seedMultiplier;
    private static long regionalCaveSeedMultiplier;
    private static long colossalCaveSeedMultiplier;
    private static int caveOffsetX;
    private static int caveOffsetZ;
    private static int mineshaftOffsetX;
    private static Random rand;
    private static World worldObj;
    private static int mineshaftOffsetZ;
    private static double field_82673_e;
    private static final int caveLimit = 18;
    private static boolean isInitialized;

    private static void initialize() {
        MysteriumPatchesFixesB.isInitialized = true;
        MysteriumPatchesFixesB.rand.setSeed(MysteriumPatchesFixesB.worldObj.func_72905_C());
        MysteriumPatchesFixesB.seedMultiplier = MysteriumPatchesFixesB.rand.nextLong() / 2L * 2L + 1L;
        MysteriumPatchesFixesB.caveOffsetX = MysteriumPatchesFixesB.rand.nextInt(128) + 2000000;
        MysteriumPatchesFixesB.caveOffsetZ = MysteriumPatchesFixesB.rand.nextInt(128) + 2000000;
        MysteriumPatchesFixesB.mineshaftOffsetX = MysteriumPatchesFixesB.rand.nextInt(7) + 2000000;
        MysteriumPatchesFixesB.mineshaftOffsetZ = MysteriumPatchesFixesB.rand.nextInt(7) + 2000000;
        final byte range = 66;
    Label_0165:
        for (int i = 0; i < 100; i += 2) {
            MysteriumPatchesFixesB.colossalCaveSeedMultiplier = MysteriumPatchesFixesB.seedMultiplier + i;
            for (int z = -range; z <= range; ++z) {
                for (int x = -range; x <= range; ++x) {
                    if (validColossalCaveLocation(x, z, x * x + z * z)) {
                        break Label_0165;
                    }
                }
            }
        }
        for (int i = 0; i < 100; i += 2) {
            MysteriumPatchesFixesB.regionalCaveSeedMultiplier = MysteriumPatchesFixesB.seedMultiplier + i;
            for (int z = -range; z <= range; z += 12) {
                for (int x = -range; x <= range; x += 12) {
                    if (validRegionalCaveLocation(x, z, x * x + z * z) && isGiantCaveRegion(x, z)) {
                        return;
                    }
                }
            }
        }
    }

    @Fix(returnSetting = EnumReturnSetting.ALWAYS)
    public static boolean canSpawnStructureAtCoords(final MapGenMineshaft m, final int chunkX, final int chunkZ) {
        MysteriumPatchesFixesB.rand = m.field_75038_b;
        MysteriumPatchesFixesB.worldObj = m.field_75039_c;
        if (!MysteriumPatchesFixesB.isInitialized) {
            initialize();
        }
        if (chunkX == 0 && chunkZ == 0) {
            return false;
        }
        final int chunkOffX = chunkX + MysteriumPatchesFixesB.mineshaftOffsetX;
        final int chunkOffZ = chunkZ + MysteriumPatchesFixesB.mineshaftOffsetZ;
        if ((chunkOffX / 7 & 0x1) == (chunkOffZ / 7 & 0x1)) {
            return false;
        }
        final int chunkModX = chunkOffX % 7;
        final int chunkModZ = chunkOffZ % 7;
        if (chunkModX <= 2 && chunkModZ <= 2) {
            MysteriumPatchesFixesB.spawnRNG.setSeed((chunkOffX / 7 * 341873128712L + chunkOffZ / 7 * 132897987541L) * MysteriumPatchesFixesB.seedMultiplier);
            if (chunkModX == MysteriumPatchesFixesB.spawnRNG.nextInt(3) && chunkModZ == MysteriumPatchesFixesB.spawnRNG.nextInt(3)) {
                MysteriumPatchesFixesB.rand.setSeed((chunkX * 341873128712L + chunkZ * 132897987541L) * MysteriumPatchesFixesB.seedMultiplier);
                final int distance = (Math.abs(chunkX) < 64 && Math.abs(chunkZ) < 64) ? (chunkX * chunkX + chunkZ * chunkZ) : 4096;
                return caveCheck(m, chunkX, chunkZ, distance);
            }
        }
        return false;
    }

    private static boolean caveCheck(final MapGenMineshaft m, final int chunkX, final int chunkZ, int distance) {
        final byte radius = 6;
        final boolean colossalCaveR2 = true;
        final byte caveR2 = 17;
        final byte centerR2 = 4;
        int caveCount1 = 0;
        int caveCount2 = 0;
        int caveCount3 = 0;
        int caveCount4 = 0;
        int caveCount5 = 0;
        float totalWidth = 0.0f;
        final boolean flag = distance < 4096;
        for (int z = -radius; z <= radius; ++z) {
            final int cz = chunkZ + z;
            for (int x = -radius; x <= radius; ++x) {
                final int x2z2 = x * x + z * z;
                if (x2z2 <= 37) {
                    final int cx = chunkX + x;
                    if (flag) {
                        distance = cx * cx + cz * cz;
                    }
                    if (validColossalCaveLocation(cx, cz, distance)) {
                        return false;
                    }
                    final int chunkOffX = cx + MysteriumPatchesFixesB.caveOffsetX + 4;
                    final int chunkOffZ = cz + MysteriumPatchesFixesB.caveOffsetZ + 4;
                    if (distance >= 1024) {
                        MysteriumPatchesFixesB.spawnRNG.setSeed((cx * 341873128712L + cz * 132897987541L) * MysteriumPatchesFixesB.seedMultiplier);
                        if (MysteriumPatchesFixesB.spawnRNG.nextInt(20) == 15) {
                            byte caveSize = 0;
                            if ((chunkOffX & 0x7) == 0x0 && (chunkOffZ & 0x7) == 0x0 && (chunkOffX & 0x8) != (chunkOffZ & 0x8)) {
                                caveSize = 2;
                            }
                            else if (MysteriumPatchesFixesB.spawnRNG.nextInt(25) < 19 && chunkOffX % 3 == 0 && chunkOffZ % 3 == 0 && (chunkOffX / 3 & 0x1) == (chunkOffZ / 3 & 0x1)) {
                                caveSize = 1;
                            }
                            if (caveSize > 0) {
                                MysteriumPatchesFixesB.spawnRNG.nextInt(MysteriumPatchesFixesB.spawnRNG.nextInt(50) + 8);
                                MysteriumPatchesFixesB.spawnRNG.nextFloat();
                                MysteriumPatchesFixesB.spawnRNG.nextFloat();
                                float applyCaveVariation = MysteriumPatchesFixesB.spawnRNG.nextFloat() * 4.0f + MysteriumPatchesFixesB.spawnRNG.nextFloat() * 2.0f;
                                if (MysteriumPatchesFixesB.spawnRNG.nextInt(4) == 0) {
                                    applyCaveVariation += MysteriumPatchesFixesB.spawnRNG.nextFloat() * ((caveSize != 1 && applyCaveVariation < 2.0f) ? 0.0f : 2.0f);
                                }
                                int largeCaveChance = 112 - MysteriumPatchesFixesB.spawnRNG.nextInt(15) * 2;
                                if (MysteriumPatchesFixesB.spawnRNG.nextInt(3) == 0) {
                                    MysteriumPatchesFixesB.spawnRNG.nextInt(3);
                                }
                                largeCaveChance += MysteriumPatchesFixesB.spawnRNG.nextInt(64) * 2;
                                if (applyCaveVariation < 2.0f) {
                                    ++applyCaveVariation;
                                    if (applyCaveVariation < 2.0f) {
                                        ++applyCaveVariation;
                                    }
                                }
                                applyCaveVariation *= MysteriumPatchesFixesB.spawnRNG.nextFloat() * MysteriumPatchesFixesB.spawnRNG.nextFloat() * 1.5f + 1.0f;
                                if (caveSize == 2) {
                                    largeCaveChance += 80 + MysteriumPatchesFixesB.spawnRNG.nextInt(40) * 2;
                                    applyCaveVariation += MysteriumPatchesFixesB.spawnRNG.nextFloat() * (largeCaveChance / 56) + 3.0f;
                                }
                                if (largeCaveChance > 160 || x2z2 <= caveR2) {
                                    totalWidth += applyCaveVariation;
                                    if (applyCaveVariation > 10.0f || totalWidth > 30.0f) {
                                        return false;
                                    }
                                }
                            }
                        }
                        if ((chunkOffX & 0x7) == 0x0 && (chunkOffZ & 0x7) == 0x0 && (chunkOffX & 0x8) == (chunkOffZ & 0x8)) {
                            MysteriumPatchesFixesB.spawnRNG.setSeed((cx * 341873128712L + cz * 132897987541L) * MysteriumPatchesFixesB.seedMultiplier);
                            if (MysteriumPatchesFixesB.spawnRNG.nextInt(15) == 0 && MysteriumPatchesFixesB.spawnRNG.nextInt(MysteriumPatchesFixesB.spawnRNG.nextInt(MysteriumPatchesFixesB.spawnRNG.nextInt(40) + 1) + 1) > 0) {
                                MysteriumPatchesFixesB.largeCaveRNG.setSeed(MysteriumPatchesFixesB.spawnRNG.nextLong());
                                final float var38 = getLargeCaveWidth(cx, cz, 0);
                                totalWidth += var38;
                                if (var38 > 18.0f || totalWidth > 30.0f) {
                                    return false;
                                }
                            }
                        }
                    }
                    if (x2z2 <= caveR2) {
                        if (validStrongholdLocation(cx, cz, distance)) {
                            return false;
                        }
                        if (validSpecialCaveLocation(cx, cz, distance) > 0) {
                            return false;
                        }
                        if (isGiantCaveRegion(cx, cz) && validRegionalCaveLocation(cx, cz, distance)) {
                            return false;
                        }
                        MysteriumPatchesFixesB.spawnRNG.setSeed((cx * 341873128712L + cz * 132897987541L) * MysteriumPatchesFixesB.seedMultiplier);
                        if (MysteriumPatchesFixesB.spawnRNG.nextInt(15) == 0) {
                            int var39 = MysteriumPatchesFixesB.spawnRNG.nextInt(MysteriumPatchesFixesB.spawnRNG.nextInt(MysteriumPatchesFixesB.spawnRNG.nextInt(40) + 1) + 1);
                            if (var39 > 0) {
                                boolean var40 = false;
                                int largeCaveChance = -1;
                                long regionSeed = 0L;
                                if (distance >= 1024) {
                                    MysteriumPatchesFixesB.caveRNG.setSeed((chunkOffX / 16 * 341873128712L + chunkOffZ / 16 * 132897987541L) * MysteriumPatchesFixesB.seedMultiplier);
                                    MysteriumPatchesFixesB.largeCaveRNG.setSeed(MysteriumPatchesFixesB.spawnRNG.nextLong());
                                    if (MysteriumPatchesFixesB.caveRNG.nextInt(4) != 0) {
                                        largeCaveChance = (1 << MysteriumPatchesFixesB.caveRNG.nextInt(3)) - 1;
                                        var40 = true;
                                        regionSeed = MysteriumPatchesFixesB.caveRNG.nextLong();
                                    }
                                    if ((chunkOffX & 0x7) == 0x0 && (chunkOffZ & 0x7) == 0x0 && (chunkOffX & 0x8) == (chunkOffZ & 0x8)) {
                                        return false;
                                    }
                                    if (var39 <= 3 && MysteriumPatchesFixesB.largeCaveRNG.nextInt(4) <= largeCaveChance && validLargeCaveLocation(cx, cz, var39) > 0) {
                                        int largerCircularRooms = 1;
                                        if (MysteriumPatchesFixesB.largeCaveRNG.nextInt(10) == 0) {
                                            largerCircularRooms += 1 + MysteriumPatchesFixesB.largeCaveRNG.nextInt(3);
                                        }
                                        for (int circularRoomChance = 0; circularRoomChance < largerCircularRooms; ++circularRoomChance) {
                                            final float widthMultiplier = getLargeCaveWidth(cx, cz, 1);
                                            totalWidth += widthMultiplier;
                                            if (widthMultiplier > 10.0f || totalWidth > 30.0f) {
                                                return false;
                                            }
                                        }
                                    }
                                }
                                boolean var41 = false;
                                int circularRoomChance = 4;
                                largeCaveChance = 10;
                                float widthMultiplier = 1.0f;
                                byte multiplier = 1;
                                if (var40) {
                                    MysteriumPatchesFixesB.caveRNG.setSeed(regionSeed);
                                    if (var39 < 20) {
                                        var41 = MysteriumPatchesFixesB.caveRNG.nextBoolean();
                                        circularRoomChance = 2 << MysteriumPatchesFixesB.caveRNG.nextInt(2) + MysteriumPatchesFixesB.caveRNG.nextInt(2);
                                        MysteriumPatchesFixesB.caveRNG.nextBoolean();
                                        largeCaveChance = 5 << MysteriumPatchesFixesB.caveRNG.nextInt(2) + MysteriumPatchesFixesB.caveRNG.nextInt(2);
                                    }
                                    if (MysteriumPatchesFixesB.caveRNG.nextBoolean()) {
                                        widthMultiplier += MysteriumPatchesFixesB.caveRNG.nextFloat();
                                        if (MysteriumPatchesFixesB.caveRNG.nextBoolean()) {
                                            widthMultiplier /= 2.0f;
                                        }
                                    }
                                    if (MysteriumPatchesFixesB.caveRNG.nextBoolean()) {
                                        MysteriumPatchesFixesB.caveRNG.nextBoolean();
                                        if (MysteriumPatchesFixesB.spawnRNG.nextBoolean()) {
                                            MysteriumPatchesFixesB.spawnRNG.nextFloat();
                                        }
                                    }
                                    if (var39 >= 20) {
                                        final float caves = (float)(var39 / 10);
                                        widthMultiplier = (widthMultiplier + caves - 1.0f) / caves;
                                    }
                                    if (var39 >= 10) {
                                        if (widthMultiplier > 1.5f) {
                                            var39 = var39 / 2 + 1;
                                            multiplier = 3;
                                        }
                                        else if (widthMultiplier > 1.25f) {
                                            var39 = var39 * 3 / 4 + 1;
                                            multiplier = 2;
                                        }
                                    }
                                }
                                int var42 = 0;
                                for (int i = 0; i < var39; ++i) {
                                    MysteriumPatchesFixesB.spawnRNG.nextInt(16);
                                    final int y = MysteriumPatchesFixesB.spawnRNG.nextInt(MysteriumPatchesFixesB.spawnRNG.nextInt(120) + 8);
                                    MysteriumPatchesFixesB.spawnRNG.nextInt(16);
                                    int caves2 = 1;
                                    if (MysteriumPatchesFixesB.spawnRNG.nextInt(circularRoomChance) == 0) {
                                        final int j = MysteriumPatchesFixesB.spawnRNG.nextInt(4);
                                        caves2 += j;
                                        float width = MysteriumPatchesFixesB.spawnRNG.nextFloat() * 6.0f + 1.0f;
                                        if (var41 && MysteriumPatchesFixesB.spawnRNG.nextInt(16 / circularRoomChance) == 0) {
                                            width = width * (MysteriumPatchesFixesB.spawnRNG.nextFloat() * MysteriumPatchesFixesB.spawnRNG.nextFloat() + 1.0f) + 3.0f;
                                            if (width > 8.5f) {
                                                caves2 += 2;
                                            }
                                        }
                                        if (widthMultiplier >= 1.0f) {
                                            width *= widthMultiplier;
                                            if (j == 0 && width > 10.0f && width < 17.0f) {
                                                final float f = (width - 10.0f) / 7.0f;
                                                width *= MysteriumPatchesFixesB.spawnRNG.nextFloat() * (1.0f - f) + 1.0f + f;
                                            }
                                        }
                                        if (width > 15.5f) {
                                            totalWidth += width / 2.0f;
                                            if (width > 23.5f || totalWidth > 30.0f) {
                                                return false;
                                            }
                                        }
                                        MysteriumPatchesFixesB.spawnRNG.nextLong();
                                    }
                                    MysteriumPatchesFixesB.spawnRNG.nextFloat();
                                    if (y > 10 && y < 60) {
                                        var42 += caves2 * multiplier;
                                    }
                                    for (int j = 0; j < caves2; ++j) {
                                        MysteriumPatchesFixesB.spawnRNG.nextFloat();
                                        MysteriumPatchesFixesB.spawnRNG.nextFloat();
                                        if (MysteriumPatchesFixesB.spawnRNG.nextInt(largeCaveChance) == 0) {
                                            MysteriumPatchesFixesB.spawnRNG.nextFloat();
                                            MysteriumPatchesFixesB.spawnRNG.nextFloat();
                                        }
                                        if (j > 0) {
                                            MysteriumPatchesFixesB.spawnRNG.nextFloat();
                                        }
                                        MysteriumPatchesFixesB.spawnRNG.nextLong();
                                        MysteriumPatchesFixesB.spawnRNG.nextFloat();
                                    }
                                }
                                if (x < 0) {
                                    caveCount1 += var42;
                                    m.getClass();
                                    if (caveCount1 > 18) {
                                        return false;
                                    }
                                }
                                else if (x > 0) {
                                    caveCount2 += var42;
                                    m.getClass();
                                    if (caveCount2 > 18) {
                                        return false;
                                    }
                                }
                                if (z < 0) {
                                    caveCount3 += var42;
                                    m.getClass();
                                    if (caveCount3 > 18) {
                                        return false;
                                    }
                                }
                                else if (z > 0) {
                                    caveCount4 += var42;
                                    m.getClass();
                                    if (caveCount4 > 18) {
                                        return false;
                                    }
                                }
                                if (x2z2 <= centerR2) {
                                    caveCount5 += var42;
                                    m.getClass();
                                    if (caveCount5 > 12) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private static float getLargeCaveWidth(final int chunkX, final int chunkZ, final int type) {
        if (type == 0) {
            final int length = 224 + MysteriumPatchesFixesB.largeCaveRNG.nextInt(113);
            float width = MysteriumPatchesFixesB.largeCaveRNG.nextFloat() * 8.0f + MysteriumPatchesFixesB.largeCaveRNG.nextFloat() * 6.0f + 10.0f;
            if (MysteriumPatchesFixesB.largeCaveRNG.nextBoolean()) {
                width *= length / 224.0f;
            }
            return width * (length / 336.0f);
        }
        final int length = Math.min(112 + MysteriumPatchesFixesB.largeCaveRNG.nextInt(MysteriumPatchesFixesB.largeCaveRNG.nextInt(336) + 1), 336);
        MysteriumPatchesFixesB.caveRNG.setSeed(((chunkX + MysteriumPatchesFixesB.caveOffsetX + 12) / 16 * 341873128712L + (chunkZ + MysteriumPatchesFixesB.caveOffsetZ + 12) / 16 * 132897987541L) * MysteriumPatchesFixesB.seedMultiplier);
        float width = MysteriumPatchesFixesB.largeCaveRNG.nextFloat() * MysteriumPatchesFixesB.largeCaveRNG.nextFloat() * MysteriumPatchesFixesB.largeCaveRNG.nextFloat();
        if (MysteriumPatchesFixesB.caveRNG.nextBoolean()) {
            width = width * 8.0f + 2.0f;
        }
        else {
            width = width * 2.66667f + 2.66667f;
        }
        if (MysteriumPatchesFixesB.largeCaveRNG.nextBoolean()) {
            final float multiplier = MysteriumPatchesFixesB.largeCaveRNG.nextFloat() * length / 96.0f + (672 - length) / 672.0f;
            if (multiplier > 1.0f) {
                width *= multiplier;
            }
        }
        else {
            final float multiplier = MysteriumPatchesFixesB.largeCaveRNG.nextFloat();
            width *= multiplier * multiplier * 3.0f + 1.0f;
        }
        MysteriumPatchesFixesB.largeCaveRNG.nextInt(16);
        MysteriumPatchesFixesB.largeCaveRNG.nextInt(length / 4);
        MysteriumPatchesFixesB.largeCaveRNG.nextFloat();
        MysteriumPatchesFixesB.largeCaveRNG.nextLong();
        MysteriumPatchesFixesB.largeCaveRNG.nextFloat();
        return width;
    }

    private static boolean validStrongholdLocation(int chunkX, int chunkZ, final int distance) {
        chunkX += MysteriumPatchesFixesB.caveOffsetX;
        chunkZ += MysteriumPatchesFixesB.caveOffsetZ;
        if ((chunkX & 0x40) != (chunkZ & 0x40)) {
            return false;
        }
        MysteriumPatchesFixesB.caveRNG.setSeed((chunkX / 64 * 341873128712L + chunkZ / 64 * 132897987541L) * MysteriumPatchesFixesB.seedMultiplier);
        return (chunkX & 0x3F) == MysteriumPatchesFixesB.caveRNG.nextInt(32) && (chunkZ & 0x3F) == MysteriumPatchesFixesB.caveRNG.nextInt(32) && distance >= 1600;
    }

    private static boolean validColossalCaveLocation(int chunkX, int chunkZ, final int distance) {
        chunkX += MysteriumPatchesFixesB.caveOffsetX;
        chunkZ += MysteriumPatchesFixesB.caveOffsetZ;
        if ((chunkX & 0x40) == (chunkZ & 0x40)) {
            return false;
        }
        MysteriumPatchesFixesB.caveRNG.setSeed((chunkX / 64 * 341873128712L + chunkZ / 64 * 132897987541L) * MysteriumPatchesFixesB.colossalCaveSeedMultiplier);
        return (chunkX & 0x3F) == MysteriumPatchesFixesB.caveRNG.nextInt(32) && (chunkZ & 0x3F) == MysteriumPatchesFixesB.caveRNG.nextInt(32) && distance >= 1600;
    }

    private static int validSpecialCaveLocation(final int chunkX, final int chunkZ, int distance) {
        if (distance >= 1024) {
            int offsetX = chunkX + MysteriumPatchesFixesB.caveOffsetX + 1;
            int offsetZ = chunkZ + MysteriumPatchesFixesB.caveOffsetZ + 1;
            if ((offsetX & 0x7) <= 2 && (offsetZ & 0x7) <= 2) {
                final int d = validSpecialCaveLocation2(offsetX, offsetZ);
                if (d != 0) {
                    return d;
                }
                offsetX -= 16;
                offsetZ -= 16;
                MysteriumPatchesFixesB.caveRNG.setSeed((offsetX / 32 * 341873128712L + offsetZ / 32 * 132897987541L) * MysteriumPatchesFixesB.seedMultiplier);
                if ((offsetX & 0x1F) == MysteriumPatchesFixesB.caveRNG.nextInt(4) * 8 + MysteriumPatchesFixesB.caveRNG.nextInt(3) && (offsetZ & 0x1F) == MysteriumPatchesFixesB.caveRNG.nextInt(4) * 8 + MysteriumPatchesFixesB.caveRNG.nextInt(3)) {
                    final boolean flag = distance < 5041;
                    for (int z = -7; z <= 7; ++z) {
                        final int cz = chunkZ + z;
                        for (int x = -7; x <= 7; ++x) {
                            final int x2z2 = x * x + z * z;
                            if (x2z2 <= 50) {
                                final int cx = chunkX + x;
                                if (flag) {
                                    distance = cx * cx + cz * cz;
                                }
                                if (validColossalCaveLocation(cx, cz, distance)) {
                                    return 0;
                                }
                                if (x2z2 <= 37) {
                                    if (validStrongholdLocation(cx, cz, distance)) {
                                        return 0;
                                    }
                                    if (x2z2 <= 24) {
                                        if (validRegionalCaveLocation(cx, cz, distance)) {
                                            return 0;
                                        }
                                        if (x2z2 > 0 && x2z2 <= 17 && validSpecialCaveLocation2(cx + MysteriumPatchesFixesB.caveOffsetX + 1, cz + MysteriumPatchesFixesB.caveOffsetZ + 1) != 0) {
                                            return 0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return 1;
                }
            }
        }
        return 0;
    }

    private static int validSpecialCaveLocation2(final int offsetX, final int offsetZ) {
        MysteriumPatchesFixesB.caveRNG.setSeed((offsetX / 64 * 341873128712L + offsetZ / 64 * 132897987541L) * MysteriumPatchesFixesB.regionalCaveSeedMultiplier);
        int x;
        int z;
        if (MysteriumPatchesFixesB.caveRNG.nextBoolean()) {
            x = MysteriumPatchesFixesB.caveRNG.nextInt(4) * 8 + MysteriumPatchesFixesB.caveRNG.nextInt(3);
            z = MysteriumPatchesFixesB.caveRNG.nextInt(3) * 8 + MysteriumPatchesFixesB.caveRNG.nextInt(3) + 40;
        }
        else {
            x = MysteriumPatchesFixesB.caveRNG.nextInt(3) * 8 + MysteriumPatchesFixesB.caveRNG.nextInt(3) + 40;
            z = MysteriumPatchesFixesB.caveRNG.nextInt(4) * 8 + MysteriumPatchesFixesB.caveRNG.nextInt(3);
        }
        return ((offsetX & 0x3F) == x && (offsetZ & 0x3F) == z) ? 4 : (((offsetX & 0x3F) == MysteriumPatchesFixesB.caveRNG.nextInt(3) * 8 + MysteriumPatchesFixesB.caveRNG.nextInt(3) + 40 && (offsetZ & 0x3F) == MysteriumPatchesFixesB.caveRNG.nextInt(3) * 8 + MysteriumPatchesFixesB.caveRNG.nextInt(3) + 40) ? 5 : 0);
    }

    private static boolean validRegionalCaveLocation(int chunkX, int chunkZ, final int distance) {
        int x1 = chunkX;
        int z1 = chunkZ;
        chunkX += MysteriumPatchesFixesB.caveOffsetX;
        chunkZ += MysteriumPatchesFixesB.caveOffsetZ;
        MysteriumPatchesFixesB.caveRNG.setSeed((chunkX / 64 * 341873128712L + chunkZ / 64 * 132897987541L) * MysteriumPatchesFixesB.regionalCaveSeedMultiplier);
        chunkX &= 0x3F;
        chunkZ &= 0x3F;
        int offsetX;
        int offsetZ;
        if (MysteriumPatchesFixesB.caveRNG.nextBoolean()) {
            offsetX = MysteriumPatchesFixesB.caveRNG.nextInt(9) + 38;
            offsetZ = MysteriumPatchesFixesB.caveRNG.nextInt(21);
        }
        else {
            offsetX = MysteriumPatchesFixesB.caveRNG.nextInt(21);
            offsetZ = MysteriumPatchesFixesB.caveRNG.nextInt(9) + 38;
        }
        if (chunkX >= offsetX && chunkX <= offsetX + 11 && chunkZ >= offsetZ && chunkZ <= offsetZ + 11) {
            if (distance < 4096) {
                x1 -= chunkX - offsetX;
                z1 -= chunkZ - offsetZ;
                int x2 = x1 + 11;
                int z2 = z1 + 11;
                x1 *= x1;
                z1 *= z1;
                x2 *= x2;
                z2 *= z2;
                if (x1 + z1 < 1024 || x2 + z1 < 1024 || x1 + z2 < 1024 || x2 + z2 < 1024) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean isGiantCaveRegion(int chunkX, int chunkZ) {
        chunkX = (chunkX + MysteriumPatchesFixesB.caveOffsetX) / 64;
        chunkZ = (chunkZ + MysteriumPatchesFixesB.caveOffsetZ) / 64;
        MysteriumPatchesFixesB.caveRNG.setSeed((chunkX / 2 * 341873128712L + chunkZ / 2 * 132897987541L) * MysteriumPatchesFixesB.regionalCaveSeedMultiplier);
        return (chunkX & 0x1) == MysteriumPatchesFixesB.caveRNG.nextInt(2) && (chunkZ & 0x1) == MysteriumPatchesFixesB.caveRNG.nextInt(2);
    }

    private static int validLargeCaveLocation(final int chunkX, final int chunkZ, int caves) {
        int flag = 3;
        int caves2 = caves;
        for (int x = -3; x <= 3; ++x) {
            for (int z = -3; z <= 3; ++z) {
                final int x2z2 = x * x + z * z;
                if (x2z2 > 0 && x2z2 <= 10) {
                    MysteriumPatchesFixesB.caveRNG.setSeed(((chunkX + x) * 341873128712L + (chunkZ + z) * 132897987541L) * MysteriumPatchesFixesB.seedMultiplier);
                    if (MysteriumPatchesFixesB.caveRNG.nextInt(15) == 0) {
                        final int c = MysteriumPatchesFixesB.caveRNG.nextInt(MysteriumPatchesFixesB.caveRNG.nextInt(MysteriumPatchesFixesB.caveRNG.nextInt(40) + 1) + 1);
                        if (x2z2 <= 5) {
                            caves += c;
                            if (caves > 12) {
                                return 0;
                            }
                        }
                        caves2 += c;
                        if (caves2 > 6) {
                            flag = ((caves2 > 12) ? 1 : 2);
                        }
                    }
                }
            }
        }
        return flag;
    }

    static {
        caveRNG = new Random();
        largeCaveRNG = new Random();
        spawnRNG = new Random();
        MysteriumPatchesFixesB.field_82673_e = 0.004;
    }
}
