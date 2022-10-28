import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(BlockJUnit4ClassRunner.class)
public class TestSavingAndLoading {

    @Test
    public void testSimpleThing() {
        String expected = "one";
        String actual = "one";
        assertEquals(expected, actual);
    }


}