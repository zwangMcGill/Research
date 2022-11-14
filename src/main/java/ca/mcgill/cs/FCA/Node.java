package ca.mcgill.cs.FCA;

import java.util.*;

public class Node {
    public List<Method> values; //data for storage
    public List<Node> children;//array will keep children
    public Node parent;//parent to start the tree

    public Node(List<Method> data) {
        children = new ArrayList<>();
        this.values = data;
    }

    public void setData(List<Method> data) {
        this.values = data;
    }

    public Node addChild(Node node) {
        children.add(node);
        node.parent = this;
        return this;
    }

    @Override
    public String toString() {
        return this.values.toString();
    }

    public List<String> getNestedName() {
        List<List<Method>> init = new ArrayList<>();
        children.forEach(e -> init.add(e.values));

        HashMap<List<Method>, List<String>> intersections = StaticFCA.findAllIntersection(init);
        HashMap<List<Method>, List<String>> data = StaticFCA.getSortedIntersections(intersections);

        List<List<String>> res = new ArrayList<>(data.values());
        List<String> firstRes = res.get(0);

//        for (List<String> object : res) {
//            firstRes.retainAll(object);
//        }

        res.forEach(firstRes::retainAll);

        return firstRes;
    }


    public String levelTraverse() {
        return levelTraverse(this);
    }

    private String levelTraverse(Node root) {
        StringBuilder sb = new StringBuilder();
        int level = 0;
        if (root == null) return null;
        Queue<Node> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            int sz = q.size();
            sb.append(level++).append("\t");
            for (int i = 0; i < sz; i++) {
                Node current = q.poll();
                sb.append(current.toString());
                current.children.forEach(q::offer);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}