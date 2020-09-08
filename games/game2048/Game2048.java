package com.javarush.games.game2048;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score = 0;

    @Override
    //отображение экрана
    public void initialize() {

        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }

    //создание игры
    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();
    }

    //нажатие на клавишу
    @Override
    public void onKeyPress(Key key) {
        if (!canUserMove()) {
            gameOver();
        }
        if (isGameStopped && key == Key.SPACE) {
            isGameStopped = false;
            createGame();
            drawScene();
            score = 0;
            setScore(score);
            return;
        }
        if (!isGameStopped){
            switch (key) {
                case LEFT:
                    moveLeft();
                    drawScene();
                    break;
                case RIGHT:
                    moveRight();
                    drawScene();
                    break;
                case UP:
                    moveUp();
                    drawScene();
                    break;
                case DOWN:
                    moveDown();
                    drawScene();
                    break;
            }
        }
    }

    //заливка фона
    private void drawScene() {
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                setCellColoredNumber(i, j, gameField[j][i]);
            }
        }
    }

    //создание нового числа
    private void createNewNumber() {
        if (getMaxTileValue() == 2048) {
            win();
        }
        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);
        if (gameField[y][x] == 0) {
            if (getRandomNumber(10) == 9) {
                gameField[y][x] = 4;
            } else gameField[y][x] = 2;
        } else {
            createNewNumber();
        }
    }

    //присваивает цвета в зависимости от цифр
    private Color getColorByValue(int value) {
        Color color = Color.LAVENDERBLUSH;
        switch (value) {
            case 0:
                color = Color.LAVENDERBLUSH;
                break;
            case 2:
                color = Color.VIOLET;
                break;
            case 4:
                color = Color.DEEPSKYBLUE;
                break;
            case 8:
                color = Color.CYAN;
                break;
            case 16:
                color = Color.LIGHTGREEN;
                break;
            case 32:
                color = Color.YELLOWGREEN;
                break;
            case 64:
                color = Color.LIME;
                break;
            case 128:
                color = Color.DARKORANGE;
                break;
            case 256:
                color = Color.CRIMSON;
                break;
            case 512:
                color = Color.DARKRED;
                break;
            case 1024:
                color = Color.MAGENTA;
                break;
            case 2048:
                color = Color.PURPLE;
                break;
        }
        return color;
    }

    //отображение значения и цвета клетки на поле
    private void setCellColoredNumber(int x, int y, int value) {
        setCellValueEx(x, y, getColorByValue(value), ((value == 0) ? "" : "" + value));
    }

    //сдвиг влево
    private boolean compressRow(int[] row) {
        boolean compressRow = false;
        int[] newRow = new int[row.length];
        int x = 0;
        for (int i = 0; i < row.length; i++) {
            if (row[i] != 0) {
                newRow[x] = row[i];
                if (i != x) {
                    compressRow = true;
                }
                x++;
            }
        }
        for (int i = 0; i < row.length; i++) {
            row[i] = newRow[i];
        }
        return compressRow;
    }

    //соединение плиток при сдвиге влево
    private boolean mergeRow(int[] row) {
        boolean mergeRow = false;
        int[] newRow = new int[row.length];
        int x = 0;
        for (int i = 0; i < row.length; ) {
            if (row[i] == 0) {
                newRow[x] = row[i];
                x++;
                i++;
            } else {
                if (i != row.length - 1) {
                    if (row[i] == row[i + 1]) {
                        newRow[x] = row[i] + row[i + 1];
                        score = score+newRow[x];
                        setScore(score);
                        newRow[x + 1] = 0;
                        x += 2;
                        i += 2;
                        mergeRow = true;

                    } else {
                        newRow[x] = row[i];
                        x++;
                        i++;
                    }
                } else {
                    newRow[x] = row[i];
                    x++;
                    i++;
                }
            }
        }
        System.arraycopy(newRow, 0, row, 0, row.length);
        return mergeRow;
    }

    private void moveLeft() {
        boolean change = false;
        for (int i = 0; i < gameField.length; i++) {
            if (compressRow(gameField[i])) change = true;
            if (mergeRow(gameField[i])) change = true;
            if (compressRow(gameField[i])) change = true;
        }
        if (change) {
            createNewNumber();
        }
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    //вращение матрицы
    private void rotateClockwise() {
        int n = SIDE;
        int[][] newField = new int[n][n];
        for (int x = 0; x < n / 2; x++) {
            for (int y = x; y < n - x - 1; y++) {
                int tmp = gameField[x][y];
                newField[x][y] = gameField[n - y - 1][x];
                newField[n - y - 1][x] = gameField[n - x - 1][n - y - 1];
                newField[n - x - 1][n - y - 1] = gameField[y][n - x - 1];
                newField[y][n - x - 1] = tmp;
            }
        }
        gameField = newField;
    }

    private int getMaxTileValue() {
        int max = 0;
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                if (max < gameField[j][i]) {
                    max = gameField[j][i];
                }
            }
        }
        return max;
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.WHITESMOKE, "You win!", Color.RED, 50);
    }

    private boolean canUserMove() {
        boolean canMove = true;
        int count = 0;
        int count2 = 0;
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                if (gameField[j][i] == 0) {
                    count++;
                }
            }
        }
        for (int q = SIDE; q > 0; q--) {
            for (int i = 0; i < gameField.length - 1; i++) {
                for (int j = 0; j < gameField[i].length - 1; j++) {
                    if (gameField[j][i] == gameField[j][i + 1] || gameField[j][i] == gameField[j + 1][i]) {
                        count2++;
                    }
                }
            }
            rotateClockwise();
        }

        if (count == 0 && count2 == 0) {
            canMove = false;
        }
        return canMove;
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.WHITESMOKE, "Try again :)", Color.RED, 50);
    }
}
