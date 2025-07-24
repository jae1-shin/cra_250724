import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssembleTest {
    private Assemble assemble;
    private Assemble assembleSpy;

    private final PrintStream standardOut = System.out; // 기본값
    private final InputStream standardIn = System.in; // 기본값
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        assemble = new Assemble();
        assembleSpy = spy(new Assemble());

        outputStream = new ByteArrayOutputStream();
    }

    @AfterEach
    void tearDown() { // 복원
        System.setOut(standardOut);
        System.setIn(standardIn);
    }

    @Test
    void 단순_모음_익셉션_발생_안_하면_된다() {
        assertDoesNotThrow(() -> assemble.clearScreen());
//        assertDoesNotThrow(() -> assemble.showCarTypeMenu());
//        assertDoesNotThrow(() -> assemble.showEngineMenu());
//        assertDoesNotThrow(() -> assemble.showBrakeSystemMenu());
//        assertDoesNotThrow(() -> assemble.showSteeringSystemMenu());
//        assertDoesNotThrow(() -> assemble.showRunOrTestMenu());
        assertDoesNotThrow(() -> assemble.delayWhenError());
        assertDoesNotThrow(() -> assemble.delayForWhile());
    }

    @Test
    void initStep_시작은_CAR_TYPE부터() {
        assertEquals(CommonUtil.ASK_CAR_TYPE, assemble.initStep());
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
    void isValidRange_정상적인_범위는_true를_반환한다() {
        ICommand command;

        command = new CarTypeCommand();
        assertTrue(command.isValidRange(1));

        command = new EngineCommand();
        assertTrue(command.isValidRange(0));

        command = new BrakeSystemCommand();
        assertTrue(command.isValidRange(3));

        command = new SteeringSystemCommand();
        assertTrue(command.isValidRange(2));

        command = new RunOrTestCommand();
        assertTrue(command.isValidRange(1));
    }

    @Test
    void isValidRange_비정상적인_범위는_false를_반환한다() {
        ICommand command;

        command = new CarTypeCommand();
        assertFalse(command.isValidRange(0));

        command = new EngineCommand();
        assertFalse(command.isValidRange(5));

        command = new BrakeSystemCommand();
        assertFalse(command.isValidRange(4));

        command = new SteeringSystemCommand();
        assertFalse(command.isValidRange(3));

        command = new RunOrTestCommand();
        assertFalse(command.isValidRange(3));
    }

//    @Test
//    void getOptionsForStep_비정상적인_범위는_예외를_발생시킨다() {
//        assertThrows(IllegalStateException.class, () -> assemble.isValidRange(100, 0));
//    }

    @Test
    void selectCarType_carSpec이_정상적으로_설정된다() {
        CarTypeCommand command = new CarTypeCommand();

        command.execute(assemble.carSpec, 1);
        assertEquals(1, assemble.carSpec[CommonUtil.ASK_CAR_TYPE]);

        command.execute(assemble.carSpec, 2);
        assertEquals(2, assemble.carSpec[CommonUtil.ASK_CAR_TYPE]);

        command.execute(assemble.carSpec, 3);
        assertEquals(3, assemble.carSpec[CommonUtil.ASK_CAR_TYPE]);
    }

    @Test
    void selectEngine_carSpec이_정상적으로_설정된다() {
        EngineCommand command = new EngineCommand();

        command.execute(assemble.carSpec, 1);
        assertEquals(1, assemble.carSpec[CommonUtil.ASK_ENGINE]);

        command.execute(assemble.carSpec, 2);
        assertEquals(2, assemble.carSpec[CommonUtil.ASK_ENGINE]);

        command.execute(assemble.carSpec, 3);
        assertEquals(3, assemble.carSpec[CommonUtil.ASK_ENGINE]);

        command.execute(assemble.carSpec, 4);
        assertEquals(4, assemble.carSpec[CommonUtil.ASK_ENGINE]);
    }

    @Test
    void selectBrakeSystem_carSpec이_정상적으로_설정된다() {
        BrakeSystemCommand command = new BrakeSystemCommand();

        command.execute(assemble.carSpec, 1);
        assertEquals(1, assemble.carSpec[CommonUtil.ASK_BRAKE_SYSTEM]);

        command.execute(assemble.carSpec, 2);
        assertEquals(2, assemble.carSpec[CommonUtil.ASK_BRAKE_SYSTEM]);

        command.execute(assemble.carSpec, 3);
        assertEquals(3, assemble.carSpec[CommonUtil.ASK_BRAKE_SYSTEM]);
    }

    @Test
    void selectSteeringSystem_carSpec이_정상적으로_설정된다() {
        SteeringSystemCommand command = new SteeringSystemCommand();

        command.execute(assemble.carSpec, 1);
        assertEquals(1, assemble.carSpec[CommonUtil.ASK_STEERING_SYSTEM]);

        command.execute(assemble.carSpec, 2);
        assertEquals(2, assemble.carSpec[CommonUtil.ASK_STEERING_SYSTEM]);
    }

    @Test
    void getNextStep_다음_거_잘_찾는지_확인() {
        ICommand command;

        command = new CarTypeCommand();
        assertEquals(CommonUtil.ASK_ENGINE, command.getNextStep());

        command = new EngineCommand();
        assertEquals(CommonUtil.ASK_BRAKE_SYSTEM, command.getNextStep());

        command = new BrakeSystemCommand();
        assertEquals(CommonUtil.ASK_STEERING_SYSTEM, command.getNextStep());

        command = new SteeringSystemCommand();
        assertEquals(CommonUtil.ASK_RUN_OR_TEST, command.getNextStep());

        command = new RunOrTestCommand();
        assertEquals(CommonUtil.ASK_RUN_OR_TEST, command.getNextStep());
    }

//    @Test
//    void getNextStep_정의되지_않으면_예외를_발생시킨다() {
//        assertThrows(IllegalStateException.class, () -> assemble.getNextStep(assemble.ASK_RUN_OR_TEST));
//    }

    @Test
    void isToBeTested_TEST_숫자_선택_때만_true_반환한다() {
        RunOrTestCommand command = new RunOrTestCommand();
        assertTrue(command.isToBeTested(RunOrTestCommand.TEST));
        assertFalse(command.isToBeTested(RunOrTestCommand.RUN));
    }

    @Test
    void isToBeRun_RUN_숫자_선택_때만_true_반환한다() {
        RunOrTestCommand command = new RunOrTestCommand();
        assertTrue(command.isToBeRun(RunOrTestCommand.RUN));
        assertFalse(command.isToBeRun(RunOrTestCommand.TEST));
    }

    @Test
    void getCheckMsg_정상은_NULL을_반환한다() {
        RunOrTestCommand command = new RunOrTestCommand();
        command.carSpec = new int[]{CarTypeCommand.SEDAN, EngineCommand.GM, BrakeSystemCommand.MANDO, SteeringSystemCommand.MOBIS, 0};
        assertNull(command.getCheckMsg());
    }

    @Test
    void getCheckMsg_Sedan에_Break는_Contiental_실패한다() {
        RunOrTestCommand command = new RunOrTestCommand();
        command.carSpec = new int[]{CarTypeCommand.SEDAN, EngineCommand.GM, BrakeSystemCommand.CONTINENTAL, SteeringSystemCommand.MOBIS, 0};
        assertNotNull(command.getCheckMsg());
    }

    @Test
    void getCheckMsg_SUV에_Engine은_Toyota_실패한다() {
        RunOrTestCommand command = new RunOrTestCommand();
        command.carSpec = new int[]{CarTypeCommand.SUV, EngineCommand.TOYOTA, BrakeSystemCommand.MANDO, SteeringSystemCommand.MOBIS, 0};
        assertNotNull(command.getCheckMsg());
    }

    @Test
    void getCheckMsg_Trunk에_Engine은_Wia_실패한다() {
        RunOrTestCommand command = new RunOrTestCommand();
        command.carSpec = new int[]{CarTypeCommand.TRUCK, EngineCommand.WIA, BrakeSystemCommand.BOSCH_B, SteeringSystemCommand.BOSCH_S, 0};
        assertNotNull(command.getCheckMsg());
    }

    @Test
    void getCheckMsg_Truck에_Break는_Mando_실패한다() {
        RunOrTestCommand command = new RunOrTestCommand();
        command.carSpec = new int[]{CarTypeCommand.TRUCK, EngineCommand.GM, BrakeSystemCommand.MANDO, SteeringSystemCommand.MOBIS, 0};
        assertNotNull(command.getCheckMsg());
    }

    @Test
    void getCheckMsg_Break가_Bosch면_Steering도_Bosch여야_한다() {
        RunOrTestCommand command = new RunOrTestCommand();
        command.carSpec = new int[]{CarTypeCommand.SEDAN, EngineCommand.GM, BrakeSystemCommand.BOSCH_B, SteeringSystemCommand.MOBIS, 0};
        assertNotNull(command.getCheckMsg());
    }

    @Test
    void getCheckMsg_Steering이_Bosch라고_Break도_Bosch일_필요는_없다() {
        RunOrTestCommand command = new RunOrTestCommand();
        command.carSpec = new int[]{CarTypeCommand.SEDAN, EngineCommand.GM, BrakeSystemCommand.MANDO, SteeringSystemCommand.BOSCH_S, 0};
        assertNull(command.getCheckMsg());
    }

    @Test
    void hasBrokenEngine_결과_잘_뱉는지_확인한다() {
        RunOrTestCommand command = new RunOrTestCommand();
        command.carSpec = assemble.carSpec;
        command.carSpec[CommonUtil.ASK_ENGINE] = EngineCommand.BROKEN;
        assertTrue(command.hasBrokenEngine());

        assemble.carSpec[CommonUtil.ASK_ENGINE] = EngineCommand.GM;
        assertFalse(command.hasBrokenEngine());
    }

    @Test
    void runProducedCar_getCheckMsg_fail일_때() {
        RunOrTestCommand command = spy(new RunOrTestCommand());
        command.carSpec = new int[]{CarTypeCommand.SEDAN, EngineCommand.GM, BrakeSystemCommand.CONTINENTAL, SteeringSystemCommand.MOBIS, 0};
        command.runProducedCar();
        verify(command, never()).hasBrokenEngine();
    }

    @Test
    void runProducedCar_hasBrokenEngine_해당될_때() {
        RunOrTestCommand command = spy(new RunOrTestCommand());
        command.carSpec = assemble.carSpec;
        command.carSpec[CommonUtil.ASK_ENGINE] = EngineCommand.BROKEN;
        command.runProducedCar();
//        verify(command, never()).delay(anyInt());
    }

    @Test
    void runProducedCar_정상일_때() {
        RunOrTestCommand command = spy(new RunOrTestCommand());
        command.carSpec = new int[]{CarTypeCommand.SEDAN, EngineCommand.GM, BrakeSystemCommand.MANDO, SteeringSystemCommand.MOBIS, 0};

        doReturn(null).when(command).getCheckMsg();
        doReturn(false).when(command).hasBrokenEngine();

        command.runProducedCar();

//        verify(command, times(1)).delay(2000);
    }

    @Test
    void testProducedCar_정상일_때() {
        RunOrTestCommand command = spy(new RunOrTestCommand());

        doReturn(null).when(command).getCheckMsg();

        command.testProducedCar();

//        verify(command, times(1)).delay(1500);
//        verify(command, times(1)).delay(2000);
    }

    @Test
    void testProducedCar_비정상일_때() {
        RunOrTestCommand command = spy(new RunOrTestCommand());

        String expectedMsg = "SPYING FAIL";
        doReturn(expectedMsg).when(command).getCheckMsg();

        command.testProducedCar();

//        verify(command, times(1)).fail(expectedMsg);
//        verify(command, times(1)).delay(1500);
//        verify(command, times(1)).delay(2000);
    }

    @Test
    void CarTypeCommand에서_getRollbackStep은_호출되면_안된다() {
        CarTypeCommand command = new CarTypeCommand();
        assertThrows(UnsupportedOperationException.class, command::getRollbackStep);
    }

    @Test
    void 나머지_getRollbackStep_호출_결과_확인한다() {
        ICommand command;

        command = new EngineCommand();
        assertEquals(CommonUtil.ASK_CAR_TYPE, command.getRollbackStep());

        command = new BrakeSystemCommand();
        assertEquals(CommonUtil.ASK_ENGINE, command.getRollbackStep());

        command = new SteeringSystemCommand();
        assertEquals(CommonUtil.ASK_BRAKE_SYSTEM, command.getRollbackStep());

        command = new RunOrTestCommand();
        assertEquals(CommonUtil.ASK_CAR_TYPE, command.getRollbackStep());
    }

    @Test
    void main_정상_시나리오_최대한_다양하게() {
        // 세단 1
        // 지엠 엔진 1
        // 돌아가기 0
        // 세단 1
        // 지엠 엔진 1
        // 만도 브레이크 1
        // 돌아가기 0
        // 세단 1
        // 만도 브레이크 1
        // 모비스 스티어링 2
        // 돌아가기 0
        // 만도 브레이크 1
        // 모비스 스티어링 2
        // RUN 1
        // TEST 2
        // 돌아가기 0
        // exit
        String scenario = "1\n1\n0\n1\n1\n1\n0\n1\n1\n2\n0\n1\n2\n1\n2\n0\nexit\n";
        InputStream inputStream = new ByteArrayInputStream(scenario.getBytes());

        System.setIn(inputStream);
        System.setOut(new PrintStream(outputStream));

        assemble.main(new String[]{});

        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("자동차가 동작됩니다.")); // RUN
        assertTrue(consoleOutput.contains("자동차 부품 조합 테스트 결과 : PASS")); // TEST
        assertTrue(consoleOutput.contains("바이바이")); // exit
    }

    @Test
    void main_비정상_시나리오_라인_커버리지_최대한() {
        // 잘못된 문자
        // 숫자 범위 밖
        // exit
        String scenario = "abc\n0\nexit\n";
        InputStream inputStream = new ByteArrayInputStream(scenario.getBytes());

        System.setIn(inputStream);
        System.setOut(new PrintStream(outputStream));

        assemble.main(new String[]{});
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("ERROR :: 숫자만 입력 가능"));
        assertTrue(consoleOutput.contains("ERROR :: 1 ~ 3 범위만 선택 가능"));
        assertTrue(consoleOutput.contains("바이바이"));
    }

    @Test
    void Factory_예외_처리_시나리오() {
        assertThrows(IllegalArgumentException.class, () -> CommandFactory.getCommand(1000));    }
}