import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class Rds {
    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    final String DB_URL = "jdbc:mysql://tweetmap.crsarl5br9bw.us-east-1.rds.amazonaws.com:3306/tweet";
    Connection conn;
    private String password = null;;
    
    private static Rds instance = null;
    private Rds() {
    	conn = null;
    }
    
    public static Rds getInstance() {
    	if (instance == null)
    		instance = new Rds();
    	return instance;
    }
    
    public boolean isConnected() {
    	return conn != null;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    	if (conn == null)
    		init();
    }
    
    public boolean isPasswordSet() {
    	return this.password != null;
    }
    
    public void init() {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, "FallMonkey", password);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    public void update(String message) {
    	//Todo: Update our database using newest data.
    }
    
    static HashMap<String, String> map = new HashMap<String, String>();

    public List<SelectResult> select(String table, String start, String end) {
        String sql = "SELECT * FROM "+table+" WHERE created_at < '"+end+"' AND created_at > '"+start+"'";
new StringBuilder();
        Statement stmt;
        int count = 0;
        List<SelectResult> list = new LinkedList<SelectResult>();
        while (true) {
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    SelectResult sr = new SelectResult(rs.getString("id_str"));

                    String text = rs.getString("text");
                    String c1 = rs.getString("coor1");
                    String c2 = rs.getString("coor2");
                    String time = rs.getString("created_at");

                    sr.setText(text);
                    sr.setCoor1(c1);
                    sr.setCoor2(c2);
                    sr.setTime(time);

                    list.add(sr);
                    count++;
                }
                rs.close();
                stmt.close();
                break;
            } catch (Exception e) {
            	System.err.println("Reconnect to database.");
            	init();
                e.printStackTrace();
            }
        }

        System.out.println("Total count of tweets:" + count);
        return list;
    }
}