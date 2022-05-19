import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Connect {
	
	private Connection con;
	private Statement stat;
	ResultSet rs = null;


	public Connect() {
		//CONNECT DATABASE
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/tokobaju", "root", "");
			stat = con.createStatement();
		} catch (Exception e) {
			System.out.println("Connect failed");
			e.printStackTrace();
		}
	}
	
	//HELP EXECUTE
	public ResultSet executeQuery(String query) {
		try {
			rs = stat.executeQuery(query); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	//HELP UPDATE
	public boolean executeUpdate(String query) {
		try {
			stat.executeUpdate(query); 
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}