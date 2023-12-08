package org.example;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CurrencyConvertingTask {
    private static final String API_URL = "https://api.apilayer.com/fixer/convert";
    private static final String API_KEY = "d5MyKOGZ9BNKHHexSjZJMzhAYWYW4Ij9";
    private static final Set<String> favoriteCurrencies = new HashSet<>();
    public static void main(String[] args) {
        CurrencyConvertingTask converter = new CurrencyConvertingTask();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("-- Menu --");
            System.out.println("1. Convert Currency");
            System.out.println("2. View Favorite Currencies");
            System.out.println("3. Add Favorite Currency");
            System.out.println("4. Update Favorite Currency");
            System.out.println("5. Exit");

            System.out.print("Enter your choice : ");
            int ch = scanner.nextInt();

            switch (ch) {
                case 1 -> converter.convertCurrency();
                case 2 -> converter.viewFavoriteCurrencies();
                case 3 -> converter.addFavoriteCurrency();
                case 4 -> converter.updateFavoriteCurrency();
                case 5 -> {
                    System.out.println("Thank You Bye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }
    public static void convertCurrency() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();

        System.out.print("Enter the source currency \n Example:(USD,INR,EUR,AED,NGN) : ");
        String sourceCurrency = scanner.next().toUpperCase();

        System.out.print("Enter the target currency : ");
        String targetCurrency = scanner.next().toUpperCase();

        try {
            URI uri = new URI(API_URL + String.format("?from=%s&to=%s&amount=%f", sourceCurrency, targetCurrency, amount));
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("apikey", API_KEY);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            JSONObject json = new JSONObject(response.toString());

            if (json.has("result")) {
                double result = json.getDouble("result");
                System.out.printf("%.2f %s = %.2f %s%n", amount, sourceCurrency, result, targetCurrency);
            } else {
                System.out.println("Unable to perform currency conversion. Response: " + response.toString());
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Please check your input and try again.");
        }
    }
    public static void viewFavoriteCurrencies() {
        System.out.println("-- Favorite Currencies --");
        if (favoriteCurrencies.isEmpty()) {
            System.out.println("No favorite currencies added.");
        } else {
            favoriteCurrencies.forEach(currency -> System.out.println("* " + currency));
        }
    }
    public static void addFavoriteCurrency() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the currency code to add : ");
        String currency = scanner.next().toUpperCase();

        favoriteCurrencies.add(currency);
        System.out.println(currency + " added to favorites.");
    }
    public static void updateFavoriteCurrency() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("** Favorite Currencies **");
        if (favoriteCurrencies.isEmpty()) {
            System.out.println("No favorite currencies added.");
        } else {
            favoriteCurrencies.forEach(currency -> System.out.println("* " + currency));
        }

        System.out.print("Enter the currency code to remove from favorites: ");
        String currencyToRemove = scanner.next().toUpperCase();

        if (favoriteCurrencies.remove(currencyToRemove)) {
            System.out.println(currencyToRemove + " removed from favorites.");
        } else {
            System.out.println("Currency not found in favorites.");
        }
    }
}
