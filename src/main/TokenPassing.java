package main;

public class TokenPassing implements Runnable{
    
    public TokenPassing(){
        thread = new Thread(this, "token");
        thread.start();
    }// constructor
    
    @Override
    public void run(){
        while(true){
            Main.monitor.tickToken();
        }//run
    }// run
    
    private Thread thread;
}
