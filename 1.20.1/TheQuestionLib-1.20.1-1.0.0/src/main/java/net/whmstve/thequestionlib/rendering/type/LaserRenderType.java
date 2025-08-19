package net.whmstve.thequestionlib.rendering.type;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.whmstve.thequestionlib.TheQuestionLibMod;

public class LaserRenderType extends RenderType {

    public LaserRenderType(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    private final static ResourceLocation RL_LASER = new ResourceLocation(TheQuestionLibMod.MOD_ID, "textures/effect/laser.png");
    private final static ResourceLocation RL_BEAM = new ResourceLocation(TheQuestionLibMod.MOD_ID, "textures/effect/beam.png");
    private final static ResourceLocation RL_GLOW = new ResourceLocation(TheQuestionLibMod.MOD_ID, "textures/effect/glow.png");
    public static final RenderType LASER_MAIN_BEAM = create("MAIN_",
            DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder().setTextureState(new TextureStateShard(RL_BEAM, false, false))
                    .setShaderState(ShaderStateShard.POSITION_COLOR_TEX_SHADER)
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(NO_DEPTH_TEST)
                    .setCullState(NO_CULL)
                    .setLightmapState(NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false));
    public static final RenderType LASER_MAIN_ADDITIVE = create("LaserAdditiveBeam",
            DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder().setTextureState(new TextureStateShard(RL_GLOW, false, false))
                    .setShaderState(ShaderStateShard.POSITION_COLOR_TEX_SHADER)
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(NO_DEPTH_TEST)
                    .setCullState(NO_CULL)
                    .setLightmapState(NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false));
    public static final RenderType LASER_MAIN_CORE = create("LaserCoreBeam",
            DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false,
            RenderType.CompositeState.builder().setTextureState(new TextureStateShard(RL_LASER, false, false))
                    .setShaderState(ShaderStateShard.POSITION_COLOR_TEX_SHADER)
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(NO_DEPTH_TEST)
                    .setCullState(NO_CULL)
                    .setLightmapState(NO_LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false));
}
