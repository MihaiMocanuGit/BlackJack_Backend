package blackjack.backend.domain;


import java.util.Objects;

public class AdminUserDTO {
    private String uid;
    private String email;
    private String password;


    public AdminUserDTO() {
    }

    public AdminUserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AdminUser{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminUserDTO adminUser = (AdminUserDTO) o;

        if (!Objects.equals(uid, adminUser.uid)) return false;
        if (!Objects.equals(email, adminUser.email)) return false;
        return Objects.equals(password, adminUser.password);
    }

    @Override
    public int hashCode() {
        int result = uid != null ? uid.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
