import java.util.Map;

public class SteeringSystemCommand extends Command {
    public static final int BOSCH_S = 1, MOBIS = 2;
    public static final Map<Integer, String> OPTIONS = Map.of(BOSCH_S, "BOSCH", MOBIS, "MOBIS");

    @Override
    public void execute(int[] carSpec, int input) {
        carSpec[CommonUtil.ASK_STEERING_SYSTEM] = input;
        System.out.printf("%s 조향장치를 선택하셨습니다.\n", OPTIONS.get(input));
    }

    @Override
    public int getNextStep() {
        return CommonUtil.ASK_RUN_OR_TEST;
    }

    @Override
    public int getRollbackStep() {
        return Math.max(CommonUtil.ASK_STEERING_SYSTEM - 1, CommonUtil.ASK_CAR_TYPE);
    }

    @Override
    public Map<Integer, String> getOptions() {
        return OPTIONS;
    }

    @Override
    public void addHeader() {
        System.out.println("어떤 조향장치를 선택할까요?");
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
