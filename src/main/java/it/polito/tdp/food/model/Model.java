package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private Graph<Food,DefaultWeightedEdge> grafo;
	private FoodDao dao;
	private Map<Integer,Food> idMap;
	
	public Model() {
		dao = new FoodDao();
		idMap = new HashMap<>();
		dao.listAllFoods(idMap);
	}
	
	public void creaGrafo(int porzioni) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class); 
		Graphs.addAllVertices(grafo, dao.getVertici(idMap, porzioni));
		
		for (Adiacenze a: dao.getAdiacenze(idMap)) {
			if (grafo.containsVertex(a.getF1()) && grafo.containsVertex(a.getF2())) {
				Graphs.addEdgeWithVertices(grafo, a.getF1(), a.getF2(), a.getPeso());
			}
		}
		
		System.out.format("Grafo creato con %d vertici e %d archi\n",
 				this.grafo.vertexSet().size(), this.grafo.edgeSet().size()); 
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Food> getDescrizioneCibi(){
		List<Food> descrizioni = new ArrayList<>();
		for (Food f: grafo.vertexSet()) {
			descrizioni.add(f);
		}
		Collections.sort(descrizioni);
		return descrizioni;
	}
	
	public List<ArcoPeso> getConnessi(Food food){
		List<ArcoPeso> result = new ArrayList<>();
		double max=0.0;
		Food f = idMap.get(food.getFood_code());
		for (Food cibo: Graphs.neighborListOf(grafo, f)) {
			DefaultWeightedEdge e = grafo.getEdge(f, cibo);
			if (grafo.getEdgeWeight(e)>max) {
				max=grafo.getEdgeWeight(e);
			}
		}
		for (Food cibo: Graphs.neighborListOf(grafo, f)) {
			DefaultWeightedEdge e = grafo.getEdge(f, cibo);
			if (grafo.getEdgeWeight(e)==max) {
				result.add(new ArcoPeso(cibo, max));
			}
		}
		
		return result;
	}

}
