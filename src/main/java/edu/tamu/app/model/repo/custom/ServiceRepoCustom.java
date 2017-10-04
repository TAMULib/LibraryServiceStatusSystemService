package edu.tamu.app.model.repo.custom;

import edu.tamu.app.model.Service;

public interface ServiceRepoCustom {

    public Service create(Service service);

    public Service update(Service service);

    public void delete(Service service);

}
