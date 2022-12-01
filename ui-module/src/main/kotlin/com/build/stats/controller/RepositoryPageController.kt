package com.build.stats.controller

import com.build.stats.config.OAuth2GitHubAttrs
import com.build.stats.dao.UserDao
import com.build.stats.utils.retrieveRequired
import com.build.stats.utils.retrieveRequiredAs
import com.build.stats.vo.user.UserVo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.AuthenticatedPrincipal
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/repo")
class RepositoryPageController @Autowired constructor(
    private val userDao: UserDao
) {
    @RequestMapping("/all")
    fun repositoryListPage(model: Model, @AuthenticationPrincipal oAuthUser: DefaultOAuth2User): String {
        val email = oAuthUser.attributes[OAuth2GitHubAttrs.EMAIL]
            .retrieveRequiredAs<Any?, String> {
                "Email must be present in the authentication attributes"
            }
        val systemUser = userDao.findByEmail(email)
            .retrieveRequired {
                "No user is associated with $email in teh system"
            }
        with(systemUser) {
            model.addAttribute(
                USER_DATA_ATTR,
                UserVo(
                    id = id,
                    username = username
                )
            )
        }
        return "authorized/repos"
    }

    @RequestMapping("/single")
    fun singleRepositoryPage(model: Model, @AuthenticationPrincipal oAuthUser: AuthenticatedPrincipal): String {
        return "index"
    }

    private companion object {
        const val USER_DATA_ATTR = "user"
    }
}