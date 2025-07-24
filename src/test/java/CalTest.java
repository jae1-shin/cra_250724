import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalTest {
    @Test
    void getSum() {
        Cal cal =  new Cal();
        int actual = cal.getSum(1, 2);
        assertEquals(3, actual);
    }

    @Test
    void getSum2() {
        Cal cal =  mock(Cal.class);
        when( cal.getSum(1, 2)).thenReturn(1);
        int actual = cal.getSum(1, 2);
        assertEquals(1, actual);
    }
}