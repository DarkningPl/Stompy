package com.qb.stompy.living;

import com.qb.stompy.objects.GameObject;
import com.qb.stompy.objects.SolidBlock;
import com.rubynaxela.kyanite.game.entities.AnimatedEntity;
import org.jsfml.graphics.Drawable;
import org.jsfml.system.Time;

public abstract class LivingGameObject extends GameObject implements AnimatedEntity {
    protected int currentHp, maxHp = 1;
    protected float invincibilityTime = 0, speedX = 0, speedY = 0;
    protected boolean dead = false, onGround = false, foundGround = false, affectedByGravity = true, recentlyDamaged = false, canBeDamaged = true;
    protected SolidBlock floor = null;

    public void damage(float dmg) {
        if (canBeDamaged) currentHp -= dmg;
        recentlyDamaged = true;
        makeInvincible();
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int hp) {
        maxHp = hp;
        currentHp = hp;
    }

    protected void preventBlockCollision(Time deltaTime) {
        float L = gGB().left, R = L + gGB().width, T = gGB().top, B = T + gGB().height;
        for (final Drawable object : window.getScene()) {
            if (object instanceof final SolidBlock sBlock) {
                float L2 = sBlock.gGB().left, R2 = L2 + sBlock.gGB().width, T2 = sBlock.gGB().top, B2 = T2 + sBlock.gGB().height;
                //Vertical Collision
                if (L < R2 && R > L2) {
                    if (T >= B2 && -speedY * deltaTime.asSeconds() >= T - B2) {
                        speedY = (B2 - T) / deltaTime.asSeconds();
                    }
                    if (B <= T2 && speedY * deltaTime.asSeconds() >= T2 - B) {
                        speedY = (T2 - B) / deltaTime.asSeconds();
                        onGround = true;
                        foundGround = true;
                        floor = sBlock;
                    }
                }
                //Horizontal Collision
                if ((T < B2 && B > T2) || (T + speedY * deltaTime.asSeconds() < B2 && B + speedY * deltaTime.asSeconds() > T2)) {
                    if (L >= R2 && -speedX * deltaTime.asSeconds() >= L - R2) {
                        speedX = (R2 - L) / deltaTime.asSeconds();
                    }
                    if (R <= L2 && speedX * deltaTime.asSeconds() >= L2 - R) {
                        speedX = (L2 - R) / deltaTime.asSeconds();
                    }
                }
            }
        }
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
