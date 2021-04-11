package AI1;

public class Apple {

    private static Point appleCoordinates;

    public static void setAppleCoordinates(String sAppleCoordinates){
        String[] coords = sAppleCoordinates.split(" ");
        int ax = Integer.parseInt(coords[0]);
        int ay = Integer.parseInt(coords[1]);
        appleCoordinates = new Point(ax, ay);
    }

    public static Point getAppleCoordinates(){
        return appleCoordinates;
    }


}
