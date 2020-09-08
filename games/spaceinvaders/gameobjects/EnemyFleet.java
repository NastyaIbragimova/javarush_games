package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnemyFleet {
    private static final int ROWS_COUNT = 3;
    private static final int COLUMNS_COUNT = 10;
    private static final int STEP = ShapeMatrix.ENEMY.length + 1;
    private List<EnemyShip> ships;
    private Direction direction = Direction.RIGHT;

    public EnemyFleet() {
        createShips();
    }

    private void createShips() {
        ships = new ArrayList<>();
        for (int y = 0; y < ROWS_COUNT; y++) {
            for (int x = 0; x < COLUMNS_COUNT; x++) {
                EnemyShip enemyShip = new EnemyShip(x * STEP, y * STEP +12);
                ships.add(enemyShip);
            }
        }
        ships.add(new Boss(STEP * COLUMNS_COUNT / 2 - ShapeMatrix.BOSS_ANIMATION_FIRST.length / 2 - 1 , 5) );
    }
    public void draw(Game game){
        for (EnemyShip ship : ships) {
            ship.draw(game);
        }
    }
    private double getLeftBorder(){
        double min = SpaceInvadersGame.WIDTH;
        for (EnemyShip ship:ships) {
            if(ship.x < min){
                min=ship.x;
            }
        }
        return min;
    }
    private double getRightBorder(){
        double max = 0;
        for (EnemyShip ship:ships) {
            if(ship.x > max){
                max=ship.x;
            }
        }
        return max + ships.get(0).width;
    }
    private double getSpeed(){
        return Math.min(2.0, (3.0 / ships.size()));
    }
    public void move(){
        if(ships.size()>0){
            if(direction.equals(Direction.LEFT) && getLeftBorder()<0){
                direction=Direction.RIGHT;
                for (EnemyShip ship:ships) {
                    ship.move(Direction.DOWN, getSpeed());
                }
            }
            else if(direction.equals(Direction.RIGHT) && getRightBorder()>SpaceInvadersGame.WIDTH){
                direction=Direction.LEFT;
                for (EnemyShip ship:ships) {
                    ship.move(Direction.DOWN, getSpeed());
                }
            }
            else {
                for (EnemyShip ship:ships) {
                    ship.move(direction, getSpeed());
                }
            }
        }
    }
    public Bullet fire(Game game){
        if(ships.size()>0){
           int random = game.getRandomNumber(100/SpaceInvadersGame.COMPLEXITY);
            if(random>0){
                return null;
            }
            else {
               return ships.get(game.getRandomNumber(ships.size())).fire();
            }
        }
        else return null;
    }
    public int verifyHit(List<Bullet> bullets){
        int score=0;
        if (bullets.size() == 0) {
            return 0;
        }
        for (EnemyShip ship : ships) {
            for (Bullet bullet : bullets) {
                if (ship.isCollision(bullet)){
                    if(ship.isAlive && bullet.isAlive){
                        ship.kill();
                        score+=ship.score;
                        bullet.kill();
                    }
                }
            }
        }

        return score;
    }

    public void deleteHiddenShips(){
        Iterator iterator = ships.iterator();
        while (iterator.hasNext()) {
            Ship item = (Ship) iterator.next();
            if (!item.isVisible()) {
                iterator.remove();
            }
        }
    }

    public double getBottomBorder(){
        double max=0;
        for (EnemyShip enemyShip:ships) {
            if((enemyShip.y+enemyShip.height)>max){
                max = enemyShip.y+enemyShip.height;
            }
        }
        return max;
    }
    public int getShipsCount(){
        return ships.size();
    }
}
