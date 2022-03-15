package com.qb.stompy.buttons;

import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.assets.TextureAtlas;
import com.rubynaxela.kyanite.game.entities.AnimatedEntity;
import com.rubynaxela.kyanite.util.Vec2;
import com.rubynaxela.kyanite.window.event.MouseButtonListener;
import org.jetbrains.annotations.NotNull;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.MouseButtonEvent;

public abstract class GameButton extends RectangleShape implements AnimatedEntity {

    private final Texture idleTexture, hoverTexture;
    private boolean isLPressed = false, wasLPressed = false, cancelPress = false, selected = false;

    public GameButton(String textureName, Vector2i textureSize) {
        TextureAtlas atlas = GameContext.getInstance().getAssetsBundle().get(textureName);
        idleTexture = atlas.get(0, 0, textureSize.x, textureSize.y);
        hoverTexture = atlas.get(0, textureSize.y, textureSize.x, 2 * textureSize.y);
        idleTexture.apply(this);
        setSize(Vec2.f(idleTexture.getSize()));
        setOrigin(Vec2.divideFloat(getSize(), 2));

        GameContext.getInstance().getWindow().addMouseButtonListener(new MouseButtonListener() {
            @Override
            public void mouseButtonPressed(MouseButtonEvent e) {
                if (e.button == Mouse.Button.LEFT) {
                    isLPressed = true;
                }
            }

            @Override
            public void mouseButtonReleased(MouseButtonEvent e) {
                if (e.button == Mouse.Button.LEFT) {
                    isLPressed = false;
                    cancelPress = false;
                }
            }
        });
    }

    public abstract void clickAction();

    public void select() {
        selected = true;
    }
    public void unselect() {
        selected = false;
    }
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void animate(@NotNull Time deltaTime, @NotNull Time elapsedTime) {
        if (getGlobalBounds().contains(Vec2.f(Mouse.getPosition(GameContext.getInstance().getWindow())))) {
            hoverTexture.apply(this);
        } else {
            idleTexture.apply(this);
            if (isLPressed) cancelPress = true;
        }
        if (selected) {
            hoverTexture.apply(this);
            if (Keyboard.isKeyPressed(Keyboard.Key.SPACE)) {
                clickAction();
            }
        }

        //Do action
        if (!isLPressed && wasLPressed) {
            clickAction();
        }

        wasLPressed = isLPressed && !cancelPress;
    }
}
