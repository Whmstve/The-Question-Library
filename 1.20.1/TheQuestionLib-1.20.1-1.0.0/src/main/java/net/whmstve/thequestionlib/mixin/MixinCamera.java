package net.whmstve.thequestionlib.mixin;

import net.minecraft.client.Camera;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.whmstve.thequestionlib.general.camera.client.CameraManager;
import net.whmstve.thequestionlib.general.camera.client.CameraMode;
import net.whmstve.thequestionlib.general.camera.client.ExtendedCamera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class MixinCamera implements ExtendedCamera {
    // Camera state
    @Shadow private boolean initialized;
    @Shadow private BlockGetter level;
    @Shadow private Entity entity;
    @Shadow private boolean detached;

    public MixinCamera() {}

    // Shadowed methods from Camera
    @Shadow protected abstract void setRotation(float pitch, float yaw);
    @Shadow protected abstract void setPosition(Vec3 position);
    @Shadow protected abstract void move(double x, double y, double z);
    @Shadow protected abstract double getMaxZoom(double zoom);
    @Shadow public abstract Vec3 getPosition();
    @Shadow private float xRot;
    @Shadow private float yRot;

    // Inject into setup
    @Inject(method = "setup", at = @At("HEAD"), cancellable = true)
    public void onSetup(BlockGetter level, Entity cameraEntity, boolean detached, boolean mirrored, float partialTicks, CallbackInfo ci) {
        CameraManager cameraManager = CameraManager.getInstance();
        if (cameraManager.isEnabled()) {
            CameraMode cameraMode = ExtendedCamera.getCameraMode();

            if (cameraManager.optionCameraMode.isEnabled()) {
                cameraManager.setup((Camera) (Object) this, level, cameraEntity, partialTicks);
            }

            this.onCustomSetup(cameraManager, cameraMode, level, cameraEntity, partialTicks);
            ci.cancel();
        }
    }

    // Inject into move
    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void onMove(double dx, double dy, double dz, CallbackInfo ci) {
        CameraManager cameraManager = CameraManager.getInstance();
        if (cameraManager.isEnabled()) {
            CameraMode cameraMode = ExtendedCamera.getCameraMode();
            if (cameraMode.isFree()) {
                ci.cancel();
            }
        }
    }

    // Custom camera setup
    public void onCustomSetup(CameraManager cameraManager, CameraMode cameraMode, BlockGetter level, Entity cameraEntity, float partialTicks) {
        this.initialized = true;
        this.level = level;
        this.entity = cameraEntity;
        this.detached = cameraMode.isDetached();

        Vec3 cameraPos = this.getPosition();
        Vec2 cameraRot = new Vec2(this.xRot, this.yRot);
        Vec3 cameraOffset = cameraMode.getOffset();

        // Standard entity-based camera position
        if (!cameraMode.isFree()) {
            if (cameraMode.getPosition() != null) {
                cameraPos = cameraMode.getPosition();
            } else {
                cameraPos = new Vec3(
                        Mth.lerp(partialTicks, cameraEntity.xOld, cameraEntity.getX()),
                        Mth.lerp(partialTicks, cameraEntity.yOld, cameraEntity.getY()) + Mth.lerp(partialTicks, this.xRot, this.yRot),
                        Mth.lerp(partialTicks, cameraEntity.zOld, cameraEntity.getZ())
                );
            }

            cameraRot = new Vec2(cameraEntity.getViewXRot(partialTicks), cameraEntity.getViewYRot(partialTicks));
        }

        // Override with CameraManager options
        if (cameraManager.optionCameraPosition.isEnabled()) {
            cameraPos = cameraManager.getPosition();
        }

        if (cameraManager.optionCameraRotation.isEnabled() || cameraManager.optionCameraLookAt.isEnabled()) {
            cameraRot = cameraManager.getGlobalRotation();
        }

        this.setRotation(
                new Vec2(
                        Mth.wrapDegrees(cameraMode.getRotation().x + cameraRot.x),
                        Mth.wrapDegrees(cameraMode.getRotation().y + cameraRot.y)
                ),
                cameraMode.isMirrored()
        );

        this.setPosition(cameraPos);
        this.move(cameraOffset.x, cameraOffset.y, cameraOffset.z);

        if (cameraMode.isDetached()) {
            this.move(-this.getMaxZoom(cameraMode.getZoomOut()), 0.0D, 0.0D);
        }

        // Special handling for sleeping entities
        if (!cameraMode.isDetached() && cameraEntity instanceof LivingEntity livingEntity) {
            if (livingEntity.isSleeping()) {
                Direction bedDirection = livingEntity.getBedOrientation();
                if (bedDirection != null) {
                    this.setRotation(new Vec2(0.0F, bedDirection.toYRot() - 180.0F), cameraMode.isMirrored());
                }
                this.move(0.0D, 0.3D, 0.0D);
            }
        }
    }

    @Override
    public void setRotation(Vec2 rotation, boolean mirrored) {
        this.setRotation(rotation.y, rotation.x);
        if (mirrored) {
            this.setRotation(this.xRot + 180.0F, -this.yRot);
        }
    }
}
