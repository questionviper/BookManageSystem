package BookManageSystem;

import BookManageSystem.frame.MyFrame;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        MyFrame frame=new MyFrame();
    }
}