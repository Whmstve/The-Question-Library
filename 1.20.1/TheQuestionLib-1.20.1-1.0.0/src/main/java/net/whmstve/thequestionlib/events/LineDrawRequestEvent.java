package net.whmstve.thequestionlib.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;

import java.awt.*;

public class LineDrawRequestEvent extends Event {
    private final BlockPos startingPosition;
    private final BlockPos endingPosition;
    
    public LineDrawRequestEvent(BlockPos startingPosition, BlockPos endingPosition) {
        this.startingPosition = startingPosition;
        this.endingPosition = endingPosition;
        
    }

    public BlockPos getStartingPosition() {
        return startingPosition;
    }

    public BlockPos getEndingPosition() {
        return endingPosition;
    }

    public static ColoredLineDrawRequestEvent.WithPlayer withPlayer(Player player, BlockPos startingPosition, BlockPos endingPosition, Color color){
        return new ColoredLineDrawRequestEvent.WithPlayer(player, startingPosition, endingPosition, color);
    }

    public static ColoredLineDrawRequestEvent.WithLevel withLevel(Level level, BlockPos startingPosition, BlockPos endingPosition, Color color){
        return new ColoredLineDrawRequestEvent.WithLevel(level, startingPosition, endingPosition, color);
    }

    public static ColoredLineDrawRequestEvent.WithPlayerAndLevel withPlayerAndLevel(Level level, Player player, BlockPos startingPosition, BlockPos endingPosition, Color color){
        return new ColoredLineDrawRequestEvent.WithPlayerAndLevel(level, player, startingPosition, endingPosition, color);
    }

    public static class WithPlayer extends PlayerEvent{
        private final BlockPos startingPosition;
        private final BlockPos endingPosition;
        

        public WithPlayer(Player player, BlockPos startingPosition, BlockPos endingPosition) {
            super(player);
            this.startingPosition = startingPosition;
            this.endingPosition = endingPosition;
            
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

        public WithLevel(Level level, BlockPos startingPosition, BlockPos endingPosition) {
            this.level = level;
            this.startingPosition = startingPosition;
            this.endingPosition = endingPosition;
            
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

        public WithPlayerAndLevel(Level level, Player player, BlockPos startingPosition, BlockPos endingPosition) {
            super(player);
            this.level = level;
            this.startingPosition = startingPosition;
            this.endingPosition = endingPosition;
            
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
