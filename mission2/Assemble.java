import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Assemble {
    final String CLEAR_SCREEN = "\033[H\033[2J";
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
        return CommonUtil.ASK_CAR_TYPE;
    }
}