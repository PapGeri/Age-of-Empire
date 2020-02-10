package Model.Building;

public class MainBuilding extends Buildings{
    public MainBuilding (int PosX, int PosY, int buildingIndex) {
         this.posX = PosX;
         this.posY = PosY;
         this.buildingIndex = buildingIndex;
         
         this.type = 1;
         this.health = 300;
         this.score = 15;
         this.woodCost = 300;
         this.goldCost = 200;
    }
}
