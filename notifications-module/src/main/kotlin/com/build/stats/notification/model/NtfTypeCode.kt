package com.build.stats.notification.model


enum class NtfTypeCode(val description: String) {
    MAIL("Notifications will be send to your email."),
    TELEGRAM(
        "Notifications will be send to your Telegram account. " +
                "You should start Telegram bot @GitHubBuildStatsBot and follow all instructions."
    )
}