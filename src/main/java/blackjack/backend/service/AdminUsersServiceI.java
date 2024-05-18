package blackjack.backend.service;

import blackjack.backend.domain.AdminUser;
import blackjack.backend.domain.AdminUserDTO;
import blackjack.backend.domain.LoginDTO;
import blackjack.backend.response.LoginMessage;

public interface AdminUsersServiceI {
    AdminUser addAdminUser(AdminUserDTO adminUserDTO);
    LoginMessage loginAdminUser(LoginDTO loginDTO);
}
