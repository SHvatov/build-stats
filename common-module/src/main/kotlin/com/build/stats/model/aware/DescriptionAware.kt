package com.build.stats.model.aware


interface DescriptionAware {
    /**
     * Name of the entity, that will be displayed for the users.
     */
    var name: String?

    /**
     * Optional description of the entity.
     */
    var description: String?
}