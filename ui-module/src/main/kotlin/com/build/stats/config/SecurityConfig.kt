package com.build.stats.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.HttpStatusEntryPoint

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    @set:Autowired
    lateinit var authSuccessHandler: ExtractUserAndRedirectAuthSuccessHandler

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests {
            it.mvcMatchers(*ALLOWED_ENDPOINTS).permitAll()
                .anyRequest().authenticated()
        }.logout {
            it.logoutSuccessUrl(LOGOUT_URL).permitAll()
        }.csrf {
            it.disable()
        }.exceptionHandling {
            it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        }.oauth2Login {
            it.successHandler(authSuccessHandler)
        }
    }

    companion object {
        val ALLOWED_ENDPOINTS = arrayOf(
            "/", "/error", "/js/**", "/img/**",
            "/css/**", "/login", "/webjars/**",
            "/static/**", "/template/**"
        )

        const val LOGOUT_URL = "/logout"

        const val HOME_PAGE = "/repo/all"
    }
}