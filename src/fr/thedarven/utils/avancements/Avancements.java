package fr.thedarven.utils.avancements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import fr.thedarven.main.LGUHC;
import fr.thedarven.utils.SqlConnection;

public class Avancements {

	public static void modifierAvancement(Player player, String nom) {
		if(!LGUHC.developpement && LGUHC.sqlConnect) {
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT * FROM site_succes_list WHERE nom_plugin = ?");
				q.setString(1, nom);
				ResultSet resultat = q.executeQuery();
				while(resultat.next()){
					try {
						int value = aAvancement(player, resultat.getInt("id"));
						if(value != -1) {
							PreparedStatement q1 = SqlConnection.connection.prepareStatement("UPDATE site_succes_joueur_avancement SET valeur = ? WHERE uuid = ? AND id_succes = ?");
							q1.setInt(1, value+1);
							q1.setString(2, player.getUniqueId().toString());
							q1.setInt(3, resultat.getInt("id"));
				        	q1.execute();
				        	q1.close(); 
						}else {
							créerAvancement(player, resultat.getInt("id"));
						}
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
	
	private static void créerAvancement(Player player, int id_succes) {
		if(!LGUHC.developpement && LGUHC.sqlConnect) {
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("INSERT INTO site_succes_joueur_avancement(id_succes,uuid) VALUES (?,?)");
		         q.setInt(1, id_succes);
		         q.setString(2, player.getUniqueId().toString());
		         q.execute();
		         q.close();
			 } catch (SQLException error) {
				 error.printStackTrace();
		     }
		}
	}
	
	private static int aAvancement(Player player, int id_succes){
		if(!LGUHC.developpement && LGUHC.sqlConnect) {
			try {
				PreparedStatement q = SqlConnection.connection.prepareStatement("SELECT * FROM site_succes_joueur_avancement WHERE uuid = ? AND id_succes = ?");
				q.setString(1, player.getUniqueId().toString());
				q.setInt(2, id_succes);
				ResultSet resultat = q.executeQuery();
				while(resultat.next()){
					return resultat.getInt("valeur");
				}
			} catch (SQLException error) {
				error.printStackTrace();
			}
		}
		return -1;
	}
}
