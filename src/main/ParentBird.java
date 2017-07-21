package main;

import java.util.Random;

// fornece a comida para os filhotes
public class ParentBird implements Runnable{
    
    public ParentBird(){
        Random r = new Random();
        
        // cria um delay para repor comida
        foodReleaseDelay = (int)(r.nextFloat()*1000)%100 + 100;
        
        thread = new Thread(this, "parent");
        thread.start();
    }// public

    @Override
    public void run() {
        while(true){
            status = "sleeping";
            Main.monitor.replaceFood();
        }// forever-loop
    }// run
    
    public String getStatus(){
        return status;
    }
        
    private Thread thread;
    // foodReleaseDelay
    private static int foodReleaseDelay;
            
    public String status;
    // counter
    public int repor = 0;
}// ParentBird

