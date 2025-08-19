package net.whmstve.thequestionlib.general.camera.client;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public abstract class TweenValue<T> {
    private final T value;

    public TweenValue(T value) {
        this.value = value;
    }

    public abstract T tween(TweenValue<T> to, double t);

    public T getValue() {
        return this.value;
    }

    public static TweenValue<Vec3> vec3(Vec3 vec3) {
        return new TweenValue<Vec3>(vec3) {
            @Override
            public Vec3 tween(TweenValue<Vec3> to, double t) {
                return new Vec3(
                        Mth.lerp(t, this.getValue().x, to.getValue().x),
                        Mth.lerp(t, this.getValue().y, to.getValue().y),
                        Mth.lerp(t, this.getValue().z, to.getValue().z)
                );
            }
        };
    }

    public static TweenValue<Vec2> vec2(Vec2 vec2) {
        return new TweenValue<Vec2>(vec2) {
            @Override
            public Vec2 tween(TweenValue<Vec2> to, double t) {
                return new Vec2(
                        Mth.lerp((float) t, this.getValue().x, to.getValue().x),
                        Mth.lerp((float) t, this.getValue().y, to.getValue().y)
                );
            }
        };
    }
}
