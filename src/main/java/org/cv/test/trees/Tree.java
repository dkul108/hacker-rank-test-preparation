package org.cv.test.trees;


import java.util.*;

public class Tree<T> {

    private static final String TOTAL_POSTFIX = "";
    private static final String GRAND_TOTAL = "";

    private Root root;

    private Collection<T> initialList;
    private TotalsCallback<T> callback;
    private TreeTotalLevelsProvider<T> levelsProvider;
    private Map<Integer, FieldAccessor<T, String>> fieldModifiers;

    interface TotalsCallback<T> {
        T newTotal();

        void sum(T total, T item);
    }

    interface TreeTotalLevelsProvider<T> {
        List<GroupingTotalTreeLevel> provideLevels(T domain);
    }

    interface FieldAccessor<T, V> {
        void update(T object, V value);

        V extract(T object);
    }

    interface GroupingTotalTreeLevel {
        boolean isTotalNeeded();

        boolean isGrandTotalNeeed();

        String getGroupingName();

        boolean isAllowedToClearName();

    }


    @SuppressWarnings("unchecked")
    public List<T> getListWithGroupedTotals() {
        List<T> processedList = new ArrayList<>();
        buildTree();
        List<T> processedSubList = null;
        for (Map.Entry<String, Node> zeroLevelGroupEntry : root.groups.entrySet()) {
            Group zeroLevelGroup = (Group) zeroLevelGroupEntry.getValue();
            processedSubList = new ArrayList<>();
            reverseOrderGroupTraversal(zeroLevelGroup, processedSubList);
            processedList.addAll(processedSubList);
        }
        if (root.total != null) {
            processedList.add(root.total);
        }
        return processedList;
    }

    public void setCallback(TotalsCallback<T> callback) {
        this.callback = callback;
    }

    public void setFieldModifiers(Map<Integer, FieldAccessor<T, String>> fieldModifiers) {
        this.fieldModifiers = fieldModifiers;
    }

    public void setLevelsProvider(TreeTotalLevelsProvider<T> levelsProvider) {
        this.levelsProvider = levelsProvider;
    }

    public void setInitialList(Collection<T> initialList) {
        this.initialList = initialList;
    }

    public void assertIncomingData() {
        if (levelsProvider == null) {
            throw new IllegalStateException("you need to setup levelsProvider first with withLevelProvider() builder method");
        }
        if (fieldModifiers == null || fieldModifiers.isEmpty()) {
            throw new IllegalStateException("you need to setup field modifiers first with withFieldModifiers() builder method");
        }
        if (callback == null) {
            throw new IllegalStateException("you need to setup calculation callback first with withTotalsCallback() builder method");
        }
        if (initialList == null) {
            throw new IllegalStateException("you need to setup list to be processed first with withRawInitialList() builder method");
        }
    }

    private void buildTree() {
        root = new Root();
        for (T t : initialList) {
            root.add(getGroupingLevels(t).iterator(), t);
        }
    }

    @SuppressWarnings("unchecked")
    private void reverseOrderGroupTraversal(Node n, List<T> processedSubList) {
        if (!n.isLeaf()) {
            for (Map.Entry<String, Node> nodeEntry : ((Group) n).children.entrySet()) {
                reverseOrderGroupTraversal(nodeEntry.getValue(), processedSubList);
            }
        }
        if (n.isLeaf()) {
            processedSubList.add(((Leaf) n).value);
        } else {
            Group g = (Group) n;
            if (g.total != null) {
                processedSubList.add(g.total);
            }
        }
    }

    private void clearName(T leaf, int depth) {
        getFieldModifiers().get(depth).update(leaf, "");
    }

    private T newTotal(T source, int depth, String overrideValue) {
        T total = callback.newTotal();
        FieldAccessor<T, String> fieldAccessor = getFieldModifiers().get(depth);
        if (overrideValue == null) {
            fieldAccessor.update(total, fieldAccessor.extract(source) + TOTAL_POSTFIX);
        } else {
            fieldAccessor.update(total, overrideValue);
        }
        return total;
    }

    @SuppressWarnings("unchecked")
    private Node processGroup(Map<String, Node> childrenGroups, GroupingTotalTreeLevel level, T t) {
        int depth = getGroupingLevels(t).indexOf(level);
        Group group = (Group) childrenGroups.get(level.getGroupingName() + TOTAL_POSTFIX);
        if (group == null) {
            group = new Group();
            if (level.isTotalNeeded()) {
                group.total = newTotal(t, depth, null);
            }
            childrenGroups.put(level.getGroupingName() + TOTAL_POSTFIX, group);
        }
        if (level.isTotalNeeded()) {
            callback.sum(group.total, t);
        }
        if (group.isFirstSummarizedItemExists() && level.isAllowedToClearName()) {
            clearName(t, depth);
        }
        if (!group.isFirstSummarizedItemExists()) {
            group.firstSummarizedItem = t;
        }
        return group;
    }

    private Map<Integer, FieldAccessor<T, String>> getFieldModifiers() {
        return fieldModifiers;
    }

    private List<GroupingTotalTreeLevel> getGroupingLevels(T domain) {
        return levelsProvider.provideLevels(domain);
    }


    class Root {
        private T total;
        private Map<String, Node> groups = new LinkedHashMap<String, Node>();

        public void add(Iterator<GroupingTotalTreeLevel> levels, T t) {
            GroupingTotalTreeLevel zeroLevel = levels.next();
            if (zeroLevel.isGrandTotalNeeed()) {
                if (total == null) {
                    total = newTotal(t, 0, GRAND_TOTAL);
                }
                callback.sum(total, t);
            }
            getGroup(zeroLevel, t).add(levels, t);
        }

        @SuppressWarnings("unchecked")
        private Group getGroup(GroupingTotalTreeLevel zeroLevel, T t) {
            return (Group) processGroup(groups, zeroLevel, t);
        }
    }


    abstract class Node {
        protected abstract boolean isLeaf();
    }

    class Leaf extends Node {

        private T value;

        @Override
        public String toString() {
            return "Leaf [" + value.toString() + "]";
        }

        @Override
        protected boolean isLeaf() {
            return true;
        }
    }

    class Group extends Node {
        private T total;
        private Map<String, Node> children = new LinkedHashMap<String, Node>();
        private T firstSummarizedItem = null;

        public void add(Iterator<GroupingTotalTreeLevel> levels, T t) {
            add(this, levels, t);
        }

        @SuppressWarnings("unchecked")
        public void add(Node n, Iterator<GroupingTotalTreeLevel> levels, T t) {
            GroupingTotalTreeLevel level = levels.next();
            if (levels.hasNext()) {
                add(processGroup((Group) n, level, t), levels, t);
            } else {
                processLeaf((Group) n, level.getGroupingName(), t);
            }
        }

        private void processLeaf(Group existingGroup, String key, T t) {
            Leaf newLeaf = new Leaf();
            newLeaf.value = t;
            existingGroup.children.put(key + TOTAL_POSTFIX, newLeaf);
        }

        private Node processGroup(Group existingGroup, GroupingTotalTreeLevel level, T t) {
            return Tree.this.processGroup(existingGroup.children, level, t);
        }

        @Override
        protected boolean isLeaf() {
            return false;
        }

        private boolean isFirstSummarizedItemExists() {
            return firstSummarizedItem != null;
        }

        @Override
        public String toString() {
            return "Group [children=" + children.size() + " " + children + ", total=" + total + "]";
        }
    }


}
