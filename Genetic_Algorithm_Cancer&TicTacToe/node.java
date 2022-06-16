public class node {
    public node parent;
    public node left;
    public node right;
    public String data;

    public node(String data) {
        this.data = data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setParent(node parent) {
        this.parent = parent;
    }

    public void setLeft(node left) {
        this.left = left;
    }

    public void setRight(node right) {
        this.right = right;
    }
}
