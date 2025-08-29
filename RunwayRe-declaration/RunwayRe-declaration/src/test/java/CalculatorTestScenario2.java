import org.junit.jupiter.api.Test;
import uk.ac.soton.comp2211.runwayredeclaration.Calculator.RunwayCalculator;
import uk.ac.soton.comp2211.runwayredeclaration.Component.Obstacle;
import uk.ac.soton.comp2211.runwayredeclaration.Component.SubRunway;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTestScenario2 {

    SubRunway testRunway1 = new SubRunway("27L", 3660, 3660, 3660, 3660, 0, 60, 300, 240);
    SubRunway testRunway2 = new SubRunway("09R", 3660, 3660, 3660, 3353, 307, 60, 300, 240);

    Obstacle testObstacle = new Obstacle("plane", 25, 0,10, 2853);

    @Test
    public void testTORA09R() {
        double tora = RunwayCalculator.calculateTORA(testRunway2, testObstacle, 2853);
        System.out.println("Test tora is: " + tora);
        assertEquals(1850, tora);
    }

    @Test
    public void testTODA09R() {
        double toda = RunwayCalculator.calculateTODA(testRunway2, testObstacle, 2853);
        System.out.println("Test toda is: " + toda);
        assertEquals(1850, toda);
    }

    @Test
    public void testASDA09R() {
        double asda = RunwayCalculator.calculateASDA(testRunway2, testObstacle, 2853);
        System.out.println("Test asda is: " + asda);
        assertEquals(1850, asda);
    }

    @Test
    public void testLDA09R() {
        double lda = RunwayCalculator.calculateLDA(testRunway2, testObstacle, 2853);
        System.out.println("Test lda is: " + lda);
        assertEquals(2553, lda);
    }

    @Test
    public void testTORA27L() {
        double tora = RunwayCalculator.calculateTORA(testRunway1, testObstacle, 500);
        System.out.println("Test tora is: " + tora);
        assertEquals(2860, tora);
    }

    @Test
    public void testTODA27L() {
        double toda = RunwayCalculator.calculateTODA(testRunway1, testObstacle, 500);
        System.out.println("Test toda is: " + toda);
        assertEquals(2860, toda);
    }

    @Test
    public void testASDA27L() {
        double asda = RunwayCalculator.calculateASDA(testRunway1, testObstacle, 500);
        System.out.println("Test asda is: " + asda);
        assertEquals(2860, asda);
    }

    @Test
    public void testLDA27L() {
        double lda = RunwayCalculator.calculateLDA(testRunway1, testObstacle, 500);
        System.out.println("Test lda is: " + lda);
        assertEquals(1850, lda);
    }


}
