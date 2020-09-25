package com.acme.app.main.resources;

public class Auxiliar {

    public static int count;


    static public int autoIncrementKey(){
        if(count == 0)
            count = 1;
        else{
            count++;
        }
        return count;
    }
}
