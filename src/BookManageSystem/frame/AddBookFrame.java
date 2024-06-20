package BookManageSystem.frame;

import BookManageSystem.element.Book;
import BookManageSystem.element.BookDataBaseManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
public class AddBookFrame extends JFrame {
    BookDataBaseManager manager= new BookDataBaseManager();
    JTextField nameField = new JTextField();
    JTextField isbnField = new JTextField();
    JTextField publisherField = new JTextField();
    JTextField writerField = new JTextField();
    JCheckBox borrowState = new JCheckBox("已借出");
    JTextField borrowerField = new JTextField();
    JTextField borrowDateField = new JTextField();
    GridBagConstraints constraints = new GridBagConstraints();
    public AddBookFrame() {
        this.setSize(400, 300);
        this.setLayout(new GridBagLayout());
        InitFiled();
        InitButton();
// 设置窗口关闭时不中断程序
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    public void InitFiled(){
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST; // 锚点设置为西，即左对齐// 组件水平填充
        constraints.insets = new Insets(5, 5, 5, 5); // 组件与网格线之间的间距

        // 添加组件到窗口
        constraints.gridx = 0; // 第一列
        constraints.gridy = 0; // 第一行

        constraints.gridwidth = 1; // 标签占据一列
        this.add(new JLabel("书名:"), constraints);
        constraints.gridx++;
        constraints.weightx = 1.0;
        this.add(nameField, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        this.add(new JLabel("ISBN:"), constraints);
        constraints.gridx++;
        constraints.weightx = 1.0;
        this.add(isbnField, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        this.add(new JLabel("出版社:"), constraints);
        constraints.gridx++;
        constraints.weightx = 1.0;
        this.add(publisherField, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        this.add(new JLabel("作者:"), constraints);
        constraints.gridx++;
        constraints.weightx = 1.0;
        this.add(writerField, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        constraints.weightx = 0;
        this.add(borrowState, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2; // 借书人和借书日期占两列
        this.add(new JLabel("借书人:"), constraints);
        constraints.gridx = 1;
        this.add(borrowerField, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        this.add(new JLabel("借书日期:"), constraints);
        constraints.gridx = 1;
        this.add(borrowDateField, constraints);
    }

    public void InitButton(){
        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // 按钮靠右对齐

        // 创建按钮
        JButton addButton = new JButton("添加");
        JButton cancelButton = new JButton("取消");
        // 为添加按钮添加事件监听器
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String isbn = isbnField.getText();
            String publisher = publisherField.getText();
            String writer = writerField.getText();
            boolean isBorrowed = borrowState.isSelected();
            String borrower = borrowerField.getText();
            String borrowDate = borrowDateField.getText();

            // 检查输入是否有效
            if (name.isEmpty() || isbn.isEmpty() || publisher.isEmpty() || writer.isEmpty()) {
                JOptionPane.showMessageDialog(this, "所有字段都是必填项！");
                return;
            }

            // 创建Book对象
            Book newBook = new Book(name, writer, isbn, publisher, isBorrowed, borrower, borrowDate);

            // 调用BookDataBaseManager的insert方法添加书籍
            try {
                manager.insert(newBook);
                JOptionPane.showMessageDialog(this, "书籍添加成功！");
                this.dispose(); // 关闭添加书籍的窗口
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "添加书籍失败: " + ex.getMessage());
            }
        });

// 为取消按钮添加事件监听器
        cancelButton.addActionListener(e -> this.dispose());

// 添加按钮到按钮面板
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

// 将按钮面板添加到窗口
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 3; // 按钮面板占三列
        constraints.fill = GridBagConstraints.NONE; // 不填充
        this.add(buttonPanel, constraints);

    }
}
