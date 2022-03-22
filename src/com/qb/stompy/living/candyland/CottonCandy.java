package com.qb.stompy.living.candyland;

import com.qb.stompy.living.Character;
import com.qb.stompy.living.Enemy;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.util.Colors;
import com.rubynaxela.kyanite.util.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Time;
import org.jsfml.window.Keyboard;

public class CottonCandy extends Enemy {

    private final float movementSpeed = 150, changeDirectionTime = 1.2f;
    private float floatingAroundTime = 0;

    public CottonCandy () {
        this(Colors.LIGHT_PINK);
    }

    public CottonCandy(Color color) {
        super();
        assets.<Texture>get("texture_cotton_candy").apply(this.mainBody);
        mainBody.setTextureRect(new IntRect(0, 0, 32, 16));
        mainBody.setSize(Vec2.f(32, 16));
        mainBody.setOrigin(Vec2.divideFloat(mainBody.getSize(), 2));
        mainBody.setFillColor(color);
        affectedByGravity = false;
        setMaxHp(1);
    }

    @Override
    public void animate(@NotNull Time deltaTime, @NotNull Time elapsedTime) {
        if (!getLevelScene().isPaused()) {
            int animationFrame = 0, direction = 1;

            //Kill
            if (currentHp <= 0) {
                kill();
                animationFrame = 1;
            }

            //Gravity
            if (affectedByGravity) speedY += 1000 * deltaTime.asSeconds();

            //Loop Variables
            float L = gGB().left, R = L + gGB().width, T = gGB().top, B = T + gGB().height;

            //Target player
            if (getLevelScene() != null) {
                boolean shouldClearTarget = true;
                if (getLevelScene().getPlayerCharacter() != null && !getLevelScene().getPlayerCharacter().isDead()) {
                    Character character = getLevelScene().getPlayerCharacter();
                    if (Math.abs(character.getPositionOnMap().x - getPositionOnMap().x) < 600) {
                        if (Math.abs(character.getPositionOnMap().x - getPositionOnMap().x) < 300) {
                            target = character;
                        }
                        shouldClearTarget = false;
                    }
                }
                if (target != null && shouldClearTarget) {
                    target = null;
                }
            }

            if (!affectedByGravity) {
                if (target != null) {
                    float distanceX = target.getPositionOnMap().x - getPositionOnMap().x,
                            distanceY = target.getPositionOnMap().y - target.gGB().height / 2 - getPositionOnMap().y,
                            factorX = distanceX / (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY),
                            factorY = distanceY / (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
                    speedX = movementSpeed * factorX;
                    speedY = movementSpeed * factorY;
                    if (Keyboard.isKeyPressed(Keyboard.Key.H)) {
                        System.out.println("Distances: " + distanceX + ", " + distanceY);
                        System.out.println("Factors: " + factorX + ", " + factorY);
                        System.out.println("Speeds: " + speedX + ", " + speedY);
                    }
                } else {
                    //Float around
                    floatingAroundTime += deltaTime.asSeconds();
                    if (floatingAroundTime >= changeDirectionTime) {
                        int verticalCorrection = 0;
                        if (getPositionOnMap().y < 100) verticalCorrection = 1;
                        speedX = (float) ((Math.random() * 2 - 1) * movementSpeed / Math.sqrt(2));
                        speedY = (float) ((Math.random() * 2 - 1 + verticalCorrection) * movementSpeed / Math.sqrt(2));
                        floatingAroundTime -= changeDirectionTime;
                    }
                }
                if (speedX < 0) direction = -1;
            }

            preventBlockCollision(deltaTime);

            //No ankle biting
            if (target != null) {
                if ((L < target.gGB().left + target.gGB().width && R > target.gGB().left) ||
                        (L + speedX * deltaTime.asSeconds() < target.gGB().left + target.gGB().width && R + speedX * deltaTime.asSeconds() > target.gGB().left)) {
                    if (T > target.gGB().top + target.gGB().height && speedY < 0) {
                        if (-speedY * deltaTime.asSeconds() > T - (target.gGB().top + target.gGB().height)) {
                            speedY = (target.gGB().top + target.gGB().height - T + 1) / deltaTime.asSeconds();
                            stomp();
                            target.bounce();
                        }
                    }
                }
            }
            if (speedY < 0) onGround = false;

            setScale(-direction * Math.abs(getScale().x), getScale().y);
            mainBody.setTextureRect(new IntRect(0, animationFrame * 16, 32, 16));
            moveOnMap(speedX * deltaTime.asSeconds(), speedY * deltaTime.asSeconds());
        }
    }

    @Override
    public void kill() {
        affectedByGravity = true;
        if (onGround) dead = true;
    }
}
