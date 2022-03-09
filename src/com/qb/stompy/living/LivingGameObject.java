package com.qb.stompy.living;

import com.qb.stompy.objects.GameObject;
import com.rubynaxela.kyanite.game.entities.AnimatedEntity;

public abstract class LivingGameObject extends GameObject implements AnimatedEntity {
    protected float currentHp, invincibilityTime = 0, maxHp = 1, speedX = 0, speedY = 0;
    protected boolean dead = false, onGround = false, affectedByGravity = true, recentlyDamaged = false, canBeDamaged = true;

    public void damage(float dmg) {
        if (canBeDamaged) currentHp -= dmg;
        recentlyDamaged = true;
        makeInvincible();
    }

    public float getCurrentHp() {
        return currentHp;
    }

    public void setMaxHp(float hp) {
        maxHp = hp;
        currentHp = hp;
    }

    public void replenishHp() {
        currentHp = maxHp;
    }

    public void makeInvincible() {
        canBeDamaged = false;
    }

    public boolean isDead() {
        return dead;
    }

    public void kill() {
        dead = true;
    }
}
