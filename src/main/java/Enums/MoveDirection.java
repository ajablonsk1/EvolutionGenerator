package Enums;

import Objects.Vector2d;


public enum MoveDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;


    public String toString() {
        return switch (this) {
            case NORTH -> "North";
            case NORTH_EAST -> "North East";
            case EAST -> "East";
            case SOUTH_EAST -> "South East";
            case SOUTH -> "South";
            case SOUTH_WEST -> "South West";
            case WEST -> "West";
            case NORTH_WEST -> "North West";
        };
    }

    public Vector2d toUnitVector(){
        return switch (this){
            case NORTH -> new Vector2d(0, 1);
            case NORTH_EAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH_EAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTH_WEST -> new Vector2d(-1, 1);
        };
    }

    public MoveDirection rotation(int rotation){
        return switch(rotation) {
            case 0 -> getMoveDirection(NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST);
            case 1 -> getMoveDirection(NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NORTH);
            case 2 -> getMoveDirection(EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NORTH, NORTH_EAST);
            case 3 -> getMoveDirection(SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NORTH, NORTH_EAST, EAST);
            case 4 -> getMoveDirection(SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NORTH, NORTH_EAST, EAST, SOUTH_EAST);
            case 5 -> getMoveDirection(SOUTH_WEST, WEST, NORTH_WEST, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH);
            case 6 -> getMoveDirection(WEST, NORTH_WEST, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST);
            case 7 -> getMoveDirection(NORTH_WEST, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST);
            default -> throw new IllegalStateException("Unexpected value: " + rotation);
        };
    }

    private MoveDirection getMoveDirection(MoveDirection north, MoveDirection northEast, MoveDirection east, MoveDirection southEast, MoveDirection south, MoveDirection southWest, MoveDirection west, MoveDirection northWest) {
        return switch (this){
            case NORTH -> north;
            case NORTH_EAST -> northEast;
            case EAST -> east;
            case SOUTH_EAST -> southEast;
            case SOUTH -> south;
            case SOUTH_WEST -> southWest;
            case WEST -> west;
            case NORTH_WEST -> northWest;
        };
    }

}
