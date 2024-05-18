package blackjack.backend.service;
import blackjack.backend.domain.AdminUser;
import blackjack.backend.domain.AdminUserDTO;
import blackjack.backend.domain.LoginDTO;
import blackjack.backend.repository.AdminUserRepository;
import blackjack.backend.response.LoginMessage;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class AdminUsersService implements AdminUsersServiceI {

    @Autowired
    private AdminUserRepository adminUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AdminUser addAdminUser(AdminUserDTO employeeDTO) {
        AdminUser employee = new AdminUser(
                employeeDTO.getEmail(),
                this.passwordEncoder.encode(employeeDTO.getPassword())
        );
        return adminUserRepository.save(employee);
    }
    @Override
    public LoginMessage loginAdminUser(LoginDTO loginDTO) {
        String msg = "";
        AdminUser employee1 = adminUserRepository.findByEmail(loginDTO.getEmail());
        if (employee1 != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = employee1.getPassword();
            boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                Optional<AdminUser> employee = adminUserRepository.findOneByEmailAndPassword(loginDTO.getEmail(), encodedPassword);
                if (employee.isPresent()) {
                    return new LoginMessage("Login Success", true);
                } else {
                    return new LoginMessage("Login Failed", false);
                }
            } else {
                return new LoginMessage("Password does not match", false);
            }
        }else {
            return new LoginMessage("Email does not exits", false);
        }
    }

}
