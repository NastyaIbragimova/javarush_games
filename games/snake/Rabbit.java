package com.javarush.games.snake;

import com.javarush.engine.cell.*;

public class Rabbit extends GameObject {
    private static final String RABBIT_SIGN = "\uD83D\uDC30";

    public Rabbit(int x, int y) {
        super(x, y);
        isAlive = true;
    }

    @Override
    public void draw(Game game) {
        game.setCellValueEx(x, y, Color.NONE, RABBIT_SIGN, Color.BLACK, 75);
    }
}
