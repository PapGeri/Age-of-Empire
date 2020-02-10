package Model.Unit;

public class Knight extends Entity{
    public Knight (int posX, int posY, int unitIndex) {
       this.posX = posX;
       this.posY = posY;
       this.unitIndex = unitIndex;
       
       this.type = 7;
       this.health = 150;
       this.power = 20;
       this.defence = 3;
       
       this.wheatCost = 60;
       this.goldCost = 100;
       this.actionPointUsage = 1;
       this.popCount = 2;
       this.range = 1;
    }    
}
