package net.whmstve.thequestionlib.general.camera.common;

import net.minecraft.resources.ResourceLocation;
import net.whmstve.thequestionlib.general.camera.client.CameraMode;
import net.whmstve.thequestionlib.general.content.ServerCameraModeManager;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CameraParent {
    FIRST_PERSON("first_person"),
    THIRD_PERSON("third_person"),
    THIRD_PERSON_BACK("third_person_back"),
    FREE("free");

    private static final Map<ResourceLocation,CameraParent> location2parent = createMapping(e->e.location);

    private final ResourceLocation location;
    CameraParent(String name) {
        location = new ResourceLocation(name);
    }

    public ResourceLocation getResourceLocation() {
        return location;
    }

    public static CameraParent fromLocation(String location){
        ResourceLocation resource = new ResourceLocation(location);
        if(location2parent.containsKey(resource)){
            return location2parent.get(resource);
        }else{
            throw new RuntimeException("Invalid parent: "+location);
        }
    }

    private static <T> Map<T, CameraParent> createMapping(Function<CameraParent,T> keyGetting) {
        return Arrays.stream(values()).collect(Collectors.toMap(keyGetting,e->e));
    }

    public CameraMode cameraMode(){return ServerCameraModeManager.getCameraMode(this.location);}
}
