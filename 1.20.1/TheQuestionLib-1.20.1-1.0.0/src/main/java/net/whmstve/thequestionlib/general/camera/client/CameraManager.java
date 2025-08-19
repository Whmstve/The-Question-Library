package net.whmstve.thequestionlib.general.camera.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.whmstve.thequestionlib.TheQuestionLibMod;
import net.whmstve.thequestionlib.general.camera.common.CameraLookTarget;
import net.whmstve.thequestionlib.general.camera.common.EasingType;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = TheQuestionLibMod.MOD_ID, value = {Dist.CLIENT})
@OnlyIn(Dist.CLIENT)
public class CameraManager {
    public Tweener positionTweener = new Tweener();
    public Tweener rotationTweener = new Tweener();
    private EasingType easingType;
    public ToggleableProperty<CameraMode> optionCameraMode;
    public ToggleableProperty<Vec3> optionCameraPosition;
    public ToggleableProperty<Vec2> optionCameraRotation;
    public ToggleableProperty<CameraLookTarget> optionCameraLookAt;
    public ToggleableProperty<Float> optionCameraEasingTime;
    public Vec3 position;
    public Vec2 rotation;
    public Vec2 lookAtRotation;

    public CameraManager() {
        this.easingType = EasingType.LINEAR;
        this.optionCameraMode = new ToggleableProperty();
        this.optionCameraPosition = new ToggleableProperty();
        this.optionCameraRotation = new ToggleableProperty();
        this.optionCameraLookAt = new ToggleableProperty();
        this.optionCameraEasingTime = (new ToggleableProperty()).onActivated((f) -> {
            Camera camera = Minecraft.getInstance().getEntityRenderDispatcher().camera;
            if (this.optionCameraPosition.isEnabled()) {
                Vec3 pos = this.getPosition();
                if (pos == null) {
                    pos = camera.getPosition();
                }

                this.positionTweener.tween(pos, (Vec3)this.optionCameraPosition.getValue(), this.easingType, (double)f);
            }

            if (this.optionCameraRotation.isEnabled()) {
                Vec2 rot = this.getRotation();
                if (rot == null) {
                    rot = new Vec2(camera.getXRot(), camera.getYRot());
                }

                this.rotationTweener.tween(rot, (Vec2)this.optionCameraRotation.getValue(), this.easingType, (double)f);
            }

        });
    }

    public void setup(Camera camera, BlockGetter level, Entity cameraEntity, float partialTick) {
        double deltaFrame = Minecraft.getInstance().getFrameTime();
        if (this.position == null) {
            this.position = camera.getPosition();
        }

        if (this.rotation == null) {
            this.rotation = new Vec2(0.0F, 0.0F);
        }

        if (this.lookAtRotation == null) {
            this.lookAtRotation = new Vec2(0.0F, 0.0F);
        }

        if (this.optionCameraPosition.isEnabled()) {
            Vec3 newPos = (Vec3)this.optionCameraPosition.getValue();
            if (this.positionTweener.isEnabled()) {
                this.position = (Vec3)this.positionTweener.update();
            } else {
                this.position = new Vec3(Mth.lerp(deltaFrame, this.position.x, newPos.x), Mth.lerp(deltaFrame, this.position.y, newPos.y), Mth.lerp(deltaFrame, this.position.z, newPos.z));
            }
        }

        Vec2 newRot;
        if (this.optionCameraLookAt.isEnabled()) {
            newRot = this.calculateLookAtRot();
            this.lookAtRotation = new Vec2(Mth.lerp((float)deltaFrame, this.lookAtRotation.x, newRot.x), Mth.lerp((float)deltaFrame, this.lookAtRotation.y, newRot.y));
        }

        if (this.optionCameraRotation.isEnabled()) {
            if (this.rotationTweener.isEnabled()) {
                this.rotation = (Vec2)this.rotationTweener.update();
            } else {
                newRot = (Vec2)this.optionCameraRotation.getValue();
                this.rotation = new Vec2(Mth.lerp((float)deltaFrame, this.rotation.x, newRot.x), Mth.lerp((float)deltaFrame, this.rotation.y, newRot.y));
            }
        }

    }

