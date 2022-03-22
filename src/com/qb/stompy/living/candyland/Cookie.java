package com.qb.stompy.living.candyland;

import com.qb.stompy.living.Enemy;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.entities.AnimatedEntity;
import com.rubynaxela.kyanite.util.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Time;

public class Cookie extends Enemy implements AnimatedEntity {
    private float timeSinceStomped = 0;
    private final float recoveryTime = 0.4f;
    private boolean stomped = false;

    public Cookie() {
        Texture texture = assets.get("texture_cookie");
        texture.apply(this.mainBody);
        mainBody.setTextureRect(new IntRect(0, 0, 16, 15));
        mainBody.setSize(Vec2.f(32, 30));
        mainBody.setOrigin(mainBody.getSize().x / 2, mainBody.getSize().y);
        setMaxHp(1);
    }

    @Override
    public void animate(@NotNull Time deltaTime, @NotNull Time elapsedTime) {
        if (!getLevelScene().isPaused()) {
            if (currentHp <= 0) kill();
            if (affectedByGravity) speedY += 1000 * deltaTime.asSeconds();

            //Loop Variables
            float L = gGB().left, R = L + gGB().width, T = gGB().top, B = T + gGB().height;

            //Target player
            if (getLevelScene() != null) {
                if (getLevelScene().getPlayerCharacter() != null && !getLevelScene().getPlayerCharacter().isDead())
                    target = getLevelScene().getPlayerCharacter();
                else
                    target = null;
            }

            preventBlockCollision(deltaTime);
            moveOnMap(speedX * deltaTime.asSeconds(), speedY * deltaTime.asSeconds());

            //Animation
            if (stomped)
                timeSinceStomped += deltaTime.asSeconds();
            if (timeSinceStomped > 0) {
                mainBody.setTextureRect(new IntRect(16, 0, 16, 15));
                if (timeSinceStomped >= 0.25 * recoveryTime) {
                    mainBody.setTextureRect(new IntRect(32, 0, 16, 15));
                    if (timeSinceStomped >= 0.75 * recoveryTime) {
                        mainBody.setTextureRect(new IntRect(48, 0, 16, 15));
                        if (timeSinceStomped >= recoveryTime) {
                            mainBody.setTextureRect(new IntRect(0, 0, 16, 15));
                            timeSinceStomped = 0;
                            stomped = false;
                        }
                    }
                }
            }
        }
    }
    @Override
    public void stomp() {
        stomped = true;
    }
}
