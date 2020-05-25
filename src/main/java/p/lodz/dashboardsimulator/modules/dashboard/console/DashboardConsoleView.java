package p.lodz.dashboardsimulator.modules.dashboard.console;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.model.light.LightsMode;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardInjector;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardPresenter;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardView;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implements {@link DashboardView} using console. It doesn't support opening other modules.
 * Current status of the program is displayed on the console. It contains whole engine state, statistics and odometers.
 * On every change current status is clear and replaced with new one.
 * Messages are printed above the current status.
 */
public class DashboardConsoleView implements DashboardView {

    private double speed;
    private double rpm;

    private Observable<String> commands;
    private Disposable commandsSubscription;

    private DashboardPresenter presenter;

    private int lastStatusLength = 0;
    private int gear = 0;
    private String totalMileage = "-";
    private String[] resettables;
    private String fuelConsumption = "-";
    private String travelTime = "-";
    private String distance = "-";
    private String maximumSpeed = "-";
    private String avgSpeed = "-";

    public DashboardConsoleView(Observable<String> commands) {
        this.commands = commands;
    }

    /**
     * Initializes {@link DashboardPresenter} using passed {@link DashboardInjector} and attaches itself to it.
     * Starts the subscription of the commands stream.
     * @param injector Instance of {@link DashboardInjector} that provides model classes for {@link DashboardPresenter}
     */
    @Override
    public void start(Injector injector) {

        DashboardInjector dashboardInjector = (DashboardInjector) injector;

        presenter = new DashboardPresenter(
                dashboardInjector.getEngine(),
                dashboardInjector.getStatisticsMonitor(),
                dashboardInjector.getLightsController(),
                dashboardInjector.getOdometer(),
                dashboardInjector.getActiveCruiseControl(),
                dashboardInjector.getTravelDataRepository()
        );

        presenter.attach(this);

        commandsSubscription = commands.subscribe(this::notifyPresenter);
    }

    private void notifyPresenter(String s) {

        Matcher matcher = Pattern.compile("cc(\\d+)").matcher(s);

        if (matcher.matches()) {

            presenter.activateCruiseControl(matcher.group(1));
        }

        switch (s) {
            case "w":
                presenter.setEngineAcceleration(true);
                break;
            case "s":
                presenter.setEngineAcceleration(false);
                break;
            case "r":
                presenter.closeView();
                break;
            case "d":
                presenter.triggerRightTurnSignal();
                break;
            case "a":
                presenter.triggerLeftTurnSignal();
                break;
            case "1":
                presenter.changeLightMode(LightsMode.OFF);
                break;
            case "2":
                presenter.changeLightMode(LightsMode.PARKING);
                break;
            case "3":
                presenter.changeLightMode(LightsMode.LOW_BEAM);
                break;
            case "4":
                presenter.changeLightMode(LightsMode.HIGH_BEAM);
            case "add":
                presenter.saveCurrentStatsToDatabase();
                break;
            case "nocc":
                presenter.deactivateCruiseControl();
                break;

        }
    }

    /**
     * Updates current gear. Reprints the status.
     * @param gear Current gear.
     */
    @Override
    public void updateGear(int gear) {
        this.gear = gear;
        reprintStatus();
    }

    /**
     * Updates current speed value. Reprints the status.
     * @param speed value of the speed in km/h.
     */
    @Override
    public void updateSpeed(double speed) {
        this.speed = speed;
        reprintStatus();
    }

    /**
     * Updates current mileage. Reprints the status.
     * @param total Total mileage {@link String}.
     * @param resettable Resettable mileages {@link String}s.
     */
    @Override
    public void updateMileage(String total, String... resettable) {
        this.totalMileage = total;
        this.resettables = resettable;
        reprintStatus();
    }

