package com.build.stats.service


interface BuildServiceStartupManager {
    /**
     * Updates the statuses of the builds, that are being untracked due to
     * some service malfunctions. Set the statuses of all builds in progress
     * to error.
     */
    fun updateUntrackedBuilds()
}