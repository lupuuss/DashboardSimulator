package p.lodz.dashboardsimulator.model.engine;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import p.lodz.dashboardsimulator.utils.AtomicDouble;

import java.util.concurrent.TimeUnit;

/**
 * Simulates the behaviour of car engine. Speed increases according o acceleration const.
 * Rmp and current gear depends only on speed level. Max speed equals to maximum rpm and
 * gear and minimum speed equals to minimum rpm and gear. Engine simulates automatic 5-speed gear-box.
 * When rpm reaches min level and gear equals to 1, engine sets idle mode (gear 0).
 */
public class EngineSimulator implements Engine {

    private final double noAcceleration = -10.0;
    private final double brakeAcceleration = -50.0;

    private final int minRpm = 1000;
    private final int switchRpm = 4000;
    private final int maxRpm = 6000;

    private final double minConsumption = 8;
    private final double rpmModifier = 6;
    private final double speedModifier = 1;

    private final int maxGear = 5;

    private final int rpmLeftover = maxRpm - minRpm;
    private final int perGearRpm = switchRpm - minRpm;
    private final int rpmSum = perGearRpm * (maxGear - 1) + rpmLeftover;

    private final double accelerationConst;
    private final double maximumSpeed;

    private AtomicDouble currentAcceleration = new AtomicDouble(noAcceleration);
    private AtomicDouble currentSpeed = new AtomicDouble(0.0);

    private boolean acceleration = false;
    private boolean brake = false;

    private final long betweenTicks;

    private ConnectableObservable<EngineState> engineState;
    private Disposable sub;

    /**
     * Initializes new EngineSimiulator. Before any other operations {@link EngineSimulator#launch()} must be called.
     * @param acceleration Determines a constant engine accceleration.
     * @param maximumSpeed Sets maximum speed that is allowed for this engine.
     * @param betweenTicks Determines how often engine should propagate its state.
     */
    public EngineSimulator(double acceleration, double maximumSpeed, long betweenTicks) {
        this.accelerationConst = acceleration;
        this.maximumSpeed = maximumSpeed;
        this.betweenTicks = betweenTicks;
    }

    /**
     * Creates observable and starts to publishing engine state. Engine is considerd “turned on”.
     */
    @Override
    public final void launch() {

        engineState = Observable
                .interval(betweenTicks, TimeUnit.MILLISECONDS)
                .map(timer -> {

                    double tmpSpeed = currentSpeed.get();

                    tmpSpeed += currentAcceleration.get();

                    if (tmpSpeed > maximumSpeed) {
                        currentSpeed.set(maximumSpeed);
                    } else {
                        currentSpeed.set(Math.max(tmpSpeed, 0.0));
                    }

                    int totalRpm = (int) (rpmSum * (currentSpeed.get() / maximumSpeed));

                    int tmpRpm;

                    if (totalRpm > (maxGear - 1) * perGearRpm) {
                        tmpRpm = minRpm + totalRpm - (maxGear - 1) * perGearRpm;
                    } else {
                        tmpRpm = minRpm + totalRpm % perGearRpm;
                    }

                    int tmpGear = totalRpm / perGearRpm + 1;

                    if (tmpGear > maxGear) {
                        tmpGear = maxGear;
                    }

                    if (tmpGear == 1 && tmpRpm == minRpm) {
                        tmpGear = 0;
                    }

                    double fuelConsumption = minConsumption
                                    + ((double) tmpRpm / maxRpm) * rpmModifier
                                    + (tmpSpeed / maximumSpeed) * speedModifier;

                    return new EngineState(currentSpeed.get(), betweenTicks, tmpRpm, tmpGear, fuelConsumption);
                })
                .subscribeOn(Schedulers.single())
                .publish();

        sub = engineState.connect();
    }

    private void determineAcceleration() {

        if (brake) {
            currentAcceleration.set(brakeAcceleration * betweenTicks / 1000);
        } else if (acceleration) {
            double accelerationValue = accelerationConst * betweenTicks / 1000;

            currentAcceleration.set(accelerationValue);
        } else {
            currentAcceleration.set(noAcceleration * betweenTicks / 1000);
        }
    }

    /**
     * If true engine speeds up according to its acceleration constant. Otherwise, the speed drops to 0 with constant -10 km/s^2.
     * @param isOn Determines if engine should speed up or not.
     */
    @Override
    public final void setAcceleration(boolean isOn) {
        this.acceleration = isOn;
        determineAcceleration();
    }

    /**
     * If true engine ignores acceleration state (on/off) and its speed drops to 0 with constant -50 km/s^2
     * @param isOn Determines if engine brakes are on/off.
     */
    @Override
    public final void setBrake(boolean isOn) {
        this.brake = isOn;
        determineAcceleration();
    }

    /**
     * Internal observable stops publishing and engine is considered “turned off”.
     */
    @Override
    public final void stop() {
        sub.dispose();
    }

    /**
     * Returns observable engine state. The way it changes is described in class description.
     * @return {@link Observable} object that allows to watch {@link EngineState}.
     */
    @Override
    public Observable<EngineState> getEngineState() {
        return engineState;
    }
}
