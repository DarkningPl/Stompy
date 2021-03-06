package com.qb.stompy.living;

import com.rubynaxela.kyanite.util.Vec2;
import org.jsfml.system.Vector2f;

public abstract class Enemy extends LivingGameObject {
    protected final float aggroJumpStrength = 160;
    protected boolean stompable = true, active = true;
    protected Character target = null;

    public boolean canBeStomped() {
        return stompable;
    }

    public boolean isActive() { return active; }

    public void stomp() {
        damage(1);
    }

    public Vector2f getSpeed() {
        return Vec2.f(speedX, speedY);
    }
}
