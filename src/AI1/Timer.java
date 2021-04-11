package AI1;

public class Timer {

    private static long start, end;

    public static void start(){
        start = System.currentTimeMillis();
    }

    public static void end(){
        end = System.currentTimeMillis();
    }

    public static long getDuration(){
        return end - start;
    }

}
