/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.financial.service.savings;

import at.adridi.financial.exceptions.DataValueNotFoundException;
import at.adridi.financial.model.savings.Savings;
import at.adridi.financial.repository.savings.SavingsRepository;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of savings DAO
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class SavingsService {

    @Autowired
    private SavingsRepository savingsRepository;

    /**
     * Save new savings.
     *
     * @param newSavings
     * @return saved savings object. Null if not successful.
     */
    @Transactional
    public Savings save(Savings newSavings) {
        if (newSavings == null) {
            return null;
        }
        return this.savingsRepository.save(newSavings);
    }

    /**
     * Get certain Savings with the passed id. Throws DataValueNotFoundException
     * if Savings is not available.
     *
     * @param id
     * @return
     */
    public Savings getSavingsById(Long id) {
        return this.savingsRepository.findBySavingsId(id)
                .orElseThrow(() -> new DataValueNotFoundException("Savings Does Not Exist"));
    }

    /**
     * Get certain Savings with the passed savings Frequency amount. Throws
     * DataValueNotFoundException if Savings is not available.
     *
     * @param savingsFrequency
     * @param userId
     * @return
     */
    public List<Savings> getSavingssByFrequencyAndUserId(Integer savingsFrequency, int userId) {
        return this.savingsRepository.getAllSavingsBySavingsFrequency(savingsFrequency, userId).orElseThrow(() -> new DataValueNotFoundException("Savings Does Not Exist"));
    }

    /**
     * Get a List of all saved savings items of a user
     *
     * @param userId
     * @return
     */
    public List<Savings> getAllSavings(int userId) {
        this.updateSavingsAccountsState(userId);
        return this.savingsRepository.getAllSavingsList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have Savings items or does not exist"));
    }

    /**
     * Update the state (current saved amount, information text, etc.) for all
     * savings account of a user.
     */
    public boolean updateSavingsAccountsState(int userId) {
        Date currentDate = new Date();
        SimpleDateFormat formatDD = new SimpleDateFormat("dd");
        SimpleDateFormat formatMM = new SimpleDateFormat("MM");
        SimpleDateFormat formatYYYY = new SimpleDateFormat("yyyy");
        List<Savings> savingsAccountList = this.savingsRepository.getAllSavingsList(userId).orElseThrow(() -> new DataValueNotFoundException("User " + userId + " does Not have Savings items or does not exist"));

        for (Savings savingsAccount : savingsAccountList) {
            Savings savingsAccountUpdated = savingsAccount;
            //Check if savings account is still active (saving target not reached) 
            if (savingsAccount.getLastSavingsUpdateDate() != null) {
                int lastPartValueDay = Integer.parseInt(formatDD.format(savingsAccount.getLastSavingsUpdateDate()));
                int lastPartValueMonth = Integer.parseInt(formatMM.format(savingsAccount.getLastSavingsUpdateDate()));
                int lastPartValueYear = Integer.parseInt(formatYYYY.format(savingsAccount.getLastSavingsUpdateDate()));

                //Check if a day went past. If that's the case, then do update the savings account.
                if (!(Integer.parseInt(formatDD.format(currentDate)) == lastPartValueDay && Integer.parseInt(formatMM.format(currentDate)) == lastPartValueMonth && Integer.parseInt(formatYYYY.format(currentDate)) == lastPartValueYear)) {

                    if (savingsAccount.getSavingsFrequency() == 1) {
                        //savings account with custom savings frequency 
                        try {
                            double daymonths = 0.0;
                            daymonths = ((savingsAccount.getTargetCent() - (savingsAccount.getSavedTillNowCent())) / savingsAccount.getStepAmountCent());

                            int months = (int) daymonths;
                            //Variable dayAmount is a percentage used to calculate the number of days in a month
                            double dayAmount = Double.parseDouble(String.format("%.2f", daymonths));
                            dayAmount = ((dayAmount * 100) % 100) / 100;

                            int days = 30 * (int) dayAmount;
                            //Add months and days (until saving goal), years are added automatically
                            LocalDate savingsTargetDate = LocalDate.now().plusMonths(months);
                            savingsTargetDate = savingsTargetDate.plusDays(days);
                            savingsAccountUpdated.setTargetCalculatedDate(Date.from(savingsTargetDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            return false;
                        }
                    } else if (savingsAccount.getSavingsFrequency() == 1) {
                        //savings account with daily savings frequency 
                        //Add saved part amounts till to today
                        try {
                            int pastDays = (int) ChronoUnit.DAYS.between(LocalDate.of(lastPartValueYear, lastPartValueMonth, lastPartValueDay), LocalDate.now());
                            if (pastDays > 0) {
                                double days = ((savingsAccount.getTargetCent() - (savingsAccount.getSavedTillNowCent())) / savingsAccount.getStepAmountCent());

                                //Check if target (of saving) was reached
                                if (days == 1.0) {
                                    //target reached
                                    savingsAccountUpdated.setLastSavingsUpdateDate(null);
                                    savingsAccountUpdated.setTargetCalculatedDate(currentDate);
                                } else {
                                    //Aaprox. day amount
                                    //Add days (until still saving target), years will be added automatically.
                                    LocalDate savingTargetDate = LocalDate.now().plusDays((int) days);
                                    //Sparziel Info erstellen 

                                    if (pastDays >= days) {
                                        //Savings target reached long ago - Add remaining savings amounts to it
                                        savingsAccountUpdated.setLastSavingsUpdateDate(null);
                                        savingsAccountUpdated.setSavedTillNowCent(savingsAccountUpdated.getSavedTillNowCent() + (savingsAccountUpdated.getTargetCent() - savingsAccountUpdated.getSavedTillNowCent()));
                                        savingsAccountUpdated.setTargetCalculatedDate(currentDate);
                                    } else {
                                        savingsAccountUpdated.setSavedTillNowCent(savingsAccountUpdated.getSavedTillNowCent() + (pastDays * savingsAccountUpdated.getStepAmountCent()));
                                        savingsAccountUpdated.setLastSavingsUpdateDate(currentDate);
                                        savingsAccountUpdated.setTargetCalculatedDate(Date.from(savingTargetDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                                    }
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            return false;
                        }
                    } else if (savingsAccount.getSavingsFrequency() == 3) {
                        try {
                            int pastMonths = (int) ChronoUnit.MONTHS.between(LocalDate.of(lastPartValueYear, lastPartValueMonth, lastPartValueDay), LocalDate.now());
                            if (pastMonths > 0) {

                                double daysMonths = 0.0;
                                //How much you have to save this partial amount to reach the savings goal. Result corresponds to the number of months (integer value) and number of days (value in the decimal places).
                                daysMonths = ((savingsAccount.getTargetCent() - (savingsAccount.getSavedTillNowCent())) / savingsAccount.getStepAmountCent());
                                int months = (int) daysMonths;

                                //Day share is a percentage used to calculate the number of days in a month
                                double daysAmount = Double.parseDouble(String.format("%.2f", daysMonths));
                                daysAmount = ((daysAmount * 100) % 100) / 100;
                                //Approx. amount of days 
                                int days = 30 * (int) daysAmount;
                                //Add months and days (until the saving goal), years are added automatically
                                LocalDate savingTargetDate = LocalDate.now().plusMonths(months);
                                if (days > 0) {
                                    savingTargetDate = savingTargetDate.plusDays(days);
                                }

                                //Sparziel erreich nach regelmäßigem Sparen (auto.)
                                if (pastMonths >= months) {
                                    //Savings target (already) reached
                                    savingsAccountUpdated.setLastSavingsUpdateDate(null);
                                    savingsAccountUpdated.setSavedTillNowCent(savingsAccountUpdated.getSavedTillNowCent() + (savingsAccountUpdated.getTargetCent() - savingsAccountUpdated.getSavedTillNowCent()));
                                    savingsAccountUpdated.setTargetCalculatedDate(Date.from(savingTargetDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                                } else {
                                    if (daysMonths == 1.0) {
                                        //Saving target reached
                                        savingsAccountUpdated.setLastSavingsUpdateDate(null);
                                        savingsAccountUpdated.setTargetCalculatedDate(currentDate);
                                    } else {
                                        savingsAccountUpdated.setSavedTillNowCent(savingsAccountUpdated.getSavedTillNowCent() + (pastMonths * savingsAccountUpdated.getStepAmountCent()));
                                        savingsAccountUpdated.setTargetCalculatedDate(Date.from(savingTargetDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                                    }
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            return false;
                        }
                    } else if (savingsAccount.getSavingsFrequency() == 4) {
                        try {
                            int pastYears = (int) ChronoUnit.YEARS.between(LocalDate.of(lastPartValueYear, lastPartValueMonth, lastPartValueDay), LocalDate.now());
                            if (pastYears > 0) {

                                double yearsmonths = 0.0;
                                //How many years and months you have to save this partial amount to reach the savings goal. Result corresponds to the number of months (integer value) and number of days (value in the decimal places).                                
                                yearsmonths = ((savingsAccount.getTargetCent() - (savingsAccount.getSavedTillNowCent())) / savingsAccount.getStepAmountCent());

                                if (yearsmonths == 1.0) {
                                    //Savings target already reached
                                    savingsAccountUpdated.setTargetCalculatedDate(savingsAccountUpdated.getLastSavingsUpdateDate());
                                    savingsAccountUpdated.setLastSavingsUpdateDate(null);
                                } else {
                                    //Round up years, as you this is a savings account with a yearly frequency. 
                                    int years = (int) Math.ceil(yearsmonths);
                                    //Add years
                                    LocalDate savingsTargetDate = LocalDate.now();
                                    if (years > 0) {
                                        savingsTargetDate = savingsTargetDate.plusYears(years);
                                    }

                                    if (pastYears >= yearsmonths) {
                                        savingsAccountUpdated.setLastSavingsUpdateDate(null);
                                        savingsAccountUpdated.setSavedTillNowCent(savingsAccountUpdated.getSavedTillNowCent() + (savingsAccountUpdated.getTargetCent() - savingsAccountUpdated.getSavedTillNowCent()));
                                        savingsAccountUpdated.setTargetCalculatedDate(Date.from(savingsTargetDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                                    } else {
                                        savingsAccountUpdated.setLastSavingsUpdateDate(currentDate);
                                        savingsAccountUpdated.setSavedTillNowCent(savingsAccountUpdated.getSavedTillNowCent() + (savingsAccountUpdated.getTargetCent() - savingsAccountUpdated.getSavedTillNowCent()));
                                        savingsAccountUpdated.setTargetCalculatedDate(Date.from(savingsTargetDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                                    }
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }

                    this.save(savingsAccountUpdated);
                }
            }
        }
        return true;
    }

    /**
     * *
     * Set up savings account (savings target info) for the first time.
     *
     * @param userId
     * @return
     */
    public boolean setupSavingsAccount(Savings savedSavingsAccount) {
        Date currentDate = new Date();
        SimpleDateFormat formatDD = new SimpleDateFormat("dd");
        SimpleDateFormat formatMM = new SimpleDateFormat("MM");
        SimpleDateFormat formatYYYY = new SimpleDateFormat("yyyy");

        Savings savingsAccountUpdated = savedSavingsAccount;
        //Check if savings account is still active (saving target not reached) 
        int lastPartValueDay = Integer.parseInt(formatDD.format(savedSavingsAccount.getLastSavingsUpdateDate()));
        int lastPartValueMonth = Integer.parseInt(formatMM.format(savedSavingsAccount.getLastSavingsUpdateDate()));
        int lastPartValueYear = Integer.parseInt(formatYYYY.format(savedSavingsAccount.getLastSavingsUpdateDate()));

        if (savedSavingsAccount.getSavingsFrequency() == 1) {
            //savings account with custom savings frequency 
            try {
                double daymonths = 0.0;
                daymonths = ((savedSavingsAccount.getTargetCent() - (savedSavingsAccount.getSavedTillNowCent())) / savedSavingsAccount.getStepAmountCent());

                int months = (int) daymonths;
                //Variable dayAmount is a percentage used to calculate the number of days in a month
                double dayAmount = Double.parseDouble(String.format("%.2f", daymonths));
                dayAmount = ((dayAmount * 100) % 100) / 100;
                int days = 30 * (int) dayAmount;
                
                //Add months and days (until saving goal), years are added automatically
                LocalDate savingsTargetDate = LocalDate.now().plusMonths(months);
                savingsTargetDate = savingsTargetDate.plusDays(days);
                savingsAccountUpdated.setTargetCalculatedDate(Date.from(savingsTargetDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        } else if (savedSavingsAccount.getSavingsFrequency() == 1) {
            //savings account with daily savings frequency 
            //Add saved part amounts till to today
            try {
                double days = ((savedSavingsAccount.getTargetCent() - (savedSavingsAccount.getSavedTillNowCent())) / savedSavingsAccount.getStepAmountCent());
                
                //Add days (until still saving target), years will be added automatically.
                LocalDate savingTargetDate = LocalDate.now().plusDays((int) days);
                savingsAccountUpdated.setTargetCalculatedDate(Date.from(savingTargetDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        } else if (savedSavingsAccount.getSavingsFrequency() == 3) {
            try {
                double daysMonths = 0.0;
                //How much you have to save this partial amount to reach the savings goal. Result corresponds to the number of months (integer value) and number of days (value in the decimal places).
                daysMonths = ((savedSavingsAccount.getTargetCent() - (savedSavingsAccount.getSavedTillNowCent())) / savedSavingsAccount.getStepAmountCent());
                int months = (int) daysMonths;

                //Day share is a percentage used to calculate the number of days in a month
                double daysAmount = Double.parseDouble(String.format("%.2f", daysMonths));
                daysAmount = ((daysAmount * 100) % 100) / 100;
                //Approx. amount of days 
                int days = 30 * (int) daysAmount;

                //Add months and days (until the saving goal), years are added automatically
                LocalDate savingTargetDate = LocalDate.now().plusMonths(months);
                if (days > 0) {
                    savingTargetDate = savingTargetDate.plusDays(days);
                }
                savingsAccountUpdated.setTargetCalculatedDate(Date.from(savingTargetDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        } else if (savedSavingsAccount.getSavingsFrequency() == 4) {
            try {
                double yearsmonths = 0.0;
                //How many years and months you have to save this partial amount to reach the savings goal. Result corresponds to the number of months (integer value) and number of days (value in the decimal places).                                
                yearsmonths = ((savedSavingsAccount.getTargetCent() - (savedSavingsAccount.getSavedTillNowCent())) / savedSavingsAccount.getStepAmountCent());

                //Round up years, as you this is a savings account with a yearly frequency. 
                int years = (int) Math.ceil(yearsmonths);
                //Add years
                LocalDate savingsTargetDate = LocalDate.now();
                if (years > 0) {
                    savingsTargetDate = savingsTargetDate.plusYears(years);
                }

                savingsAccountUpdated.setTargetCalculatedDate(Date.from(savingsTargetDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }

        this.save(savingsAccountUpdated);
        return true;
    }

    /**
     * Update only table data (without attachment info) of an Savings item
     *
     * @param description
     * @param targetCent
     * @param savingsFrequency
     * @param savedTillNowCent
     * @param lastSavingsUpdateDate
     * @param notice
     * @param savingsId
     * @param userId
     * @return
     */
    @Transactional
    public int updateSavingsTableData(String description, Integer targetCent,
            Integer stepAmountCent, Integer savingsFrequency,
            Integer savedTillNowCent, Date lastSavingsUpdateDate,
            String notice, Long savingsId,
            int userId
    ) {
        try {
            this.savingsRepository.updateSavingsTableData(description, targetCent, stepAmountCent, savingsFrequency, savedTillNowCent, lastSavingsUpdateDate, notice, savingsId, userId);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Delete an existing savings
     *
     * @param savingsId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long savingsId
    ) {
        if (savingsId == null || savingsId == 0) {
            return false;
        }
        Savings savings = null;
        try {
            savings = this.getSavingsById(savingsId);
        } catch (DataValueNotFoundException e) {
        }

        if (savings != null) {
            savings.setDeleted(true);
            try {
                if (this.savingsRepository.save(savings) != null) {
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
     * Set deleted parameter of certain data row. This makes it available again.
     *
     * @param savingsId
     */
    @Transactional
    public void restoreDeletedSavings(Long savingsId
    ) {
        this.savingsRepository.restoreDeletedSavings(savingsId);
    }

}
