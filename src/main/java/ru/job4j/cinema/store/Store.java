package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Ticker;

import java.util.Collection;

public interface Store {
    Collection<Ticker> findAllReservedTicker();

    void save(Ticker ticker);
}
