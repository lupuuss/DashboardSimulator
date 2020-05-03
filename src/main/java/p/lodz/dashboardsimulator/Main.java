package p.lodz.dashboardsimulator;

import p.lodz.dashboardsimulator.base.Activity;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardActivity;
import p.lodz.dashboardsimulator.utils.Utils;

public class Main {

    public static void main(String[] args) {

        System.out.println(Utils.round(0.009999, 3));

        Activity activity = new DashboardActivity();

        activity.start();
    }
}
