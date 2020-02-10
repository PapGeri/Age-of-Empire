package Model.Building;

public class Market extends Buildings{
    public Market (int PosX, int PosY, int buildingIndex) {
         this.posX = PosX;
         this.posY = PosY;
         this.buildingIndex = buildingIndex;
         
         this.type = 5;
         this.health = 100;
         this.score = 1;
         this.woodCost = 80;
         this.goldCost = 50;
    }
}
