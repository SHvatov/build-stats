package com.build.stats.config


object StartupOrder {
    /**
     * Application has loaded.
     */
    const val APPLICATION_LOADED = 1

    /**
     * All actions, that are required to be done on startup have finished.
     */
    const val APPLICATION_STARTUP_ACTIONS_FINISHED = 10

    /**
     * Clients are allowed to connect to the endpoints of the application.
     */
    const val APP_ENDPOINTS_READY = 20
}