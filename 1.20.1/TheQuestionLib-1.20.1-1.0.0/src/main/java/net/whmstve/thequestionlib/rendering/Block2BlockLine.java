package net.whmstve.thequestionlib.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.whmstve.thequestionlib.general.util.Transform;
import net.whmstve.thequestionlib.rendering.type.LaserRenderType;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class Block2BlockLine {
    private static float calculateFlickerModifier(long gameTime) {
        return 0.9f + 0.1f * Mth.sin(gameTime * 0.99f) * Mth.sin(gameTime * 0.3f) * Mth.sin(gameTime * 0.1f);
    }

    public static void render(RenderLevelStageEvent event, float r, float g, float b, float thickness, Player player, Transform from, Transform to) {
        Minecraft mc = Minecraft.getInstance();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        VertexConsumer builder = buffer.getBuffer(LaserRenderType.LASER_MAIN_ADDITIVE);
        PoseStack matrix = event.getPoseStack();
        Vec3 view = mc.gameRenderer.getMainCamera().getPosition();

        matrix.pushPose();
        matrix.translate(-view.x, -view.y, -view.z); // World-relative

        PoseStack.Pose pose = matrix.last();
        Matrix4f positionMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        // Beam flicker/thickness
        long gameTime = player.level().getGameTime();
        float flicker = calculateFlickerModifier(gameTime);
        float additiveThickness = (thickness * 3.5f) * flicker;

        List<Transform> line = from.draw(to);
        for (int i = 0; i < line.size() - 1; i++) {
            Transform start = line.get(i);
            Transform end = line.get(i + 1);

            drawLine(start, end, builder, positionMatrix, normalMatrix, additiveThickness, 0f, 1f, r, g, b, 1f);
        }

        matrix.popPose();
    }


    private static void drawLine(Transform start, Transform end, VertexConsumer builder, Matrix4f positionMatrix, Matrix3f normalMatrix, float thickness, float v1, float v2, float r, float g, float b, float alpha) {
        Vector3f normal = new Vector3f(0.0f, 1.0f, 0.0f);
        normal.mul(normalMatrix);

        Transform direction = end.subtract(start).normalize();
        Transform up = new Transform(0, 1, 0);
        Transform right = direction.cross(up).normalize().scale(thickness);

        Transform bottomOffset = right.reverse();

        Vector4f p1 = new Vector4f((float) (start.x() + bottomOffset.x()), (float) (start.y() + bottomOffset.y()), (float) (start.z() + bottomOffset.z()), 1.0f);
        Vector4f p2 = new Vector4f((float) (end.x() + bottomOffset.x()), (float) (end.y() + bottomOffset.y()), (float) (end.z() + bottomOffset.z()), 1.0f);
        Vector4f p3 = new Vector4f((float) (end.x() + right.x()), (float) (end.y() + right.y()), (float) (end.z() + right.z()), 1.0f);
        Vector4f p4 = new Vector4f((float) (start.x() + right.x()), (float) (start.y() + right.y()), (float) (start.z() + right.z()), 1.0f);

        // Transform the vertices to screen space
        p1.mul(positionMatrix);
        p2.mul(positionMatrix);
        p3.mul(positionMatrix);
        p4.mul(positionMatrix);

        // Front face
        builder.vertex(p4.x(), p4.y(), p4.z(), r, g, b, alpha, 0, v1, OverlayTexture.NO_OVERLAY, 15728880, normal.x(), normal.y(), normal.z());
        builder.vertex(p3.x(), p3.y(), p3.z(), r, g, b, alpha, 0, v2, OverlayTexture.NO_OVERLAY, 15728880, normal.x(), normal.y(), normal.z());
        builder.vertex(p2.x(), p2.y(), p2.z(), r, g, b, alpha, 1, v2, OverlayTexture.NO_OVERLAY, 15728880, normal.x(), normal.y(), normal.z());
        builder.vertex(p1.x(), p1.y(), p1.z(), r, g, b, alpha, 1, v1, OverlayTexture.NO_OVERLAY, 15728880, normal.x(), normal.y(), normal.z());

        // Back face (to ensure visibility from both sides)
        builder.vertex(p1.x(), p1.y(), p1.z(), r, g, b, alpha, 1, v1, OverlayTexture.NO_OVERLAY, 15728880, normal.x(), normal.y(), normal.z());
        builder.vertex(p2.x(), p2.y(), p2.z(), r, g, b, alpha, 1, v2, OverlayTexture.NO_OVERLAY, 15728880, normal.x(), normal.y(), normal.z());
        builder.vertex(p3.x(), p3.y(), p3.z(), r, g, b, alpha, 0, v2, OverlayTexture.NO_OVERLAY, 15728880, normal.x(), normal.y(), normal.z());
        builder.vertex(p4.x(), p4.y(), p4.z(), r, g, b, alpha, 0, v1, OverlayTexture.NO_OVERLAY, 15728880, normal.x(), normal.y(), normal.z());
    }

}
