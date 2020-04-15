package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;
import java.util.*;

public class RedBlackTree <T extends Comparable<T>, V> implements IRedBlackTree {
    private INode root = null;
    private int size = 0;

    public int getSize() {
        return size;
    }

    @Override
    public INode getRoot() {
        return this.root;
    }

    public void setRoot(INode<T,V> root) {
        this.root = root;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public void clear() {
        this.root = null;
        size = 0;
    }

    @Override
    public Object search(Comparable key) {

        check(key);
        INode node = root;
        if(node == null)
            return null;

        while (node != Node.NullNode){
            if(node.getKey().equals(key))
                return node.getValue();
            else if(key.compareTo(node.getKey()) < 0)
                node = node.getLeftChild();
            else
                node = node.getRightChild();
        }
        return null;
    }


    public INode searchNode(Comparable key) {
        if(key == null) {
            return null;
        }

        INode node = root;
        if(node == null)
            return null;

        while (node != Node.NullNode){
            if(node.getKey().equals(key))
                return node;
            else if(key.compareTo(node.getKey()) < 0)
                node = node.getLeftChild();
            else
                node = node.getRightChild();
        }
        return null;
    }

    /**
     * there is an edit Mon 13-4-2020
     * @author Mohammed Magdy Abd-Elghany
     * @editor Mahmoud Kamal Mahmoud
     * @param key to search.
     * @return true if there is the searched key and false other wise
     */
    @Override
    public boolean contains(Comparable key) {
        check(key);

        return this.search(key) != null;
    }

    @Override
    public void insert(Comparable key, Object value) {
        check(key);
        check(value);
        if (root == null){
            INode node = new Node(key,value,INode.BLACK);
            this.root = node;
            size++;
            return;
        }

        // just BST insert
        INode parent = null;
        INode node = root;
        while (node != Node.NullNode){
            if(key.compareTo(node.getKey()) < 0) {
                parent = node;
                node = node.getLeftChild();
            }else if(key.compareTo(node.getKey()) > 0){
                parent = node;
                node = node.getRightChild();
            }else{
                node.setValue(value);
                return;
            }
        }

        node = new Node(key,value,INode.RED);

        if(key.compareTo(parent.getKey()) < 0)
            parent.setLeftChild(node);
        else
            parent.setRightChild(node);

        node.setParent(parent);

        // fixing the tree started now !!!!
        // while i'm not the root and parent is red
        while (node.getParent() != Node.NullNode && node.getParent().getColor() == INode.RED) {
            // here is the case where I inserted in left sub-tree
            if (node.getParent().getParent().getLeftChild() == node.getParent()) {
                INode y = node.getParent().getParent().getRightChild();
                if (y != Node.NullNode && y.getColor() == INode.RED) {
                    node.getParent().getParent().setColor(INode.RED);
                    node.getParent().setColor(INode.BLACK);
                    y.setColor(INode.BLACK);
                    node = node.getParent().getParent();
                } else {
                    if (node.getParent().getRightChild() == node) {
                        leftRotate(node.getParent());
                        node = node.getLeftChild();
                    }
                    node.getParent().setColor(INode.BLACK);
                    node.getParent().getParent().setColor(INode.RED);
                    rightRotate(node.getParent().getParent());
                }
            }
            // here is the case where I inserted in right sub-tree
            // just replaced left with right
            else {
                INode y = node.getParent().getParent().getLeftChild();
                if (y != Node.NullNode && y.getColor() == INode.RED) {
                    node.getParent().getParent().setColor(INode.RED);
                    node.getParent().setColor(INode.BLACK);
                    y.setColor(INode.BLACK);
                    node = node.getParent().getParent();
                } else {
                    if (node.getParent().getLeftChild() == node) {
                        rightRotate(node.getParent());
                        node = node.getRightChild();
                    }
                    node.getParent().setColor(INode.BLACK);
                    node.getParent().getParent().setColor(INode.RED);
                    leftRotate(node.getParent().getParent());
                }
            }
        }

        root.setColor(INode.BLACK);
        size++;
    }

    @Override
    public boolean delete(Comparable key) {
        // if key equals null, then throw exception.
        if (key == null)
            throw new RuntimeErrorException(null);
        if(size == 0) return false;
        else if(size == 1) {
            root.setValue(null);
            root.setValue(null);
            root.setColor(false);
            root.setRightChild(Node.NullNode);
            root.setLeftChild(Node.NullNode);
        }
        // search about the wanted node and return it.
        INode<T, V> wanted_node = this.searchNode(key);
        if(wanted_node == null || this.isEmpty()) return false;
        INode<T, V> next, dummy;
        boolean next_original_color = wanted_node.getColor();
        if (wanted_node.getLeftChild() == Node.NullNode || wanted_node.getLeftChild() == null) {
            dummy = wanted_node.getRightChild();
            RB_Transplant(wanted_node, wanted_node.getRightChild());
        }
        else if (wanted_node.getRightChild() == Node.NullNode || wanted_node.getRightChild() == null) {
            dummy = wanted_node.getLeftChild();
            RB_Transplant(wanted_node, wanted_node.getLeftChild());
        }
        else {
            next = this.Minimum(wanted_node.getRightChild());
            next_original_color = next.getColor();
            dummy = next.getRightChild();
            if((dummy != Node.NullNode || dummy != null) && wanted_node == next.getParent())
                dummy.setParent(next);
            else {
                RB_Transplant(next, next.getRightChild());
                next.setRightChild(wanted_node.getRightChild());
                if(next.getRightChild() != null || next.getRightChild() != Node.NullNode)
                    next.getRightChild().setParent(next);
            }
            RB_Transplant(wanted_node, next);
            next.setLeftChild(wanted_node.getLeftChild());
            next.getLeftChild().setParent(next);
            next.setColor(wanted_node.getColor());
        }
        if(next_original_color == INode.BLACK)
            RB_delete_fixup(dummy);
        --size;
        return true;
    }

    private void RB_delete_fixup(INode<T,V> x) {
        while(x != this.root && x.getColor() != INode.RED) {
            if(x == x.getParent().getLeftChild()) {
                INode<T, V> w = x.getParent().getRightChild();
                if (w.getColor() == INode.RED) {
                    w.setColor(INode.BLACK);
                    x.getParent().setColor(INode.RED);
                    this.leftRotate(x.getParent());
                    w = x.getParent().getRightChild();
                }
                if (w.getLeftChild().getColor() == INode.BLACK && w.getRightChild().getColor() == INode.BLACK) {
                    w.setColor(INode.RED);
                    x = x.getParent();
                } else {
                    if (w.getRightChild().getColor() == INode.BLACK) {
                        w.getLeftChild().setColor(INode.BLACK);
                        w.setColor(INode.RED);
                        this.rightRotate(w);
                        w = x.getParent().getRightChild();
                    }
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(INode.BLACK);
                    if(w.getRightChild() != null)
                        w.getRightChild().setColor(INode.BLACK);
                    this.leftRotate(x.getParent());
                    x = (Node) this.getRoot();
                }
            }
            else {
                INode<T, V> w = x.getParent().getLeftChild();
                if (w.getColor() == INode.RED) {
                    w.setColor(INode.BLACK);
                    x.getParent().setColor(INode.RED);
                    this.rightRotate(x.getParent());
                    w = x.getParent().getLeftChild();
                }
                if (w.getRightChild().getColor() == INode.BLACK && w.getLeftChild().getColor() == INode.BLACK) {
                    w.setColor(INode.RED);
                    x = x.getParent();
                } else {
                    if (w.getLeftChild().getColor() == INode.BLACK) {
                        w.getRightChild().setColor(INode.BLACK);
                        w.setColor(INode.RED);
                        this.leftRotate(w);
                        w = x.getParent().getLeftChild();
                    }
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(INode.BLACK);
                    w.getLeftChild().setColor(INode.BLACK);
                    this.rightRotate(x.getParent());
                    x = (Node) this.getRoot();
                }
            }
        }
        x.setColor(INode.BLACK);
    }

    private void RB_Transplant(INode<T,V> u, INode<T,V> v) {
        if(u == null || v == null)
            return;
        if(u.getParent() == Node.NullNode || u.getParent() == null)
            this.setRoot(v);
        else if(u == u.getParent().getLeftChild())
            u.getParent().setLeftChild(v);
        else u.getParent().setRightChild(v);
        v.setParent(u.getParent());
    }

    public Collection inorder_tree_walk(INode x) {
        Collection<V> collection = new Vector<>();
        while(x != null) {
            if(x.getLeftChild() == null) {
                collection.add((V) x.getValue());
                x = x.getRightChild();
            } else {
                INode dummy = x.getLeftChild();
                while(dummy.getRightChild() != null && dummy.getRightChild() != x)
                    dummy = dummy.getRightChild();
                if(dummy.getRightChild() != null) {
                    dummy.setRightChild(x);
                    x = x.getLeftChild();
                } else {
                    dummy.setRightChild(null);
                    collection.add((V) x.getValue());
                    x = x.getRightChild();
                }
            }
        }
        return collection;
    }

    public INode Minimum(INode x){
        while(x.getLeftChild() != Node.NullNode)
            x = x.getLeftChild();
        return x;
    }

    public INode Maximum(INode x){
        while(x.getRightChild() != Node.NullNode)
            x = x.getRightChild();
        return x;
    }

    private void leftRotate(INode x){
        INode y = x.getRightChild();
        // making left child of y right child of x
        x.setRightChild(y.getLeftChild());
        if(x.getRightChild() != Node.NullNode){
            x.getRightChild().setParent(x);
        }
        // setting parent
        y.setParent(x.getParent());
        if(y.getParent() == Node.NullNode)
            this.root = y;
        else if(y.getParent().getLeftChild() == x)
            y.getParent().setLeftChild(y);
        else if(y.getParent().getRightChild() == x)
            y.getParent().setRightChild(y);
        // setting x left child for y
        x.setParent(y);
        y.setLeftChild(x);
    }

    private void rightRotate(INode x){
        INode y = x.getLeftChild();
        // setting right of y left of x
        x.setLeftChild(y.getRightChild());
        if(y.getRightChild() != Node.NullNode)
            y.getRightChild().setParent(x);

        // setting parent
        y.setParent(x.getParent());
        if(x.getParent() == Node.NullNode)
            this.root = y;
        else if(x.getParent().getLeftChild() == x)
            x.getParent().setLeftChild(y);
        else if(x.getParent().getRightChild() == x)
            x.getParent().setRightChild(y);

        // setting x right child of y
        y.setRightChild(x);
        x.setParent(y);
    }


    public boolean searchvalue(Object value,INode node){
        if(node == Node.NullNode)
            return false;

        boolean x = searchvalue(value,node.getLeftChild());

        if(node.getValue().equals(value))
            return true;

        boolean y = searchvalue(value,node.getRightChild());

        return x|y;
    }
    // first bigger
    public INode successor(INode node){
        if(node.getRightChild() != null)
            return Minimum(node.getRightChild());
        INode y = node.getParent();
        while ( y!= null && node == y.getRightChild()){
            node = y;
            y = y.getParent();
        }
        return y;
    }
    // first smaller
    public INode predecessor(INode node){
        if(node.getLeftChild() != null)
            return Maximum(node.getLeftChild());
        INode y = node.getParent();
        while (y != null && y.getLeftChild() == null){
            node = y;
            y = y.getParent();
        }
        return y;
    }

    public void entryset(INode node, Set<Map.Entry> set){
        if(node == Node.NullNode)
            return;

        entryset(node.getLeftChild(),set);
        set.add(new AbstractMap.SimpleEntry(node.getKey(),node.getValue()));
        entryset(node.getRightChild(),set);
    }

    public void keyset(INode node, Set<T> set){
        if(node == Node.NullNode)
            return;

        keyset(node.getLeftChild(),set);
        set.add((T)node.getKey());
        keyset(node.getRightChild(),set);
    }

    public void values(INode node,ArrayList<V> list){
        if(node == Node.NullNode)
            return;
        values(node.getLeftChild(),list);
        list.add((V)node.getValue());
        values(node.getRightChild(),list);
    }


    public  void entrySetInList(INode node,ArrayList<Map.Entry> entryList,T key,boolean inclusive){
        if(node == Node.NullNode)
            return;
        entrySetInList(node.getLeftChild(),entryList,key,inclusive);
        if(node.getKey().compareTo(key) < 0)entryList.add(new AbstractMap.SimpleEntry(node.getKey(),node.getValue()));
        if(node.getKey().compareTo(key) == 0 && inclusive)
            entryList.add(new AbstractMap.SimpleEntry(node.getKey(),node.getValue()));
        if(key.compareTo((T)node.getKey()) > 0) entrySetInList(node.getRightChild(),entryList,key,inclusive);
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