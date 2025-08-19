package net.whmstve.thequestionlib.general.camera.common;

import net.minecraft.util.Mth;

import java.util.function.Function;
import java.util.stream.Stream;

public enum EasingType {
    LINEAR((t) -> {
        return t;
    }),
    SPRING((t) -> {
        return 1.0D - Math.cos(t * 3.141592653589793D * 4.0D) * Math.exp(-t * 6.0D);
    }),
    IN_QUAD((t) -> {
        return t * t;
    }),
    OUT_QUAD((t) -> {
        return -t * (t - 2.0D);
    }),
    IN_OUT_QUAD((t) -> {
        return t < 0.5D ? 2.0D * t * t : -2.0D * t * t + 4.0D * t - 1.0D;
    }),
    IN_CUBIC((t) -> {
        return t * t * t;
    }),
    OUT_CUBIC((t) -> {
        return t = t - 1.0D * t * t + 1.0D;
    }),
    IN_OUT_CUBIC((t) -> {
        t = t * 2.0D;
        if (t < 1.0D) {
            return 0.5D * t * t * t;
        } else {
            t = t - 2.0D;
            return 0.5D * (t * t * t + 2.0D);
        }
    }),
    IN_QUART((t) -> {
        return t * t * t * t;
    }),
    OUT_QUART((t) -> {
        return 1.0D - Math.pow(1.0D - t, 4.0D);
    }),
    IN_OUT_QUART((t) -> {
        return t < 0.5D ? 8.0D * t * t * t * t : 1.0D - Math.pow(-2.0D * t + 2.0D, 4.0D) / 2.0D;
    }),
    IN_QUINT((t) -> {
        return t * t * t * t * t;
    }),
    OUT_QUINT((t) -> {
        return t = t - 1.0D * t * t * t * t + 1.0D;
    }),
    IN_OUT_QUINT((t) -> {
        return t = t * 2.0D < 1.0D ? 0.5D * t * t * t * t * t : 0.5D * (t = t - 2.0D * t * t * t * t + 2.0D);
    }),
    IN_SINE((t) -> {
        return 1.0D - Math.cos(t * 3.141592653589793D / 2.0D);
    }),
    OUT_SINE((t) -> {
        return Math.sin(t * 3.141592653589793D / 2.0D);
    }),
    IN_OUT_SINE((t) -> {
        return -0.5D * (Math.cos(3.141592653589793D * t) - 1.0D);
    }),
    IN_EXPO((t) -> {
        return t == 0.0D ? 0.0D : Math.pow(2.0D, 10.0D * (t - 1.0D));
    }),
    OUT_EXPO((t) -> {
        return t == 1.0D ? 1.0D : 1.0D - Math.pow(2.0D, -10.0D * t);
    }),
    IN_OUT_EXPO((t) -> {
        if (t == 0.0D) {
            return 0.0D;
        } else if (t == 1.0D) {
            return 1.0D;
        } else {
            return t < 0.5D ? 0.5D * Math.pow(2.0D, 20.0D * t - 10.0D) : -0.5D * Math.pow(2.0D, -20.0D * t + 10.0D) + 1.0D;
        }
    }),
    IN_CIRC((t) -> {
        return 1.0D - Math.sqrt(1.0D - Math.pow(t, 2.0D));
    }),
    OUT_CIRC((t) -> {
        return Math.sqrt(1.0D - Math.pow(t - 1.0D, 2.0D));
    }),
    IN_OUT_CIRC((t) -> {
        return t < 0.5D ? (1.0D - Math.sqrt(1.0D - Math.pow(2.0D * t, 2.0D))) / 2.0D : (Math.sqrt(1.0D - Math.pow(-2.0D * t + 2.0D, 2.0D)) + 1.0D) / 2.0D;
    }),
    OUT_BOUNCE((t) -> {
        if (t < 0.36363636363636365D) {
            return 121.0D * t * t / 16.0D;
        } else if (t < 0.7272727272727273D) {
            return 9.075D * t * t - 9.9D * t + 3.4D;
        } else {
            return t < 0.9D ? 12.066481994459833D * t * t - 19.63545706371191D * t + 8.898060941828255D : 10.8D * t * t - 20.52D * t + 10.72D;
        }
    }),
    IN_BOUNCE((t) -> {
        return 1.0D - OUT_BOUNCE.ease(1.0D - t);
    }),
    IN_OUT_BOUNCE((t) -> {
        return t < 0.5D ? 0.5D * IN_BOUNCE.ease(t * 2.0D) : 0.5D * OUT_BOUNCE.ease(t * 2.0D - 1.0D) + 0.5D;
    }),
    IN_BACK((t) -> {
        return t * t * (2.70158D * t - 1.70158D);
    }),
    OUT_BACK((t) -> {
        return t = t - 1.0D * t * (2.70158D * t + 1.70158D) + 1.0D;
    }),
    IN_OUT_BACK((t) -> {
        double s = 2.5949095D;
        return t = t * 2.0D < 1.0D ? 0.5D * t * t * ((s + 1.0D) * t - s) : 0.5D * (t = t - 2.0D * t * ((s + 1.0D) * t + s) + 2.0D);
    }),
    IN_ELASTIC((t) -> {
        double c4 = 2.0943951023931953D;
        return t == 0.0D ? 0.0D : (t == 1.0D ? 1.0D : -Math.pow(2.0D, 10.0D * t - 10.0D) * Math.sin((t * 10.0D - 10.75D) * c4));
    }),
    OUT_ELASTIC((t) -> {
        double c4 = 2.0943951023931953D;
        return t == 0.0D ? 0.0D : (t == 1.0D ? 1.0D : Math.pow(2.0D, -10.0D * t) * Math.sin((t * 10.0D - 0.75D) * c4) + 1.0D);
    }),
    IN_OUT_ELASTIC((t) -> {
        double c5 = 1.3962634015954636D;
        double sin = Math.sin((20.0D * t - 11.125D) * c5);
        return t == 0.0D ? 0.0D : (t == 1.0D ? 1.0D : (t < 0.5D ? -(Math.pow(2.0D, 20.0D * t - 10.0D) * sin) / 2.0D : Math.pow(2.0D, -20.0D * t + 10.0D) * sin / 2.0D + 1.0D));
    });

    private Function<Double, Double> function;

    EasingType(Function<Double, Double> function) {
        this.function = function;
    }

    public double ease(double value) {
        return (Double)this.function.apply(Mth.lerp(value, -1.0D, 1.0D));
    }

    private String argumentName() {
        return this.toString().toLowerCase();
    }

    public static Stream<String> names() {
        return Stream.of(values()).map(EasingType::argumentName);
    }
}