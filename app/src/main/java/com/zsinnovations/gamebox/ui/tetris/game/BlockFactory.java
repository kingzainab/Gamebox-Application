package com.zsinnovations.gamebox.ui.tetris.game;

import com.zsinnovations.gamebox.ui.tetris.blocks.IShape;
import com.zsinnovations.gamebox.ui.tetris.blocks.JShape;
import com.zsinnovations.gamebox.ui.tetris.blocks.LShape;
import com.zsinnovations.gamebox.ui.tetris.blocks.OShape;
import com.zsinnovations.gamebox.ui.tetris.blocks.SShape;
import com.zsinnovations.gamebox.ui.tetris.blocks.Shape;
import com.zsinnovations.gamebox.ui.tetris.blocks.TShape;
import com.zsinnovations.gamebox.ui.tetris.blocks.ZShape;
import com.zsinnovations.gamebox.ui.tetris.constants.ShapeName;

public class BlockFactory {
    /**
     * Singleton Pattern - Only one blockFactory should exist within a game
     * Factory Pattern - Use for generating new block
     */
    private static BlockFactory blockFactory = new BlockFactory();
    private BlockFactory(){};
    public static BlockFactory getBlockFactory() {
        return blockFactory;
    }
    public Shape getShape(int shapeType) {
        if (shapeType < 1 || shapeType > 7) {
            return null;
        }
        switch(shapeType) {
            case ShapeName.I_SHAPE:
                return new IShape();
            case ShapeName.J_SHAPE:
                return new JShape();
            case ShapeName.L_SHAPE:
                return new LShape();
            case ShapeName.O_SHAPE:
                return new OShape();
            case ShapeName.S_SHAPE:
                return new SShape();
            case ShapeName.T_SHAPE:
                return new TShape();
            case ShapeName.Z_SHAPE:
                return new ZShape();
        }
        return null;
    }


}
