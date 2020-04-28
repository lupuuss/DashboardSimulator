package p.lodz.dashboardsimulator.model.engine;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simulates the behaviour of car engine.
 */
public class EngineSimulator implements Engine {

    private AtomicBoolean stopWorking = new AtomicBoolean(false);

    private final long noAcceleration = Double.doubleToLongBits(-1.0);
    private final long brakeAcceleration = Double.doubleToLongBits(-10.0);

    private final double accelerationConst;
    private final double maximumSpeed;

    private AtomicLong currentAcceleration = new AtomicLong(Double.doubleToLongBits(noAcceleration));
    private AtomicLong currentSpeed = new AtomicLong(Double.doubleToLongBits(0.0));

    private boolean acceleration = false;
    private boolean brake = false;

    private final long betweenTicks;

    private Observable<EngineState> engineState;

    public EngineSimulator(double acceleration, double maximumSpeed, long betweenTicks) {
        this.accelerationConst = acceleration;
        this.maximumSpeed = maximumSpeed;
        this.betweenTicks = betweenTicks;
    }

    @Override
    public final void launch() {

        engineState = Observable
                .interval(betweenTicks, TimeUnit.MILLISECONDS)
                .flatMap(timer -> {

                    double speedBeforeChange = Double.longBitsToDouble(currentSpeed.get());
                    double tmpSpeed = speedBeforeChange;

                    tmpSpeed += Double.longBitsToDouble(currentAcceleration.get());

                    if (tmpSpeed > maximumSpeed) {
                        currentSpeed.set(Double.doubleToLongBits(maximumSpeed));
                    } else if (tmpSpeed < 0.0){
                        currentSpeed.set(Double.doubleToLongBits(0.0));
                    } else {
                        currentSpeed.set(Double.doubleToLongBits(tmpSpeed));
                    }

                    double speedAfterChange = Double.longBitsToDouble(currentSpeed.get());


                    if (speedBeforeChange != speedAfterChange) {

                        return Observable.just(new EngineState(speedAfterChange, betweenTicks));
                    } else {
                        return Observable.empty();
                    }
                })
                .takeWhile((state) -> stopWorking.get())
                .subscribeOn(Schedulers.computation());
    }

    private void determineAcceleration() {

        if (brake) {
            currentAcceleration.set(brakeAcceleration);
        } else if (acceleration) {
            double accelerationValue = accelerationConst * betweenTicks / 1000;

            currentAcceleration.set(Double.doubleToLongBits(accelerationValue));
        } else {
            currentAcceleration.set(noAcceleration);
        }
    }

    @Override
    public final void setAcceleration(boolean isOn) {
        this.acceleration = isOn;
        determineAcceleration();
    }

    @Override
    public final void setBrake(boolean isOn) {
        this.brake = isOn;
        determineAcceleration();
    }

    @Override
    public final void stop() {
        stopWorking.set(true);
        engineState = null;
    }

    @Override
    public Observable<EngineState> getEngineState() {
        return engineState;
    }
}
