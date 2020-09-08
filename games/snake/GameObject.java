package com.javarush.games.snake;

import com.javarush.engine.cell.*;

public class GameObject {
    public int x;
    public int y;
    public boolean isAlive = true;
    public GameObject(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void draw(Game game){
        game.setCellValueEx(x, y, Color.NONE, "", Color.GRAY, 75);
    }
}