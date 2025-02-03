import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaxiDAO {
	
	private String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=TaxiEvalJava";
	private String user = "sa";
	private String pwd = "Florian_123";
	
	private Connection connection;

	public TaxiDAO() {
		try {
			this.connection = DriverManager.getConnection(dbURL,user,pwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ResultSet ExecuteQuery(String sql) {
		// TODO Auto-generated method stub
		try {
			return this.connection.createStatement().executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
