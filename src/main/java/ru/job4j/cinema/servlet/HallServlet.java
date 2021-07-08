package ru.job4j.cinema.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import ru.job4j.cinema.model.Ticker;
import ru.job4j.cinema.store.PsqlStore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class HallServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        var tickers = PsqlStore.instOf().findAllReservedTicker();
        var json = new Gson().toJson(tickers);
        PrintWriter writer = resp.getWriter();
        writer.print(json);
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        var sb = new StringBuilder();
        String line;
        var reader = req.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String everything = sb.toString();
        System.out.println(everything);
        var tickerList = new ArrayList<Ticker>();
        var jsonArray = JsonParser.parseString(everything).getAsJsonArray();
        createTickers(tickerList, jsonArray);
        tickerList.forEach(t -> PsqlStore.instOf().save(t));
    }

    private void createTickers(ArrayList<Ticker> tickerList, com.google.gson.JsonArray array) {
        array.forEach(e -> {
            var obj = e.getAsJsonObject();
            tickerList.add(new Ticker(0,
                    obj.get("session").getAsInt(),
                    obj.get("row").getAsInt(),
                    obj.get("column").getAsInt(),
                    1));
        });
    }
}
