package com.qb.stompy.buttons;

import com.qb.stompy.scenes.LoadedWorldScene;
import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.HUD;
import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

public class CancelButton extends GameButton {
    public CancelButton(Vector2f buttonSize, String message) {
        super(buttonSize, message);
    }
    public CancelButton(Vector2f buttonSize, int fontSize, String message, Color textColor) {
        super(buttonSize, fontSize, message, textColor);
    }

    @Override
    public void clickAction() {
        window.setHUD(HUD.empty());
        if (window.getScene() instanceof final LoadedWorldScene worldScene) {
            worldScene.getPlayerCharacter().enableMovement();
            worldScene.getPlayerCharacter().cancelButtonPress();
        }
    }
}
