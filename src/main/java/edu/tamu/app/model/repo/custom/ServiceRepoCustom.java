package edu.tamu.app.model.repo.custom;

import edu.tamu.app.enums.Status;
import edu.tamu.app.model.Service;
import edu.tamu.framework.model.Credentials;

public interface ServiceRepoCustom {

    public Service create(String name, Status status, Boolean isAuto, Boolean isPublic, Boolean onShortList, String serviceUrl, String description);

    public Service update(Service service);

    public void delete(Service service);

    public void sendStatusUpdate(Service service, Credentials credentials);

}
