package edu.tamu.app.model.repo.custom;

import edu.tamu.app.enums.Status;
import edu.tamu.app.model.Service;

public interface ServiceRepoCustom {

    public Service create(String name, Status status, Boolean isAuto, Boolean isPublic, Boolean onShortList, String serviceUrl, String description);

    public Service update(Service service);

    public void delete(Service service);

}
