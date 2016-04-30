package com.example.parth.alarmclock;

/**
 * Created by Priyanshu on 12-04-2016.
 */
import java.util.Random;

public class GenerateMathsQuestion {


    public static char getOperator(int i){

        if(i == 0)
            return '+';
        else if(i == 1)
            return '*';
        else
            return '-';
    }

    public static String generateQuestion(String diff){
        //int [] operands = new int[3];
        int operations;
        int n;
        if(diff.equals("Easy"))
            n=3;
        else if(diff.equals("Medium"))
            n=5;
        else
            n=7;
        Random operand = new Random();
        String question = "";
        for(int i=0; i<n; i++){
            question = question + " " + operand.nextInt(9 - 0);
            operations = operand.nextInt(2-0) + 0;
            question = question + " " + getOperator(operations);
        }
        question = question.substring(1,question.length()-1);
        return question;

    }

}
