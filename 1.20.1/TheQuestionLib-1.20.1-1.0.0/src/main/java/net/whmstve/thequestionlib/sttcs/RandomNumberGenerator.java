package net.whmstve.thequestionlib.sttcs;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public class RandomNumberGenerator {
    private static final Random rng = new Random();

    @SuppressWarnings("unchecked")
    public static <N extends Number> N getRandomValue(N min, N max, N minStep, N maxStep) {
        if (min instanceof Byte) {
            int lower = min.byteValue() + minStep.byteValue();
            int upper = max.byteValue() - maxStep.byteValue();
            return (N) Byte.valueOf((byte) getRandomInt(lower, upper));
        } else if (min instanceof Short) {
            int lower = min.shortValue() + minStep.shortValue();
            int upper = max.shortValue() - maxStep.shortValue();
            return (N) Short.valueOf((short) getRandomInt(lower, upper));
        } else if (min instanceof Integer) {
            int lower = min.intValue() + minStep.intValue();
            int upper = max.intValue() - maxStep.intValue();
            return (N) Integer.valueOf(getRandomInt(lower, upper));
        } else if (min instanceof Long) {
            long lower = min.longValue() + minStep.longValue();
            long upper = max.longValue() - maxStep.longValue();
            return (N) Long.valueOf(getRandomLong(lower, upper));
        } else if (min instanceof Float) {
            float lower = min.floatValue() + minStep.floatValue();
            float upper = max.floatValue() - maxStep.floatValue();
            return (N) Float.valueOf(getRandomFloat(lower, upper));
        } else if (min instanceof Double) {
            double lower = min.doubleValue() + minStep.doubleValue();
            double upper = max.doubleValue() - maxStep.doubleValue();
            return (N) Double.valueOf(getRandomDouble(lower, upper));
        } else if (min instanceof BigInteger) {
            BigInteger lower = ((BigInteger) min).add((BigInteger) minStep);
            BigInteger upper = ((BigInteger) max).subtract((BigInteger) maxStep);
            return (N) getRandomBigInteger(lower, upper);
        } else if (min instanceof BigDecimal) {
            BigDecimal lower = ((BigDecimal) min).add((BigDecimal) minStep);
            BigDecimal upper = ((BigDecimal) max).subtract((BigDecimal) maxStep);
            return (N) getRandomBigDecimal(lower, upper);
        }

        throw new UnsupportedOperationException("Unsupported number type: " + min.getClass());
    }

    public static <N extends Number> N createOpened(N minInclusive, N maxInclusive) {
        return getRandomValue(minInclusive, maxInclusive, getMinStep(minInclusive), getMaxStep(maxInclusive));
    }

    public static <N extends Number> N createClosed(N minInclusive, N maxExclusive) {
        return getRandomValue(minInclusive, maxExclusive, getZeroStep(minInclusive), getMaxStep(maxExclusive));
    }

    public static <N extends Number> N createBetweenOpened(N minExclusive, N maxInclusive) {
        return getRandomValue(minExclusive, maxInclusive, getMinStep(minExclusive), getZeroStep(maxInclusive));
    }

    public static <N extends Number> N createBetweenClosed(N minExclusive, N maxExclusive) {
        return getRandomValue(minExclusive, maxExclusive, getMinStep(minExclusive), getMaxStep(maxExclusive));
    }

    // ========= Helpers =========

    private static int getRandomInt(int min, int max) {
        if (min > max) throw new IllegalArgumentException("Invalid range: " + min + " > " + max);
        return rng.nextInt(max - min + 1) + min;
    }

    private static long getRandomLong(long min, long max) {
        if (min > max) throw new IllegalArgumentException("Invalid range: " + min + " > " + max);
        return min + (long) (rng.nextDouble() * (max - min + 1));
    }

    private static float getRandomFloat(float min, float max) {
        if (min > max) throw new IllegalArgumentException("Invalid range: " + min + " > " + max);
        return (float) (rng.nextDouble() * (max - min) + min);
    }

    private static double getRandomDouble(double min, double max) {
        if (min > max) throw new IllegalArgumentException("Invalid range: " + min + " > " + max);
        return rng.nextDouble() * (max - min) + min;
    }

    private static BigInteger getRandomBigInteger(BigInteger min, BigInteger max) {
        if (min.compareTo(max) > 0) throw new IllegalArgumentException("Invalid BigInteger range.");
        BigInteger range = max.subtract(min).add(BigInteger.ONE);
        int bitLength = range.bitLength();
        BigInteger result;
        do {
            result = new BigInteger(bitLength, rng);
        } while (result.compareTo(range) >= 0);
        return result.add(min);
    }

    private static BigDecimal getRandomBigDecimal(BigDecimal min, BigDecimal max) {
        if (min.compareTo(max) > 0) throw new IllegalArgumentException("Invalid BigDecimal range.");
        BigDecimal range = max.subtract(min);
        BigDecimal result = min.add(range.multiply(BigDecimal.valueOf(rng.nextDouble())));
        return result;
    }

    @SuppressWarnings("unchecked")
    private static <N extends Number> N getZeroStep(N number) {
        if (number instanceof Byte) return (N) Byte.valueOf((byte) 0);
        if (number instanceof Short) return (N) Short.valueOf((short) 0);
        if (number instanceof Integer) return (N) Integer.valueOf(0);
        if (number instanceof Long) return (N) Long.valueOf(0L);
        if (number instanceof Float) return (N) Float.valueOf(0f);
        if (number instanceof Double) return (N) Double.valueOf(0d);
        if (number instanceof BigInteger) return (N) BigInteger.ZERO;
        if (number instanceof BigDecimal) return (N) BigDecimal.ZERO;
        throw new UnsupportedOperationException("Unsupported type for zero step: " + number.getClass());
    }

    @SuppressWarnings("unchecked")
    private static <N extends Number> N getMinStep(N number) {
        if (number instanceof Byte) return (N) Byte.valueOf((byte) 1);
        if (number instanceof Short) return (N) Short.valueOf((short) 1);
        if (number instanceof Integer) return (N) Integer.valueOf(1);
        if (number instanceof Long) return (N) Long.valueOf(1L);
        if (number instanceof Float) return (N) Float.valueOf(Float.MIN_VALUE);
        if (number instanceof Double) return (N) Double.valueOf(Double.MIN_VALUE);
        if (number instanceof BigInteger) return (N) BigInteger.ONE;
        if (number instanceof BigDecimal) return (N) BigDecimal.valueOf(1e-15); // precision-based
        throw new UnsupportedOperationException("Unsupported type for min step: " + number.getClass());
    }

    private static <N extends Number> N getMaxStep(N number) {
        return getMinStep(number);
    }
}
