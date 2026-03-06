package net.tclproject.mysteriumlib.math;

import java.util.*;

public class SimplexNoise
{
    private static final Grad[] grad3;
    private static final Grad[] grad4;
    private static final double F2;
    private static final double G2;
    private static final double F3 = 0.3333333333333333;
    private static final double G3 = 0.16666666666666666;
    private static final double F4;
    private static final double G4;
    protected final short[] doubledPermutationTable;
    protected final short[] variatedPermutationTable;
    protected final Random random;

    private static int fastfloor(final double x) {
        final int xi = (int)x;
        return (x < xi) ? (xi - 1) : xi;
    }

    private static double dot(final Grad g, final double x, final double y) {
        return g.x * x + g.y * y;
    }

    private static double dot(final Grad g, final double x, final double y, final double z) {
        return g.x * x + g.y * y + g.z * z;
    }

    private static double dot(final Grad g, final double x, final double y, final double z, final double w) {
        return g.x * x + g.y * y + g.z * z + g.w * w;
    }

    public SimplexNoise(final Random random) {
        final byte[] bytes = new byte[1024];
        (this.random = random).nextBytes(bytes);
        this.doubledPermutationTable = new short[bytes.length * 2];
        this.variatedPermutationTable = new short[this.doubledPermutationTable.length];
        for (int i = 0; i < bytes.length; ++i) {
            final short value = (short)(bytes[i] & 0xFF);
            this.doubledPermutationTable[i] = value;
            this.variatedPermutationTable[i] = (short)(value % 12);
        }
        System.arraycopy(this.doubledPermutationTable, 0, this.doubledPermutationTable, bytes.length, bytes.length);
        System.arraycopy(this.variatedPermutationTable, 0, this.variatedPermutationTable, bytes.length, bytes.length);
    }

    public Random getRandom() {
        return this.random;
    }

    public NoiseStretch generateNoiseStretcher(final double stretchX, final double stretchZ, final double offsetX, final double offsetZ) {
        return new NoiseStretch(this, stretchX, stretchZ, offsetX, offsetZ);
    }

    public NoiseStretch generateNoiseStretcher(final double stretchX, final double stretchY, final double stretchZ, final double offsetX, final double offsetY, final double offsetZ) {
        return new NoiseStretch(this, stretchX, stretchY, stretchZ, offsetX, offsetY, offsetZ);
    }

    public double noise(final double xin, final double yin) {
        final double s = (xin + yin) * SimplexNoise.F2;
        final int i = fastfloor(xin + s);
        final int j = fastfloor(yin + s);
        final double t = (i + j) * SimplexNoise.G2;
        final double X0 = i - t;
        final double Y0 = j - t;
        final double x0 = xin - X0;
        final double y0 = yin - Y0;
        byte i2;
        byte j2;
        if (x0 > y0) {
            i2 = 1;
            j2 = 0;
        }
        else {
            i2 = 0;
            j2 = 1;
        }
        final double x2 = x0 - i2 + SimplexNoise.G2;
        final double y2 = y0 - j2 + SimplexNoise.G2;
        final double x3 = x0 - 1.0 + 2.0 * SimplexNoise.G2;
        final double y3 = y0 - 1.0 + 2.0 * SimplexNoise.G2;
        final int ii = i & 0x3FF;
        final int jj = j & 0x3FF;
        final int gi0 = this.variatedPermutationTable[ii + this.doubledPermutationTable[jj]];
        final int gi2 = this.variatedPermutationTable[ii + i2 + this.doubledPermutationTable[jj + j2]];
        final int gi3 = this.variatedPermutationTable[ii + 1 + this.doubledPermutationTable[jj + 1]];
        double t2 = 0.5 - x0 * x0 - y0 * y0;
        double n0;
        if (t2 < 0.0) {
            n0 = 0.0;
        }
        else {
            t2 *= t2;
            n0 = t2 * t2 * dot(SimplexNoise.grad3[gi0], x0, y0);
        }
        double t3 = 0.5 - x2 * x2 - y2 * y2;
        double n2;
        if (t3 < 0.0) {
            n2 = 0.0;
        }
        else {
            t3 *= t3;
            n2 = t3 * t3 * dot(SimplexNoise.grad3[gi2], x2, y2);
        }
        double t4 = 0.5 - x3 * x3 - y3 * y3;
        double n3;
        if (t4 < 0.0) {
            n3 = 0.0;
        }
        else {
            t4 *= t4;
            n3 = t4 * t4 * dot(SimplexNoise.grad3[gi3], x3, y3);
        }
        return 70.0 * (n0 + n2 + n3);
    }

