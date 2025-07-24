import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Command implements ICommand {
    @Override
    public void showOptions() {
        printInputOptions();
        printPartition();
    }

    @Override
    public boolean isValidRange(int input) {
        if (CommonUtil.isRollback(input) && hasRollback()) return true;

        Map<Integer, String> options = getOptions();
        if (!options.containsKey(input)) {
            int min = options.keySet().stream().min(Integer::compareTo).orElse(0);
            int max = options.keySet().stream().max(Integer::compareTo).orElse(0);
            System.out.printf("ERROR :: %d ~ %d 범위만 선택 가능\n", min, max);
            return false;
        }
        return true;
    }

    @Override
    public void printInputOptions() {
        addHeader();
        addRollbackOption();

        for (Map.Entry<Integer, String> entry : getOptions().entrySet()) {
            System.out.printf("%d. %s\n", entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void printPartition() {
        System.out.println("===============================");
    }

    public abstract void execute(int[] carSpec, int input);
    public abstract int getNextStep();
    public abstract int getRollbackStep();
    public abstract Map<Integer, String> getOptions();
    public abstract void addHeader();
    public abstract void addRollbackOption();
    public abstract boolean hasRollback();
}
