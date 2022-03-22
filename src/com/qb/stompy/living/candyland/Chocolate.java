package com.qb.stompy.living.candyland;

import com.qb.stompy.living.Enemy;
import com.rubynaxela.kyanite.game.Scene;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.util.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Time;

public class Chocolate extends Enemy {
    private final float walkingSpeed = 80;
    private boolean facingLeft = true;


    public Chocolate() {
        this(5);
    }

    public Chocolate(int rows) {
        super();
        Texture texture = assets.get("texture_chocolate");
        texture.apply(this.mainBody);
        if (rows > 5) rows = 5;
        if (rows <= 0) rows = 1;
        setMaxHp(rows);
        mainBody.setTextureRect(new IntRect((2 - rows / 2) * 18, (5 - rows) * 6, 18, 34 - (5 - rows) * 6));
        mainBody.setSize(Vec2.f(36, 8 + 12 * rows));
        mainBody.setOrigin(mainBody.getSize().x / 2, mainBody.getSize().y);
        invincibilityTime = 0.5f;
    }

    @Override
    public void animate(@NotNull Time deltaTime, @NotNull Time elapsedTime) {
        if (!getLevelScene().isPaused()) {
            Scene scene = window.getScene();
            foundGround = false;
            float direction = 1, animationSpeed = 2.5f;
            int animationFrame, model;

            //Updating data
            if (currentHp <= 0) {
                kill();
                scene.scheduleToRemove(this);
                return;
            }
            speedY += 1000 * deltaTime.asSeconds();
            if (recentlyDamaged) {
                invincibilityTime -= deltaTime.asSeconds();
                if (invincibilityTime < 0) invincibilityTime = 0;
                mainBody.setFillColor(new Color(255, 255, 255, (int) (255 - 127 * Math.sin(invincibilityTime * Math.PI))));
                if (invincibilityTime == 0) {
                    recentlyDamaged = false;
                    canBeDamaged = true;
                    invincibilityTime = 0.5f;
                }
            }
            if (facingLeft) direction = -1;
            speedX = direction * (walkingSpeed + (5 - getCurrentHp()) * 20);

            //Loop Variables
            float L = gGB().left, R = L + gGB().width, T = gGB().top, B = T + gGB().height;

            //Target player
            if (getLevelScene() != null) {
                if (getLevelScene().getPlayerCharacter() != null && !getLevelScene().getPlayerCharacter().isDead())
                    target = getLevelScene().getPlayerCharacter();
                else
                    target = null;
            }

            //Out of map prevention
            if (getLevelScene() != null) {
                if (-(L + speedX * deltaTime.asSeconds()) >= getLevelScene().getMapOffset().x) {
                    speedX = (-L - getLevelScene().getMapOffset().x) / deltaTime.asSeconds();
                }
                if (R + speedX * deltaTime.asSeconds() >= getLevelScene().getMapSize().x) {
                    speedX = (getLevelScene().getMapSize().x - R) / deltaTime.asSeconds();
                }
            } else {
                if (-(L + speedX * deltaTime.asSeconds()) >= 0) {
                    speedX = -L / deltaTime.asSeconds();
                }
                if (R + speedX * deltaTime.asSeconds() >= window.getSize().x) {
                    speedX = (window.getSize().x - R) / deltaTime.asSeconds();
                }
            }

            //Block collisions
            preventBlockCollision(deltaTime);
            if (!foundGround) floor = null;

            //Prevent from falling
            if (floor != null) {
                //Left edge
                if (L >= floor.gGB().left && -speedX * deltaTime.asSeconds() >= L - floor.gGB().left) {
                    speedX = (floor.gGB().left - L) / deltaTime.asSeconds();
                    facingLeft = false;
                }
                //Right edge
                if (R <= floor.gGB().left + floor.gGB().width && speedX * deltaTime.asSeconds() >= floor.gGB().left + floor.gGB().width - R) {
                    speedX = (floor.gGB().left + floor.gGB().width - R) / deltaTime.asSeconds();
                    facingLeft = true;
                }
            }

            //Animation
            model = (int) (getCurrentHp() / 2);
            animationFrame = (int) (elapsedTime.asSeconds() * animationSpeed) % 2;
            mainBody.setTextureRect(new IntRect((2 - model) * 18, animationFrame * 34 + (5 - (int) getCurrentHp()) * 6, 18, 34 - (5 - (int) getCurrentHp()) * 6));

            setScale(-direction * Math.abs(getScale().x), getScale().y);
            moveOnMap(speedX * deltaTime.asSeconds(), speedY * deltaTime.asSeconds());
        }
    }

    @Override
    public void stomp() {
        damage(1);
        mainBody.setSize(Vec2.f(mainBody.getSize().x, 68 - (5 - (int) getCurrentHp()) * 12));
        mainBody.setOrigin(mainBody.getSize().x / 2, mainBody.getSize().y);
    }
}
