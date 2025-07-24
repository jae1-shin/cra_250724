import org.junit.jupiter.api.*;
import org.mockito.NotExtensible;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssembleTest {
    private Assemble assemble;
    private Assemble assembleSpy;


    @BeforeEach
    void setUp() {
        assemble = new Assemble();
        assembleSpy = spy(new Assemble());
    }

    @Test
    void 단순_모음_익셉션_발생_안_하면_된다() {
        assertDoesNotThrow(() -> assemble.clearScreen());
        assertDoesNotThrow(() -> assemble.showCarTypeMenu());
        assertDoesNotThrow(() -> assemble.showEngineMenu());
        assertDoesNotThrow(() -> assemble.showBrakeSystemMenu());
        assertDoesNotThrow(() -> assemble.showSteeringSystemMenu());
        assertDoesNotThrow(() -> assemble.showRunOrTestMenu());
        assertDoesNotThrow(() -> assemble.delayWhenError());
        assertDoesNotThrow(() -> assemble.delayForWhile());
    }

    @Test
    void initStep_시작은_CAR_TYPE부터() {
        assertEquals(assemble.ASK_CAR_TYPE, assemble.initStep());
    }

    @Test
    void needsToBeHalted_exit_이면_true를_반환한다() {
        assertTrue(assemble.needsToBeHalted("exit"));
    }

    @Test
    void needsToBeHalted_다른_입력은_false를_반환한다() {
        assertFalse(assemble.needsToBeHalted("0"));
    }

    @Test
    void convertRawInputToInt_정상적인_입력은_정수로_변환한다() {
        assertEquals(1, assemble.convertRawInputToInt("1"));
        assertEquals(2, assemble.convertRawInputToInt("2"));
    }

    @Test
    void convertRawInputToInt_비정상적인_입력은_예외를_발생시킨다() {
        assertThrows(NumberFormatException.class, () -> assemble.convertRawInputToInt("abc"));
        assertThrows(NumberFormatException.class, () -> assemble.convertRawInputToInt("1.5"));
        assertThrows(NumberFormatException.class, () -> assemble.convertRawInputToInt(""));
    }

    @Test
    void isRunOrTestStep_정상_판별_확인한다(){
        assertTrue(assemble.isRunOrTestStep(assemble.ASK_RUN_OR_TEST));
        assertFalse(assemble.isRunOrTestStep(assemble.ASK_CAR_TYPE));
    }

    @Test
    void isValidRange_정상적인_범위는_true를_반환한다() {
        assertTrue(assemble.isValidRange(0, 1));
        assertTrue(assemble.isValidRange(1, 0));
        assertTrue(assemble.isValidRange(2, 3));
        assertTrue(assemble.isValidRange(3, 2));
        assertTrue(assemble.isValidRange(4, 1));
    }

    @Test
    void isValidRange_비정상적인_범위는_false를_반환한다() {
        assertFalse(assemble.isValidRange(0, 0));
        assertFalse(assemble.isValidRange(1, 5));
        assertFalse(assemble.isValidRange(2, 4));
        assertFalse(assemble.isValidRange(3, 3));
        assertFalse(assemble.isValidRange(4, 3));
    }

    @Test
    void getOptionsForStep_비정상적인_범위는_예외를_발생시킨다() {
        assertThrows(IllegalStateException.class, () -> assemble.isValidRange(100, 0));
    }

    @Test
    void selectCarType_carSpec이_정상적으로_설정된다() {
        assemble.selectCarType(1);
        assertEquals(1, assemble.carSpec[assemble.ASK_CAR_TYPE]);

        assemble.selectCarType(2);
        assertEquals(2, assemble.carSpec[assemble.ASK_CAR_TYPE]);

        assemble.selectCarType(3);
        assertEquals(3, assemble.carSpec[assemble.ASK_CAR_TYPE]);
    }

    @Test
    void selectEngine_carSpec이_정상적으로_설정된다() {
        assemble.selectEngine(1);
        assertEquals(1, assemble.carSpec[assemble.ASK_ENGINE]);

        assemble.selectEngine(2);
        assertEquals(2, assemble.carSpec[assemble.ASK_ENGINE]);

        assemble.selectEngine(3);
        assertEquals(3, assemble.carSpec[assemble.ASK_ENGINE]);

        assemble.selectEngine(4);
        assertEquals(4, assemble.carSpec[assemble.ASK_ENGINE]);
    }

    @Test
    void selectBrakeSystem_carSpec이_정상적으로_설정된다() {
        assemble.selectBrakeSystem(1);
        assertEquals(1, assemble.carSpec[assemble.ASK_BREAK_SYSTEM]);

        assemble.selectBrakeSystem(2);
        assertEquals(2, assemble.carSpec[assemble.ASK_BREAK_SYSTEM]);

        assemble.selectBrakeSystem(3);
        assertEquals(3, assemble.carSpec[assemble.ASK_BREAK_SYSTEM]);
    }

    @Test
    void selectSteeringSystem_carSpec이_정상적으로_설정된다() {
        assemble.selectSteeringSystem(1);
        assertEquals(1, assemble.carSpec[assemble.ASK_STEERING_SYSTEM]);

        assemble.selectSteeringSystem(2);
        assertEquals(2, assemble.carSpec[assemble.ASK_STEERING_SYSTEM]);
    }

    @Test
    void getNextStep_다음_거_잘_찾는지_확인() {
        assertEquals(assemble.ASK_ENGINE, assemble.getNextStep(assemble.ASK_CAR_TYPE));
        assertEquals(assemble.ASK_BREAK_SYSTEM, assemble.getNextStep(assemble.ASK_ENGINE));
        assertEquals(assemble.ASK_STEERING_SYSTEM, assemble.getNextStep(assemble.ASK_BREAK_SYSTEM));
        assertEquals(assemble.ASK_RUN_OR_TEST, assemble.getNextStep(assemble.ASK_STEERING_SYSTEM));
    }

    @Test
    void getNextStep_정의되지_않으면_예외를_발생시킨다() {
        assertThrows(IllegalStateException.class, () -> assemble.getNextStep(assemble.ASK_RUN_OR_TEST));
    }

    @Test
    void isToBeTested_TEST_숫자_선택_때만_true_반환한다() {
        assertTrue(assemble.isToBeRun(assemble.RUN));
        assertFalse(assemble.isToBeRun(assemble.TEST));
    }

    @Test
    void isToBeRun_RUN_숫자_선택_때만_true_반환한다() {
        assertTrue(assemble.isToBeTested(assemble.TEST));
        assertFalse(assemble.isToBeTested(assemble.RUN));
    }

    @Test
    void getCheckMsg_정상은_NULL을_반환한다() {
        assemble.carSpec = new int[]{assemble.SEDAN, assemble.GM, assemble.MANDO, assemble.MOBIS, 0};
        assertNull(assemble.getCheckMsg());
    }

    @Test
    void getCheckMsg_Sedan에_Break는_Contiental_실패한다() {
        assemble.carSpec = new int[]{assemble.SEDAN, assemble.GM, assemble.CONTINENTAL, assemble.MOBIS, 0};
        assertNotNull(assemble.getCheckMsg());
    }

    @Test
    void getCheckMsg_SUV에_Engine은_Toyota_실패한다() {
        assemble.carSpec = new int[]{assemble.SUV, assemble.TOYOTA, assemble.MANDO, assemble.MOBIS, 0};
        assertNotNull(assemble.getCheckMsg());
    }

    @Test
    void getCheckMsg_Trunk에_Engine은_Wia_실패한다() {
        assemble.carSpec = new int[]{assemble.TRUCK, assemble.WIA, assemble.BOSCH_B, assemble.BOSCH_S, 0};
        assertNotNull(assemble.getCheckMsg());
    }

    @Test
    void getCheckMsg_Truck에_Break는_Mando_실패한다() {
        assemble.carSpec = new int[]{assemble.TRUCK, assemble.GM, assemble.MANDO, assemble.MOBIS, 0};
        assertNotNull(assemble.getCheckMsg());
    }

    @Test
    void getCheckMsg_Break가_Bosch면_Steering도_Bosch여야_한다() {
        assemble.carSpec = new int[]{assemble.SEDAN, assemble.GM, assemble.BOSCH_B, assemble.MOBIS, 0};
        assertNotNull(assemble.getCheckMsg());
    }

    @Test
    void getCheckMsg_Steering이_Bosch라고_Break도_Bosch일_필요는_없다() {
        assemble.carSpec = new int[]{assemble.SEDAN, assemble.GM, assemble.MANDO, assemble.BOSCH_S, 0};
        assertNull(assemble.getCheckMsg());
    }

    @Test
    void hasBrokenEngine_결과_잘_뱉는지_확인한다() {
        assemble.carSpec[assemble.ASK_ENGINE] = assemble.BROKEN;
        assertTrue(assemble.hasBrokenEngine());

        assemble.carSpec[assemble.ASK_ENGINE] = assemble.GM;
        assertFalse(assemble.hasBrokenEngine());
    }

    @Test
    void runProducedCar_getCheckMsg_fail일_때() {
        assembleSpy.carSpec = new int[]{assembleSpy.SEDAN, assembleSpy.GM, assembleSpy.CONTINENTAL, assembleSpy.MOBIS, 0};
        assembleSpy.runProducedCar();
        verify(assembleSpy, never()).hasBrokenEngine();
    }

    @Test
    void runProducedCar_hasBrokenEngine_해당될_때() {
        assembleSpy.carSpec[assembleSpy.ASK_ENGINE] = assembleSpy.BROKEN;
        assembleSpy.runProducedCar();
        verify(assembleSpy, never()).delay(anyInt());
    }

    @Test
    void runProducedCar_정상일_때() {
        doReturn(null).when(assembleSpy).getCheckMsg();
        doReturn(false).when(assembleSpy).hasBrokenEngine();

        assembleSpy.runProducedCar();

        verify(assembleSpy, times(1)).delay(2000);
    }

    @Test
    void testProducedCar_정상일_때() {
        doReturn(null).when(assembleSpy).getCheckMsg();

        assembleSpy.testProducedCar();

        verify(assembleSpy, never()).fail(anyString());
        verify(assembleSpy, times(1)).delay(1500);
        verify(assembleSpy, times(1)).delay(2000);
    }

    @Test
    void testProducedCar_비정상일_때() {
        String expectedMsg = "SPYING FAIL";
        doReturn(expectedMsg).when(assembleSpy).getCheckMsg();

        assembleSpy.testProducedCar();

        verify(assembleSpy, times(1)).fail(expectedMsg);
        verify(assembleSpy, times(1)).delay(1500);
        verify(assembleSpy, times(1)).delay(2000);
    }
}