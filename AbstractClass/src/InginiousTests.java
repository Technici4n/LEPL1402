package src;

import com.github.guillaumederval.javagrading.Grade;
import com.github.guillaumederval.javagrading.GradeFeedback;
import com.github.guillaumederval.javagrading.GradeFeedbacks;
import com.github.guillaumederval.javagrading.GradingRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.function.Supplier;

import templates.*;

import java.lang.reflect.*;

import static org.junit.Assert.*;

@RunWith(GradingRunner.class) // classic "jail runner" from Guillaume's library
public class InginiousTests {

    // generate random number
    private Supplier<Double> rng = () -> ((Math.random()*100) + 1); // never generate 0

    // Check that Shape is correctly implemented
    @Test
    @Grade
    @GradeFeedbacks({@GradeFeedback(message = "", onSuccess = true),
    @GradeFeedback(message = "Wrong modifiers in your Shape implementation\n", onFail = true, onTimeout = true)})
    public void checkShapeImplementation() {
        Class c = Shape.class;
        int modifier_of_class = c.getModifiers();
        // should be abstract and public
        assertTrue(Modifier.isAbstract(modifier_of_class));
        assertTrue(Modifier.isPublic(modifier_of_class));
        // Now check if they respected given implementation ; will throw exception if not the case
        try {

            Method my_method = c.getDeclaredMethod("getArea", double.class);
            int modifier_of_method = my_method.getModifiers();

            assertTrue(Modifier.isAbstract(modifier_of_method));
            assertTrue(Modifier.isPublic(modifier_of_method));
            assertTrue(my_method.getReturnType().equals(double.class));

            my_method = c.getDeclaredMethod("getPerimeter", double.class);
            modifier_of_method = my_method.getModifiers();

            assertTrue(Modifier.isAbstract(modifier_of_method));
            assertTrue(Modifier.isPublic(modifier_of_method));
            assertTrue(my_method.getReturnType().equals(double.class));

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @Grade
    @GradeFeedbacks({@GradeFeedback(message = "", onSuccess = true),
    @GradeFeedback(message = "Wrong modifiers in your subclasses implementation\n", onFail = true, onTimeout = true)})
    public void checkSubClassImplementation() {
        Class[] class_array = new Class[] {Circle.class, Square.class};

        for(int i = 0; i < class_array.length; i++) {
            int modifier_of_class = class_array[i].getModifiers();
            // should be public only
            assertTrue(modifier_of_class == Modifier.PUBLIC);

            // should extends the Square class
            assertTrue(Shape.class.isAssignableFrom(class_array[i]));

            try {

                Method my_method = class_array[i].getDeclaredMethod("getArea", new Class[]{double.class});
                int modifier_of_method = my_method.getModifiers();

                // should be only public
                assertTrue(modifier_of_method == Modifier.PUBLIC);
                assertTrue(my_method.getReturnType().equals(double.class));

                // should also be only public
                my_method = class_array[i].getDeclaredMethod("getPerimeter", new Class[]{double.class});
                modifier_of_method = my_method.getModifiers();

                assertTrue(modifier_of_method == Modifier.PUBLIC);
                assertTrue(my_method.getReturnType().equals(double.class));
                i++;

            } catch (Exception e) {
                fail();
            }

        }

    }

    @Test
    @Grade(value=1, cpuTimeout=100)
    @GradeFeedbacks({@GradeFeedback(message = "", onSuccess = true),
    @GradeFeedback(message = "Not the expected result for getArea / getPerimeter\n", onFail = true, onTimeout = true)})
    public void testImplementationCorrectness() {
        Circle c = new Circle();
        Square s = new Square();

        for(int i = 0; i < 10; i++) {
            double random_double = rng.get();
            assertEquals(random_double * random_double * Math.PI, c.getArea(random_double), Math.pow(10, -10));
            assertEquals(random_double * random_double, s.getArea(random_double), Math.pow(10, -10));
            assertEquals(Math.PI * 2 * random_double, c.getPerimeter(random_double), Math.pow(10, -10));
            assertEquals(random_double * 4, s.getPerimeter(random_double), Math.pow(10, -10));
        }
    }

}
