import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
	
	private static TaxiDAO dao = new TaxiDAO();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Scanner sc = new Scanner(System.in);
//		addClient(sc);
		System.out.println("-------------------------------------");
		System.out.println("afficherNbConducteurParTypes();");
		afficherNbConducteurParTypes();
		System.out.println("-------------------------------------");
		System.out.println("afficherClients()");
		afficherClients();
		System.out.println("-------------------------------------");
		System.out.println("afficherConducteurs()");
		afficherConducteurs();
		System.out.println("-------------------------------------");
		System.out.println("afficherConducteurParID(2);");
		afficherConducteurParID(2);
		System.out.println("-------------------------------------");
		System.out.println("afficherClientParID(1);");
		afficherClientParID(1);
		System.out.println("-------------------------------------");
		System.out.println("afficher1erConducter();");
		afficher1erConducter();
		System.out.println("-------------------------------------");
		System.out.println("afficherAncienneteMoyClients();");
		afficherAncienneteMoyClients();
		System.out.println("-------------------------------------");
		System.out.println("afficherNbCourseEtDistClient(1);");
		afficherNbCourseEtDistClient(1);
		System.out.println("-------------------------------------");
		System.out.println("afficherNPremierConducteur(1);");
		afficherNPremierConducteur(1);
		System.out.println("-------------------------------------");
		System.out.println("afficherMoyTempsCours();");
		afficherMoyTempsCours();
		System.out.println("-------------------------------------");
		
	}
	
	
	
	
	
	public static void afficherRevenueMoisAnne(int Anne) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String sql="SELECT  "
				+ "    MONTH(date_course) AS mois, "
				+ "    SUM((CASE  "
				+ "        WHEN type_vehicule = 'moto' THEN 5  "
				+ "        WHEN type_vehicule = 'voiture' THEN 8  "
				+ "        ELSE 0 "
				+ "     END  "
				+ "     + (distance * 0.3)  "
				+ "     + (temps_seconde * 1.5)) * 0.9) AS chiffre_affaires "
				+ "FROM courses "
				+ "WHERE YEAR(date_course) = "+Anne
				+ "GROUP BY MONTH(date_course) "
				+ "ORDER BY mois;";
		ResultSet rs = dao.ExecuteQuery(sql);
		
		try {
			System.out.println("Reveunu de l'anné ( "+Anne+" ) : ");
			while(rs.next()) {
				System.out.println("Reveunu du mois ( "+rs.getString("mois")+" ) : "+rs.getInt("chiffre_affaires")+" €");
			}
			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void afficherRevenuePeriode(LocalDate datedébut, LocalDate datefin) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String sql="SELECT "
				+ "    SUM((CASE  "
				+ "        WHEN type_vehicule = 'moto' THEN 5  "
				+ "        WHEN type_vehicule = 'voiture' THEN 8  "
				+ "        ELSE 0 "
				+ "     END  "
				+ "     + (distance * 0.3)  "
				+ "     + (temps_seconde * 1.5)) * 0.9) AS reveunu "
				+ "FROM courses "
				+ "WHERE date_course BETWEEN "+datedébut.format(formatter)+" AND "+datefin.format(formatter)+";";
		ResultSet rs = dao.ExecuteQuery(sql);
		try {
			rs.next();
			System.out.println("Reveunu de la periode ("+datedébut.format(formatter)+" - "+datefin.format(formatter)+") : "+rs.getInt(1)+" €");
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void afficherMoyTempsCours() {
		String sql="SELECT AVG(temps_trajet) "
				+ "FROM courses;";
		ResultSet rs = dao.ExecuteQuery(sql);
		try {
			rs.next();
			System.out.println("Temps de trajet moyen : "+rs.getInt(1)+" sec ou " +rs.getInt(1)/3600F+" heur ou " +rs.getInt(1)/60F+" min");
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void afficherNPremierConducteur(int N) {
		String sql ="SELECT TOP ("+N+") "
				+ "    con.id AS id, "
				+ "    con.nom_conducteur AS nom, "
				+ "    SUM(c.distance_parcourue) AS dist "
				+ "FROM Conducteurs con "
				+ "JOIN courses c ON con.id = c.chauffeur_id "
				+ "GROUP BY con.id, con.nom_conducteur "
				+ "ORDER BY dist DESC;";
//		System.out.println(sql);
		ResultSet rs = dao.ExecuteQuery(sql);
		try {
			while(rs.next()) {
				System.out.println("ID du Conducteur : "+rs.getString("id")+" | Nom du Conducteur : "+rs.getString("nom")+" | Distance totale "+rs.getString("dist"));
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void afficherNbCourseEtDistClient(int id) {
		String sql = "SELECT "
				+ "    COUNT(*) AS nombre_courses, "
				+ "    ISNULL(SUM(distance_parcourue), 0) AS somme_distances "
				+ "FROM courses "
				+ "WHERE client_id = "+id;
//		System.out.println(sql);
		ResultSet rs = dao.ExecuteQuery(sql);
		String sqlnom = "SELECT nom_client "
				+ "FROM clients "
				+ "WHERE ID = "+id;

//		System.out.println(sqlnom);
		ResultSet nom = dao.ExecuteQuery(sqlnom);
		try {
			nom.next();
			rs.next();
			System.out.println("Client : "+nom.getString(1)+" | Nombre de course : "+rs.getString("nombre_courses")+" | Distance totale "+rs.getString("somme_distances"));
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
 	public static void afficherAncienneteMoyClients() {
		String sql="SELECT AVG(DATEDIFF(dd,  date_creation, GETDATE())) "
				+ "FROM clients;";
		ResultSet rs = dao.ExecuteQuery(sql);
		try {
			while(rs.next()) {
				System.out.println("Anciennneté Moyenne des client : "+rs.getInt(1));
		}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void afficher1erConducter() {
		String sql= "Select * "
				+"From Conducteurs "
				+"where date_ajout=("
					+"Select Min(date_ajout) "
					+"From Conducteurs"
				+")";
		//System.out.println(sql);
		ResultSet rs = dao.ExecuteQuery(sql);
		try {
			while(rs.next()) {

				afficherConducteurParID(rs.getInt(1));
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void afficherNbConducteurParTypes() {
		String rep="[ ";
		for (TypeConducteur type: TypeConducteur.values()) {
			rep = String.format(rep+"%s : %d ", type,getnbConducteurParType(type));
		}
		rep+="]";
		System.out.println(rep);
	}
	public static Integer getnbConducteurParType(TypeConducteur type) {
		String sql=String.format("Select COUNT(*)"
				+"FROM conducteurs"
				+" where type='%s'",type);
		//System.out.println(sql);
		ResultSet rs = dao.ExecuteQuery(sql);
		try {
			while(rs.next()) {
				return rs.getInt(1);
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public static void addClient(Scanner sc) {
		System.out.println("Entrer le nom du client");
		String nom=sc.nextLine();
		System.out.println("Entrer l'anné de naissance du client");
		int anne=sc.nextInt();
		System.out.println("Entrer le mois de naissance du client");
		int mois=sc.nextInt();
		System.out.println("Entrer le jour de naissance du client");
		int jour=sc.nextInt();
		LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String date= jour+"-"+mois+"-"+anne;
		String sql=String.format("Insert into clients (nom_client, date_naissance, date_creation) "
				+"Values ("
				+"'%s', ",nom);
		sql=String.format(sql+"'%s', ", date);
		sql=String.format(sql+"'%s') ", today.format(formatter));
		System.out.println(sql);
		dao.ExecuteQuery(sql);
		}
	
	public static void addConducteur(Scanner sc) {
		System.out.println("Entrer le nom du Conducteur");
		String nom=sc.nextLine();
		System.out.println("Entrer l'anné de naissance du Conducteur");
		int anne=sc.nextInt();
		System.out.println("Entrer le mois de naissance du Conducteur");
		int mois=sc.nextInt();
		System.out.println("Entrer le jour de naissance du Conducteur");
		int jour=sc.nextInt();
		String rep =sc.nextLine();
		TypeConducteur type = null;
		switch (rep) {
		case "voiture": {
			type = TypeConducteur.Voiture;
		}
		case "moto": { 
			type = TypeConducteur.Moto;
		}
		default:
			System.out.println("Unexpected value: " + rep);
		}
		LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String date= jour+"-"+mois+"-"+anne;
		String sql=String.format("Insert into clients (nom_client, date_naissance, date_creation) "
				+"Values ("
				+"'%s', ",nom);
		sql=String.format(sql+"'%s', ", date);
		sql=String.format(sql+"'%s', ", today.format(formatter));
		sql=String.format(sql+"'%s') ", type);
		System.out.println(sql);
		dao.ExecuteQuery(sql);
		}
	
	public static void afficherClients() {
		String sql="Select *"
				+"FROM clients";
		ResultSet rs = dao.ExecuteQuery(sql);
		try {
			while(rs.next()) {
				 System.out.println(rs.getString(1)+" | "+rs.getString(2)+" | "+rs.getString(3)+" | "+rs.getString(4)); 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void afficherClientParID(int id) {
		String sql=String.format("Select *"
				+"FROM clients "
				+"WHERE ID=%s",id);
		ResultSet rs = dao.ExecuteQuery(sql);
		try {
			while(rs.next()) {
				 System.out.println(rs.getString(1)+" | "+rs.getString(2)+" | "+rs.getString(3)+" | "+rs.getString(4)); 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void afficherConducteurs() {
		String sql="Select *"
				+"FROM conducteurs ";
		ResultSet rs = dao.ExecuteQuery(sql);
		try {
			while(rs.next()) {
				 System.out.println(rs.getString(1)+" | "+rs.getString(2)+" | "+rs.getString(3)+" | "+rs.getString(4)+" | "+rs.getString(5)); 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void afficherConducteurParID(int id) {
		String sql=String.format("Select *"
				+"FROM conducteurs "
				+"WHERE ID=%s",id);
		ResultSet rs = dao.ExecuteQuery(sql);
//		System.out.println(sql);
		try {
			while(rs.next()) {
				 System.out.println(rs.getString(1)+" | "+rs.getString(2)+" | "+rs.getString(3)+" | "+rs.getString(4)+" | "+rs.getString(5)); 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void suprimerClientParID(int id) {
		String sql = String.format("DELETE FROM Clients "
									+"WHERE ID = %s ", id);
		dao.ExecuteQuery(sql);
	}
	
	public static void suprimerConducteurParID(int id) {
		String sql = String.format("DELETE FROM Conducteurs "
									+"WHERE ID = %s ", id);
		dao.ExecuteQuery(sql);
	}
	
	
	public static void addCourse(Scanner sc) {
		System.out.println("Entrer l'id du Conducteur");
		int id_conducteur=sc.nextInt();
		System.out.println("Entrer l'id du Conducteur");
		int id_client=sc.nextInt();
		
		System.out.println("Entrer l'anné ");
		int anne=sc.nextInt();
		System.out.println("Entrer le mois ");
		int mois=sc.nextInt();
		System.out.println("Entrer le jour ");
		int jour=sc.nextInt();
		String date= jour+"-"+mois+"-"+anne;
		
		System.out.println("Entrer la distance parcourue ");
		int dist=sc.nextInt();
		System.out.println("Entrer le temps de trajet ");
		int temps=sc.nextInt();
		
		LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		String sql=String.format("Insert into courses (id_client, chauffeur_id , date_course, temps_trajet, distance_parcourue) "
				+"Values ("
				+"'%s', ",id_conducteur);
		sql=String.format(sql+"'%s', ", id_client);
		sql=String.format(sql+"'%s', ", date);
		sql=String.format(sql+"'%s', ", temps);
		sql=String.format(sql+"'%s') ", dist);
		System.out.println(sql);
		dao.ExecuteQuery(sql);
		}
		
	public static void afficherCoursetParID(int id) {
		String sql=String.format("Select *"
				+"FROM courses "
				+"WHERE ID=%s",id);
		ResultSet rs = dao.ExecuteQuery(sql);
		try {
			while(rs.next()) {
				 System.out.println(rs.getString(1)+" | "+rs.getString(2)+" | "+rs.getString(3)+" | "+rs.getString(4)+" | "+rs.getString(5)+" | "+rs.getString(6)); 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void afficherCourses() {
		String sql="Select *"
				+"FROM courses ";
		ResultSet rs = dao.ExecuteQuery(sql);
		try {
			while(rs.next()) {
				 System.out.println(rs.getString(1)+" | "+rs.getString(2)+" | "+rs.getString(3)+" | "+rs.getString(4)+" | "+rs.getString(5)+" | "+rs.getString(6)); 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void suprimerCourseParID(int id) {
		String sql = String.format("DELETE FROM Courses "
									+"WHERE ID = %s ", id);
		dao.ExecuteQuery(sql);
	}
	
}
