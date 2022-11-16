/*
A custom map to store an unmodified and modified string at the same key
String[0] will be immutable, and String[1] will be mutable
 */
package HRP;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A custom map to store two versions of a string at the same index.
 * The first is immutable and the second is mutable. This will allow
 * a string to be modified while still having a reference to its original state.
 * 
 * @author ethan
 */
public class StringDict {

    //backbone datastructure
    private ArrayList<String[]> list;

    /**
     * Constructor instantiates map as a LinkedHashMap
     */
    public StringDict() {
        list = new ArrayList<String[]>();
    }

    public int length() {
        return list.size();
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
        list.add(key, vals);

        return value;
    }
    
    public String put(String value) {
        String[] vals = {value, value};
        list.add(vals);

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
        if (list.get(key) == null) {
            put(key, value);
        } else {
            String[] temp = {list.get(key)[0], value};
            list.set(key, temp);
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
        return list.get(key)[0];
    }
    
    /**
     * Provides read-only access to the mutable string at key
     * 
     * @param   key     Key to retrieve the dictionary value with
     * @return          The mutable string at key
     */
    public String get(int key) {
        return list.get(key)[1];
    }
    
    public void remove(int key) {
        list.remove(key);
    }
    
    public void clearUselessData() {
        list.removeIf((String[] e) -> {
            return e[1].length() <= 12;
        });
    }
    
    /**
     * Provides access to the map's key set
     * 
     * @return          The map's key set.
     */
    /*public Set<Integer> keySet() {
        return list.keySet();
    }*/
    
    public ArrayList<String> getValues() {
        ArrayList<String> output = new ArrayList<String>();
        for (String[] s : list) {
            output.add((String) s[1]);
        }
        
        return output;
    }
    
    /*public String[] toArray() {
        String[] sentences = getValues().toArray();
        return sentences;
    }*/
}
