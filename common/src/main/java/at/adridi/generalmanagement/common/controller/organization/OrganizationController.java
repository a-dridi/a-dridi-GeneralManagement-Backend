/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.controller.organization;

import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.model.ResponseMessage;
import at.adridi.generalmanagement.common.model.organization.Organization;
import at.adridi.generalmanagement.common.model.organization.OrganizationCategory;
import at.adridi.generalmanagement.common.service.organization.OrganizationService;
import at.adridi.generalmanagement.common.util.ApiEndpoints;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.status;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * API: Organization - Table
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATION + "/all/{userId}")
    public ResponseEntity<List<Organization>> getAllOrganization(@PathVariable int userId) {
        List<Organization> organizationList = new ArrayList<>();
        try {
            organizationList = this.organizationService.getAllOrganization(userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(organizationList)) {
            return status(HttpStatus.OK).body(organizationList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Organization>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATION + "/get/byId/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.organizationService.getOrganizationById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new Organization());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATION + "/get/byCategory/{userId}")
    public ResponseEntity<List<Organization>> getAllOrganizationByOrganizationCategory(@RequestBody String organizationCategoryJson, @PathVariable int userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        OrganizationCategory organizationCategory;
        try {
            organizationCategory = objectMapper.readValue(organizationCategoryJson, OrganizationCategory.class);
            List<Organization> organizationList = new ArrayList<>();
            try {
                organizationList = this.organizationService.getOrganizationsByCategoryAndUserId(organizationCategory, userId);
            } catch (DataValueNotFoundException e) {
            }
            if (!CollectionUtils.isEmpty(organizationList)) {
                return status(HttpStatus.OK).body(organizationList);
            } else {
                return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Organization>());
            }
        } catch (IOException ex) {
            Logger.getLogger(OrganizationController.class.getName()).log(Level.SEVERE, null, ex);
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Organization>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATION + "/search/byDescription/{userId}")
    public ResponseEntity<List<Organization>> searchOrganizationByDescription(@RequestBody String descriptionPattern, @PathVariable int userId) {
        List<Organization> organizationList = new ArrayList<>();
        try {
            organizationList = this.organizationService.searchOrganizationItemByDescription(descriptionPattern, userId);
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(organizationList)) {
            return status(HttpStatus.OK).body(organizationList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<Organization>());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATION + "/add")
    public ResponseEntity<Organization> addOrganization(@RequestBody String newOrganizationJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
        Organization newOrganization;
        Organization savedOrganization;
        try {
            newOrganization = objectMapper.readValue(newOrganizationJson, Organization.class);
            savedOrganization = this.organizationService.save(newOrganization);
            return ResponseEntity.status(HttpStatus.OK).body(savedOrganization);
        } catch (IOException ex) {
            Logger.getLogger(OrganizationController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Organization());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATION + "/update")
    public ResponseEntity<Organization> updateOrganization(@RequestBody String updatedOrganizationJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Organization updatedOrganization;
        Organization savedOrganization;
        try {
            updatedOrganization = objectMapper.readValue(updatedOrganizationJson, Organization.class);
            savedOrganization = this.organizationService.save(updatedOrganization);
            return ResponseEntity.status(HttpStatus.OK).body(savedOrganization);
        } catch (IOException ex) {
            Logger.getLogger(OrganizationController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Organization());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATION + "/updateTableData")
    public ResponseEntity<ResponseMessage> updateOrganizationTableData(@RequestBody String updatedOrganizationJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Organization updatedOrganization;
        try {
            updatedOrganization = objectMapper.readValue(updatedOrganizationJson, Organization.class);
            if (this.organizationService.updateOrganizationTableData(updatedOrganization.getDescription(), updatedOrganization.getOrganizationCategory().getOrganizationCategoryId(), updatedOrganization.getLocation(), updatedOrganization.getStatus(), updatedOrganization.getInformation(), updatedOrganization.getOrganizationId(), updatedOrganization.getUserId()) != -1) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Organization updated."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Organization could not be updated!"));
            }
        } catch (Exception ex) {
            Logger.getLogger(OrganizationController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Organization could not be updated!"));

        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATION + "/updateOrganizationCategories/{oldOrganizationCategoryId}/{newOrganizationCategoryId}/{userId}")
    public ResponseEntity<ResponseMessage> updateCategoryOfOrganizationItems(@PathVariable long oldOrganizationCategoryId, @PathVariable long newOrganizationCategoryId, @PathVariable int userId) {
        if (this.organizationService.updateOrganizationItemsCategoryId(oldOrganizationCategoryId, newOrganizationCategoryId, userId) == 0) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Categories for Organization items were updated."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Categories for Organization items COULD NOT be updated!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATION + "/delete/{organizationId}")
    public ResponseEntity<ResponseMessage> deleteOrganizationById(@PathVariable Long organizationId) {
        if (this.organizationService.deleteById(organizationId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your Organization item was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Organization item does not exists!"));
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATION + "/restore/{organizationId}")
    public ResponseEntity<ResponseMessage> restoreDeletedOrganizationItem(@PathVariable Long organizationId) {
        try {
            this.organizationService.restoreDeletedOrganization(organizationId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(organizationId + " Restored "));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

}
