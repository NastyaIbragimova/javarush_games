package com.javarush.games.moonlander;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class Rocket extends GameObject {
    private double speedY = 0;
    private double speedX = 0;
    private double boost = 0.05;
    private double slowdown = boost / 10;
    private RocketFire downFire;
    private RocketFire leftFire;
    private RocketFire rightFire;

    public Rocket(double x, double y) {
        super(x, y, ShapeMatrix.ROCKET);
        List<int[][]> down = new ArrayList<>();
        down.add(ShapeMatrix.FIRE_DOWN_1);
        down.add(ShapeMatrix.FIRE_DOWN_2);
        down.add(ShapeMatrix.FIRE_DOWN_3);
        downFire = new RocketFire(down);
        List<int[][]> sides = new ArrayList<>();
        sides.add(ShapeMatrix.FIRE_SIDE_1);
        sides.add(ShapeMatrix.FIRE_SIDE_2);
        leftFire = new RocketFire(sides);
        rightFire = new RocketFire(sides);
    }

    public void move(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed) {
        if (isUpPressed) {
            speedY -= boost;
        } else {
            speedY += boost;
        }
        y += speedY;

        if (isLeftPressed) {
            speedX -= boost;
            x += speedX;
        } else if (isRightPressed) {
            speedX += boost;
            x += speedX;
        } else if (speedX > slowdown) {
            speedX -= slowdown;
        } else if (speedX < -slowdown) {
            speedX += slowdown;
        } else {
            speedX = 0;
        }
        x += speedX;
        checkBorders();
        switchFire(isUpPressed, isLeftPressed, isRightPressed);
    }

    private void checkBorders() {
        if (x < 0) {
            x = 0;
            speedX = 0;
        } else if (x + width > MoonLanderGame.WIDTH) {
            x = MoonLanderGame.WIDTH - width;
            speedX = 0;
        }
        if (y <= 0) {
            y = 0;
            speedY = 0;
        }
    }

    public boolean isStopped() {
        return speedY < 10 * boost;
    }

    public boolean isCollision(GameObject object) {
        int transparent = Color.NONE.ordinal();
        for (int matrixX = 0; matrixX < width; matrixX++) {
            for (int matrixY = 0; matrixY < height; matrixY++) {
                int objectX = matrixX + (int) x - (int) object.x;
                int objectY = matrixY + (int) y - (int) object.y;

                if (objectX < 0 || objectX >= object.width || objectY < 0 || objectY >= object.height) {
                    continue;
                }

                if (matrix[matrixY][matrixX] != transparent && object.matrix[objectY][objectX] != transparent) {
                    return true;
                }
            }
        }
        return false;
    }

    public void land() {
        y = y - 1;
    }

    public void crash() {
        matrix = ShapeMatrix.ROCKET_CRASH;
    }

    private void switchFire(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed) {
        if (isUpPressed) {
            downFire.x = x + width / 2;
            downFire.y = y + height;
            downFire.show();
        } else {
            downFire.hide();
        }
        if (isLeftPressed) {
            leftFire.x = x + width;
            leftFire.y = y + height;
            leftFire.show();
        } else {
            leftFire.hide();
        }
        if (isRightPressed) {
            rightFire.x = x - ShapeMatrix.FIRE_SIDE_1[0].length;
            rightFire.y = y + height;
            rightFire.show();
        } else {
            rightFire.hide();
        }
    }
    @Override
    public void draw(Game game) {
        super.draw(game);
        downFire.draw(game);
        leftFire.draw(game);
        rightFire.draw(game);
    }
}
/*
7. В методе switchFire(boolean, boolean, boolean) необходимо установить объекту rightFire значение поля x,
равное x ракеты - ShapeMatrix.FIRE_SIDE_1[0].length, если значение параметра isRightPressed метода равное true.
8. В методе switchFire(boolean, boolean, boolean) необходимо установить объекту rightFire значение поля y,
равное y ракеты + height ракеты, если значение параметра isRightPressed метода равное true.
9. В методе switchFire(boolean, boolean, boolean) необходимо вызвать у объекта rightFire метод show(),
если значение параметра isRightPressed метода равное true.
10. В методе switchFire(boolean, boolean, boolean) необходимо вызвать у объекта rightFire метод hide(),
если значение параметра isRightPressed метода равное false.*/