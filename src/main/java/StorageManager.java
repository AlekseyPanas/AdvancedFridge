import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;

public class StorageManager {
    private ActualProduct[] actualProducts;

    // Gets the hour difference between now and desired date
    public static int getDateDifference(LocalDate date) {
        LocalDate now = LocalDate.now();
        return Math.round(ChronoUnit.DAYS.between(now, date)) * 24;
    }

    public StorageManager() throws IOException, ParseException {
        ArrayList<Integer> idList = new ArrayList<>();
        ArrayList<LocalDate> dateList = new ArrayList<>();
        ArrayList<Integer> quantityList = new ArrayList<>();
        // Only for custom items
        ArrayList<String> productNames = new ArrayList<>();

        for (JSONObject item : (Iterable<JSONObject>) ((JSONObject) new JSONParser().parse(new FileReader(Constants.STORAGE_FILE))).get("items")) {
            idList.add(Math.toIntExact((Long) item.get("id")));
            quantityList.add(Math.toIntExact((Long) item.get("quantity")));
            dateList.add(LocalDate.parse((String) item.get("expire"), DateTimeFormatter.ofPattern("MM-dd-yyyy")));

            if (((Long) item.get("id")) < 0) {
                productNames.add((String) item.get("prodName"));
            } else {
                productNames.add(null);
            }
        }


        actualProducts = new ActualProduct[idList.size()];

        for (int i = 0; i < idList.size(); i++) {
            int ID = idList.get(i);
            if (ID >= 0) {
                Product prod = Fridge.db.getItemFromID(ID);

                actualProducts[i] = new ActualProduct(prod.ID, prod.barcode, prod.product_name,
                        dateList.get(i), prod.isQuantifiable, quantityList.get(i));
            } else /* Manages custom products */ {
                actualProducts[i] = new ActualProduct(ID, "N/A", productNames.get(i), dateList.get(i), true, quantityList.get(i));
            }

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
            if (getDateDifference(actualProducts[i].expiration) <= maxDaysUntilExpire) {
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

    // Products saved by custom name have negative IDs. This gets the next available ID
    public int getNextCustomID () {
        // ID to be tested
        int currentID = -1;

        // stores whether test deemed the ID available
        boolean valid = false;

        // Searches IDs until valid
        while (!valid) {
            // Innocent ID until proven guilty
            valid = true;

            // Checks if ID already exists
            for (ActualProduct actualProduct : actualProducts) {
                if (actualProduct.ID == currentID) {
                    // If exists, set valid to false for another while loop pass and decrement ID
                    valid = false;
                    currentID--;
                    break;
                }
            }
        }
        return currentID;
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

    // Add new item to storage array
    public void add(ActualProduct product) {
        boolean exists = false;
        for (int i = 0; i < actualProducts.length; i++) {
            // Checks if product already exists
            if (actualProducts[i].ID == product.ID && product.expiration.isEqual(actualProducts[i].expiration)) {
                actualProducts[i].quantity += product.quantity;

                exists = true;

                // Exits loop
                break;
            }
        }

        if (!exists) {
            // Adds new product
            ActualProduct[] prodArr = new ActualProduct[actualProducts.length + 1];

            System.arraycopy(actualProducts, 0, prodArr, 0, actualProducts.length);

            prodArr[actualProducts.length] = new ActualProduct(product.ID, product.barcode, product.product_name,
                    product.expiration, product.isQuantifiable, product.quantity);

            actualProducts = prodArr;
        }
    }

    // Remove item from storage array or subtracts its quantity
    public void remove(int id, LocalDate date) {

        // Finds item
        ActualProduct item;
        boolean found = false;

        for (int j = 0; j < actualProducts.length; j++) {
            if (actualProducts[j].ID == id && date.toString().equals(actualProducts[j].expiration.toString()) && !found) {
                item = actualProducts[j];
                found = true;

                // Subtracts quantity
                if (item.isQuantifiable) {
                    item.quantity--;
                }

                // Checks if item removal is needed
                if ( (!item.isQuantifiable) || (item.quantity <= 0) ) {

                    // Removes item
                    if (actualProducts.length > 1) {
                        ActualProduct[] new_prods = new ActualProduct[actualProducts.length - 1];

                        int idx = 0;
                        boolean found_item = false;

                        for (int i = 0; i < actualProducts.length; i++) {
                            if (!(actualProducts[i].ID == id && date.toString().equals(actualProducts[i].expiration.toString())) || found_item) {
                                new_prods[idx] = actualProducts[i];
                                idx++;
                            } else {
                                found_item = true;
                            }
                        }

                        actualProducts = new_prods;
                        //for (ActualProduct prod: actualProducts) {System.out.println(prod);}
                    } else {
                        actualProducts = new ActualProduct[0];
                    }
                }
            }
        }
    }

    public void saveStorage() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        for (int i = 0; i < actualProducts.length; i++) {
            JSONObject item = new JSONObject();
            item.put("id", actualProducts[i].ID);
            item.put("expire", actualProducts[i].expiration.format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
            item.put("quantity", actualProducts[i].quantity);
            if (actualProducts[i].ID < 0) {
                item.put("prodName", actualProducts[i].product_name);
            }
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
