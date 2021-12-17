package com.company;
class TickTock{
    String state; // przechowuje stan zegara
    synchronized void tick(boolean running){
        if(!running){ // zatrzymanie zegara
            state = "ticked";
            notify(); // powiadamia oczekiwujący wątek
            return; }
        System.out.println("tick ");
        state = "ticked"; // zmienia stan zegara na ticked
        notify(); // umożliwia wykonanie metody tock() || metoda tick() powiadamia metodę tock()
        try{
            Thread.sleep(500);
            while (!state.equals("tocked"))
                wait(); // oczekuje na zakończenie metody tock()
        } catch (InterruptedException e) {
            System.out.println("Wątek został przerwany.");}}
    synchronized void tock(boolean running) {
        if (!running) { // zatrzymuje zegar
            state = "tocked";
            notify(); // powiadamia oczekujący wątek
            return; }
        System.out.println("tock");
        state = "tocked"; // zmienia stan zegara na tocked
        notify(); // umożłiwia wykonanie metody tick()
        try {
            Thread.sleep(500);
            while (!state.equals("ticked"))
                wait(); // oczekuje na zakończenie metody tick()
        } catch (InterruptedException e) {
            System.out.println("Wątek został przerwany");
        }
    }
}
class MyThreadClock implements Runnable{
    Thread thread;
    TickTock tt0b;
    //tworzymy nowy wątek
    MyThreadClock(String name, TickTock tt){
        thread = new Thread(this, name);
        tt0b = tt;
    }
    //metoda wywoławcza (tworzy i uruchamia)
    public static MyThreadClock createAndStart(String name, TickTock tt){
        MyThreadClock myThreadClock = new MyThreadClock(name, tt);
        myThreadClock.thread.start();
        return myThreadClock;
    }
    public void run(){
        if(thread.getName().compareTo("tick") == 0){
            for(int i = 0; i < 5; i++) tt0b.tick(true);
            tt0b.tick(false);
        }else{
            for(int i = 0; i < 5; i++) tt0b.tock(true);
            tt0b.tock(false);
        }
    }
}
public class Main {

    public static void main(String[] args) {
	// write your code here
        TickTock tt = new TickTock();
        MyThreadClock myThreadClock1 = MyThreadClock.createAndStart("tick", tt);
        MyThreadClock myThreadClock2 = MyThreadClock.createAndStart("tock", tt);
        try{
            myThreadClock1.thread.join();
            myThreadClock2.thread.join();
        } catch (InterruptedException e) {
            System.out.println("Wątek główny został przerwany");
        }
    }
}
