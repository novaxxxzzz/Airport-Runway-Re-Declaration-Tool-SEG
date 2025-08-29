import org.junit.jupiter.api.Test;
import uk.ac.soton.comp2211.runwayredeclaration.Calculator.RunwayCalculator;
import uk.ac.soton.comp2211.runwayredeclaration.Component.Obstacle;
import uk.ac.soton.comp2211.runwayredeclaration.Component.SubRunway;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTestScenario4 {

    SubRunway testRunway1 = new SubRunway("09L",3902,3902,3902,3595,306,60,300,240);
    SubRunway testRunway2 = new SubRunway("27R",3884,3962,3884,3884,0,60,300,240);
    Obstacle testObstacle = new Obstacle("plane", 20, 0,3456, 10);


    @Test
    public void testTORA09L() {
        double tora = RunwayCalculator.calculateTORA(testRunway1, testObstacle, 3546);
        System.out.println("Test tora is: " + tora);
        assertEquals(2792, tora);
    }

    @Test
    public void testTODA09L() {
        double toda = RunwayCalculator.calculateTODA(testRunway1, testObstacle, 3546);
        System.out.println("Test toda is: " + toda);
        assertEquals(2792, toda);
    }

    @Test
    public void testASDA09L() {
        double asda = RunwayCalculator.calculateASDA(testRunway1, testObstacle, 3546);
        System.out.println("Test asda is: " + asda);
        assertEquals(2792, asda);
    }

    @Test
    public void testLDA09L() {
        double lda = RunwayCalculator.calculateLDA(testRunway1, testObstacle, 3546);
        System.out.println("Test lda is: " + lda);
        assertEquals(3246, lda);
    }

    @Test
    public void testTORA27R() {
        double tora = RunwayCalculator.calculateTORA(testRunway2, testObstacle, 50);
        System.out.println("Test tora is: " + tora);
        assertEquals(3534, tora);
    }

    @Test
    public void testTODA27R() {
        double toda = RunwayCalculator.calculateTODA(testRunway2, testObstacle, 50);
        System.out.println("Test toda is: " + toda);
        assertEquals(3612, toda);
    }

    @Test
    public void testASDA27R() {
        double asda = RunwayCalculator.calculateASDA(testRunway2, testObstacle, 50);
        System.out.println("Test asda is: " + asda);
        assertEquals(3534, asda);
    }

    @Test
    public void testLDA27R() {
        double lda = RunwayCalculator.calculateLDA(testRunway2, testObstacle, 50);
        System.out.println("Test lda is: " + lda);
        assertEquals(2774, lda);
    }
}
