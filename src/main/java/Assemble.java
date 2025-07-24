import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Assemble {
    final String CLEAR_SCREEN = "\033[H\033[2J";

    final int ASK_CAR_TYPE = 0, ASK_ENGINE = 1, ASK_BRAKE_SYSTEM = 2, ASK_STEERING_SYSTEM = 3, ASK_RUN_OR_TEST = 4;

    final int ROLLBACK = 0;
    final int SEDAN = 1, SUV = 2, TRUCK = 3;
    final Map<Integer, String> CAR_TYPES = Map.of(SEDAN, "Sedan", SUV, "SUV", TRUCK, "Truck");
    final int GM = 1, TOYOTA = 2, WIA = 3, BROKEN = 4;
    final Map<Integer, String> ENGINES = Map.of(GM, "GM", TOYOTA, "TOYOTA", WIA, "WIA", BROKEN, "고장난 엔진");
    final int MANDO = 1, CONTINENTAL = 2, BOSCH_B = 3;
    final Map<Integer, String> BRAKE_SYSTEMS = Map.of(MANDO, "MANDO", CONTINENTAL, "CONTINENTAL", BOSCH_B, "BOSCH");
    final int BOSCH_S = 1, MOBIS = 2;
    final Map<Integer, String> STEERING_SYSTEMS = Map.of(BOSCH_S, "BOSCH", MOBIS, "MOBIS");
    final int RUN = 1, TEST = 2;
    final Map<Integer, String> RUN_OR_TEST = Map.of(RUN, "RUN", TEST, "Test");

    int[] carSpec = new int[5];

    public void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int step = initStep();

        while (true) {
            clearScreen();

            ICommand command = CommandFactory.getCommand(step);
            command.showOptions();

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

            if (!command.isValidRange(input)) {
                delayWhenError();
                continue;
            }

            if (CommonUtil.isRollback(input)) {
                step = command.getRollbackStep();
                continue;
            }

            command.execute(carSpec, input);
            step = command.getNextStep();
        }

        sc.close();
    }

    void delayWhenError() {
        CommonUtil.delay(800);
    }

    void delayForWhile() {
        CommonUtil.delay(800);
    }

    int convertRawInputToInt(String rawInput) {
        try {
            return Integer.parseInt(rawInput);
        } catch (NumberFormatException e) {
            System.out.println("ERROR :: 숫자만 입력 가능");
            throw new NumberFormatException();
        }
    }

    boolean needsToBeHalted(String buf) {
        if (buf.equalsIgnoreCase("exit")) {
            System.out.println("바이바이");
            return true;
        }

        return false;
    }

    void clearScreen() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }

    int initStep() {
        return ASK_CAR_TYPE;
    }
}