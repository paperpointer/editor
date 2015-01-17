package yk.myengine.visualrealm;

import yk.myengine.optiseq.states.SimplestLightState;

/**
 * Created by: Yuri Kravchik
 * Date: 8/11/2007
 * Time: 17:06:52
 */
public class Resources {
    private SimplestLightState simplestLightState;

    public Resources() {
        simplestLightState = new SimplestLightState();
    }

    public SimplestLightState getSimplestLightState() {
        return simplestLightState;
    }
}
