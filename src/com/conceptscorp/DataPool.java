package com.conceptscorp;

import java.util.ArrayList;

/**
 * Created by conceptscorp on 1/21/18.
 */

public class DataPool {
    private ArrayList<Long> wordMillis = new ArrayList<Long>();
    private ArrayList<ArrayList> dataset = new ArrayList<ArrayList>();

    public void addWordMillis(long millis){
        wordMillis.add(millis);
    }

    private void generateDataset(){
        this.dataset.clear();
        for(int j= 0; j < 9; j++) {
            ArrayList<Long> col = new ArrayList<Long>();
            for (int i = 0; i < 20; i++) {
                col.add(this.wordMillis.get(9*i + j));
            }
            this.dataset.add(col);
        }
    }

    public ArrayList<ArrayList> getDataset(){
        this.generateDataset();
        return this.dataset;
    }
}