    public double noise(final double xin, final double yin, final double zin) {
        final double s = (xin + yin + zin) * 0.3333333333333333;
        final int i = fastfloor(xin + s);
        final int j = fastfloor(yin + s);
        final int k = fastfloor(zin + s);
        final double t = (i + j + k) * 0.16666666666666666;
        final double X0 = i - t;
        final double Y0 = j - t;
        final double Z0 = k - t;
        final double x0 = xin - X0;
        final double y0 = yin - Y0;
        final double z0 = zin - Z0;
        byte i2;
        byte j2;
        byte k2;
        byte i3;
        byte j3;
        byte k3;
        if (x0 >= y0) {
            if (y0 >= z0) {
                i2 = 1;
                j2 = 0;
                k2 = 0;
                i3 = 1;
                j3 = 1;
                k3 = 0;
            }
            else if (x0 >= z0) {
                i2 = 1;
                j2 = 0;
                k2 = 0;
                i3 = 1;
                j3 = 0;
                k3 = 1;
            }
            else {
                i2 = 0;
                j2 = 0;
                k2 = 1;
                i3 = 1;
                j3 = 0;
                k3 = 1;
            }
        }
        else if (y0 < z0) {
            i2 = 0;
            j2 = 0;
            k2 = 1;
            i3 = 0;
            j3 = 1;
            k3 = 1;
        }
        else if (x0 < z0) {
            i2 = 0;
            j2 = 1;
            k2 = 0;
            i3 = 0;
            j3 = 1;
            k3 = 1;
        }
        else {
            i2 = 0;
            j2 = 1;
            k2 = 0;
            i3 = 1;
            j3 = 1;
            k3 = 0;
        }
        final double x2 = x0 - i2 + 0.16666666666666666;
        final double y2 = y0 - j2 + 0.16666666666666666;
        final double z2 = z0 - k2 + 0.16666666666666666;
        final double x3 = x0 - i3 + 0.3333333333333333;
        final double y3 = y0 - j3 + 0.3333333333333333;
        final double z3 = z0 - k3 + 0.3333333333333333;
        final double x4 = x0 - 1.0 + 0.5;
        final double y4 = y0 - 1.0 + 0.5;
        final double z4 = z0 - 1.0 + 0.5;
        final int ii = i & 0xFF;
        final int jj = j & 0xFF;
        final int kk = k & 0xFF;
        final int gi0 = this.variatedPermutationTable[ii + this.doubledPermutationTable[jj + this.doubledPermutationTable[kk]]];
        final int gi2 = this.variatedPermutationTable[ii + i2 + this.doubledPermutationTable[jj + j2 + this.doubledPermutationTable[kk + k2]]];
        final int gi3 = this.variatedPermutationTable[ii + i3 + this.doubledPermutationTable[jj + j3 + this.doubledPermutationTable[kk + k3]]];
        final int gi4 = this.variatedPermutationTable[ii + 1 + this.doubledPermutationTable[jj + 1 + this.doubledPermutationTable[kk + 1]]];
        double t2 = 0.6 - x0 * x0 - y0 * y0 - z0 * z0;
        double n0;
        if (t2 < 0.0) {
            n0 = 0.0;
        }
        else {
            t2 *= t2;
            n0 = t2 * t2 * dot(SimplexNoise.grad3[gi0], x0, y0, z0);
        }
        double t3 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2;
        double n2;
        if (t3 < 0.0) {
            n2 = 0.0;
        }
        else {
            t3 *= t3;
            n2 = t3 * t3 * dot(SimplexNoise.grad3[gi2], x2, y2, z2);
        }
        double t4 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3;
        double n3;
        if (t4 < 0.0) {
            n3 = 0.0;
        }
        else {
            t4 *= t4;
            n3 = t4 * t4 * dot(SimplexNoise.grad3[gi3], x3, y3, z3);
        }
        double t5 = 0.6 - x4 * x4 - y4 * y4 - z4 * z4;
        double n4;
        if (t5 < 0.0) {
            n4 = 0.0;
        }
        else {
            t5 *= t5;
            n4 = t5 * t5 * dot(SimplexNoise.grad3[gi4], x4, y4, z4);
        }
        return 32.0 * (n0 + n2 + n3 + n4);
    }

