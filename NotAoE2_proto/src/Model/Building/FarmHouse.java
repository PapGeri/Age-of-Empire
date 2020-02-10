package Model.Building;

public class FarmHouse extends Buildings{
    public FarmHouse(int PosX, int PosY, int buildingIndex) {
         this.posX = PosX;
         this.posY = PosY;
         this.buildingIndex = buildingIndex;
         
         this.type = 2;
         this.health = 150;
         this.score = 5;
         this.woodCost = 150;
         this.goldCost = 0;
    }
}
