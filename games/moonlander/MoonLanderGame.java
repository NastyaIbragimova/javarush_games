package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;

public class MoonLanderGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private Rocket rocket;
    private GameObject landscape;
    private GameObject platform;
    private boolean isUpPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private boolean isGameStopped;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
        showGrid(false);
    }

    private void createGame() {
        createGameObjects();
        setTurnTimer(50);
        isUpPressed = false;
        isLeftPressed = false;
        isRightPressed = false;
        isGameStopped = false;
        score=1000;
        drawScene();

    }

    private void drawScene() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                setCellColor(j, i, Color.WHITESMOKE);
            }
        }
        rocket.draw(this);
        landscape.draw(this);
    }

    private void createGameObjects() {
        rocket = new Rocket(WIDTH / 2, 0);
        landscape = new GameObject(0, 25, ShapeMatrix.LANDSCAPE);
        platform = new GameObject(23, MoonLanderGame.HEIGHT - 1, ShapeMatrix.PLATFORM);
    }

    @Override
    public void onTurn(int step) {
        if(score>0){
            score--;
        }
        rocket.move(isUpPressed, isLeftPressed, isRightPressed);
        check();
        setScore(score);
        drawScene();
    }
    /*
3. Метод onTurn(int) должен уменьшать значение поля score на единицу, если значение поля score положительное.
4. Метод onTurn(int) должен вызвать метод setScore(int) с параметром score после вызова метода check().*/

    @Override
    public void setCellColor(int x, int y, com.javarush.engine.cell.Color color) {
        if ((x < WIDTH && x > 0) && (y < HEIGHT && y > 0)) {
            super.setCellColor(x, y, color);
        }
    }

    @Override
    public void onKeyPress(Key key) {
        if (key.equals(Key.RIGHT)) {
            isRightPressed = true;
            isLeftPressed = false;
        } else if (key.equals(Key.LEFT)) {
            isLeftPressed = true;
            isRightPressed = false;
        } else if (key.equals(Key.UP)) {
            isUpPressed = true;
        }
        if(key.equals(Key.SPACE)){
            if(isGameStopped){
                createGame();
            }
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if (key.equals(Key.RIGHT)) {
            isRightPressed = false;
        } else if (key.equals(Key.LEFT)) {
            isLeftPressed = false;
        } else if (key.equals(Key.UP)) {
            isUpPressed = false;
        }
    }

    private void check() {
        if (rocket.isCollision(platform) && rocket.isStopped()) {
            win();
        } else if (rocket.isCollision(landscape)) {
            gameOver();
        }
    }

    private void gameOver() {
        rocket.crash();
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "You lose!", Color.RED, 45);
        stopTurnTimer();
        score=0;
        setScore(score);
    }

    private void win() {
        rocket.land();
        isGameStopped = true;
        showMessageDialog(Color.YELLOW, "You win!", Color.BLACK, 45);
        stopTurnTimer();
    }
}
