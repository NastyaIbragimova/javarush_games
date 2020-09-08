package com.javarush.games.racer.road;

import com.javarush.engine.cell.Game;
import com.javarush.games.racer.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoadManager {
    public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
    public static final int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;
    private static final int FIRST_LANE_POSITION = 16;
    private static final int FOURTH_LANE_POSITION = 44;
    private static final int PLAYER_CAR_DISTANCE = 25;
    private List<RoadObject> items = new ArrayList<>();
    private int passedCarsCount = 0;

    public int getPassedCarsCount() {
        return passedCarsCount;
    }


    private RoadObject createRoadObject(RoadObjectType type, int x, int y) {
        if (type.equals(RoadObjectType.THORN)) {
            return new Thorn(x, y);
        } else if (type.equals(RoadObjectType.DRUNK_CAR)) {
            return new MovingCar(x, y);
        } else return new Car(type, x, y);
    }

    private void generateRegularCar(Game game) {
        int random = game.getRandomNumber(100);
        int carTypeNumber = game.getRandomNumber(4);
        if (random < 30) {
            addRoadObject(RoadObjectType.values()[carTypeNumber], game);
        }
    }

    private void addRoadObject(RoadObjectType type, Game game) {
        int x = game.getRandomNumber(FIRST_LANE_POSITION, FOURTH_LANE_POSITION);
        int y = -1 * RoadObject.getHeight(type);
        RoadObject roadObject = createRoadObject(type, x, y);
        if (roadObject != null) {
            if (isRoadSpaceFree(roadObject)) {
                items.add(roadObject);
            }
        }
    }

    public void draw(Game game) {
        for (RoadObject item : items) {
            item.draw(game);
        }
    }

    public void move(int boost) {
        for (RoadObject item : items) {
            item.move(boost + item.speed, items);
        }
        deletePassedItems();
    }

    private void generateThorn(Game game) {
        int random = game.getRandomNumber(100);
        if (random < 10 && !isThornExists()) {
            addRoadObject(RoadObjectType.THORN, game);
        }
    }

    private boolean isThornExists() {
        for (RoadObject item : items) {
            if (item.getClass().equals(Thorn.class)) {
                return true;
            }
        }
        return false;
    }

    public void generateNewRoadObjects(Game game) {
        generateThorn(game);
        generateRegularCar(game);
        generateMovingCar(game);
    }

    private void deletePassedItems() {
        Iterator iterator = items.iterator();
        while(iterator.hasNext()){
            RoadObject item = (RoadObject)iterator.next();
            if(item.y >= RacerGame.HEIGHT){
                if(!(item instanceof Thorn)) passedCarsCount++;
                iterator.remove();
            }
        }
    }

    public boolean checkCrush(PlayerCar playerCar) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isCollision(playerCar)) {
                return true;
            }
        }
        return false;
    }

    private boolean isRoadSpaceFree(RoadObject object) {
        for (RoadObject item : items) {
            if (item.isCollisionWithDistance(object, PLAYER_CAR_DISTANCE)) {
                return false;
            }
        }
        return true;
    }

    private boolean isMovingCarExists() {
        for (RoadObject item : items) {
            if (item.getClass().equals(MovingCar.class)) {
                return true;
            }
        }
        return false;
    }

    private void generateMovingCar(Game game) {
        int random = game.getRandomNumber(100);
        if (!isMovingCarExists() && random < 10) {
            addRoadObject(RoadObjectType.DRUNK_CAR, game);
        }
    }
}
