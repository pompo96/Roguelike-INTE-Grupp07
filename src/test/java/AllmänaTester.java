import org.junit.jupiter.api.Test;
import utrustning.Utrustning;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AllmänaTester {

    @Test
    public void testUtrustning() {
        Utrustning ut = new Utrustning();
        assertEquals(true, ut.getValue());
    }

}
