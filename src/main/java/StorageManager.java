import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class StorageManager {
    private int[] storage;

    public StorageManager() throws IOException, ParseException {
        storage = setStorage();
    }

    public void add(int id) {
        int[] array = new int[storage.length + 1];
        System.arraycopy(storage, 0, array, 0, storage.length);
        array[storage.length] = id;
        storage = array;
    }

    public int[] getStorage() {
        return storage;
    }

    private int[] setStorage() throws IOException, ParseException {
        Iterator<Long> iterator = ((JSONArray) ((JSONObject) new JSONParser().parse(new FileReader(Constants.STORAGE_FILE))).get("ids")).iterator();
        ArrayList<Integer> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(Math.toIntExact(iterator.next()));
        }
        int[] array = new int[list.size()];
        for (int i = 0; i < array.length; i++)
            array[i] = list.get(i);
        return array;
    }

    public void saveStorage() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        for (int id : storage)
            array.add(id);
        object.put("ids", array);
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
