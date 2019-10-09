package fr.thedarven.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.thedarven.main.LGUHC;
import fr.thedarven.main.PlayerLG;


public class SqlRequest {
	
	public static int id_partie = 0;
	
	public static void createGame() {
		if(!LGUHC.developpement && LGUHC.sqlConnect) {
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("INSERT INTO site_partie(type,debut) VALUES (?,?)");
		         q.setString(1, "lguhc");
		         q.setInt(2, getTimestamp());
		         q.execute();
		         q.close();
			 } catch (SQLException error) {
				 error.printStackTrace();
		     }
			 
			 try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT * FROM site_partie WHERE 1 ORDER BY id DESC LIMIT 1");
		        ResultSet resultat = q.executeQuery();
		        while(resultat.next()){
		        	id_partie = resultat.getInt("id");
		        }
		            q.close();         
		        } catch (SQLException error) {
		        	error.printStackTrace();
		     }
		}
	}
	
	public static void updateGameDuree() {
		if(!LGUHC.developpement && LGUHC.sqlConnect) {
			try {
	        	PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_partie SET duree = ? WHERE id = ?");
	        	q.setInt(1, LGUHC.timer);
	        	q.setInt(2, id_partie);
	        	q.execute();
	        	q.close();         
	        } catch (SQLException error) {
	        	error.printStackTrace();
	        }       
			id_partie = 0;
		}
	}
	
	
	
	public static void createLG(Player p) {
		if(!LGUHC.developpement && LGUHC.sqlConnect) {
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("INSERT INTO site_role(id_partie,nom,uuid) VALUES (?,?,?)");
		         q.setInt(1, id_partie);
		         q.setString(2, PlayerLG.getPlayerManager(p.getUniqueId()).getRole().getName());
		         q.setString(3, p.getUniqueId().toString());
		         q.execute();
		         q.close();
			} catch (SQLException error) {
				error.printStackTrace();
		    }
		}
	}
	
	public static void updateLGRole(UUID uuid) {
		if(!LGUHC.developpement && LGUHC.sqlConnect) {
			try {
	            PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_role SET nom = ? WHERE uuid = ? AND id_partie = ?");
	            q.setString(1, PlayerLG.getPlayerManager(uuid).getRole().getName());
	            q.setString(2, uuid.toString());
	            q.setInt(3, id_partie);
	            q.execute();
	            q.close();         
	        } catch (SQLException error) {
	        	error.printStackTrace();
	        }
		}
	}
	
	public static void updateLGKill(Player p) {
		if(!LGUHC.developpement && LGUHC.sqlConnect) {
			try {
	            PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT * FROM site_role WHERE uuid = ? AND id_partie = ?");
	            q.setString(1, p.getUniqueId().toString());
	            q.setInt(2, id_partie);
	            ResultSet resultat = q.executeQuery();
	            while(resultat.next()){
	            	try {
	    				PreparedStatement q1 = SqlConnection.connection.prepareStatement("UPDATE site_role SET kills = ? WHERE id = ?");
	    	            q1.setInt(1, resultat.getInt("kills")+1);
	    	            q1.setInt(2, resultat.getInt("id"));
	    	            q1.execute();
	    	            q1.close();
	    	        } catch (SQLException error) {
	    	        	error.printStackTrace();
	    	        }
	            }
	            q.close();         
	        } catch (SQLException error) {
	        	error.printStackTrace();
	        }
		}
	}
	
	public static void updateLGInfecté(UUID uuid, Boolean infecté) {
		if(!LGUHC.developpement && LGUHC.sqlConnect) {
			int value = 0;
			if(infecté) {
				value = 1;
			}
			try {
	            PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_role SET infecte = ? WHERE uuid = ? AND id_partie = ?");
	            q.setInt(1, value);
	            q.setString(2, uuid.toString());
	            q.setInt(3, id_partie);
	            q.execute();
	            q.close();         
	        } catch (SQLException error) {
	        	error.printStackTrace();
	        }
		}
	}
	
	public static void updateLGCouple(UUID uuid) {
		if(!LGUHC.developpement && LGUHC.sqlConnect) {
			try {
	            PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_role SET couple = ? WHERE uuid = ? AND id_partie = ?");
	            q.setInt(1, 1);
	            q.setString(2, uuid.toString());
	            q.setInt(3, id_partie);
	            q.execute();
	            q.close();         
	        } catch (SQLException error) {
	        	error.printStackTrace();
	        }
		}
	}
	
	public static void updateLGMort(String uuid, int mort){
		if(!LGUHC.developpement && LGUHC.sqlConnect) {
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_role SET mort = ? WHERE uuid = ? AND id_partie = ?");
				q.setInt(1, mort);
				q.setString(2, uuid);
		        q.setInt(3, id_partie);
		        q.execute();
		        q.close();         
		    } catch (SQLException error) {
		    	error.printStackTrace();
		    }
		}
	}
	
	public static void updatePlayerTimePlay(Player p) {
		if(!LGUHC.developpement && LGUHC.sqlConnect && hasAccount(p)) {
			try {
	            PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT * FROM site_joueur WHERE uuid = ?");
	            q.setString(1, p.getUniqueId().toString());
	            ResultSet resultat = q.executeQuery();
	            while(resultat.next()){
	            	try {
        				PreparedStatement q1 = SqlConnection.connection.prepareStatement("UPDATE site_joueur SET time_play = ? WHERE uuid = ?");
        	            q1.setInt(1,resultat.getInt("time_play")+(getTimestamp()-resultat.getInt("last")));
        	            q1.setString(2, p.getUniqueId().toString());
        	            q1.execute();
        	            q1.close();
        	        } catch (SQLException error) {
        	        	error.printStackTrace();
        	        }
	            }
	            q.close();         
	        } catch (SQLException error) {
	        	error.printStackTrace();
	        }
		}
	}
	
	public static void updatePlayerLast(Player p) {
		if(!LGUHC.developpement && LGUHC.sqlConnect && hasAccount(p)) {
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_joueur SET last = ? WHERE uuid = ?");
	            q.setInt(1, getTimestamp());
	            q.setString(2, p.getUniqueId().toString());
	            q.execute();
	            q.close();
	        } catch (SQLException error) {
	        	error.printStackTrace();
	        }
			updatePlayerPseudo(p);
		}
	}
	
	public static void updatePlayerPseudo(Player p) {
		if(!LGUHC.developpement && LGUHC.sqlConnect) {
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("UPDATE site_joueur SET pseudo = ? WHERE uuid = ?");
		        q.setString(1, p.getName());
		        q.setString(2, p.getUniqueId().toString());
		        q.execute();
		        q.close();
		    } catch (SQLException error) {
		    	error.printStackTrace();
		    }
		}
	}
	
	public static boolean hasAccount(Player p){  
		if(!LGUHC.developpement && LGUHC.sqlConnect) {
	        try {
	            PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT uuid FROM site_joueur WHERE uuid = ?");
	            q.setString(1, p.getUniqueId().toString());
	            ResultSet resultat = q.executeQuery();
	            
	            while(resultat.next()){
	            	return true;
	            }
	            q.close();         
	        } catch (SQLException error) {
	        	error.printStackTrace();
	        }
	       
	        try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("INSERT INTO site_joueur(uuid,pseudo,last,time_play) VALUES(?,?,?,?)");
	            q.setString(1, p.getUniqueId().toString());
	            q.setString(2, p.getName());
	            q.setInt(3, getTimestamp());
	            q.setInt(4, 0);
	            q.execute();
	            q.close();
	        } catch (SQLException error) {
	        	error.printStackTrace();
	        }
		}
        return false;
    }
	
	public static int getTimestamp() {
		long date = new Date().getTime();
		return (int) ((date-(date%100))/1000);
	}
}
