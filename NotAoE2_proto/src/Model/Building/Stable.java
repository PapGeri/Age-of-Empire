package Model.Building;

public class Stable extends Buildings{
    public Stable(int PosX, int PosY, int buildingIndex){
         this.posX = PosX;
         this.posY = PosY;
         this.buildingIndex = buildingIndex;
         
         this.type = 4;
         this.health = 200;
         this.score = 5;
         this.woodCost = 150;
         this.goldCost = 60;
    }
}
