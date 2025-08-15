package io.github.arasdenizhan.bts.management.model.enums;

public enum EventType {
    CHECKED_IN,
    LOADED,
    UNLOADED,
    DELIVERED,
    CLAIMED;

    public static EventType getNextStatus(EventType eventType){
        int currentOrdinal = eventType.ordinal();
        if(currentOrdinal+1 < values().length){
            return values()[currentOrdinal+1];
        }
        return null;
    }
}
