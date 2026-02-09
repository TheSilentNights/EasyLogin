package com.thesilentnights.easylogin.pojo;

public class PlayerSession {
    private final PlayerAccount account;

    public PlayerSession(PlayerAccount account) {
        this.account = account;
    }

    public PlayerAccount getAccount() {
        return account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayerSession that = (PlayerSession) o;
        return java.util.Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(account);
    }

    @Override
    public String toString() {
        return "PlayerSession{" +
                "account=" + account +
                '}';
    }
}