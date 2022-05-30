package yandex.praktikum;

public class ProfileCourierData {
    private String login;
    private String password;
    private String firstName;

    public ProfileCourierData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return firstName;
    }

    public void setName(String name) {
        this.firstName = name;
    }

    public ProfileCourierData LoginInfo() {
        return new ProfileCourierData(this.login, this.password);
    }

    public ProfileCourierData(String login, String password, String name) {
        this.login = login;
        this.password = password;
        this.firstName = name;
    }

    public ProfileCourierData() {
    }
}
