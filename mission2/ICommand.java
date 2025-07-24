import java.util.Map;

public interface ICommand {
    void showOptions();
    boolean isValidRange(int input);
    void printInputOptions();
    void printPartition();

    void execute(int[] carSpec, int input);
    int getNextStep();
    int getRollbackStep();
    Map<Integer, String> getOptions();
    void addHeader();
    void addRollbackOption();
    boolean hasRollback();
}