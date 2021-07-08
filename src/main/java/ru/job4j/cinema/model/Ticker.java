package ru.job4j.cinema.model;

public class Ticker {
    private int id;
    private int sessionId;
    private int row;
    private int columnn;
    private int accountId;

    public Ticker(int id, int sessionId, int row, int columnn, int accountId) {
        this.id = id;
        this.sessionId = sessionId;
        this.row = row;
        this.columnn = columnn;
        this.accountId = accountId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumnn() {
        return columnn;
    }

    public void setColumnn(int cell) {
        this.columnn = cell;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Ticker ticker = (Ticker) o;

        if (id != ticker.id) {
            return false;
        }
        if (sessionId != ticker.sessionId) {
            return false;
        }
        if (row != ticker.row) {
            return false;
        }
        if (columnn != ticker.columnn) {
            return false;
        }
        return accountId == ticker.accountId;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + sessionId;
        result = 31 * result + row;
        result = 31 * result + columnn;
        result = 31 * result + accountId;
        return result;
    }

    @Override
    public String toString() {
        return "Ticker{" +
                "id=" + id +
                ", sessionId=" + sessionId +
                ", row=" + row +
                ", cell=" + columnn +
                ", accountId=" + accountId +
                '}';
    }
}
