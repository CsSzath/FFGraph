
package com.thau.DataModel;

public class DataRecord {
    
    private int id;
    private int date;
    private double time;
    private int milliseconds;
    private int dayOfWeek;
    private long elapsedTimeMs;
    private int subInterval;
    private int triggered;
    private int airTemp;
    private int coreTemp;
    private int humidity;
    private int stepType;
    private int setAirTemp;
    private int setCoreTemp;
    private int setHumidity;

    public DataRecord(int id, int date, double time, int milliseconds, int dayOfWeek, long elapsedTimeMs, int subInterval, 
                        int triggered, int airTemp, int coreTemp, int humidity, int stepType, int setAirTemp, int setCoreTemp, int setHumidity) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.milliseconds = milliseconds;
        this.dayOfWeek = dayOfWeek;
        this.elapsedTimeMs = elapsedTimeMs;
        this.subInterval = subInterval;
        this.triggered = triggered;
        this.airTemp = airTemp;
        this.coreTemp = coreTemp;
        this.humidity = humidity;
        this.stepType = stepType;
        this.setAirTemp = setAirTemp;
        this.setCoreTemp = setCoreTemp;
        this.setHumidity = setHumidity;
                        }                        

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public int getDate() {
        return date;
    }
    public double getTime() {
        return time;
    }
    public int getMilliseconds() {
       return milliseconds;
    }
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    public long getElapsedTimeMs() {
        return elapsedTimeMs;
    }
    public int getSubInterval() {
        return subInterval;
    }
    public int getTriggered() {
        return triggered;
    }
    public int getAirTemp() {
        return airTemp;
    }
    public int getCoreTemp() {
        return coreTemp;
    }
    public int getHumidity() {
        return humidity;
    }
    public int getStepType() {
        return stepType;
    }
    public int getSetAirTemp() {
       return setAirTemp;
    }
    public int getSetCoreTemp() {
        return setCoreTemp;
    }
    public int getSetHumidity() {
        return setHumidity;
    }

    @Override
    public String toString() {
        return "DataRecord{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", milliseconds=" + milliseconds +
                ", dayOfWeek=" + dayOfWeek +
                ", elapsedTimeMs=" + elapsedTimeMs +
                ", subInterval=" + subInterval +
                ", triggered=" + triggered +
                ", airTemp=" + airTemp +
                ", coreTemp=" + coreTemp +
                ", humidity=" + humidity +
                ", stepType=" + stepType +
                ", setAirTemp=" + setAirTemp +
                ", setCoreTemp=" + setCoreTemp +
                ", setHumidity=" + setHumidity +
                '}';
        
    }
}
