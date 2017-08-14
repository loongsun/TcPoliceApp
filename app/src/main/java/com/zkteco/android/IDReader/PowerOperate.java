package com.zkteco.android.IDReader;

import com.android.charger.EmGpio;

public class PowerOperate
{
    private static final int FINGERPRINT_MODULE_5V_PIN = 4;
    private static final int PREMISE_5V_PIN = 137;
    private static final int RFID_5V_PIN = 95;
    private static final int VOLT_3_3_PIN = 136;

    private void mtSetGPIOValue(int paramInt, boolean paramBoolean)
    {
        if (paramInt < 0) {
            return;
        }
        EmGpio.gpioInit();
        EmGpio.setGpioMode(paramInt);
        if (paramBoolean)
        {
            EmGpio.setGpioOutput(paramInt);
            EmGpio.setGpioDataHigh(paramInt);
        }
        for (;;)
        {
            EmGpio.gpioUnInit();
            return;
            //EmGpio.setGpioOutput(paramInt);
            //EmGpio.setGpioDataLow(paramInt);
        }
    }

    public void disableFingerprintModule_5Volt() {}

    public void disablePremise_5Volt() {}

    public void disableRfid_5Volt()
    {
        mtSetGPIOValue(95, false);
    }

    public void disable_3_3Volt() {}

    public void enableFingerprintModule_5Volt() {}

    public void enablePremise_5Volt() {}

    public void enableRfid_5Volt()
    {
        mtSetGPIOValue(95, true);
    }

    public void enable_3_3Volt() {}
}
