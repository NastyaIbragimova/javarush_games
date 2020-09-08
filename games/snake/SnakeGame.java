package com.javarush.games.snake;

import com.javarush.engine.cell.*;

public class SnakeGame extends Game {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private static final int GOAL = 30;
    private int score;
    private Snake snake;
    private GameObject gameObject;
    private int turnDelay;
    private boolean isGameStopped;

    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    //отрисовка экрана
    private void drawScene() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                setCellValueEx(j, i, Color.FLORALWHITE, "");
            }
        }
        snake.draw(this);
       // createNewGameObject();
        gameObject.draw(this);

    }

    //создание игры
    private void createGame() {
        score = 0;
        setScore(score);
        turnDelay = 300;
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        createNewGameObject();
        isGameStopped = false;
        setTurnTimer(turnDelay);
        showGrid(false);
        drawScene();
    }

    @Override
    public void onTurn(int step) {
        snake.move(gameObject);
        if (!gameObject.isAlive && gameObject.getClass().equals(Apple.class)) {
            score += 15;
            setScore(score);
            turnDelay -= 5;
            setTurnTimer(turnDelay);
            createNewGameObject();
        } else if (!gameObject.isAlive && gameObject.getClass().equals(Mouse.class)) {
            score += 25;
            setScore(score);
            turnDelay -= 10;
            setTurnTimer(turnDelay);
            createNewGameObject();
        } else if (!gameObject.isAlive && gameObject.getClass().equals(Rabbit.class)) {
            score += 50;
            setScore(score);
            createNewGameObject();
        }

        if (!snake.isAlive) {
            gameOver();
        }
        if (snake.getLength() > GOAL) {
            win();
        }

        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key) {
            case UP:
                snake.setDirection(Direction.UP);
                break;
            case DOWN:
                snake.setDirection(Direction.DOWN);
                break;
            case RIGHT:
                snake.setDirection(Direction.RIGHT);
                break;
            case LEFT:
                snake.setDirection(Direction.LEFT);
                break;
            case SPACE:
                if (isGameStopped) {
                    createGame();
                }
                break;
        }
    }

    private void createNewGameObject() {
        int random = getRandomNumber(5);
        if (random == 1) {
            gameObject = new Rabbit(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
            while (snake.checkCollision(gameObject)) {
                gameObject = new Rabbit(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
            }
        } else if (random > 1 && random < 4) {
            gameObject = new Apple(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
            while (snake.checkCollision(gameObject)) {
                gameObject = new Apple(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
            }
        } else {
            gameObject = new Mouse(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
            while (snake.checkCollision(gameObject)) {
                gameObject = new Mouse(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
            }
        }
    }

    private void gameOver() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.RED, "Game over :(", Color.WHITE, 35);
    }

    private void win() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.BLUE, "You win:)", Color.WHITE, 35);
    }
}