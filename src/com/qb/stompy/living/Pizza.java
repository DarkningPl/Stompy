package com.qb.stompy.living;

import com.qb.stompy.objects.Enemy;
import com.qb.stompy.objects.SolidBlock;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.entities.AnimatedEntity;
import com.rubynaxela.kyanite.util.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Time;

public class Pizza extends Enemy implements AnimatedEntity {
    private float timeSinceStomped = 0;
    private final float recoveryTime = 0.4f;
    private boolean stomped = false;

    public Pizza() {
        Texture texture = assets.get("texture_pizza");
        texture.apply(this);
        setTextureRect(new IntRect(0, 0, 16, 15));
        setSize(Vec2.f(32, 30));
        setOrigin(getSize().x / 2, getSize().y);
        setMaxHp(1);
    }

    @Override
    public void animate(@NotNull Time deltaTime, @NotNull Time elapsedTime) {
        if (currentHp <= 0) kill();
        if (affectedByGravity) speedY += 1000 * deltaTime.asSeconds();

        //Loop Variables
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
                    }
                }
            }
        }
        moveOnMap(speedX * deltaTime.asSeconds(), speedY * deltaTime.asSeconds());

        //animation
        if (stomped)
            timeSinceStomped += deltaTime.asSeconds();
        if (timeSinceStomped > 0) {
            setTextureRect(new IntRect(16, 0, 16, 15));
            if (timeSinceStomped >= 0.25 * recoveryTime) {
                setTextureRect(new IntRect(32, 0, 16, 15));
                if (timeSinceStomped >= 0.75 * recoveryTime) {
                    setTextureRect(new IntRect(48, 0, 16, 15));
                    if (timeSinceStomped >= recoveryTime) {
                        setTextureRect(new IntRect(0, 0, 16, 15));
                        timeSinceStomped = 0;
                        stomped = false;
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
