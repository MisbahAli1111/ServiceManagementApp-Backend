package com.qavi.carmaintanence.business.services;

import com.qavi.carmaintanence.business.entities.Invoice;
import com.qavi.carmaintanence.business.entities.MaintenanceRecord;
import com.qavi.carmaintanence.business.repositories.MaintenanceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class MaintenanceRecordService {

    @Autowired
    MaintenanceRecordRepository maintenanceRecordRepository;

    public boolean addRecord(MaintenanceRecord maintenanceRecord)
    {
        maintenanceRecord.setMaintanenceDateTime(LocalDateTime.now());
        maintenanceRecordRepository.save(maintenanceRecord);


        return true;
    }

    public List<MaintenanceRecord> getallrecords() {
        List<MaintenanceRecord> myRecords= maintenanceRecordRepository.findAll();
        return myRecords;
    }

    public Optional<MaintenanceRecord> getMaintenanceRecordById(Long Id) {
        Optional<MaintenanceRecord> myRecords =maintenanceRecordRepository.findById(Id);

        return myRecords;
    }

    public boolean editMaintenanceRecord(MaintenanceRecord maintenanceRecord,Long Id) {
        MaintenanceRecord foundRecord =maintenanceRecordRepository.findById(Id).get();
        //Service
        if(Objects.nonNull(maintenanceRecord.getService()) &&
                !"".equals(maintenanceRecord.getService()))
        {
            foundRecord.setService(maintenanceRecord.getService());
        }
        if(Objects.nonNull(maintenanceRecord.getMaintanenceDateTime()) &&
                !"".equals(maintenanceRecord.getMaintanenceDateTime()))
        {
            foundRecord.setMaintanenceDateTime(maintenanceRecord.getMaintanenceDateTime());
        }


        //Kilometers
        if(Objects.nonNull(maintenanceRecord.getKilometerDriven()) &&
                !"".equals(maintenanceRecord.getKilometerDriven()))
        {
            foundRecord.setKilometerDriven(maintenanceRecord.getKilometerDriven());
        }


        //MaintenanceDetails
        if(Objects.nonNull(maintenanceRecord.getMaintanenceDetail()) &&
                !"".equals(maintenanceRecord.getMaintanenceDetail()))
        {
            foundRecord.setMaintanenceDetail(maintenanceRecord.getMaintanenceDetail());
        }

        if(foundRecord != null)
        {
            maintenanceRecordRepository.save(foundRecord);
            return true;
        }
        else {
            return false;
        }

    }
}
