package algorithm.algorithm_core;

public interface Processor {
    public default void launch(){
        while(isContinue()){
            process();
        }
    };
    public boolean isContinue();
    public void process();
}
