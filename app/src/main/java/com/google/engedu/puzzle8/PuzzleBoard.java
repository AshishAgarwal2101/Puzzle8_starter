package com.google.engedu.puzzle8;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;


public class PuzzleBoard {

    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };
    private ArrayList<PuzzleTile> tiles;
    private int steps, manDistance;
    private PuzzleBoard previousBoard;

    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        tiles = new ArrayList<>();
        int x,y, tileNo=0;
        PuzzleTile tile;
        steps = 0;
        previousBoard = null;
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, parentWidth, parentWidth, true);
        try {
            for (x = 0; x < NUM_TILES; x++) {
                for (y = 0; y < NUM_TILES; y++) {
                    if (tileNo < (NUM_TILES*NUM_TILES-1)) {
                        Bitmap b = Bitmap.createBitmap(newBitmap, parentWidth/ NUM_TILES*y, parentWidth/ NUM_TILES*x, parentWidth / NUM_TILES, parentWidth/NUM_TILES);
                        tile = new PuzzleTile(b, tileNo++);
                        tiles.add(tile);
                    } else {
                        tile = null;
                        tiles.add(tile);
                    }
                }
            }
        }catch (IllegalArgumentException e){

        }

    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
        steps= otherBoard.steps + 1;
        previousBoard = otherBoard;
    }

    public PuzzleBoard getPreviousBoard(){
        return previousBoard;
    }
    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i % NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    public ArrayList<PuzzleBoard> neighbours() {
        int i,j,move;
        ArrayList<PuzzleBoard> newBoard= new ArrayList<>();
        for(i=0; i< tiles.size(); i++)
        {
            if(tiles.get(i)== null){
                break;
            }
        }

        for(j=0; j< NUM_TILES; j++){
            move= XYtoIndex(NEIGHBOUR_COORDS[j][0],NEIGHBOUR_COORDS[j][1])+ i;
           // move = (i/NUM_TILES+NEIGHBOUR_CORDS[j][0])*NUM_TILES + (i%NUM_TILES+NEIGHBOUR_COORDS[j][1]);
            if(move> -1 && move < 9){

                PuzzleBoard pb = new PuzzleBoard(this);
                pb.swapTiles(i, move);
                newBoard.add(pb);
            }

        }
        return newBoard;

    }

    public int priority() {
        int i,verDist,horDist;
        PuzzleTile tile;
        manDistance = 0;
        for(i=0; i<tiles.size(); i++){
            tile = tiles.get(i);
            if(tile != null) {
                horDist = Math.abs(tile.getNumber() % NUM_TILES - i % NUM_TILES);
                verDist = Math.abs(tile.getNumber() / NUM_TILES - i / NUM_TILES);
                manDistance = manDistance + horDist + verDist;
            }
        }
        return manDistance + steps;
    }

    public ArrayList<PuzzleTile> getTiles(){
        return tiles;
    }

}
