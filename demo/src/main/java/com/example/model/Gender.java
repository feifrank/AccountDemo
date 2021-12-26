package com.example.model;

public enum Gender {
    male("male"), female("female");
    public String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
