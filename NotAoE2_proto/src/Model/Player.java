package Model;

import Model.Building.Barracks;
import Model.Building.Buildings;
import Model.Building.Farm;
import Model.Building.FarmHouse;
import Model.Building.MainBuilding;
import Model.Building.Market;
import Model.Building.OutPost;
import Model.Building.Stable;
import Model.Building.Tavern;
import Model.Building.Wall;
import Model.Unit.Archer;
import Model.Unit.Builder;
import Model.Unit.Destroyer;
import Model.Unit.Entity;
import Model.Unit.Farmer;
import Model.Unit.Hero;
import Model.Unit.Knight;
import Model.Unit.Lumberjack;
import Model.Unit.Miner;
import Model.Unit.Warrior;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    
    static int unitNumber = 0;
    static int buildingNumber = 0;
    private List<Integer> deletedUnitNumber;
    private List<Integer> deletedBuildingNumber;
    
    private int wheat;
    private int wood;
    private int gold;
    private int actionPoint = 15;
    private int score = 0;
    private int population;
    private int maxPopCount = 15;
    private List<Entity> units;
    private List<Buildings> buildings;
    private int numberOfHeroes = 0;
    private int maxActionPoint = 15;

    
    public Player(){
        units = new ArrayList<>();
        buildings = new ArrayList<>();
        deletedUnitNumber = new ArrayList<>();
        deletedBuildingNumber = new ArrayList<>();
    }
    


    public int getWheat() {
        return wheat;
    }

    public void setWheat(int wheat) {
        this.wheat = wheat;
    }

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getActionPoint() {
        return actionPoint;
    }

    public void setActionPoint(int actionPoint) {
        this.actionPoint = actionPoint;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    public void addUnit(int type, int posX, int posY){
        switch (type){
            case 0:
                units.add(new Builder(posX, posY, newUnitIndex('u')));
            break;
            case 1:
                units.add(new Farmer(posX, posY, newUnitIndex('u')));
            break;
            case 2:
                units.add(new Lumberjack(posX, posY, newUnitIndex('u')));
            break;
            case 3:
                units.add(new Miner(posX, posY, newUnitIndex('u')));
            break;
            case 4:
                    units.add(new Warrior(posX, posY, newUnitIndex('u')));
            break;
            case 5:
                    units.add(new Archer(posX, posY, newUnitIndex('u')));
            break;
            case 6:
                    units.add(new Destroyer(posX, posY, newUnitIndex('u')));
            break;
            case 7:
                    units.add(new Knight(posX, posY, newUnitIndex('u')));
            break;
            case 8:
                    units.add(new Hero(posX, posY, newUnitIndex('u')));
            break;

        }
        
    }
    
    
    public void addBuilding(int type, int posX, int posY){
        switch(type){
            case 1:
                buildings.add(new MainBuilding(posX, posY, newUnitIndex('b')));
            break;
            case 2:
                buildings.add(new FarmHouse(posX, posY, newUnitIndex('b')));
            break;
            case 3:
                buildings.add(new Barracks(posX, posY, newUnitIndex('b')));
            break;
            case 4:
                buildings.add(new Stable(posX, posY, newUnitIndex('b')));
            break;
            case 5:
                buildings.add(new Market(posX, posY, newUnitIndex('b')));
            break;
            case 6:
                buildings.add(new OutPost(posX, posY, newUnitIndex('b')));
            break;
            case 7:
                buildings.add(new Tavern(posX, posY, newUnitIndex('b')));
            break;
            case 8:
                buildings.add(new Wall(posX, posY, newUnitIndex('b')));
            break;
        }
    }
    
    public void removeUnit(int row, int column) {
        
        
        for(int i=0; i<units.size(); ++i){
            if(units.get(i).getPosX() == row && units.get(i).getPosY() == column) {
                deletedUnitNumber.add(units.get(i).getUnitIndex());
                units.remove(units.get(i));
            }
        }
    }
    
    public void removeBuilding(int row, int column) {
        
        
        for(int i=0; i<buildings.size(); ++i){
            if(buildings.get(i).getPosX() == row && buildings.get(i).getPosY() == column) {
                deletedBuildingNumber.add(buildings.get(i).getBuildingIndex());
                buildings.remove(buildings.get(i));
            }
        }
    }
    
    
    public List<Integer> getDeletedUnitNumber() {
        return deletedUnitNumber;
    }

    public List<Entity> getUnits() {
        return units;
    }
    
    public List<Buildings> getBuildings(){
        return buildings;
    }

    public int newUnitIndex (char c) {
       int nUI;
       switch(c) {
        case 'u' :   
            if(deletedUnitNumber.isEmpty()) {
             nUI = units.size()+1;
                return nUI;
                } else {
                nUI=Collections.min(deletedUnitNumber);
                return nUI;
        }
        case 'b':
            if(deletedBuildingNumber.isEmpty()) {
                nUI = buildings.size()+1;
                return nUI;
            } else {
                nUI = Collections.min(deletedBuildingNumber);
                return nUI;
            }
    }
        return 5000;
    }
    
    
    
    public int getArrayIndexOfUnit(int row, int column) {
        for (int i=0; i<units.size(); ++i){
            if (units.get(i).getPosX() == row && units.get(i).getPosY() == column) {
                return i;
                
            }
        }
        return 5;
    }

    public static int getUnitNumber() {
        return unitNumber;
    }

    public static void setUnitNumber(int unitNumber) {
        Player.unitNumber = unitNumber;
    }

    public List<Integer> getDeletedBuildingNumber() {
        return deletedBuildingNumber;
    }

    public void setDeletedBuildingNumber(List<Integer> deletedBuildingNumber) {
        this.deletedBuildingNumber = deletedBuildingNumber;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setBuildings(List<Buildings> buildings) {
        this.buildings = buildings;
    }

    public int getMaxPopCount() {
        return maxPopCount;
    }

    public void setMaxPopCount(int maxPopCount) {
        this.maxPopCount = maxPopCount;
    }
    
    
    
    public int getArrayIndexOfBuilding(int row, int column){
        for(int i = 0; i < buildings.size(); i++){
            if(buildings.get(i).getPosX() == row && buildings.get(i).getPosY() == column){
                return i;
            }
        }
        return 5;
    }

    public static int getBuildingNumber() {
        return buildingNumber;
    }

    public static void setBuildingNumber(int buildingNumber) {
        Player.buildingNumber = buildingNumber;
    }

    public int getNumberOfHeroes() {
        return numberOfHeroes;
    }

    public void setNumberOfHeroes(int numberOfHeroes) {
        this.numberOfHeroes = numberOfHeroes;
    }

    public int getMaxActionPoint() {
        return maxActionPoint;
    }

    public void setMaxActionPoint(int maxActionPoint) {
        this.maxActionPoint = maxActionPoint;
    }
    
    
}
