package net.whmstve.thequestionlib.general.camera.client;

import com.google.common.base.Suppliers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.whmstve.thequestionlib.general.camera.common.CameraParent;

import java.util.Objects;
import java.util.function.Supplier;

public class CameraMode {
    public static final Supplier<CameraMode> FIRST_PERSON;
    public static final Supplier<CameraMode> THIRD_PERSON;
    public static final Supplier<CameraMode> THIRD_PERSON_BACK;
    public static final Supplier<CameraMode> FREE;
    private ResourceLocation location;
    private CameraParent parent;
    private boolean detached;
    private boolean mirrored;
    private boolean bobView;
    private boolean free;
    private Vec2 rotation;
    private Vec3 position;
    private Vec3 offset;
    private double zoomOut;

    public CameraMode(CameraParent parent, ResourceLocation location) {
        this.parent = parent;
        this.location = location;
    }

    public CameraParent getParent() {
        return this.parent;
    }

    public ResourceLocation getLocation() {
        return this.location;
    }

    public boolean isDetached() {
        return this.detached;
    }

    public void setDetached(boolean detached) {
        this.detached = detached;
    }

    public boolean isMirrored() {
        return this.mirrored;
    }

    public void setMirrored(boolean mirrored) {
        this.mirrored = mirrored;
    }

    public boolean isBobView() {
        return this.bobView;
    }

    public void setBobView(boolean bobView) {
        this.bobView = bobView;
    }

    public boolean isFree() {
        return this.free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public Vec2 getRotation() {
        return this.rotation;
    }

    public void setRotation(Vec2 rotation) {
        this.rotation = rotation;
    }

    public Vec3 getOffset() {
        return this.offset;
    }

    public void setOffset(Vec3 offset) {
        this.offset = offset;
    }

    public Vec3 getPosition() {
        return this.position;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public double getZoomOut() {
        return this.zoomOut;
    }

    public void setZoomOut(double zoomOut) {
        this.zoomOut = zoomOut;
    }

    static {
        FIRST_PERSON = Suppliers.memoize(Objects.requireNonNull(CameraParent.FIRST_PERSON)::cameraMode);
        THIRD_PERSON = Suppliers.memoize(Objects.requireNonNull(CameraParent.THIRD_PERSON)::cameraMode);
        THIRD_PERSON_BACK = Suppliers.memoize(Objects.requireNonNull(CameraParent.THIRD_PERSON_BACK)::cameraMode);
        FREE = Suppliers.memoize(Objects.requireNonNull(CameraParent.FREE)::cameraMode);
    }
}
