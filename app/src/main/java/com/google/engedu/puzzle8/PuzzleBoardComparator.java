package com.google.engedu.puzzle8;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class PuzzleBoardComparator {
    //private PriorityQueue<PuzzleBoard> priorityQueue;
    //private Comparator<PuzzleBoard> comparator;

    private PuzzleBoard board1, board2,min;
    PuzzleBoardComparator(PuzzleBoard board1, PuzzleBoard board2){
        this.board1 = board1;
        this.board2 = board2;
        /*comparator = new Comparator<PuzzleBoard>() {
            @Override
            public int compare(PuzzleBoard lhs, PuzzleBoard rhs) {
                if(lhs.priority()< rhs.priority()){
                    priorityQueue.add(lhs);
                }
                else {
                    priorityQueue.add(rhs);
                }
                return 0;
            }
        };
        priorityQueue = new PriorityQueue<>(0, comparator);*/
    }

    private void check(){
        /*ArrayList<PuzzleBoard> boards;
        while (priorityQueue.size() != 0){
            min = priorityQueue.remove();
            if(!checkSolution(min)){
                boards = min.neighbours();
                for(PuzzleBoard p:boards){
                    priorityQueue.add(p);
                }
            }
        }*/
    }

    /*private boolean checkSolution(PuzzleBoard p){
        ArrayList<PuzzleTile> tiles = p.getTiles();
        PuzzleTile tile;
        for(int i=0; i<tiles.size(); i++){
            tile = tiles.get(i);
            if(i != tile.getNumber()){
                return false;
            }
        }
        return true;
    }*/
}
