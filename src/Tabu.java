import java.awt.Point;
import java.util.*;

public class Tabu {

    private static final int HEIGHT = 4;
    private static final int WIDTH = 5;

    private final int[][] flowDepartment = {
            {0, 0, 5, 0, 5, 2, 10, 3, 1, 5, 5, 5, 0, 0, 5, 4, 4, 0, 0, 1},
            {0, 0, 3, 10, 5, 1, 5, 1, 2, 4, 2, 5, 0, 10, 10, 3, 0, 5, 10, 5},
            {5, 3, 0, 2, 0, 5, 2, 4, 4, 5, 0, 0, 0, 5, 1, 0, 0, 5, 0, 0},
            {0, 10, 2, 0, 1, 0, 5, 2, 1, 0, 10, 2, 2, 0, 2, 1, 5, 2, 5, 5},
            {5, 5, 0, 1, 0, 5, 6, 5, 2, 5, 2, 0, 5, 1, 1, 1, 5, 2, 5, 1},
            {2, 1, 5, 0, 5, 0, 5, 2, 1, 6, 0, 0, 10, 0, 2, 0, 1, 0, 1, 5},
            {10, 5, 2, 5, 6, 5, 0, 0, 0, 0, 5, 10, 2, 2, 5, 1, 2, 1, 0, 10},
            {3, 1, 4, 2, 5, 2, 0, 0, 1, 1, 10, 10, 2, 0, 10, 2, 5, 2, 2, 10},
            {1, 2, 4, 1, 2, 1, 0, 1, 0, 2, 0, 3, 5, 5, 0, 5, 0, 0, 0, 2},
            {5, 4, 5, 0, 5, 6, 0, 1, 2, 0, 5, 5, 0, 5, 1, 0, 0, 5, 5, 2},
            {5, 2, 0, 10, 2, 0, 5, 10, 0, 5, 0, 5, 2, 5, 1, 10, 0, 2, 2, 5},
            {5, 5, 0, 2, 0, 0, 10, 10, 3, 5, 5, 0, 2, 10, 5, 0, 1, 1, 2, 5},
            {0, 0, 0, 2, 5, 10, 2, 2, 5, 0, 2, 2, 0, 2, 2, 1, 0, 0, 0, 5},
            {0, 10, 5, 0, 1, 0, 2, 0, 5, 5, 5, 10, 2, 0, 5, 5, 1, 5, 5, 0},
            {5, 10, 1, 2, 1, 2, 5, 10, 0, 1, 1, 5, 2, 5, 0, 3, 0, 5, 10, 10},
            {4, 3, 0, 1, 1, 0, 1, 2, 5, 0, 10, 0, 1, 5, 3, 0, 0, 0, 2, 0},
            {4, 0, 0, 5, 5, 1, 2, 5, 0, 0, 0, 1, 0, 1, 0, 0, 0, 5, 2, 0},
            {0, 5, 5, 2, 2, 0, 1, 2, 0, 5, 2, 1, 0, 5, 5, 0, 5, 0, 1, 1},
            {0, 10, 0, 5, 5, 1, 0, 2, 0, 5, 2, 2, 0, 5, 10, 2, 2, 1, 0, 6},
            {1, 5, 0, 5, 1, 5, 10, 10, 2, 2, 5, 5, 5, 0, 10, 0, 0, 1, 6, 0}
    };

    private final int[][] tabuCountDepartment = new int[HEIGHT * WIDTH][HEIGHT * WIDTH];

    private int[][] departmentLocations;

    private static class Candidate {
        private int firstDepartment;
        private int secondDepartment;
        private int value;
        private int cost;