    public double noise(final double x, final double y, final double z, final double w) {
        final double s = (x + y + z + w) * SimplexNoise.F4;
        final int i = fastfloor(x + s);
        final int j = fastfloor(y + s);
        final int k = fastfloor(z + s);
        final int l = fastfloor(w + s);
        final double t = (i + j + k + l) * SimplexNoise.G4;
        final double X0 = i - t;
        final double Y0 = j - t;
        final double Z0 = k - t;
        final double W0 = l - t;
        final double x2 = x - X0;
        final double y2 = y - Y0;
        final double z2 = z - Z0;
        final double w2 = w - W0;
        int rankx = 0;
        int ranky = 0;
        int rankz = 0;
        int rankw = 0;
        if (x2 > y2) {
            ++rankx;
        }
        else {
            ++ranky;
        }
        if (x2 > z2) {
            ++rankx;
        }
        else {
            ++rankz;
        }
        if (x2 > w2) {
            ++rankx;
        }
        else {
            ++rankw;
        }
        if (y2 > z2) {
            ++ranky;
        }
        else {
            ++rankz;
        }
        if (y2 > w2) {
            ++ranky;
        }
        else {
            ++rankw;
        }
        if (z2 > w2) {
            ++rankz;
        }
        else {
            ++rankw;
        }
        final int i2 = (rankx >= 3) ? 1 : 0;
        final int j2 = (ranky >= 3) ? 1 : 0;
        final int k2 = (rankz >= 3) ? 1 : 0;
        final int l2 = (rankw >= 3) ? 1 : 0;
        final int i3 = (rankx >= 2) ? 1 : 0;
        final int j3 = (ranky >= 2) ? 1 : 0;
        final int k3 = (rankz >= 2) ? 1 : 0;
        final int l3 = (rankw >= 2) ? 1 : 0;
        final int i4 = (rankx >= 1) ? 1 : 0;
        final int j4 = (ranky >= 1) ? 1 : 0;
        final int k4 = (rankz >= 1) ? 1 : 0;
        final int l4 = (rankw >= 1) ? 1 : 0;
        final double x3 = x2 - i2 + SimplexNoise.G4;
        final double y3 = y2 - j2 + SimplexNoise.G4;
        final double z3 = z2 - k2 + SimplexNoise.G4;
        final double w3 = w2 - l2 + SimplexNoise.G4;
        final double x4 = x2 - i3 + 2.0 * SimplexNoise.G4;
        final double y4 = y2 - j3 + 2.0 * SimplexNoise.G4;
        final double z4 = z2 - k3 + 2.0 * SimplexNoise.G4;
        final double w4 = w2 - l3 + 2.0 * SimplexNoise.G4;
        final double x5 = x2 - i4 + 3.0 * SimplexNoise.G4;
        final double y5 = y2 - j4 + 3.0 * SimplexNoise.G4;
        final double z5 = z2 - k4 + 3.0 * SimplexNoise.G4;
        final double w5 = w2 - l4 + 3.0 * SimplexNoise.G4;
        final double x6 = x2 - 1.0 + 4.0 * SimplexNoise.G4;
        final double y6 = y2 - 1.0 + 4.0 * SimplexNoise.G4;
        final double z6 = z2 - 1.0 + 4.0 * SimplexNoise.G4;
        final double w6 = w2 - 1.0 + 4.0 * SimplexNoise.G4;
        final int ii = i & 0xFF;
        final int jj = j & 0xFF;
        final int kk = k & 0xFF;
        final int ll = l & 0xFF;
        final int gi0 = this.doubledPermutationTable[ii + this.doubledPermutationTable[jj + this.doubledPermutationTable[kk + this.doubledPermutationTable[ll]]]] % 32;
        final int gi2 = this.doubledPermutationTable[ii + i2 + this.doubledPermutationTable[jj + j2 + this.doubledPermutationTable[kk + k2 + this.doubledPermutationTable[ll + l2]]]] % 32;
        final int gi3 = this.doubledPermutationTable[ii + i3 + this.doubledPermutationTable[jj + j3 + this.doubledPermutationTable[kk + k3 + this.doubledPermutationTable[ll + l3]]]] % 32;
        final int gi4 = this.doubledPermutationTable[ii + i4 + this.doubledPermutationTable[jj + j4 + this.doubledPermutationTable[kk + k4 + this.doubledPermutationTable[ll + l4]]]] % 32;
        final int gi5 = this.doubledPermutationTable[ii + 1 + this.doubledPermutationTable[jj + 1 + this.doubledPermutationTable[kk + 1 + this.doubledPermutationTable[ll + 1]]]] % 32;
        double t2 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2;
        double n0;
        if (t2 < 0.0) {
            n0 = 0.0;
        }
        else {
            t2 *= t2;
            n0 = t2 * t2 * dot(SimplexNoise.grad4[gi0], x2, y2, z2, w2);
        }
        double t3 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3;
        double n2;
        if (t3 < 0.0) {
            n2 = 0.0;
        }
        else {
            t3 *= t3;
            n2 = t3 * t3 * dot(SimplexNoise.grad4[gi2], x3, y3, z3, w3);
        }
        double t4 = 0.6 - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4;
        double n3;
        if (t4 < 0.0) {
            n3 = 0.0;
        }
        else {
            t4 *= t4;
            n3 = t4 * t4 * dot(SimplexNoise.grad4[gi3], x4, y4, z4, w4);
        }
        double t5 = 0.6 - x5 * x5 - y5 * y5 - z5 * z5 - w5 * w5;
        double n4;
        if (t5 < 0.0) {
            n4 = 0.0;
        }
        else {
            t5 *= t5;
            n4 = t5 * t5 * dot(SimplexNoise.grad4[gi4], x5, y5, z5, w5);
        }
        double t6 = 0.6 - x6 * x6 - y6 * y6 - z6 * z6 - w6 * w6;
        double n5;
        if (t6 < 0.0) {
            n5 = 0.0;
        }
        else {
            t6 *= t6;
            n5 = t6 * t6 * dot(SimplexNoise.grad4[gi5], x6, y6, z6, w6);
        }
        return 27.0 * (n0 + n2 + n3 + n4 + n5);
    }

