package net.whmstve.thequestionlib.general.intr;

import net.minecraft.core.BlockPos;

import java.awt.*;
import java.util.List;


public interface ILaserable {
    BlockPos mainPosition();
    List<BlockPos> targetPositions();
    Color getLaserColor();
}
