package com.qb.stompy;

import com.qb.stompy.dataReaders.ProgressReader;
import com.qb.stompy.scenes.LoadedWorldScene;
import com.rubynaxela.kyanite.game.Game;
import com.rubynaxela.kyanite.game.assets.*;
import com.rubynaxela.kyanite.window.Window;

public class StompyGame extends Game {
    public StompyGame(){
        final AssetsBundle assets = getContext().getAssetsBundle();
        final Window window = getContext().setupWindow(800, 600, "Stompy Game");


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

        assets.register("texture_strawberry", new Texture("assets/images/strawberry.png"));

        assets.register("font_mc", new FontFace("assets/fonts/Minecraftia-Regular.ttf"));

        assets.register("butt", new TextureAtlas("assets/images/test_button.png"));
        assets.register("button_go", new TextureAtlas("assets/images/button_go.png"));

        assets.register("worlds", new DataAsset("assets/data/worlds_data.json"));
        for (int i = 0; i < getContext().getAssetsBundle().<DataAsset>get("worlds").convertTo(ProgressReader.class).getWorlds().size(); i++) {
            assets.register("world_" + i, new DataAsset("assets/data/world_" + i + "_data.json"));
        }
        assets.register("levels", new DataAsset("assets/data/levels_data.json"));
        //assets.register("character", new DataAsset("assets/data/character_data.json"));

        //DataFile dat = new DataFile("assets/data/test.txt");
        //dat.append("" + 32);
        //System.out.println(dat.read());

        window.setScene(new LoadedWorldScene(0));
//        window.setScene(new LoadedLevelScene(0, 2));
//        window.setScene(new StompyScene());
    }
}