    public Vec2 calculateLookAtRot() {
        if (this.optionCameraLookAt.isEnabled()) {
            float partialTick = Minecraft.getInstance().getPartialTick();
            CameraLookTarget lookTarget = (CameraLookTarget)this.optionCameraLookAt.getValue();
            Vec3 lookAtPos = new Vec3(0.0D, 0.0D, 0.0D);
            if (lookTarget.isLookingAtEntity()) {
                Entity lookAtEntity = Minecraft.getInstance().level.getEntity(lookTarget.getLookAtEntityId());
                if (lookAtEntity == null) {
                    this.optionCameraLookAt.deactivate();
                } else {
                    lookAtPos = new Vec3(Mth.lerp((double)partialTick, lookAtEntity.xo, lookAtEntity.getX()), Mth.lerp((double)partialTick, lookAtEntity.yo, lookAtEntity.getY()) + (double)(lookAtEntity.getBbHeight() / 2.0F), Mth.lerp((double)partialTick, lookAtEntity.zo, lookAtEntity.getZ()));
                }
            } else if (lookTarget.isLookingAtPos()) {
                lookAtPos = lookTarget.getLookAtPos();
            }

            Vec3 diff = new Vec3((double)((float)(lookAtPos.x - this.position.x)), (double)((float)(lookAtPos.y - this.position.y)), (double)((float)(lookAtPos.z - this.position.z)));
            float xRot = (float)Mth.wrapDegrees(-(Mth.atan2(diff.y, (double)((float)Math.sqrt(diff.x * diff.x + diff.z * diff.z))) * 57.2957763671875D));
            float yRot = (float)Mth.wrapDegrees(Mth.atan2(diff.z, diff.x) * 57.2957763671875D) - 90.0F;
            return new Vec2(xRot, yRot);
        } else {
            return new Vec2(0.0F, 0.0F);
        }
    }

    public void clear() {
        this.lookAtRotation = null;
        this.position = null;
        this.rotation = null;
        this.easingType = EasingType.LINEAR;
        this.positionTweener = new Tweener();
        this.rotationTweener = new Tweener();
        this.optionCameraMode.deactivate();
        this.optionCameraPosition.deactivate();
        this.optionCameraRotation.deactivate();
        this.optionCameraEasingTime.deactivate();
        this.optionCameraLookAt.deactivate();
        CameraFade.getInstance().clear();
    }

    public boolean isEnabled() {
        return ExtendedOptions.isCameraCommandEnabled();
    }

    public void setEasingType(EasingType easingType) {
        this.easingType = easingType;
    }

    public Vec3 getPosition() {
        return this.position;
    }

    public Vec2 getRotation() {
        return this.rotation;
    }

    public Vec2 getGlobalRotation() {
        float xRot;
        float yRot;
        if (this.optionCameraLookAt.isEnabled()) {
            xRot = Mth.wrapDegrees(this.lookAtRotation.x);
            yRot = Mth.wrapDegrees(this.lookAtRotation.y);
        } else {
            xRot = Mth.wrapDegrees(this.rotation.x);
            yRot = Mth.wrapDegrees(this.rotation.y);
        }

        return new Vec2(xRot, yRot);
    }

    public EasingType getEasingType() {
        return this.easingType;
    }

    public static CameraManager getInstance() {
        return TheQuestionLibMod.getCameraManager();
    }

    @SubscribeEvent
    public static void onCameraFov(ComputeFovModifierEvent e) {
        if (getInstance().isEnabled() && ExtendedCamera.getCameraMode().isFree()) {
            e.setNewFovModifier(1.0F);
        }

    }
}
