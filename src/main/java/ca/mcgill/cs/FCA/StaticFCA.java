package ca.mcgill.cs.FCA;

import java.util.*;
import java.util.stream.Collectors;

public class StaticFCA {

    /**
     * Return the powerSets of the given list of Methods
     *
     * @return return a nested list of Methods
     */

    private static List<List<Method>> powerSet(List<Method> originalList)
    {
        List<List<Method>> res = new ArrayList<>();
        if (originalList.isEmpty()) {
            res.add(new ArrayList<>());
            return res;
        }

        List<Method> aList = new ArrayList<>(originalList);
        List<Method> rest = new ArrayList<>(aList.subList(1, aList.size()));
        Method head = aList.get(0);

        for (List<Method> list : powerSet(rest)) {
            List<Method> newList = new ArrayList<>();
            newList.add(head);
            newList.addAll(list);
            res.add(newList);
            res.add(list);
        }
        return res;
    }

    public static HashMap<List<Method>, List<String>> findAllIntersection(List<List<Method>> input)
    {
        HashMap<List<Method>, List<String>> intersections = new HashMap<>();

        input.removeIf(List::isEmpty);
        for (List<Method> objects : input) {
            List<String> intersectionFeatures = new ArrayList<>(objects.get(0).getProperties());

            for (Method object : objects) {
                List<String> features = object.getProperties();
                intersectionFeatures.retainAll(features);
            }
            if (!intersectionFeatures.isEmpty()) {
                intersections.put(objects, intersectionFeatures);
            }
        }

        return intersections;
    }

    public static HashMap<List<Method>, List<String>> getSortedIntersections(HashMap<List<Method>, List<String>> intersections)
    {
        HashMap<List<Method>, List<String>> sortedMap = intersections.entrySet().stream()
                .sorted(Comparator.comparing(e -> -e.getKey().size()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> {
                            throw new AssertionError();
                        },
                        LinkedHashMap::new
                ));

        return filter(sortedMap);
    }

    private static HashMap<List<Method>, List<String>> filter(HashMap<List<Method>, List<String>> data)
    {
        HashMap<List<Method>, List<String>> res = new HashMap<>();
        for (Map.Entry<List<Method>, List<String>> entry : data.entrySet()) {
            if (!res.containsKey(entry.getKey()) && !res.containsValue(entry.getValue()))
                res.put(entry.getKey(), entry.getValue());
        }

        return res.entrySet().stream()
                .sorted(Comparator.comparing(e -> -e.getKey().size()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> {
                            throw new AssertionError();
                        },
                        LinkedHashMap::new
                ));
    }

    public static HashMap<List<Method>, List<String>> createFCAMAtrix(List<Method> input)
    {
        List<List<Method>> methods = powerSet(input);
        HashMap<List<Method>, List<String>> matrix = findAllIntersection(methods);
        return getSortedIntersections(matrix);
    }
}

