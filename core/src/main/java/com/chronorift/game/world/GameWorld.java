package com.chronorift.game.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.chronorift.game.builder.PlayerBuilder;
import com.chronorift.game.combat.ArmorDamageHandler;
import com.chronorift.game.combat.DamageContext;
import com.chronorift.game.combat.DamageHandler;
import com.chronorift.game.combat.MinimumDamageHandler;
import com.chronorift.game.combat.PlayerResistanceHandler;
import com.chronorift.game.command.GameCommand;
import com.chronorift.game.core.GameAssets;
import com.chronorift.game.core.GameConfig;
import com.chronorift.game.entity.Enemy;
import com.chronorift.game.entity.LivingEntity;
import com.chronorift.game.entity.Player;
import com.chronorift.game.entity.Projectile;
import com.chronorift.game.entity.ProjectileOwner;
import com.chronorift.game.event.GameEvent;
import com.chronorift.game.event.GameEventBus;
import com.chronorift.game.event.GameEventType;
import com.chronorift.game.event.HudLog;
import com.chronorift.game.factory.TimelineFactory;
import com.chronorift.game.factory.TimelineFactoryProvider;
import com.chronorift.game.integration.TimelineAiIntegrator;
import com.chronorift.game.level.LevelDefinition;
import com.chronorift.game.level.LevelManager;
import com.chronorift.game.level.TimelineType;
import com.chronorift.game.level.objective.BossObjective;
import com.chronorift.game.level.objective.KillEnemiesObjective;
import com.chronorift.game.level.objective.ObjectiveGroup;
import com.chronorift.game.memento.CheckpointCaretaker;
import com.chronorift.game.time.TimeContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameWorld {
    private static final Vector2 STREET_PLAYER_SPAWN = new Vector2(230f, 165f);
    private static final Vector2 TOWER_DOOR = new Vector2(905f, 455f);
    private static final float DOOR_TRIGGER_RADIUS = 78f;
    private static final int BODY_COLLISION_ITERATIONS = 4;
    private static final float BODY_COLLISION_SKIN = 0.35f;
    private static final float SAME_POSITION_EPSILON = 0.001f;

    private final GameAssets assets;
    private final BitmapFont font;
    private final GameEventBus eventBus;
    private final HudLog hudLog;
    private final TimeContext timeContext;
    private final CheckpointCaretaker caretaker;
    private final LevelManager levelManager;
    private final TimelineAiIntegrator timelineAiIntegrator;
    private final DamageHandler damageChain;
    private final Player player;
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Projectile> projectiles = new ArrayList<>();

    private TimelineFactory currentFactory;
    private LevelDefinition currentLevel;
    private Enemy currentBoss;
    private int waveIndex;
    private int killsThisLevel;
    private float spawnTimer;
    private boolean bossSpawned;
    private boolean bossDefeated;
    private boolean streetDoorOpen;
    private boolean streetDoorLogged;
    private boolean levelAdvanceQueued;
    private float levelAdvanceDelay;
    private boolean gameOver;
    private boolean victory;

    public GameWorld(GameAssets assets, BitmapFont font) {
        this.assets = assets;
        this.font = font;
        this.eventBus = new GameEventBus();
        this.hudLog = new HudLog();
        eventBus.register(hudLog);
        this.timeContext = new TimeContext(eventBus);
        this.caretaker = new CheckpointCaretaker();
        this.levelManager = new LevelManager();
        this.timelineAiIntegrator = new TimelineAiIntegrator();
        this.damageChain = createDamageChain();
        this.player = new PlayerBuilder().build();
        caretaker.save(player.save());
        loadCurrentLevel();
    }

    private DamageHandler createDamageChain() {
        DamageHandler head = new PlayerResistanceHandler();
        head.linkWith(new ArmorDamageHandler()).linkWith(new MinimumDamageHandler());
        return head;
    }

    private void loadCurrentLevel() {
        currentFactory = TimelineFactoryProvider.create(levelManager.current());
        currentLevel = currentFactory.createLevelDefinition();
        timeContext.lockToZone(currentLevel.getZoneEffect());
        ObjectiveGroup objectives = new ObjectiveGroup("Objectives")
                .add(new KillEnemiesObjective(currentLevel.getWaves() * currentLevel.getEnemiesPerWave()));
        if (currentLevel.hasBoss()) {
            objectives.add(new BossObjective());
        }
        currentLevel.setObjectives(objectives);
        enemies.clear();
        projectiles.clear();
        currentBoss = null;
        waveIndex = 0;
        killsThisLevel = 0;
        bossSpawned = false;
        bossDefeated = false;
        streetDoorOpen = false;
        streetDoorLogged = false;
        levelAdvanceQueued = false;
        levelAdvanceDelay = 0f;
        spawnTimer = 1f;
        if (currentLevel.getType() == TimelineType.STREET) {
            player.getPosition().set(STREET_PLAYER_SPAWN);
        } else {
            player.getPosition().set(GameConfig.WORLD_WIDTH / 2f, GameConfig.WORLD_HEIGHT / 2f);
        }
        player.heal(player.getMaxHealth());
        eventBus.post(new GameEvent(GameEventType.LEVEL_CHANGED,
                "Loaded " + currentLevel.getTitle() + ": " + currentLevel.getDescription(), currentLevel));
    }

    public void execute(List<GameCommand> commands, float delta) {
        for (GameCommand command : commands) {
            command.execute(this, delta);
        }
    }

    public void update(float delta) {
        if (gameOver || victory) {
            return;
        }

        timeContext.update(delta);
        if (levelAdvanceQueued) {
            levelAdvanceDelay -= delta;
            if (levelAdvanceDelay <= 0f) {
                advanceLevel();
            }
            return;
        }

        player.update(delta, timeContext);
        spawnWaveLogic(delta);
        updateEnemies(delta);
        updateProjectiles(delta);
        handleProjectileHits();
        removeDeadEnemies();
        removeExpiredProjectiles();
        checkStreetDoorTransition();
        if (!player.isAlive()) {
            gameOver = true;
            eventBus.post(new GameEvent(GameEventType.LOG, "Chrono Agent down. The rift remains open.", null));
        }
        if (currentLevel.hasBoss() && bossDefeated && enemies.isEmpty()) {
            queueBossAdvance();
        }
    }

    private void spawnWaveLogic(float delta) {
        if (bossSpawned || enemies.size() > 0) {
            return;
        }
        if (!currentLevel.hasBoss() && waveIndex >= currentLevel.getWaves()) {
            openStreetDoor();
            return;
        }
        spawnTimer -= delta;
        if (spawnTimer > 0f) {
            return;
        }
        if (waveIndex < currentLevel.getWaves()) {
            waveIndex++;
            int count = currentLevel.getEnemiesPerWave();
            for (int i = 0; i < count; i++) {
                enemies.add(createMinionForCurrentLevel(i, spawnPointFor(i)));
            }
            eventBus.post(new GameEvent(GameEventType.LOG, "Wave " + waveIndex + "/" + currentLevel.getWaves() + " started.", null));
            spawnTimer = currentLevel.getType() == TimelineType.STREET ? 1.2f : 1.4f;
        } else if (currentLevel.hasBoss() && !bossSpawned) {
            currentBoss = currentFactory.createBoss(bossSpawnPoint(), currentLevel.getEnemyHpMultiplier(), currentLevel.getEnemyDamageMultiplier());
            enemies.add(currentBoss);
            bossSpawned = true;
            eventBus.post(new GameEvent(GameEventType.BOSS_SPAWNED,
                    "Boss appeared: " + currentLevel.getBossName() + " | Berserk at 30% HP.", currentBoss));
        }
    }

    private Enemy createMinionForCurrentLevel(int index, Vector2 position) {
        if (currentLevel.getType() == TimelineType.DESERT) {
            TimelineType[] mixedTypes = {TimelineType.MEDIEVAL, TimelineType.FROZEN, TimelineType.CYBER, TimelineType.DESERT};
            TimelineType mixedType = mixedTypes[(waveIndex + index) % mixedTypes.length];
            TimelineFactory factory = TimelineFactoryProvider.create(mixedType);
            return factory.createMinion(position, currentLevel.getEnemyHpMultiplier(), currentLevel.getEnemyDamageMultiplier());
        }
        return currentFactory.createMinion(position, currentLevel.getEnemyHpMultiplier(), currentLevel.getEnemyDamageMultiplier());
    }

    private void openStreetDoor() {
        streetDoorOpen = true;
        if (!streetDoorLogged) {
            eventBus.post(new GameEvent(GameEventType.LOG, "Tower entrance unlocked. Move into the glowing door marker.", null));
            streetDoorLogged = true;
        }
    }

    private void checkStreetDoorTransition() {
        if (currentLevel.getType() == TimelineType.STREET && streetDoorOpen
                && player.getPosition().dst2(TOWER_DOOR) <= DOOR_TRIGGER_RADIUS * DOOR_TRIGGER_RADIUS) {
            eventBus.post(new GameEvent(GameEventType.LOG, "Entering the tower...", null));
            advanceLevel();
        }
    }

    private Vector2 spawnPointFor(int index) {
        if (currentLevel.getType() == TimelineType.STREET) {
            Vector2[] burrows = {
                    new Vector2(700f, 470f),
                    new Vector2(820f, 350f),
                    new Vector2(1010f, 280f),
                    new Vector2(610f, 230f),
                    new Vector2(760f, 175f),
                    new Vector2(970f, 425f)
            };
            return jitter(burrows[index % burrows.length], 28f);
        }
        if (currentLevel.getType() == TimelineType.MEDIEVAL) {
            Vector2[] jungleClearings = {
                    new Vector2(210f, 190f),
                    new Vector2(GameConfig.WORLD_WIDTH - 215f, 195f),
                    new Vector2(220f, GameConfig.WORLD_HEIGHT - 190f),
                    new Vector2(GameConfig.WORLD_WIDTH - 230f, GameConfig.WORLD_HEIGHT - 190f),
                    new Vector2(GameConfig.WORLD_WIDTH / 2f, 132f)
            };
            return jitter(jungleClearings[index % jungleClearings.length], 42f);
        }
        Vector2[] entries = {
                new Vector2(150f, 140f),
                new Vector2(GameConfig.WORLD_WIDTH - 150f, 140f),
                new Vector2(150f, GameConfig.WORLD_HEIGHT - 140f),
                new Vector2(GameConfig.WORLD_WIDTH - 150f, GameConfig.WORLD_HEIGHT - 140f)
        };
        return jitter(entries[index % entries.length], 46f);
    }

    private Vector2 bossSpawnPoint() {
        return new Vector2(GameConfig.WORLD_WIDTH / 2f, GameConfig.WORLD_HEIGHT / 2f + 90f);
    }

    private Vector2 jitter(Vector2 base, float amount) {
        return new Vector2(base.x + MathUtils.random(-amount, amount), base.y + MathUtils.random(-amount, amount));
    }

    private void updateEnemies(float delta) {
        for (Enemy enemy : enemies) {
            enemy.update(delta, player, this);
            clampEnemyToWorld(enemy);
        }
        resolveEnemyBodyCollisions();
    }

    private void resolveEnemyBodyCollisions() {
        for (int iteration = 0; iteration < BODY_COLLISION_ITERATIONS; iteration++) {
            for (Enemy enemy : enemies) {
                resolveEnemyVsPlayer(enemy);
            }
            for (int i = 0; i < enemies.size(); i++) {
                Enemy first = enemies.get(i);
                for (int j = i + 1; j < enemies.size(); j++) {
                    resolveEnemyVsEnemy(first, enemies.get(j), i, j);
                }
            }
            for (Enemy enemy : enemies) {
                clampEnemyToWorld(enemy);
            }
        }
    }

    private void resolveEnemyVsPlayer(Enemy enemy) {
        Vector2 delta = new Vector2(enemy.getPosition()).sub(player.getPosition());
        float minimumDistance = enemy.getRadius() + player.getRadius() + BODY_COLLISION_SKIN;
        pushEnemyOutOfCircle(enemy, delta, minimumDistance, 1f);
    }

    private void resolveEnemyVsEnemy(Enemy first, Enemy second, int firstIndex, int secondIndex) {
        Vector2 delta = new Vector2(second.getPosition()).sub(first.getPosition());
        float minimumDistance = first.getRadius() + second.getRadius() + BODY_COLLISION_SKIN;
        float distance2 = delta.len2();
        if (distance2 >= minimumDistance * minimumDistance) {
            return;
        }

        if (distance2 <= SAME_POSITION_EPSILON) {
            float angle = 37f * (firstIndex + 1) + 73f * (secondIndex + 1);
            delta.set(MathUtils.cosDeg(angle), MathUtils.sinDeg(angle));
            distance2 = 1f;
        }

        float distance = (float) Math.sqrt(distance2);
        float overlap = minimumDistance - distance;
        if (overlap <= 0f) {
            return;
        }

        delta.scl(1f / distance);
        float firstShare = first.isBoss() && !second.isBoss() ? 0.25f : 0.5f;
        float secondShare = second.isBoss() && !first.isBoss() ? 0.25f : 0.5f;
        if (first.isBoss() && !second.isBoss()) {
            secondShare = 0.75f;
        } else if (second.isBoss() && !first.isBoss()) {
            firstShare = 0.75f;
        }

        first.getPosition().mulAdd(delta, -overlap * firstShare);
        second.getPosition().mulAdd(delta, overlap * secondShare);
    }

    private void pushEnemyOutOfCircle(Enemy enemy, Vector2 delta, float minimumDistance, float pushShare) {
        float distance2 = delta.len2();
        if (distance2 >= minimumDistance * minimumDistance) {
            return;
        }

        if (distance2 <= SAME_POSITION_EPSILON) {
            delta.set(1f, 0f);
            distance2 = 1f;
        }

        float distance = (float) Math.sqrt(distance2);
        float overlap = minimumDistance - distance;
        if (overlap <= 0f) {
            return;
        }

        delta.scl(1f / distance);
        enemy.getPosition().mulAdd(delta, overlap * pushShare);
    }

    private void clampEnemyToWorld(Enemy enemy) {
        enemy.getPosition().x = MathUtils.clamp(enemy.getPosition().x, enemy.getRadius(), GameConfig.WORLD_WIDTH - enemy.getRadius());
        enemy.getPosition().y = MathUtils.clamp(enemy.getPosition().y, enemy.getRadius(), GameConfig.WORLD_HEIGHT - enemy.getRadius());
    }

    private void updateProjectiles(float delta) {
        for (Projectile projectile : projectiles) {
            projectile.update(delta);
        }
    }

    private void handleProjectileHits() {
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            if (projectile.getOwner() == ProjectileOwner.PLAYER) {
                for (Enemy enemy : enemies) {
                    if (enemy.getPosition().dst2(projectile.getPosition()) <= squared(enemy.getRadius() + projectile.getRadius())) {
                        applyDamage(player, enemy, projectile.getDamage());
                        iterator.remove();
                        break;
                    }
                }
            } else {
                if (player.getPosition().dst2(projectile.getPosition()) <= squared(player.getRadius() + projectile.getRadius())) {
                    applyDamage(null, player, projectile.getDamage());
                    iterator.remove();
                }
            }
        }
    }

    private void removeDeadEnemies() {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (!enemy.isAlive()) {
                iterator.remove();
                killsThisLevel++;
                player.addScore(enemy.isBoss() ? 500 : 120);
                if (enemy.isBoss()) {
                    bossDefeated = true;
                }
                eventBus.post(new GameEvent(GameEventType.ENEMY_KILLED,
                        enemy.getName() + " defeated.", enemy));
            }
        }
    }

    private void removeExpiredProjectiles() {
        projectiles.removeIf(projectile -> !projectile.isAlive()
                || projectile.getPosition().x < -20f || projectile.getPosition().x > GameConfig.WORLD_WIDTH + 20f
                || projectile.getPosition().y < -20f || projectile.getPosition().y > GameConfig.WORLD_HEIGHT + 20f);
    }

    private void queueBossAdvance() {
        if (levelAdvanceQueued) {
            return;
        }
        levelAdvanceQueued = true;
        levelAdvanceDelay = 1.45f;
        projectiles.clear();
        eventBus.post(new GameEvent(GameEventType.LOG, "Boss defeated. Timeline stabilizing before the next stage...", null));
    }

    private void advanceLevel() {
        if (levelManager.hasNext()) {
            caretaker.save(player.save());
            levelManager.next();
            loadCurrentLevel();
        } else {
            victory = true;
            eventBus.post(new GameEvent(GameEventType.LOG, "All tower timelines stabilized. Victory!", null));
        }
    }

    private float squared(float value) {
        return value * value;
    }

    public void render(SpriteBatch batch) {
        batch.setColor(Color.WHITE);
        batch.draw(assets.texture(currentLevel.getBackgroundPath()), 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        drawZoneOverlay(batch);
        drawStreetDoorMarker(batch);
        for (Projectile projectile : projectiles) {
            projectile.render(batch, assets);
        }
        for (Enemy enemy : enemies) {
            enemy.render(batch, assets);
            float healthOffset = enemy.isBoss() ? (enemy.isBerserk() ? 124f : 106f) : 60f;
            drawHealthBar(batch, enemy.getPosition().x - 28f, enemy.getPosition().y + healthOffset, 56f,
                    enemy.getHealth() / enemy.getMaxHealth(), enemy.isBoss() ? (enemy.isBerserk() ? Color.RED : Color.ORANGE) : Color.SCARLET);
        }
        player.render(batch, assets);
        drawHealthBar(batch, player.getPosition().x - 30f, player.getPosition().y + player.getRadius() + 60f, 60f,
                player.getHealth() / player.getMaxHealth(), Color.GREEN);
        drawHud(batch);
        drawTransitionOverlay(batch);
    }


    private void drawZoneOverlay(SpriteBatch batch) {
        Color old = batch.getColor().cpy();
        Color zoneColor = currentLevel.getZoneEffect().accentColor();
        batch.setColor(zoneColor.r, zoneColor.g, zoneColor.b, 0.08f);
        batch.draw(assets.pixel(), 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        batch.setColor(old);
    }

    private void drawStreetDoorMarker(SpriteBatch batch) {
        if (currentLevel.getType() != TimelineType.STREET) {
            return;
        }
        Color old = batch.getColor().cpy();
        batch.setColor(streetDoorOpen ? new Color(0.1f, 0.9f, 1f, 0.34f) : new Color(1f, 0.15f, 0.1f, 0.18f));
        batch.draw(assets.pixel(), TOWER_DOOR.x - 36f, TOWER_DOOR.y - 48f, 72f, 96f);
        batch.setColor(old);
    }

    private void drawTransitionOverlay(SpriteBatch batch) {
        if (!levelAdvanceQueued) {
            return;
        }
        Color oldBatch = batch.getColor().cpy();
        Color oldFont = font.getColor().cpy();
        batch.setColor(0f, 0f, 0f, 0.42f);
        batch.draw(assets.pixel(), 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        batch.setColor(oldBatch);
        font.setColor(Color.GOLD);
        font.draw(batch, "BOSS DEFEATED", GameConfig.WORLD_WIDTH / 2f - 92f, GameConfig.WORLD_HEIGHT / 2f + 28f);
        font.setColor(Color.WHITE);
        font.draw(batch, "Timeline stabilizing... next stage opens in " + String.format("%.1fs", Math.max(0f, levelAdvanceDelay)),
                GameConfig.WORLD_WIDTH / 2f - 205f, GameConfig.WORLD_HEIGHT / 2f - 10f);
        font.setColor(oldFont);
    }

    private void drawHealthBar(SpriteBatch batch, float x, float y, float width, float percent, Color fill) {
        Color old = batch.getColor().cpy();
        batch.setColor(0f, 0f, 0f, 0.55f);
        batch.draw(assets.pixel(), x, y, width, 7f);
        batch.setColor(fill);
        batch.draw(assets.pixel(), x + 1f, y + 1f, (width - 2f) * MathUtils.clamp(percent, 0f, 1f), 5f);
        batch.setColor(old);
    }

    private void drawHud(SpriteBatch batch) {
        Color old = font.getColor().cpy();
        font.setColor(Color.WHITE);
        font.draw(batch, "Chrono Rift - " + currentLevel.getTitle(), 18f, GameConfig.WORLD_HEIGHT - 18f);
        font.draw(batch, "Stage: " + levelManager.number() + "/" + levelManager.total(), 18f, GameConfig.WORLD_HEIGHT - 44f);
        font.draw(batch, "Zone: " + timeContext.current().getName() + " (locked by level)", 18f, GameConfig.WORLD_HEIGHT - 70f);
        font.draw(batch, "Effect: " + timeContext.current().getDescription(), 18f, GameConfig.WORLD_HEIGHT - 96f);
        font.draw(batch, "Tools: Chrono Elixir Q " + cooldown(player.getToolbelt().getChronoElixirCooldown())
                + " | Blue Shield E " + cooldown(player.getToolbelt().getShieldCooldown())
                + " | Ultra R " + cooldown(player.getToolbelt().getOverdriveCooldown()),
                18f, GameConfig.WORLD_HEIGHT - 122f);
        font.draw(batch, "Tools: Rewind C " + cooldown(player.getToolbelt().getRewindCooldown())
                + " | Dash SPACE", 18f, GameConfig.WORLD_HEIGHT - 148f);
        font.draw(batch, "HP: " + (int) player.getHealth() + "/" + (int) player.getMaxHealth()
                + "   Score: " + player.getScore() + "   Active: " + player.getToolbelt().activeItemsLabel(),
                18f, GameConfig.WORLD_HEIGHT - 174f);
        float objectiveY = GameConfig.WORLD_HEIGHT - 214f;
        for (String line : currentLevel.getObjectives().describe(this).split("\n")) {
            font.draw(batch, line, 18f, objectiveY);
            objectiveY -= 22f;
        }
        if (currentLevel.getType() == TimelineType.STREET) {
            font.draw(batch, streetDoorOpen ? "Tower door: OPEN - enter the blue marker" : "Tower door: LOCKED - clear mole guards",
                    18f, objectiveY);
        }
        float logY = 124f;
        font.setColor(currentLevel.getAccentColor());
        font.draw(batch, "Battle Log", GameConfig.WORLD_WIDTH - 310f, logY + 22f);
        font.setColor(Color.WHITE);
        for (String line : hudLog.getLines()) {
            font.draw(batch, "- " + line, GameConfig.WORLD_WIDTH - 310f, logY);
            logY -= 22f;
        }
        if (bossSpawned && currentBoss != null && currentBoss.isAlive()) {
            font.setColor(currentBoss.isBerserk() ? Color.RED : Color.ORANGE);
            font.draw(batch, currentLevel.getBossName() + " HP: " + (int) currentBoss.getHealth() + "/" + (int) currentBoss.getMaxHealth()
                            + (currentBoss.isBerserk() ? "  BERSERK" : ""),
                    GameConfig.WORLD_WIDTH / 2f - 150f, GameConfig.WORLD_HEIGHT - 24f);
        }
        font.setColor(old);
    }

    private String cooldown(float value) {
        return value <= 0f ? "READY" : String.format("%.1fs", value);
    }

    public void spawnProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    public void applyDamage(LivingEntity source, LivingEntity target, float amount) {
        if (target == player && player.getToolbelt().shieldActive()) {
            eventBus.post(new GameEvent(GameEventType.LOG, "Blue Shield blocked all incoming damage.", null));
            return;
        }

        DamageContext context = new DamageContext(source, target, amount);
        float finalDamage = damageChain.handle(context);
        target.damage(finalDamage);
        if (target == player) {
            eventBus.post(new GameEvent(GameEventType.PLAYER_DAMAGED,
                    "Player hit for " + (int) finalDamage + " damage.", null));
        }
    }

    public void postLog(String message) {
        eventBus.post(new GameEvent(GameEventType.LOG, message, null));
    }

    public void movePlayer(Vector2 direction) {
        player.setMoveIntent(direction);
    }

    public void dashPlayer() {
        if (player.dash(timeContext)) {
            eventBus.post(new GameEvent(GameEventType.LOG, "Phase Crystal dash executed.", null));
        }
    }

    public void playerShoot(Vector2 target) {
        player.shoot(target, this);
    }


    public void useChronoElixir() {
        if (player.useChronoElixir()) {
            eventBus.post(new GameEvent(GameEventType.LOG,
                    "Chrono Elixir used: slow, reverse, boss, and frozen penalties are reduced temporarily.", null));
        }
    }


    public void activateShield() {
        if (player.activateShield()) {
            eventBus.post(new GameEvent(GameEventType.LOG, "Blue Shield activated: damage immunity for 5 seconds.", null));
        }
    }

    public void activateOverdrive() {
        if (player.activateOverdrive()) {
            eventBus.post(new GameEvent(GameEventType.LOG, "Ultra activated: hero form and combat power upgraded temporarily.", null));
        }
    }

    public void rewindPlayer() {
        if (player.rewind(caretaker.getSnapshot())) {
            eventBus.post(new GameEvent(GameEventType.PLAYER_REWOUND, "Checkpoint rewind successful.", null));
        }
    }

    public float getEffectiveEnemyMultiplier(Enemy enemy) {
        return timelineAiIntegrator.enemySpeedMultiplier(enemy, currentLevel, timeContext);
    }

    public float getEffectiveEnemyMultiplier() {
        return timeContext.current().enemySpeedMultiplier();
    }

    public float getEffectiveEnemyDamageMultiplier(Enemy enemy) {
        return timelineAiIntegrator.enemyDamageMultiplier(enemy, currentLevel);
    }

    public TimeContext getTimeContext() {
        return timeContext;
    }

    public int getKillsThisLevel() {
        return killsThisLevel;
    }

    public boolean isBossDefeated() {
        return bossDefeated;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isVictory() {
        return victory;
    }

    public int getScore() {
        return player.getScore();
    }
}
