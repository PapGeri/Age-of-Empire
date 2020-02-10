package Model.Unit;

public class Archer extends Entity{
    public Archer (int posX, int posY, int unitIndex) {
       this.posX = posX;
       this.posY = posY;
       this.unitIndex = unitIndex;
       
       this.type = 5;
       this.health = 40;
       this.power = 15;
       this.defence = 0;
       
       this.wheatCost = 70;
       this.goldCost = 30;
       this.actionPointUsage = 2;
       this.popCount = 1;
       this.range = 3;

    }
}
