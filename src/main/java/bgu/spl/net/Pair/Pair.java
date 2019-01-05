package bgu.spl.net.Pair;

/**
 * we as not sure if it will be testen on java 8+, so we decided to implement it our self, just in case...
 */
public class Pair<K, V>
{
    /** the key */
    private K key;

    /** Get value */
    private  V value;

    /**
     * Getter to the key
     * @return the key
     */
    public K getKey() {
        return key;
    }

    /**
     * Getter to the value
     * @return the value
     */
    public V getValue() {
        return value;
    }


    /**
     * Constructor
     * @param key the key
     * @param value the value
     */
    public Pair(K key, V value)
    {
        this.key = key;
        this.value = value;
    }
}
