import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class StorageManager {
    private ActualProduct[] actualProducts;

    public StorageManager() throws IOException, ParseException {
        ArrayList<Integer> idList = new ArrayList<>();
        ArrayList<LocalDate> dateList = new ArrayList<>();

        for (JSONObject item : (Iterable<JSONObject>) ((JSONObject) new JSONParser().parse(new FileReader(Constants.STORAGE_FILE))).get("items")) {
            idList.add(Math.toIntExact((Long) item.get("id")));
            dateList.add(LocalDate.parse((String) item.get("expire"), DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        }

        actualProducts = new ActualProduct[idList.size()];

        for (int i = 0; i < idList.size(); i++) {
            Product prod = Fridge.db.getItemFromID(idList.get(i));

            actualProducts[i] = new ActualProduct(prod.ID, prod.barcode, prod.product_name, dateList.get(i));
        }
    }

    public ActualProduct[] getActualProducts() {
        return actualProducts;
    }

    public ActualProduct[] getExpiringProducts(int maxDaysUntilExpire) {
        // Tracks indexes of products that are valid for expiration display
        int[] productIndexes = new int[actualProducts.length];

        // Tracks index in above array
        int idxTracker = 0;
        for (int i = 0; i < actualProducts.length; i++) {
            // Adds indexes of products that are within expiration range to list
            if (Fridge.getDateDifference(actualProducts[i].expiration) <= maxDaysUntilExpire) {
                productIndexes[idxTracker] = i;
                idxTracker++;
            }
        }

        // Creates array with the length of expiring products
        ActualProduct[] expiringProducts = new ActualProduct[idxTracker];
        // Adds expiring products into new array
        for (int i = 0; i < idxTracker; i++) {
            expiringProducts[i] = actualProducts[productIndexes[i]];
        }

        return expiringProducts;
    }

    // Counts how many products from actualProducts list contain the searchString
    private int getAmountMatched(String searchString) {
        int count = 0;
        for (ActualProduct prod: actualProducts) {
            if (prod.product_name.toLowerCase(Locale.ENGLISH).contains(searchString.toLowerCase(Locale.ENGLISH))) {
                count++;
            }
        }
        return count;
    }

    // Returns a sorted list of products which matched the criteria
    public ActualProduct[] getSearchedActualProducts(String searchString) {
        // Lowercase of search string
        String lowercase_searchString = searchString.toLowerCase(Locale.ENGLISH);

        // Creates blank array with length of the amount of products that match the search
        ActualProduct[] matchedItems = new ActualProduct[getAmountMatched(searchString)];

        // idx represents position in matchedItems
        int idx = 0;
        for (ActualProduct prod: actualProducts) {
            // If the current product matches the search string, add it to matchedItems and increment
            if (prod.product_name.toLowerCase(Locale.ENGLISH).contains(lowercase_searchString)) {
                matchedItems[idx] = prod;
                idx++;
            }
        }

        // Sorts matchedItems putting the products that start with the search term at the start
        boolean sorted = false;
        while (!sorted) {
            sorted = true;

            // Iterates through matches items
            for (int i = 0; i < matchedItems.length - 1; i++) {

                // Gets lowercase name of current and next product in list
                String lowercase_current_prodName = matchedItems[i].product_name.toLowerCase(Locale.ENGLISH);
                String lowercase_next_prodName = matchedItems[i + 1].product_name.toLowerCase(Locale.ENGLISH);

                // If next product starts with the search but the current one does not...
                if (lowercase_next_prodName.startsWith(lowercase_searchString) &&
                        !lowercase_current_prodName.startsWith(lowercase_searchString)) {

                    // Switches the next and current item around
                    ActualProduct tempStore_current = matchedItems[i];
                    matchedItems[i] = matchedItems[i + 1];
                    matchedItems[i + 1] = tempStore_current;

                    // Flags the boolean so another search pass is done
                    sorted = false;
                }
            }
        }

        // Returns the matched items
        return matchedItems;
    }

    public void add(int id, LocalDate date) {
        ActualProduct[] prodArr = new ActualProduct[actualProducts.length + 1];

        System.arraycopy(actualProducts, 0, prodArr, 0, actualProducts.length);

        Product prod = Fridge.db.getItemFromID(id);
        prodArr[actualProducts.length] = new ActualProduct(prod.ID, prod.barcode, prod.product_name, date);

        actualProducts = prodArr;
    }

    public void saveStorage() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        for (int i = 0; i < actualProducts.length; i++) {
            JSONObject item = new JSONObject();
            item.put("id", actualProducts[i].ID);
            item.put("expire", actualProducts[i].expiration.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
            array.add(item);
        }
        object.put("items", array);
        try {
            FileWriter file = new FileWriter(Constants.STORAGE_FILE);
            file.write(object.toJSONString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
