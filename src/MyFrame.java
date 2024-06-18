import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyFrame extends JFrame {
    DefaultTableModel model;
    DataBaseManager manager=new DataBaseManager();
    public MyFrame() throws SQLException {
        InitMenu();
        InitTable();
        InitFrame();
        this.pack();
        this.setVisible(true);
    }
    public void InitFrame() {
        // 设置窗口标题和一些基本属性
        super.setTitle("书籍管理系统");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(400, 400, 1400, 800);
        this.setLocationRelativeTo(null);

        // 添加窗口关闭时的确认对话框
        super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                int result = JOptionPane.showConfirmDialog(MyFrame.this,
                        "您确定要退出系统吗？", "退出确认",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }
    public void InitMenu(){
        JMenuBar menuBar = new JMenuBar(); // 菜单栏
        JMenu fileMenu = new JMenu("文件"); // 菜单项
        JMenuItem exitItem = new JMenuItem("退出");
        exitItem.addActionListener(e -> System.exit(0)); // 添加退出功能

        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        this.setJMenuBar(menuBar);
    }
    public void InitTable() throws SQLException {

        model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 5;
            };
        };
        String[] Columns={"Name","Isbn","Publisher","Writer","BorrowState","Borrower","BorrowDate"};
        for(String column:Columns){
            model.addColumn(column);
        }

        model.addTableModelListener(e-> {
            if(e.getType()== TableModelEvent.UPDATE){
                try {
                    int col=e.getColumn();
                    if(col==4){

                    }
                    else{
                        manager.update(e.getFirstRow()+1, model.getColumnName(e.getColumn()),model.getValueAt(e.getFirstRow(),e.getColumn()));
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "更新数据库失败: " + ex.getMessage());
                }
            }
        });
        JTable table=new JTable(model);

        //美化界面
        table.setSelectionBackground(new Color(100, 149, 237));
        table.setSelectionForeground(Color.WHITE);
        table.setRowHeight(25);

        //表头
        JTableHeader header=table.getTableHeader();
        header.setBackground(new Color(109, 100, 149));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 25));

        //列
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane=new JScrollPane(table);
        this.getContentPane().add(scrollPane);


        DataLoad();
        new Timer(1000, e->DataLoad()).start();
    }

    public void DataLoad() {
        model.setRowCount(0);
        try(ResultSet rs=manager.connection.createStatement().executeQuery("select * from book");){
            while(rs.next()){
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
        catch(SQLException e){
            System.out.println("error!!!");
            System.exit(0);
        }
    }
}
