package com.qb.stompy.objects;

import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.util.Vec2;

public class MapPoint extends MapObject {
    public MapPoint(float scale) {
        Texture texture = assets.get("texture_map_point");
        texture.apply(this);
        setSize(Vec2.f(texture.getSize().x * scale, texture.getSize().y * scale));
        setOrigin(getSize().x / 2, getSize().y / 2);
    }
}
