package com.qb.stompy.living.candyland;

import com.qb.stompy.living.Character;
import com.qb.stompy.objects.Enemy;
import com.qb.stompy.objects.SolidBlock;
import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.util.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Time;

public class Caramel extends Enemy {

    private boolean wasStomped = false;
    private final float timeToRecover = 4, movementSpeed = 80;
    private float timeUntilRecovery = 0;

    public Caramel() {
        assets.<Texture>get("texture_caramel").apply(this.mainBody);
        mainBody.setTextureRect(new IntRect(0, 0, 16, 8));
        mainBody.setSize(Vec2.f(32, 16));
        mainBody.setOrigin(mainBody.getSize().x / 2f, mainBody.getSize().y);
        setMaxHp(1);
    }

    @Override
    public void stomp() {
        wasStomped = true;
        stompable = false;
        active = false;
        timeUntilRecovery = timeToRecover;
    }

    @Override
    public void animate(@NotNull Time deltaTime, @NotNull Time elapsedTime) {
        foundGround = false;
        int direction = 1, animationFrame = 0;

        //Kill
        if (currentHp <= 0) {
            kill();
        }

        //Gravity
        speedX = 0;
        speedY += 1000 * deltaTime.asSeconds();

        //Loop Variables
        float L = gGB().left, R = L + gGB().width, T = gGB().top, B = T + gGB().height;

        //Recover from getting stomped
        if (wasStomped) {
            timeUntilRecovery -= deltaTime.asSeconds();
            if (timeUntilRecovery > 3.8)
                animationFrame = 1;
            else if (timeUntilRecovery > 3.6)
                animationFrame = 2;
            else if (timeUntilRecovery > 1.2)
                animationFrame = 3;
            else if (timeUntilRecovery > 1)
                animationFrame = 2;
            else if (timeUntilRecovery > 0.8 || (timeUntilRecovery > 0.4 && timeUntilRecovery < 0.6) || (timeUntilRecovery > 0 && timeUntilRecovery < 0.2))
                animationFrame = 1;
            else if (timeUntilRecovery <= 0) {
                active = true;
                wasStomped = false;
                stompable = true;
            }
        }

        //Target player
        if (getLevelScene() != null) {
            boolean shouldClearTarget = true;
            if (getLevelScene().getPlayerCharacter() != null && !getLevelScene().getPlayerCharacter().isDead()) {
                Character character = getLevelScene().getPlayerCharacter();
                if (character.getPositionOnMap().y <= getPositionOnMap().y) {
                    if (Math.abs(character.getPositionOnMap().x - getPositionOnMap().x) < 600) {
                        if (Math.abs(character.getPositionOnMap().x - getPositionOnMap().x) < 300) {
                            target = character;
                        }
                        shouldClearTarget = false;
                    }
                }
            }
            if (target != null && shouldClearTarget) {
                target = null;
            }
        }

        if (target != null && !wasStomped) {
            if (target.getPositionOnMap().x < getPositionOnMap().x) {
                direction = -1;
            }
            setScale(-direction * Math.abs(getScale().x), getScale().y);
            speedX = direction * movementSpeed;
        }

        //Block collisions
        preventBlockCollision(deltaTime);

        if (!foundGround) onGround = false;

        mainBody.setTextureRect(new IntRect(0, animationFrame * 8, 16, 8));
        moveOnMap(speedX * deltaTime.asSeconds(), speedY * deltaTime.asSeconds());
    }
}
