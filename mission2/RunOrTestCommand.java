import java.util.Map;

public class RunOrTestCommand extends Command {
    public static final int RUN = 1, TEST = 2;
    public static final Map<Integer, String> OPTIONS = Map.of(RUN, "RUN", TEST, "Test");
    public int[] carSpec;

    @Override
    public void execute(int[] carSpec, int input) {
        this.carSpec = carSpec;
        if (isToBeRun(input)) {
            runProducedCar();
        } else if (isToBeTested(input)) {
            testProducedCar();
        }
    }

    @Override
    public int getNextStep() {
        return CommonUtil.ASK_RUN_OR_TEST;
    }

    @Override
    public int getRollbackStep() {
        return CommonUtil.ASK_CAR_TYPE;
    }

    @Override
    public Map<Integer, String> getOptions() {
        return OPTIONS;
    }

    @Override
    public void addHeader() {
        System.out.println("멋진 차량이 완성되었습니다.");
        System.out.println("어떤 동작을 할까요?");
    }

    @Override
    public void addRollbackOption() {
        System.out.println("0. 처음 화면으로 돌아가기");
    }

    @Override
    public boolean hasRollback() {
        return true;
    }

    boolean isToBeTested(int input) {
        return input == TEST;
    }

    boolean isToBeRun(int input) {
        return input == RUN;
    }

    String getCheckMsg() {
        if (carSpec[CommonUtil.ASK_CAR_TYPE] == CarTypeCommand.SEDAN && carSpec[CommonUtil.ASK_BRAKE_SYSTEM] == BrakeSystemCommand.CONTINENTAL) {
            return "Sedan에는 Continental제동장치 사용 불가";
        }
        if (carSpec[CommonUtil.ASK_CAR_TYPE] == CarTypeCommand.SUV && carSpec[CommonUtil.ASK_ENGINE] == EngineCommand.TOYOTA) {
            return "SUV에는 TOYOTA엔진 사용 불가";
        }
        if (carSpec[CommonUtil.ASK_CAR_TYPE] == CarTypeCommand.TRUCK && carSpec[CommonUtil.ASK_ENGINE] == EngineCommand.WIA) {
            return "Truck에는 WIA엔진 사용 불가";
        }
        if (carSpec[CommonUtil.ASK_CAR_TYPE] == CarTypeCommand.TRUCK && carSpec[CommonUtil.ASK_BRAKE_SYSTEM] == BrakeSystemCommand.MANDO) {
            return "Truck에는 Mando제동장치 사용 불가";
        }
        if (carSpec[CommonUtil.ASK_BRAKE_SYSTEM] == BrakeSystemCommand.BOSCH_B && carSpec[CommonUtil.ASK_STEERING_SYSTEM] != SteeringSystemCommand.BOSCH_S) {
            return "Bosch제동장치에는 Bosch조향장치 이외 사용 불가";
        }

        return null;
    }

    void runProducedCar() {
        if (getCheckMsg() != null) {
            System.out.println("자동차가 동작되지 않습니다");
            return;
        }

        if (hasBrokenEngine()) {
            System.out.println("엔진이 고장나있습니다.");
            System.out.println("자동차가 움직이지 않습니다.");
            return;
        }

        System.out.printf("Car Type : %s\n", CarTypeCommand.OPTIONS.get(carSpec[CommonUtil.ASK_CAR_TYPE]));
        System.out.printf("Engine   : %s\n", EngineCommand.OPTIONS.get(carSpec[CommonUtil.ASK_ENGINE]));
        System.out.printf("Brake    : %s\n", BrakeSystemCommand.OPTIONS.get(carSpec[CommonUtil.ASK_BRAKE_SYSTEM]));
        System.out.printf("Steering : %s\n", SteeringSystemCommand.OPTIONS.get(carSpec[CommonUtil.ASK_STEERING_SYSTEM]));
        System.out.println("자동차가 동작됩니다.");
        CommonUtil.delay(2000);
    }

    boolean hasBrokenEngine() {
        return carSpec[CommonUtil.ASK_ENGINE] == 4;
    }

    void testProducedCar() {
        System.out.println("Test...");
        CommonUtil.delay(1500);

        String checkMsg = getCheckMsg();

        if (checkMsg == null) {
            System.out.println("자동차 부품 조합 테스트 결과 : PASS");
        } else {
            System.out.println("자동차 부품 조합 테스트 결과 : FAIL");
            System.out.println(checkMsg);
        }
        CommonUtil.delay(2000);
    }
}
