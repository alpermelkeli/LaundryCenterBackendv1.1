package org.alpermelkeli.api.rest;

import org.alpermelkeli.model.Company;
import org.alpermelkeli.service.CompanyService;
import org.alpermelkeli.service.MachineTimeController;
import org.alpermelkeli.service.MqttControllerService;
import org.alpermelkeli.utils.RequiresApiKey;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v1/api/companies")
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private MqttControllerService mqttControllerService;
    @Autowired
    private MachineTimeController machineTimeController;

    @RequiresApiKey
    @GetMapping("/getCompanies")
    public List<Company> getAllCompanies() {
        return companyService.getCompanies();
    }

    @RequiresApiKey
    @GetMapping("/getCompanyPrice")
    public double getCompanyPrice(@RequestParam String companyId) {
        return companyService.getCompanyPrice(companyId);
    }
    @RequiresApiKey
    @GetMapping("/getDevices")
    public List<Company.Device> getDevices(@RequestParam String companyId) {
        return companyService.findDevicesByCompanyId(companyId);
    }
    @RequiresApiKey
    @GetMapping("/getMachine")
    public Company.Device.Machine getMachine(@RequestParam String companyId, @RequestParam String deviceId, @RequestParam String machineId) {
        return companyService.getMachine(companyId, deviceId, machineId);
    }
    @RequiresApiKey
    @PostMapping("/increaseMachineTime")
    public void increaseMachineTime(@RequestParam String companyId, @RequestParam String deviceId, @RequestParam String machineId, @RequestParam long time) {
        companyService.increaseMachineTime(companyId, deviceId, machineId, time, ((currentTimeMillis, durationTime) -> {
            machineTimeController.startWatchingDevice(companyId, deviceId, machineId, currentTimeMillis, durationTime, (cId, dId, mId) -> {
                mqttControllerService.sendTurnOnOffCommand(dId, mId, "OFF");
                companyService.updateMachineStatus(cId, dId, mId, false);
            });
        }));

    }

    @RequiresApiKey
    @PostMapping("/turnOn")
    public String turnOnRelay(@RequestParam String companyId, @RequestParam String deviceId, @RequestParam String relayNo, @RequestParam String time) {
        mqttControllerService.sendTurnOnOffCommand(deviceId, relayNo, "ON");
        companyService.updateMachineStatus(companyId, deviceId, relayNo, true);
        long currentTimeMillis = System.currentTimeMillis();
        companyService.updateMachineTime(companyId, deviceId, relayNo, currentTimeMillis, Long.parseLong(time));
        machineTimeController.startWatchingDevice(companyId, deviceId, relayNo, currentTimeMillis, Long.parseLong(time), (cId, dId, mId) -> {
            mqttControllerService.sendTurnOnOffCommand(dId, mId, "OFF");
            companyService.updateMachineStatus(cId, dId, mId, false);
        });
        return "Success";
    }
    @RequiresApiKey
    @PostMapping("/turnOff")
    public String turnOffRelay(@RequestParam String companyId, @RequestParam String deviceId, @RequestParam String relayNo) {
        mqttControllerService.sendTurnOnOffCommand(deviceId, relayNo, "OFF");
        companyService.updateMachineStatus(companyId, deviceId, relayNo, false);
        return "Success";
    }
    @RequiresApiKey
    @GetMapping("/getTime")
    public long getTime(){
        return System.currentTimeMillis();
    }
}
