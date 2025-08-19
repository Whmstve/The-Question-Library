package net.whmstve.thequestionlib.general.content.packets.networks;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent.Context;
import net.whmstve.thequestionlib.general.camera.client.CameraFade;
import net.whmstve.thequestionlib.general.camera.client.CameraManager;
import net.whmstve.thequestionlib.general.camera.client.CameraMode;
import net.whmstve.thequestionlib.general.camera.client.ExtendedOptions;
import net.whmstve.thequestionlib.general.camera.common.CameraLookTarget;
import net.whmstve.thequestionlib.general.camera.common.EasingType;

public abstract class CameraMessageHandle {
    public CameraMessageHandle() {
    }

    public abstract void encode(FriendlyByteBuf var1);

    public abstract void accept(Context var1);

    public static class SetLookAt extends CameraMessageHandle {
        private CameraLookTarget lookTarget;

        public SetLookAt(CameraLookTarget lookTarget) {
            this.lookTarget = lookTarget;
        }

        public void encode(FriendlyByteBuf buffer) {
            this.lookTarget.encode(buffer);
        }

        public void accept(Context context) {
            CameraManager.getInstance().optionCameraLookAt.activate(this.lookTarget);
        }
    }

    public static class SetRotation extends CameraMessageHandle {
        private Vec2 rotation;

        public SetRotation(Vec2 rotation) {
            this.rotation = rotation;
        }

        public void encode(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.rotation.x);
            buffer.writeFloat(this.rotation.y);
        }

        public void accept(Context context) {
            CameraManager.getInstance().optionCameraRotation.activate(this.rotation);
        }
    }

    public static class SetPosition extends CameraMessageHandle {
        private Vec3 position;

        public SetPosition(Vec3 position) {
            this.position = position;
        }

        public void encode(FriendlyByteBuf buffer) {
            buffer.writeFloat((float)this.position.x);
            buffer.writeFloat((float)this.position.y);
            buffer.writeFloat((float)this.position.z);
        }

        public void accept(Context context) {
            CameraManager properties = CameraManager.getInstance();
            properties.optionCameraPosition.activate(this.position);
        }
    }

    public static class SetEasing extends CameraMessageHandle {
        private EasingType easingType;
        private float easeTime;

        public SetEasing(EasingType easingType, float easeTime) {
            this.easingType = easingType;
            this.easeTime = easeTime;
        }

        public void encode(FriendlyByteBuf buffer) {
            buffer.writeEnum(this.easingType);
            buffer.writeFloat(this.easeTime);
        }

        public void accept(Context context) {
            CameraManager cameraManager = CameraManager.getInstance();
            cameraManager.setEasingType(this.easingType);
            cameraManager.optionCameraEasingTime.activate(this.easeTime);
        }
    }

    public static class Default extends CameraMessageHandle {
        public Default() {
        }

        public void encode(FriendlyByteBuf buffer) {
        }

        public void accept(Context context) {
            CameraManager.getInstance().optionCameraRotation.deactivate();
            CameraManager.getInstance().optionCameraPosition.deactivate();
        }
    }

    public static class SetMode extends CameraMessageHandle {
        private CameraMode cameraMode;

        public SetMode(CameraMode cameraMode) {
            this.cameraMode = cameraMode;
        }

        public void encode(FriendlyByteBuf buffer) {
            buffer.writeResourceLocation(this.cameraMode.getLocation());
        }

        public void accept(Context context) {
            CameraManager.getInstance().optionCameraMode.activate(this.cameraMode);
        }
    }

    public static class FadeColor extends CameraMessageHandle {
        private int r;
        private int g;
        private int b;

        public FadeColor(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public void encode(FriendlyByteBuf buffer) {
            buffer.writeInt(this.r);
            buffer.writeInt(this.g);
            buffer.writeInt(this.b);
        }

        public void accept(Context context) {
            CameraFade.getInstance().color(this.r, this.g, this.b);
        }
    }

    public static class FadeTimings extends CameraMessageHandle {
        private int fadeInSeconds;
        private int holdSeconds;
        private int fadeOutSeconds;

        public FadeTimings(int fadeInSeconds, int holdSeconds, int fadeOutSeconds) {
            this.fadeInSeconds = fadeInSeconds;
            this.holdSeconds = holdSeconds;
            this.fadeOutSeconds = fadeOutSeconds;
        }

        public void encode(FriendlyByteBuf buffer) {
            buffer.writeInt(this.fadeInSeconds);
            buffer.writeInt(this.holdSeconds);
            buffer.writeInt(this.fadeOutSeconds);
        }

        public void accept(Context context) {
            CameraFade.getInstance().timings(this.fadeInSeconds, this.holdSeconds, this.fadeOutSeconds);
        }
    }

    public static class Clear extends CameraMessageHandle {
        private boolean disableFreeCam;

        public Clear(boolean disableFreeCam) {
            this.disableFreeCam = disableFreeCam;
        }

        public void encode(FriendlyByteBuf buffer) {
            buffer.writeBoolean(this.disableFreeCam);
        }

        public void accept(Context context) {
            CameraManager.getInstance().clear();
            if (this.disableFreeCam) {
                ExtendedOptions.disableFreecam();
            }

        }
    }
}
