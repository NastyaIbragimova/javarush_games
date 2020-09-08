package com.javarush.games.snake;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<GameObject> snakeParts = new ArrayList<>();
    private static final String HEAD_SIGN = "\uD83D\uDC32";
    private static final String BODY_SIGN = "\uD83D\uDCA2";
    public boolean isAlive = true;
    private Direction direction = Direction.LEFT;

    public Snake(int x, int y) {
        GameObject gameObject1 = new GameObject(x, y);
        GameObject gameObject2 = new GameObject(x + 1, y);
        GameObject gameObject3 = new GameObject(x + 2, y);
        snakeParts.add(gameObject1);
        snakeParts.add(gameObject2);
        snakeParts.add(gameObject3);
    }

    public void draw(Game game) {
        for (int i = 0; i < snakeParts.size(); i++) {
            if (isAlive) {
                if (i == 0) {
                    game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, HEAD_SIGN, Color.BLUEVIOLET, 75);
                } else {
                    game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN, Color.BLUEVIOLET, 75);
                }
            } else {
                if (i == 0) {
                    game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, HEAD_SIGN, Color.RED, 75);
                } else {
                    game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN, Color.RED, 75);
                }
            }
        }
    }

    public void setDirection(Direction direction) {
        switch (direction) {
            case LEFT:
                if (!this.direction.equals(Direction.RIGHT)) {
                    if (snakeParts.get(0).y == snakeParts.get(1).y) {
                        break;
                    } else {
                        this.direction = direction;
                        break;
                    }
                } else break;
            case RIGHT:
                if (!this.direction.equals(Direction.LEFT)) {
                    if (snakeParts.get(0).y == snakeParts.get(1).y) {
                        break;
                    } else {
                        this.direction = direction;
                        break;
                    }
                } else break;
            case UP:
                if (!this.direction.equals(Direction.DOWN)) {
                    if (snakeParts.get(0).x == snakeParts.get(1).x) {
                        break;
                    } else {
                        this.direction = direction;
                        break;
                    }
                } else break;
            case DOWN:
                if (!this.direction.equals(Direction.UP)) {
                    if (snakeParts.get(0).x == snakeParts.get(1).x) {
                        break;
                    } else {
                        this.direction = direction;
                        break;
                    }
                } else break;
        }
    }

    public void move(GameObject gameObject) {
        GameObject newHead = createNewHead();
        if (newHead.x < 0 || newHead.x >= SnakeGame.WIDTH || newHead.y < 0 || newHead.y >= SnakeGame.HEIGHT) {
            isAlive = false;
        } else {
            if (checkCollision(newHead)) {
                isAlive = false;
            }
            else {
                snakeParts.add(0, newHead);
                if (gameObject.x == newHead.x && gameObject.y == newHead.y) {
                    gameObject.isAlive = false;
                }
                else {
                    removeTail();
                }
            }
        }
    }

    public GameObject createNewHead() {
        int x = snakeParts.get(0).x;
        int y = snakeParts.get(0).y;
        GameObject newHead = null;
        switch (direction) {
            case LEFT:
                newHead = new GameObject(x - 1, y);
                break;
            case RIGHT:
                newHead = new GameObject(x + 1, y);
                break;
            case DOWN:
                newHead = new GameObject(x, y + 1);
                break;
            case UP:
                newHead = new GameObject(x, y - 1);
                break;
        }
        return newHead;
    }

    public void removeTail() {
        snakeParts.remove(snakeParts.size() - 1);
    }

    public boolean checkCollision(GameObject gameObject) {
        for (int i = 0; i < snakeParts.size(); i++) {
            if (snakeParts.get(i).x == gameObject.x && snakeParts.get(i).y == gameObject.y) {
                return true;
            }
        }
        return false;
    }

    public int getLength() {
        return snakeParts.size();
    }
}
