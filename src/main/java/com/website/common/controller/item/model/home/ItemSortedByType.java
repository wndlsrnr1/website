package com.website.common.controller.item.model.home;

public enum ItemSortedByType {

    MAX_PRICE("maxPrice"), MIN_PRICE("minPrice"), NAME("name"), RELEASED("released"), CREATED("created");

    private final String stringValue;

    ItemSortedByType(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public static ItemSortedByType fromString(String stringValue) {
        for (ItemSortedByType type : ItemSortedByType.values()) {
            if (type.getStringValue().equalsIgnoreCase(stringValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant with string value " + stringValue);
    }

}
