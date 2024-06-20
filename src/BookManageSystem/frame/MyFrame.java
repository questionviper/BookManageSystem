package BookManageSystem.frame;

import BookManageSystem.element.BookDataBaseManager;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MyFrame extends JFrame {
    DefaultTableModel model;
    JTable table;
    BookDataBaseManager manager=new BookDataBaseManager();
    // 在MyFrame类中声明JPopupMenu
    private JPopupMenu popupMenu;
    private final Map<Integer,Integer> Ids=new HashMap<>();

    public MyFrame() throws SQLException {
        InitMenu();
        InitTable();
        InitFrame();
        this.setVisible(true);
    }


    public void InitFrame() {
        // 设置窗口标题和一些基本属性
        this.setTitle("书籍管理系统");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(400, 400, 800, 600);
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
        JMenu fileMenu = new JMenu("菜单"); // 菜单项

        JMenuItem searchItem = new JMenuItem("查找");
        searchItem.addActionListener(e -> searchBookByName());
        fileMenu.add(searchItem);

        JMenuItem addItem = new JMenuItem("添加");
        addItem.addActionListener(e->new AddBookFrame());
        fileMenu.add(addItem);


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
            }
        };
        String[] Columns={"Name","IsbnNum","Publisher","Writer","BorrowState","Borrower","BorrowDate"};
        for(String column:Columns){
            model.addColumn(column);
        }

        model.addTableModelListener(e-> {
            if(e.getType()== TableModelEvent.UPDATE){
                try {
                    int col = e.getColumn();
                    if (col == 4) {
                        String newState = (String) model.getValueAt(e.getFirstRow(), e.getColumn());
                        if ("已借出".equals(newState)) {
                            // 用户选择 "已借出"
                            String borrowerName = JOptionPane.showInputDialog(MyFrame.this,
                                    "请输入借书人的名字:", "借书人输入");
                            if (borrowerName != null && !borrowerName.isEmpty()) {
                                // 更新数据库
                                manager.update(Ids.get(e.getFirstRow()), "Borrower", borrowerName);
                                manager.update(Ids.get(e.getFirstRow()), "BorrowStatement", 1);
                            }
                        } else {
                            // 用户选择 "未借出"
                            manager.update(Ids.get(e.getFirstRow()), "Borrower", " ");
                            manager.update(Ids.get(e.getFirstRow()), "BorrowStatement", 0);
                        }
                    }
                    else {
                        manager.update(Ids.get(e.getFirstRow()), model.getColumnName(e.getColumn()), model.getValueAt(e.getFirstRow(), e.getColumn()));
                    }
                }catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "更新数据库失败: " + ex.getMessage());
                }
            }
        });
        table=new JTable(model);


        popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("删除");
        deleteItem.addActionListener(e -> {
            try {
                deleteSelectedBook();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "删除失败： " + ex.getMessage());
            }
        });
        popupMenu.add(deleteItem);
        // 在InitTable方法中为table添加鼠标事件，删除功能
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
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
        new Timer(5000, e->DataLoad()).start();
    }

    private void deleteSelectedBook() throws SQLException {
            int row = table.getSelectedRow();
            if (row != -1) {
                if(table.getValueAt(row,4).equals("已借出")) {
                    JOptionPane.showMessageDialog(this,"该书已借出，无法删除");
                }
                else{
                    manager.delete(Ids.get(row));
                    JOptionPane.showMessageDialog(this,"删除成功");
                }

            }
    }

    public void DataLoad() {
        Ids.clear();
        model.setRowCount(0);
        int i=0;
        try(ResultSet rs=manager.connection.createStatement().executeQuery("select * from book")){
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
                Ids.put(i,rs.getInt("Id") );
                i++;
            }
        }
        catch(SQLException e){
            System.out.println("error!!!");
            System.exit(0);
        }
    }

    // 修改searchBookByName方法
    private void searchBookByName() {
        String bookName = JOptionPane.showInputDialog(this, "请输入书名进行查找:", "查找书籍");
        if (bookName != null && !bookName.isEmpty()) {
            try {
                ResultSet rs = manager.searchBookByName(bookName);
                BookSearchFrame bookSearchFrame = new BookSearchFrame();
                bookSearchFrame.showBooks(rs); // 展示查找结果
                bookSearchFrame.setVisible(true); // 显示窗体
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "查找书籍失败: " + ex.getMessage());
            }
        }
    }
    }

