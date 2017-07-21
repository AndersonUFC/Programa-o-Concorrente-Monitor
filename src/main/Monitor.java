package main;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author anderson
 * Monitor: responsável por monitorar 3 funções de 3 classes de processos
 */
public class Monitor {
        
    /*
        função de ter acesso à comida
    */
    public void getFood(int id, int delay){
        
        // garante exclusão mútua
        lock.lock();
        
        // getOut: mantém o processo no loop até que o token o escolha para acessar a
        //          comida
        boolean getOut = false;
        
        try{
            
            // configura o status
            Main.children[id].setStatus("waiting");
            
            while(!getOut){
                // cada processo baby bird fica em espera esperando o signalAll do token
                queueBirds.await();
                
                /* se o token estiver dando a permissão para esse processo e 
                    o parentBird não está repondo comida, então jogue ele pra fora do
                    loop, caso contrário, repita o loop e caia no wait
                */
                if(token == id)
                    getOut = true;
            }// while
            
            // conta o acesso
            Main.children[id].access++;
            
            // remove uma unidade de comida
            foodStorage--;

            // acorda o parentBird para repor comida
            if(foodStorage == 0)
                parentBird.signal();

            // muda status
            Main.children[id].setStatus("eating");
            Main.children[id].eat++;
            
            // se alimenta
            while(Main.children[id].getEnergy() < 100)
                Main.children[id].eat();            
        } catch (InterruptedException e){
            
        } finally{
            lock.unlock();
        }
        

    }// getFood
    
    // replaceFood: procedimento do processo parentBird para repor comida
    public void replaceFood(){
        // exlusão mútua
        lock.lock();
                
        try{
            // fica em espera até um passaro que pegou o ultimo pedaço de comida
            // acordar o parentBird
            parentBird.await();
            
            // incrementa contador
            Main.parent.repor++;
            
            // atualiza status
            Main.parent.status = "awaken";            
            
            // repõe comida
            foodStorage = MAX_FOOD;
        } catch(InterruptedException e){
            
        } finally{
            // destrava lock
            lock.unlock();
        }// TCF      
    }// replaceFood
    
    // tickToken: a cada entrada no monitor, ele passa o token pro proximo processo
    public void tickToken(){
        // exclusão mutua
        lock.lock();
        try{
            token = (token+1)%Main.N;
            queueBirds.signalAll();
        } finally{
            lock.unlock();
        }
    }// tickToken

    // shared locks
    private final Lock lock = new ReentrantLock();
    
    private final Condition queueBirds = lock.newCondition();
    private final Condition parentBird = lock.newCondition();
    
    // shared variables
    private final static int MAX_FOOD = Main.N-1;
        public int getMaxFood(){
            return MAX_FOOD;
        }// getter
    
    private static int foodStorage = MAX_FOOD;
        public int getFoodStorage(){
            return foodStorage;
        }// getter
    
    // tokenPassing
    private static int token = 0;
        public int getTokenPosition(){
            return token;
        }
}// Monitor
