package com.javarush.games.minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private int countClosedTiles = SIDE * SIDE;
    private static final int SIDE = 15;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private static final String MINE = "\uD83D\uDD25";
    private static final String FLAG = "\uD83D\uDCCD";
    private int countFlags;
    private boolean isGameStopped;
    private int score;


    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    @Override
    public void onMouseLeftClick(int x, int y) {

        if (isGameStopped == true) {
            restart();
        }
        if (isGameStopped != true) {
            openTile(x, y);
        }
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }

    private void createGame() {
        // isGameStopped = false;
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                setCellValue(x, y, "");
                boolean isMine = getRandomNumber(10) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.CYAN);
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;
    }

    private void countMineNeighbors() {
        List<GameObject> list;
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                if (!gameField[y][x].isMine) {
                    list = getNeighbors(gameField[y][x]);
                    for (GameObject object : list) {
                        if (object.isMine) {
                            gameField[y][x].countMineNeighbors++;
                        }
                    }
                }
            }
        }
    }

    private void openTile(int x, int y) {
        if (isGameStopped != true && gameField[y][x].isOpen != true && gameField[y][x].isFlag != true) {
            if (gameField[y][x].isMine) {
                gameField[y][x].isOpen = true;
                countClosedTiles--;
                setCellValueEx(x, y, Color.RED, MINE);
                gameOver();
            } else if (!gameField[y][x].isMine && gameField[y][x].countMineNeighbors == 0) {
                gameField[y][x].isOpen = true;
                countClosedTiles--;
                setCellColor(x, y, Color.YELLOW);
                setCellValue(x, y, "");
                List<GameObject> list = getNeighbors(gameField[y][x]);
                for (GameObject o : list) {
                    if (!o.isOpen) {
                        openTile(o.x, o.y);
                    }
                }
                score += 5;
                setScore(score);
                if (countClosedTiles == countMinesOnField) {
                    win();
                }
            } else {
                gameField[y][x].isOpen = true;
                countClosedTiles--;
                setCellColor(x, y, Color.YELLOW);
                setCellNumber(x, y, gameField[y][x].countMineNeighbors);
                score += 5;
                setScore(score);
                if (countClosedTiles == countMinesOnField) {
                    win();
                }
            }
        }
    }

    private void markTile(int x, int y) {
        if (!isGameStopped) {
            if (!gameField[y][x].isOpen && countFlags != 0) {
                if (!gameField[y][x].isFlag) {
                    gameField[y][x].isFlag = true;
                    countFlags--;
                    setCellValue(x, y, FLAG);
                    setCellColor(x, y, Color.GREEN);
                } else if (gameField[y][x].isFlag) {
                    gameField[y][x].isFlag = false;
                    countFlags++;
                    setCellValue(x, y, "");
                    setCellColor(x, y, Color.CYAN);
                }
            }
        }

    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.RED, "Game over", Color.WHITE, 70);

    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.BLUE, "You win" , Color.CORAL, 70);

    }

    private void restart() {
        isGameStopped = false;
        countClosedTiles = SIDE * SIDE;
        score = 0;
        countMinesOnField = 0;
        setScore(score);
        createGame();

    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }
}

/*

8. Метод onMouseLeftClick(int, int) должен вызывать метод restart() и ничего не делать, если игра остановлена.
*/