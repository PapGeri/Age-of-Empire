package Model.Unit;

public class Hero extends Entity{
    public Hero (int posX, int posY, int unitIndex) {
       this.posX = posX;
       this.posY = posY;
       this.unitIndex = unitIndex;
       
       this.type = 8;
       this.health = 300;
       this.power = 35;
       this.defence = 5;
       
       this.wheatCost = 350;
       this.goldCost = 250;
       this.actionPointUsage = 4;
       this.popCount = 5;
       this.range = 1;
    }    
}
