package net.whmstve.thequestionlib.general.camera.client;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.whmstve.thequestionlib.TheQuestionLibMod;
import net.whmstve.thequestionlib.general.util.ARGB32;

public class CameraFade {
    private int fadeInTime;
    private int holdTime;
    private int fadeOutTime;
    private int totalFadeTime;
    private int red = 255;
    private int green = 255;
    private int blue = 255;
    private boolean render;

    public CameraFade() {
    }

    public void render(GuiGraphics pGuiGraphics, float pPartialTicks){
        if(ExtendedOptions.isCameraCommandEnabled()){
            Window window = Minecraft.getInstance().getWindow();
            if(this.render){
                int alpha = 255;
                float totalTick = (float) this.totalFadeTime - pPartialTicks;
                if(this.totalFadeTime > this.fadeOutTime + this.holdTime){
                    float resultTick = (float)(this.fadeInTime + this.holdTime + this.fadeOutTime) - totalTick;
                    alpha = (int) (resultTick * 255.0f / (float)this.fadeInTime);
                }

                if(this.totalFadeTime <= this.fadeOutTime){
                    alpha = (int) (totalTick * 255.0f / (float) this.fadeOutTime);
                }

                alpha = Mth.lerpInt(alpha, 0, 255);
                pGuiGraphics.fill(0,0, window.getWidth(), window.getHeight(), ARGB32.color(alpha,this.red,this.green,this.blue));
            }
        }
    }

    public void tick() {
        if (this.totalFadeTime > 0) {
            --this.totalFadeTime;
            if (this.totalFadeTime <= 0) {
                this.render = false;
                this.red = 255;
                this.green = 255;
                this.blue = 255;
            }
        }
    }

    public void color(int red, int green, int blue) {
        if (this.render) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
    }

    public void timings(int fadeInTime, int holdTime, int fadeOutTime) {
        if (fadeInTime >= 0) {
            this.fadeInTime = fadeInTime;
        }

        if (holdTime >= 0) {
            this.holdTime = holdTime;
        }

        if (fadeOutTime >= 0) {
            this.fadeOutTime = fadeOutTime;
        }

        this.totalFadeTime = fadeInTime + holdTime + fadeOutTime;
        this.render = true;
    }

    public void clear() {
        this.render = false;
        this.red = 255;
        this.green = 255;
        this.blue = 255;
        this.fadeInTime = 0;
        this.fadeOutTime = 0;
        this.holdTime = 0;
        this.totalFadeTime = 0;
    }

    public static CameraFade getInstance() {
        return TheQuestionLibMod.getCameraFade();
    }
}
