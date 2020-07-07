package algorithm.algorithm_core;

import java.util.Date;
import java.util.Objects;

/**
 * Hello world!
 *
 */
public class App 
{
    private static int a;
    public static void main( String[] args )
    {
       Date start = new Date();
       Date end = new Date();
       Period p = new Period(start, end);
       end.setYear(78);

       p.end().toString();
    }


}

final class Period{
    private final Date start;
    private final Date end;

    public Period(Date start, Date end){
        this.start = start;
        this.end = end;
    }
    public Date start(){
        return start;
    }
    public Date end(){
        return end;
    }

}
