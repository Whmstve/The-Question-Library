package net.whmstve.thequestionlib.general.content;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.whmstve.thequestionlib.general.camera.client.CameraMode;
import net.whmstve.thequestionlib.general.camera.common.CameraParent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerCameraModeManager extends SimpleJsonResourceReloadListener {
    private static Set<CameraMode> cameraModes = new HashSet<>();
    private static boolean loaded;
    private static final Gson GSON = (new GsonBuilder()).create();
    private static final String folder = "camera_modes";

    public ServerCameraModeManager() {
        super(GSON, folder);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        cameraModes = new HashSet<>();
        for (Map.Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
            JsonElement element = entry.getValue();
            try{
                if (element.isJsonObject()){
                    JsonObject object = element.getAsJsonObject();
                    String inherits = GsonHelper.getAsString(object,"inherits_from","minecaft:third_person");
                    boolean detached = GsonHelper.getAsBoolean(object,"detached",false);
                    boolean mirrored = GsonHelper.getAsBoolean(object,"mirrored",false);
                    boolean bobView = GsonHelper.getAsBoolean(object,"bobView",false);
                    boolean free = GsonHelper.getAsBoolean(object,"free",false);
                    double zoomOut = GsonHelper.getAsDouble(object,"zoomOut", 4.0D);
                    Vec2 rotation = Vec2.ZERO;
                    Vec3 offset = Vec3.ZERO;
                    Vec3 position = null;
                    JsonArray array;
                    if(object.has("rotation")){
                        array = object.getAsJsonArray("rotation");
                        rotation = new Vec2(array.get(0).getAsFloat(), array.get(1).getAsFloat());
                    }
                    if(object.has("offset")) {
                        array = object.getAsJsonArray("offset");
                        offset = new Vec3(array.get(0).getAsDouble(),array.get(1).getAsDouble(),array.get(2).getAsDouble());
                    }
                    if (object.has("position")) {
                        array = object.getAsJsonArray("position");
                        position = new Vec3(array.get(0).getAsDouble(), array.get(1).getAsDouble(), array.get(2).getAsDouble());
                    }

                    CameraMode cameraMode = new CameraMode(CameraParent.fromLocation(inherits), entry.getKey());
                    cameraMode.setDetached(detached);
                    cameraMode.setBobView(bobView);
                    cameraMode.setFree(free);
                    cameraMode.setRotation(rotation);
                    cameraMode.setOffset(offset);
                    cameraMode.setPosition(position);
                    cameraMode.setZoomOut(zoomOut);
                    cameraModes.add(cameraMode);
                }
            }catch (JsonParseException exception){
                throw new RuntimeException("Invalid camera mode JSON: "+exception);
            }

            loaded = true;
        }
    }

    public static CameraMode getCameraMode(ResourceLocation location){
        return cameraModes.stream().filter(cameraMode -> cameraMode.getLocation().equals(location)).findFirst()
                .orElseThrow(()->new RuntimeException("Invalid camera mode: "+location));
    }

    public static Set<CameraMode> getCameraModes(){
        return cameraModes;
    }

    public static boolean isLoaded(){
        return loaded;
    }
}
