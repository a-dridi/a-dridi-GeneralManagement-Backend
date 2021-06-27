/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.repository.expense;

import at.adridi.generalmanagement.budgeting.model.expense.ExpenseTimerange;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author A.Dridi
 */
@Repository
public interface ExpenseTimerangeRepository extends JpaRepository<ExpenseTimerange, Long> {

    Optional<ExpenseTimerange> findByTimerangeId(Long timerangeId);
    Optional<ExpenseTimerange> findByTimerangeTitle(String timerangeTitle);
    
    @Query(value = "SELECT * FROM Expense_Timerange ORDER BY timerange_id ASC", nativeQuery = true)
    Optional<ArrayList<ExpenseTimerange>> getAllExpenseTimerangeList();

}
