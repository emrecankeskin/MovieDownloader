package org.MovieDownloader;

import java.util.ArrayList;
import java.util.List;

public class Partition {

    int nThread;
    int divider;
    //a list for part1 part2 ---- part16 text file names


    ArrayList<List<String>> secondList;


    public Partition(int nThread){
        this.nThread = nThread;
        //thirdStageThreads = Executors.newFixedThreadPool(2);

    }

    public ArrayList<List<String>> divide(ArrayList<String> firstList){

        secondList = new ArrayList<>();
        divider = firstList.size() / nThread;


        for(int i = 0; i< firstList.size(); i = i + divider){
            System.out.println("PARTITION CLASS ->"+i);
            secondList.add(firstList.subList(i,Math.min(i+divider,firstList.size())));
        }
        return secondList;
    }


    public ArrayList<List<String>> getPartedList() {
        return secondList;
    }



}
