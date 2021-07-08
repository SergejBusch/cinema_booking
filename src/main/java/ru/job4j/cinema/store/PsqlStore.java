package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.cinema.model.Ticker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();

//            System.out.println(System.getProperty("user.dir"));

        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Ticker> findAllReservedTicker() {
        List<Ticker> tickers = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM cinema.public.ticker")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    tickers.add(new Ticker(it.getInt("id"),
                            it.getInt("session_id"),
                            it.getInt("row"),
                            it.getInt("columnn"),
                            it.getInt("account_id")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tickers;
    }

    @Override
    public void save(Ticker ticker) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO cinema.public.ticker(session_id, row, columnn, account_id" +
                             ") VALUES (?, ?, ?, ?)")
        ) {
            ps.setInt(1, ticker.getSessionId());
            ps.setInt(2, ticker.getRow());
            ps.setInt(3, ticker.getColumnn());
            ps.setInt(4, ticker.getAccountId());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
