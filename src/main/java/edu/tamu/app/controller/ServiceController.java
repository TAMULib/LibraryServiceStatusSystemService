package edu.tamu.app.controller;

import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;
import static edu.tamu.framework.enums.BusinessValidationType.CREATE;
import static edu.tamu.framework.enums.BusinessValidationType.DELETE;
import static edu.tamu.framework.enums.BusinessValidationType.EXISTS;
import static edu.tamu.framework.enums.BusinessValidationType.NONEXISTS;
import static edu.tamu.framework.enums.BusinessValidationType.UPDATE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.app.model.Service;
import edu.tamu.app.model.repo.ServiceRepo;
import edu.tamu.framework.aspect.annotation.ApiCredentials;
import edu.tamu.framework.aspect.annotation.ApiMapping;
import edu.tamu.framework.aspect.annotation.ApiValidatedModel;
import edu.tamu.framework.aspect.annotation.ApiValidation;
import edu.tamu.framework.aspect.annotation.ApiVariable;
import edu.tamu.framework.aspect.annotation.Auth;
import edu.tamu.framework.model.ApiResponse;
import edu.tamu.framework.model.Credentials;

@RestController
@ApiMapping("/service")
public class ServiceController {

    @Autowired
    private ServiceRepo serviceRepo;

    @ApiMapping("/all")
    @Auth(role = "ROLE_ANONYMOUS")
    public ApiResponse getAllServices() {
        return new ApiResponse(SUCCESS, serviceRepo.findAllByOrderByStatusDescNameAsc());
    }

    @ApiMapping("/public")
    @Auth(role = "ROLE_ANONYMOUS")
    public ApiResponse getPublicServices() {
        return new ApiResponse(SUCCESS, serviceRepo.findByIsPublicOrderByStatusDescNameAsc(true));
    }

    @ApiMapping("/{id}")
    @Auth(role = "ROLE_ANONYMOUS")
    public ApiResponse getService(@ApiVariable Long id) {
        return new ApiResponse(SUCCESS, serviceRepo.findOne(id));
    }

    @ApiMapping("/create")
    @Auth(role = "ROLE_SERVICE_MANAGER")
    @ApiValidation(business = { @ApiValidation.Business(value = CREATE), @ApiValidation.Business(value = EXISTS) })
    public ApiResponse createService(@ApiValidatedModel Service service, @ApiCredentials Credentials credentials) {
        return new ApiResponse(SUCCESS, serviceRepo.create(service));
    }

    @ApiMapping("/update")
    @Auth(role = "ROLE_SERVICE_MANAGER")
    @ApiValidation(business = { @ApiValidation.Business(value = UPDATE), @ApiValidation.Business(value = NONEXISTS) })
    public ApiResponse updateService(@ApiValidatedModel Service service, @ApiCredentials Credentials credentials) {
        return new ApiResponse(SUCCESS, serviceRepo.update(service));
    }

    @ApiMapping("/remove")
    @Auth(role = "ROLE_SERVICE_MANAGER")
    @ApiValidation(business = { @ApiValidation.Business(value = DELETE), @ApiValidation.Business(value = NONEXISTS) })
    public ApiResponse removeService(@ApiValidatedModel Service service) {
        serviceRepo.delete(service);
        return new ApiResponse(SUCCESS);
    }

}
