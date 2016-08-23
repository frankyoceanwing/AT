package com.oceanwing.at.routing.here;

/**
 * Created by franky on 16/8/13.
 */
public class RouteFeature {

    private Type mType;
    private Weight mWeight;

    public RouteFeature(Type type, Weight weight) {
        mType = type;
        mWeight = weight;
    }

    public Type getType() {
        return mType;
    }

    public void setType(Type type) {
        mType = type;
    }

    public Weight getWeight() {
        return mWeight;
    }

    public void setWeight(Weight weight) {
        mWeight = weight;
    }

    @Override
    public String toString() {
        return String.format("%s:%d", mType.value(), mWeight.value());
    }

    public enum Type {
        TOLL_ROAD("tollroad"),
        MOTORWAY("motorway"),
        BOAT_FERRY("boatFerry"),
        RAIL_FERRY("railFerry"),
        TUNNEL("tunnel"),
        DIRT_ROAD("dirtRoad"),
        PARK("park");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum Weight {
        STRICT_EXCLUDE(-3),
        SOFT_EXCLUDE(-2),
        AVOID(-1),
        NORMAL(0);

        private int value;

        Weight(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
