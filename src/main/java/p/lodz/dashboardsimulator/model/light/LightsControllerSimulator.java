package p.lodz.dashboardsimulator.model.light;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Simulates standard behaviour of lights in every car. It handles main lights, fog lighs and turn lights.
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

    /**
     * Initializes all lights states to off.
     */
    public LightsControllerSimulator() {
        lightsModeSubject.onNext(LightsMode.OFF);
        fogBackLightsSubject.onNext(false);
        fogFrontLightsSubject.onNext(false);
        leftTurnSubject.onNext(false);
        leftTurnSubject.onNext(false);
    }

    private long betweenBlinks = 500;
    private long blinkTime = 300;

    /**
     * Switches between on/off state of left turn signal. Initial state is off.
     * After setting to on, turn signal blinks, and right turn singal state is set to off.
     * Blink last for 300 ms and pause between blinks last for 500ms.
     */
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

    /**
     * Switches between on/off state of right turn signal. Initial state is off.
     * After setting to on, turn signal blinks, and left turn singal state is set to off.
     * Blink last for 300 ms and pause between blinks last for 500ms.
     */
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

    /**
     * Changes mode of the main lights. Initial mode is {@link LightsMode#OFF}
     * @param mode Determines lights mode with enum {@link LightsMode}.
     */
    @Override
    public void setMainLightMode(LightsMode mode) {
        lightsMode.set(mode);
        lightsModeSubject.onNext(mode);
    }

    /**
     * Switches between on/off state of front fog lights. Initial state is off.
     * @param areOn Determines state of front fog lights (on/off).
     */
    @Override
    public void setFogFrontLights(boolean areOn) {
        fogFrontLightsState.set(areOn);
        fogFrontLightsSubject.onNext(areOn);
    }

    /**
     * Switches between on/off state of back fog lights. Initial state is off.
     * @param areOn Determines state of back fog lights (on/off).
     */
    @Override
    public void setFogBackLights(boolean areOn) {
        fogBackLightsState.set(areOn);
        fogBackLightsSubject.onNext(areOn);
    }

    /**
     * Returns observable state of left turn signal.
     * @return Instance of {@link Observable} that allows to watch the state of left turn signal.
     */
    @Override
    public Observable<Boolean> getLeftTurnState() {
        return leftTurnSubject;
    }

    /**
     * Returns observable state of right turn signal.
     * @return Instance of {@link Observable} that allows to watch the state of right turn signal.
     */
    @Override
    public Observable<Boolean> getRightTurnState() {
        return rightTurnSubject;
    }

    /**
     * Returns observable mode of main lights.
     * @return Instance of {@link Observable} that allows to watch the mode of the main lights.
     */
    @Override
    public Observable<LightsMode> getMainLightMode() {
        return lightsModeSubject;
    }

    /**
     * Returns observable state of fog front lights.
     * @return Instance of {@link Observable} that allows to watch the state of fog back lights.
     */
    @Override
    public Observable<Boolean> getFogBackLightsState() {
        return fogBackLightsSubject;
    }

    /**
     * Returns observable state of fog back lights.
     * @return Instance of {@link Observable} that allows to watch the state of fog front lights.
     */
    @Override
    public Observable<Boolean> getFogFrontLightsState() {
        return fogFrontLightsSubject;
    }

    /**
     * Returns last state of fog front light.
     * @return Last state of fog front light.
     */
    @Override
    public boolean isFogFrontLightOn() {
        return fogFrontLightsState.get();
    }

    /**
     * Returns last state of fog back light.
     * @return Last state of fog back light.
     */
    @Override
    public boolean isFogBackLightOn() {
        return fogBackLightsState.get();
    }

    /**
     * Returns last lights mode.
     * @return Last lights mode.
     */
    @Override
    public LightsMode getCurrentLightsMode() {
        return lightsMode.get();
    }
}
