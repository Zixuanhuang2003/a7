package diver;

import game.*;
import graph.ShortestPaths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/** This is the place for your implementation of the {@code SewerDiver}.
 */
public class McDiver implements SewerDiver {

    Map<Long, Boolean> visited_nodes = new HashMap<>();

    /** See {@code SewerDriver} for specification. */
    @Override
    public void seek(SeekState state) {
        // TODO : Look for the ring and return.
        // DO NOT WRITE ALL THE CODE HERE. DO NOT MAKE THIS METHOD RECURSIVE.
        // Instead, write your method (it may be recursive) elsewhere, with a
        // good specification, and call it from this one.
        //
        // Working this way provides you with flexibility. For example, write
        // one basic method, which always works. Then, make a method that is a
        // copy of the first one and try to optimize in that second one.
        // If you don't succeed, you can always use the first one.
        //
        // Use this same process on the second method, scram.
        dfs_search(state);
    }

    /** See {@code SewerDriver} for specification. */
    @Override
    public void scram(ScramState state) {
        // TODO: Get out of the sewer system before the steps are used up.
        // DO NOT WRITE ALL THE CODE HERE. Instead, write your method elsewhere,
        // with a good specification, and call it from this one.
        scram_route(state);

    }


    public void dfs_search(SeekState state){
        long id = 0;
        long cur = state.currentLocation();
        ArrayList<NodeStatus> neighbor_list = new ArrayList<>();
        visited_nodes.put(cur, true);
        if (state.distanceToRing()!=0){
            for(NodeStatus ns : state.neighbors()){
                id = ns.getId();
                if(!visited_nodes.containsKey(id)) visited_nodes.put(id, false);
                neighbor_list.add(ns);
            }
            for(NodeStatus node_stat : neighbor_list){
                if(visited_nodes.get(node_stat.getId())==false) {
                    state.moveTo(node_stat.getId());
                    seek(state);
                    if(state.distanceToRing() == 0) return;
                    state.moveTo(cur);
                }
            }
        }
        else return;
    }

    public void scram_route(ScramState state){
        List<Node> nodes = new ArrayList<>();
        for(Node n : state.allNodes()){
            nodes.add(n);
        }
        Maze escape_route = new Maze(Collections.unmodifiableSet(new HashSet<>(nodes)));
        ShortestPaths<Node, Edge> dijkstra = new ShortestPaths<>(escape_route);
        dijkstra.singleSourceDistances(state.currentNode());
        List<Edge> route = dijkstra.bestPath(state.exit());
        System.out.println("current at :" + state.currentNode().getId());
        System.out.println("route :" + route.size());
        for(Edge e : route){
            System.out.println(e.destination().getId());
            state.moveTo(e.destination());
        }
    }
}
