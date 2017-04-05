package com.assignment.canvas.models;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

/**
 * Created by mayursharma on 4/4/17.
 */

public class Position extends GenericJson {

    @Key
    private float x;
    @Key
    private float y;
    @Key
    private float width;
    @Key
    private float height;
    @Key
    private String name;

    public Position() {
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
