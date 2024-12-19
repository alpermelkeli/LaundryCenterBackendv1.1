package org.alpermelkeli.service;

import org.alpermelkeli.model.Company;
import org.alpermelkeli.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;


    public List<Company.Device> findDevicesByCompanyId(String companyId) {
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            return company.getDevices();
        }
        return null;
    }

    public List<Company> getCompanies() {
        return companyRepository.findAll();
    }

    public double getCompanyPrice(String companyId) {
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            return company.getPrice();
        }
        return 0;
    }

    public Company.Device.Machine getMachine(String companyId, String deviceId, String machineId){
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            List<Company.Device> devices = company.getDevices();
            for (Company.Device device : devices) {
                if (device.getId().equals(deviceId)) {
                    List<Company.Device.Machine> machines = device.getMachines();
                    for (Company.Device.Machine machine : machines) {
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
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            List<Company.Device> devices = company.getDevices();
            for (Company.Device device : devices) {
                if (device.getId().equals(deviceId)) {
                    List<Company.Device.Machine> machines = device.getMachines();
                    for (Company.Device.Machine machine : machines) {
                        if (machine.getId().equals(machineId)) {
                            long newDuration = machine.getDuration() + time;
                            long currentTime = machine.getStartTime();
                            machine.setDuration(newDuration);
                            companyRepository.save(company);
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
        List<Company> companies = companyRepository.findAll();
        for (Company company : companies) {
            List<Company.Device> devices = company.getDevices();
            for (Company.Device device : devices) {
                if (device.getId().equals(deviceId)) {
                    device.setStatus(status ? "connected" : "disconnected");
                    companyRepository.save(company);
                }
            }
        }
    }

    public void updateMachineStatus(String companyId, String deviceId, String machineId, boolean status){
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            List<Company.Device> devices = company.getDevices();
            for (Company.Device device : devices) {
                if (device.getId().equals(deviceId)) {
                    List<Company.Device.Machine> machines = device.getMachines();
                    for (Company.Device.Machine machine : machines) {
                        if (machine.getId().equals(machineId)) {
                            machine.setActive(status);
                            companyRepository.save(company);
                        }
                    }
                }
            }
        }
    }

    public void updateMachineTime(String companyId , String deviceId, String machineId, long currentTimeMillis, long time){
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            List<Company.Device> devices = company.getDevices();
            for (Company.Device device : devices) {
                if (device.getId().equals(deviceId)) {
                    List<Company.Device.Machine> machines = device.getMachines();
                    for (Company.Device.Machine machine : machines) {
                        if (machine.getId().equals(machineId)) {
                            machine.setStartTime(currentTimeMillis);
                            machine.setDuration(time);
                            companyRepository.save(company);
                        }
                    }
                }
            }
        }
    }


}
