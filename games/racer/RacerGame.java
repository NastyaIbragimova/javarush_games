package com.javarush.games.racer;

import com.javarush.engine.cell.*;
import com.javarush.games.racer.road.RoadManager;

public class RacerGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int CENTER_X = WIDTH / 2;
    public static final int ROADSIDE_WIDTH = 14;
    private static final int RACE_GOAL_CARS_COUNT = 40;
    private RoadMarking roadMarking;
    private PlayerCar player;
    private RoadManager roadManager;
    private boolean isGameStopped;
    private FinishLine finishLine;
    private ProgressBar progressBar;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        showGrid(false);
        createGame();
    }

    private void createGame() {
        roadMarking = new RoadMarking();
        player = new PlayerCar();
        setTurnTimer(40);
        roadManager = new RoadManager();
        isGameStopped = false;
        finishLine = new FinishLine();
        progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
        score = 3500;
        drawScene();
    }

    private void drawScene() {
        drawField();
        roadMarking.draw(this);
        player.draw(this);
        roadManager.draw(this);
        finishLine.draw(this);
        progressBar.draw(this);
    }

    private void drawField() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (x == CENTER_X) {
                    setCellColor(x, y, Color.WHITE);
                } else if ((x >= ROADSIDE_WIDTH) && (x < WIDTH - ROADSIDE_WIDTH)) {
                    setCellColor(x, y, Color.GREY);
                } else {
                    setCellColor(x, y, Color.YELLOWGREEN);
                }
            }
        }
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if ((x < WIDTH && x >= 0) && (y < HEIGHT && y >= 0)) {
            super.setCellColor(x, y, color);
        }
    }

    private void moveAll() {
        roadMarking.move(player.speed);
        player.move();
        roadManager.move(player.speed);
        finishLine.move(player.speed);
        progressBar.move(roadManager.getPassedCarsCount());
    }
    @Override
    public void onTurn(int step) {
        if (roadManager.getPassedCarsCount() >= RACE_GOAL_CARS_COUNT) {
            finishLine.show();
        }
        if(finishLine.isCrossed(player)){
            win();
        }
        else if (roadManager.checkCrush(player)) {
            gameOver();
        } else {
            moveAll();
            roadManager.generateNewRoadObjects(this);
            score-=5;
            setScore(score);
        }
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        if (key.equals(Key.LEFT)) {
            player.setDirection(Direction.LEFT);
        } else if (key.equals(Key.RIGHT)) {
            player.setDirection(Direction.RIGHT);
        } else if (key.equals(Key.SPACE)) {
            if (isGameStopped) {
                createGame();
            }
        } else if (key.equals(Key.UP)) {
            player.speed = 2;
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if (key.equals(Key.RIGHT) && player.getDirection().equals(Direction.RIGHT)) {
            player.setDirection(Direction.NONE);
        } else if (key.equals(Key.LEFT) && player.getDirection().equals(Direction.LEFT)) {
            player.setDirection(Direction.NONE);
        } else if (key.equals(Key.UP)) {
            player.speed = 1;
        }
    }

    private void gameOver() {
        isGameStopped = true;
        stopTurnTimer();
        player.stop();
        showMessageDialog(Color.WHITE, "You lose", Color.RED, 50);
    }

    private void win() {
        isGameStopped = true;
        stopTurnTimer();
        showMessageDialog(Color.WHITE, "You win", Color.GREEN, 50);
    }
}
