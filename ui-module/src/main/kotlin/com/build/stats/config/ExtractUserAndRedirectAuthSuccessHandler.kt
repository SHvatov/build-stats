package com.build.stats.config

import com.build.stats.dao.UserDao
import com.build.stats.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * [AuthenticationSuccessHandler] implementation, which is used to retrieve
 * users' credentials after authorization using external provider.
 */
@Component
class ExtractUserAndRedirectAuthSuccessHandler @Autowired constructor(
    private val authorizedClientService: OAuth2AuthorizedClientService,
    private val userDao: UserDao,
) : SimpleUrlAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        if (authentication is OAuth2AuthenticationToken) {
            val username = authentication.principal.attributes["login"] as String
            val email = authentication.principal.attributes["email"] as String

            val clientDetails = authorizedClientService.loadAuthorizedClient<OAuth2AuthorizedClient>(
                authentication.authorizedClientRegistrationId,
                authentication.name
            )

            val user = userDao.findByEmail(email)?.apply {
                this.username = username
                this.email = email
                this.token = clientDetails.accessToken.tokenValue
            } ?: User(username = username, email = email, token = clientDetails.accessToken.tokenValue)
            userDao.save(user)
        }
        this.defaultTargetUrl = SecurityConfig.HOME_PAGE
        super.onAuthenticationSuccess(request, response, authentication)
    }
}