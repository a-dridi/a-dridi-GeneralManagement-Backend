/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.service.earning;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.earning.EarningDevelopment;
import at.adridi.generalmanagement.budgeting.model.earning.EarningDevelopment;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseDevelopment;
import at.adridi.generalmanagement.budgeting.repository.earning.EarningDevelopmentRepository;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Implementation of EarningDevelopmentService DAO. Create data for
 * EarningDevelopment table to the current month and year through the
 * checkUpdate method.
 *
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class EarningDevelopmentService {

    @Autowired
    private EarningDevelopmentRepository earningDevelopmentRepository;
    @Autowired
    private EarningService earningService;

    /**
     * Save new earning development.
     *
     * @param newEarningDevelopment
     * @return saved earningdevelopment object. Null if not successful.
     */
    @Transactional
    public EarningDevelopment save(EarningDevelopment newEarningDevelopment) {
        if (newEarningDevelopment == null) {
            return null;
        }
        return this.earningDevelopmentRepository.save(newEarningDevelopment);
    }

    /**
     * Get certain earnungdevelopment with the passed id. Throws
     * DataValueNotFoundException if earnungdevelopment is not available.
     *
     * @param id
     * @return
     */
    public EarningDevelopment getEarningDevelopmentById(Long id) {
        return this.earningDevelopmentRepository.findByEarningDevelopmentId(id).orElseThrow(() -> new DataValueNotFoundException("EarningDevelopment Does Not Exist"));
    }

    /**
     * Get information if EarningDevelopment table needs to be updated with the
     * current month and year. Returns true if that is the case.
     *
     * @param userId
     * @return
     */
    public boolean isUpdateNeeded(int userId) {
        Date currentDate = new Date();
        SimpleDateFormat monthFormat = new SimpleDateFormat("M");
        SimpleDateFormat yearFormat = new SimpleDateFormat("YYYY");

        List<EarningDevelopment> earningDevelopmentOfCurrentMonthYear = this.earningDevelopmentRepository.findByMonthAndYearAndUserId(Integer.parseInt(monthFormat.format(currentDate)), Integer.parseInt(yearFormat.format(currentDate)), userId).orElse(new ArrayList<>());
        if (CollectionUtils.isEmpty(earningDevelopmentOfCurrentMonthYear)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Do fill out all recurring earning information from the last month without
     * EarningDevelopment data for every month till to the current month
     * (inclusive).
     *
     * @param userId
     * @return
     */
    @Transactional
    public void doEarningDevelopmentUpdate(int userId) {
        Date currentDate = new Date();
        SimpleDateFormat monthFormat = new SimpleDateFormat("M");
        SimpleDateFormat yearFormat = new SimpleDateFormat("YYYY");
        int currentMonth = Integer.parseInt(monthFormat.format(currentDate));
        int currentYear = Integer.parseInt(yearFormat.format(currentDate));
        EarningDevelopment earningDevelopmentLastItem = this.earningDevelopmentRepository.getLastEarningDevelopmentRow(userId).orElse(new EarningDevelopment());

        //Check if earning development has data - not used for the first time
        if (earningDevelopmentLastItem.getDateDisplay() != null && !earningDevelopmentLastItem.getDateDisplay().equals("")) {
            int lastSavedMonth = earningDevelopmentLastItem.getMonth();
            int lastSavedYear = earningDevelopmentLastItem.getYear();
            int amountOfUnsavedMonths = 0;
            int amountOfUnsavedYears = currentYear - lastSavedYear;
            //iteration variables save the month and year position where the update process is currently at. 
            int iterationMonth = lastSavedMonth + 1;
            int iterationYear = lastSavedYear;

            //Create EarningDevelopment items for all passed months since the last saved month and year
            if (amountOfUnsavedYears > 0) {
                //Calculate: First unsaved year
                amountOfUnsavedMonths = 12 - lastSavedMonth;
                if (amountOfUnsavedYears > 1) {
                    amountOfUnsavedMonths += (12 * amountOfUnsavedYears);
                }
                //Calculated Last unsaved year
                amountOfUnsavedMonths += currentMonth;
            } else {
                //Create EarningDevelopment items for all passed months since the last saved month of the current year
                amountOfUnsavedMonths = currentMonth - lastSavedMonth;
            }

            for (; amountOfUnsavedMonths > 0; amountOfUnsavedMonths--) {
                int earningSum = 0;
                try {
                    earningSum = this.earningService.getSumOfSingleAndCustomEarningsByCertainMonthYear(iterationMonth, iterationYear, userId);
                } catch (DataValueNotFoundException e) {

                }
                this.save(new EarningDevelopment(iterationMonth, iterationYear, iterationMonth + "/" + iterationYear, earningSum, userId));
                if (iterationMonth == 12) {
                    //Go to next year
                    iterationMonth = 1;
                    iterationYear++;
                } else {
                    iterationMonth++;
                }
            }
        } else {
            int earningSum = 0;
            try {
                earningSum = this.earningService.getSumOfSingleAndCustomEarningsByCertainMonthYear(currentMonth, currentYear, userId);
            } catch (DataValueNotFoundException e) {

            }
            this.save(new EarningDevelopment(currentMonth, currentYear, currentMonth + "/" + currentYear, earningSum, userId));
        }
    }

    /**
     * Check if EarningDevelopment has saved all earnings till to the current
     * month and year. Update it to the current month and year, if that is not
     * the case.
     *
     * @param userId
     * @return true if update was done
     */
    public boolean checkAndUpdate(int userId) {
        if (this.isUpdateNeeded(userId)) {
            this.doEarningDevelopmentUpdate(userId);
            return true;
        }
        return false;
    }

    /**
     * Get the EarningDevelopment of the last 24 months and rows before the
     * current month and year.
     *
     * @param userId
     * @return empty list if monthsAmount is zero or negative.
     */
    public List<EarningDevelopment> getLast24EarningDevelopmentList(int userId) {
        return this.earningDevelopmentRepository.getLast24EarningDevelopmentList(userId).orElseThrow(() -> new DataValueNotFoundException("EarningDevelopment Does Not Exist"));
    }

    /**
     * Get the EarningDevelopment of the last n of months before the current
     * month and year. Example monthsAmount is 48 for the last two years.
     *
     * @param monthsAmount here n the number of months before the current month
     * and year.
     * @param userId
     * @return empty list if monthsAmount is zero or negative.
     */
    public List<EarningDevelopment> getMonthlyEarningDevelopmentOfLastCertainMonths(int monthsAmount, int userId) {
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
            return this.earningDevelopmentRepository.getEarningDevelopmentItemsForMonthAndYearRange(startMonth, currentMonth, startYear, currentYear, userId).orElse(new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the yearly EarningDevelopment of the last n years before the current
     * year. Example yearsAmount is 2 for the last two years (2021 the current
     * year and 2020).
     *
     * @param yearsAmount here n the number of months before the current month
     * and year.
     * @param userId
     * @return empty list if yearsAmount is zero or negative.
     */
    public List<EarningDevelopment> getYearlyEarningDevelopmentOfLastCertainYears(int yearsAmount, int userId) {
        try {
            if (yearsAmount <= 0) {
                return new ArrayList<>();
            }
            Date currentDate = new Date();
            SimpleDateFormat yearFormat = new SimpleDateFormat("YYYY");
            int currentYear = Integer.parseInt(yearFormat.format(currentDate));
            int iterationYear = currentYear;
            int yearCounter = yearsAmount;
            List<EarningDevelopment> yearlyEarningDevelopmentList = new ArrayList<>();
            do {
                yearlyEarningDevelopmentList.add(new EarningDevelopment(0, iterationYear, "" + iterationYear, this.earningDevelopmentRepository.getCentSumOfEarningDevelopmentOfCertainYear(iterationYear, userId).orElse(0), userId));
                yearCounter--;
                iterationYear--;
            } while (yearCounter >= yearsAmount);

            return yearlyEarningDevelopmentList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Add an earning to earning of current Earning Development. IMPORTANT: Run
     * only after method checkAndUpdate of EarningDevelopmentService.
     *
     * @param earningsCentValue
     * @param earningTimerangeId
     * @param userId
     */
    @Transactional
    public void addEarningDevelopmentOfCurrentMonthYear(int earningCentValue, long earningTimerangeId, int userId) {
        EarningDevelopment currentEarningDevelopment;
        try {
            currentEarningDevelopment = this.earningDevelopmentRepository.getLastEarningDevelopmentRow(userId).orElseThrow(() -> new DataValueNotFoundException("Cannot Update, because Earning Development table for user is empty!"));
        } catch (DataValueNotFoundException e) {
            System.out.println(e.getErrorMessage());
            return;
        }

        int addedEarningCentValue = 0;
        switch ((int) earningTimerangeId) {
            case 1:
                addedEarningCentValue = earningCentValue;
                break;
            case 2:
                addedEarningCentValue = earningCentValue * 30;
                break;
            case 3:
                addedEarningCentValue = earningCentValue * 4;
                break;
            case 4:
                addedEarningCentValue = earningCentValue * 2;
                break;
            case 5:
                addedEarningCentValue = earningCentValue;
                break;
            case 6:
                addedEarningCentValue = earningCentValue / 2;
                break;
            case 7:
                addedEarningCentValue = earningCentValue / 4;
                break;
            case 8:
                addedEarningCentValue = earningCentValue / 6;
                break;
            case 9:
                addedEarningCentValue = earningCentValue / 12;
                break;
            case 10:
                addedEarningCentValue = earningCentValue / 24;
                break;
            case 11:
                addedEarningCentValue = earningCentValue / 60;
                break;
            case 12:
                addedEarningCentValue = earningCentValue;
                break;
        }

        this.earningDevelopmentRepository.updateEarningDevelopmentTableData(currentEarningDevelopment.getMonth(), currentEarningDevelopment.getYear(), currentEarningDevelopment.getMonth() + "/" + currentEarningDevelopment.getYear(), currentEarningDevelopment.getCentSum() + addedEarningCentValue, currentEarningDevelopment.getEarningDevelopmentId(), userId);
    }

    /**
     * Update an earning of current Earning Development. IMPORTANT: Run only
     * after method checkAndUpdate of EarningDevelopmentService.
     *
     * @param oldEarningCentValue Earning Value of the old Earning
     * @param newEarningCentValue Earning Value of the updated Earning
     * @param earningTimerangeId
     * @param userId
     */
    @Transactional
    public void updateEarningDevelopmentOfCurrentMonthYear(int oldEarningCentValue, int newEarningCentValue, long earningTimerangeId, int userId) {
        EarningDevelopment currentEarningDevelopment;
        try {
            currentEarningDevelopment = this.earningDevelopmentRepository.getLastEarningDevelopmentRow(userId).orElseThrow(() -> new DataValueNotFoundException("Cannot Update, because Earning Development table for user is empty!"));
        } catch (DataValueNotFoundException e) {
            System.out.println(e.getErrorMessage());
            return;
        }

        int addedEarningCentValue = 0;
        switch ((int) earningTimerangeId) {
            case 1:
                addedEarningCentValue = newEarningCentValue;
                break;
            case 2:
                addedEarningCentValue = newEarningCentValue * 30;
                break;
            case 3:
                addedEarningCentValue = newEarningCentValue * 4;
                break;
            case 4:
                addedEarningCentValue = newEarningCentValue * 2;
                break;
            case 5:
                addedEarningCentValue = newEarningCentValue;
                break;
            case 6:
                addedEarningCentValue = newEarningCentValue / 2;
                break;
            case 7:
                addedEarningCentValue = newEarningCentValue / 4;
                break;
            case 8:
                addedEarningCentValue = newEarningCentValue / 6;
                break;
            case 9:
                addedEarningCentValue = newEarningCentValue / 12;
                break;
            case 10:
                addedEarningCentValue = newEarningCentValue / 24;
                break;
            case 11:
                addedEarningCentValue = newEarningCentValue / 60;
                break;
            case 12:
                addedEarningCentValue = newEarningCentValue;
                break;
        }

        this.earningDevelopmentRepository.updateEarningDevelopmentTableData(currentEarningDevelopment.getMonth(), currentEarningDevelopment.getYear(), currentEarningDevelopment.getMonth() + "/" + currentEarningDevelopment.getYear(), (currentEarningDevelopment.getCentSum() - oldEarningCentValue) + addedEarningCentValue, currentEarningDevelopment.getEarningDevelopmentId(), userId);
    }

    /**
     * Remove an earning of current Earning Development. IMPORTANT: Run only
     * after method checkAndUpdate of EarningDevelopmentService.
     *
     * @param removedEarningCentValue Earning Value of the removed earning
     * @param earningTimerangeId
     * @param userId
     */
    @Transactional
    public void deleteEarningDevelopmentOfCurrentMonthYear(int removedEarningCentValue, long earningTimerangeId, int userId) {
        EarningDevelopment currentEarningDevelopment;
        try {
            currentEarningDevelopment = this.earningDevelopmentRepository.getLastEarningDevelopmentRow(userId).orElseThrow(() -> new DataValueNotFoundException("Cannot Update, because Earning Development table for user is empty!"));
        } catch (DataValueNotFoundException e) {
            System.out.println(e.getErrorMessage());
            return;
        }

        int adjustedRemovedCentValue = 0;
        switch ((int) earningTimerangeId) {
            case 1:
                adjustedRemovedCentValue = removedEarningCentValue;
                break;
            case 2:
                adjustedRemovedCentValue = removedEarningCentValue * 30;
                break;
            case 3:
                adjustedRemovedCentValue = removedEarningCentValue * 4;
                break;
            case 4:
                adjustedRemovedCentValue = removedEarningCentValue * 2;
                break;
            case 5:
                adjustedRemovedCentValue = removedEarningCentValue;
                break;
            case 6:
                adjustedRemovedCentValue = removedEarningCentValue / 2;
                break;
            case 7:
                adjustedRemovedCentValue = removedEarningCentValue / 4;
                break;
            case 8:
                adjustedRemovedCentValue = removedEarningCentValue / 6;
                break;
            case 9:
                adjustedRemovedCentValue = removedEarningCentValue / 12;
                break;
            case 10:
                adjustedRemovedCentValue = removedEarningCentValue / 24;
                break;
            case 11:
                adjustedRemovedCentValue = removedEarningCentValue / 60;
                break;
            case 12:
                adjustedRemovedCentValue = removedEarningCentValue;
                break;
        }

        this.earningDevelopmentRepository.updateEarningDevelopmentTableData(currentEarningDevelopment.getMonth(), currentEarningDevelopment.getYear(), currentEarningDevelopment.getMonth() + "/" + currentEarningDevelopment.getYear(), (currentEarningDevelopment.getCentSum() - adjustedRemovedCentValue), currentEarningDevelopment.getEarningDevelopmentId(), userId);
    }
}
