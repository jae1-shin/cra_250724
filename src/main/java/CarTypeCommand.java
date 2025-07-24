import java.util.Map;

public class CarTypeCommand extends Command {
    public static final int SEDAN = 1, SUV = 2, TRUCK = 3;
    public static final Map<Integer, String> OPTIONS = Map.of(SEDAN, "Sedan", SUV, "SUV", TRUCK, "Truck");;

    @Override
    public void execute(int[] carSpec, int input) {
        carSpec[CommonUtil.ASK_CAR_TYPE] = input;
        System.out.printf("차량 타입으로 %s을 선택하셨습니다.\n", OPTIONS.get(input));
    }

    @Override
    public int getNextStep() {
        return CommonUtil.ASK_ENGINE;
    }

    @Override
    public int getRollbackStep() {
        throw new UnsupportedOperationException("이 단계에선 뒤로갈 수 없습니다.");
    }

    @Override
    public Map<Integer, String> getOptions() {
        return OPTIONS;
    }

    @Override
    public void addHeader() {
        System.out.println("        ______________");
        System.out.println("       /|            |");
        System.out.println("  ____/_|_____________|____");
        System.out.println(" |                      O  |");
        System.out.println(" '-(@)----------------(@)--'");
        printPartition();
        System.out.println("어떤 차량 타입을 선택할까요?");
    }

    @Override
    public void addRollbackOption() {
    }

    @Override
    public boolean hasRollback() {
        return false;
    }
}
