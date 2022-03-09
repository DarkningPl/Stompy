package com.qb.stompy.objects;

import com.qb.stompy.living.Character;
import com.qb.stompy.living.LivingGameObject;
import com.rubynaxela.kyanite.util.Vec2;
import org.jsfml.system.Vector2f;

public abstract class Enemy extends LivingGameObject {
    protected final float aggroJumpStrength = 160;
    protected boolean stompable = true, active = true;
    protected Character target = null;

    public boolean canBeStomped() {
        return stompable;
    }

    public void stomp() {
        damage(1);
    }

    public Vector2f getSpeed() {
        return Vec2.f(speedX, speedY);
    }
}
