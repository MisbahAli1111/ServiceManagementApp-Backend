package com.qavi.carmaintanence.business.services;

import com.qavi.carmaintanence.business.entities.Business;
import com.qavi.carmaintanence.business.entities.BusinessMedia;
import com.qavi.carmaintanence.business.entities.MaintenanceRecord;
import com.qavi.carmaintanence.business.repositories.BusinessMediaRepository;
import com.qavi.carmaintanence.business.repositories.BusinessRepository;
import com.qavi.carmaintanence.globalexceptions.RecordNotFoundException;
import com.qavi.carmaintanence.usermanagement.entities.role.Role;
import com.qavi.carmaintanence.usermanagement.entities.user.ProfileImage;
import com.qavi.carmaintanence.usermanagement.entities.user.User;
import com.qavi.carmaintanence.usermanagement.repositories.ProfileImageRepository;
import com.qavi.carmaintanence.usermanagement.repositories.RoleRepository;
import com.qavi.carmaintanence.usermanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BusinessService {
    @Autowired
    BusinessRepository businessRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ProfileImageRepository profileImageRepository;

    @Autowired
    BusinessMediaRepository businessMediaRepository;

    public Long addBusiness(String id, Business business)
    {
        try
        {
            Optional<User> owner = userRepository.findById(Long.valueOf(id));
            if (owner.isPresent()) {
                Role role=roleRepository.searchByName("ROLE_OWNER");
                if(!owner.get().getRole().stream().toList().contains(role))
                {
                    System.out.println(owner.get().getRole());
                    throw new RuntimeException("User is not registered as owner");
                }
                else {
                    business.setOwner(owner.get());
                }
                business.setEnabled(true);
                Business business1 = businessRepository.save(business);
                return business1.getId();


            } else {
                throw new RecordNotFoundException("User doesn't exists");
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            return null;
        }
    }

    public List<Business> getMyBusinesses(String id) {
        List<Business> fetchedBusiness=businessRepository.findAllByOwnerId(Long.valueOf(id));
        return fetchedBusiness;
    }
    public List<Business> getAllBusiness() {
        List <Business> businesses=businessRepository.findAll();
        return businesses;
    }


    public boolean editBusiness(Business business, Long id) {
        Business foundBusinessRecord =businessRepository.findById(id).get();
        //Service
        if(Objects.nonNull(foundBusinessRecord.getBusinessCity()) &&
                !foundBusinessRecord.getBusinessCity().isEmpty())
        {
            foundBusinessRecord.setBusinessCity(foundBusinessRecord.getBusinessCity());
        }

        if(Objects.nonNull(foundBusinessRecord.getBusinessAddress()) &&
                !foundBusinessRecord.getBusinessAddress().isEmpty())
        {
            foundBusinessRecord.setBusinessAddress(foundBusinessRecord.getBusinessAddress());
        }

        if(Objects.nonNull(foundBusinessRecord.getBusinessCountry()) &&
                !foundBusinessRecord.getBusinessCountry().isEmpty())
        {
            foundBusinessRecord.setBusinessCountry(foundBusinessRecord.getBusinessCountry());
        }

        if(Objects.nonNull(foundBusinessRecord.getBusinessEmail()) &&
                !foundBusinessRecord.getBusinessEmail().isEmpty())
        {
            foundBusinessRecord.setBusinessEmail(foundBusinessRecord.getBusinessEmail());
        }

        if(Objects.nonNull(foundBusinessRecord.getBusinessName()) &&
                !foundBusinessRecord.getBusinessName().isEmpty())
        {
            foundBusinessRecord.setBusinessName(foundBusinessRecord.getBusinessName());
        }
        if(Objects.nonNull(foundBusinessRecord.getBusinessPhoneNumber()) &&
                !foundBusinessRecord.getBusinessPhoneNumber().isEmpty())
        {
            foundBusinessRecord.setBusinessPhoneNumber(foundBusinessRecord.getBusinessPhoneNumber());
        }

        businessRepository.save(foundBusinessRecord);
        return true;


    }

    public boolean deleteBusiness(Long id) {
        try {
            Optional<Business> business = businessRepository.findById(id);
            if (business.isPresent()) {
                businessRepository.deleteById(id);
                return true;
            } else {
                throw new RecordNotFoundException("Record not found");
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
            return false;
        }
    }

    //get one business
    public Business getBusiness(Long id) {
        return businessRepository.findById(id).get();
    }
    public void saveProfileImage(Long profileImgId, Long appUserId) {
        BusinessMedia savedImg = businessMediaRepository.findById(profileImgId).get();
        Business business = getBusiness(appUserId);
        business.setBusinessProfileImage(savedImg);
        businessRepository.save(business);
    }
}