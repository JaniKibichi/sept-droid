package com.mjuaji.platformgame;

public class Teleport extends GameObject {
    Location target;

    Teleport(float worldStartX, float worldStartY, char type, Location target){
        final float HEIGHT = 2;
        final float WIDTH =2;
        setHeight(HEIGHT);
        setWidth(WIDTH);
        setType(type);
        setBitmapName("door");
        this.target = new Location(target.level, target.x, target.y);
        //where does the time start, X and Y locations from constructor parameters
        setWorldLocation(worldStartX, worldStartY, 0);
        setRectHitbox();
    }

    public Location getTarget(){
        return target;
    }
    @Override
    public void update(long fps, float gravity) {

    }
}
