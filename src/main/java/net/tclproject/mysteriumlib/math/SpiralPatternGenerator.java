package net.tclproject.mysteriumlib.math;

import net.minecraft.world.*;
import joptsimple.internal.*;
import java.util.*;

public class SpiralPatternGenerator implements Iterable
{
    protected final ChunkCoordIntPair center;
    protected final int rangeMax;

    public SpiralPatternGenerator(final ChunkCoordIntPair center) {
        this(center, Integer.MAX_VALUE);
    }

    public SpiralPatternGenerator(final ChunkCoordIntPair center, final int rangeMax) {
        Objects.ensureNotNull((Object)center);
        if (rangeMax < 0) {
            throw new IllegalArgumentException("The maximum range must be more than or equal to 0.");
        }
        this.center = center;
        this.rangeMax = rangeMax;
    }

    @Override
    public Iterator iterator() {
        return new Iterator() {
            private int currentX;
            private int currentZ;
            protected SpiralDirection currentDirection;
            private int currentRange = 0;

            @Override
            public boolean hasNext() {
                return this.currentRange <= SpiralPatternGenerator.this.rangeMax;
            }

            @Override
            public ChunkCoordIntPair next() {
                if (this.currentRange == 0) {
                    ++this.currentRange;
                    this.currentX = SpiralPatternGenerator.this.center.field_77276_a - this.currentRange;
                    this.currentZ = SpiralPatternGenerator.this.center.field_77275_b - this.currentRange;
                    this.currentDirection = SpiralDirection.right;
                    return SpiralPatternGenerator.this.center;
                }
                final ChunkCoordIntPair result = new ChunkCoordIntPair(this.currentX, this.currentZ);
                this.step();
                return result;
            }

            protected void step() {
                Label_0178: {
                    switch (this.currentDirection) {
                        case right: {
                            if (this.currentX < SpiralPatternGenerator.this.center.field_77276_a + this.currentRange) {
                                ++this.currentX;
                                break Label_0178;
                            }
                            this.currentDirection = SpiralDirection.down;
                            this.step();
                            break Label_0178;
                        }
                        case down: {
                            if (this.currentZ < SpiralPatternGenerator.this.center.field_77275_b + this.currentRange) {
                                ++this.currentZ;
                                break Label_0178;
                            }
                            this.currentDirection = SpiralDirection.left;
                            this.step();
                            break Label_0178;
                        }
                        case left: {
                            if (this.currentX > SpiralPatternGenerator.this.center.field_77276_a - this.currentRange) {
                                --this.currentX;
                                break Label_0178;
                            }
                            this.currentDirection = SpiralDirection.up;
                            this.step();
                            break Label_0178;
                        }
                        case up: {
                            if (this.currentZ - 1 > SpiralPatternGenerator.this.center.field_77275_b - this.currentRange) {
                                --this.currentZ;
                                break;
                            }
                            this.currentDirection = SpiralDirection.right;
                            ++this.currentRange;
                            this.currentX = SpiralPatternGenerator.this.center.field_77276_a - this.currentRange;
                            this.currentZ = SpiralPatternGenerator.this.center.field_77275_b - this.currentRange;
                            break;
                        }
                    }
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported at this moment in time.");
            }
        };
    }

    public ChunkCoordIntPair getCenter() {
        return this.center;
    }

    public int getRangeMax() {
        return this.rangeMax;
    }

    private enum SpiralDirection
    {
        right,
        down,
        left,
        up;
    }
}
