package p.lodz.dashboardsimulator;

import p.lodz.dashboardsimulator.base.Activity;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardActivity;

public class Main {

    public static void main(String[] args) {

        Activity activity = new DashboardActivity();

        activity.start();
    }
}
