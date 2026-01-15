package com.JobPortalNotificationService.Template;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationTemplate {

    public static String title(String eventType) {
        return switch (eventType) {
            case "JOB_SHORTLISTED" -> "Application Shortlisted";
            case "JOB_REJECTED" -> "Application Rejected";
            case "CHAT_SESSION_SCHEDULED" -> "Chat Session Scheduled";
            default -> "Notification";
        };
    }

    public static String message(
            String eventType,
            String jobTitle,
            String chatId,
            LocalDateTime startAt,
            LocalDateTime endAt
    ) {
        return switch (eventType) {

            case "JOB_SHORTLISTED" ->
                "Your application for " + jobTitle + " has been shortlisted.";

            case "JOB_REJECTED" ->
                "Your application for " + jobTitle + " was not selected.";

            case "CHAT_SESSION_SCHEDULED" ->
                buildChatMessage(jobTitle, chatId, startAt, endAt);

            default ->
                "You have a new notification.";
        };
    }

    private static String buildChatMessage(
            String jobTitle,
            String chatId,
            LocalDateTime startAt,
            LocalDateTime endAt
    ) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

        return "A chat session has been scheduled for the job: " + jobTitle
                + "\nChat ID: " + chatId
                + "\nStarts at: " + startAt.format(fmt)
                + "\nEnds at: " + endAt.format(fmt);
    }
}
