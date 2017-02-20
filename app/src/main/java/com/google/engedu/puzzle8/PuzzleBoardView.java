package com.google.engedu.puzzle8;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class PuzzleBoardView extends View {
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard, min;
    private ArrayList<PuzzleBoard> animation, solution;
    private PriorityQueue<PuzzleBoard> priorityQueue;
    private Comparator<PuzzleBoard> puzzleBoardComparator;

    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
    }

    public void initialize(Bitmap imageBitmap) {
        int width = getWidth();
        puzzleBoard = new PuzzleBoard(imageBitmap, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(500);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    public void shuffle() {
        Random random= new Random();
        int i;
        if (animation == null && puzzleBoard != null) {
            for(i=0; i< NUM_SHUFFLE_STEPS; i++) {
                // Do something. Then:
                ArrayList<PuzzleBoard> newBoards= puzzleBoard.neighbours();
                puzzleBoard = newBoards.get(random.nextInt(newBoards.size()));
                puzzleBoard.reset();
            }
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    public void solve() {
        puzzleBoardComparator = new Comparator<PuzzleBoard>() {
            @Override
            public int compare(PuzzleBoard lhs, PuzzleBoard rhs) {
                /*if(lhs.priority()> rhs.priority()){
                    //priorityQueue.add(lhs);
                    return 1;
                }
                else {
                    //priorityQueue.add(rhs);
                    return -1;
                }*/
                //return 0;
                return lhs.priority()-rhs.priority();
            }
        };
        priorityQueue = new PriorityQueue<>(16, puzzleBoardComparator);
        priorityQueue.add(puzzleBoard);

        ArrayList<PuzzleBoard> boards;
        PuzzleBoard prevBoard;
        while (!priorityQueue.isEmpty()){
            min = priorityQueue.poll();
            if(min.resolved()){
                boards = min.neighbours();
                for(PuzzleBoard p:boards){
                    if(!p.equals(min.getPreviousBoard())) {
                        priorityQueue.add(p);
                    }
                }
            }
            else{
                solution = new ArrayList<>();
                solution.add(min);
                prevBoard = min.getPreviousBoard();
                while(prevBoard != null){
                    solution.add(prevBoard);
                    prevBoard = prevBoard.getPreviousBoard();
                }
                //Collections.reverse(solution);
                animation = solution;
                invalidate();
            }
        }
    }

    /*private boolean isSolution(PuzzleBoard p){
        ArrayList<PuzzleTile> tiles = p.getTiles();
        PuzzleTile tile;
        for(int i=0; i<tiles.size(); i++){
            tile = tiles.get(i);
            if(tile == null){
                if(i != 15){
                    return false;
                }
            }
            else if(i != tile.getNumber()){
                return false;
            }
        }
        return true;
    }*/

}
