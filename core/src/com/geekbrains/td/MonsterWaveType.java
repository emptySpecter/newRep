package com.geekbrains.td;

public enum MonsterWaveType {
    PAUSE(0,1000),
    SINGLE(30,100),
    WEEK(30,4f),
    STRONG(20,3.0f);

    int duration;
    float rate;

    public void setDuration(int duration) {
        this.duration = duration;
    }

     MonsterWaveType(int duration, float rate) {
        this.duration = duration;
        this.rate = rate;
    }
}
