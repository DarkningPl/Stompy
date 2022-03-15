package com.qb.stompy.objects;

import com.qb.stompy.scenes.LoadedLevelScene;
import com.qb.stompy.scenes.LoadedWorldScene;
import com.rubynaxela.kyanite.game.GameContext;
import com.rubynaxela.kyanite.game.assets.AssetsBundle;
import com.rubynaxela.kyanite.util.Vec2;
import com.rubynaxela.kyanite.window.Window;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;

public abstract class GameObject extends RectangleShape {

    private Vector2f positionOnMap, mapOffset;
    protected AssetsBundle assets = GameContext.getInstance().getAssetsBundle();
    protected Window window = GameContext.getInstance().getWindow();

    public GameObject() {
        positionOnMap = Vec2.f(0, 0);
        mapOffset = Vec2.f(0, 0);
    }

    public void setPositionOnMap(Vector2f pos) {
        positionOnMap = pos;
        if (getLevelScene() != null) mapOffset = getLevelScene().getMapOffset();
        else if (getWorldScene() != null) mapOffset = getWorldScene().getMapOffset();
        else mapOffset = Vec2.f(0, 0);
    }

    public void setPositionOnMap(float posX, float posY) {
        positionOnMap = new Vector2f(posX, posY);
    }

    public Vector2f getPositionOnMap() {
        return positionOnMap;
    }

    public void moveOnMap(Vector2f offset) {
        positionOnMap = new Vector2f(positionOnMap.x + offset.x, positionOnMap.y + offset.y);
    }

    public void moveOnMap(float offsetX, float offsetY) {
        positionOnMap = new Vector2f(positionOnMap.x + offsetX, positionOnMap.y + offsetY);
    }

    public FloatRect gGB() {
        return new FloatRect(Vec2.add(Vec2.f(getGlobalBounds().left, getGlobalBounds().top), mapOffset),
                Vec2.f(getGlobalBounds().width, getGlobalBounds().height));
    }

    public LoadedLevelScene getLevelScene() {
        if (GameContext.getInstance().getWindow().getScene() instanceof final LoadedLevelScene levelScene)
            return levelScene;
        return null;
    }

    public LoadedWorldScene getWorldScene() {
        if (GameContext.getInstance().getWindow().getScene() instanceof final LoadedWorldScene worldScene)
            return worldScene;
        return null;
    }
}
