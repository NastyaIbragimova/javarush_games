package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.gameobjects.Bullet;
import com.javarush.games.spaceinvaders.gameobjects.EnemyFleet;
import com.javarush.games.spaceinvaders.gameobjects.PlayerShip;
import com.javarush.games.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpaceInvadersGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int COMPLEXITY = 5;
    private static final int PLAYER_BULLETS_MAX = 3;
    private List<Star> stars;
    private EnemyFleet enemyFleet;
    private List<Bullet> enemyBullets;
    private PlayerShip playerShip;
    private boolean isGameStopped;
    private int animationsCount;
    private List<Bullet> playerBullets;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void drawField() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                setCellValueEx(j, i, Color.BLACK, "");
            }
        }
        for (Star star : stars) {
            star.draw(this);
        }
        showGrid(false);

    }

    private void drawScene() {
        drawField();
        enemyFleet.draw(this);
        for (Bullet bullet : enemyBullets) {
            bullet.draw(this);
        }
        for (Bullet bullet : playerBullets) {
            bullet.draw(this);
        }
        playerShip.draw(this);

    }

    private void createGame() {
        createStars();
        enemyFleet = new EnemyFleet();
        enemyBullets = new ArrayList<>();
        playerBullets = new ArrayList<>();
        playerShip = new PlayerShip();
        isGameStopped = false;
        animationsCount = 0;
        score=0;
        setScore(score);
        setTurnTimer(40);
        drawScene();

    }

    private void createStars() {
        stars = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            stars.add(new Star(getRandomNumber(WIDTH), getRandomNumber(HEIGHT)));
        }
    }

    @Override
    public void onTurn(int step) {
        moveSpaceObjects();
        check();
        Bullet bullet = enemyFleet.fire(this);
        if (bullet != null) {
            enemyBullets.add(bullet);
        }
        setScore(score);
        drawScene();
    }

    private void moveSpaceObjects() {
        enemyFleet.move();
        for (Bullet bullet : enemyBullets) {
            bullet.move();
        }
        for (Bullet bullet : playerBullets) {
            bullet.move();
        }
        playerShip.move();
    }

    private void removeDeadBullets() {
        Iterator iterator = enemyBullets.iterator();
        while (iterator.hasNext()) {
            Bullet item = (Bullet) iterator.next();
            if (!item.isAlive || item.y >= HEIGHT - 1) {
                iterator.remove();
            }
        }
        Iterator iterator2 = playerBullets.iterator();
        while (iterator2.hasNext()) {
            Bullet item = (Bullet) iterator2.next();
            if (!item.isAlive || item.y + item.height < 0) {
                iterator2.remove();
            }
        }
    }

    private void check() {
        playerShip.verifyHit(enemyBullets);
        score+=enemyFleet.verifyHit(playerBullets);
        enemyFleet.deleteHiddenShips();
        removeDeadBullets();
        if (!playerShip.isAlive) {
            stopGameWithDelay();
        }

        if (enemyFleet.getBottomBorder() >= playerShip.y) {
                    playerShip.kill();
        }
        if(enemyFleet.getShipsCount()==0){
            playerShip.win();
            stopGameWithDelay();
        }
    }
    /* В методе check() значение поля score должно быть увеличено на результат, который вернул вызов метода verifyHit(List<Bullet>) у объекта enemyFleet.*/
    private void stopGame(boolean isWin) {
        isGameStopped = true;
        stopTurnTimer();
        if (isWin) {
            showMessageDialog(Color.WHITE, "You win!", Color.GREEN, 50);
        } else {
            showMessageDialog(Color.WHITE, "You lose!", Color.RED, 50);
        }
    }

    private void stopGameWithDelay() {
        animationsCount++;
        if (animationsCount >= 10) {
            stopGame(playerShip.isAlive);
        }
    }

    @Override
    public void onKeyPress(Key key) {
        if (isGameStopped && key.equals(Key.SPACE)) {
            createGame();
        } else if (key.equals(Key.LEFT)) {
            playerShip.setDirection(Direction.LEFT);
        } else if (key.equals(Key.RIGHT)) {
            playerShip.setDirection(Direction.RIGHT);
        } else if (key.equals(Key.SPACE)) {
            Bullet bullet = playerShip.fire();
            if (bullet != null && playerBullets.size() < PLAYER_BULLETS_MAX) {
                playerBullets.add(bullet);
            }

        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if (key.equals(Key.LEFT) && playerShip.getDirection().equals(Direction.LEFT)) {
            playerShip.setDirection(Direction.UP);
        } else if (key.equals(Key.RIGHT) && playerShip.getDirection().equals(Direction.RIGHT)) {
            playerShip.setDirection(Direction.UP);
        }
    }

    @Override
    public void setCellValueEx(int x, int y, Color color, String text) {
        if (x > 0 && x < WIDTH && y > 0 && y < HEIGHT) {
            super.setCellValueEx(x, y, color, text);
        }
    }
}

