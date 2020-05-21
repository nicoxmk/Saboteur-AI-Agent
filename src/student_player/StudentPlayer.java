package student_player;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurCard;
import Saboteur.cardClasses.SaboteurDrop;

import Saboteur.cardClasses.SaboteurTile;
import boardgame.Move;

import Saboteur.SaboteurPlayer;
import Saboteur.SaboteurBoardState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/** A player file submitted by a student. */
public class StudentPlayer extends SaboteurPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260770119");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(SaboteurBoardState boardState) {
        //TODO:nuggetKnown and positionRevealed, then set up goal, and get intgoal
        //TODO:get current hiddenBoard and hintBoard
        int turn = boardState.getTurnNumber();
        Move myMove = null;
        boolean nuggetKnown = false;
        boolean[] positionRevealed = {false,false,false};
        // set goal to be at the middle position
        int[] goal = {12,5};
        //TODO: initialize goal every turn and board info
        int hiddenNum = 0;
        SaboteurTile[][] hiddenBoard = boardState.getHiddenBoard();
        for (int i = 0; i < 3; i++) {
            //if a tile is revealed as nugget, directly return as goal
            if (hiddenBoard[12][3+2*i].getIdx().equals("nugget")) {
                goal[0] = 12;
                goal[1] = 3+2*i;
                nuggetKnown = true;
                positionRevealed[i] = true;
                break;
            }
            else if(hiddenBoard[12][3+2*i].getIdx().equals("hidden1") ||
                    hiddenBoard[12][3+2*i].getIdx().equals("hidden2")){
                positionRevealed[i] = true;
                hiddenNum++;
            }
        }
        //if two tiles are revealed as hidden, then the last one must be nugget
        if(hiddenNum == 2){
            for(int i = 0; i < 3; i++){
                if(!hiddenBoard[12][3+2*i].getIdx().equals("hidden1") &&
                        !hiddenBoard[12][3+2*i].getIdx().equals("hidden2")){
                    goal[0] = 12;
                    goal[1] = 3+2*i;
                    nuggetKnown = true;
                }
            }
        }
        // if only one tile is revealed "Hidden" (either LEFT/RIGHT), still set MIDDLE as goal
        //System.out.println("Goal is set to: { " + goal[0] + " , " + goal[1] + " }");
        //System.out.print("The position revealed as: ");
        //for(int i=0;i<3;i++) { System.out.print(positionRevealed[i]);}
        //System.out.print("nugget known ? "+nuggetKnown);

        int[] intgoal = MyTools.setIntGoal(goal);
        int[][] hintboard = boardState.getHiddenIntBoard();
        //TODO:get all legal moves and classify them
        ArrayList<SaboteurMove> legalMoves = boardState.getAllLegalMoves();
        //create different arraylist to store different moves
        ArrayList<SaboteurMove> bonusCard = new ArrayList<>();
        ArrayList<SaboteurMove> malusCard = new ArrayList<>();
        ArrayList<SaboteurMove> tileCardGo = new ArrayList<>();
        ArrayList<SaboteurMove> tileCardEnd = new ArrayList<>();
        ArrayList<SaboteurMove> destroyCard = new ArrayList<>();
        ArrayList<SaboteurMove> mapCard = new ArrayList<>();
        ArrayList<SaboteurMove> dropCard = new ArrayList<>();
        //Get all current cards on hand
        ArrayList<SaboteurCard> currentHand = boardState.getCurrentPlayerCards();
        //filter them into different Arraylists
        for(SaboteurMove sm : legalMoves) {
            if (sm.toPrettyString().contains("Bonus")) {
                bonusCard.add(sm);
            } else if (sm.toPrettyString().contains("Malus")) {
                malusCard.add(sm);
            } else if (sm.toPrettyString().contains("Tile")) {
                ArrayList<String> tileEndName= new ArrayList<>();
                String[] temp_str= {"1","2","2_flip","3","3_flip","4","4_flip","11","11_flip","12","12_flip","13","14","14_flip","15"};
                tileEndName.addAll(Arrays.asList(temp_str));
                String temp_moveName = (sm.getCardPlayed().getName().split(":"))[1];
                if (tileEndName.contains(temp_moveName)) {
                    tileCardEnd.add(sm);
                }
                else {
                    tileCardGo.add(sm);
                }
            }  else if (sm.toPrettyString().contains("Destroy")) {
                destroyCard.add(sm);
            } else if(sm.toPrettyString().contains("Map")){
                mapCard.add(sm);
            }
            else if(sm.getCardPlayed() instanceof SaboteurDrop){
                dropCard.add(sm);
            }
        }

        //int turn=boardState.getTurnNumber();
        int myPlayNumber=boardState.getTurnPlayer();
        int otPlayNumber = 0;
        if(myPlayNumber==1) { otPlayNumber = 0;}
        else {otPlayNumber=1;}
        if(boardState.getNbMalus(myPlayNumber)!=0) {
            System.out.println("I am MALUSED");
            if(bonusCard.size()!=0) {
//////////////////do bonus//////////////////////////
                myMove = bonusCard.get(0);
                System.out.println("Bonus Card used!");
                return myMove;
            }
//////////////////do malus/////////////////////////
            else if((boardState.getNbMalus(otPlayNumber)==0) && (malusCard.size()!=0)) {
                myMove = malusCard.get(0);
                System.out.println("Malus Card used!");
                return myMove;
            }
//////////////////do map//////////////////////////
            else if ((!nuggetKnown) && (mapCard.size() != 0)) {
                // {false, false, false}
                if(positionRevealed[0] == false && mapCard.get(0).getPosPlayed()[0]==12 && mapCard.get(0).getPosPlayed()[1]==3){
                    myMove = mapCard.get(0);
                    System.out.println("Map Card used as {12,3}!");
                    return myMove;
                }
                //posRevealed{true,false,false}
                else if(positionRevealed[0] == true && positionRevealed[1] == false){
                    if(mapCard.get(1).getPosPlayed()[0]==12 && mapCard.get(1).getPosPlayed()[1]==5){
                        myMove = mapCard.get(1);
                        System.out.println("Map Card used at {12,5}!");
                        return myMove;
                    }
                }
            }
//////////////////drop map////////////////////////
            else if ((nuggetKnown==true) && (mapCard.size() != 0)) {
                for(SaboteurCard sc: currentHand){
                    if(sc.getName()=="Map"){
                        int dropPosition = currentHand.indexOf(sc);
                        myMove = new SaboteurMove(new SaboteurDrop(), dropPosition,0, boardState.getTurnPlayer());
                        System.out.println("Map Card dropped!");
                        return myMove;
                    }
                }
            }
//////////////////drop destroy/////////////////////
            else if (destroyCard.size() != 0) {
                for(SaboteurCard sc: currentHand){
                    if(sc.getName() == "Destroy"){
                        int dropPosition = currentHand.indexOf(sc);
                        myMove = new SaboteurMove(new SaboteurDrop(), dropPosition,0, boardState.getTurnPlayer());
                        System.out.println("Destroy Card dropped!");
                        return myMove;
                    }
                }
            }
//////////////////drop tile end///////////////////
            else if (tileCardEnd.size() !=0) {
                for(int i=0; i<currentHand.size();i++) {
                    if (currentHand.get(i).getName().contains("1")
                            ||currentHand.get(i).getName().contains("2")
                            ||currentHand.get(i).getName().contains("3")
                            ||currentHand.get(i).getName().contains("4")
                            ||currentHand.get(i).getName().contains("11")
                            ||currentHand.get(i).getName().contains("12")
                            ||currentHand.get(i).getName().contains("13")
                            ||currentHand.get(i).getName().contains("14")
                            ||currentHand.get(i).getName().contains("15")) {
                        myMove = new SaboteurMove(new SaboteurDrop(), i,0, boardState.getTurnPlayer());
                        System.out.println("Tile End Card dropped!");
                        return myMove;
                    }
                }
            }
//////////////////drop tile go//////////////////////
            else if (tileCardGo.size() != 0) {
                for(int i=0; i<currentHand.size();i++) {
                    if (currentHand.get(i).getName().contains("0")
                            ||currentHand.get(i).getName().contains("5")
                            ||currentHand.get(i).getName().contains("6")
                            ||currentHand.get(i).getName().contains("7")
                            ||currentHand.get(i).getName().contains("8")
                            ||currentHand.get(i).getName().contains("9")
                            ||currentHand.get(i).getName().contains("10")) {
                        myMove = new SaboteurMove(new SaboteurDrop(), i,0, boardState.getTurnPlayer());
                        System.out.println("Tile Go Card dropped!");
                        return myMove;
                    }
                }
            }
//////////////////drop malus//////////////////////
            else if (malusCard.size() != 0) {
                for(SaboteurCard sc: currentHand){
                    if(sc.getName() == "Malus"){
                        int dropPosition = currentHand.indexOf(sc);
                        myMove = new SaboteurMove(new SaboteurDrop(), dropPosition,0, boardState.getTurnPlayer());
                        System.out.println("Malus Card dropped!");
                        return myMove;
                    }
                }
            }
//////////////////drop random////////////////////
            else {
                myMove = dropCard.get(0);
                System.out.println("drop random IF MALUSED, this should not happen!");
                return myMove;
            }
        }
