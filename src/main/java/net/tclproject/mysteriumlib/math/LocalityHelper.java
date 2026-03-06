package net.tclproject.mysteriumlib.math;

public class LocalityHelper
{
    public static int locality(final int x, final int y, final int seed, final int width) {
        return localitySingle(x, y, seed, width) * 7 + localitySingle(x, y, seed, width / 2) * 4 + localitySingle(x, y, seed, width / 4);
    }

    public static int localitySingle(final int x, final int y, final int seed, final int width) {
        final int qa = localityAxis(x, seed, width);
        final int qb = localityAxis(y, seed, width);
        return (Math.abs(qa - 6) > Math.abs(qb - 6)) ? qa : qb;
    }

    public static int localityAxis(final int coordinate, final int seed, final int width) {
        int q = Math.abs(coordinate) + Math.abs(seed);
        int q2 = q / width % width;
        q %= width;
        final int q3 = (q2 + 1) * 21 % 13;
        q2 = q2 * 21 % 13;
        return (q3 - q2) * q / width + q2;
    }
}
