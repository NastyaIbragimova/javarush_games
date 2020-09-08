package com.javarush.games.snake;

import com.javarush.engine.cell.*;

public class Mouse extends GameObject {
    private static final String  MOUSE_SIGN = "\uD83D\uDC2D";

    public Mouse(int x, int y) {
        super(x, y);
    }
    public void draw(Game game){
        game.setCellValueEx(x, y, Color.NONE, MOUSE_SIGN, Color.DARKGOLDENROD, 75);
    }
}
