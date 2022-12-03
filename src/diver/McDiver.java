package diver;

import cms.util.maybe.Maybe;
import datastructures.PQueue;
import datastructures.SlowPQueue;
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
        dfs_search(state);
    }

    /** See {@code SewerDriver} for specification. */
    @Override
    public void scram(ScramState state) {
        scram_route(state);
    }


    public void dfs_search(SeekState state){
        long id = 0;
        long cur = state.currentLocation();
        PQueue<NodeStatus> neighbor_list_pq = new SlowPQueue<>();
        visited_nodes.put(cur, true);
        if (state.distanceToRing()!=0){
            for(NodeStatus ns : state.neighbors()){
                id = ns.getId();
                if(!visited_nodes.containsKey(id)) visited_nodes.put(id, false);
                neighbor_list_pq.add(ns, ns.getDistanceToRing());
            }
            while(!neighbor_list_pq.isEmpty()){
                NodeStatus node_stat = neighbor_list_pq.extractMin();
                if(visited_nodes.get(node_stat.getId())==false){
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
//            System.out.println(n.);
            nodes.add(n);
        }
        Maze escape_route = new Maze(Collections.unmodifiableSet(new HashSet<>(nodes)));
        //shorttest path
        ShortestPaths<Node, Edge> dijkstra = new ShortestPaths<>(escape_route);
        dijkstra.singleSourceDistances(state.currentNode());
        List<Edge> route = dijkstra.bestPath(state.exit());
        for(Edge e : route){
            state.moveTo(e.destination());
        }
        //

        return;
    }
}
