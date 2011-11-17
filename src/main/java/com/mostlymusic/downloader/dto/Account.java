package com.mostlymusic.downloader.dto;

import org.jetbrains.annotations.Nullable;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 3:48 PM
 */
public class Account {
    private String username;
    private int id;
    private Long lastOrderId;
    private boolean lastLoggedIn;
    private String password;

    public Account() {
    }

    public Account(String username) {
        this.username = username;
    }

    public Account(Account account) {
        this.username = account.username;
        this.id = account.id;
        this.lastOrderId = account.lastOrderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastOrderId(Long lastOrderId) {
        this.lastOrderId = lastOrderId;
    }

    public Long getLastOrderId() {
        return lastOrderId;
    }

    public boolean isLastLoggedIn() {
        return lastLoggedIn;
    }

    public void setLastLoggedIn(boolean lastLoggedIn) {
        this.lastLoggedIn = lastLoggedIn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
    }

    @SuppressWarnings({"RedundantIfStatement"})
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != account.id) return false;
        if (lastLoggedIn != account.lastLoggedIn) return false;
        if (lastOrderId != null ? !lastOrderId.equals(account.lastOrderId) : account.lastOrderId != null) return false;
        if (password != null ? !password.equals(account.password) : account.password != null) return false;
        if (username != null ? !username.equals(account.username) : account.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + id;
        result = 31 * result + (lastOrderId != null ? lastOrderId.hashCode() : 0);
        result = 31 * result + (lastLoggedIn ? 1 : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", id=" + id +
                ", lastOrderId=" + lastOrderId +
                ", lastLoggedIn=" + lastLoggedIn +
                '}';
    }
}
