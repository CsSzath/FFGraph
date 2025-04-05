package com.thau.ffgraph;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import com.thau.DataModel.DataRecord;

public class Calculations {

    public static String getTotalTime(ArrayList<DataRecord> records) {
        if (records == null || records.isEmpty()) {
            return "No data available";
        }

        DataRecord lastRecord = records.get(records.size() - 1);

        long timeDifferenceMillis = lastRecord.getElapsedTimeMs();
        long hours = TimeUnit.MILLISECONDS.toHours(timeDifferenceMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifferenceMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifferenceMillis) % 60;
        //System.out.println(records.get(records.size() - 1));
        //System.out.println("Elapsed time in milliseconds: " + timeDifferenceMillis);
        //System.out.println(hours + " hours, " + minutes + " minutes, " + seconds + " seconds");
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String getElapsedTime(DataRecord record) {

        long elapsedTimeMillis = record.getElapsedTimeMs();
        long hours = TimeUnit.MILLISECONDS.toHours(elapsedTimeMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMillis) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


    public static String getHighestCoreTemp(ArrayList<DataRecord> records) {
        if (records == null || records.isEmpty()) {
            return "No data available";
        }

        double highestCoreTemp = Double.MIN_VALUE;
        double actualCoreTemp;

        for (DataRecord record : records) {
            actualCoreTemp = record.getCoreTemp() / 10;
            if (actualCoreTemp > highestCoreTemp) {
                highestCoreTemp = actualCoreTemp;
            }
        }
        return String.format("%.1f", highestCoreTemp) + " °C";
    }



    public static String countRecordsAboveCoreTemp(ArrayList<DataRecord> records, int threshold) {
        if (records == null || records.isEmpty()) {
            return "No data available";
        }

        int count = 0;
        for (DataRecord record : records) {
            if ((int) record.getCoreTemp() > threshold * 10) {
                count++;
            }
        }
        return count + " perc";
    }


}