//////////////////if I'm not malused.//////////////
        else {
            System.out.println("I am NOT MALUSED");

//////////////////do malus/////////////////////////
            if ((boardState.getNbMalus(otPlayNumber)==0) && (malusCard.size()!=0)) {
                myMove = malusCard.get(0);
                System.out.println("Malus Card used!");
                return myMove;
            }
////////////////do map///////////////////////////
            else if ((!nuggetKnown) && (mapCard.size() != 0)) {
                if(positionRevealed[0] == false && mapCard.get(0).getPosPlayed()[0]==12 && mapCard.get(0).getPosPlayed()[1]==3){
                    myMove = mapCard.get(0);
                    System.out.println("Map Card used as {12,3}!");
                    return myMove;
                }
                //posRevealed{true,false,false}
                else if(positionRevealed[0] == true && positionRevealed[1] == false){
                    if(mapCard.get(1).getPosPlayed()[0]==12 && mapCard.get(1).getPosPlayed()[1]==5){
                        myMove = mapCard.get(1);
                        System.out.println("Map Card used at {12,5}!");
                        return myMove;
                    }
                }
            }
//////////////////do tile///////////////////////////
            else if (tileCardGo.size() != 0) {
                System.out.println("We do have tile card go!");
                ArrayList<int[]> store=new ArrayList<int []>();
                for(int i=0; i<tileCardGo.size(); i++) {
                    SaboteurMove tileMove=tileCardGo.get(i);
                    System.out.println("move No:"+i+tileMove.toPrettyString());
                    int[] playPos=tileMove.getPosPlayed();
                    SaboteurTile playCard=(SaboteurTile)tileMove.getCardPlayed();
                    int[][] playCardPath=MyTools.turn3X3mat(playCard.getPath());
                    //copy the hintboard first
                    int[][] copyhintboard=MyTools.copy2dMat(hintboard);
                    //put our new move on hint board
                    for(int p=0;p<3;p++) {
                        for(int q=0;q<3;q++) {
                            int[] intPos= {playPos[0]*3+p,playPos[1]*3+q};
                            if(copyhintboard[intPos[0]][intPos[1]]==-1) {
                                copyhintboard[intPos[0]][intPos[1]]=playCardPath[p][q];
                            } else {
                                System.out.println("Error from tile card in StudentPlayer.java; this part of hint board shoule be empty!");}
                            //hintboard=hiddenIntBoard, should be empty at where no tile or objective place
                            //we should never be able to put tile on objective place, no such legal move output
                        }
                    }
                    //1st:check if move connects back to entrance!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    //set our path end point as the middle point of the entrance tile, start point should be the middle point of current move
                    int[] movePoint = {playPos[0]*3+1,playPos[1]*3+1};
                    int[] entrancePoint = {5*3+1,5*3+1};
                    boolean containPath = MyTools.checkPath(movePoint,entrancePoint,copyhintboard);
                    //if no path, skip this move
                    if(containPath==false) {
                        System.out.println("You pass this move because it is not a path!");
                        continue;}
                    for(int p=0; p<3; p++) {
                        for(int q=0;q<3; q++) {
                            if ((playCardPath[p][q]==1)&&((p!=1)||(q!=1))) {
                                int[] intOnePos={playPos[0]*3+p,playPos[1]*3+q};
                                System.out.println("AP%"+i+"--"+"gate1 int position: x:"+intOnePos[0]+"y:"+intOnePos[1]);/////////////////////////////
                                if (!MyTools.isInside(intOnePos,hintboard)) {
                                    int ManDistance = MyTools.calManDistance(intOnePos, intgoal);
                                    int[] temp= {i,ManDistance};
                                    store.add(temp);}
                            }
                        }
                    }
                }

                //after for loop of all move
                //System.out.println("AP% size of store:"+store.size());
                if (store.size()==0) {
                    System.out.println("you pass all move, do random drop!");
                    myMove = dropCard.get(0);
                    return myMove;
                } else {
                    //every tile move is sorted with Man dis in this List
                    ArrayList<int[]> sortedStore=MyTools.sortIntL2(store);
                    System.out.println("you do have prefered move!");
                    MyTools.printALint(sortedStore);
                    int[] tempList=sortedStore.get(0);
                    int bestOne=tempList[0];
                    myMove = tileCardGo.get(bestOne);
                    System.out.println("this is my move:"+myMove.toPrettyString());
                    return myMove;
                }
            }
//////////////////drop map//////////////////////
            else if ((nuggetKnown) && (mapCard.size() != 0)) {
                for(SaboteurCard sc: currentHand){
                    if(sc.getName()=="Map"){
                        int dropPosition = currentHand.indexOf(sc);
                        myMove = new SaboteurMove(new SaboteurDrop(), dropPosition,0, boardState.getTurnPlayer());
                        System.out.println("Map Card dropped!");
                        return myMove;
                    }
                }
            }
//////////////////drop destroy/////////////////
            else if (destroyCard.size() !=0) {
                for(SaboteurCard sc: currentHand){
                    if(sc.getName() == "Destroy"){
                        int dropPosition = currentHand.indexOf(sc);
                        if(boardState.isLegal(new SaboteurMove(new SaboteurDrop(), dropPosition,0, boardState.getTurnPlayer()))) {
                            myMove = new SaboteurMove(new SaboteurDrop(), dropPosition,0, boardState.getTurnPlayer());
                            System.out.println("Destroy Card dropped!");
                            return myMove;
                        }
                        else {
                            System.out.println("Destroy Drop is not legal.");
                        }
                    }
                }
            }
//////////////////drop tile end/////////////////
            else if (tileCardEnd.size() !=0) {
                for(int i=0; i<currentHand.size();i++) {
                    if (currentHand.get(i).getName().contains("1")
                            ||currentHand.get(i).getName().contains("2")
                            ||currentHand.get(i).getName().contains("3")
                            ||currentHand.get(i).getName().contains("4")
                            ||currentHand.get(i).getName().contains("11")
                            ||currentHand.get(i).getName().contains("12")
                            ||currentHand.get(i).getName().contains("13")
                            ||currentHand.get(i).getName().contains("14")
                            ||currentHand.get(i).getName().contains("15")) {
                        myMove = new SaboteurMove(new SaboteurDrop(), i,0, boardState.getTurnPlayer());
                        System.out.println("Tile End Card dropped!");
                        return myMove;
                    }
                }
            }
//////////////////drop malus////////////////////
            else if (malusCard.size() !=0) {
                for(SaboteurCard sc: currentHand){
                    if(sc.getName() == "Malus"){
                        int dropPosition = currentHand.indexOf(sc);
                        if(boardState.isLegal(new SaboteurMove(new SaboteurDrop(), dropPosition,0, boardState.getTurnPlayer()))) {
                            myMove = new SaboteurMove(new SaboteurDrop(), dropPosition,0, boardState.getTurnPlayer());
                            System.out.println("Malus Card dropped!");
                            return myMove;
                        }
                        else {
                            System.out.println("Malus Drop is not legal.");
                        }
                    }
                }
            }
//////////////////drop Tile Go////////////////////
            else if (tileCardGo.size() != 0) {
                for(int i=0; i<currentHand.size();i++) {
                    if (currentHand.get(i).getName().contains("0")
                            ||currentHand.get(i).getName().contains("5")
                            ||currentHand.get(i).getName().contains("6")
                            ||currentHand.get(i).getName().contains("7")
                            ||currentHand.get(i).getName().contains("8")
                            ||currentHand.get(i).getName().contains("9")
                            ||currentHand.get(i).getName().contains("10")) {
                        myMove = new SaboteurMove(new SaboteurDrop(), i,0, boardState.getTurnPlayer());
                        System.out.println("Tile Go Card dropped!");
                        return myMove;
                    }
                }
            }
//////////////////drop bonus///////////////////////
            else if (bonusCard.size() !=0) {
                for(SaboteurCard sc: currentHand){
                    if(sc.getName() == "Bonus"){
                        int dropPosition = currentHand.indexOf(sc);
                        if(boardState.isLegal(new SaboteurMove(new SaboteurDrop(), dropPosition,0, boardState.getTurnPlayer()))) {
                            myMove = new SaboteurMove(new SaboteurDrop(), dropPosition,0, boardState.getTurnPlayer());
                            System.out.println("Bonus Card dropped!");
                            return myMove;
                        }
                        else {
                            System.out.println("Bonus Drop is not legal.");
                        }
                    }
                }
            }
//////////////////random Drop////////////////////
            else {
                myMove = dropCard.get(0);
                System.out.println("drop random IF NOT MALUSED, this should not happen!");
                return myMove;
            }
        }
//////////////////Really rare cases/////////////
        System.out.println("BIG WARNING!!!Nothing can be return as a move! all legal moves printed:");

        for(SaboteurMove sm : legalMoves) {
            System.out.println(sm.toPrettyString());
        }
        myMove = dropCard.get(0);
        return myMove;
    }
}