package net.whmstve.thequestionlib.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;

import java.awt.*;

public class ColoredLineDrawRequestEvent extends Event {
    private final BlockPos startingPosition;
    private final BlockPos endingPosition;
    private final Color color;

    public ColoredLineDrawRequestEvent(BlockPos startingPosition, BlockPos endingPosition, Color color) {
        this.color = color;
        this.startingPosition = startingPosition;
        this.endingPosition = endingPosition;
    }

    public Color getColor() {
        return color;
    }
    
    public BlockPos getStartingPosition() {
        return startingPosition;
    }

    public BlockPos getEndingPosition() {
        return endingPosition;
    }
    
    public static WithPlayer withPlayer(Player player, BlockPos startingPosition, BlockPos endingPosition, Color color){
        return new WithPlayer(player, startingPosition, endingPosition, color);
    }
    
    public static WithLevel withLevel(Level level, BlockPos startingPosition, BlockPos endingPosition, Color color){
        return new WithLevel(level, startingPosition, endingPosition, color);
    }
    
    public static WithPlayerAndLevel withPlayerAndLevel(Level level, Player player, BlockPos startingPosition, BlockPos endingPosition, Color color){
        return new WithPlayerAndLevel(level, player, startingPosition, endingPosition, color);
    }

    public static class WithPlayer extends PlayerEvent {
        private final BlockPos startingPosition;
        private final BlockPos endingPosition;

        private final Color color;

        public WithPlayer(Player player, BlockPos startingPosition, BlockPos endingPosition, Color color) {
            super(player);
            this.color = color;
            this.startingPosition = startingPosition;
            this.endingPosition = endingPosition;
        }

        public Color getColor() {
            return color;
        }

        public BlockPos getStartingPosition() {
            return startingPosition;
        }

        public BlockPos getEndingPosition() {
            return endingPosition;
        }
    }

    public static class WithLevel extends Event {
        private final BlockPos startingPosition;
        private final BlockPos endingPosition;

        private final Level level;
        private final Color color;

        public WithLevel(Level level, BlockPos startingPosition, BlockPos endingPosition, Color color) {
            this.color = color;
            this.level = level;
            this.startingPosition = startingPosition;
            this.endingPosition = endingPosition;
        }

        public Color getColor() {
            return color;
        }

        public Level getLevel() {
            return level;
        }

        public BlockPos getStartingPosition() {
            return startingPosition;
        }

        public BlockPos getEndingPosition() {
            return endingPosition;
        }
    }

    public static class WithPlayerAndLevel extends PlayerEvent{
        private final BlockPos startingPosition;
        private final BlockPos endingPosition;

        private final Level level;
        private final Color color;

        public WithPlayerAndLevel(Level level, Player player, BlockPos startingPosition, BlockPos endingPosition, Color color) {
            super(player);
            this.color = color;
            this.level = level;
            this.startingPosition = startingPosition;
            this.endingPosition = endingPosition;
        }

        public Color getColor() {
            return color;
        }

        public Level getLevel() {
            return level;
        }


        public BlockPos getStartingPosition() {
            return startingPosition;
        }

        public BlockPos getEndingPosition() {
            return endingPosition;
        }
    }
}

