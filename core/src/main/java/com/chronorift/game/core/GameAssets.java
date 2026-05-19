package com.chronorift.game.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chronorift.game.animation.AnimatedSprite;
import com.chronorift.game.animation.AnimationProfile;

import java.util.HashMap;
import java.util.Map;

public class GameAssets {
    private final AssetManager assetManager = new AssetManager();
    private final Map<String, AnimatedSprite> animations = new HashMap<>();
    private Texture pixel;

    public void load() {
        String[] textures = {
                // Static fallback character sprites
                "game/player.png",
                "game/player_ultimate.png",
                "game/mole_guard.png",
                "game/orc_soldier.png",
                "game/mech_soldier.png",
                "game/frost_soldier.png",
                "game/grunt.png",
                "game/boss_medieval.png",
                "game/boss_cyber.png",
                "game/boss_frozen.png",
                "game/boss_desert.png",
                "game/boss_mech.png",
                "game/boss_mech_berserk.png",
                "game/boss_ice.png",
                "game/boss_ice_berserk.png",
                "game/boss_orc.png",
                "game/boss_orc_berserk.png",

                // Legacy backgrounds
                "game/bg_medieval.png",
                "game/bg_cyber.png",
                "game/bg_frozen.png",
                "game/bg_desert.png",
                "game/bg_street.png",
                "game/bg_mech_floor.png",
                "game/bg_ice_floor.png",
                "game/bg_orc_floor.png",

                // Clean HD gameplay maps
                "game/bg_street_hd.png",
                "game/bg_mech_floor_hd.png",
                "game/bg_ice_floor_hd.png",
                "game/bg_orc_floor_hd.png",
                "game/bg_jungle_floor_hd.png",

                // Animation sprite sheets
                "game/player_sheet.png",
                "game/mole_guard_sheet.png",
                "game/orc_soldier_sheet.png",
                "game/mech_soldier_sheet.png",
                "game/frost_soldier_sheet.png",
                "game/boss_mech_sheet.png",
                "game/boss_ice_sheet.png",
                "game/boss_orc_sheet.png"
        };
        for (String texture : textures) {
            assetManager.load(texture, Texture.class);
        }
        assetManager.finishLoading();
        registerAnimations();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixel = new Texture(pixmap);
        pixmap.dispose();
    }

    private void registerAnimations() {
        register("game/player.png", "game/player_sheet.png", 4, 4, true, AnimationProfile.PLAYER, 0.14f);
        register("game/player_ultimate.png", "game/player_sheet.png", 4, 4, true, AnimationProfile.PLAYER, 0.12f);

        register("game/mole_guard.png", "game/mole_guard_sheet.png", 4, 2, false, AnimationProfile.MOLE, 0.16f);
        register("game/orc_soldier.png", "game/orc_soldier_sheet.png", 4, 2, false, AnimationProfile.MINION, 0.15f);
        register("game/mech_soldier.png", "game/mech_soldier_sheet.png", 4, 2, false, AnimationProfile.MINION, 0.13f);
        register("game/frost_soldier.png", "game/frost_soldier_sheet.png", 4, 2, false, AnimationProfile.MINION, 0.16f);

        register("game/boss_mech.png", "game/boss_mech_sheet.png", 4, 4, true, AnimationProfile.BOSS, 0.15f);
        register("game/boss_mech_berserk.png", "game/boss_mech_sheet.png", 4, 4, true, AnimationProfile.BOSS, 0.12f);
        register("game/boss_ice.png", "game/boss_ice_sheet.png", 4, 4, true, AnimationProfile.BOSS, 0.16f);
        register("game/boss_ice_berserk.png", "game/boss_ice_sheet.png", 4, 4, true, AnimationProfile.BOSS, 0.13f);
        register("game/boss_orc.png", "game/boss_orc_sheet.png", 4, 4, true, AnimationProfile.BOSS, 0.15f);
        register("game/boss_orc_berserk.png", "game/boss_orc_sheet.png", 4, 4, true, AnimationProfile.BOSS, 0.12f);
    }

    private void register(String spritePath, String sheetPath, int columns, int rows, boolean alternateForm,
                          AnimationProfile profile, float frameDuration) {
        animations.put(spritePath, new AnimatedSprite(texture(sheetPath), columns, rows, alternateForm, profile, frameDuration));
    }

    public Texture texture(String path) {
        return assetManager.get(path, Texture.class);
    }

    public TextureRegion region(String path) {
        return new TextureRegion(texture(path));
    }

    public AnimatedSprite animation(String spritePath) {
        return animations.get(spritePath);
    }

    public Texture pixel() {
        return pixel;
    }

    public void dispose() {
        if (pixel != null) {
            pixel.dispose();
        }
        assetManager.dispose();
    }
}
