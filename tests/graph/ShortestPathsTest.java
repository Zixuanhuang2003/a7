package graph;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ShortestPathsTest {
    /** The graph example from lecture */
    static final String[] vertices1 = { "a", "b", "c", "d", "e", "f", "g" };
    static final int[][] edges1 = {
        {0, 1, 9}, {0, 2, 14}, {0, 3, 15},
        {1, 4, 23},
        {2, 4, 17}, {2, 3, 5}, {2, 5, 30},
        {3, 5, 20}, {3, 6, 37},
        {4, 5, 3}, {4, 6, 20},
        {5, 6, 16}
    };
    static class TestGraph implements WeightedDigraph<String, int[]> {
        int[][] edges;
        String[] vertices;
        Map<String, Set<int[]>> outgoing;

        TestGraph(String[] vertices, int[][] edges) {
            this.vertices = vertices;
            this.edges = edges;
            this.outgoing = new HashMap<>();
            for (String v : vertices) {
                outgoing.put(v, new HashSet<>());
            }
            //创建新的hashtable，并赋值给vertices
            for (int[] edge : edges) {
                outgoing.get(vertices[edge[0]]).add(edge);
                //每个edge 有比如说，3个指，有很多个edge，这里是找到对应的每个edge的头一个值的vertecy，并把
                //3个值都加给它？
            }
        }
        public Iterable<int[]> outgoingEdges(String vertex) { return outgoing.get(vertex); }
        public String source(int[] edge) { return vertices[edge[0]]; }
        public String dest(int[] edge) { return vertices[edge[1]]; }
        public double weight(int[] edge) { return edge[2]; }
    }
    static TestGraph testGraph1() {
        return new TestGraph(vertices1, edges1);
    }

    @Test
    void lectureNotesTest() {
        TestGraph graph = testGraph1();
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph);
        ssp.singleSourceDistances("a");
        assertEquals(50, ssp.getDistance("g"));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("g")) {
            sb.append(" " + vertices1[e[0]]);
        }
        sb.append(" g");
        assertEquals("best path: a c e f g", sb.toString());
    }

    // TODO: Add 2 more tests
    //Test2: change best path, test
    static final String[] vertices2 = { "a", "b", "c", "d", "e", "f", "g" };
    static final int[][] edges2 = {
            {0, 1, 9}, {0, 2, 14}, {0, 3, 20},
            {1, 4, 23},
            {2, 4, 17}, {2, 3, 5}, {2, 5, 30},
            {3, 5, 5}, {3, 6, 37},
            {4, 5, 3}, {4, 6, 20},
            {5, 6, 5}
    };
    void Test2() {
        TestGraph graph = testGraph1();
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph);
        ssp.singleSourceDistances("a");
        assertEquals(29, ssp.getDistance("g"));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("g")) {
            sb.append(" " + vertices1[e[0]]);
        }
        sb.append(" g");
        assertEquals("best path: a c d f g", sb.toString());
    }

    //Test 3: change best path, test
    static final String[] vertices3 = { "a", "b", "c", "d", "e", "f", "g" };
    static final int[][] edges3 = {
            {0, 1, 9}, {0, 2, 14}, {0, 3, 20},
            {1, 4, 11},
            {2, 4, 17}, {2, 3, 5}, {2, 5, 30},
            {3, 5, 5}, {3, 6, 37},
            {4, 5, 3}, {4, 6, 20},
            {5, 6, 5}
    };
    void Test3() {
        TestGraph graph = testGraph1();
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph);
        ssp.singleSourceDistances("a");
        assertEquals(29, ssp.getDistance("g"));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("g")) {
            sb.append(" " + vertices1[e[0]]);
        }
        sb.append(" g");
        assertEquals("best path: a b e f g", sb.toString());
    }

    //Test 4: two path has same distance, test best path.
    static final String[] vertices4 = { "a", "b", "c", "d", "e", "f", "g" };
    static final int[][] edges4 = {
            {0, 1, 9}, {0, 2, 14}, {0, 3, 20},
            {1, 4, 11},
            {2, 4, 17}, {2, 3, 5}, {2, 5, 30},
            {3, 5, 5}, {3, 6, 37},
            {4, 5, 3}, {4, 6, 20},
            {5, 6, 5}
    };
    void Test4() {
        TestGraph graph = testGraph1();
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph);
        ssp.singleSourceDistances("a");
        assertEquals(29, ssp.getDistance("g"));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("g")) {
            sb.append(" " + vertices1[e[0]]);
        }
        sb.append(" g");
        assertEquals("best path: a c d f g", sb.toString());
    }
}
