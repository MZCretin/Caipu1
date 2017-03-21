package com.cretin.www.caipu.eventbus;

public class NotifyLoginRegisterChange {
    public static final int STATE_LOGIN = 0;
    public static final int STATE_REGISTERS = 1;
    private int currStation = STATE_LOGIN;

    private String phone;

    public static int getStateLogin() {
        return STATE_LOGIN;
    }

    public NotifyLoginRegisterChange(int currStation, String phone) {
        this.currStation = currStation;
        this.phone = phone;
    }

    public int getCurrStation() {
        return currStation;
    }

    public void setCurrStation(int currStation) {
        this.currStation = currStation;
    }
}
