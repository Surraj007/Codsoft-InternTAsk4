import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String API_KEY = "YOUR_API_KEY"; // Replace with your API key
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";

    public static double getExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            String urlString = API_URL + API_KEY + "/latest/" + baseCurrency;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            String jsonResponse = response.toString();
            int start = jsonResponse.indexOf(targetCurrency) + targetCurrency.length() + 3;
            int end = jsonResponse.indexOf(",", start);
            if (end == -1) end = jsonResponse.indexOf("}", start);
            return Double.parseDouble(jsonResponse.substring(start, end));
        } catch (Exception e) {
            System.out.println("Error fetching exchange rates: " + e.getMessage());
            return -1;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the base currency (e.g., USD): ");
        String baseCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Enter the target currency (e.g., EUR): ");
        String targetCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Enter the amount to convert: ");
        double amount = scanner.nextDouble();

        double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);
        if (exchangeRate != -1) {
            double convertedAmount = amount * exchangeRate;
            System.out.printf("Converted amount: %.2f %s\n", convertedAmount, targetCurrency);
        } else {
            System.out.println("Failed to fetch exchange rates. Please try again.");
        }

        scanner.close();
    }
}
