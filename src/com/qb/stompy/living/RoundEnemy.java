package com.qb.stompy.living;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;

public abstract class RoundEnemy extends Enemy {
    protected float radius;
    protected RectangleShape squareHitbox;
    public RoundEnemy(float diameter) {
        radius = diameter / 2f;
        squareHitbox = new RectangleShape(new Vector2f(2 * radius, 2 * radius));
        squareHitbox.setOrigin(radius, radius);
        squareHitbox.setPosition(getPositionOnMap());
    }
    public float getRadius() {
        return radius;
    }
    public RectangleShape getSquareHitbox() {
        return squareHitbox;
    }

    public void setRadius(float _radius) {
        radius = _radius;
        setScale(getScale());
    }

    @Override
    public void setScale(Vector2f vector2f) {
        super.setScale(vector2f);
        squareHitbox.setScale(vector2f);
    }
}