    static {
        grad3 = new Grad[] { new Grad(1.0, 1.0, 0.0), new Grad(-1.0, 1.0, 0.0), new Grad(1.0, -1.0, 0.0), new Grad(-1.0, -1.0, 0.0), new Grad(1.0, 0.0, 1.0), new Grad(-1.0, 0.0, 1.0), new Grad(1.0, 0.0, -1.0), new Grad(-1.0, 0.0, -1.0), new Grad(0.0, 1.0, 1.0), new Grad(0.0, -1.0, 1.0), new Grad(0.0, 1.0, -1.0), new Grad(0.0, -1.0, -1.0) };
        grad4 = new Grad[] { new Grad(0.0, 1.0, 1.0, 1.0), new Grad(0.0, 1.0, 1.0, -1.0), new Grad(0.0, 1.0, -1.0, 1.0), new Grad(0.0, 1.0, -1.0, -1.0), new Grad(0.0, -1.0, 1.0, 1.0), new Grad(0.0, -1.0, 1.0, -1.0), new Grad(0.0, -1.0, -1.0, 1.0), new Grad(0.0, -1.0, -1.0, -1.0), new Grad(1.0, 0.0, 1.0, 1.0), new Grad(1.0, 0.0, 1.0, -1.0), new Grad(1.0, 0.0, -1.0, 1.0), new Grad(1.0, 0.0, -1.0, -1.0), new Grad(-1.0, 0.0, 1.0, 1.0), new Grad(-1.0, 0.0, 1.0, -1.0), new Grad(-1.0, 0.0, -1.0, 1.0), new Grad(-1.0, 0.0, -1.0, -1.0), new Grad(1.0, 1.0, 0.0, 1.0), new Grad(1.0, 1.0, 0.0, -1.0), new Grad(1.0, -1.0, 0.0, 1.0), new Grad(1.0, -1.0, 0.0, -1.0), new Grad(-1.0, 1.0, 0.0, 1.0), new Grad(-1.0, 1.0, 0.0, -1.0), new Grad(-1.0, -1.0, 0.0, 1.0), new Grad(-1.0, -1.0, 0.0, -1.0), new Grad(1.0, 1.0, 1.0, 0.0), new Grad(1.0, 1.0, -1.0, 0.0), new Grad(1.0, -1.0, 1.0, 0.0), new Grad(1.0, -1.0, -1.0, 0.0), new Grad(-1.0, 1.0, 1.0, 0.0), new Grad(-1.0, 1.0, -1.0, 0.0), new Grad(-1.0, -1.0, 1.0, 0.0), new Grad(-1.0, -1.0, -1.0, 0.0) };
        F2 = 0.5 * (Math.sqrt(3.0) - 1.0);
        G2 = (3.0 - Math.sqrt(3.0)) / 6.0;
        F4 = (Math.sqrt(5.0) - 1.0) / 4.0;
        G4 = (5.0 - Math.sqrt(5.0)) / 20.0;
    }

    private static class Grad
    {
        double x;
        double y;
        double z;
        double w;

        Grad(final double x, final double y, final double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        Grad(final double x, final double y, final double z, final double w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }
    }
}