    /**
     * Updates current average speed. Reprints status.
     * @param avgSpeed Well formatted {@link String} that contains average speed.
     */
    @Override
    public void setAverageSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
        reprintStatus();
    }

    /**
     * Updates current maximum speed. Reprints the status.
     * @param maximumSpeed Well formatted {@link String} that contains maximum speed in km/h.
     */
    @Override
    public void setMaximumSpeed(String maximumSpeed) {

        this.maximumSpeed = maximumSpeed;
        reprintStatus();
    }

    /**
     * Updates current distance. Reprints the status.
     * @param distance Well formatted {@link String} that contains distance in km.
     */
    @Override
    public void setDistance(String distance) {

        this.distance = distance;
        reprintStatus();
    }

    /**
     * Updates current travel time. Reprints status.
     * @param formatDuration Well formatted {@link String} that contains travel duration.
     */
    @Override
    public void setTravelTime(String formatDuration) {

        this.travelTime = formatDuration;
        reprintStatus();
    }

    /**
     * Updates current average consumption. Reprints status.
     * @param fuelConsumption Well formatted {@link String} that contains average fuel consumption in L/100km.
     */
    @Override
    public void setAvgFuelConsumption(String fuelConsumption) {

        this.fuelConsumption = fuelConsumption;
        reprintStatus();
    }

    /**
     * Prints passed value as a state of cruise control in a simple log above program status.
     * @param isOn Determines cruse control state.
     */
    @Override
    public void setCruiseControlState(boolean isOn) {

        showMessage("Cruise control state: " + isOn, MessageType.INFO);
    }

    /**
     * Prints passed value as a state of left turn signal in a simple log above program status.
     * @param isOn Determines if left turn signal is on/off.
     */
    @Override
    public void setLeftTurnSignalLight(boolean isOn) {
        showMessage("Left turn state changed: " + isOn, MessageType.INFO);
    }

    /**
     * Prints passed value as a state of right turn signal in a simple log above program status.
     * @param isOn Determines if right turn signal is on/off.
     */
    @Override
    public void setRightTurnSignalLight(boolean isOn) {
        showMessage("Right turn state changed: " + isOn, MessageType.INFO);
    }

    /**
     * Prints passed value as a state of parking lights in a simple log above program status.
     * @param isOn Determines if parking lights are on/off.
     */
    @Override
    public void setParkingLight(boolean isOn) {
        showMessage("Parking lights state changed: " + isOn, MessageType.INFO);
    }


    /**
     * Print passed value as a state of low beam lights in a simple log above program status.
     * @param isOn Determines if low beam lights are on/off.
     */
    @Override
    public void setLowBeamLight(boolean isOn) {
        showMessage("Low beam lights state changed: " + isOn, MessageType.INFO);
    }

    /**
     * Print passed value as a state of high beam lights in a simple log above program status.
     * @param isOn Determines if high beam lights are on/off.
     */
    @Override
    public void setHighBeamLight(boolean isOn) {
        showMessage("High beam lights state changed: " + isOn, MessageType.INFO);
    }

    /**
     * Print passed value as a state of back fog light in a simple log above program status.
     * @param isOn Determines if back fog light is on/off.
     */
    @Override
    public void setBackFogLightState(boolean isOn) {
        showMessage("Back fog state changed: " + isOn, MessageType.INFO);
    }

    /**
     * Print passed value as a state of front fog light
     * @param isOn Determines if front fog light is on/off.
     */
    @Override
    public void setFrontFogLightState(boolean isOn) {
        showMessage("Front fog state changed: " + isOn, MessageType.INFO);
    }

    private void printNotSupported() {
        showMessage("Not supported in cli mode!", MessageType.ERROR);
    }

    /**
     * Not supported in console implementation.
     */
    @Override
    public void openStatsHistory() {
        printNotSupported();
    }

    /**
     * Not supported in console implementation.
     */
    @Override
    public void openSettings() {
        printNotSupported();
    }

    /**
     * Not supported in console implementation.
     */
    @Override
    public void openMp3() {
        printNotSupported();
    }

    /**
     * Updates current rpm. Reprints the status.
     * @param rpm Engines rpm.
     */
    @Override
    public void updateRpm(int rpm) {
        this.rpm = rpm;
        reprintStatus();
    }

    /**
     * Prints passed message in a simple log above program status.
     * @param message {@link String} that contains message to be shown.
     * @param type Enum that describes the level of concern.
     */
    @Override
    public void showMessage(String message, MessageType type) {
        clearStatus();
        System.out.println("[" + type  + "] " + message);
        printStatus();
    }

    /**
     * Not supported in console implementation.
     */
    @Override
    public void askUser(String message, Consumer<Boolean> onUserDecision) {
        printNotSupported();
    }

    private void reprintStatus() {
        clearStatus();
        printStatus();
    }

    private void clearStatus() {
        System.out.print("\r" + Stream.generate(() -> " ").limit(lastStatusLength).collect(Collectors.joining("")));
        System.out.print("\r");
    }

    private void printStatus() {

        String status =
                "Speed:" + speed
                        + "|Gear:" + gear
                        + "|RPM:" + rpm
                        + "|Mileage:" + totalMileage + " " + Arrays.toString(resettables)
                        + "|Avg speed:" + avgSpeed
                        + "|Max speed:" + maximumSpeed
                        + "|Distance:" + distance
                        + "|Duration:" + travelTime
                        + "|Fuel consumption:" + fuelConsumption;

        lastStatusLength = status.length();
        System.out.print("\r" + status);
    }

    /**
     * Disposes the subscription and detaches the presenter.
     */
    @Override
    public void close() {

        commandsSubscription.dispose();
        presenter.detach();
    }

    /**
     * Returns {@link Scheduler} for console view. It uses {@link Schedulers#single()}.
     * @return {@link Scheduler} for console view from {@link Schedulers#single()}.
     */
    @Override
    public Scheduler getViewScheduler() {
        return Schedulers.single();
    }
}
