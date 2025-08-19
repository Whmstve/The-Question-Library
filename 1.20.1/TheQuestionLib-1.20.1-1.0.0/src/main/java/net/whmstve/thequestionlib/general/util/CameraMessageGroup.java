package net.whmstve.thequestionlib.general.util;

import net.whmstve.thequestionlib.general.content.packets.networks.CameraMessageHandle;
import net.whmstve.thequestionlib.general.content.packets.networks.CameraMessageType;
import oshi.util.tuples.Pair;

public record CameraMessageGroup(CameraMessageType type, CameraMessageHandle handle) {
    public Pair<CameraMessageType,CameraMessageHandle> pair(){
        return new Pair<>(type,handle);
    }
}
