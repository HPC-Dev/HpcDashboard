package com.results.HpcDashboard.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Variance
{

    public static double average(double sum, int size) {
        return sum/size;
    }

    public static double sum(List<Double> list) {
        double sum = 0;
        for(double num : list) {
            sum += num;
        }
        return sum;
    }


    public static double variance(List<Double> list) {

        if(list.size()==1)
            return 0;
        double sumDiffsSquared = 0.0;
        int length = list.size();
        double sum = sum(list);

        double mean = average(sum,length);

        for (double value : list)
        {
            double diff = value - mean;
            diff *= diff;
            sumDiffsSquared += diff;
        }
        return sumDiffsSquared  /(list.size()-1);
    }


    public static double calculateSD(List<Double> list)
    {
        if(list.size()==1)
            return 0;

        double standardDeviation = 0.0;
        int length = list.size();
        double sum = sum(list);

        double mean = average(sum,length);

        for(double num: list) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }

    public static void main(String[] args) {
        //List<Double> list = new ArrayList<>(Arrays.asList(0.23, 0.2, 0.22, 0.25));
        List<Double> list = new ArrayList<Double>(Arrays.asList(1419.29,1418.06,1452.62));
        //System.out.println(calculateSD(list));

    }
}
