package ca.mcgill.cs.FCA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ECLATree {
    private final HashMap<List<Method>, List<String>> data;
    private final Node root;

    public ECLATree(HashMap<List<Method>, List<String>> data, Node root) {
        this.data = data;
        this.root = root;
    }

    public Node addAllLevel() {
        return addAllLevel(root);
    }

    private Node addAllLevel(Node root) {
        if (root == null) return null;

        List<Node> children = addOneLevel(root);

        if (children.size() == 0) return root;

        for (Node aChild : children) {
            addAllLevel(aChild);
        }
        return root;
    }

    private List<Node> addOneLevel(Node root) {

        List<Method> all = root.values;
        List<Method> rest = new ArrayList<>(all);
        List<Node> levelRes = new ArrayList<>();

        if (rest.size() == 1) {
            return levelRes;
        }

        Node aChild = findMaxSub(rest);
        levelRes.add(aChild);
        root.addChild(aChild);
        List<Method> subMax = new ArrayList<>(aChild.values);
        rest.removeAll(subMax);

        while (rest.size() > 0) {
//            Node newChild;
//            if (rest.size() == 1) {
//                newChild = new Node(new ArrayList<>(rest));
//            } else {
//                newChild = findIncludedMaxSub(rest);
//            }
            Node newChild = (rest.size() == 1) ? new Node(new ArrayList<>(rest)) : findIncludedMaxSub(rest);

            levelRes.add(newChild);
            root.addChild(newChild);
            subMax = new ArrayList<>(newChild.values);
            rest.removeAll(subMax);
        }
        return levelRes;
    }

    // find the max sub list from FCA Matrix
    private Node findMaxSub(List<Method> all) {
        assert all != null;

        List<Method> res = new ArrayList<>();
        if (all.size() == 0) return null;

        for (Map.Entry<List<Method>, List<String>> entry : data.entrySet()) {
            if (all.containsAll(entry.getKey()) && !entry.getKey().containsAll(all)) {
                res = new ArrayList<>(entry.getKey());
                break;
            }
        }

        return new Node(res);
    }

    // find the max sub include itself
    private Node findIncludedMaxSub(List<Method> all) {
        List<List<Method>> res = findAllSub(new ArrayList<>(all));
        return new Node(new ArrayList<>(res.get(0)));
    }

    //find all possible subset in the hashmap
    private List<List<Method>> findAllSub(List<Method> all) {
        if (all == null) return null;

        List<List<Method>> res = new ArrayList<>();
        if (all.size() == 0) return null;

        for (Map.Entry<List<Method>, List<String>> entry : data.entrySet()) {
            if (all.containsAll(entry.getKey())) res.add(new ArrayList<>(entry.getKey()));
        }
        return res;
    }

}
