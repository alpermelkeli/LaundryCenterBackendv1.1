package org.alpermelkeli.service;

import org.alpermelkeli.model.CompanyEntity;
import org.alpermelkeli.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;


    public List<CompanyEntity.Device> findDevicesByCompanyId(String companyId) {
        Optional<CompanyEntity> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isPresent()) {
            CompanyEntity companyEntity = companyOpt.get();
            return companyEntity.getDevices();
        }
        return null;
    }

    public List<CompanyEntity> getCompanies() {
        return companyRepository.findAll();
    }

    public double getCompanyPrice(String companyId) {
        Optional<CompanyEntity> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isPresent()) {
            CompanyEntity companyEntity = companyOpt.get();
            return companyEntity.getPrice();
        }
        return 0;
    }

    public CompanyEntity.Device.Machine getMachine(String companyId, String deviceId, String machineId){
        Optional<CompanyEntity> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isPresent()) {
            CompanyEntity companyEntity = companyOpt.get();
            List<CompanyEntity.Device> devices = companyEntity.getDevices();
            for (CompanyEntity.Device device : devices) {
                if (device.getId().equals(deviceId)) {
                    List<CompanyEntity.Device.Machine> machines = device.getMachines();
                    for (CompanyEntity.Device.Machine machine : machines) {
                        if (machine.getId().equals(machineId)) {
                            return machine;
                        }
                    }
                }
            }
        }
        return null;
    }

    public void increaseMachineTime(String companyId, String deviceId, String machineId, long time, OnTimeUpdatedCallback callback){
        Optional<CompanyEntity> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isPresent()) {
            CompanyEntity companyEntity = companyOpt.get();
            List<CompanyEntity.Device> devices = companyEntity.getDevices();
            for (CompanyEntity.Device device : devices) {
                if (device.getId().equals(deviceId)) {
                    List<CompanyEntity.Device.Machine> machines = device.getMachines();
                    for (CompanyEntity.Device.Machine machine : machines) {
                        if (machine.getId().equals(machineId)) {
                            long newDuration = machine.getDuration() + time;
                            long currentTime = machine.getStartTime();
                            machine.setDuration(newDuration);
                            companyRepository.save(companyEntity);
                            callback.onTimeUpdated(currentTime, newDuration);
                        }
                    }
                }
            }
        }
    }

    public interface OnTimeUpdatedCallback{
        void onTimeUpdated(long currentTimeMillis, long durationTime);
    }

    public void updateDeviceStatus(String deviceId, boolean status){
        List<CompanyEntity> companies = companyRepository.findAll();
        for (CompanyEntity companyEntity : companies) {
            List<CompanyEntity.Device> devices = companyEntity.getDevices();
            for (CompanyEntity.Device device : devices) {
                if (device.getId().equals(deviceId)) {
                    device.setStatus(status ? "connected" : "disconnected");
                    companyRepository.save(companyEntity);
                }
            }
        }
    }

    public void updateMachineStatus(String companyId, String deviceId, String machineId, boolean status){
        Optional<CompanyEntity> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isPresent()) {
            CompanyEntity companyEntity = companyOpt.get();
            List<CompanyEntity.Device> devices = companyEntity.getDevices();
            for (CompanyEntity.Device device : devices) {
                if (device.getId().equals(deviceId)) {
                    List<CompanyEntity.Device.Machine> machines = device.getMachines();
                    for (CompanyEntity.Device.Machine machine : machines) {
                        if (machine.getId().equals(machineId)) {
                            machine.setActive(status);
                            companyRepository.save(companyEntity);
                        }
                    }
                }
            }
        }
    }

    public void updateMachineTime(String companyId , String deviceId, String machineId, long currentTimeMillis, long time){
        Optional<CompanyEntity> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isPresent()) {
            CompanyEntity companyEntity = companyOpt.get();
            List<CompanyEntity.Device> devices = companyEntity.getDevices();
            for (CompanyEntity.Device device : devices) {
                if (device.getId().equals(deviceId)) {
                    List<CompanyEntity.Device.Machine> machines = device.getMachines();
                    for (CompanyEntity.Device.Machine machine : machines) {
                        if (machine.getId().equals(machineId)) {
                            machine.setStartTime(currentTimeMillis);
                            machine.setDuration(time);
                            companyRepository.save(companyEntity);
                        }
                    }
                }
            }
        }
    }


}