        Candidate(int firstDepartment, int secondDepartment, int value, int cost) {
            this.firstDepartment = firstDepartment;
            this.secondDepartment = secondDepartment;
            this.value = value;
            this.cost = cost;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Candidate)) {
                return false;
            }
            var candidate = (Candidate) o;
            return firstDepartment == candidate.firstDepartment
                    && secondDepartment == candidate.secondDepartment
                    && cost == candidate.cost;
        }
    }

    public static void main(String[] args) {
        var tabu = new Tabu();
        tabu.vanillaTabu();
        var best = tabu.changingStarting();
        tabu.changeTabuListSize(best);
        tabu.dynamicListSize();
        tabu.aspiration();
        tabu.partialNeighborhood();
        tabu.frequencyBased();
    }

    private void vanillaTabu() {
        departmentLocations = new int[][]{
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 13, 14, 15},
                {16, 17, 18, 19, 20}
        };
        int cost = doLogic(5, 5, false, false, false);
        System.out.println("Vanilla cost = " + cost);
        System.out.println();
    }

    private int[][] changingStarting() {
        int bestCost = Integer.MAX_VALUE;
        int[][] best = departmentLocations;
        for (int i = 0; i < 30; i++) {
            Integer[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
            var list = new ArrayList<>(Arrays.asList(arr));
            Collections.shuffle(list);
            for (int j = 0; j < arr.length; j++) {
                departmentLocations[j / WIDTH][j % WIDTH] = list.get(j);
            }
            var backup = departmentLocations.clone();
            int cost = doLogic(5, 5, false, false, false);
            System.out.println("Random initial starting cost = " + cost);
            if (cost < bestCost) {
                bestCost = cost;
                best = backup;
            }
        }
        System.out.println("The best cost is " + bestCost + ", with grid: ");
        printGrid(best);
        System.out.println();
        return best;
    }

    private void changeTabuListSize(int[][] best) {
        departmentLocations = best;
        int minCost = Integer.MAX_VALUE;
        int listSize = -1;
        for (int i = 1; i < 20; i++) {
            int cost = doLogic(i, i, false, false, false);
            if (cost < minCost) {
                minCost = cost;
                listSize = i;
            }
            System.out.println("List size = " + i + ", cost = " + cost);
        }
        System.out.println("Best list size is " + listSize + ", with cost " + minCost);
        System.out.println();
    }

    private void dynamicListSize() {
        int cost = doLogic(3, 7, false, false, false);
        System.out.println("With a dynamic list size the cost is " + cost);
        System.out.println();
    }

    private void aspiration() {
        int cost = doLogic(3, 7, true, false, false);
        System.out.println("With aspiration the cost is " + cost);
        System.out.println();
    }

    private void partialNeighborhood() {
        int cost = doLogic(3, 7, true, true, false);
        System.out.println("With partial neighborhood the cost is " + cost);
        System.out.println();
    }

    private void frequencyBased() {
        int cost = doLogic(3, 7, true, false, true);
        System.out.println("With frequency based the cost is " + cost);
        System.out.println();
    }

    private int doLogic(int tabuMinSize, int tabuMaxSize, boolean isAspiration, boolean partialNeighborhood,
                        boolean isFrequency) {
        int iterations = 500;
        int currentCost = calculateCost();
        var bestCandidate = isAspiration ? new Candidate(0, 0, 0, Integer.MAX_VALUE) : null;
        for (int i = 0; i < iterations; i++) {
            int tabuSize = tabuMinSize;
            if (tabuMinSize != tabuMaxSize && i % 25 == 0) {
                tabuSize = tabuMinSize + (int) ((tabuMaxSize - tabuMinSize + 1) * Math.random());
            }
            var candidates = generateCandidates(currentCost, tabuSize, bestCandidate, partialNeighborhood, isFrequency);
            var currentCandidate = candidates.get(candidates.size() - 1);
            if (isAspiration && currentCandidate.cost < bestCandidate.cost) {
                bestCandidate = currentCandidate;
            }
            var locationOne = localizeDepartment(currentCandidate.firstDepartment);
            var locationTwo = localizeDepartment(currentCandidate.secondDepartment);
            swapLocation(locationOne, locationTwo);
            currentCost = currentCandidate.cost;
            decrementTabu();
        }
        return currentCost;
    }

    private int calculateCost() {
        int cost = 0;
        for (int y1 = 0; y1 < HEIGHT; y1++) {
            for (int x1 = 0; x1 < WIDTH; x1++) {
                for (int y2 = 0; y2 < HEIGHT; y2++) {
                    for (int x2 = 0; x2 < WIDTH; x2++) {
                        int firstDepartment = departmentLocations[y1][x1];
                        int secondDepartment = departmentLocations[y2][x2];
                        if (firstDepartment < secondDepartment) {
                            int manhattanDistance = Math.abs(x1 - x2) + Math.abs(y1 - y2);
                            int flow = flowDepartment[firstDepartment - 1][secondDepartment - 1];
                            cost += manhattanDistance * flow;
                        }
                    }
                }
            }
        }
        return cost;
    }

    private List<Candidate> generateCandidates(int currentCost, int tabuSize, Candidate best,
                                               boolean partialNeighborhood, boolean isFrequency) {
        var candidates = new ArrayList<Candidate>();
        for (int y1 = 0; y1 < HEIGHT; y1++) {
            for (int x1 = 0; x1 < WIDTH; x1++) {
                for (int y2 = 0; y2 < (partialNeighborhood ? HEIGHT * Math.random() : HEIGHT); y2++) {
                    for (int x2 = 0; x2 < (partialNeighborhood ? WIDTH * Math.random() : WIDTH); x2++) {
                        int firstDepartment = departmentLocations[y1][x1];
                        int secondDepartment = departmentLocations[y2][x2];
                        if (firstDepartment < secondDepartment) {
                            var one = new Point(x1, y1);
                            var two = new Point(x2, y2);
                            swapLocation(one, two);
                            int cost = calculateCost();
                            int value = cost - currentCost;
                            swapLocation(one, two);
                            var candidate = new Candidate(firstDepartment, secondDepartment, value, cost);
                            int recency = tabuCountDepartment[firstDepartment - 1][secondDepartment - 1];
                            boolean satisfies = candidates.isEmpty();
                            if (!satisfies) {
                                int frequency = tabuCountDepartment[secondDepartment - 1][firstDepartment - 1];
                                var last = candidates.get(candidates.size() - 1);
                                int candidateFrequency =
                                        tabuCountDepartment[last.secondDepartment - 1][last.firstDepartment - 1];
                                satisfies = value + frequency < last.value + candidateFrequency;
                            }
                            if ((recency == 0 && satisfies) || (recency < 5 && candidate.equals(best))) {
                                candidates.add(candidate);
                                tabuCountDepartment[firstDepartment - 1][secondDepartment - 1] = tabuSize + 1;
                                if (isFrequency) {
                                    tabuCountDepartment[secondDepartment - 1][firstDepartment - 1] += 2;
                                }
                            }
                        }
                    }
                }
            }
        }
        return candidates;
    }

    private void swapLocation(Point one, Point two) {
        int temp = departmentLocations[one.y][one.x];
        departmentLocations[one.y][one.x] = departmentLocations[two.y][two.x];
        departmentLocations[two.y][two.x] = temp;
    }

    private Point localizeDepartment(int department) {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (departmentLocations[y][x] == department) {
                    return new Point(x, y);
                }
            }
        }
        throw new IllegalStateException("Department must exist");
    }

    private void decrementTabu() {
        for (int y = 0; y < HEIGHT * WIDTH; y++) {
            for (int x = 0; x < HEIGHT * WIDTH; x++) {
                if (tabuCountDepartment[y][x] > 0) {
                    tabuCountDepartment[y][x]--;
                }
            }
        }
    }

    private void printGrid(int[][] locations) {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int number = locations[y][x];
                if (number < 10) {
                    System.out.print(" ");
                }
                System.out.print(locations[y][x] + " ");
            }
            System.out.println();
        }
    }
}
