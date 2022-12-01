package com.build.stats.model.aware

/**
 * Used for the generic mechanism of working with unique tokens.
 * @author shvatov
 */
interface TokenAware {
    var token: String

    companion object {
        /**
         * Defines an empty token.
         */
        const val EMPTY_TOKEN = "\$_EMPTY-TOKEN_\$"
    }
}