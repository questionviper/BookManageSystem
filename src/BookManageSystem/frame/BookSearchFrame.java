package BookManageSystem.frame;

import BookManageSystem.element.BookDataBaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.sql.ResultSet;
import java.sql.SQLException;
public class BookSearchFrame extends JFrame {
    private DefaultTableModel model;

    public BookSearchFrame() {
        InitUI();
    }

    private void InitUI() {
        // 设置窗体标题和一些基本属性
        this.setTitle("查找书籍结果");
        this.setSize(600, 400);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        // 创建表格模型和表格
        model = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
            }
        };
        String[] columns = {"Name", "IsbnNum", "Publisher", "Writer", "BorrowState", "Borrower", "BorrowDate"};
        model.setColumnIdentifiers(columns);
        JTable table = new JTable(model);

        // 表头设置
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);

        // 滚动面板
        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane);

        // 确保窗体不会太小
        this.pack();
    }

    public void showBooks(ResultSet rs) throws SQLException {
        model.setRowCount(0);// 清空表格数据
        while (rs.next()) {
            model.addRow(new Object[]{
                    rs.getString("Name"),
                    rs.getString("IsbnNum"),
                    rs.getString("Publisher"),
                    rs.getString("Writer"),
                    rs.getBoolean("BorrowStatement") ? "已借出" : "未借出",
                    rs.getString("Borrower"),
                    rs.getString("BorrowDate")
            });
        }
    }
}