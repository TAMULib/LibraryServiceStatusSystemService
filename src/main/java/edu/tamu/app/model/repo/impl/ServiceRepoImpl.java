package edu.tamu.app.model.repo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import edu.tamu.app.enums.Status;
import edu.tamu.app.model.Service;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.app.model.repo.custom.ServiceRepoCustom;

public class ServiceRepoImpl implements ServiceRepoCustom {

    @Autowired
    ServiceRepo serviceRepo;
    
    @Override
    public Service create(String name, Status status) {
        return new Service(name, status);
    }
}
