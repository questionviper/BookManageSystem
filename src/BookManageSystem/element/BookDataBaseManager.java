package BookManageSystem.element;

import java.sql.*;

public class BookDataBaseManager {
    String url="jdbc:mysql://localhost:3306/BookManageSystem";
    String username="root";
    String password="jtzgys0327";
    public Connection connection =null;
    PreparedStatement preparedStatement = null;
    public BookDataBaseManager(){
        boolean success=false;
        while(!success)
        {
            try{
                connection= DriverManager.getConnection(url,username,password);
                success=true;
            }
            catch(SQLException e){
                System.out.println("连接失败");
                System.exit(0);
            }
        }
        System.out.println("连接成功！");
    }

    public void insert(Book book) throws SQLException {
        preparedStatement=connection.prepareStatement("INSERT INTO" +
                " bookmanagesystem.book (Name, IsbnNum, Publisher, Writer, BorrowStatement, Borrower, BorrowDate)\n" +
                "VALUES(?,?,?,?,?,?,?)");
        preparedStatement.setObject(1,book.Name);
        preparedStatement.setObject(2,book.IsbnNum);
        preparedStatement.setObject(3,book.Publisher);
        preparedStatement.setObject(4,book.Writer);
        preparedStatement.setObject(5,book.BorrowStatement);
        preparedStatement.setObject(6,book.Borrower);
        preparedStatement.setObject(7,book.BorrowDate);
        preparedStatement.executeUpdate();
    }

    public void delete(int id) throws SQLException {
        preparedStatement=connection.prepareStatement("DELETE FROM book WHERE Id=?");
        preparedStatement.setString(1, String.valueOf(id));
        preparedStatement.executeUpdate();
    }

    public void update(int id,String column,Object value) throws SQLException {
        preparedStatement=connection.prepareStatement("update book set " +column+"=? where Id=? ");
        preparedStatement.setObject(1,String.valueOf(value));
        preparedStatement.setString(2,String.valueOf(id));
        preparedStatement.execute();
    }
    public ResultSet searchBookByName(String bookName) throws SQLException {
        preparedStatement = connection.prepareStatement("SELECT * FROM book WHERE Name LIKE ?");
        preparedStatement.setString(1, "%" + bookName + "%"); // 使用 LIKE 进行模糊匹配
        return preparedStatement.executeQuery();
    }
}
