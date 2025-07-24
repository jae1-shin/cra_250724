import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Assemble {
    private static final String CLEAR_SCREEN = "\033[H\033[2J";

    private static final int ASK_CAR_TYPE = 0;
    private static final int ASK_ENGINE = 1;
    private static final int ASK_BREAK_SYSTEM = 2;
    private static final int ASK_STEERING_SYSTEM = 3;
    private static final int ASK_RUN_OR_TEST = 4;

    private static final int ROLLBACK = 0;
    private static final int SEDAN = 1, SUV = 2, TRUCK = 3;
    private static final Map<Integer, String> CAR_TYPES = Map.of(SEDAN, "Sedan", SUV, "SUV", TRUCK, "Truck");
    private static final int GM = 1, TOYOTA = 2, WIA = 3, BROKEN = 4;
    private static final Map<Integer, String> ENGINES = Map.of(GM, "GM", TOYOTA, "TOYOTA", WIA, "WIA", BROKEN, "고장난 엔진");
    private static final int MANDO = 1, CONTINENTAL = 2, BOSCH_B = 3;
    private static final Map<Integer, String> BRAKE_SYSTEMS = Map.of(MANDO, "MANDO", CONTINENTAL, "CONTINENTAL", BOSCH_B, "BOSCH");
    private static final int BOSCH_S = 1, MOBIS = 2;
    private static final Map<Integer, String> STEERING_SYSTEMS = Map.of(BOSCH_S, "BOSCH", MOBIS, "MOBIS");
    private static final int RUN = 1, TEST = 2;
    private static final Map<Integer, String> RUN_OR_TEST = Map.of(RUN, "RUN", TEST, "Test");

    private static int[] carSpec = new int[5];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int step = initStep();

        while (true) {
            clearScreen();

            switch (step) {
                case ASK_CAR_TYPE:
                    showCarTypeMenu(); break;
                case ASK_ENGINE:
                    showEngineMenu(); break;
                case ASK_BREAK_SYSTEM:
                    showBrakeSystemMenu(); break;
                case ASK_STEERING_SYSTEM:
                    showSteeringSystemMenu(); break;
                case ASK_RUN_OR_TEST:
                    showRunOrTestMenu(); break;
            }

            System.out.print("INPUT > ");
            String rawInput = sc.nextLine().trim();
            int input;

            if (needsToBeHalted(rawInput)) {
                break;
            }

            try {
                input = convertRawInputToInt(rawInput);
            } catch (NumberFormatException e) {
                delayWhenError();
                continue;
            }

            if (!isValidRange(step, input)) {
                delayWhenError();
                continue;
            }

            if (input == ROLLBACK) {
                if (isRunOrTestStep(step)) {
                    step = ASK_CAR_TYPE;
                } else {
                    step = Math.max(step - 1, ASK_CAR_TYPE);
                }
                continue;
            }

            switch (step) {
                case ASK_CAR_TYPE:
                    selectCarType(input);
                    step = getNextStep(ASK_CAR_TYPE);
                    break;
                case ASK_ENGINE:
                    selectEngine(input);
                    step = getNextStep(ASK_ENGINE);
                    break;
                case ASK_BREAK_SYSTEM:
                    selectBrakeSystem(input);
                    step = getNextStep(ASK_BREAK_SYSTEM);
                    break;
                case ASK_STEERING_SYSTEM:
                    selectSteeringSystem(input);
                    step = getNextStep(ASK_STEERING_SYSTEM);
                    break;
                case ASK_RUN_OR_TEST:
                    if (isToBeRun(input)) {
                        runProducedCar();
                    } else if (isToBeTested(input)) {
                        testProducedCar();
                    }
                    break;
            }
        }

        sc.close();
    }

    private static boolean isToBeTested(int input) {
        return input == TEST;
    }

    private static boolean isToBeRun(int input) {
        return input == RUN;
    }

    private static int getNextStep(int currentStep) {
        return switch (currentStep) {
            case ASK_CAR_TYPE -> ASK_ENGINE;
            case ASK_ENGINE -> ASK_BREAK_SYSTEM;
            case ASK_BREAK_SYSTEM -> ASK_STEERING_SYSTEM;
            case ASK_STEERING_SYSTEM -> ASK_RUN_OR_TEST;
            default -> throw new IllegalStateException("Has no next step");
        };
    }

    private static boolean isRunOrTestStep(int step) {
        return step == ASK_RUN_OR_TEST;
    }

    private static void delayWhenError() {
        delay(800);
    }

    private static void delayForWhile() {
        delay(800);
    }

    private static int convertRawInputToInt(String rawInput) {
        try {
            return Integer.parseInt(rawInput);
        } catch (NumberFormatException e) {
            System.out.println("ERROR :: 숫자만 입력 가능");
            throw new NumberFormatException();
        }
    }

    private static boolean needsToBeHalted(String buf) {
        if (buf.equalsIgnoreCase("exit")) {
            System.out.println("바이바이");
            return true;
        }

        return false;
    }

    private static void clearScreen() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }

    private static int initStep() {
        return ASK_CAR_TYPE;
    }

    private static void printPartition() {
        System.out.println("===============================");
    }

    private static void showCarTypeMenu() {
        printInputOptions(ASK_CAR_TYPE, CAR_TYPES);
        printPartition();
    }

    private static void showEngineMenu() {
        printInputOptions(ASK_ENGINE, ENGINES);
        printPartition();
    }
    private static void showBrakeSystemMenu() {
        printInputOptions(ASK_STEERING_SYSTEM, BRAKE_SYSTEMS);
        printPartition();
    }
    private static void showSteeringSystemMenu() {
        printInputOptions(ASK_STEERING_SYSTEM, STEERING_SYSTEMS);
        printPartition();
    }
    private static void showRunOrTestMenu() {
        printInputOptions(ASK_RUN_OR_TEST, RUN_OR_TEST);
        printPartition();
    }

    private static void printInputOptions(int step, Map<Integer, String> options) {
        addHeader(step);

        if (hasRollback(step)) {
            addRollbackOption(step);
        }

        for (Map.Entry<Integer, String> entry : options.entrySet()) {
            System.out.printf("%d. %s\n", entry.getKey(), entry.getValue());
        }
    }

    private static void addHeader(int step) {
        switch (step) {
            case ASK_CAR_TYPE:
                System.out.println("        ______________");
                System.out.println("       /|            |");
                System.out.println("  ____/_|_____________|____");
                System.out.println(" |                      O  |");
                System.out.println(" '-(@)----------------(@)--'");
                printPartition();
                System.out.println("어떤 차량 타입을 선택할까요?");
                break;
            case ASK_ENGINE:
                System.out.println("어떤 엔진을 탑재할까요?");
                break;
            case ASK_BREAK_SYSTEM:
                System.out.println("어떤 제동장치를 선택할까요?");
                break;
            case ASK_STEERING_SYSTEM:
                System.out.println("어떤 조향장치를 선택할까요?");
                break;
            case ASK_RUN_OR_TEST:
                System.out.println("멋진 차량이 완성되었습니다.");
                System.out.println("어떤 동작을 할까요?");
                break;
        }
    }

    private static boolean hasRollback(int step) {
        return List.of(ASK_ENGINE, ASK_BREAK_SYSTEM, ASK_STEERING_SYSTEM, ASK_RUN_OR_TEST).contains(step);
    }

    private static void addRollbackOption(int step) {
        if (step == ASK_RUN_OR_TEST) {
            System.out.println("0. 처음 화면으로 돌아가기");
        } else {
            System.out.println("0. 뒤로가기");
        }
    }

    private static boolean isValidRange(int step, int input) {
        if (hasRollback(step) && input == ROLLBACK) return true;

        switch (step) {
            case ASK_CAR_TYPE:
                if (!CAR_TYPES.containsKey(input)) {
                    System.out.println("ERROR :: 차량 타입은 1 ~ 3 범위만 선택 가능");
                    return false;
                }
                break;
            case ASK_ENGINE:
                if (!ENGINES.containsKey(input)) {
                    System.out.println("ERROR :: 엔진은 1 ~ 4 범위만 선택 가능");
                    return false;
                }
                break;
            case ASK_BREAK_SYSTEM:
                if (!BRAKE_SYSTEMS.containsKey(input)) {
                    System.out.println("ERROR :: 제동장치는 1 ~ 3 범위만 선택 가능");
                    return false;
                }
                break;
            case ASK_STEERING_SYSTEM:
                if (!STEERING_SYSTEMS.containsKey(input)) {
                    System.out.println("ERROR :: 조향장치는 1 ~ 2 범위만 선택 가능");
                    return false;
                }
                break;
            case ASK_RUN_OR_TEST:
                if (!RUN_OR_TEST.containsKey(input)) {
                    System.out.println("ERROR :: Run 또는 Test 중 하나를 선택 필요");
                    return false;
                }
                break;
        }
        return true;
    }

    private static void selectCarType(int input) {
        carSpec[ASK_CAR_TYPE] = input;
        System.out.printf("차량 타입으로 %s을 선택하셨습니다.\n", CAR_TYPES.get(input));
        delayForWhile();
    }

    private static void selectEngine(int input) {
        carSpec[ASK_ENGINE] = input;
        System.out.printf("%s 엔진을 선택하셨습니다.\n", ENGINES.get(input));
        delayForWhile();
    }

    private static void selectBrakeSystem(int input) {
        carSpec[ASK_BREAK_SYSTEM] = input;
        System.out.printf("%s 제동장치를 선택하셨습니다.\n", BRAKE_SYSTEMS.get(input));
        delayForWhile();
    }

    private static void selectSteeringSystem(int input) {
        carSpec[ASK_STEERING_SYSTEM] = input;
        System.out.printf("%s 조향장치를 선택하셨습니다.\n", STEERING_SYSTEMS.get(input));
        delayForWhile();
    }

    private static String getCheckMsg() {
        if (carSpec[ASK_CAR_TYPE] == SEDAN && carSpec[ASK_BREAK_SYSTEM] == CONTINENTAL) return "Sedan에는 Continental제동장치 사용 불가";
        if (carSpec[ASK_CAR_TYPE] == SUV   && carSpec[ASK_ENGINE] == TOYOTA)       return "SUV에는 TOYOTA엔진 사용 불가";
        if (carSpec[ASK_CAR_TYPE] == TRUCK && carSpec[ASK_ENGINE] == WIA)          return "Truck에는 WIA엔진 사용 불가";
        if (carSpec[ASK_CAR_TYPE] == TRUCK && carSpec[ASK_BREAK_SYSTEM] == MANDO)  return "Truck에는 Mando제동장치 사용 불가";
        if (carSpec[ASK_BREAK_SYSTEM] == BOSCH_B && carSpec[ASK_STEERING_SYSTEM] != BOSCH_S) return "Bosch제동장치에는 Bosch조향장치 이외 사용 불가";
        return null;
    }

    private static void runProducedCar() {
        if (getCheckMsg() != null) {
            System.out.println("자동차가 동작되지 않습니다");
            return;
        }

        if (hasBrokenEngine()) {
            System.out.println("엔진이 고장나있습니다.");
            System.out.println("자동차가 움직이지 않습니다.");
            return;
        }

        System.out.printf("Car Type : %s\n", CAR_TYPES.get(carSpec[ASK_CAR_TYPE]));
        System.out.printf("Engine   : %s\n", ENGINES.get(carSpec[ASK_ENGINE]));
        System.out.printf("Brake    : %s\n", BRAKE_SYSTEMS.get(carSpec[ASK_BREAK_SYSTEM]));
        System.out.printf("Steering : %s\n", STEERING_SYSTEMS.get(carSpec[ASK_STEERING_SYSTEM]));
        System.out.println("자동차가 동작됩니다.");
        delay(2000);
    }

    private static boolean hasBrokenEngine() {
        return carSpec[ASK_ENGINE] == 4;
    }

    private static void testProducedCar() {
        System.out.println("Test...");
        delay(1500);

        String checkMsg = getCheckMsg();
        if (checkMsg == null) {
            System.out.println("자동차 부품 조합 테스트 결과 : PASS");
        } else {
            fail(checkMsg);
        }

        delay(2000);
    }

    private static void fail(String msg) {
        System.out.println("자동차 부품 조합 테스트 결과 : FAIL");
        System.out.println(msg);
    }

    private static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}