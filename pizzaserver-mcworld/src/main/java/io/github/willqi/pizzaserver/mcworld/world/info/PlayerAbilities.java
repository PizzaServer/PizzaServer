package io.github.willqi.pizzaserver.mcworld.world.info;

public class PlayerAbilities {

    private boolean canAttackMobs;
    private boolean canAttackPlayers;
    private boolean canBuild;
    private boolean canFly;
    private boolean canInstaBuild;  // TODO: what exactly is this? How does it differ from canBuild?
    private boolean canMine;
    private boolean canOpenContainers;
    private boolean canTeleport;
    private boolean canUseDoorsAndSwitches;
    private float flySpeed;
    private boolean isFlying;
    private boolean isInvulnerable;
    private boolean isOp;
    private boolean lightning;  // TODO: what is this?
    private int permissionLevel;
    private int playersPermissionLevel; // TODO: what is this and isn't it the same as permissionLevel?
    private float walkSpeed;


    public boolean canAttackMobs() {
        return this.canAttackMobs;
    }

    public PlayerAbilities setCanAttackMobs(boolean canAttackMobs) {
        this.canAttackMobs = canAttackMobs;
        return this;
    }

    public boolean canAttackPlayers() {
        return this.canAttackPlayers;
    }

    public PlayerAbilities setCanAttackPlayers(boolean canAttackPlayers) {
        this.canAttackPlayers = canAttackPlayers;
        return this;
    }

    public boolean canBuild() {
        return this.canBuild;
    }

    public PlayerAbilities setCanBuild(boolean canBuild) {
        this.canBuild = canBuild;
        return this;
    }

    public boolean canFly() {
        return this.canFly;
    }

    public PlayerAbilities setCanFly(boolean canFly) {
        this.canFly = canFly;
        return this;
    }

    public boolean canInstaBuild() {
        return this.canInstaBuild;
    }

    public PlayerAbilities setCanInstaBuild(boolean instaBuild) {
        this.canInstaBuild = instaBuild;
        return this;
    }

    public boolean canMine(boolean canMine) {
        return this.canMine;
    }

    public PlayerAbilities setCanMine(boolean canMine) {
        this.canMine = canMine;
        return this;
    }

    public boolean canOpenContainers() {
        return this.canOpenContainers;
    }

    public PlayerAbilities setCanOpenContainers(boolean canOpenContainers) {
        this.canOpenContainers = canOpenContainers;
        return this;
    }

    public boolean canTeleport() {
        return this.canTeleport;
    }

    public PlayerAbilities setCanTeleport(boolean canTeleport) {
        this.canTeleport = canTeleport;
        return this;
    }

    public boolean canUseDoorsAndSwitches() {
        return this.canUseDoorsAndSwitches;
    }

    public PlayerAbilities setCanUseDoorsAndSwitches(boolean canUseDoorsAndSwitches) {
        this.canUseDoorsAndSwitches = canUseDoorsAndSwitches;
        return this;
    }

    public float getFlySpeed() {
        return this.flySpeed;
    }

    public PlayerAbilities setFlySpeed(float flySpeed) {
        this.flySpeed = flySpeed;
        return this;
    }

    public boolean isFlying() {
        return this.isFlying;
    }

    public PlayerAbilities setIsFlying(boolean isFlying) {
        this.isFlying = isFlying;
        return this;
    }

    public boolean isInvulnerable() {
        return this.isInvulnerable;
    }

    public PlayerAbilities setIsInvulnerable(boolean invulnerable) {
        this.isInvulnerable = invulnerable;
        return this;
    }

    public boolean isOp() {
        return this.isOp;
    }

    public PlayerAbilities setIsOp(boolean isOp) {
        this.isOp = isOp;
        return this;
    }

    public boolean isLightning() {
        return this.lightning;
    }

    public PlayerAbilities setIsLightning(boolean isLightning) {
        this.lightning = lightning;
        return this;
    }

    public int getPermissionLevel() {
        return this.permissionLevel;
    }

    public PlayerAbilities setPermissionLevel(int level) {
        this.permissionLevel = level;
        return this;
    }

    public int getPlayersPermissionLevel() {
        return this.playersPermissionLevel;
    }

    public PlayerAbilities setPlayersPermissionLevel(int level) {
        this.playersPermissionLevel = level;
        return this;
    }

    public float getWalkSpeed() {
        return this.walkSpeed;
    }

    public PlayerAbilities setWalkSpeed(float walkSpeed) {
        this.walkSpeed = walkSpeed;
        return this;
    }

}
