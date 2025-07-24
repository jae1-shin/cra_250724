public class CommonUtil {
    public static final int ROLLBACK = 0;
    public static final int ASK_CAR_TYPE = 0, ASK_ENGINE = 1, ASK_BRAKE_SYSTEM = 2, ASK_STEERING_SYSTEM = 3, ASK_RUN_OR_TEST = 4;

    public static boolean isRollback(int input) {
        return input == CommonUtil.ROLLBACK;
    }

    public static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
