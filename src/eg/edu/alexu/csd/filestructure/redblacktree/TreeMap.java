package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;
import javax.swing.text.html.parser.Entity;
import java.util.*;

public class TreeMap <T extends Comparable<T>, V>  implements ITreeMap {
    private RedBlackTree<T,V> redBlackTree = new RedBlackTree<>();

    @Override
    public Map.Entry ceilingEntry(Comparable key) {
        check(key);
        INode node;
        if((node = redBlackTree.searchNode(key)) != null){
            return new AbstractMap.SimpleEntry(node.getKey(),node.getValue());
        }else{
            node = redBlackTree.successor(node);
            return new AbstractMap.SimpleEntry(node.getKey(),node.getValue());
        }
    }

    @Override
    public Comparable ceilingKey(Comparable key) {
        check(key);
        INode node;
        if((node = redBlackTree.searchNode(key)) != null){
            return node.getKey();
        }else{
            return redBlackTree.successor(node).getKey();
        }
    }

    @Override
    public void clear() {
        redBlackTree.clear();
    }

    @Override
    public boolean containsKey(Comparable key) {
        check(key);
        return redBlackTree.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        check(value);
        return redBlackTree.searchvalue(value,redBlackTree.getRoot());
    }


    @Override
    public Set<Map.Entry> entrySet() {
        Set<Map.Entry> set = new LinkedHashSet<>();
        redBlackTree.entryset(redBlackTree.getRoot(),set);
        return set;
    }

    @Override
    public Map.Entry firstEntry() {
        if(redBlackTree.getRoot() != null){
            INode node = (redBlackTree.Minimum(redBlackTree.getRoot()));
            return new AbstractMap.SimpleEntry(node.getKey(),node.getValue());
        }
        return null;
    }

    @Override
    public Comparable firstKey() {
        Map.Entry entry = firstEntry();
        if(entry != null)
            return (T)entry.getKey();
        return null;
    }

    @Override
    public Map.Entry floorEntry(Comparable key) {
        check(key);
        INode node;
        if((node = redBlackTree.searchNode(key)) != null){
            return new AbstractMap.SimpleEntry(node.getKey(),node.getValue());
        }else{
            node = redBlackTree.predecessor(node);
            return new AbstractMap.SimpleEntry(node.getKey(),node.getValue());
        }
    }

    @Override
    public Comparable floorKey(Comparable key) {
        check(key);
        INode node;
        if((node = redBlackTree.searchNode(key)) != null){
            return node.getKey();
        }else{
            return redBlackTree.predecessor(node).getKey();
        }
    }

    @Override
    public Object get(Comparable key) {
        check(key);
        return redBlackTree.search(key);
    }

    @Override
    public ArrayList<Map.Entry> headMap(Comparable toKey) {
        check(toKey);
        ArrayList<Map.Entry> list = new ArrayList<>();
        redBlackTree.entrySetInList(redBlackTree.getRoot(),list,(T)toKey,false);
        return list;
    }


    @Override
    public ArrayList<Map.Entry> headMap(Comparable toKey, boolean inclusive) {
        check(toKey);
        ArrayList<Map.Entry> list = new ArrayList<>();
        redBlackTree.entrySetInList(redBlackTree.getRoot(),list,(T)toKey,inclusive);
        return list;
    }

    @Override
    public Set keySet() {
        Set<T> set = new LinkedHashSet<>();
        redBlackTree.keyset(redBlackTree.getRoot(),set);
        return set;
    }

    @Override
    public Map.Entry lastEntry() {
        if(redBlackTree.getRoot() == null)
            return null;
        INode node = redBlackTree.Maximum(redBlackTree.getRoot());
        return new AbstractMap.SimpleEntry(node.getKey(),node.getValue());
    }

    @Override
    public Comparable lastKey() {
        if(redBlackTree.getSize() == 0)
            return null;
        return redBlackTree.Maximum(redBlackTree.getRoot()).getKey();
    }

    @Override
    public Map.Entry pollFirstEntry() {
        if(redBlackTree.getSize() == 0)
            return null;
        INode node = redBlackTree.Minimum(redBlackTree.getRoot());
        redBlackTree.delete(node.getKey());
        return new AbstractMap.SimpleEntry(node.getKey(),node.getValue());
    }

    @Override
    public Map.Entry pollLastEntry() {
        if(redBlackTree.getSize() == 0)
            return null;
        INode node = redBlackTree.Maximum(redBlackTree.getRoot());
        redBlackTree.delete(node.getKey());
        return new AbstractMap.SimpleEntry(node.getKey(),node.getValue());
    }

    @Override
    public void put(Comparable key, Object value) {
        check(key);
        check(value);
        redBlackTree.insert(key,value);
    }

    @Override
    public void putAll(Map map) {
        if(map == null)
            check(null);
        Set<Map.Entry> set =  map.entrySet();
        for(Map.Entry entry : set){
            redBlackTree.insert((T)entry.getKey(),entry.getValue());
        }
    }

    @Override
    public boolean remove(Comparable key) {
        check(key);
        return redBlackTree.delete(key);
    }

    @Override
    public int size() {
        return redBlackTree.getSize();
    }

    @Override
    public Collection values() {
        ArrayList<V> list = new ArrayList<>();
        redBlackTree.values(redBlackTree.getRoot(),list);
        return  list;
    }

    private void check(Comparable key){
        if(key == null)
            throw new RuntimeErrorException(new Error());
    }
    private void check(Object key){
        if(key == null)
            throw new RuntimeErrorException(new Error());
    }
}
