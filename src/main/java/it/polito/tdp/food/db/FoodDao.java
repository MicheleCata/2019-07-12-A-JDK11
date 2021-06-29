package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenze;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	
	public void listAllFoods(Map<Integer,Food> idMap){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Food f= new Food(res.getInt("food_code"),
							res.getString("display_name"));
					idMap.put(f.getFood_code(),f);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
			
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Food> getVertici(Map<Integer,Food> idMap, int porzioni){
		String sql = "SELECT p.`food_code` as cibo "
				+ "FROM `portion` p, food f "
				+ "where p.`food_code`=f.`food_code` "
				+ "group by cibo "
				+ "having Count(*)<= ? "
				+ "order by cibo asc";
		List<Food> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection() ;
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, porzioni);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(idMap.get(res.getInt("cibo")));
			}
			conn.close();
			return result ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Adiacenze> getAdiacenze(Map<Integer,Food> idMap){
		String sql = "SELECT fc1.`food_code` as f1, fc2.`food_code` as f2, AVG(c.`condiment_calories`) as peso "
				+ "FROM food_condiment fc1, food_condiment fc2, condiment c "
				+ "where fc1.`food_code`>fc2.`food_code` and fc1.`condiment_code`=fc2.`condiment_code` and fc1.`condiment_code`=c.`condiment_code` "
				+ "group by f1, f2 ";
		
		List<Adiacenze> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection() ;
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Food f1 = idMap.get(res.getInt("f1"));
				Food f2 = idMap.get(res.getInt("f2"));
				
				if (f1!=null && f2!=null) {
					result.add(new Adiacenze(f1,f2,res.getDouble("peso")));
				}
			}
			conn.close();
			return result ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
}
