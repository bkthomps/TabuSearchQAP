import java.util.ArrayList;
import java.util.List;

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


    private final int[][] departmentLocations = {
            {1, 2, 3, 4, 5},
            {6, 7, 8, 9, 10},
            {11, 12, 13, 14, 15},
            {16, 17, 18, 19, 20}
    };

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
    }

    private void vanillaTabu() {
        doLogic(5);
    }

    private void doLogic(int tabuSize) {
        int iterations = 500;
        int currentCost = calculateCost();
        for (int i = 0; i < iterations; i++) {
            var candidates = generateCandidates(currentCost, tabuSize);
            var bestCandidate = candidates.get(candidates.size() - 1);
            var firstDepartment = bestCandidate.firstDepartment;
            var secondDepartment = bestCandidate.secondDepartment;
            int y1 = -1;
            int x1 = -1;
            int y2 = -1;
            int x2 = -1;
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    if (departmentLocations[y][x] == firstDepartment) {
                        y1 = y;
                        x1 = x;
                    }
                    if (departmentLocations[y][x] == secondDepartment) {
                        y2 = y;
                        x2 = x;
                    }
                }
            }
            int tempDepartment = departmentLocations[y1][x1];
            departmentLocations[y1][x1] = departmentLocations[y2][x2];
            departmentLocations[y2][x2] = tempDepartment;
            currentCost = calculateCost();
            decrementTabu();
        }
        System.out.println("Cost = " + currentCost);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int number = departmentLocations[y][x];
                if (number < 10) {
                    System.out.print(" ");
                }
                System.out.print(departmentLocations[y][x] + " ");
            }
            System.out.println();
        }
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

    private List<Candidate> generateCandidates(int currentCost, int tabuSize) {
        var candidates = new ArrayList<Candidate>();
        for (int y1 = 0; y1 < HEIGHT; y1++) {
            for (int x1 = 0; x1 < WIDTH; x1++) {
                for (int y2 = 0; y2 < HEIGHT; y2++) {
                    for (int x2 = 0; x2 < WIDTH; x2++) {
                        int firstDepartment = departmentLocations[y1][x1];
                        int secondDepartment = departmentLocations[y2][x2];
                        if (firstDepartment < secondDepartment) {
                            // Temporarily swap the departments
                            int temp = departmentLocations[y1][x1];
                            departmentLocations[y1][x1] = departmentLocations[y2][x2];
                            departmentLocations[y2][x2] = temp;
                            // Get the cost
                            int cost = calculateCost();
                            int value = cost - currentCost;
                            // Swap them back
                            temp = departmentLocations[y1][x1];
                            departmentLocations[y1][x1] = departmentLocations[y2][x2];
                            departmentLocations[y2][x2] = temp;
                            boolean satisfies = candidates.isEmpty() || value + tabuCountDepartment[secondDepartment - 1][firstDepartment - 1] < candidates.get(candidates.size() - 1).value + tabuCountDepartment[candidates.get(candidates.size() - 1).secondDepartment - 1][candidates.get(candidates.size() - 1).firstDepartment - 1];
                            if (tabuCountDepartment[firstDepartment - 1][secondDepartment - 1] == 0 && satisfies) {
                                var candidate = new Candidate(firstDepartment, secondDepartment, value, cost);
                                candidates.add(candidate);
                                tabuCountDepartment[firstDepartment - 1][secondDepartment - 1] = tabuSize;
                            }
                        }
                    }
                }
            }
        }
        return candidates;
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
}
