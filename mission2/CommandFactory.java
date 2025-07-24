public class CommandFactory {
    public static ICommand getCommand(int step) {
        return switch (step) {
            case CommonUtil.ASK_CAR_TYPE -> new CarTypeCommand();
            case CommonUtil.ASK_ENGINE -> new EngineCommand();
            case CommonUtil.ASK_BRAKE_SYSTEM -> new BrakeSystemCommand();
            case CommonUtil.ASK_STEERING_SYSTEM -> new SteeringSystemCommand();
            case CommonUtil.ASK_RUN_OR_TEST -> new RunOrTestCommand();
            default -> throw new IllegalArgumentException("Invalid step: " + step);
        };
    }
}
