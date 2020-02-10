package Model.Unit;

public class Lumberjack extends Entity{
    
    public Lumberjack (int posX, int posY, int unitIndex) {
       this.posX = posX;
       this.posY = posY;
       this.unitIndex = unitIndex;
       
       this.type = 2;
       this.health = 60;
       this.power = 5;
       this.defence = 0;
       
       this.wheatCost = 100;
       this.goldCost = 0;
       this.actionPointUsage = 2;
       this.popCount = 1;
       this.range = 1;
    }    
}
