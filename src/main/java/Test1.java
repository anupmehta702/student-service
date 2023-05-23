import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test1 {


    public static double calculateHoldingValue(String date) {
        try {
            List<MarketPrice> inputMarketPriceList = restCallForMarketPrice();
            List<SecurityHolding> inputSecurityHoldingList = restCallForSecurityHolding();//new ArrayList<>();

            Portfolio portfolio = new Portfolio();
            portfolio.populateMarketPrice(inputMarketPriceList);
            portfolio.populateSecurityHoldings(inputSecurityHoldingList);
            return portfolio.calculateHoldingValue(date);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /*public static void main(String[] args) throws IOException {

        System.out.println("Holding value -->" + calculateHoldingValue("20190506"));
        *//*MarketPrice marketPrice1 = new MarketPrice("20190506", "ibm", 23.54);
        MarketPrice marketPrice2 = new MarketPrice("20190506", "googl", 74.12);
        List<MarketPrice> inputMarketPriceList = restCallForMarketPrice();//new ArrayList<>();
        *//**//*
        inputMarketPriceList.add(marketPrice1);
        inputMarketPriceList.add(marketPrice2);
        *//**//*
        SecurityHolding sc1 = new SecurityHolding("20190506", "ibm", 100.00, "portfolio_1");
        SecurityHolding sc2 = new SecurityHolding("20190506", "googl", 200.00, "portfolio_1");
        List<SecurityHolding> inputSecurityHoldingList = restCallForSecurityHolding();//new ArrayList<>();
        *//**//*
        inputSecurityHoldingList.add(sc1);
        inputSecurityHoldingList.add(sc2);
        *//**//*
        Portfolio portfolio = new Portfolio();
        portfolio.populateMarketPrice(inputMarketPriceList);
        portfolio.populateSecurityHoldings(inputSecurityHoldingList);

        System.out.println("Holding value --> " + portfolio.calculateHoldingValue("20190506"));
*//*
    }
*/
    public static List<MarketPrice> restCallForMarketPrice() throws IOException {
        List<MarketPrice> inputMarketPriceList = new ArrayList<>();
        URL url = new URL("https://api.myjson.com/bins/vf9ac");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        MarketPriceJson marketPriceJson;
        while ((output = br.readLine()) != null) {
            marketPriceJson = new Gson().fromJson(output, MarketPriceJson.class);
            return marketPriceJson.data;
        }

        conn.disconnect();
        return inputMarketPriceList;
    }


    public static List<SecurityHolding> restCallForSecurityHolding() throws IOException {
        List<SecurityHolding> inputSecurityHoldingList = new ArrayList<>();
        URL url = new URL("https://api.myjson.com/bins/1eleys");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        String output;
        SecurityHoldingJson securityHoldingJson;
        while ((output = br.readLine()) != null) {
            securityHoldingJson = new Gson().fromJson(output, SecurityHoldingJson.class);
            return securityHoldingJson.data;
        }

        conn.disconnect();
        return inputSecurityHoldingList;
    }
}


/*
 "date": "20190506",
            "security": "ibm",
            "price": 23.54
 */

class MarketPriceJson {
    int totalRecords;
    List<MarketPrice> data = new ArrayList<>();

    public MarketPriceJson(int totalRecords, List<MarketPrice> data) {
        this.totalRecords = totalRecords;
        this.data = data;
    }

    @Override
    public String toString() {
        return "MarketPriceJson{" +
                "totalRecords=" + totalRecords +
                ", data=" + data +
                '}';
    }
}

class MarketPrice {
    String date;
    String security;
    Double price;


    public MarketPrice(String date, String security, Double price) {
        this.date = date;
        this.security = security;
        this.price = price;
    }

    public String getSecurity() {
        return security;
    }

    public Double getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }
}

class SecurityWiseMarketPrice {
    Map<String, Double> securityWiseMarketPriceMap = new HashMap<>();

    public SecurityWiseMarketPrice() {
    }

    public void addSecurityWiseMarketPrice(MarketPrice inputMarketPrice) {
        securityWiseMarketPriceMap.put(inputMarketPrice.getSecurity(), inputMarketPrice.getPrice());

    }


    public Double getMarketPriceFor(String security) {
        return securityWiseMarketPriceMap.get(security);
    }
}
/*
"date": "20190506",
            "security": "ibm",
            "quantity": 100,
            "portfolio": "portfolio_1"
 */

class SecurityHoldingJson {
    int totalRecords;
    List<SecurityHolding> data = new ArrayList<>();

    public SecurityHoldingJson(int totalRecords, List<SecurityHolding> data) {
        this.totalRecords = totalRecords;
        this.data = data;
    }

    @Override
    public String toString() {
        return "SecurityHoldingJson{" +
                "totalRecords=" + totalRecords +
                ", data=" + data +
                '}';
    }
}

class SecurityHolding {
    String date;
    String security;
    Double quantity = new Double(0);
    String portfolio;


    public SecurityHolding(String date, String security, Double quantity, String portfolio) {
        this.date = date;
        this.security = security;
        this.quantity = quantity;
        this.portfolio = portfolio;
    }

    public String getDate() {
        return date;
    }

    public String getSecurity() {
        return security;
    }
}

class Portfolio {
    Map<String, List<SecurityHolding>> dayWiseSecurityHoldingMap = new HashMap<>();
    Map<String, SecurityWiseMarketPrice> dayWiseMarketPriceMap = new HashMap<>();

    public void populateSecurityHoldings(List<SecurityHolding> securityHoldings) {
        for (SecurityHolding sh : securityHoldings) {
            if (dayWiseSecurityHoldingMap.containsKey(sh.getDate())) {
                List<SecurityHolding> dateWiseSecurityHoldings = dayWiseSecurityHoldingMap.get(sh.getDate());
                dateWiseSecurityHoldings.add(sh);
            } else {
                List<SecurityHolding> dateWiseSecurityHolding = new ArrayList<>();
                dateWiseSecurityHolding.add(sh);
                dayWiseSecurityHoldingMap.put(sh.getDate(), dateWiseSecurityHolding);
            }
        }
    }

    public void populateMarketPrice(List<MarketPrice> inputMarketPriceList) {
        for (MarketPrice marketPrice : inputMarketPriceList) {
            if (dayWiseMarketPriceMap.containsKey(marketPrice.getDate())) {
                SecurityWiseMarketPrice securityWiseMarketPrice = dayWiseMarketPriceMap.get(marketPrice.getDate());
                securityWiseMarketPrice.addSecurityWiseMarketPrice(marketPrice);
            } else {
                SecurityWiseMarketPrice securityWiseMarketPrice = new SecurityWiseMarketPrice();
                securityWiseMarketPrice.addSecurityWiseMarketPrice(marketPrice);
                dayWiseMarketPriceMap.put(marketPrice.getDate(), securityWiseMarketPrice);
            }
        }

    }

    public double calculateHoldingValue(String date) {
        Double dateHoldingValue = new Double(0);
        if (dayWiseSecurityHoldingMap.containsKey(date)) {
            List<SecurityHolding> inputSecurityHoldingList = dayWiseSecurityHoldingMap.get(date);
            for (SecurityHolding inputSecurityHolding : inputSecurityHoldingList) {
                SecurityWiseMarketPrice dateMarketPrice = dayWiseMarketPriceMap.get(date);
                Double securityMarketPrice = dateMarketPrice.getMarketPriceFor(inputSecurityHolding.security);
                dateHoldingValue = dateHoldingValue + (securityMarketPrice * inputSecurityHolding.quantity);
            }
        } else {
            return 0.0;
        }
        return dateHoldingValue;
    }

}
