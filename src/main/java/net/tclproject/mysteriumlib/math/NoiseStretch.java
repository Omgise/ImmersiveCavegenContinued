package net.tclproject.mysteriumlib.math;

public class NoiseStretch
{
    protected final double stretchX;
    protected final double stretchY;
    protected final double stretchZ;
    protected final double offsetX;
    protected final double offsetY;
    protected final double offsetZ;
    protected final SimplexNoise noise;

    public NoiseStretch(final SimplexNoise noise, final double stretchX, final double stretchZ, final double offsetX, final double offsetZ) {
        this(noise, stretchX, 100.0, stretchZ, offsetX, 0.0, offsetZ);
    }

    public NoiseStretch(final SimplexNoise noise, final double stretchX, final double stretchY, final double stretchZ, final double offsetX, final double offsetY, final double offsetZ) {
        this.noise = noise;
        this.stretchX = stretchX;
        this.stretchY = stretchY + 100.0;
        this.stretchZ = stretchZ;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    public double getNoise(final double blockX, final double blockZ) {
        return this.noise.noise(blockX / (this.stretchX * 2.5) + this.offsetX, blockZ / (this.stretchZ * 2.5) + this.offsetZ);
    }

    public double getNoise(final double blockX, final double blockY, final double blockZ) {
        return this.noise.noise(blockX / this.stretchX + this.offsetX, blockY / this.stretchY + this.offsetY, blockZ / this.stretchZ + this.offsetZ);
    }
}
