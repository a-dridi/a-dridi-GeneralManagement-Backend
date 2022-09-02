/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.budgeting.service.expense;

import at.adridi.generalmanagement.budgeting.exceptions.DataValueNotFoundException;
import at.adridi.generalmanagement.budgeting.model.AppUser;
import at.adridi.generalmanagement.budgeting.model.UserSetting;
import at.adridi.generalmanagement.budgeting.model.expense.ExpenseReminder;
import at.adridi.generalmanagement.budgeting.repository.expense.ExpenseReminderRepository;
import com.netflix.discovery.EurekaClient;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author A.Dridi
 */
@Service
@NoArgsConstructor
public class ExpenseReminderService {

    @Autowired
    private ExpenseReminderRepository expenseReminderRepository;

    @Autowired
    private EurekaClient discoveryClient;

    /**
     * Get certain expense reminder for the passed expense id. Throws
     * DataValueNotFoundException if expense reminder is not available.
     *
     * @param id
     * @return
     */
    public ExpenseReminder getExpenseReminderByExpenseId(Long expenseId) {
        return this.expenseReminderRepository.getExpenseReminderByExpenseId(expenseId).orElseThrow(() -> new DataValueNotFoundException("Expense Reminder Does Not Exist"));
    }

    /**
     * Save new expense reminder.
     *
     * @param newExpenseReminder
     * @return saved expense reminder object. Null if not successful.
     */
    @Transactional
    public ExpenseReminder save(ExpenseReminder newExpenseReminder) {
        if (newExpenseReminder == null) {
            return null;
        }
        newExpenseReminder.setPayedDate(null);
        return this.expenseReminderRepository.save(newExpenseReminder);
    }

