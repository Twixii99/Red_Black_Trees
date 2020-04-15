package eg.edu.alexu.csd.filestructure.redblacktree;

import java.util.Map;

public class Node<T extends Comparable<T>, V> implements INode {
    public static INode NullNode = new Node(INode.BLACK,true);
    private INode parent = NullNode;
    private INode leftChild = NullNode;
    private INode rightChild = NullNode;
    private T key;
    private V val;
    private boolean color = BLACK;
    private boolean isNull = false;

    public Node() {
    }

    public Node(boolean color,boolean isNull) {
        this.color = color;
        this.isNull = isNull;
    }

    public Node(T key, V val, boolean color) {
        this.key = key;
        this.val = val;
        this.color = color;
    }

    @Override
    public void setParent(INode parent) {
        this.parent = parent;
    }

    @Override
    public INode getParent() {
        return parent;
    }

    @Override
    public void setLeftChild(INode leftChild) {
        this.leftChild = leftChild;
    }

    @Override
    public INode getLeftChild() {
        return leftChild;
    }

    @Override
    public void setRightChild(INode rightChild) {
        this.rightChild = rightChild;
    }

    @Override
    public INode getRightChild() {
        return rightChild;
    }

    @Override
    public Comparable getKey() {
        return this.key;
    }

    @Override
    public void setKey(Comparable key) {
        this.key = (T)key;
    }

    @Override
    public Object getValue() {
        return this.val;
    }

    @Override
    public void setValue(Object value) {
        this.val = (V)value;
    }

    @Override
    public boolean getColor() {
        return this.color;
    }

    @Override
    public void setColor(boolean color) {
        this.color = color;
    }

    @Override
    public boolean isNull() {
        return isNull;
    }
}