package org.alpermelkeli.service;


import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MachineTimeController {

    private final Map<String, ScheduledExecutorService> deviceTimers = new ConcurrentHashMap<>();


    public void startWatchingDevice(String companyId, String deviceId, String machineId, long startTimeInMillis, long durationInMillis, OnTimeIsDoneCallback callback) {
        stopWatchingDevice(deviceId+machineId);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        deviceTimers.put(deviceId+machineId, scheduler);

        long currentTimeInMillis = System.currentTimeMillis();
        long remainingTimeInMillis = (startTimeInMillis + durationInMillis) - currentTimeInMillis;

        if (remainingTimeInMillis > 0) {
            scheduler.schedule(() -> {
                stopWatchingDevice(deviceId);
                System.out.println("Device " + deviceId + " turned off after " + remainingTimeInMillis + " milliseconds.");
                callback.onTimeIsDone(companyId, deviceId, machineId);
            }, remainingTimeInMillis, TimeUnit.MILLISECONDS);
        } else {
            stopWatchingDevice(deviceId);
            System.out.println("Device " + deviceId + " should already have been turned off.");
            callback.onTimeIsDone(companyId, deviceId, machineId);
        }
    }


    public void stopWatchingDevice(String deviceId_machineId) {
        ScheduledExecutorService scheduler = deviceTimers.remove(deviceId_machineId);
        if (scheduler != null) {
            scheduler.shutdownNow();
            System.out.println("Device " + deviceId_machineId + " tracking stopped.");
        }
    }


    public interface OnTimeIsDoneCallback {
        void onTimeIsDone(String companyId, String deviceId, String machineId);
    }
}

