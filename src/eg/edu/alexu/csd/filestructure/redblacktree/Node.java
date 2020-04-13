package eg.edu.alexu.csd.filestructure.redblacktree;

public class Node<T extends Comparable<T>, V> implements INode {
    private INode parent = null;
    private INode leftChild = null;
    private INode rightChild = null;
    private T key;
    private V val;
    private boolean color = BLACK;

    public Node() {
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
        this.val = val;
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
        return this.equals(null);
    }
}