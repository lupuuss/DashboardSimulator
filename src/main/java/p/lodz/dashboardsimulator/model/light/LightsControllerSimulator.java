package p.lodz.dashboardsimulator.model.light;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Simulates standard behaviour of lights in every car.
 */
public class LightsControllerSimulator implements LightsController {

    private Subject<LightsMode> lightsModeSubject = PublishSubject.create();
    private AtomicReference<LightsMode> lightsMode = new AtomicReference<>(LightsMode.OFF);

    private Subject<Boolean> fogBackLightsSubject = PublishSubject.create();
    private Subject<Boolean> fogFrontLightsSubject = PublishSubject.create();

    private AtomicBoolean fogFrontLightsState = new AtomicBoolean(false);
    private AtomicBoolean fogBackLightsState = new AtomicBoolean(false);

    private Subject<Boolean> leftTurnSubject = PublishSubject.create();
    private Subject<Boolean> rightTurnSubject = PublishSubject.create();

    private AtomicBoolean leftTurnTrigger = new AtomicBoolean(false);
    private AtomicBoolean rightTurnTrigger = new AtomicBoolean(false);

    private Thread leftThread;
    private Thread rightThread;

    public LightsControllerSimulator() {
        lightsModeSubject.onNext(LightsMode.OFF);
        fogBackLightsSubject.onNext(false);
        fogFrontLightsSubject.onNext(false);
        leftTurnSubject.onNext(false);
        leftTurnSubject.onNext(false);
    }

    private long betweenBlinks = 500;
    private long blinkTime = 300;

    @Override
    public void triggerLeftTurnSignal() {

        negateAtomic(leftTurnTrigger);

        if (!leftTurnTrigger.get()) {
            return;
        }

        rightTurnTrigger.set(false);

        if (rightThread != null && rightThread.isAlive()) {
            rightThread.interrupt();
        }

        rightThread = createBlinkingThread(leftTurnTrigger, leftTurnSubject);
        rightThread.start();
    }

    @Override
    public void triggerRightTurnSignal() {
        negateAtomic(rightTurnTrigger);

        if (!rightTurnTrigger.get()) {
            return;
        }

        leftTurnTrigger.set(false);

        if (leftThread != null && leftThread.isAlive()) {
            leftThread.interrupt();
        }

        leftThread = createBlinkingThread(rightTurnTrigger, rightTurnSubject);
        leftThread.start();
    }

    private void negateAtomic(AtomicBoolean atomicBoolean) {
        boolean temp;
        do {
            temp = atomicBoolean.get();
        } while(!atomicBoolean.compareAndSet(temp, !temp));
    }


    private Thread createBlinkingThread(AtomicBoolean controller, Subject<Boolean> subject) {
        return new Thread(() -> {

            try {

                while (controller.get()) {

                    subject.onNext(true);
                    Thread.sleep(blinkTime);

                    subject.onNext(false);
                    Thread.sleep(betweenBlinks);
                }

            } catch (InterruptedException e) {
                System.out.println("Thread interrupted!");
            }

        });
    }

    @Override
    public void setMainLightMode(LightsMode mode) {
        lightsMode.set(mode);
        lightsModeSubject.onNext(mode);
    }

    @Override
    public void setFogFrontLights(boolean areOn) {
        fogFrontLightsState.set(areOn);
        fogFrontLightsSubject.onNext(areOn);
    }

    @Override
    public void setFogBackLights(boolean areOn) {
        fogBackLightsState.set(areOn);
        fogBackLightsSubject.onNext(areOn);
    }

    @Override
    public Observable<Boolean> getLeftTurnState() {
        return leftTurnSubject;
    }

    @Override
    public Observable<Boolean> getRightTurnState() {
        return rightTurnSubject;
    }

    @Override
    public Observable<LightsMode> getMainLightMode() {
        return lightsModeSubject;
    }

    @Override
    public Observable<Boolean> getFogBackLightsState() {
        return fogBackLightsSubject;
    }

    @Override
    public Observable<Boolean> getFogFrontLightsState() {
        return fogFrontLightsSubject;
    }

    @Override
    public boolean isFogFrontLightOn() {
        return fogFrontLightsState.get();
    }

    @Override
    public boolean isFogBackLightOn() {
        return fogBackLightsState.get();
    }

    @Override
    public LightsMode getCurrentLightsMode() {
        return lightsMode.get();
    }
}
