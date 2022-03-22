package com.qb.stompy.living.candyland;

import com.qb.stompy.living.Character;
import com.qb.stompy.living.Enemy;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.assets.TextureAtlas;
import com.rubynaxela.kyanite.game.entities.AnimatedEntity;
import org.jetbrains.annotations.NotNull;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

public class Cupcake extends Enemy implements AnimatedEntity {

    private float timeToStopDashing = 0;
    private final float stopDashTime = 1, walkingSpeed = 80, dashingSpeed = 200;
    private boolean facingLeft = true, dashing = false, foundTarget = false, didAHop = false, shouldSlowDown = false;

    private final TextureAtlas atlas = assets.get("texture_cupcake");
    private final Texture[][] textures = {{
            atlas.get(0, 0, 16, 14),
            atlas.get(16, 0, 32, 14)
    }, {
            atlas.get(32, 0, 48, 14),
            atlas.get(48, 0, 64, 14)
    }};

    public Cupcake() {
        textures[0][0].apply(this.mainBody);
        mainBody.setSize(new Vector2f(32, 28));
        mainBody.setOrigin(mainBody.getSize().x / 2, mainBody.getSize().y);
        setMaxHp(1);
        stompable = false;
    }

    @Override
    public void animate(@NotNull Time deltaTime, @NotNull Time elapsedTime) {
        if (!getLevelScene().isPaused()) {
            foundGround = false;
            int direction = 1, animationFrames = 0, animationFrame;
            float animationSpeed = 2.5f;

            //Kill
            if (currentHp <= 0) {
                kill();
            }

            //Gravity
            speedY += 1000 * deltaTime.asSeconds();

            //Loop Variables
            float L = gGB().left, R = L + gGB().width, T = gGB().top, B = T + gGB().height;

            //Target player
            if (getLevelScene() != null) {
                boolean shouldClearTarget = true;
                if (getLevelScene().getPlayerCharacter() != null && !getLevelScene().getPlayerCharacter().isDead()) {
                    Character character = getLevelScene().getPlayerCharacter();
                    if (character.getPositionOnMap().y <= getPositionOnMap().y || didAHop) {
                        if (Math.abs(character.getPositionOnMap().x - getPositionOnMap().x) < 600) {
                            if (Math.abs(character.getPositionOnMap().x - getPositionOnMap().x) < 300) {
                                target = character;
                                foundTarget = true;
                            }
                            shouldClearTarget = false;
                        }
                    }
                }
                if (target != null && shouldClearTarget) {
                    target = null;
                    foundTarget = false;
                    didAHop = false;
                }
            }

            //Movement logic
            if (target != null) {
                if (!dashing) {
                    facingLeft = target.getPositionOnMap().x < getPositionOnMap().x;
                }
                if (target.getPositionOnMap().y == getPositionOnMap().y && Math.abs(target.getPositionOnMap().x - getPositionOnMap().x) < 200) {
                    dashing = true;
                }
            }
            if (facingLeft) direction = -1;
            if (timeToStopDashing >= stopDashTime) {
                timeToStopDashing = 0;
                dashing = false;
                shouldSlowDown = false;
            }
            if (getLevelScene() != null) {
                if (Math.abs(getPositionOnMap().x - getLevelScene().getMapOffset().x) < window.getSize().x) {
                    if (dashing) {
                        speedX = direction * dashingSpeed;
                        if (target == null) shouldSlowDown = true;
                        else {
                            if ((facingLeft && target.getPositionOnMap().x > getPositionOnMap().x) || (!facingLeft && target.getPositionOnMap().x < getPositionOnMap().x))
                                shouldSlowDown = true;
                        }
                        if (shouldSlowDown) timeToStopDashing += deltaTime.asSeconds();
                        animationSpeed = 5;
                        animationFrames = 1;
                    } else {
                        if (floor != null) speedX = direction * walkingSpeed;
                        else speedX = 0;
                    }
                }
            }

            //Out of map prevention
            if (getLevelScene() != null) {
                if (-(L + speedX * deltaTime.asSeconds()) >= getLevelScene().getMapOffset().x) {
                    speedX = -L / deltaTime.asSeconds();
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

            //Aggro
            if (foundTarget && floor != null && !didAHop) {
                didAHop = true;
                speedY = -aggroJumpStrength;
                onGround = false;
            }
            //Prevent from falling
            if (floor != null) {
                if (!dashing) {
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
            }

            //Animation
            setScale(-direction * Math.abs(getScale().x), getScale().y);
            animationFrame = (int) (elapsedTime.asSeconds() * animationSpeed) % 2;
            //setTextureRect(new IntRect(16 * (int) (animationFrames + animationFrame), 0, 16, 14));
            textures[animationFrames][animationFrame].apply(this.mainBody);

            moveOnMap(speedX * deltaTime.asSeconds(), speedY * deltaTime.asSeconds());
        }
    }
}
