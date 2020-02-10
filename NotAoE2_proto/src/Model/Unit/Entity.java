package Model.Unit;

public abstract class Entity {
    protected int posX;
    protected int posY;
    protected int unitIndex;
    
    protected int type;
    protected int health;
    protected int power;
    protected int defence;
    
    protected int level;
    
    protected int wheatCost;
    protected int goldCost;
    protected int actionPointUsage;
    protected int popCount;
    protected int range;
    protected boolean alreadyAttacked = false;
    
    
    
    public Entity(){
        
    }
    
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getUnitIndex() {
        return unitIndex;
    }

    public void setUnitIndex(int unitIndex) {
        this.unitIndex = unitIndex;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getWheatCost() {
        return wheatCost;
    }

    public void setWheatCost(int wheatCost) {
        this.wheatCost = wheatCost;
    }

    public int getGoldCost() {
        return goldCost;
    }

    public void setGoldCost(int goldCost) {
        this.goldCost = goldCost;
    }

    public int getActionPointUsage() {
        return actionPointUsage;
    }

    public void setActionPointUsage(int actionPointUsage) {
        this.actionPointUsage = actionPointUsage;
    }

    public int getPopCount() {
        return popCount;
    }

    public void setPopCount(int popCount) {
        this.popCount = popCount;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public boolean isAlreadyAttacked() {
        return alreadyAttacked;
    }

    public void setAlreadyAttacked(boolean alreadyAttacked) {
        this.alreadyAttacked = alreadyAttacked;
    }
    
}
