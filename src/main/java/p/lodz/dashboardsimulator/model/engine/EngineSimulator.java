package p.lodz.dashboardsimulator.model.engine;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import p.lodz.dashboardsimulator.utils.AtomicDouble;

import java.util.concurrent.TimeUnit;

/**
 * Simulates the behaviour of car engine.
 */
public class EngineSimulator implements Engine {

    private final double noAcceleration = -1.0;
    private final double brakeAcceleration = -10.0;

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
     *
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

                    return new EngineState(currentSpeed.get(), betweenTicks);
                })
                .subscribeOn(Schedulers.computation())
                .publish();

        sub = engineState.connect();
    }

    private void determineAcceleration() {

        if (brake) {
            currentAcceleration.set(brakeAcceleration);
        } else if (acceleration) {
            double accelerationValue = accelerationConst * betweenTicks / 1000;

            currentAcceleration.set(accelerationValue);
        } else {
            currentAcceleration.set(noAcceleration);
        }
    }

    /**
     * If true engine speeds up according to its acceleration constant. Otherwise, the speed drops to 0 with constant -1 km/s^2.
     * @param isOn Determines if engine should speed up or not.
     */
    @Override
    public final void setAcceleration(boolean isOn) {
        this.acceleration = isOn;
        determineAcceleration();
    }

    /**
     * If true engine ignores acceleration state (on/off) and its speed drops to 0 with constant -10 km/s^2
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
     * Returns observable engine state.
     * @return {@link Observable} object that allows to watch {@link EngineState}.
     */
    @Override
    public Observable<EngineState> getEngineState() {
        return engineState;
    }
}
