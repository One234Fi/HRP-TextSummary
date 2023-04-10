package nlp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A custom map to store two versions of a string at the same index.
 * The first is [0] immutable and the second [1] is mutable. This will allow
 * a string to be modified while still having a reference to its original state.
 * 
 * @author ethan
 */
public class StringDict {
    //backbone datastructure
    private final HashMap<Integer, String[]> map;

    /**
     * Constructor
     */
    public StringDict() {
        map = new HashMap<>();
    }

    /**
     * getter
     * 
     * @return the size of the backbone list
     */
    public int length() {
        return map.size();
    }
    
    
    /**
     * Creates an array with two instantiations of value. 
     * The string at 0 will be treated as immutable.
     * The string at 1 will be treated as mutable.
     * 
     * @param   key     The key to insert the value at
     * @param   value   A string to insert
     * @return          The value parameter
     */
    public String put(int key, String value) {
        String[] vals = {value, value};
        map.put(key, vals);

        return value;
    }

    /**
     * If map.key == null, puts value there, otherwise overwrites map.key[1] with value
     * 
     * @param   key     An int to determine a location in the map
     * @param   value   A string to overwrite the key location with
     * @return          The value parameter
     */
    public String modify(int key, String value) {
        if (map.get(key) == null) {
            put(key, value);
        } else {
            String[] old = map.get(key);
            String[] temp = {map.get(key)[0], value};
            map.replace(key, old, temp);
        }

        return value;
    }

    /**
     * Provides read-only access to the immutable string at key
     * 
     * @param   key     Key to retrieve the dictionary value with
     * @return          The immutable string at key
     */
    public String getBase(int key) {
        return map.get(key)[0];
    }
    
    /**
     * Provides read-only access to the mutable string at key
     * 
     * @param   key     Key to retrieve the dictionary value with
     * @return          The mutable string at key
     */
    public String get(int key) {
        return map.get(key)[1];
    }
    
    /**
     * removes the value(s) at key in list
     *
     * @param key: int index
     */
    public void remove(int key) {
        map.remove(key);
    }
    
    /**
     * clears out all of the short trash strings
     */
    public void clearUselessData() {
        for(Map.Entry<Integer, String[]> e : map.entrySet()) {
            if (e.getValue()[1].length() >= 12) {
                map.remove(e.getKey());
            }
        }
    }
    
    /**
     * @return an array list of the mutable values
     */
    public ArrayList<String> getValues() {
        return (ArrayList)map.values();
    }
}
