/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.common.controller.organization;

import at.adridi.generalmanagement.common.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.common.model.ResponseMessage;
import at.adridi.generalmanagement.common.model.organization.OrganizationCategory;
import at.adridi.generalmanagement.common.service.organization.OrganizationCategoryService;
import at.adridi.generalmanagement.common.util.ApiEndpoints;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
 * API: OrganizationCategory - Categories for Organization
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class OrganizationCategoryController {

    @Autowired
    private OrganizationCategoryService organizationCategoryService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATIONCATEGORY + "/all")
    public ResponseEntity<List<OrganizationCategory>> getAllOrganizationCategory() {
        List<OrganizationCategory> organizationCategoryList = new ArrayList<>();
        try {
            organizationCategoryList = this.organizationCategoryService.getAllOrganizationCategory();
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(organizationCategoryList)) {
            return status(HttpStatus.OK).body(organizationCategoryList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<OrganizationCategory>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATIONCATEGORY + "/get/byId/{id}")
    public ResponseEntity<OrganizationCategory> getOrganizationCategoryById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.organizationCategoryService.getOrganizationCategoryById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new OrganizationCategory());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATIONCATEGORY + "/get/byTitle/{title}")
    public ResponseEntity<OrganizationCategory> getAllOrganizationCategoryByTitle(@PathVariable String title) {
        try {
            return status(HttpStatus.OK).body(this.organizationCategoryService.getOrganizationCategoryByCategoryTitle(title));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new OrganizationCategory());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATIONCATEGORY + "/add")
    public ResponseEntity<OrganizationCategory> addOrganizationCategory(@RequestBody String newOrganizationCategoryString) {
        if (newOrganizationCategoryString != null || newOrganizationCategoryString.trim().equals("")) {
            OrganizationCategory newOrganizationCategory = new OrganizationCategory();
            newOrganizationCategory.setCategoryTitle(newOrganizationCategoryString);
            OrganizationCategory createdOrganizationCategory = this.organizationCategoryService.save(newOrganizationCategory);
            if (createdOrganizationCategory != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createdOrganizationCategory);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrganizationCategory());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrganizationCategory());
        }

    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATIONCATEGORY + "/update")
    public ResponseEntity<OrganizationCategory> updateOrganizationCategory(@RequestBody String updatedOrganizationCategoryString) {
        ObjectMapper objectMapper = new ObjectMapper();
        OrganizationCategory updatedOrganizationCategory;
        try {
            updatedOrganizationCategory = objectMapper.readValue(updatedOrganizationCategoryString, OrganizationCategory.class);
            OrganizationCategory newUpdatedOrganizationCategory = this.organizationCategoryService.save(updatedOrganizationCategory);
            if (newUpdatedOrganizationCategory != null) {
                return ResponseEntity.status(HttpStatus.OK).body(newUpdatedOrganizationCategory);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrganizationCategory());
            }
        } catch (IOException ex) {
            Logger.getLogger(OrganizationCategoryController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrganizationCategory());
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATIONCATEGORY + "/delete/byId/{expenseCategoryId}")
    public ResponseEntity<ResponseMessage> deleteOrganizationCategoryById(@PathVariable Long organizationCategoryId) {
        if (this.organizationCategoryService.deleteById(organizationCategoryId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your organization category was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Organization Category cannot be null!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_ORGANIZATIONCATEGORY + "/delete/byTitle/{title}")
    public ResponseEntity<ResponseMessage> deleteOrganizationCategoryByTitle(@PathVariable String title) {
        if (this.organizationCategoryService.deleteByTitle(title)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your organization category was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Organization category does not exists!"));
        }
    }

}
