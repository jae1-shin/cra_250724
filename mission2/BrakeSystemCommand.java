import java.util.Map;

public class BrakeSystemCommand extends Command {
    public static final int MANDO = 1, CONTINENTAL = 2, BOSCH_B = 3;
    public static final Map<Integer, String> OPTIONS = Map.of(MANDO, "MANDO", CONTINENTAL, "CONTINENTAL", BOSCH_B, "BOSCH");

    @Override
    public void execute(int[] carSpec, int input) {
        carSpec[CommonUtil.ASK_BRAKE_SYSTEM] = input;
        System.out.printf("%s 제동장치를 선택하셨습니다.\n", OPTIONS.get(input));
    }

    @Override
    public int getNextStep() {
        return CommonUtil.ASK_STEERING_SYSTEM;
    }

    @Override
    public int getRollbackStep() {
        return Math.max(CommonUtil.ASK_BRAKE_SYSTEM - 1, CommonUtil.ASK_CAR_TYPE);
    }

    @Override
    public Map<Integer, String> getOptions() {
        return OPTIONS;
    }

    @Override
    public void addHeader() {
        System.out.println("어떤 제동장치를 선택할까요?");
    }

    @Override
    public void addRollbackOption() {
        System.out.println("0. 뒤로가기");
    }

    @Override
    public boolean hasRollback() {
        return true;
    }
}
