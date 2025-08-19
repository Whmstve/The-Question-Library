package net.whmstve.thequestionlib.general.camera.common;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class CameraLookTarget {
    private Vec3 lookAtPos = null;
    private int lookAtEntityId = -1;

    public CameraLookTarget() {
    }

    public CameraLookTarget lookAtPos(Vec3 lookAtPos) {
        this.lookAtPos = lookAtPos;
        this.lookAtEntityId = -1;
        return this;
    }

    public CameraLookTarget lookAtEntity(int lookAtEntityId) {
        this.lookAtEntityId = lookAtEntityId;
        this.lookAtPos = null;
        return this;
    }

    public boolean isLookingAtEntity() {
        return this.lookAtEntityId != -1;
    }

    public boolean isLookingAtPos() {
        return this.lookAtPos != null;
    }

    public Vec3 getLookAtPos() {
        return this.lookAtPos;
    }

    public int getLookAtEntityId() {
        return this.lookAtEntityId;
    }

    public void encode(FriendlyByteBuf buffer) {
        if (this.isLookingAtPos()) {
            buffer.writeInt(0);
            buffer.writeFloat((float)this.lookAtPos.x);
            buffer.writeFloat((float)this.lookAtPos.y);
            buffer.writeFloat((float)this.lookAtPos.z);
        } else if (this.isLookingAtEntity()) {
            buffer.writeInt(1);
            buffer.writeInt(this.lookAtEntityId);
        }

    }

    public static CameraLookTarget decode(FriendlyByteBuf buffer) {
        int id = buffer.readInt();
        if (id == 0) {
            return (new CameraLookTarget()).lookAtPos(new Vec3(buffer.readFloat(), buffer.readFloat(), buffer.readFloat()));
        } else {
            return id == 1 ? (new CameraLookTarget()).lookAtEntity(buffer.readInt()) : null;
        }
    }
}