package student_player;
import Saboteur.SaboteurBoard;
import Saboteur.SaboteurBoardState;
import Saboteur.cardClasses.SaboteurTile;
import boardgame.BoardState;

import java.util.ArrayList;
import java.util.*;


public class MyTools {

    //TODO: set goal int position
    public static int[] setIntGoal(int[] goal) {
        int[] intgoal={goal[0]*3+1,goal[1]*3+1};
        return intgoal;
    }

    //TODO: make path 3X3 matrix into correct config direction
    public static int[][] turn3X3mat(int[][] original){
        int[][] temping = new int[3][3];
        for(int p=0; p<3; p++) {
            for(int q=0; q<3; q++) {
                temping [2-q][p]=original[p][q];
            }
        }
        return temping;
    }

    //TODO: consider whether this position is a inside position or not
    public static boolean isInside(int[] pos, int[][] refBoard){
        ArrayList<int[]> storePos = new ArrayList<int[]>();
        for(int i=0;i<refBoard.length;i++) {
            for(int j=0;j<refBoard[0].length;j++) {
                if(refBoard[i][j]==1) {
                    int[] temp1= {i-1,j};
                    int[] temp2= {i+1,j};
                    int[] temp3= {i,j-1};
                    int[] temp4= {i,j+1};
                    storePos.add(temp1);
                    storePos.add(temp2);
                    storePos.add(temp3);
                    storePos.add(temp4);}
            }
        }
        if(storePos.contains(pos)) {
            return true;
        } else {
            return false;
        }
    }

    //TODO: calculate manhatten distance between two points
    public static int calManDistance(int[] pos, int[] target) {
        int dis=Math.abs(pos[0]-target[0])+Math.abs(pos[1]-target[1]);
        return dis;
    }

    //TODO: sort based on the second element in the element list
    public static ArrayList<int[]> sortIntL2(ArrayList<int[]> original){
        Collections.sort(original, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(a[1], b[1]);}
        });
        return original;
    }

    //TODO: to print matrix:
    public static String printMat(int[][] t) {
        String r="";
        for(int i=0;i<t.length;i++) {
            for(int j=0;j<t.length;j++) {
                r=r+t[i][j];
                System.out.print(t[i][j]+" ");
            }
            r=r+"\n";
            System.out.println("");
        }
        return r;
    }

    //TODO: to copy a 2d mattrix
    public static int[][] copy2dMat(int[][] o) {
        if ((o.length==0)||(o[0].length==0)) {
            System.out.println("Error from Mytools.java, copy2dMat; the length of each side should not be 0;");}
        int[][] c=new int[o.length][o[0].length];
        for(int i=0;i<o.length;i++) {
            for(int j=0;j<o[0].length;j++) {
                c[i][j]=o[i][j];
            }
        }
        return c;
    }


    //TODO: to print ArrayList<int[]>
    public static void printALint(ArrayList<int[]> tar) {
        System.out.print("Printing ArrayList<int[]>, size="+tar.size()+" contenet: ");
        if (tar.size()==0) {} else {
            for(int i=0;i<tar.size();i++) {
                int[] tari=tar.get(i);
                System.out.print(tari[0]+","+tari[1]+"  ");
            }
        }
        System.out.println("");
        return;
    }

    //TODO: to check if there is a path from start point to end point at the map (DFS)
    public static boolean checkPath(int[] startP, int[] endP, int[][] map) {
        if(map[startP[0]][startP[1]]!=1) {
            System.out.println("Error(MyTools.java-checkPath):this start point is not valid;");
            return false;}
        else if (map[endP[0]][endP[1]]!=1) {
            System.out.println("Error(MyTools.java-checkPath):this end point is not valid;");
            return false;}
        else {
            ArrayList<int[]> pipe = new ArrayList<int[]>();
            pipe.add(startP);
            ArrayList<int[]> checked = new ArrayList<int []>();
            int counter=0;
            while(pipe.size()!=0) {
                counter++;

                int[] currentPoint=pipe.get(0);
                checked.add(currentPoint);
                pipe.remove(0);
                if (currentPoint[0]==endP[0] && currentPoint[1]==endP[1]) {
                    return true;}
                int curx=currentPoint[0];
                int cury=currentPoint[1];
                ArrayList<int[]> neigh= new ArrayList<int []>();
                if (curx-1>=0) {
                    int[] neighP= {curx-1,cury};
                    neigh.add(neighP);}
                if (curx+1<map.length) {
                    int[] neighP= {curx+1,cury};
                    neigh.add(neighP);}
                if (cury-1>=0) {
                    int[] neighP= {curx,cury-1};
                    neigh.add(neighP);}
                if (cury+1<map[0].length) {
                    int[] neighP= {curx,cury+1};
                    neigh.add(neighP);}
                for(int i=0;i<neigh.size();i++) {
                    int[] neighP=neigh.get(i);
                    if(map[neighP[0]][neighP[1]]==1) {
                        boolean contains=false;
                        for(int j=0;j<checked.size();j++) {
                            int[] tempCheck=checked.get(j);
                            if(tempCheck[0]==neighP[0] && tempCheck[1]==neighP[1]) {contains=true;}
                        }
                        if(contains==false) {
                            pipe.add(neighP);
                        }
                    }
                }

            }
        }

        return false;
    }

    public static void main(String[] args) { }
}