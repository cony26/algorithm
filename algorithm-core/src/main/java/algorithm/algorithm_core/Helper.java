package algorithm.algorithm_core;

public class Helper {
    public static void sleepForMoment(int aTime){
        try{
            Thread.sleep(aTime);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
