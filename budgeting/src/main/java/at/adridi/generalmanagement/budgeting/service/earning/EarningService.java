/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.service.earning;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.earning.Earning;
import at.adridi.generalmanagement.budgeting.model.earning.EarningCategory;
import at.adridi.generalmanagement.budgeting.model.earning.EarningGraph;
import at.adridi.generalmanagement.budgeting.model.earning.EarningTimerange;
import at.adridi.generalmanagement.budgeting.repository.earning.EarningRepository;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * Implementation of earning DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class EarningService {

    @Autowired
    private EarningRepository earningRepository;
    @Autowired
    private EarningCategoryService earningCategoryService;

    /**
     * Save new Earning.
     *
     * @param newEarning
     * @return saved Earning object. Null if not successful.
     */
    @Transactional
    public Earning save(Earning newEarning) {
        if (newEarning == null) {
            return null;
        }
        return this.earningRepository.save(newEarning);

    }

    /**
     * Get certain earning with the passed id. Throws DataValueNotFoundException
     * if earning is not available.
     *
     * @param id
     * @return
     */
    public Earning getEarningById(Long id) {
        return this.earningRepository.findByEarningId(id).orElseThrow(() -> new DataValueNotFoundException("Earning Does Not Exist"));
    }

    /**
     * Get certain earning with the passed title. Throws
     * DataValueNotFoundException if earning is not available.
     *
     * @param title
     * @param userId
     * @return
     */
    public List<Earning> getEarningByTitle(String title, int userId) {
        return this.earningRepository.findByTitleAndUserId(title, userId)
                .orElseThrow(() -> new DataValueNotFoundException("Earning Does Not Exist"));
    }

    /**
     * Get certain earning with the passed EarningCategory object. Throws
     * DataValueNotFoundException if earning is not available.
     *
     * @param earningCategory
     * @param userId
     * @return
     */
    public ArrayList<Earning> getEarningsByCategoryAndUserId(EarningCategory earningCategory, int userId) {
        return this.earningRepository.findByEarningCategoryAndUserId(earningCategory, userId)
                .orElseThrow(() -> new DataValueNotFoundException("Earning Does Not Exist"));
    }

    /**
     * Get certain earning with the passed EarningTimerange object. Throws
     * DataValueNotFoundException if earning is not available.
     *
     * @param earningTimerange
     * @param userId
     * @return
     */
    public ArrayList<Earning> getEarningsByEarningTimerangeAndUserId(EarningTimerange earningTimerange, int userId) {
        return this.earningRepository.findByEarningTimerangeAndUserId(earningTimerange, userId)
                .orElseThrow(() -> new DataValueNotFoundException("Earning Does Not Exist"));
    }

    /**
     * Get a List of all saved earnings of a user
     *
     * @param userId
     * @return
     */
    public List<Earning> getAllEarning(int userId) {
        return this.earningRepository.getAllEarningList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have earnings or does not exist"));
    }

    /**
     * Get all earnings with one time earnings that belong to a certain year.
     * Throws DataValueNotFoundException if not available.
     *
     * @param month
     * @param year
     * @param userId
     * @return
     */
    public List<Earning> getEarningsByMonthYear(int month, int year, int userId) {
        return this.earningRepository.getEarningsOfCertainMonthYear(month, year, userId).orElseThrow(() -> new DataValueNotFoundException("Earnings could not be loaded for the passed arguments!"));
    }

    /**
     * Get all earnings with one time earnings that belong to a certain year.
     * Throws DataValueNotFoundException if not available.
     *
     * @param year
     * @param userId
     * @return
     */
    public List<Earning> getEarningsByYear(int year, int userId) {
        return this.earningRepository.getEarningsOfCertainYear(year, userId).orElseThrow(() -> new DataValueNotFoundException("Earnings could not be loaded for the passed arguments!"));
    }

        /**
     * Get sum of single and custom earning of a certain month calculated.
     * DataValueNotFoundException if it is not available.
     *
     * @param month Month number 1-12
     * @param userId
     * @return
     */
    public Integer getSumOfSingleAndCustomEarningsByCertainMonth(int month, int userId) {
        Integer earningsOfCertainMonth = this.getMonthlyRecurringEarningsSum(userId);
        earningsOfCertainMonth += this.earningRepository.getSumByCertainTimerangeAndCertainMonth(1, month, userId).orElse(0);
        earningsOfCertainMonth += this.earningRepository.getSumByCertainTimerangeAndCertainMonth(12, month, userId).orElse(0);
        return earningsOfCertainMonth;
    }

    /**
     * Get sum of single and custom earnings of a certain month and year
     * calculated. DataValueNotFoundException if it is not available.
     *
     * @param month 
     * @param year year number example: 1990
     * @param userId
     * @return
     */
    public Integer getSumOfSingleAndCustomEarningsByCertainMonthYear(int month, int year, int userId) {
        Integer earningsOfCertainYear = this.getMonthlyRecurringEarningsSum(userId);
        earningsOfCertainYear += this.earningRepository.getSumByCertainTimerangeAndCertainMonthYear(1, month, year, userId).orElse(0);
        earningsOfCertainYear += this.earningRepository.getSumByCertainTimerangeAndCertainMonthYear(12, month, year, userId).orElse(0);
        return earningsOfCertainYear;
    }
    
    /**
     * Delete an existing earning
     *
     * @param earningId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long earningId) {
        if (earningId == null || earningId == 0) {
            return false;
        }
        Earning earning = null;
        try {
            earning = this.getEarningById(earningId);
        } catch (DataValueNotFoundException e) {
        }

        if (earning != null) {
            earning.setDeleted(true);
            try {
                if (this.earningRepository.save(earning) != null) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Get single and custom earning of a certain month calculated.
     * DataValueNotFoundException if it is not available.
     *
     * @param month Month number 1-12
     * @param userId
     * @return
     */
    public Integer getSingleAndCustomEarningsByCertainMonth(int month, int userId) {
        Integer earningsOfCertainMonth = this.getMonthlyRecurringEarningsSum(userId);
        earningsOfCertainMonth += this.earningRepository.getSumByCertainTimerangeAndCertainMonth(1, month, userId).orElse(0);
        earningsOfCertainMonth += this.earningRepository.getSumByCertainTimerangeAndCertainMonth(12, month, userId).orElse(0);
        return earningsOfCertainMonth;
    }

    /**
     * Get Earnings of certain year (onetime earnings with earning date on the
     * certain year). DataValueNotFoundException if it is not available.
     *
     * @param year example 2000, 2010
     * @param userId
     * @return
     */
    public ArrayList<Earning> getEarningsOfCertainYear(int year, int userId) {
        return this.earningRepository.getEarningsOfCertainYear(year, userId).orElseThrow(() -> new DataValueNotFoundException("Earnings of year" + year + " could not be loaded"));
    }

    /**
     * Set deleted parameter of certain data row. This makes it available again.
     *
     * @param earningId
     */
    @Transactional
    public void restoreDeletedEarning(int earningId) {
        this.earningRepository.restoreDeletedEarning(earningId);
    }

    /**
     * Get certain earning with the passed EarningCategory id. Throws
     * DataValueNotFoundException if earning is not available.
     *
     * @param earningCategoryId
     * @param userId
     * @return
     */
    public ArrayList<Earning> getEarningsByEarningCategoryId(int earningCategoryId, int userId) {
        return this.earningRepository.getEarningsByEarningCategoryId(earningCategoryId, userId).orElseThrow(() -> new DataValueNotFoundException("Earning with the category id " + earningCategoryId + " could not be loaded"));
    }

    /**
     * Update earning categories of all earnings with oldEarningCategoryId and
     * userId to the new earning category id. And delete old earning category
     * object.
     *
     * @param oldEarningCategoryId
     * @param newEarningCategoryId
     * @param userId
     * @return 0 if successful. -1 if unsucessful.
     */
    @Transactional
    public int updateEarningsEarningCategoryId(long oldEarningCategoryId, long newEarningCategoryId, int userId) {
        if ((oldEarningCategoryId > 0 && newEarningCategoryId > 0 && userId > 0)) {
            EarningCategory oldEarningCategory = this.earningCategoryService.getEarningCategoryById(oldEarningCategoryId);
            EarningCategory newEarningCategory = this.earningCategoryService.getEarningCategoryById(newEarningCategoryId);

            if ((oldEarningCategory != null && !oldEarningCategory.getCategoryTitle().equals("")) && (newEarningCategory != null && !newEarningCategory.getCategoryTitle().equals(""))) {
                try {
                    this.earningRepository.updateCategoryOfAllEarning(newEarningCategoryId, oldEarningCategoryId, userId);
                    this.earningCategoryService.deleteById(oldEarningCategoryId);
                    return 0;
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    /**
     * Update only table data (without attachment info) of an Earning item
     *
     * @param title
     * @param earningCategoryId
     * @param centValue
     * @param timerangeId
     * @param earningDate
     * @param information
     * @param earningId
     * @param userId
     * @return
     */
    @Transactional
    public int updateEarningTableData(String title, Long earningCategoryId, int centValue, Long timerangeId, Date earningDate, String information, Long earningId, int userId) {
        try {
            this.earningRepository.updateEarningTableData(title, earningCategoryId, centValue, timerangeId, earningDate.toString(), information, earningId, userId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Get monthly sum of recurring earnings for every earning category of a
     * user. Recurring earnings are divided down to a month.
     *
     * @param userId
     * @return
     */
    public List<EarningGraph> getMonthlySumEarnings(int userId) {
        try {
            List<EarningCategory> allEarningCategories = this.earningCategoryService.getAllEarningCategory();
            List<EarningGraph> monthlyEarnings = new ArrayList();
            for (EarningCategory earningCategory : allEarningCategories) {
                int monthlySum = 0;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(2L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 30;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(3L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 4;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(4L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 2;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(5L, userId, earningCategory.getEarningCategoryId())).orElse(0);
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(6L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 2;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(7L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 4;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(8L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 6;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(9L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 12;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(10L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 24;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(11L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 60;
                monthlyEarnings.add(new EarningGraph(earningCategory.getCategoryTitle(), monthlySum));
            }
            return monthlyEarnings;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get yearly sum of recurring earnings for every earning category of a
     * user. Recurring earnings are divided down to a year.
     *
     * @param userId
     * @return
     */
    public List<EarningGraph> getYearlySumEarnings(int userId) {
        try {
            List<EarningCategory> allEarningCategories = this.earningCategoryService.getAllEarningCategory();
            List<EarningGraph> yearlyEarnings= new ArrayList();
            for (EarningCategory earningCategory : allEarningCategories) {
                int yearlySum = 0;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(2L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 365;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(3L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 52;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(4L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 26;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(5L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 12;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(6L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 6;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(7L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 4;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(8L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 2;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(9L, userId, earningCategory.getEarningCategoryId())).orElse(0);
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(10L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 2;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(11L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 5;
                yearlyEarnings.add(new EarningGraph(earningCategory.getCategoryTitle(), yearlySum));
            }
            return yearlyEarnings;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get sum of all earnings (incl. recurring earnings) happened in this year
     * of a user. Recurring earnings are divided down to a year. Earnings sum
     * for every earnings category.
     *
     * @param userId
     * @return
     */
    public List<EarningGraph> getSumEarningsOfCurrentYear(int userId) {
        try {
            List<EarningCategory> allEarningCategories = this.earningCategoryService.getAllEarningCategory();
            List<EarningGraph> currentYearEarnings = new ArrayList();
            for (EarningCategory earningCategory : allEarningCategories) {
                int currentYearSum = 0;
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                int currentYear = Integer.parseInt(yearFormat.format(Calendar.getInstance().getTime()));
                currentYearSum += this.earningRepository.getSumSingleEarningByYear(currentYear, userId).orElse(0);
                currentYearSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(2L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 365;
                currentYearSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(3L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 52;
                currentYearSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(4L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 26;
                currentYearSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(5L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 12;
                currentYearSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(6L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 6;
                currentYearSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(7L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 4;
                currentYearSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(8L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 2;
                currentYearSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(9L, userId, earningCategory.getEarningCategoryId())).orElse(0);
                currentYearSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(10L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 2;
                currentYearSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(11L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 5;
                currentYearEarnings.add(new EarningGraph(earningCategory.getCategoryTitle(), currentYearSum));
            }
            return currentYearEarnings;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get sum of monthly earnings and other recurring earnings calculated down to a
     * monthly rate. 0 if it is not available.
     *
     * @param userId
     * @return
     */
    public int getMonthlyRecurringEarningsSum(int userId) {
        try {
            List<EarningCategory> allEarningCategories = this.earningCategoryService.getAllEarningCategory();
            int monthlySum = 0;
            for (EarningCategory earningCategory : allEarningCategories) {
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(2L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 30;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(3L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 4;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(4L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 2;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(5L, userId, earningCategory.getEarningCategoryId())).orElse(0);
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(6L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 2;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(7L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 4;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(8L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 6;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(9L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 12;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(10L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 24;
                monthlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(11L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 60;
            }
            return monthlySum;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Get sum of yearly earnings and other recurring earnings calculated down to a
     * yearly rate. 0 if it is not available.
     *
     * @param userId
     * @return
     */
    public int getYearlyRecurringEarningsSum(int userId) {
        try {
            List<EarningCategory> allEarningCategories = this.earningCategoryService.getAllEarningCategory();
            int yearlySum = 0;
            for (EarningCategory earningCategory : allEarningCategories) {
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(2L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 365;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(3L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 52;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(4L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 26;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(5L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 12;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(6L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 6;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(7L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 4;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(8L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 2;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(9L, userId, earningCategory.getEarningCategoryId())).orElse(0);
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(10L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 2;
                yearlySum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(11L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 5;
            }
            return yearlySum;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Get sum of current earnings this month. All recurring earnings counted to
     * a monthly basis. Single earnings of the current month are added as well.
     *
     * @param userId
     * @return 0 if it is not available.
     */
    public int getCurrentMonthEarnings(int userId) {
        try {
            List<EarningCategory> allEarningCategories = this.earningCategoryService.getAllEarningCategory();
            int currentMonthSum = 0;
            for (EarningCategory earningCategory : allEarningCategories) {
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                int currentMonth = Integer.parseInt(monthFormat.format(Calendar.getInstance().getTime()));
                currentMonthSum += this.earningRepository.getSumSingleEarningByMonth(currentMonth, userId).orElse(0);
                currentMonthSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(2L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 30;
                currentMonthSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(3L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 4;
                currentMonthSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(4L, userId, earningCategory.getEarningCategoryId())).orElse(0) * 2;
                currentMonthSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(5L, userId, earningCategory.getEarningCategoryId())).orElse(0);
                currentMonthSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(6L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 2;
                currentMonthSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(7L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 4;
                currentMonthSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(8L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 6;
                currentMonthSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(9L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 12;
                currentMonthSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(10L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 24;
                currentMonthSum += (this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(11L, userId, earningCategory.getEarningCategoryId())).orElse(0) / 60;
            }
            return currentMonthSum;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Get sum of current earnings this month of an earning category id. All
     * recurring earnings counted to a monthly basis. Single earnings of the
     * current month are added as well.
     *
     * @param long earningCategoryId
     * @param userId
     * @return 0 if it is not available.
     */
    public int getCurrentMonthEarningsOfEarningsCategory(long earningCategoryId, int userId) {
        try {
            int currentMonthSum = 0;
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            int currentMonth = Integer.parseInt(monthFormat.format(Calendar.getInstance().getTime()));
            currentMonthSum += this.earningRepository.getSumByCertainTimerangeAndCategoryAndCertainMonth(1L, earningCategoryId, currentMonth, userId).orElse(0);
            currentMonthSum += this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(2L, userId, earningCategoryId).orElse(0) * 30;
            currentMonthSum += this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(3L, userId, earningCategoryId).orElse(0) * 4;
            currentMonthSum += this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(4L, userId, earningCategoryId).orElse(0) * 2;
            currentMonthSum += this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(5L, userId, earningCategoryId).orElse(0);
            currentMonthSum += this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(6L, userId, earningCategoryId).orElse(0) / 2;
            currentMonthSum += this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(7L, userId, earningCategoryId).orElse(0) / 4;
            currentMonthSum += this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(8L, userId, earningCategoryId).orElse(0) / 6;
            currentMonthSum += this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(9L, userId, earningCategoryId).orElse(0) / 12;
            currentMonthSum += this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(10L, userId, earningCategoryId).orElse(0) / 24;
            currentMonthSum += this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(11L, userId, earningCategoryId).orElse(0) / 60;
            currentMonthSum += this.earningRepository.getSumEarningsByTimerangeIdEarningCategoryId(12L, userId, earningCategoryId).orElse(0);
            return currentMonthSum;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
