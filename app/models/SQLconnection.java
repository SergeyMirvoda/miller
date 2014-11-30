package models;

import play.db.ebean.Model;

import java.sql.*;


/**
 * Created by Vladimir on 30.11.2014.
 */
public class SQLconnection extends Model{

    static final String JDBC_DRIVER = "org.sqlite.JDBC";
    static final String DB_URL = "jdbc:sqlite:C:/Users/Vladimir/Downloads/play/web-app/public/sqlite/new.db";

    public static void newQuery (String[] args){
        Connection conn = null;
        Statement stmt = null;
        try{
            //get connection
            conn = DriverManager.getConnection(DB_URL);
            //query
            stmt = conn.createStatement();
            String sql;
            sql = "select a, b from first"; // new.db содержит одну таблицу в которой две строки (a, b)
            ResultSet rs = stmt.executeQuery(sql); // test.db включает в себя две таблицы - Account, LogTables
            //extract data
            while(rs.next()){
                int a = rs.getInt("a");
                String b = rs.getString("b");

                //Display values
                System.out.print("a: " + a);
                System.out.print("b: " + b);

            }
            //Clean-up
            rs.close();
            stmt.close();
            conn.close();
        }catch (SQLException se){ se.printStackTrace();
        }catch (Exception e){ e.printStackTrace();
        }finally{
            try {
                if (stmt != null) stmt.close();
            }catch (SQLException se2){
            }try{
                if (conn != null) conn.close();
            }catch (SQLException se){se.printStackTrace();
            }
        }
    }
}
