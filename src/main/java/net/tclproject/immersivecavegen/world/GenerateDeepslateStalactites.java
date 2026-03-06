package net.tclproject.immersivecavegen.world;

import net.tclproject.immersivecavegen.blocks.*;
import net.minecraft.world.*;
import java.util.*;

public class GenerateDeepslateStalactites extends GenerateStoneStalactite
{
    public GenerateDeepslateStalactites() {
        super(BlockInit.deepslateStalactiteBlock);
    }

    public void generateBottom(final World world, final Random random, final int x, final int y, final int z, final int distance, final int maxLength) {
    }

    public void generateTop(final World world, final Random random, final int x, final int y, final int z, final int distance, final int maxLength) {
    }
}
