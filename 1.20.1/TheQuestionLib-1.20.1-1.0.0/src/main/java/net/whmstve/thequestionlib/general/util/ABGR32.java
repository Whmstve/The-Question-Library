package net.whmstve.thequestionlib.general.util;

public class ABGR32 {
      public static int alpha(int pPackedColor) {
         return pPackedColor >>> 24;
      }

      public static int red(int pPackedColor) {
         return pPackedColor & 255;
      }

      public static int green(int pPackedColor) {
         return pPackedColor >> 8 & 255;
      }

      public static int blue(int pPackedColor) {
         return pPackedColor >> 16 & 255;
      }

      public static int transparent(int pPackedColor) {
         return pPackedColor & 16777215;
      }

      public static int opaque(int pPackedColor) {
         return pPackedColor | -16777216;
      }

      public static int color(int pAlpha, int pBlue, int pGreen, int pRed) {
         return pAlpha << 24 | pBlue << 16 | pGreen << 8 | pRed;
      }

      public static int color(int pAlpha, int pPackedColor) {
         return pAlpha << 24 | pPackedColor & 16777215;
      }
   }