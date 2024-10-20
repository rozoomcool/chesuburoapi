package com.rozoomcool.chguburoapi.config

import com.rozoomcool.chguburoapi.auth.JwtAuthFilter
import com.rozoomcool.chguburoapi.entity.Role
import com.rozoomcool.chguburoapi.service.CustomUserDetailsService
import org.springframework.context.annotation.Configuration

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.access.expression.SecurityExpressionHandler
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.FilterInvocation
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val userDetailsService: CustomUserDetailsService,
    private val jwtAuthFilter: JwtAuthFilter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { csrf -> csrf.disable() }
            .cors { cors ->
                cors.configurationSource {
                    val corsConfiguration = CorsConfiguration()
                    corsConfiguration.setAllowedOriginPatterns(listOf("*"))
                    corsConfiguration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    corsConfiguration.allowedHeaders = listOf("*")
                    corsConfiguration.allowCredentials = true
                    corsConfiguration
                }
            }

            .authorizeHttpRequests { request ->
                request
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    .requestMatchers("/api/v1/service/**").permitAll()
//                    .requestMatchers("/api/v1/application/a/**").hasAnyRole(Role.ADMIN.name, Role.MODERATOR.name)
//                    .requestMatchers("/media/**").permitAll()
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers("/health/**").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling(Customizer { CustomExceptionHandler() })
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.getAuthenticationManager()

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    fun roleHierarchy(): RoleHierarchyImpl {
        val roleHierarchy = RoleHierarchyImpl()
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MODERATOR > ROLE_EMPLOYEE > ROLE_STUDENT")
        return roleHierarchy
    }

    private fun webExpressionHandler(): SecurityExpressionHandler<FilterInvocation> {
        val defaultWebSecurityExpressionHandler = DefaultWebSecurityExpressionHandler()
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy())
        return defaultWebSecurityExpressionHandler
    }
}