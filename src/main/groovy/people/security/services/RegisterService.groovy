package people.security.services

import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic
import io.micronaut.security.authentication.providers.PasswordEncoder
import people.security.domain.Role
import people.security.domain.User
import people.security.domain.UserRole

import javax.inject.Singleton

@CompileStatic
@Singleton
class RegisterService {

    protected final RoleGormService roleGormService
    protected final UserGormService userGormService
    protected final UserRoleGormService userRoleGormService
    PasswordEncoder passwordEncoder

    RegisterService(RoleGormService roleGormService,
                    UserGormService userGormService,
                    PasswordEncoder passwordEncoder,
                    UserRoleGormService userRoleGormService) {
        this.roleGormService = roleGormService
        this.userGormService = userGormService
        this.userRoleGormService = userRoleGormService
        this.passwordEncoder = passwordEncoder
    }

    @Transactional
    void register(String username, String rawPassword, List<String> authorities) {

        User user = userGormService.findByUsername username
        if (!user) {
            final String encodedPassword = passwordEncoder.encode rawPassword
            user = userGormService.save username, encodedPassword
        }

        if (user && authorities) {

            for (String authority : authorities) {
                Role role = roleGormService.find authority
                if (!role) {
                    role = roleGormService.save authority
                }
                UserRole userRole = userRoleGormService.find user, role
                if (!userRole) {
                    userRoleGormService.save user, role
                }
            }
        }
    }
}
