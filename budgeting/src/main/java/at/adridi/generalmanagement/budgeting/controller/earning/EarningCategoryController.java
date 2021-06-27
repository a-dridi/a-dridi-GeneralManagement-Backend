/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.controller.earning;

import at.adridi.generalmanagement.budgeting.controller.expense.*;
import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.ResponseMessage;
import at.adridi.generalmanagement.budgeting.model.earning.EarningCategory;
import at.adridi.generalmanagement.budgeting.service.earning.EarningCategoryService;
import at.adridi.generalmanagement.budgeting.util.ApiEndpoints;
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
 * API: EarningCategory - Categories for Earning
 *
 * @author A.Dridi
 */
@RestController
@AllArgsConstructor
public class EarningCategoryController {

    @Autowired
    private EarningCategoryService earningCategoryService;

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGCATEGORY + "/all")
    public ResponseEntity<List<EarningCategory>> getAllEarningCategory() {
        List<EarningCategory> earningCategoryList = new ArrayList<>();
        try {
            earningCategoryList = this.earningCategoryService.getAllEarningCategory();
        } catch (DataValueNotFoundException e) {
        }
        if (!CollectionUtils.isEmpty(earningCategoryList)) {
            return status(HttpStatus.OK).body(earningCategoryList);
        } else {
            return status(HttpStatus.BAD_REQUEST).body(new ArrayList<EarningCategory>());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGCATEGORY + "/get/byId/{id}")
    public ResponseEntity<EarningCategory> getEarningCategoryById(@PathVariable Long id) {
        try {
            return status(HttpStatus.OK).body(this.earningCategoryService.getEarningCategoryById(id));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new EarningCategory());
        }
    }

    @GetMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGCATEGORY + "/get/byTitle/{title}")
    public ResponseEntity<EarningCategory> getAllEarningCategoryByTitle(@PathVariable String title) {
        try {
            return status(HttpStatus.OK).body(this.earningCategoryService.getEarningCategoryByTitle(title));
        } catch (DataValueNotFoundException e) {
            return status(HttpStatus.BAD_REQUEST).body(new EarningCategory());
        }
    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGCATEGORY + "/add")
    public ResponseEntity<EarningCategory> addEarningCategory(@RequestBody String newEarningCategoryString) {
        if (newEarningCategoryString != null || newEarningCategoryString.trim().equals("")) {
            EarningCategory newEarningCategory = new EarningCategory();
            newEarningCategory.setCategoryTitle(newEarningCategoryString);
            EarningCategory createdEarningCategory = this.earningCategoryService.save(newEarningCategory);
            if (createdEarningCategory != null) {
                return ResponseEntity.status(HttpStatus.OK).body(createdEarningCategory);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new EarningCategory());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new EarningCategory());
        }

    }

    @PostMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGCATEGORY + "/update")
    public ResponseEntity<EarningCategory> updateEarningCategory(@RequestBody String updatedEarningCategoryString) {
        ObjectMapper objectMapper = new ObjectMapper();
        EarningCategory updatedEarningCategory;
        try {
            updatedEarningCategory = objectMapper.readValue(updatedEarningCategoryString, EarningCategory.class);
            EarningCategory newUpdatedEarningCategory = this.earningCategoryService.save(updatedEarningCategory);
            if (newUpdatedEarningCategory != null || updatedEarningCategoryString.trim().equals("")) {
                return ResponseEntity.status(HttpStatus.OK).body(newUpdatedEarningCategory);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new EarningCategory());
            }
        } catch (IOException ex) {
            Logger.getLogger(ExpenseTableController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new EarningCategory());
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGCATEGORY + "/delete/byId/{earningCategoryId}")
    public ResponseEntity<ResponseMessage> deleteEarningCategoryById(@PathVariable Long earningCategoryId) {
        if (this.earningCategoryService.deleteById(earningCategoryId)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Your earning category was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Earning Category cannot be null!"));
        }
    }

    @DeleteMapping(ApiEndpoints.API_RESTRICTED_DATABASE_EARNINGCATEGORY + "/delete/byTitle/{title}")
    public ResponseEntity<ResponseMessage> deleteEarningCategoryByTitle(@PathVariable String title) {
        if (this.earningCategoryService.deleteByTitle(title)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("OK. Your earning category was deleted successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR. Earning category does not exists!"));
        }
    }

}
