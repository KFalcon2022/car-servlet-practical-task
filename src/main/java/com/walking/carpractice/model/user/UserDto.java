package com.walking.carpractice.model.user;

// В данном случае ключевое отличие DTO от доменного объекта - отсутствие пароля.
// Странно было бы передавать его клиенту, даже в зашифрованном виде
public class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String secondName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
}
