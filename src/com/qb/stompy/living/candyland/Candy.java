package com.qb.stompy.living.candyland;

import com.qb.stompy.living.Enemy;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.assets.TextureAtlas;
import com.rubynaxela.kyanite.game.entities.AnimatedEntity;
import com.rubynaxela.kyanite.util.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jsfml.graphics.Color;
import org.jsfml.system.Time;

@SuppressWarnings("FieldCanBeLocal")
public class Candy extends Enemy implements AnimatedEntity {

    private int boingedTexture = 0;
    private float timeSinceJump = 0;
    private final float timeBetweenJumps = 2, horizontalSpeed = 100, jumpStrength = 250, chaseMultiplier = 1.5f;
    private boolean hasJumped = false, facingLeft = true, foundTarget = false, didAHop = false;

    private final TextureAtlas textures = assets.get("texture_candy");
    private final Texture[][] animationTextures = textures.getMatrix(16, 8, 3, 2);


    public Candy(Color color) {
        super();
        mainBody.setSize(Vec2.f(32, 16));
        mainBody.setOrigin(mainBody.getSize().x / 2, mainBody.getSize().y);
        mainBody.setFillColor(color);
        setMaxHp(1);
    }

    @Override
    public void animate(@NotNull Time deltaTime, @NotNull Time elapsedTime) {
        if (!getLevelScene().isPaused()) {
            //Kill
            if (currentHp <= 0) {
                kill();
            }

            //Gravity
            speedY += 1000 * deltaTime.asSeconds();

            //Loop Variables
            float jumpMultiplier = 1, L = gGB().left, R = L + gGB().width, T = gGB().top, B = T + gGB().height;

            //Target player
            if (getLevelScene() != null) {
                boolean shouldClearTarget = true;
                if (getLevelScene().getPlayerCharacter() != null && !getLevelScene().getPlayerCharacter().isDead()) {
                    if (Math.abs(getLevelScene().getPlayerCharacter().getPositionOnMap().x - getPositionOnMap().x) < 600) {
                        if (Math.abs(getLevelScene().getPlayerCharacter().getPositionOnMap().x - getPositionOnMap().x) < 300) {
                            target = getLevelScene().getPlayerCharacter();
                            foundTarget = true;
                        }
                    }
                    shouldClearTarget = false;
                }
                if (shouldClearTarget) {
                    target = null;
                    foundTarget = false;
                    didAHop = false;
                }
            }

            if (target != null)
                jumpMultiplier = chaseMultiplier;

            //Jumping
            if (getLevelScene() != null) {
                if (Math.abs(getPositionOnMap().x - getLevelScene().getMapOffset().x) < window.getSize().x) {
                    if (onGround) {
                        if (foundTarget && !didAHop) {
                            didAHop = true;
                            speedY = -aggroJumpStrength;
                            onGround = false;
                            if (target != null) {
                                if (target.getPositionOnMap().x - getPositionOnMap().x < 0) {
                                    facingLeft = true;
                                    setScale(Math.abs(getScale().x), getScale().y);
                                } else {
                                    facingLeft = false;
                                    setScale(-Math.abs(getScale().x), getScale().y);
                                }
                            }
                        }
                        if (hasJumped) {
                            hasJumped = false;
                            speedX = 0;
                            //turnaround
                            if (target != null) {
                                if (target.getPositionOnMap().x - getPositionOnMap().x < 0) {
                                    facingLeft = true;
                                    setScale(Math.abs(getScale().x), getScale().y);
                                } else {
                                    facingLeft = false;
                                    setScale(-Math.abs(getScale().x), getScale().y);
                                }
                            } else {
                                if ((int) (Math.random() * 2) == 0) {
                                    facingLeft = !facingLeft;
                                    setScale(-getScale().x, getScale().y);
                                }
                            }
                        } else {
                            timeSinceJump += deltaTime.asSeconds();
                        }
                    }
                    if (timeSinceJump >= timeBetweenJumps / jumpMultiplier) {
                        if (facingLeft) {
                            speedX = -horizontalSpeed * jumpMultiplier;
                        } else {
                            speedX = horizontalSpeed * jumpMultiplier;
                        }
                        speedY = -jumpStrength * (float) Math.sqrt(jumpMultiplier);
                        onGround = false;
                        hasJumped = true;
                        timeSinceJump = (float) (Math.random() * 0.5);
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

            preventBlockCollision(deltaTime);

            //No ankle biting
            if (target != null) {
                if ((L < target.gGB().left + target.gGB().width && R > target.gGB().left) ||
                        (L + speedX * deltaTime.asSeconds() < target.gGB().left + target.gGB().width && R + speedX * deltaTime.asSeconds() > target.gGB().left)) {
                    if (T > target.gGB().top + target.gGB().height && speedY < 0) {
                        if (-speedY * deltaTime.asSeconds() > T - (target.gGB().top + target.gGB().height)) {
                            speedY = 0;
                            stomp();
                            target.bounce();
                        }
                    }
                }
            }

            //frame animation
            if (speedY * deltaTime.asSeconds() > 0 || timeSinceJump >= 0.75 * timeBetweenJumps / jumpMultiplier)
                animationTextures[1][boingedTexture].apply(this.mainBody);
            else if (speedY * deltaTime.asSeconds() < 0) animationTextures[2][boingedTexture].apply(this.mainBody);
            else animationTextures[0][boingedTexture].apply(this.mainBody);

            moveOnMap(speedX * deltaTime.asSeconds(), speedY * deltaTime.asSeconds());
        }
    }

    @Override
    public void kill() {
        boingedTexture = 1;
        if (onGround) dead = true;
    }
}
