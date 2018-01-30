package com.conceptscorp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by conceptscorp on 1/22/18.
 */
public class Regressor{
    private DataPool dataPool;
    ArrayList<ArrayList> temp = new ArrayList<ArrayList>();
    private ArrayList<Double> meanFeatures = new ArrayList<Double>();
    private ArrayList<ArrayList> rowSet = new ArrayList<ArrayList>(); //data in row format x0,x1,x3 etc. per one inner list
    private ArrayList<Long> deviation = new ArrayList<Long>();
    private ArrayList<Double> hypothesis = new ArrayList<Double>();
    private double[] theta = new double[10];
    //private int[] theta = new int[87];

    public Regressor(DataPool dataPool){
        this.dataPool = dataPool;
    }

    private void printTheta(){
        for(double item : theta){
            System.out.print(item + " ");
        }
        System.out.println();
    }

    private void calculateMean(){
        for(ArrayList<Long> item : temp){
            double mean = 0;
            for(Long val : item){
                mean += val.doubleValue();
            }
            mean = mean / 20;
            this.meanFeatures.add(mean);
        }
    }

    private void calculateDeviation(){
        long max, min, devi = 0;
        for(ArrayList<Long> item : temp){
            max = Collections.max(item);
            min = Collections.min(item);
            devi = max - min;
            this.deviation.add(devi);
        }
    }

    private void generateRows(){
        double norm = 0;
        double devi = 0;
        for(int i = 0; i < 20; i++){
            ArrayList<Double> row = new ArrayList<Double>();
            row.add((double)1);
            for(int j = 0; j < temp.size(); j++){
                ArrayList<Long> col = temp.get(j);
                devi = (double)this.deviation.get(j);
                norm = (col.get(i) - this.meanFeatures.get(j)) / devi;
                System.out.println(norm);
                row.add(norm);
            }
            this.rowSet.add(row);
        }
    }

    private void calculateHypothesis(){
        for(ArrayList<Double> item : this.rowSet) {
            double hypo = 0;
            for (int i = 0; i < 10; i++) {
                hypo = hypo + this.theta[i] * item.get(i).doubleValue();
            }
            hypo = 1 /( 1 + Math.exp(-1*hypo) );
            this.hypothesis.add(hypo);
        }
    }

    private double sumDerivative(int  i){
        double sum = 0;
        for(int j = 0; j < 10; j++){
            ArrayList<Double> row = this.rowSet.get(j);
            sum = sum + this.hypothesis.get(j).doubleValue()*row.get(i).doubleValue();
        }

        for(int j = 10; j < 20; j++){
            ArrayList<Double> row = this.rowSet.get(j);
            sum = sum + (this.hypothesis.get(j).doubleValue() - 1)*row.get(i).doubleValue();
        }

        return sum;
    }

    private void fitToModel(float learningRate){
        float ratio = learningRate/20;
        double[] highAccTheta = new double[10];
        double[] previous = null;

        while(!Arrays.equals(previous, this.theta)){
            previous = this.theta.clone();
            this.calculateHypothesis();
            for (int i = 0; i < 10; i++) {
                highAccTheta[i] = highAccTheta[i] - ratio * this.sumDerivative(i);
                this.theta[i] = this.round(highAccTheta[i], 4);
            }
            this.printTheta();
            this.hypothesis.clear();
        }
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public int identifyUser(ArrayList<Long> wordMillis){
        double val = 0;
        for(int i = 0; i < 10; i++){
            if(i == 0) {
                val = val + this.theta[i];
            }else{
                val = val + this.theta[i] * ((wordMillis.get(i-1).doubleValue() - this.meanFeatures.get(i-1).doubleValue()) / this.deviation.get(i-1).doubleValue());
            }
        }
        val = 1 /( 1 + Math.exp(-1*val) );

        int user = 2;
        if(val < 0.5){
            user = 0;
        }else if(val >= 0.5){
            user = 1;
        }

        return user;
    }

    public void run(){
        //this.printDataset();
        this.temp = this.dataPool.getDataset();
        this.calculateMean();
        this.calculateDeviation();
        this.generateRows();

        for(ArrayList<Double> item : this.rowSet){
            for(Double val : item){
                System.out.print(val.doubleValue());
                System.out.print(" ");
            }
            System.out.println();
        }

        this.fitToModel((float)0.8);

        for(double item : this.theta){
            System.out.print(item + " ");
        }
    }
}
