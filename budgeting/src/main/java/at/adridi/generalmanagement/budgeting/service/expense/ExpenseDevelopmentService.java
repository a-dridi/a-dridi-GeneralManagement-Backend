/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.service.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.expense.Expense;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseCategory;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseDevelopment;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseGraph;
import at.adridi.generalmanagement.budgeting.repository.expense.ExpenseDevelopmentRepository;
import com.sun.xml.bind.v2.TODO;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Implementation of ExpenseDevelopmentService DAO. Create data for
 * ExpenseDevelopment table to the current month and year through the
 * checkUpdate method.
 *
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class ExpenseDevelopmentService {

    @Autowired
    private ExpenseDevelopmentRepository expenseDevelopmentRepository;
    @Autowired
    private ExpenseService expenseService;

    /**
     * Save new expense development.
     *
     * @param newExpenseDevelopment
     * @return saved expensedevelopment object. Null if not successful.
     */
    @Transactional
    public ExpenseDevelopment save(ExpenseDevelopment newExpenseDevelopment) {
        if (newExpenseDevelopment == null) {
            return null;
        }
        return this.expenseDevelopmentRepository.save(newExpenseDevelopment);
    }

    /**
     * Get certain expensedevelopment with the passed id. Throws
     * DataValueNotFoundException if expensedevelopment is not available.
     *
     * @param id
     * @return
     */
    public ExpenseDevelopment getExpenseDevelopmentById(Long id) {
        return this.expenseDevelopmentRepository.findByExpenseDevelopmentId(id).orElseThrow(() -> new DataValueNotFoundException("ExpenseDevelopment Does Not Exist"));
    }

    /**
     * Get information if ExpenseDevelopment table needs to be updated with the
     * current month and year. Returns true if that is the case.
     *
     * @param userId
     * @return
     */
    public boolean isUpdateNeeded(int userId) {
        Date currentDate = new Date();
        SimpleDateFormat monthFormat = new SimpleDateFormat("M");
        SimpleDateFormat yearFormat = new SimpleDateFormat("YYYY");

        List<ExpenseDevelopment> expenseDevelopmentOfCurrentMonthYear = this.expenseDevelopmentRepository.findByMonthAndYearAndUserId(Integer.parseInt(monthFormat.format(currentDate)), Integer.parseInt(yearFormat.format(currentDate)), userId).orElse(new ArrayList<>());
        if (CollectionUtils.isEmpty(expenseDevelopmentOfCurrentMonthYear)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Do fill out all recurring expense information from the last month without
     * ExpenseDevelopment data for every month till to the current month
     * (inclusive).
     *
     * @param userId
     * @return
     */
    @Transactional
    public void doExpenseDevelopmentUpdate(int userId) {
        Date currentDate = new Date();
        SimpleDateFormat monthFormat = new SimpleDateFormat("M");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        int currentMonth = Integer.parseInt(monthFormat.format(currentDate));
        int currentYear = Integer.parseInt(yearFormat.format(currentDate));
        ExpenseDevelopment expenseDevelopmentLastItem = this.expenseDevelopmentRepository.getLastExpenseDevelopmentRow(userId).orElse(new ExpenseDevelopment());

        //Check if expense development has data - not used for the first time
        if (expenseDevelopmentLastItem.getDateDisplay() != null && !expenseDevelopmentLastItem.getDateDisplay().equals("")) {
            int lastSavedMonth = expenseDevelopmentLastItem.getMonth();
            int lastSavedYear = expenseDevelopmentLastItem.getYear();
            int amountOfUnsavedYears = currentYear - lastSavedYear;
            int amountOfUnsavedMonths = 0;

            //iteration variables save the month and year position where the update process is currently at. 
            int iterationMonth = 0;
            int iterationYear = lastSavedYear;
            if (lastSavedMonth == 12) {
                iterationMonth = 1;
                iterationYear = lastSavedYear++;
            } else {
                iterationMonth = lastSavedMonth + 1;
            }

            //Create ExpenseDevelopment items for all passed months since the last saved month and year till to the current month and year (included)
            if (amountOfUnsavedYears > 0) {
                //Calculate: All of the unsaved years and months
                while(!(iterationYear >= currentYear && iterationMonth >= currentMonth)) {
                    int expenseSum = 0;
                    try {
                        expenseSum = this.expenseService.getSumOfSingleAndCustomExpensesByCertainMonthYear(iterationMonth, iterationYear, userId);
                    } catch (DataValueNotFoundException e) {

                    }
                    this.save(new ExpenseDevelopment(iterationMonth, iterationYear, iterationMonth + "/" + iterationYear, expenseSum, userId));
                    if (iterationMonth >= 12) {
                        //Go to next year
                        iterationMonth = 1;
                        iterationYear++;
                    } else {
                        iterationMonth++;
                    }
                } 
            } else {
                //Create ExpenseDevelopment items for all passed months since the last saved month of the current year
                amountOfUnsavedMonths = currentMonth - lastSavedMonth;
                for (; amountOfUnsavedMonths > 0; amountOfUnsavedMonths--) {
                    int expenseSum = 0;
                    try {
                        expenseSum = this.expenseService.getSumOfSingleAndCustomExpensesByCertainMonthYear(iterationMonth, iterationYear, userId);
                    } catch (DataValueNotFoundException e) {

                    }
                    this.save(new ExpenseDevelopment(iterationMonth, iterationYear, iterationMonth + "/" + iterationYear, expenseSum, userId));
                    iterationMonth++;
                    if (iterationMonth >= 12) {
                        amountOfUnsavedMonths=0;
                    }
                }
            }
        } else {
            int expenseSum = 0;
            try {
                expenseSum = this.expenseService.getSumOfSingleAndCustomExpensesByCertainMonthYear(currentMonth, currentYear, userId);
            } catch (DataValueNotFoundException e) {

            }
            this.save(new ExpenseDevelopment(currentMonth, currentYear, currentMonth + "/" + currentYear, expenseSum, userId));
        }
    }

    /**
     * Check if ExpenseDevelopment has saved all expenses till to the current
     * month and year. Update it to the current month and year, if that is not
     * the case.
     *
     * @param userId
     * @return true if update was done
     */
    public boolean checkAndUpdate(int userId) {
        if (this.isUpdateNeeded(userId)) {
            this.doExpenseDevelopmentUpdate(userId);
            return true;
        }
        return false;
    }

    /**
     * Get the ExpenseDevelopment of the last n of months before the current
     * month and year. Example monthsAmount is 48 for the last two years.
     *
     * @param monthsAmount here n the number of months before the current month
     * and year.
     * @param userId
     * @return empty list if monthsAmount is zero or negative.
     */
    public List<ExpenseDevelopment> getMonthlyExpenseDevelopmentOfLastCertainMonths(int monthsAmount, int userId) {
        try {
            if (monthsAmount <= 0) {
                return new ArrayList<>();
            }
            Date currentDate = new Date();
            SimpleDateFormat monthFormat = new SimpleDateFormat("M");
            SimpleDateFormat yearFormat = new SimpleDateFormat("YYYY");
            int currentMonth = Integer.parseInt(monthFormat.format(currentDate));
            int currentYear = Integer.parseInt(yearFormat.format(currentDate));

            int monthsOfPreviousYears = monthsAmount - currentMonth;
            //Amount of years in monthsOfPreviousYears
            int completeYearsAmount = (int) monthsOfPreviousYears / 12;
            int startYear;
            int startMonth;

            //Calculate the year and month before monthsAmount of the current year and month. Subtract amount of months by the amount of full years. And the end calculate the start month in the year left in the calculation. 
            if (completeYearsAmount > 0) {
                startYear = currentYear - (int) Math.ceil((monthsOfPreviousYears / 12.0));
                startMonth = monthsOfPreviousYears - (12 * completeYearsAmount);
                startMonth = 12 - startMonth;
            } else {
                startYear = currentYear;
                startMonth = currentMonth - monthsAmount;
            }
            return this.expenseDevelopmentRepository.getExpenseDevelopmentItemsForMonthAndYearRange(startMonth, currentMonth, startYear, currentYear, userId).orElse(new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the ExpenseDevelopment of the last 24 months and rows before the
     * current month and year.
     *
     * @param userId
     * @return empty list if monthsAmount is zero or negative.
     */
    public List<ExpenseDevelopment> getLast24ExpenseDevelopmentList(int userId) {
        return this.expenseDevelopmentRepository.getLast24ExpenseDevelopmentList(userId).orElseThrow(() -> new DataValueNotFoundException("ExpenseDevelopment Does Not Exist"));
    }

    /**
     * Get the yearly ExpenseDevelopment of the last n years before the current
     * year. Example yearsAmount is 2 for the last two years (2021 the current
     * year and 2020).
     *
     * @param yearsAmount here n the number of months before the current month
     * and year.
     * @param userId
     * @return empty list if yearsAmount is zero or negative.
     */
    public List<ExpenseDevelopment> getYearlyExpenseDevelopmentOfLastCertainYears(int yearsAmount, int userId) {
        try {
            if (yearsAmount <= 0) {
                return new ArrayList<>();
            }
            Date currentDate = new Date();
            SimpleDateFormat yearFormat = new SimpleDateFormat("YYYY");
            int currentYear = Integer.parseInt(yearFormat.format(currentDate));
            int iterationYear = currentYear;
            int yearCounter = yearsAmount;
            List<ExpenseDevelopment> yearlyExpenseDevelopmentList = new ArrayList<>();
            do {
                yearlyExpenseDevelopmentList.add(new ExpenseDevelopment(0, iterationYear, "" + iterationYear, this.expenseDevelopmentRepository.getCentSumOfExpenseDevelopmentOfCertainYear(iterationYear, userId).orElse(0), userId));
                yearCounter--;
                iterationYear--;
            } while (yearCounter >= yearsAmount);

            return yearlyExpenseDevelopmentList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Add an expense to expense of current Expense Development. IMPORTANT: Run
     * only after method checkAndUpdate of ExpenseDevelopmentService.
     *
     * @param expensesCentValue
     * @param expenseTimerangeId
     * @param userId
     */
    @Transactional
    public void addExpenseDevelopmentOfCurrentMonthYear(int expenseCentValue, long expenseTimerangeId, int userId) {
        ExpenseDevelopment currentExpenseDevelopment;
        try {
            currentExpenseDevelopment = this.expenseDevelopmentRepository.getLastExpenseDevelopmentRow(userId).orElseThrow(() -> new DataValueNotFoundException("Cannot Update, because Expense Development table for user is empty!"));
        } catch (DataValueNotFoundException e) {
            System.out.println(e.getErrorMessage());
            return;
        }

        int addedExpenseCentValue = 0;
        switch ((int) expenseTimerangeId) {
            case 1:
                addedExpenseCentValue = expenseCentValue;
                break;
            case 2:
                addedExpenseCentValue = expenseCentValue * 30;
                break;
            case 3:
                addedExpenseCentValue = expenseCentValue * 4;
                break;
            case 4:
                addedExpenseCentValue = expenseCentValue * 2;
                break;
            case 5:
                addedExpenseCentValue = expenseCentValue;
                break;
            case 6:
                addedExpenseCentValue = expenseCentValue / 2;
                break;
            case 7:
                addedExpenseCentValue = expenseCentValue / 4;
                break;
            case 8:
                addedExpenseCentValue = expenseCentValue / 6;
                break;
            case 9:
                addedExpenseCentValue = expenseCentValue / 12;
                break;
            case 10:
                addedExpenseCentValue = expenseCentValue / 24;
                break;
            case 11:
                addedExpenseCentValue = expenseCentValue / 60;
                break;
            case 12:
                addedExpenseCentValue = expenseCentValue;
                break;
        }

        this.expenseDevelopmentRepository.updateExpenseDevelopmentTableData(currentExpenseDevelopment.getMonth(), currentExpenseDevelopment.getYear(), currentExpenseDevelopment.getMonth() + "/" + currentExpenseDevelopment.getYear(), currentExpenseDevelopment.getCentSum() + addedExpenseCentValue, currentExpenseDevelopment.getExpenseDevelopmentId(), userId);
    }

    /**
     * Update an expense of current Expense Development. IMPORTANT: Run only
     * after method checkAndUpdate of ExpenseDevelopmentService.
     *
     * @param oldExpenseCentValue Expense Value of the old Expense
     * @param newExpenseCentValue Expense Value of the updated Expense
     * @param expenseTimerangeId
     * @param userId
     */
    @Transactional
    public void updateExpenseDevelopmentOfCurrentMonthYear(int oldExpenseCentValue, int newExpenseCentValue, long expenseTimerangeId, int userId) {
        ExpenseDevelopment currentExpenseDevelopment;
        try {
            currentExpenseDevelopment = this.expenseDevelopmentRepository.getLastExpenseDevelopmentRow(userId).orElseThrow(() -> new DataValueNotFoundException("Cannot Update, because Expense Development table for user is empty!"));
        } catch (DataValueNotFoundException e) {
            System.out.println(e.getErrorMessage());
            return;
        }

        int addedExpenseCentValue = 0;
        switch ((int) expenseTimerangeId) {
            case 1:
                addedExpenseCentValue = newExpenseCentValue;
                break;
            case 2:
                addedExpenseCentValue = newExpenseCentValue * 30;
                break;
            case 3:
                addedExpenseCentValue = newExpenseCentValue * 4;
                break;
            case 4:
                addedExpenseCentValue = newExpenseCentValue * 2;
                break;
            case 5:
                addedExpenseCentValue = newExpenseCentValue;
                break;
            case 6:
                addedExpenseCentValue = newExpenseCentValue / 2;
                break;
            case 7:
                addedExpenseCentValue = newExpenseCentValue / 4;
                break;
            case 8:
                addedExpenseCentValue = newExpenseCentValue / 6;
                break;
            case 9:
                addedExpenseCentValue = newExpenseCentValue / 12;
                break;
            case 10:
                addedExpenseCentValue = newExpenseCentValue / 24;
                break;
            case 11:
                addedExpenseCentValue = newExpenseCentValue / 60;
                break;
            case 12:
                addedExpenseCentValue = newExpenseCentValue;
                break;
        }

        this.expenseDevelopmentRepository.updateExpenseDevelopmentTableData(currentExpenseDevelopment.getMonth(), currentExpenseDevelopment.getYear(), currentExpenseDevelopment.getMonth() + "/" + currentExpenseDevelopment.getYear(), (currentExpenseDevelopment.getCentSum() - oldExpenseCentValue) + addedExpenseCentValue, currentExpenseDevelopment.getExpenseDevelopmentId(), userId);
    }

    /**
     * Remove an expense of current Expense Development. IMPORTANT: Run only
     * after method checkAndUpdate of ExpenseDevelopmentService.
     *
     * @param removedExpenseCentValue Expense Value of the removed expense
     * @param expenseTimerangeId
     * @param userId
     */
    @Transactional
    public void deleteExpenseDevelopmentOfCurrentMonthYear(int removedExpenseCentValue, long expenseTimerangeId, int userId) {
        ExpenseDevelopment currentExpenseDevelopment;
        try {
            currentExpenseDevelopment = this.expenseDevelopmentRepository.getLastExpenseDevelopmentRow(userId).orElseThrow(() -> new DataValueNotFoundException("Cannot Update, because Expense Development table for user is empty!"));
        } catch (DataValueNotFoundException e) {
            System.out.println(e.getErrorMessage());
            return;
        }

        int adjustedRemovedCentValue = 0;
        switch ((int) expenseTimerangeId) {
            case 1:
                adjustedRemovedCentValue = removedExpenseCentValue;
                break;
            case 2:
                adjustedRemovedCentValue = removedExpenseCentValue * 30;
                break;
            case 3:
                adjustedRemovedCentValue = removedExpenseCentValue * 4;
                break;
            case 4:
                adjustedRemovedCentValue = removedExpenseCentValue * 2;
                break;
            case 5:
                adjustedRemovedCentValue = removedExpenseCentValue;
                break;
            case 6:
                adjustedRemovedCentValue = removedExpenseCentValue / 2;
                break;
            case 7:
                adjustedRemovedCentValue = removedExpenseCentValue / 4;
                break;
            case 8:
                adjustedRemovedCentValue = removedExpenseCentValue / 6;
                break;
            case 9:
                adjustedRemovedCentValue = removedExpenseCentValue / 12;
                break;
            case 10:
                adjustedRemovedCentValue = removedExpenseCentValue / 24;
                break;
            case 11:
                adjustedRemovedCentValue = removedExpenseCentValue / 60;
                break;
            case 12:
                adjustedRemovedCentValue = removedExpenseCentValue;
                break;
        }

        this.expenseDevelopmentRepository.updateExpenseDevelopmentTableData(currentExpenseDevelopment.getMonth(), currentExpenseDevelopment.getYear(), currentExpenseDevelopment.getMonth() + "/" + currentExpenseDevelopment.getYear(), (currentExpenseDevelopment.getCentSum() - adjustedRemovedCentValue), currentExpenseDevelopment.getExpenseDevelopmentId(), userId);
    }
}
