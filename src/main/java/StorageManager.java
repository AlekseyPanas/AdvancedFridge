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

public class StorageManager {
    private int[] ids;
    private LocalDate[] dates;

    public StorageManager() throws IOException, ParseException {
        ArrayList<Integer> idList = new ArrayList<>();
        ArrayList<LocalDate> dateList = new ArrayList<>();

        for (JSONObject item : (Iterable<JSONObject>) ((JSONObject) new JSONParser().parse(new FileReader(Constants.STORAGE_FILE))).get("items")) {
            idList.add(Math.toIntExact((Long) item.get("id")));
            dateList.add(LocalDate.parse((String) item.get("expire"), DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        }

        ids = new int[idList.size()];
        dates = new LocalDate[dateList.size()];

        for (int i = 0; i < idList.size(); i++) {
            ids[i] = idList.get(i);
            dates[i] = dateList.get(i);
        }
    }

    public int[] getIds() {
        return ids;
    }

    public LocalDate[] getDates() {
        return dates;
    }

    public void add(int id, LocalDate date) {
        int[] idArray = new int[ids.length + 1];
        LocalDate[] dateArray = new LocalDate[dates.length + 1];

        System.arraycopy(ids, 0, idArray, 0, ids.length);
        System.arraycopy(dates, 0, dateArray, 0, dates.length);

        idArray[ids.length] = id;
        dateArray[dates.length] = date;

        ids = idArray;
        dates = dateArray;
    }

    public void saveStorage() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        for (int i = 0; i < ids.length; i++) {
            JSONObject item = new JSONObject();
            item.put("id", ids[i]);
            item.put("expire", dates[i].format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
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
