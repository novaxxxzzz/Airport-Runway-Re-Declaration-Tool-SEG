import org.junit.jupiter.api.Test;
import uk.ac.soton.comp2211.runwayredeclaration.Calculator.RunwayCalculator;
import uk.ac.soton.comp2211.runwayredeclaration.Component.Obstacle;
import uk.ac.soton.comp2211.runwayredeclaration.Component.SubRunway;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTestScenario3 {


    SubRunway testRunway1 = new SubRunway("27L", 3660, 3660, 3660, 3660, 0, 60, 300, 240);
    SubRunway testRunway2 = new SubRunway("09R", 3660, 3660, 3660, 3353, 307, 60, 300, 240);

    Obstacle testObstacle = new Obstacle("plane", 15,0, 1000, 150);


    @Test
    public void testTORA09R() {
        double tora = RunwayCalculator.calculateTORA(testRunway2, testObstacle, 150);
        System.out.println("Test tora is: " + tora);
        assertEquals(2903, tora);
    }


    @Test
    public void testTODA09R() {
        double toda = RunwayCalculator.calculateTODA(testRunway2, testObstacle, 150);
        System.out.println("Test toda is: " + toda);
        assertEquals(2903, toda);
    }

    @Test
    public void testASDA09R() {
        double asda = RunwayCalculator.calculateASDA(testRunway2, testObstacle, 150);
        System.out.println("Test asda is: " + asda);
        assertEquals(2903, asda);
    }

    @Test
    public void testLDA09R() {
        double lda = RunwayCalculator.calculateLDA(testRunway2, testObstacle, 150);
        System.out.println("Test lda is: " + lda);
        assertEquals(2393, lda);
    }

    @Test
    public void testTORA27L() {
        double tora = RunwayCalculator.calculateTORA(testRunway1, testObstacle, 3203);
        System.out.println("Test tora is: " + tora);
        assertEquals(2393, tora);
    }

    @Test
    public void testTODA27L() {
        double toda = RunwayCalculator.calculateTODA(testRunway1, testObstacle, 3203);
        System.out.println("Test toda is: " + toda);
        assertEquals(2393, toda);
    }

    @Test
    public void testASDA27L() {
        double asda = RunwayCalculator.calculateASDA(testRunway1, testObstacle, 3203);
        System.out.println("Test asda is: " + asda);
        assertEquals(2393, asda);
    }

    @Test
    public void testLDA27L() {
        double lda = RunwayCalculator.calculateLDA(testRunway1, testObstacle, 3203);
        System.out.println("Test lda is: " + lda);
        assertEquals(2903, lda);
    }



}