    /**
     * Delete an existing expense reminder
     *
     * @param expenseReminderId
     * @return true if successful
     */
    @Transactional
    public boolean deleteById(Long expenseReminderId) {
        if (expenseReminderId == null || expenseReminderId == 0) {
            return false;
        }
        try {
            this.expenseReminderRepository.deleteExpenseReminder(expenseReminderId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete an existing expense reminder by expense id.
     *
     * @param expenseId
     * @return true if successful
     */
    @Transactional
    public boolean deleteByExpenseId(Long expenseId) {
        if (expenseId == null || expenseId == 0) {
            return false;
        }
        try {
            this.expenseReminderRepository.deleteExpenseReminderByExpenseId(expenseId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update expense reminder with the paid date and the date for the next due
     * date.
     *
     * @param expenseReminder
     * @return
     */
    @Transactional
    public boolean payExpenseReminder(ExpenseReminder expenseReminder) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK);
            LocalDate newDueDate = LocalDate.now();

            //Set next due date according to if the expense is monthly, quarterly, semi yearly or yearly, etc.
            switch ((expenseReminder.getExpense().getExpenseTimerange().getTimerangeId()).intValue()) {
                case 2:
                    newDueDate = newDueDate.plusDays(1);
                    break;
                case 3:
                    newDueDate = newDueDate.plusWeeks(1);
                    break;
                case 4:
                    newDueDate = newDueDate.plusWeeks(2);
                    break;
                case 5:
                    newDueDate = newDueDate.plusMonths(1);
                    break;
                case 6:
                    newDueDate = newDueDate.plusMonths(2);
                    break;
                case 7:
                    newDueDate = newDueDate.plusMonths(3);
                    break;
                case 8:
                    newDueDate = newDueDate.plusMonths(6);
                    break;
                case 9:
                    newDueDate = newDueDate.plusYears(1);
                    break;
                case 10:
                    newDueDate = newDueDate.plusYears(2);
                    break;
                case 11:
                    newDueDate = newDueDate.plusYears(5);
                    break;
            }

            if ((expenseReminder.getExpense().getExpenseTimerange().getTimerangeId()).intValue() != 1) {
                this.expenseReminderRepository.updateExpenseReminderTableData(dateFormat.format(newDueDate), dateFormat.format(new Date()), expenseReminder.getExpensereminderId());
            } else {
                //One time expense reminder - for one time expense, whish was paid
                this.expenseReminderRepository.deleteExpenseReminder(expenseReminder.getExpensereminderId());
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Get settings (username, mail server, port) as a TreeMap that is set by
     * the user in the user settings.
     *
     * @return
     */
    private Map<String, String> getEmailServerSettings(Long userId) {
        Map<String, String> emailServerSettings = new TreeMap<>();

        String url = discoveryClient.getNextServerFromEureka("gm-gateway-service", false).getHomePageUrl();
        emailServerSettings.put("mailServer", (restTemplate().getForEntity(url + "/get/userSetting/bySettingsKey/expenseReminderMailServer/" + userId, UserSetting.class).getBody()).getSettingValue());
        emailServerSettings.put("mailUsername", (restTemplate().getForEntity(url + "/get/userSetting/bySettingsKey/expenseReminderMailUsername/" + userId, UserSetting.class).getBody()).getSettingValue());
        emailServerSettings.put("mailPassword", (restTemplate().getForEntity(url + "/get/userSetting/bySettingsKey/expenseReminderMailPassword/" + userId, UserSetting.class).getBody()).getSettingValue());
        emailServerSettings.put("mailPort", (restTemplate().getForEntity(url + "/get/userSetting/bySettingsKey/expenseReminderMailPort/" + userId, UserSetting.class).getBody()).getSettingValue());

        AppUser appUser = restTemplate().getForEntity(url + "/api/getUserObject/" + userId, AppUser.class).getBody();
        emailServerSettings.put("mailFrom", appUser.getEmail());
        emailServerSettings.put("mailTo", appUser.getEmail());

        return emailServerSettings;
    }

    /**
     * Sent an email with the content in the passed object expenseReminder.
     *
     * @param expenseReminder
     * @return true when email was sent
     */
    public boolean sendEmailReminder(ExpenseReminder expenseReminder) {
        //Get email settings
        Map<String, String> emailServerSettings = this.getEmailServerSettings(expenseReminder.getExpense().getExpenseId());
        //Setup email login details
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", emailServerSettings.get("mailServer"));
        properties.put("mail.smtp.port", emailServerSettings.get("mailPort"));
        if (emailServerSettings.get("mailPort") == "465" || emailServerSettings.get("mailPort") == "587") {
            properties.put("mail.smtp.ssl.enable", "true");
        } else {
            properties.put("mail.smtp.ssl.enable", "false");
        }
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailServerSettings.get("mailFrom"), emailServerSettings.get("mailPassword"));
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailServerSettings.get("mailFrom")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailServerSettings.get("mailTo")));
            message.setSubject("Payment Reminder - " + expenseReminder.getExpense().getTitle());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String messageText = "<h2>This is an email to remind about the following payment that is due: <h2>\n\n"
                    + "<h3><bold>" + expenseReminder.getExpense().getTitle() + "</bold></h3>\n" + "<bold>Due date: </bold>" + dateFormat.format(expenseReminder.getDueDate())
                    + "\n<bold>Payment frequency: </bold>" + expenseReminder.getExpense().getExpenseTimerange().getTimerangeTitle();

            message.setText(messageText);
            Transport.send(message);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return false;
        }

    }

    /**
     * Create an email reminder that is run in the background according to its
     * defined values (frequency). An (expense - payment due) reminder is sent
     * to the user's email address on the due date (24 hours before due date if
     * it's a onetime expense). UTC time is used.
     *
     * @param expenseReminder The expense reminder object that is used to create
     * the reminder.
     */
    public void createReminderProcess(ExpenseReminder expenseReminder) {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Europe/Reykjavík"));
        //  ScheduledExecutorService scheduledReminder = Executors.newScheduledThreadPool(1);
        ScheduledThreadPoolExecutor reminderScheduling = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
        reminderScheduling.setRemoveOnCancelPolicy(true);

        Date reminderDate = expenseReminder.getDueDate();
        int hourAmount = Integer.parseInt((new SimpleDateFormat("hh")).format(reminderDate));
        int minutesAmount = Integer.parseInt((new SimpleDateFormat("MM")).format(reminderDate));
        ZonedDateTime mailReminderTime = currentTime.withHour(hourAmount).withMinute(minutesAmount);
        long delayValue = 0; // The reminder is run every time after this amount of seconds have past since the date the expense was created 
        Duration duration = Duration.ZERO;

        //how many times the reminder is run (one time, daily, weekly, monthly, etc.)
        if (expenseReminder.getExpense().getExpenseTimerange().getTimerangeId().intValue() == 1 || expenseReminder.getExpense().getExpenseTimerange().getTimerangeId().intValue() > 9) {
            //One time reminder
            if ((mailReminderTime.toEpochSecond() - currentTime.toEpochSecond()) >= 86400) {
                mailReminderTime.minusHours(24);
            }

            //Run schedule one time - after n milliseconds when the reminder date is reached. 
            long diffMilliseconds = expenseReminder.getDueDate().getTime() - ((currentTime.toEpochSecond() * 1000));
            reminderScheduling.schedule(() -> {
                if (expenseReminder.getExpense() != null && this.reminderIsNotDeleted(expenseReminder.getExpense().getExpenseId())) {
                    this.sendEmailReminder(expenseReminder);
                } else {
                    //Remove reminder task from reminder schedule pool. 
                    reminderScheduling.shutdownNow();
                }
            }, diffMilliseconds, TimeUnit.MILLISECONDS);
        } else if (expenseReminder.getExpense().getExpenseTimerange().getTimerangeId().intValue() >= 2 && expenseReminder.getExpense().getExpenseTimerange().getTimerangeId().intValue() <= 9) {
            //Frequent (periodic) reminder
            if (currentTime.compareTo(mailReminderTime) > 0) {
                switch (expenseReminder.getExpense().getExpenseTimerange().getTimerangeId().intValue()) {
                    case 2:
                        //Run daily at reminder due date
                        mailReminderTime = mailReminderTime.plusDays(1);
                        break;
                    case 3:
                        //Run weekly at reminder due date
                        mailReminderTime = mailReminderTime.plusDays(7);
                        break;
                    case 4:
                        //Run bi-weekly at reminder due date
                        mailReminderTime = mailReminderTime.plusDays(14);
                        break;
                    case 5:
                        //Run monthly at reminder due date
                        mailReminderTime = mailReminderTime.plusDays(30);
                        break;
                    case 6:
                        //Run every 2 months at reminder due date
                        mailReminderTime = mailReminderTime.plusMonths(2);
                        break;
                    case 7:
                        //Run every 3 months at reminder due date
                        mailReminderTime = mailReminderTime.plusMonths(3);
                        break;
                    case 8:
                        //Run every 6 months at reminder due date
                        mailReminderTime = mailReminderTime.plusMonths(6);
                        break;
                    case 9:
                        //Run every 12 months at reminder due date
                        mailReminderTime = mailReminderTime.plusMonths(12);
                        break;
                }

                duration = Duration.between(currentTime, mailReminderTime);
                delayValue = duration.getSeconds();
                reminderScheduling.scheduleAtFixedRate(() -> {
                    if (expenseReminder.getExpense() != null && this.reminderIsNotDeleted(expenseReminder.getExpense().getExpenseId())) {
                        this.sendEmailReminder(expenseReminder);
                    } else {
                        //Remove reminder task from reminder schedule pool. 
                        reminderScheduling.shutdownNow();
                    }
                }, delayValue, delayValue, TimeUnit.SECONDS);
            }
        }
    }

    /**
     * Check if reminder was not delted. If that is the case, then the reminder
     * will not be send and this reminderW is removed from the schedule
     * execution pool.
     *
     * @return
     */
    public boolean reminderIsNotDeleted(long expenseId) {
        try {
            ExpenseReminder storedExpenseReminder = this.getExpenseReminderByExpenseId(expenseId);
            if (storedExpenseReminder != null) {
                return true;
            }
            return false;
        } catch (DataValueNotFoundException e) {
            return false;
        }

    }
}
