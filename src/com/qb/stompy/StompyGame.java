package com.qb.stompy;

import com.qb.stompy.loaders.LoadedWorldScene;
import com.rubynaxela.kyanite.game.Game;
import com.rubynaxela.kyanite.game.assets.AssetsBundle;
import com.rubynaxela.kyanite.game.assets.DataAsset;
import com.rubynaxela.kyanite.game.assets.Texture;
import com.rubynaxela.kyanite.game.assets.TextureAtlas;
import com.rubynaxela.kyanite.window.Window;

public class StompyGame extends Game {
    public StompyGame(){
        final AssetsBundle assets = getContext().getAssetsBundle();
        final Window window = getContext().setupWindow(800, 600, "Stompy Game");

        //TODO
        //Register assets

        assets.register("texture_candy", new TextureAtlas("assets/images/candy.png"));
        assets.register("texture_pizza", new Texture("assets/images/pizza.png"));
        assets.register("texture_cupcake", new TextureAtlas("assets/images/cupcake.png"));
        assets.register("texture_chocolate", new Texture("assets/images/chocolate.png"));
        assets.register("texture_donut", new Texture("assets/images/donut.png"));

        assets.register("texture_sky", new Texture("assets/images/sky.png"));
        assets.register("texture_dirt", new Texture("assets/images/dirt.png"));
        assets.register("texture_brick", new Texture("assets/images/brick.png"));
        assets.register("texture_meadow", new Texture("assets/images/meadow.png"));
        assets.register("texture_smile", new Texture("assets/images/smile.png"));

        assets.register("texture_map_point", new Texture("assets/images/map_point.png"));
        assets.register("texture_map_path", new TextureAtlas("assets/images/map_path.png"));

        assets.register("lemon", new Texture("assets/images/temp_lemon.png"));
        assets.register("texture_strawberry", new Texture("assets/images/strawberry.png"));

        assets.register("levels", new DataAsset("assets/data/levels_data.json"));
        assets.register("maps", new DataAsset("assets/data/maps_data.json"));
        //assets.register("character", new DataAsset("assets/data/character_data.json"));


        window.setScene(new LoadedWorldScene(0));
    }
}
