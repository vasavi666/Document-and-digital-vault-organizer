package com.smartvault.scheduler;

import com.smartvault.model.Document;
import com.smartvault.model.enums.NotificationType;
import com.smartvault.repository.DocumentRepository;
import com.smartvault.service.EmailService;
import com.smartvault.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExpiryReminderJob {

    private final DocumentRepository documentRepository;
    private final EmailService emailService;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 8 * * *") // Daily at 8 AM
    public void sendExpiryReminders() {
        log.info("Starting ExpiryReminderJob...");
        LocalDate today = LocalDate.now();
        
        checkAndNotifyExpiring(today, 7);
        checkAndNotifyExpiring(today, 14);
        checkAndNotifyExpiring(today, 30);
        
        log.info("Finished ExpiryReminderJob.");
    }

    private void checkAndNotifyExpiring(LocalDate today, int daysAhead) {
        LocalDate targetDate = today.plusDays(daysAhead);
        List<Document> expiringDocs = documentRepository.findAllExpiringDocuments(targetDate, targetDate);

        for (Document document : expiringDocs) {
            String title = "Document Expiring Soon: " + document.getTitle();
            String message = "Your document '" + document.getTitle() + "' is expiring in " + daysAhead + " days (on " + document.getExpiryDate() + ").";
            
            notificationService.createNotification(document.getUser().getId(), NotificationType.EXPIRY_WARNING, title, message);
            
            emailService.sendExpiryReminder(
                    document.getUser().getEmail(),
                    document.getUser().getFullName(),
                    document.getTitle(),
                    document.getExpiryDate()
            );
            log.info("Sent expiry reminder for document ID {} to user {}", document.getId(), document.getUser().getId());
        }
    }
}
