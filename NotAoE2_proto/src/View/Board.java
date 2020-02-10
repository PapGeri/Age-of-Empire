package View;

import Model.BoardData;
import Model.Item;
import Model.Player;
import Model.TableData;
import Model.Deployed;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class Board extends BaseWindow implements KeyListener {

    private JPanel mainPanel;
    private JLabel gameStatLabel;
    private final int mapNum;
    private int width, height;
    private Stats unitStat;
    private Stats buildingStat;
    private Stats createBuildings;

    private int prevx = 0;
    private int prevy = 0;
    private boolean selected = false;
    private int startx = 0;
    private int starty = 0;
    private int zoom = 8;
    private final int zoomArray[] = {8, 10, 16, 20};
    private int zoomArrayIndex = 0;

    private final int amount;
    private BoardData boardData;
    private Item origTable[][];
    private TableData table[][];
    private Player[] player = new Player[2];
    private List<Deployed> deployed;
    private HashMap<Tile, Integer> tiles;

    private FieldButton fieldButtons[][];
    private int actualPlayer = 0;

    public Board(int amount, int mapNum) {
        deployed = new ArrayList<>();
        this.mapNum = mapNum;
        this.amount = amount;
        this.boardData = new BoardData();
        requestFocusInWindow();
        addKeyListener(this);
        loadGame();
        initGame();
        getImages();
        this.setLayout(new BorderLayout());
        loadBoard(starty, startx);
        setSize(800, 800);
        setLocationRelativeTo(null);
        makeGameStatLabel();
        addEndTurnButton();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        unSelect();
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (startx < width - zoom) {
                removeButtonImage(startx, starty);
                remove(mainPanel);
                startx++;
                loadBoard(startx, starty);
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (startx > 0) {
                removeButtonImage(startx, starty);
                remove(mainPanel);
                startx--;
                loadBoard(startx, starty);
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (starty > 0) {
                removeButtonImage(startx, starty);
                remove(mainPanel);
                starty--;
                loadBoard(startx, starty);
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (starty < height - zoom) {
                removeButtonImage(startx, starty);
                remove(mainPanel);
                starty++;
                loadBoard(startx, starty);
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (zoomArrayIndex > 0) {
                removeButtonImage(startx, starty);
                remove(mainPanel);
                zoom(false);
                loadBoard(startx, starty);
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_W) {
            if (zoomArrayIndex < 3 /*&& zoom < width*/) {
                if (startx <= width - zoomArray[zoomArrayIndex + 1] && starty <= height - zoomArray[zoomArrayIndex + 1]) {
                    removeButtonImage(startx, starty);
                    remove(mainPanel);
                    zoom(true);
                    loadBoard(startx, starty);
                } else if (width - zoomArray[zoomArrayIndex + 1] < startx && starty <= height - zoomArray[zoomArrayIndex + 1]) {
                    removeButtonImage(startx, starty);
                    remove(mainPanel);
                    zoom(true);
                    startx -= (startx - (width - zoomArray[zoomArrayIndex]));
                    if (startx < 0) {
                        startx = 0;
                    }
                    loadBoard(startx, starty);
                } else if (height - zoomArray[zoomArrayIndex + 1] < starty && startx <= width - zoomArray[zoomArrayIndex + 1]) {
                    removeButtonImage(startx, starty);
                    remove(mainPanel);
                    zoom(true);
                    starty -= (starty - (height - zoomArray[zoomArrayIndex]));
                    if (starty < 0) {
                        starty = 0;
                    }
                    loadBoard(startx, starty);
                } else if (height - zoomArray[zoomArrayIndex + 1] < starty && width - zoomArray[zoomArrayIndex + 1] < startx) {
                    removeButtonImage(startx, starty);
                    remove(mainPanel);
                    zoom(true);
                    startx -= (startx - (width - zoomArray[zoomArrayIndex]));
                    if (startx < 0) {
                        startx = 0;
                    }
                    starty -= (starty - (height - zoomArray[zoomArrayIndex]));
                    if (starty < 0) {
                        starty = 0;
                    }
                    loadBoard(startx, starty);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void zoom(boolean b) {
        if (b) {
            if (zoomArrayIndex < 3) {
                zoomArrayIndex++;
                zoom = zoomArray[zoomArrayIndex];
                boardData.zoomImages(true);
            }
        } else {
            if (zoomArrayIndex > 0) {
                zoomArrayIndex--;
                zoom = zoomArray[zoomArrayIndex];
                boardData.zoomImages(false);
            }
        }
    }

    private void loadGame() {
        ArrayList<String> lines = new ArrayList<>();
        int maxWidth = 0;
        try {
            InputStream is = getClass().getResourceAsStream("../maps/" + mapNum + ".txt");
            Scanner s = new Scanner(is);
            while (s.hasNextLine()) {
                String line = s.nextLine();
                lines.add(line);
                if (line.length() > maxWidth) {
                    maxWidth = line.length();
                }
            }
            is.close();
        } catch (IOException ex) {
        }
        width = maxWidth;
        height = lines.size();
        int less;
        if (width <= height) {
            less = width;
        } else {
            less = height;
        }
        if (zoom > less) {
            setZoom(less);
        }
        origTable = new Item[height][width];

        if (width > 0 && height > 0) {
            table = new TableData[height][width];
            for (int i = 0; i < height; ++i) {
                String line = lines.get(i);
                for (int j = 0; j < width; ++j) {
                    char c = line.charAt(j);
                    switch (c) {
                        case 'm':
                            origTable[i][j] = Item.MINE;
                            break;
                        case 'g':
                            origTable[i][j] = Item.GRASS;
                            break;
                        case 't':
                            origTable[i][j] = Item.TREE;
                            break;
                        case 'h':
                            origTable[i][j] = Item.HILL;
                            break;
                        case 'w':
                            origTable[i][j] = Item.WATER;
                            break;
                        default:
                            origTable[i][j] = Item.GRASS;
                            break;
                    }
                }
            }
        }
    }

    private void loadBoard(int x, int y) {

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(zoom, zoom));
        fieldButtons = new FieldButton[height][width];

        for (int row = y; row < y + zoom; ++row) {
            for (int column = x; column < x + zoom; ++column) {
                switch (table[row][column].getItem()) {
                    case GRASS:
                        addButton(row, column, 0, 'g');
                        break;
                    case TREE:
                        addButton(row, column, 1, 't');
                        break;
                    case HILL:
                        addButton(row, column, 2, 'h');
                        break;
                    case WATER:
                        addButton(row, column, 3, 'w');
                        break;
                    case MINE:
                        addButton(row, column, 4, 'm');
                        break;
                    case BUILDER:
                       if(origTable[row][column] == Item.GRASS){
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 5, 'e');
                        } else {
                            addButton(row, column, 6, 'e');
                        }
                       } else {
                           if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 7, 'e');
                        } else {
                            addButton(row, column, 8, 'e');
                        }
                       }
                       break;
                    case FARMER:
                     if(origTable[row][column] == Item.GRASS){
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 9, 'e');
                        } else {
                            addButton(row, column, 10, 'e');
                        }
                       } else {
                           if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 11, 'e');
                        } else {
                            addButton(row, column, 12, 'e');
                        }
                       }
                        break;
                    case LUMBERJACK:
                       if(origTable[row][column] == Item.GRASS){
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 13, 'e');
                        } else {
                            addButton(row, column, 14, 'e');
                        }
                       } else if (origTable[row][column] == Item.FARM) {
                           if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 15, 'e');
                        } else {
                            addButton(row, column, 16, 'e');
                        }
                       } else {
                           if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 58, 'e');
                        } else {
                            addButton(row, column, 59, 'e');
                        }
                       }
                        break;
                    case MINER:
                        if(origTable[row][column] == Item.GRASS){
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 17, 'e');
                        } else {
                            addButton(row, column, 18, 'e');
                        }
                       } else if (origTable[row][column] == Item.FARM){
                           if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 19, 'e');
                        } else {
                            addButton(row, column, 20, 'e');
                        }
                       } else {
                           if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 60, 'e');
                        } else {
                            addButton(row, column, 61, 'e');
                        }
                       }
                        break;
                    case WARRIOR:
                        if(origTable[row][column] == Item.GRASS){
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 21, 'e');
                        } else {
                            addButton(row, column, 22, 'e');
                        }
                       } else {
                           if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 23, 'e');
                        } else {
                            addButton(row, column, 24, 'e');
                        }
                       }
                        break;
                    case ARCHER:
                       if(origTable[row][column] == Item.GRASS){
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 25, 'e');
                        } else {
                            addButton(row, column, 26, 'e');
                        }
                       } else {
                           if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 27, 'e');
                        } else {
                            addButton(row, column, 28, 'e');
                        }
                       }
                        break;
                    case DESTROYER:
                        if(origTable[row][column] == Item.GRASS){
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 29, 'e');
                        } else {
                            addButton(row, column, 30, 'e');
                        }
                       } else {
                           if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 31, 'e');
                        } else {
                            addButton(row, column, 32, 'e');
                        }
                       }
                        break;
                    case KNIGHT:
                        if(origTable[row][column] == Item.GRASS){
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 33, 'e');
                        } else {
                            addButton(row, column, 34, 'e');
                        }
                       } else {
                           if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 35, 'e');
                        } else {
                            addButton(row, column, 36, 'e');
                        }
                       }
                        break;
                    case HERO:
                        if(origTable[row][column] == Item.GRASS){
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 37, 'e');
                        } else {
                            addButton(row, column, 38, 'e');
                        }
                       } else {
                           if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 39, 'e');
                        } else {
                            addButton(row, column, 40, 'e');
                        }
                       }
                        break;
                    case FARM:
                        addButton(row, column, 41, 'f');
                        break;
                    case MAINB:
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 42, 'b');
                        } else {
                            addButton(row, column, 43, 'b');
                        }
                        break;
                    case FARMH:
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 44, 'b');
                        } else {
                            addButton(row, column, 45, 'b');
                        }
                        break;
                    case BARRACKS:
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 46, 'b');
                        } else {
                            addButton(row, column, 47, 'b');
                        }
                        break;
                    case STABLE:
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 48, 'b');
                        } else {
                            addButton(row, column, 49, 'b');
                        }
                        break;
                    case MARKET:
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 50, 'b');
                        } else {
                            addButton(row, column, 51, 'b');
                        }
                        break;
                    case OUTPOST:
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 52, 'b');
                        } else {
                            addButton(row, column, 53, 'b');
                        }
                        break;
                    case TAVERN:
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 54, 'b');
                        } else {
                            addButton(row, column, 55, 'b');
                        }
                        break;
                    case WALL:
                        if (deployedSearch(row, column) == 0) {
                            addButton(row, column, 56, 'b');
                        } else {
                            addButton(row, column, 57, 'b');
                        }
                        break;
                    default:
                        addButton(row, column, 0, 'n');
                        break;
                }

            }
        }
        add(mainPanel, BorderLayout.CENTER);
    }

    private void addButton(int row, int column, int fieldtype, char c) {
        fieldButtons[row][column] = new FieldButton(row, column, fieldtype, false);
        fieldButtons[row][column].setFocusable(false);
        fieldButtons[row][column].setIcon(
                new ImageIcon(boardData.getImages(
                        fieldButtons[row][column].getImgID())));
        fieldButtons[row][column].setBorder(BorderFactory.createLineBorder(Color.green));
        fieldButtons[row][column].addActionListener((ActionEvent e) -> {
            switch (c) {
                case 'f':
                    if (selected == true) {
                        moveUnit(row, column);
                    }
                    //unSelect();
                    break;
                case 'w':
                    unSelect();
                    break;
                case 'm':
                    if (selected == true && table[prevx][prevy].getItem() == Item.MINER) {
                        moveUnit(row, column);
                    }
                    unSelect();
                    break;
                case 'h':
                    unSelect();
                    break;
                case 'g':
                    if (selected && table[prevx][prevy].getItem() == Item.BUILDER && Math.abs(prevx - row) <= 1 && Math.abs(prevy - column) <= 1) {
                        createBuilding(row, column);
                        selected = false;
                    }
                    if (selected && table[prevx][prevy].getItem() == Item.FARMER && Math.abs(prevx - row) <= 1 && Math.abs(prevy - column) <= 1) {
                        createBuilding(row, column);
                        selected = false;
                    }
                    if (selected == true) {
                        moveUnit(row, column);
                    }
                    unSelect();
                    break;
                case 't':
                    if (selected == true && table[prevx][prevy].getItem() == Item.LUMBERJACK) {
                        moveUnit(row, column);
                    }
                    unSelect();
                    break;
     case 'e': 
            if (unitStat == null) {
                        viewUnitStats(row, column);
                    }

                    if (selected == true && prevx == row && prevy == column) {
                        unSelect();
                        setTilesAvelable();
                        break;
                    }
                    if (selected == true && deployedSearch(row, column) != actualPlayer
                            && player[actualPlayer].getUnits().get(player[actualPlayer].getArrayIndexOfUnit(prevx, prevy)).getActionPointUsage() <= player[actualPlayer].getActionPoint()) {
                        attackUnit(row, column,0);
                        refreshGameStatLabel();
                    } else {
                        unitStat.dispose();
                        viewUnitStats(row, column);
                        selected = select(row, column);
                        if(selected){
                            setRange(row,column);
                        }
                    }
                    break;
                case 'b': //building
                    if (buildingStat == null ) {
                        viewBuildingStats(row, column);
                    } 
                    if(selected == true && deployedSearch(row,column) != actualPlayer &&
                                player[actualPlayer].getUnits().get(player[actualPlayer].getArrayIndexOfUnit(prevx, prevy)).getActionPointUsage() <= player[actualPlayer].getActionPoint()){
                            attackUnit(row, column, 1);
                            refreshGameStatLabel();
                        } else {
                        buildingStat.dispose();
                        viewBuildingStats(row, column);
                        }
                    break;
            }
        });
        mainPanel.add(fieldButtons[row][column]);
    }

    private void removeButtonImage(int x, int y) {
        for (int row = y; row < y + zoom; ++row) {
            for (int column = x; column < x + zoom; ++column) {
                fieldButtons[row][column].setIcon(null);
            }
        }
    }

    public void initGame() {
        table = new TableData[height][width];
        for (int x = 0; x < height; ++x) {
            for (int y = 0; y < width; ++y) {
                table[x][y] = new TableData();
                table[x][y].setItem(origTable[x][y]);
            }
        }
        
        for (int i = 0; i < 2; ++i) {
            player[i] = new Player();
            player[i].setWheat(0);
            player[i].setWood(0);
            player[i].setGold(0);
        }
        makeMainBuilding();
        for (int i = 0; i < 2; ++i) {
            player[i].setWheat(amount);
            player[i].setWood(amount);
            player[i].setGold(amount);
        }
    }

    private void getImages() {
        Image images[] = new Image[62];
        for (int i = 0; i <= 61; ++i) {
            images[i] = new ImageIcon("data/images/" + i + ".png").getImage();
        }
        boardData.setImages(images);
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    private void makeGameStatLabel() {
        gameStatLabel = new JLabel();
        add(gameStatLabel, BorderLayout.SOUTH);
        refreshGameStatLabel();
    }

    private void addEndTurnButton() {
        JButton endTurn = new JButton("End Turn");
        add(endTurn, BorderLayout.NORTH);
        endTurn.setFocusable(false);
        endTurn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                unSelect();
                setTilesAvelable();
                gatherResources();
                player[actualPlayer].setActionPoint(player[actualPlayer].getMaxActionPoint());
                for (int i = 0; i<player[actualPlayer].getUnits().size(); ++i){
                player[actualPlayer].getUnits().get(i).setAlreadyAttacked(false);
                     }
                changeActualPlayer();
                refreshGameStatLabel();
            }
        });
    }

    public void changeActualPlayer() {
        if (actualPlayer == 0) {
            actualPlayer++;
        } else {
            actualPlayer--;
        }
    }

    public boolean select(int row, int column) {
        int c = actualPlayer;
        if (deployedSearch(row, column) == actualPlayer) {
            prevx = row;
            prevy = column;
            return true;
        } else {
            return false;
        }

    }

    private void refreshGameStatLabel() {

        String s = "The actual player is:  " + (actualPlayer + 1)
                + " | Action points: " + player[actualPlayer].getActionPoint()
                + " | Score: " + player[actualPlayer].getScore()
                + " | Wheat: " + player[actualPlayer].getWheat()
                + " | Wood: " + player[actualPlayer].getWood()
                + " | Gold: " + player[actualPlayer].getGold()
                + " | Population: " + player[actualPlayer].getPopulation()
                + "/" + player[actualPlayer].getMaxPopCount();
        gameStatLabel.setText(s);
    }

    public int deployedSearch(int row, int column) {
        for (int i = 0; i < deployed.size(); ++i) {
            if (deployed.get(i).getPosX() == row && deployed.get(i).getPosY() == column) {
                return deployed.get(i).getActualPlayer();
            }
        }
        return 10000;
    }

    public void moveUnit(int row, int column) {
        table[row][column].setItem(table[prevx][prevy].getItem());
        table[prevx][prevy].setItem(origTable[prevx][prevy]);
        player[actualPlayer].getUnits().get(player[actualPlayer].getArrayIndexOfUnit(prevx, prevy)).setPosX(row);
        player[actualPlayer].getUnits().get(player[actualPlayer].getArrayIndexOfUnit(row, prevy)).setPosY(column);
        deployed.get(getArrayIndexOfDeployed(prevx, prevy)).setPosX(row);
        deployed.get(getArrayIndexOfDeployed(row, prevy)).setPosY(column);
        player[actualPlayer].setActionPoint(player[actualPlayer]
            .getActionPoint()-(tiles.get(new Tile(row,column))*
            player[actualPlayer].getUnits().get(player[actualPlayer]
            .getArrayIndexOfUnit(row, column))
            .getActionPointUsage()));
        tiles = null;
        unSelect();
        refreshGameStatLabel();
        refreshGame();
    }

    public int getArrayIndexOfDeployed(int row, int column) {
        int index = 0;
        for (int i = 0; i < deployed.size(); ++i) {
            if (deployed.get(i).getPosX() == row && deployed.get(i).getPosY() == column) {
                index = i;
                return index;
            }
        }
        return index;
    }

    public void attackUnit(int row, int column, int type) {
        int notActual = deployedSearch(row,column);
        switch (type){
            case 0:
            if (Math.abs(deployed.get(getArrayIndexOfDeployed(prevx, prevy)).getPosX() - deployed.get(getArrayIndexOfDeployed(row, column)).getPosX())
                <= player[actualPlayer].getUnits().get(player[actualPlayer].getArrayIndexOfUnit(prevx, prevy)).getRange()
                && Math.abs(deployed.get(getArrayIndexOfDeployed(prevx, prevy)).getPosY() - deployed.get(getArrayIndexOfDeployed(row, column)).getPosY())
                <= player[actualPlayer].getUnits().get(player[actualPlayer].getArrayIndexOfUnit(prevx, prevy)).getRange() &&
                    player[actualPlayer].getUnits().get(player[actualPlayer].getArrayIndexOfUnit(prevx, prevy)).isAlreadyAttacked() == false) {
            int hp = getCurrentUnitHealth(row, column);
            hp -= (getCurrentUnitAttack(prevx, prevy) - getCurrentUnitDefense(row, column));
            player[notActual].getUnits().get(player[notActual].getArrayIndexOfUnit(row, column)).setHealth(hp);
            player[actualPlayer].setActionPoint(player[actualPlayer].getActionPoint() - player[actualPlayer].getUnits().get(player[actualPlayer].getArrayIndexOfUnit(prevx, prevy)).getActionPointUsage());
            player[actualPlayer].getUnits().get(player[actualPlayer].getArrayIndexOfUnit(prevx, prevy)).setAlreadyAttacked(true);
            unSelect();
            if (getCurrentUnitHealth(row, column) <= 0) {
                player[notActual].setScore(player[notActual].getScore()-player[notActual].getUnits().get(player[notActual].getArrayIndexOfUnit(row, column)).getPopCount());
                if(player[notActual].getScore() < 15) {
                    victoryCondition(actualPlayer);
                }                
                player[notActual].setPopulation(player[notActual].getPopulation()-player[notActual].getUnits().get(player[notActual].getArrayIndexOfUnit(row, column)).getPopCount());
                player[deployedSearch(row, column)].removeUnit(row, column);
                deployed.remove(deployed.get(getArrayIndexOfDeployed(row, column)));
                table[row][column].setItem(origTable[row][column]);
                refreshGame();
            }

        }
        break;
            case 1:
            if (Math.abs(deployed.get(getArrayIndexOfDeployed(prevx, prevy)).getPosX() - deployed.get(getArrayIndexOfDeployed(row, column)).getPosX())
                <= player[actualPlayer].getUnits().get(player[actualPlayer].getArrayIndexOfUnit(prevx, prevy)).getRange()
                && Math.abs(deployed.get(getArrayIndexOfDeployed(prevx, prevy)).getPosY() - deployed.get(getArrayIndexOfDeployed(row, column)).getPosY())
                <= player[actualPlayer].getUnits().get(player[actualPlayer].getArrayIndexOfUnit(prevx, prevy)).getRange() &&
                    player[actualPlayer].getUnits().get(player[actualPlayer].getArrayIndexOfUnit(prevx, prevy)).isAlreadyAttacked() == false) {
            int hp = getCurrentBuildingHealth(row, column);
            hp -= getCurrentUnitAttack(prevx, prevy);
            player[notActual].getBuildings().get(player[notActual].getArrayIndexOfBuilding(row, column)).setHealth(hp);
            player[actualPlayer].setActionPoint(player[actualPlayer].getActionPoint() - player[actualPlayer].getUnits().get(player[actualPlayer].getArrayIndexOfUnit(prevx, prevy)).getActionPointUsage());
            player[actualPlayer].getUnits().get(player[actualPlayer].getArrayIndexOfUnit(prevx, prevy)).setAlreadyAttacked(true);
            unSelect();
            if (getCurrentBuildingHealth(row, column) <= 0) {
                player[notActual].setScore(player[notActual].getScore()-player[notActual].getBuildings().get(player[notActual].getArrayIndexOfBuilding(row, column)).getScore());
                if(player[notActual].getScore() < 15) {
                    victoryCondition(actualPlayer);
                }
                if(table[row][column].getItem() == Item.FARMH){
                player[notActual].setMaxPopCount(player[notActual].getMaxPopCount()-10);
                    }
                if(table[row][column].getItem() == Item.TAVERN) {
                    player[notActual].setMaxActionPoint(player[notActual].getMaxActionPoint()-10);
                }
                player[deployedSearch(row, column)].removeBuilding(row, column);
                deployed.remove(deployed.get(getArrayIndexOfDeployed(row, column)));
                table[row][column].setItem(origTable[row][column]);
                refreshGame();
            }

        }
                break;
        }
    }

    public void gatherResources() {
        int wheat = 0;
        int wood = 0;
        int gold = 0;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (table[i][j].getItem() == Item.LUMBERJACK && origTable[i][j] == Item.TREE && deployedSearch(i, j) == actualPlayer) {
                    wood++;
                }
                if (table[i][j].getItem() == Item.MINER && origTable[i][j] == Item.MINE && deployedSearch(i, j) == actualPlayer) {
                    gold++;
                }
                if (table[i][j].getItem() == Item.FARMER && origTable[i][j] == Item.FARM && deployedSearch(i, j) == actualPlayer) {
                    wheat++;
                }
            }
        }
        player[actualPlayer].setWheat(player[actualPlayer].getWheat() + (wheat * 10));
        player[actualPlayer].setWood(player[actualPlayer].getWood() + (wood * 10));
        player[actualPlayer].setGold(player[actualPlayer].getGold() + (gold * 10));
    }

    public void createBuilding(int row, int column) {
        createBuildings = new Stats();
        JPanel createPanel = new JPanel();

        JButton buildMainBuilding = new JButton("Build MainBuilding (300w 200g)");
        JButton buildBarracks = new JButton("Build Barrack (200w)");
        JButton buildFarmHouse = new JButton("Build FarmHouse (150w)(+10rp)");
        JButton buildFarm = new JButton("Build Farm (50w)");
        JButton buildTavern = new JButton("Tavern (50w 100g)");
        JButton moveBuilder = new JButton("Move");
        JButton closeFrame = new JButton("Close");

        
        if (getCurrentUnitType(prevx, prevy) == Item.FARMER) {
            if (player[actualPlayer].getWood() > 50 && player[actualPlayer].getActionPoint() >= 2) {
                buildMainBuilding.setEnabled(false);
                buildBarracks.setEnabled(false);
                buildTavern.setEnabled(false);
                buildFarmHouse.setEnabled(false);
                buildFarm.setEnabled(true);
            }
        } else {
           buildFarm.setEnabled(false); 
        }
        
        buildMainBuilding.addActionListener((ActionEvent e) -> {
            makeBuilding(row, column, Item.MAINB, 1, actualPlayer);
            costOfRes(200, 0, 300);
            player[actualPlayer].setActionPoint(player[actualPlayer].getActionPoint()-3);
            refreshGameStatLabel();
            refreshGame();
            createBuildings.dispose();
        });

        buildFarmHouse.addActionListener((ActionEvent e) -> {
            player[actualPlayer].setMaxPopCount(player[actualPlayer].getMaxPopCount()+10);
            makeBuilding(row, column, Item.FARMH, 2, actualPlayer);
            costOfRes(0, 0, 150);
            player[actualPlayer].setActionPoint(player[actualPlayer].getActionPoint()-3);
            refreshGameStatLabel();
            refreshGame();
            createBuildings.dispose();
        });
        
        buildTavern.addActionListener((ActionEvent e) -> {
            makeBuilding(row, column, Item.TAVERN, 7, actualPlayer);
            costOfRes(100, 0, 50);
            player[actualPlayer].setActionPoint(player[actualPlayer].getActionPoint()-3);
            refreshGameStatLabel();
            refreshGame();
            createBuildings.dispose();
        });

        buildBarracks.addActionListener((ActionEvent e) -> {
            makeBuilding(row, column, Item.BARRACKS, 3, actualPlayer);
            costOfRes(0, 0, 200);
            player[actualPlayer].setActionPoint(player[actualPlayer].getActionPoint()-3);
            refreshGameStatLabel();
            refreshGame();
            createBuildings.dispose();
        });

        buildFarm.addActionListener((ActionEvent e) -> {
            origTable[row][column] = Item.FARM;
            table[row][column].setItem(Item.FARM);
            player[actualPlayer].setActionPoint(player[actualPlayer].getActionPoint()-2);
            costOfRes(0, 0, 50);
            refreshGameStatLabel();
            refreshGame();
            createBuildings.dispose();
        });

        moveBuilder.addActionListener((ActionEvent e) -> {
            moveUnit(row, column);
            createBuildings.dispose();
        });

        closeFrame.addActionListener((ActionEvent e) -> {
            createBuildings.dispose();
        });

        if (player[actualPlayer].getGold() < 200 || player[actualPlayer].getWood() < 300 || player[actualPlayer].getActionPoint() < 3) {
            buildMainBuilding.setEnabled(false);
        }
        
        if(player[actualPlayer].getGold() < 100 || player[actualPlayer].getWood() < 50 || player[actualPlayer].getActionPoint() < 3)  {
            buildTavern.setEnabled(false);
        }
        
        if (player[actualPlayer].getWood() < 150 || player[actualPlayer].getActionPoint() < 3) {
            buildFarmHouse.setEnabled(false);
        }

        if (player[actualPlayer].getWood() < 200 || player[actualPlayer].getActionPoint() < 3) {
            buildBarracks.setEnabled(false);
        }

        createPanel.add(buildMainBuilding);
        createPanel.add(buildBarracks);
        createPanel.add(buildTavern);
        createPanel.add(buildFarmHouse);
        createPanel.add(buildFarm);
        createPanel.add(moveBuilder);
        createPanel.add(closeFrame);
        createBuildings.add(createPanel);
        createBuildings.setLocation(370, 115);

    }

    public void viewBuildingStats(int row, int column) {

        buildingStat = new Stats();
        JPanel statPanel = new JPanel();
        statPanel.setLayout(new BoxLayout(statPanel, BoxLayout.PAGE_AXIS));
        Font font1 = new Font("SansSerif", Font.BOLD, 20);
        JTextArea area = new JTextArea(" ");
        switch(getCurrentUnitType(row,column)){
            case MAINB:
            area = new JTextArea(("Main Building")
                + "\n" + "Health Point: " + getCurrentBuildingHealth(row, column));
            area.setFont(font1);
            break;
            case FARMH:
            area = new JTextArea(("Farmhouse")
                + "\n" + "Health Point: " + getCurrentBuildingHealth(row, column));
            area.setFont(font1);
            break;
            case TAVERN:
            area = new JTextArea(("Tavern")
                + "\n" + "Health Point: " + getCurrentBuildingHealth(row, column));
            area.setFont(font1);
            break;
            case BARRACKS:
            area = new JTextArea(("Barracks")
                + "\n" + "Health Point: " + getCurrentBuildingHealth(row, column));
            area.setFont(font1);
            break;
        }
        JButton makeHero = new JButton("Make Hero (350wh 250g 5Pr");
        JButton makeBuilder = new JButton("Make Builder (120wh 20g 5Pr)");
        JButton makeDestroyer = new JButton("Make Destroyer (120wh 20g 2Pr");
        JButton makeWarrior = new JButton("Make Warrior (80wh 1Pr");
        JButton makeArcher = new JButton("Make Archer (70wh 30g 1Pr");
        JButton makeFarmer = new JButton("Make Farmer (100wh 1Pr)");
        JButton makeMiner = new JButton("Make Miner (100wh 1Pr)");
        JButton makeLumberjack = new JButton("Make Lumberjack (100wh 1Pr)");
        JButton closeFrame = new JButton("Close");
        makeHero.setEnabled(false);
        makeBuilder.setEnabled(false);
        makeDestroyer.setEnabled(false);
        makeWarrior.setEnabled(false);
        makeArcher.setEnabled(false);
        makeFarmer.setEnabled(false);
        makeMiner.setEnabled(false);
        makeLumberjack.setEnabled(false);
        makeMiner.setEnabled(false);        
        if(deployedSearch(row,column) != actualPlayer){
         } else {
        if (getCurrentUnitType(row, column) == Item.MAINB) {
            if(player[actualPlayer].getNumberOfHeroes() < 1 && player[actualPlayer].getWheat() >= 350 && player[actualPlayer].getGold() >= 250 && (player[actualPlayer].getMaxPopCount()-player[actualPlayer].getPopulation()) >= 5){
            makeHero.setEnabled(true);
            }
            makeHero.addActionListener((ActionEvent e) -> {
                if(player[actualPlayer].getNumberOfHeroes() < 1 && player[actualPlayer].getWheat() >= 350 && player[actualPlayer].getGold() >= 250 && (player[actualPlayer].getMaxPopCount()-player[actualPlayer].getPopulation()) >= 5){
                    makeUnit(row, column, Item.HERO, 8, actualPlayer);
                    costOfRes(250, 350, 0);
                    player[actualPlayer].setNumberOfHeroes(1);
                    refreshGameStatLabel();
                    refreshGame();
                } else {
                    makeHero.setEnabled(false);
                }
               
            });
        if(player[actualPlayer].getWheat() >= 120 && player[actualPlayer].getGold() >= 20 && (player[actualPlayer].getMaxPopCount()-player[actualPlayer].getPopulation()) >= 5) {
            makeBuilder.setEnabled(true);
        }
        makeBuilder.addActionListener((ActionEvent e) -> {
            if(player[actualPlayer].getWheat() >= 120 && player[actualPlayer].getGold() >= 20 && (player[actualPlayer].getMaxPopCount()-player[actualPlayer].getPopulation()) >= 5) {
                    makeUnit(row, column, Item.BUILDER, 0, actualPlayer);
                    costOfRes(20, 120, 0);
                    refreshGameStatLabel();
                    refreshGame();
            }else {
                    makeBuilder.setEnabled(false);
                }
             });
        } 

        if (getCurrentUnitType(row, column) == Item.BARRACKS) {
            if(player[actualPlayer].getWheat() >= 80 && (player[actualPlayer].getMaxPopCount()-player[actualPlayer].getPopulation()) >= 1){
            makeWarrior.setEnabled(true);
            }
            if(player[actualPlayer].getWheat() >= 120 && player[actualPlayer].getGold() >= 20 && (player[actualPlayer].getMaxPopCount()-player[actualPlayer].getPopulation()) >= 2){
            makeDestroyer.setEnabled(true);
            }
            if(player[actualPlayer].getWheat() >= 70 && player[actualPlayer].getGold() >= 30 && (player[actualPlayer].getMaxPopCount()-player[actualPlayer].getPopulation()) >= 1){
            makeArcher.setEnabled(true);
            }
            makeWarrior.addActionListener((ActionEvent e) -> {
                if (player[actualPlayer].getWheat() >= 80 && (player[actualPlayer].getMaxPopCount()-player[actualPlayer].getPopulation()) >= 1) {
                    makeUnit(row, column, Item.WARRIOR, 4, actualPlayer);
                    costOfRes(0, 80, 0);
                    refreshGameStatLabel();
                    refreshGame();
                } else {
                    makeWarrior.setEnabled(false);
                }
            });

            makeDestroyer.addActionListener((ActionEvent e) -> {
                if (player[actualPlayer].getWheat() >= 120 && player[actualPlayer].getGold() >= 20 && (player[actualPlayer].getMaxPopCount()-player[actualPlayer].getPopulation()) >= 2) {
                    makeUnit(row, column, Item.DESTROYER, 6, actualPlayer);
                    costOfRes(20, 120, 0);
                    refreshGameStatLabel();
                    refreshGame();
                } else {
                    makeDestroyer.setEnabled(false);
                }
            });
            
            makeArcher.addActionListener((ActionEvent e) -> {
                if (player[actualPlayer].getWheat() >= 70 && player[actualPlayer].getGold() >= 30 && (player[actualPlayer].getMaxPopCount()-player[actualPlayer].getPopulation()) >= 1) {
                    makeUnit(row, column, Item.ARCHER, 5, actualPlayer);
                    costOfRes(30, 70, 0);
                    refreshGameStatLabel();
                    refreshGame();
                } else {
                    makeArcher.setEnabled(false);
                }
            });
        }

        if (getCurrentUnitType(row, column) == Item.FARMH) {
            if (player[actualPlayer].getWheat() >= 100 && (player[actualPlayer].getMaxPopCount()-player[actualPlayer].getPopulation()) >= 1)  {
                makeFarmer.setEnabled(true);
                makeMiner.setEnabled(true);
                makeLumberjack.setEnabled(true);
                makeFarmer.addActionListener((ActionEvent e) -> {
                    makeUnit(row, column, Item.FARMER, 1, actualPlayer);
                    costOfRes(0, 100, 0);
                    refreshGameStatLabel();
                    refreshGame();
                });

                makeMiner.addActionListener((ActionEvent e) -> {
                    makeUnit(row, column, Item.MINER, 3, actualPlayer);
                    costOfRes(0, 100, 0);
                    refreshGameStatLabel();
                    refreshGame();
                });

                makeLumberjack.addActionListener((ActionEvent e) -> {
                    makeUnit(row, column, Item.LUMBERJACK, 2, actualPlayer);
                    costOfRes(0, 100, 0);
                    refreshGameStatLabel();
                    refreshGame();
                });
            }
        }
        }
        closeFrame.addActionListener((ActionEvent e) -> {
            unSelect();
        });
        
        area.setEditable(false);
        statPanel.add(area);
        statPanel.add(makeHero);
        statPanel.add(makeBuilder);
        statPanel.add(makeDestroyer);
        statPanel.add(makeWarrior);
        statPanel.add(makeArcher);
        statPanel.add(makeFarmer);
        statPanel.add(makeMiner);
        statPanel.add(makeLumberjack);
        statPanel.add(closeFrame);
        buildingStat.add(statPanel);
        buildingStat.setSize(400, 400);
        buildingStat.setLocation(1350, 415);
    }

    public void viewUnitStats(int row, int column) {

        unitStat = new Stats();
        JPanel statPanel = new JPanel();
        statPanel.setLayout(new BoxLayout(statPanel, BoxLayout.PAGE_AXIS));
        Font font1 = new Font("SansSerif", Font.BOLD, 20);
        JTextArea area = new JTextArea();
        switch(getCurrentUnitType(row,column)){
            case WARRIOR:
            area = new JTextArea(("Warrior")
                + "\n" + "Health Point: " + getCurrentUnitHealth(row, column)
                + "\n" + "Attack: " + getCurrentUnitAttack(row, column)
                + "\n" + "Defense: " + getCurrentUnitDefense(row, column));
            area.setFont(font1);
            break;
            case DESTROYER:
            area = new JTextArea(("Destroyer")
                + "\n" + "Health Point: " + getCurrentUnitHealth(row, column)
                + "\n" + "Attack: " + getCurrentUnitAttack(row, column)
                + "\n" + "Defense: " + getCurrentUnitDefense(row, column));
            area.setFont(font1);
            break;
            case HERO:
            area = new JTextArea(("Hero")
                + "\n" + "Health Point: " + getCurrentUnitHealth(row, column)
                + "\n" + "Attack: " + getCurrentUnitAttack(row, column)
                + "\n" + "Defense: " + getCurrentUnitDefense(row, column));
            area.setFont(font1);
            break;
            case ARCHER:
            area = new JTextArea(("Archer")
                + "\n" + "Health Point: " + getCurrentUnitHealth(row, column)
                + "\n" + "Attack: " + getCurrentUnitAttack(row, column)
                + "\n" + "Defense: " + getCurrentUnitDefense(row, column));
            area.setFont(font1);
            break;
            case LUMBERJACK:
            area = new JTextArea(("Lumberjack") 
                + "\n" + "Health Point: " + getCurrentUnitHealth(row, column)
                + "\n" + "Attack: " + getCurrentUnitAttack(row, column)
                + "\n" + "Defense: " + getCurrentUnitDefense(row, column));
            area.setFont(font1);
            break;
            case MINER:
            area = new JTextArea(("Miner")
                + "\n" + "Health Point: " + getCurrentUnitHealth(row, column)
                + "\n" + "Attack: " + getCurrentUnitAttack(row, column)
                + "\n" + "Defense: " + getCurrentUnitDefense(row, column));
            area.setFont(font1);
            break;
            case FARMER:
            area = new JTextArea(("Farmer")
                + "\n" + "Health Point: " + getCurrentUnitHealth(row, column)
                + "\n" + "Attack: " + getCurrentUnitAttack(row, column)
                + "\n" + "Defense: " + getCurrentUnitDefense(row, column));
            area.setFont(font1);
            break;
            case BUILDER:
            area = new JTextArea(("Builder")
                + "\n" + "Health Point: " + getCurrentUnitHealth(row, column)
                + "\n" + "Attack: " + getCurrentUnitAttack(row, column)
                + "\n" + "Defense: " + getCurrentUnitDefense(row, column));
            area.setFont(font1);
            break;
        }
        

        JButton ability1 = new JButton("Enrage (10ap)");
        JButton closeFrame = new JButton("Close");

        ability1.addActionListener((ActionEvent e) -> {
            ability1.setEnabled(false);

            //actionPoint usage
        });

        if (getCurrentUnitType(row, column) != Item.HERO) {
            ability1.setEnabled(false);
        }

        closeFrame.addActionListener((ActionEvent e) -> {
            unSelect();
        });

        area.setEditable(false);
        statPanel.add(area);
        statPanel.add(ability1);
        statPanel.add(closeFrame);
        unitStat.add(statPanel);
    }

    public int getCurrentUnitAttack(int row, int column) {
        return player[deployedSearch(row, column)].getUnits().
                get(player[deployedSearch(row, column)].
                        getArrayIndexOfUnit(row, column)).getPower();
    }

    public int getCurrentUnitHealth(int row, int column) {
        return player[deployedSearch(row, column)].getUnits().
                get(player[deployedSearch(row, column)].
                        getArrayIndexOfUnit(row, column)).getHealth();
    }

    public int getCurrentUnitDefense(int row, int column) {
        return player[deployedSearch(row, column)].getUnits().
                get(player[deployedSearch(row, column)].
                        getArrayIndexOfUnit(row, column)).getDefence();
    }

    public Item getCurrentUnitType(int row, int column) {
        return table[row][column].getItem();
    }

    private void makeUnit(int x, int y, Item item, int type, int playerNumber) {
        int indexes[] = getClosestGrass(x,y);
        table[indexes[0]][indexes[1]].setItem(item);
        player[playerNumber].addUnit(type, indexes[0], indexes[1]);
        deployed.add(new Deployed(indexes[0], indexes[1], playerNumber, player[playerNumber].
                getUnits().get(player[playerNumber].
                        getArrayIndexOfUnit(indexes[0], indexes[1])).getUnitIndex(), 0));
        player[actualPlayer].setScore(player[actualPlayer].getScore()+player[actualPlayer].getUnits().get(player[actualPlayer].getArrayIndexOfUnit(indexes[0], indexes[1])).getPopCount());
        player[actualPlayer].setPopulation(player[actualPlayer].getPopulation()+player[actualPlayer].getUnits().get(player[actualPlayer].getArrayIndexOfUnit(indexes[0], indexes[1])).getPopCount());
    }

    private void costOfRes(int gold, int wheat, int wood) {
        player[actualPlayer].setGold(player[actualPlayer].getGold() - gold);
        player[actualPlayer].setWheat(player[actualPlayer].getWheat() - wheat);
        player[actualPlayer].setWood(player[actualPlayer].getWood() - wood);
    }

    private void makeBuilding(int x, int y, Item item, int type, int playerNumber) {
        table[x][y].setItem(item);
        player[playerNumber].addBuilding(type, x, y);
        deployed.add(new Deployed(x, y, playerNumber, player[playerNumber].
                getBuildings().get(player[playerNumber].
                        getArrayIndexOfBuilding(x, y)).getBuildingIndex(), 1));
         player[actualPlayer].setScore(player[actualPlayer].getScore()+player[actualPlayer].getBuildings().get(player[actualPlayer].getArrayIndexOfBuilding(x, y)).getScore());
    }

    public int getCurrentBuildingHealth(int row, int column) {
        return player[deployedSearch(row, column)].getBuildings().
                get(player[deployedSearch(row, column)].
                        getArrayIndexOfBuilding(row, column)).getHealth();
    }

    public void unSelect() {
        selected = false;
        if (unitStat != null) {
            unitStat.dispose();
            unitStat = null;
        }

        if (buildingStat != null) {
            buildingStat.dispose();
            buildingStat = null;
        }
    }

    public void refreshGame() {
        removeButtonImage(startx, starty);
        remove(mainPanel);
        loadBoard(startx, starty);
    }

    private void setRange(int row, int column) {
        for (int j = starty; j < starty + zoomArray[zoomArrayIndex]; j++) {
            for (int i = startx; i < startx + zoomArray[zoomArrayIndex]; i++) {
               fieldButtons[j][i].setEnabled(false);
            }
        }
       fieldButtons[row][column].setEnabled(true);
       
       int unitActionPointUsage = player[actualPlayer].getUnits()
               .get(player[actualPlayer].getArrayIndexOfUnit(row,column))
               .getActionPointUsage();
       int actionPoint = player[actualPlayer].getActionPoint();
       int maximumUnitMove = actionPoint / unitActionPointUsage;
       tiles = new HashMap<>();
       recPossibleTiles(row,column,maximumUnitMove,0);
       
   }
   
    public void recPossibleTiles(int row,int column,int maximumUnitMove, int cost){
        if(maximumUnitMove>cost){
            int[][] next = new int[4][2];
            next[0][0] = row+1; next[0][1] = column;
            next[1][0] = row; next[1][1] = column-1;
            next[2][0] = row-1; next[2][1] = column;
            next[3][0] = row; next[3][1] = column+1;
            for (int i = 0; i < 4; i++) {
                if(starty < row && i==2 || (starty + zoomArray[zoomArrayIndex])-1 > row && i==0 ||
                   startx < column && i==1 || (startx + zoomArray[zoomArrayIndex])-1 > column && i==3){
                    
                    for (int j = 0; j < 4; j++) {
                        for(Item item : Item.values()){
                            if(table[next[i][0]][next[i][1]].getItem() == item &&
                                    table[next[i][0]][next[i][1]].getItem() != Item.GRASS){
                                fieldButtons[next[i][0]][next[i][1]].setEnabled(true);
                            }
                        }
                    }
                    if (table[next[i][0]][next[i][1]].getItem() == Item.GRASS ||
                        table[next[i][0]][next[i][1]].getItem() == Item.TREE ||
                        table[next[i][0]][next[i][1]].getItem() == Item.MINE ||
                        origTable[next[i][0]][next[i][1]] == Item.FARM) {
                        Tile nt = new Tile(next[i][0], next[i][1]);
                        if (tiles.containsKey(nt)) {
                            if (tiles.get(nt) > cost + 1) {
                                tiles.put(nt, cost + 1);
                            }
                        } else {
                            tiles.put(new Tile(next[i][0], next[i][1]), cost + 1);
                        }
                        fieldButtons[next[i][0]][next[i][1]].setEnabled(true);
                        recPossibleTiles(next[i][0], next[i][1], maximumUnitMove, cost + 1);
                    }
                }
            }
        }
    }

    protected class Tile {

        public int x;
        public int y;

        public Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return (x * 845 + 5) + (y * (-543) - 53);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            Tile t = (Tile) o;
            return x == t.x 
            && y == t.y;
       }
   }
    
    private void makeMainBuilding(){
        Random random = new Random();

        int x1 = random.nextInt(height);
        int y1 = random.nextInt(width);
        int x2 = random.nextInt(height);
        int y2 = random.nextInt(width);
        int indexes[];
        int numberOfMainBuldings = 0;
        while (numberOfMainBuldings < 1) {
            if ((Math.abs(x1 - x2) > 8 || Math.abs(y1 - y2) > 8)
                    && (origTable[x1][y1] == Item.GRASS
                    && origTable[x2][y2] == Item.GRASS
                    )) {
                makeBuilding(x1, y1, Item.MAINB, 1, 0);
                makeUnit(x1, y1, Item.LUMBERJACK, 2, 0);
                makeUnit(x1, y1, Item.BUILDER, 0, 0);
                changeActualPlayer();
                makeBuilding(x2, y2, Item.MAINB, 1, 1);
                makeUnit(x2, y2, Item.LUMBERJACK, 2, 1);
                makeUnit(x2, y2, Item.BUILDER, 0, 1);
                changeActualPlayer();
                numberOfMainBuldings++;
            } else {
                x1 = random.nextInt(height);
                y1 = random.nextInt(width);
                x2 = random.nextInt(height);
                y2 = random.nextInt(width);
            }
        }
    }

    public int[] getClosestGrass(int row, int column) {
        int range = 1;
        int x;
        int y;
        int x2;
        int y2;
        int indexes[] = new int[2];
        boolean found = false;
        while (found == false) {
            if (row - range <= 0 && column - range <= 0) {
                x = 0;
                y = 0;
            } else if (row - range <= 0 && column - range > 0) {
                x = 0;
                y = column - range;
            } else if (row - range > 0 && column - range <= 0) {
                x = row - range;
                y = 0;
            } else {
                x = row - range;
                y = column - range;
            }
            if (row + range >= height && column + range >= width) {
                x2 = height;
                y2 = width;
            } else if (row + range >= height && column + range < width) {
                x2 = height;
                y2 = column + range;
            } else if (row + range < height && column + range >= width) {
                x2 = row + range;
                y2 = width;
            } else {
                x2 = row + range;
                y2 = column + range;
            }
            for (int i = x; i < x2; ++i) {
                for (int j = y; j < y2; ++j) {
                    if (table[i][j].getItem() == Item.GRASS) {
                        indexes[0] = i;
                        indexes[1] = j;
                        return indexes;
                    }
                }
            }
            range++;
        }
        return null;
    }
    
 public void victoryCondition(int playerNumber) {
        UIManager UI = new UIManager();
        if(playerNumber == 0 || playerNumber == 1){
            UI.put("OptionPane.messageForeGround", Color.decode("#000000"));
        }
        JOptionPane.showMessageDialog(this, getWinmessage(playerNumber),"Congratulations!", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private String getWinmessage(int playerNumber){
        switch(playerNumber){
            case 0:
                return "BLUE PLAYER WON!";
            case 1:
                return "RED PLAYER WON!";
            default:
                return "0";
        }
    }   
    
    public void setTilesAvelable(){
        for (int i = startx; i < startx + zoomArray[zoomArrayIndex]; i++) {
            for (int j = starty; j < starty + zoomArray[zoomArrayIndex]; j++) {
                fieldButtons[j][i].setEnabled(true);
            }
        }
    }
    
}