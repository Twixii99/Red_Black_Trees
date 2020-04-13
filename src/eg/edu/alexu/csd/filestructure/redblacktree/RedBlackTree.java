package eg.edu.alexu.csd.filestructure.redblacktree;

public class RedBlackTree <T extends Comparable<T>, V> implements IRedBlackTree {
    private INode root = null;

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
    }

    @Override
    public Object search(Comparable key) {
        INode node = root;
        if(node == null)
            return null;
        while (node != null){
            if(node.getKey().equals(key))
                return node.getValue();
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
        return !this.search(key).equals(null);
    }

    @Override
    public void insert(Comparable key, Object value) {
        if (root == null){
            INode node = new Node(key,value,INode.BLACK);
            this.root = node;
            return;
        }
        // just BST insert
        INode parent = null;
        INode node = root;
        while (node != null){
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
        while (node.getParent() != null && node.getParent().getColor() == INode.RED) {
            // here is the case where I inserted in left sub-tree
            if (node.getParent().getParent().getLeftChild() == node.getParent()) {
                INode y = node.getParent().getParent().getRightChild();
                if (y != null && y.getColor() == INode.RED) {
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
                if (y != null && y.getColor() == INode.RED) {
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
    }


    @Override
    public boolean delete(Comparable key) {
        INode<T, V> wanted_node = (Node)this.search(key);
        if(wanted_node.isNull()) return true;
        INode<T, V> next = wanted_node, dummy = null;
        boolean next_original_color = wanted_node.getColor();
        if (wanted_node.getLeftChild().isNull()) {
            dummy = wanted_node.getRightChild();
            RB_Transplant(wanted_node, dummy);
        }
        else if (wanted_node.getRightChild().isNull()) {
            dummy = wanted_node.getLeftChild();
            RB_Transplant(wanted_node, dummy);
        }
        else {
            next = Minimum(wanted_node.getRightChild());
            next_original_color = next.getColor();
            dummy = next.getRightChild();
            if(next.getParent() == wanted_node)
                dummy = next.getParent();
            else {
                RB_Transplant(next, next.getRightChild());
                next.setRightChild(wanted_node.getRightChild());
                next.getRightChild().setParent(next);
            }
            RB_Transplant(wanted_node, next);
            next.setLeftChild(wanted_node.getLeftChild());
            next.getLeftChild().setParent(next);
            next.setColor(wanted_node.getColor());
        }
        if(next_original_color == INode.BLACK)
            RB_delete_fixup(dummy);
        return false;
    }

    private void RB_delete_fixup(INode<T,V> x) {
        while(x != this.getRoot() && x.getColor() != INode.RED) {
            if(x == x.getParent().getLeftChild()) {
                INode<T, V> w = (Node) (x.getParent().getRightChild());
                if (w.getColor() == INode.RED) {
                    w.setColor(INode.BLACK);
                    x.getParent().setColor(INode.RED);
                    this.leftRotate(x.getParent());
                    w = x.getParent().getRightChild();
                }
                if (w.getLeftChild().getColor() == INode.BLACK && w.getRightChild().getColor() == INode.BLACK) {
                    w.setColor(INode.BLACK);
                    x = x.getParent();
                } else{
                    if (w.getRightChild().getColor() == INode.BLACK) {
                        w.getLeftChild().setColor(INode.BLACK);
                        w.setColor(INode.RED);
                        this.rightRotate(w);
                        w = x.getParent().getRightChild();
                    }
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(INode.BLACK);
                    w.getRightChild().setColor(INode.BLACK);
                    leftRotate(x.getParent());
                    x = (Node) this.getRoot();
                }
            }
            else {
                INode<T, V> w = (Node) (x.getParent().getLeftChild());
                if (w.getColor() == INode.RED) {
                    w.setColor(INode.BLACK);
                    x.getParent().setColor(INode.RED);
                    this.rightRotate(x.getParent());
                    w = (Node) x.getParent().getLeftChild();
                }
                if (w.getLeftChild().getColor() == INode.BLACK && w.getRightChild().getColor() == INode.BLACK) {
                    w.setColor(INode.BLACK);
                    x = (Node) x.getParent();
                } else{
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

    private INode<T,V> Minimum(INode<T,V> x) {
        while(!x.getLeftChild().isNull())
            x = x.getLeftChild();
        return x;
    }

    private void RB_Transplant(INode<T,V> u, INode<T,V> v) {
        if(u.getParent().isNull())
            this.setRoot(v);
        else if(u == u.getParent().getLeftChild())
            u.getParent().setLeftChild(v);
        else u.getParent().setRightChild(v);
        if(!v.isNull())
            v.setParent(u.getParent());
    }


    private void leftRotate(INode x){
        INode y = x.getRightChild();
        // making left child of y right child of x
        x.setRightChild(y.getLeftChild());
        if(x.getRightChild() != null){
            x.getRightChild().setParent(x);
        }
        // setting parent
        y.setParent(x.getParent());
        if(y.getParent() == null)
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
        if(y.getRightChild() != null)
            y.getRightChild().setParent(x);

        // setting parent
        y.setParent(x.getParent());
        if(x.getParent() == null)
            this.root = y;
        else if(x.getParent().getLeftChild() == x)
            x.getParent().setLeftChild(y);
        else if(x.getParent().getRightChild() == x)
            x.getParent().setRightChild(y);

        // setting x right child of y
        y.setRightChild(x);
        x.setParent(y);
    }
}