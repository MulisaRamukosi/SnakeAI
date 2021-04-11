package AI1;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Algorithms {

    static int distance(Point A, Point B){
        return Math.abs(B.getX() - A.getX()) + Math.abs(B.getY() - A.getY());
    }

    public static int trueDistanceEstimate(Point A, Point B){

        for (Line line : GameBoard.getLines()){
            if (doIntersect(A, B, line.getPoint0(), line.getPoint1())){
                Point intersectionPoint = lineLineIntersection(A, B, line.getPoint0(), line.getPoint1());
                if (intersectionPoint == null) continue;
                //get lowest distance between end points of the line that's in the way
                int lowestDistanceToCoverFirst = Math.min(distance(intersectionPoint, line.getPoint0()), distance(intersectionPoint, line.getPoint1()));
                return distance(A, B) + 2 * lowestDistanceToCoverFirst;
            }
        }

        return distance(A, B);
    }

    private static Point lineLineIntersection(Point A, Point B, Point C, Point D)
    {
        // Line AB represented as a1x + b1y = c1
        double a1 = B.getY() - A.getY();
        double b1 = A.getX() - B.getX();
        double c1 = a1*(A.getX()) + b1*(A.getY());

        // Line CD represented as a2x + b2y = c2
        double a2 = D.getY() - C.getY();
        double b2 = C.getX() - D.getX();
        double c2 = a2*(C.getX())+ b2*(C.getY());

        double determinant = a1*b2 - a2*b1;

        if (determinant == 0)
        {
            return null;
        }
        else
        {
            int x = (int) ((b2*c1 - b1*c2)/determinant);
            int y = (int) ((a1*c2 - a2*c1)/determinant);
            return new Point(x, y);
        }
    }

    private synchronized static void GHOST_OUT(int n){
        SnakeManager.ghostMeOut(n);
    }

    private synchronized static void GHOST_IN(){
        SnakeManager.ghostMeIn();
    }

    static synchronized ArrayList<Point> generatePathToGoal(Point start, Point end){

        int[][] closedList = GameBoard.generateEmptyBoard();
        ArrayList<Point> openListSide = new ArrayList<>();

        PriorityQueue<Point> openList = new PriorityQueue<>((o1, o2) -> (o1.getF() == o2.getF()) ? o1.getH() - o2.getH() : o1.getF() - o2.getF());

        openList.add(start);
        openListSide.add(start);

        while (!openList.isEmpty()){
            Point parent = openList.remove();
            openListSide.remove(parent);

            GHOST_OUT(parent.getPosInLink());

            closedList[parent.getY()][parent.getX()] = 1;

            for (Point child : parent.getChildren()){
                if (child == null || closedList[child.getY()][child.getX()] == 1) continue;

                if (openList.contains(child)){
                    Point childAlreadyInList = openListSide.get(openListSide.indexOf(child));

                    if (childAlreadyInList.getG() > parent.getG() + 1//newGCost
                    ){
                        openListSide.remove(childAlreadyInList);
                        openList.remove(childAlreadyInList);
                        child.setParent(parent);
                        child.setH(trueDistanceEstimate(child, end));
                        child.setG(parent.getG() + 1);
                        child.setPosInLink(parent.getPosInLink() + 1);
                        openList.add(child);
                        openListSide.add(child);
                    }
                }
                else{
                    child.setParent(parent);
                    if (child.equals(end)) {
                        GHOST_IN();
                        return pathToGoal(child, start);
                    }
                    child.setH(trueDistanceEstimate(child, end));
                    child.setPosInLink(parent.getPosInLink() + 1);
                    child.setG(parent.getG() + 1);
                    openList.add(child);
                    openListSide.add(child);
                }

            }


            GHOST_IN();
        }

        return new ArrayList<>();
    }

    private static ArrayList<Point> pathToGoal(Point child, Point start) {
        ArrayList<Point> path = new ArrayList<>();
        path.add(child);
        child = child.getParent();
        while (!child.equals(start)){
            path.add(child);
            child = child.getParent();
        }
        return path;
    }

    public static ArrayList<Point> maxStall(Point start, Point end) {
        Point thisAlt = null;


        int[][] closedList = GameBoard.generateEmptyBoard();
        ArrayList<Point> openListSide = new ArrayList<>();

        PriorityQueue<Point> openList = new PriorityQueue<>((o1, o2) -> (o1.getF() == o2.getF()) ? o2.getH() - o1.getH() : o2.getF() - o1.getF());

        int longestDistance = 0;
        Point longestPathNode = null;

        openList.add(start);
        openListSide.add(start);
        closedList[Apple.getAppleCoordinates().getY()][Apple.getAppleCoordinates().getX()] = 1;

        while (!openList.isEmpty()){
            Point parent = openList.remove();
            openListSide.remove(parent);

            if (parent.getPosInLink() > SnakeManager.getMyLength() + 1) {
                MyAgent.log("MAX STALL SUCCESS, LONGEST PATH");
                GHOST_IN();
                return pathToGoal(parent, start);
            }

            if (parent.equals(end)){
                if (parent.equals(SnakeManager.getMyRealTail())) thisAlt = parent;

                else{
                    MyAgent.log("MAX STALL SUCCESS, SAFE TAIL");
                    GHOST_IN();
                    return pathToGoal(parent, start);
                }
            }

            if (parent.getPosInLink() > longestDistance){
                longestDistance = parent.getPosInLink();
                longestPathNode = parent;
            }


            GHOST_OUT(parent.getPosInLink());

            closedList[parent.getY()][parent.getX()] = 1;

            for (Point child : parent.getChildren()){
                if (child == null || closedList[child.getY()][child.getX()] == 1) continue;

                if (openList.contains(child)){
                    Point childAlreadyInList = openListSide.get(openListSide.indexOf(child));
                    int newGCost = parent.getG() + 1;
                    if (parent.getPosInLink() + 1 > child.getPosInLink()){
                        openListSide.remove(childAlreadyInList);
                        openList.remove(childAlreadyInList);
                        child.setParent(parent);
                        child.setH(trueDistanceEstimate(child, end));
                        child.setG(newGCost);
                        child.setPosInLink(parent.getPosInLink() + 1);
                        openList.add(child);
                        openListSide.add(child);
                    }
                }
                else{
                    child.setParent(parent);
                    child.setH(trueDistanceEstimate(child, end));
                    child.setPosInLink(parent.getPosInLink() + 1);
                    child.setG(parent.getG() + 1);
                    openList.add(child);
                    openListSide.add(child);
                }

            }

            GHOST_IN();
        }

        if (thisAlt != null){
            MyAgent.log("MAX STALL SUCCESS, REAL TAIL");
            return pathToGoal(thisAlt, start);
        }

        if (longestPathNode != null){
            MyAgent.log("max stall ONLY! did great things");
            return pathToGoal(longestPathNode, start);
        }

        return new ArrayList<>();
    }

    // The main function that returns true if line segment 'p1q1'
// and 'p2q2' intersect.
    private static boolean doIntersect(Point p1, Point q1, Point p2, Point q2)
    {
        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;

        // p1, q1 and q2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2

        return o4 == 0 && onSegment(p2, q1, q2);// Doesn't fall in any of the above cases
    }

    // Given three colinear points p, q, r, the function checks if
// point q lies on line segment 'pr'
    private static boolean onSegment(Point p, Point q, Point r)
    {
        return q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX()) &&
                q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY());
    }

    // To find orientation of ordered triplet (p, q, r).
// The function returns following values
// 0 --> p, q and r are colinear
// 1 --> Clockwise
// 2 --> Counterclockwise
    private static int orientation(Point p, Point q, Point r)
    {
        // See https://www.geeksforgeeks.org/orientation-3-ordered-points/
        // for details of below formula.
        int val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0) return 0; // colinear

        return (val > 0)? 1: 2; // clock or counterclock wise
    }

}
