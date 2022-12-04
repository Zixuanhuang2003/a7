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
    List<Node> nodes = new ArrayList<>();
    List<Node> coinlist = new ArrayList<>();

    /** See {@code SewerDriver} for specification. */
    @Override
    public void seek(SeekState state) {
        dfs_search(state);
    }

    /** See {@code SewerDriver} for specification. */
    @Override
    public void scram(ScramState state) {
        nodes = new ArrayList<>();
        for(Node n : state.allNodes()){
            nodes.add(n);
        }
        while(state.currentNode()!=state.exit()){
            find_coins_route(state);
        }
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

    public void Shortest_escape_route(ScramState state){
        Maze escape_route = new Maze(Collections.unmodifiableSet(new HashSet<>(nodes)));
        ShortestPaths<Node, Edge> dijkstra = new ShortestPaths<>(escape_route);
        dijkstra.singleSourceDistances(state.currentNode());
        List<Edge> route = dijkstra.bestPath(state.exit());
        for(Edge e : route){
            state.moveTo(e.destination());
        }
        return;
    }

    public void find_coins_route(ScramState state){
        gen_coin_list(state);
        Maze escape_route = new Maze(Collections.unmodifiableSet(new HashSet<>(nodes)));
        ShortestPaths<Node, Edge> dijkstra = new ShortestPaths<>(escape_route);
        if(state.currentNode()==state.exit())return;
        if(coinlist.size() == 0){
            Shortest_escape_route(state);
            return;
        }
        Node default_coin = coinlist.get(0);
        dijkstra.singleSourceDistances(state.currentNode());
        List<Edge> route = dijkstra.bestPath(default_coin);
        Double route_distance = dijkstra.getDistance(default_coin);
        for(Node n : coinlist){
            List<Edge> other_route = dijkstra.bestPath(n);
            Double other_route_distance = dijkstra.getDistance(n);
            if(other_route_distance < route_distance){
                route = other_route;
                default_coin = n;
                route_distance = other_route_distance;
            }
        }
        dijkstra.singleSourceDistances(default_coin);
        Double shortest_path_to_exit_distance = dijkstra.getDistance(state.exit());
        if(route_distance + shortest_path_to_exit_distance >= state.stepsToGo()){
            dijkstra.singleSourceDistances(state.currentNode());
            route = dijkstra.bestPath(state.exit());
        }
        for(Edge e : route){
            state.moveTo(e.destination());
        }
    }

    public void gen_coin_list(ScramState state){
        coinlist = new ArrayList<>();
        for(Node n : state.allNodes()){
            if(n.getTile().coins() > 0) coinlist.add(n);
        }
    }

}
