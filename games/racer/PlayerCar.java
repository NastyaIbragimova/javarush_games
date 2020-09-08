package com.javarush.games.racer;

import com.javarush.games.racer.road.RoadManager;

public class PlayerCar extends GameObject {

    private static int playerCarHeight = ShapeMatrix.PLAYER.length;
    private Direction direction = Direction.NONE;
    public int speed = 1;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public PlayerCar() {
        super(RacerGame.WIDTH / 2 + 2, RacerGame.HEIGHT - playerCarHeight - 1, ShapeMatrix.PLAYER);
    }

    public void move() {
        if(x< RoadManager.LEFT_BORDER){
            x = RoadManager.LEFT_BORDER;
        }
        else if(x> RoadManager.RIGHT_BORDER - width){
            x=RoadManager.RIGHT_BORDER-width;
        }
        else {
                if(direction.equals(Direction.RIGHT)){
                    x+=1;
                }
                else if(direction.equals(Direction.LEFT)){
                    x-=1;
            }
        }
    }

    public void stop(){
        super.matrix = ShapeMatrix.PLAYER_DEAD;
    }
}
