import java.util.Map;

public class EngineCommand extends Command {
    public static final int GM = 1, TOYOTA = 2, WIA = 3, BROKEN = 4;
    public static final Map<Integer, String> OPTIONS = Map.of(GM, "GM", TOYOTA, "TOYOTA", WIA, "WIA", BROKEN, "고장난 엔진");

    @Override
    public void execute(int[] carSpec, int input) {
        carSpec[CommonUtil.ASK_ENGINE] = input;
        System.out.printf("%s 엔진을 선택하셨습니다.\n", OPTIONS.get(input));
    }

    @Override
    public int getNextStep() {
        return CommonUtil.ASK_BRAKE_SYSTEM;
    }

    @Override
    public int getRollbackStep() {
        return Math.max(CommonUtil.ASK_ENGINE - 1, CommonUtil.ASK_CAR_TYPE);
    }

    @Override
    public Map<Integer, String> getOptions() {
        return OPTIONS;
    }

    @Override
    public void addHeader() {
        System.out.println("어떤 엔진을 탑재할까요?");
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
