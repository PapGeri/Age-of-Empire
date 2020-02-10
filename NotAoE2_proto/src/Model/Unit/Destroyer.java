package Model.Unit;

public class Destroyer extends Entity{
    public Destroyer (int posX, int posY, int unitIndex) {
       this.posX = posX;
       this.posY = posY;
       this.unitIndex = unitIndex;
       
       this.type = 6;
       this.health = 100;
       this.power = 25;
       this.defence = 1;
       
       this.wheatCost = 120;
       this.goldCost = 20;
       this.actionPointUsage = 2;
       this.popCount = 2;
       this.range = 1;
    }    
}
