package BS;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class BookInfo implements Serializable {
	String b_isbn; // �ڵ�
	String b_title; // å����
	String b_author; // ����
	String b_genre; // �帣
	String b_price; // ����
	String b_publish; // ���ǻ�
	String b_like; // �α⵵
	String b_havenum; // ���� ��������
	String b_soldnum; // �Ǹż��� 

	BookInfo(String b_isbn, String b_title, String b_author, String b_genre,
			String b_price, String b_publish, String b_like,String b_havenum, String b_soldnum) {
		this.b_isbn = b_isbn;
		this.b_title = b_title;
		this.b_author = b_author;
		this.b_genre = b_genre;
		this.b_price = b_price;
		this.b_publish = b_publish;
		this.b_like = b_like;
		this.b_havenum = b_havenum;
		this.b_soldnum = b_soldnum;
	}
}


class Insert {
	String isbn, title, author, genre, price,
			publish, like, havenum, soldnum;

	Insert() {
	}

	void inserting(JTextField t_isbn, JTextField t_title, JTextField t_author,
			JTextField t_genre, JTextField t_price, 
			JTextField t_publish, JTextField t_like, JTextField t_havenum,
			JTextField t_soldnum, Object data[],
			DefaultTableModel tableModel, ArrayList<BookInfo> list) {
		isbn = t_isbn.getText();
		title = t_title.getText();
		author = t_author.getText();
		genre = t_genre.getText();
		price = t_price.getText();
		publish = t_publish.getText();
		like = t_like.getText();
		havenum = t_havenum.getText();
		soldnum = t_soldnum.getText();

		// �ڵ� �ʵ尡 �����϶��� �Է� �� �� ����
		if (!isbn.equals("")) {
			// �ߺ��� �ڵ尡 �������� �ʴ´ٸ�
			if (new Search(isbn, list).searchData() < 0) {
				// ����Ʈ�� �����͸� �߰���Ŵ
				list.add(new BookInfo(isbn, title, author, genre, price, 
						publish, like,havenum,soldnum));

				// ���̺��� ������ �࿡ �����͸� �߰���Ŵ
				BookInfo info = list.get(list.size() - 1);
				data[0] = info.b_isbn;
				data[1] = info.b_title;
				data[2] = info.b_author;
				data[3] = info.b_genre;
				data[4] = info.b_price;
				data[5] = info.b_publish;
				data[6] = info.b_like;
				data[7] = info.b_havenum;
				data[8] = info.b_soldnum;

				tableModel.addRow(data);
				System.out.printf(
						">>>insert data: %s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
						data[0], data[1], data[2], data[3], data[4], data[5],
						data[6], data[7],data[8]);

				// ������ �߰��� ���� �� �ؽ�Ʈ �ʵ带 �ʱ�ȭ
				t_isbn.setText("");
				t_title.setText("");
				t_author.setText("");
				t_genre.setText("");
				t_price.setText("");
				t_publish.setText("");
				t_like.setText("");
				t_havenum.setText("");
				t_soldnum.setText("");

			} else {
				System.err.println(">>>duplication isbn");
				JOptionPane.showMessageDialog(null, "�ߺ��� �ڵ尡 �����մϴ�.");
			}
		} else {
			System.err.println(">>>not exist isbn");
			JOptionPane.showMessageDialog(null, "�ڵ尡 �Էµ��� �ʾҽ��ϴ�.");
		}

	}

}

class Delete {
	Delete() {
	}

	void deleting(int row, JTextField t_isbn, DefaultTableModel tableModel,
			ArrayList<BookInfo> list) {
		// ������ Ȯ���� ���� �κ�
		BookInfo temp = list.get(row);
		System.out.printf(">>>delete data: %s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
				temp.b_isbn, temp.b_title, temp.b_author, temp.b_genre,
				temp.b_price, temp.b_publish, temp.b_like, temp.b_havenum, temp.b_soldnum);
		String isbn = t_isbn.getText();
		// ����Ʈ ���� �ڵ��� ���� ������ �˻��ϰ� �ε����� ��ȯ
		int s_row = new Search(isbn, list).searchData();
		if (s_row != -1) {
			tableModel.removeRow(s_row);
			list.remove(row);
		} else {
			System.out.println(">>> delete error");
			JOptionPane.showMessageDialog(null, "����Ʈ�� �ڵ尡 �������� �ʽ��ϴ�.");
		}
	}
}

class Update {
	String isbn, title, author, genre, price, publish, like, havenum, soldnum; // ���ڿ��� ������ ����														
	JTextField t_isbn, t_title, t_author, t_genre, t_price, t_publish,
			t_like, t_havenum, t_soldnum; // �ؽ�Ʈ �ʵ�
	DefaultTableModel tableModel;
	ArrayList<BookInfo> list;
	Object data[];
	int l_row, row; // l_row: ����Ʈ�� �ε��� ��, row: ���̺��� �ε��� ��

	Update() {
	}

	public void updating(int l_row, int row, String temp_isbn, JTextField t_isbn,
			JTextField t_title, JTextField t_author, JTextField t_genre,
			JTextField t_price, JTextField t_publish,
			JTextField t_like, JTextField t_havenum, JTextField t_soldnum,
			Object data[], DefaultTableModel tableModel,
			ArrayList<BookInfo> list) {
		this.tableModel = tableModel;
		this.list = list;
		this.data = data;
		this.l_row = l_row;
		this.row = row;
		this.t_isbn = t_isbn;
		this.t_title = t_title;
		this.t_author = t_author;
		this.t_genre = t_genre;
		this.t_price = t_price;
		this.t_publish = t_publish;
		this.t_like = t_like;
		this.t_havenum = t_havenum;
		this.t_soldnum = t_soldnum;

		isbn = t_isbn.getText();
		title = t_title.getText();
		author = t_author.getText();
		genre = t_genre.getText();
		price = t_price.getText();
		publish = t_publish.getText();
		like = t_like.getText();
		havenum = t_havenum.getText();
		soldnum = t_soldnum.getText();

		// �ؽ�Ʈ �ʵ��� ���� ��ȭ�� ���ٸ� �״�� ����
		if (temp_isbn.equals(isbn)) {
			update();
		} // �ؽ�Ʈ �ʵ��� ���� ��ȭ�ƴٸ� �ߺ�üũ
		else {
			// �ڵ尪 ������ ����Ʈ�� �ߺ��� �ڵ尡 �ִ����� üũ
			if (new Search(t_isbn.getText(), list).searchData() < 0) {
				update();
			} else {
				System.err.println(">>>duplication isbn");
				JOptionPane.showMessageDialog(null, "�ߺ��� �ڵ尡 �����մϴ�");
			}
		}

	}

	public void update() {
		// ����Ʈ���� row ��ġ�� �����͸� ����
		list.set(l_row, new BookInfo(isbn, title, author, genre, price, 
				publish, like, havenum, soldnum));

		// ���̺��� row ��ġ�� ���ο� ���� �߰���Ű��
		// �ڷ� �з��� ������ ���� ������Ŵ
		data[0] = isbn;
		data[1] = title;
		data[2] = author;
		data[3] = genre;
		data[4] = price;
		data[5] = publish;
		data[6] = like;
		data[7] = havenum;
		data[8] = soldnum;

		tableModel.insertRow(row, data);
		tableModel.removeRow(row + 1);

		// ������ ���� �� �ؽ�Ʈ �ʵ带 �ʱ�ȭ��Ŵ
		t_isbn.setText("");
		t_title.setText("");
		t_author.setText("");
		t_genre.setText("");
		t_price.setText("");
		t_publish.setText("");
		t_like.setText("");
		t_havenum.setText("");
		t_soldnum.setText("");
	}

}




class Search {
	
	String isbn, title, author, genre, price, publish, like, havenum, soldnum; 
	// �迭 �� �����͸� ������ ����
	String s_isbn, s_title, s_author, s_genre, s_price, s_publish,
			s_like, s_havenum, s_soldnum; // �ؽ�Ʈ �ʵ� ���� ������ ����

	String temp_isbn; // �˻��ϴµ� Ű ������ ���� �ڵ�
	ArrayList<BookInfo> list;

	Search(String temp_isbn, ArrayList<BookInfo> list) {
		this.temp_isbn = temp_isbn;
		this.list = list;
	}

	Search(JTextField t_isbn, JTextField t_title, JTextField t_author,
			JTextField t_genre, JTextField t_price, 
			JTextField t_publish, JTextField t_like, 
			JTextField t_havenum, JTextField t_soldnum,
			Object data[], DefaultTableModel tableModel, ArrayList<BookInfo> list) {
		s_isbn = t_isbn.getText();
		s_title = t_title.getText();
		s_author = t_author.getText();
		s_genre = t_genre.getText();
		s_price = t_price.getText();
		s_publish = t_publish.getText();
		s_like = t_like.getText();
		s_havenum = t_havenum.getText();
		s_soldnum = t_soldnum.getText();

		// �˻��� ������ ����� ���̺��� �ʱ�ȭ ��Ŵ
		try {
			for (int i = 0; i < list.size(); i++) {
				tableModel.removeRow(0);
			}
		} catch (Exception e) {
		}

		// ����Ʈ�� ���鼭 �����͸� �˻�
		for (int i = 0; i < list.size(); i++) {
			BookInfo info = list.get(i);
			isbn = info.b_isbn;
			title = info.b_title;
			author = info.b_author;
			genre = info.b_genre;
			price = info.b_price;
			publish = info.b_publish;
			like = info.b_like;
			havenum = info.b_havenum;
			soldnum = info.b_soldnum;

			// indexOf > �ش� ���ڿ��� �����Ѵٸ� ��ġ, �� �̿� -1 ����
			// �ؽ�Ʈ �ʵ尡 ������� ��� 0�� ������
			if (isbn.indexOf(s_isbn) != -1 && title.indexOf(s_title) != -1
					&& author.indexOf(s_author) != -1
					&& genre.indexOf(s_genre) != -1
					&& price.indexOf(s_price) != -1
					&& publish.indexOf(s_publish) != -1
					&& like.indexOf(s_like) != -1
					&& havenum.indexOf(s_havenum) != -1
					&& soldnum.indexOf(s_soldnum) != -1 ) {
				data[0] = isbn;
				data[1] = title;
				data[2] = author;
				data[3] = genre;
				data[4] = price;
				data[5] = publish;
				data[6] = like;
				data[7] = havenum;
				data[8] = soldnum;
				// ���̺� ���ǹ��� �����ϴ� �����͸� �߰�
				tableModel.addRow(data);

			}
		}
	}

	// �迭�� ��ȸ�ϸ鼭 �ڵ��� ���翩�� �˻�
	public int searchData() {
		// ����Ʈ�� �����ϰ� ���� �� �˻�
		if (list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				BookInfo info = list.get(i);
				if (info.b_isbn.equals(temp_isbn)) {
					// ����Ʈ�� �ߺ��� �ڵ尡 �ִٸ�
					return i;
				}
			}
			// ����Ʈ�� �ߺ��� �ڵ尡 ���ٸ�
			return -1;

		}
		// ����Ʈ�� �������� �ʴ´ٸ�
		return -1;

	}

}

class Save {
	String isbn, title, author, genre, price, publish, like, havenum, soldnum;
	BufferedOutputStream bos;
	ObjectOutputStream out;

	Save() {}

	void saving(JTextField t_isbn, JTextField t_title, JTextField t_author,
			JTextField t_genre, JTextField t_price,
			JTextField t_publish, JTextField t_like, 
			JTextField t_havenum, JTextField t_soldnum, 
			Object data[], DefaultTableModel tableModel, ArrayList<BookInfo> list) {
		try {
			bos = new BufferedOutputStream(new FileOutputStream("book.dat"));
			out = new ObjectOutputStream(bos);

			// ����Ʈ ��ü�� ���鼭
			for (int i = 0; i < list.size(); i++) {
				BookInfo info = list.get(i);
				isbn = info.b_isbn;
				title = info.b_title;
				author = info.b_author;
				genre = info.b_genre;
				price = info.b_price;
				publish = info.b_publish;
				like = info.b_like;
				havenum = info.b_havenum;
				soldnum = info.b_soldnum;
				// BookInfo ��ü�� ������

				out.writeObject(new BookInfo(isbn, title, author, genre, price, publish, like, havenum, soldnum));
				System.out.printf(">>>save data: %s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n ", isbn,
						title, author, genre, price, publish, like, havenum, soldnum);
			}
		} catch (FileNotFoundException ffeo) {
			System.err.println("not found file");
		} catch (IOException ioe) {
			System.err.println("not read file");
		} finally {
			try {
				out.close();
			} catch (Exception e) {

			}
		}

	}

}

class Load {
	String isbn, title, author, genre, price, publish, like, havenum, soldnum;
	FileInputStream fis;
	ObjectInputStream in;
	BookInfo info;

	Load() {
	}

	void loading(JTextField t_isbn, JTextField t_title, JTextField t_author,
			JTextField t_genre, JTextField t_price, 
			JTextField t_publish, JTextField t_like,
			JTextField t_havenum, JTextField t_soldnum,
			Object data[], DefaultTableModel tableModel, ArrayList<BookInfo> list) {
		try {
			fis = new FileInputStream("book.dat");
			in = new ObjectInputStream(fis);

			while (true) {
				info = (BookInfo) in.readObject();
				// �о���� ��ü�� null �̶�� �ݺ��� ����
				if (info == null)
					break;
				// �о���� ��ü�� ����Ʈ�� �߰���Ŵ
				list.add(info);
				data[0] = info.b_isbn;
				data[1] = info.b_title;
				data[2] = info.b_author;
				data[3] = info.b_genre;
				data[4] = info.b_price;
				data[5] = info.b_publish;
				data[6] = info.b_like;
				data[7] = info.b_havenum;
				data[8] = info.b_soldnum;

				// �о���� ��ü �����͸� Object �迭�� ������ ���̺� �߰���
				tableModel.addRow(data);
				System.out.printf(">>>load data: %s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n ",
						data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]);
			}

		} catch (FileNotFoundException ffe) {
			System.err.println("not found file");

		} catch (ClassNotFoundException e) {
			System.err.println("not found class");
		} catch (IOException iod) {
			System.out.println(">>>file load fin.");
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}
	}

}

public class MainForm extends JFrame implements ActionListener, FocusListener {

	String titles[] = { "isbn", "����", "����", "�帣", "����", "���ǻ�", "�α⵵", "�������", "�Ǹż���" };
	Object data[] = new Object[9];
	JTable table;
	DefaultTableModel tableModel;
	JScrollPane sp;
	ArrayList<BookInfo> list = new ArrayList<BookInfo>();
	JButton b_insert, b_delete, b_update, b_save, b_load, b_search, b_clear,
			b_exit, b_item, b_top5; 
	JTextField t_isbn, t_title, t_author, t_genre, t_price, t_publish, t_like, t_havenum, t_soldnum;
	JPanel panel; // ������ ���� ������Ʈ�� ��ġ��ų �г�

	LoginView loginview;
	
	
	MainForm() {
		super("�������� ���α׷�");
		setLocation(150, 10); // ������ ��ġ ����
		
		setSize(1200, 700); // ������ ũ�� ����

		// JTable
		tableModel = new DefaultTableModel(titles, 0);
		table = new JTable(tableModel);
		
		sp = new JScrollPane(table);
		sp.setPreferredSize(new Dimension(600,600));
		add(sp, BorderLayout.CENTER);

		// �Է� ������Ʈ panel
		panel = new JPanel();
		panel.setLayout(new GridLayout(13,2));
		
		panel.add(new JLabel("isbn"));
		panel.add(t_isbn = new JTextField(10));
		
		
		panel.add(new JLabel("����"));
		panel.add(t_title = new JTextField(10));
		
		
		panel.add(new JLabel("����"));
		panel.add(t_author = new JTextField(10));
		
		
		panel.add(new JLabel("�帣"));
		panel.add(t_genre = new JTextField(10));
		
		
		panel.add(new JLabel("����"));
		panel.add(t_price = new JTextField(10));
		
		panel.add(new JLabel("���ǻ�"));
		panel.add(t_publish = new JTextField(10));
	
		panel.add(new JLabel("�α⵵"));
		panel.add(t_like = new JTextField(10));
		
		
		panel.add(new JLabel("�������"));
		panel.add(t_havenum = new JTextField(10));
		
		panel.add(new JLabel("�Ǹż���"));
		panel.add(t_soldnum = new JTextField(10));
		
		add(panel, BorderLayout.EAST);
		
		// ��ư�� ����� 
		panel.add(new JLabel("< Button >"));
		panel.add(b_insert = new JButton("����"));
		panel.add(b_delete = new JButton("����"));
		
		panel.add(b_update = new JButton("����"));
		panel.add(b_search = new JButton("�˻�"));
		panel.add(b_clear = new JButton("�ʵ��ʱ�ȭ"));
		panel.add(b_item = new JButton("������"));
		panel.add(b_top5 = new JButton("����top5"));

		// ������ ���� �̺�Ʈ
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// ���̺� Ŭ�� �̺�Ʈ
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Select();
			}
		});
		
		

		// ��ư �̺�Ʈ ������
		b_insert.addActionListener(this);
		b_delete.addActionListener(this);
		b_update.addActionListener(this);
		b_clear.addActionListener(this);
		b_search.addActionListener(this);
		b_item.addActionListener(this);
		b_top5.addActionListener(this);
		

		// �ؽ�Ʈ �ʵ� ��Ŀ�� ������
		t_isbn.addFocusListener(this);
		t_title.addFocusListener(this);
		t_author.addFocusListener(this);
		t_genre.addFocusListener(this);
		t_price.addFocusListener(this);
		t_publish.addFocusListener(this);
		t_like.addFocusListener(this);
		t_havenum.addFocusListener(this);
		t_soldnum.addFocusListener(this);
		createMenu();	
		setVisible(true);

	}

	
	// �޴��� ����� 
	public void createMenu(){
		JMenuBar mb = new JMenuBar();
		//�޴� ����
		JMenu fileMenu = new JMenu("File");
		
		//�޴������� & �׼Ǹ����� ���� 
		JMenuItem load = new JMenuItem("Load");
		load.addActionListener(new MenuActionListener());
		
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new MenuActionListener());
		
		JMenu systemMenu = new JMenu("System");
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new MenuActionListener());
		
		fileMenu.add(load);
		fileMenu.addSeparator();
		fileMenu.addSeparator();
		fileMenu.addSeparator();
		fileMenu.addSeparator();
		fileMenu.addSeparator();
		fileMenu.addSeparator();
		fileMenu.addSeparator();
		fileMenu.addSeparator();
		fileMenu.addSeparator();
		fileMenu.addSeparator();
		fileMenu.add(save);
		
		systemMenu.add(exit);
		
		mb.add(fileMenu);
		mb.add(systemMenu);
		this.setJMenuBar(mb);
		
	}
	
	


	// ���̺� Ŭ���� �ش� ���� ���� �ؽ�Ʈ �ʵ忡 ����
	public void Select() {
		int row = table.getSelectedRow();
		System.out.println(">>>click position: " + row);
		// ���̺��� (row, ?) ��ġ�� �����͸� �ؽ�Ʈ �ʵ忡 ����
		t_isbn.setText((String) tableModel.getValueAt(row, 0));
		t_title.setText((String) tableModel.getValueAt(row, 1));
		t_author.setText((String) tableModel.getValueAt(row, 2));
		t_genre.setText((String) tableModel.getValueAt(row, 3));
		t_price.setText((String) tableModel.getValueAt(row, 4));
		t_publish.setText((String) tableModel.getValueAt(row, 5));
		t_like.setText((String) tableModel.getValueAt(row, 6));
		t_havenum.setText((String) tableModel.getValueAt(row, 7));
		t_soldnum.setText((String) tableModel.getValueAt(row, 8));
		
		
		System.out.println(">>>get data: " + tableModel.getValueAt(row, 0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// ���õ� ���̺��� ����ġ�� ������, ���õ��� �ʾ��� ��� -1 ����
		int row = table.getSelectedRow();

		// ���� ��ư
		if (e.getSource() == b_insert) {
			System.out.println(">>>click insert");
			Insert insert = new Insert();
			insert.inserting(t_isbn, t_title, t_author, t_genre, t_price, 
					t_publish, t_like, t_havenum, t_soldnum, data,
					tableModel, list);
		}
		// ���� ��ư
		else if (e.getSource() == b_delete) {
			System.out.println(">>> click delete");
			Delete delete = new Delete();
			if (row == -1)
				return; // ���� ���õ��� �ʾҴٸ� �н�
			BookInfo temp = list.get(row);
			System.out.printf(">>>delete data: %s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
					temp.b_isbn, temp.b_title, temp.b_author, temp.b_genre,
					temp.b_price, temp.b_publish, temp.b_havenum, temp.b_soldnum);
			delete.deleting(row, t_isbn, tableModel, list);

		}
		// ������ư
		else if (e.getSource() == b_update) {
			System.out.println(">>> click update");
			if (row == -1)
				return; // ���� ���õ��� �ʾҴٸ� �н�
			// �ؽ�Ʈ �ʵ� ������ �����ͷ� ����Ʈ ��ġ �˻�
			String temp_isbn = (String) tableModel.getValueAt(row, 0);
			// ����Ʈ �� ������ ���濡 ����ϱ� ���ؼ� ���� ����
			int l_row = new Search(temp_isbn, list).searchData();
			BookInfo temp = list.get(l_row); // ������ Ȯ���� ���� �κ�
			System.out.println(">>> update isbn: " + temp.b_isbn + " -> "
					+ t_isbn.getText());
			Update update = new Update();
			update.updating(l_row, row, temp_isbn, t_isbn, t_title, t_author,
					t_genre, t_price, t_publish, t_like, t_havenum, t_soldnum,data, tableModel, list);

		}
		
		// �ؽ�Ʈ �ʵ� �ʱ�ȭ
		else if (e.getSource() == b_clear) {
			System.out.println(">>> click clear");
			t_isbn.setText("");
			t_title.setText("");
			t_author.setText("");
			t_genre.setText("");
			t_price.setText("");
			t_publish.setText("");
			t_like.setText("");
			t_havenum.setText("");
			t_soldnum.setText("");
		}
		// �˻� ��ư ( �ؽ�Ʈ �ʵ尡 ������� ���� ��ü ���)
		else if (e.getSource() == b_search) {
			System.out.println(">>>click search");
			new Search(t_isbn, t_title, t_author, t_genre, t_price, 
					t_publish, t_like, t_havenum, t_soldnum, data,
					tableModel, list);
		}
		else if(e.getSource() == b_item){
			System.out.println(">>>click item manage");
			managing();
		
		}
		else if(e.getSource() == b_top5){
			System.out.println(">>>click show top5 ");
			showtop5();
		}

	}
	
	
	
	
	
	public void showtop5(){
		final JFrame showtop5fr = new JFrame();
		showtop5fr.setTitle("�츮���� ���� TOP 5 Ȯ���ϱ�");
		showtop5fr.setSize(600, 300);

		showtop5fr.setLayout(new GridLayout(5,2));
		
		int lsize = list.size(); //list�� ���̸� �迭ũ��� ���ϱ� ����
		int data2[] = new int[lsize]; //list���� ũ���� �迭 data2�� ���� 
		
		for(int i=0;i<list.size();i++){
			int h = Integer.parseInt(list.get(i).b_soldnum); // �� å�� �Ǹŷ��� int�� ������ ����ǰ�
			data2[i] = h; // �װ� data2�� ���� 
		}
		
		
		int last = lsize-1;
		
		for(int i=last;i>0;i--){
			boolean sorted = true;
			for(int n=0;n<i;n++){
				if(data2[n] > data2[n+1]){
					int temp = data2[n+1];
					data2[n+1] = data2[n];
					data2[n] = temp;
					sorted = false;
				}
			}
		}
		
		//���� 1~5���� å�� �˱����� å �̸� ���� ����
		String sold_1 = null;
		String sold_2 = null;
		String sold_3 = null;
		String sold_4 = null; // sold_1~5�� å �̸� ���� ����
		String sold_5 = null;
		
		//���� 1~5���� å�� �Ǽ��� �����ֱ����� int->String ����ȯ�ؼ� ������ ���� 
		String sold1 = Integer.toString(data2[lsize-1]); // sold1~5 �� å�Ǽ���
		String sold2 = Integer.toString(data2[lsize-2]);
		String sold3 = Integer.toString(data2[lsize-3]);
		String sold4 = Integer.toString(data2[lsize-4]);
		String sold5 = Integer.toString(data2[lsize-5]);
		
		for(int i=0;i<list.size();i++){
			if(sold1.equals(list.get(i).b_soldnum)){
				sold_1 = list.get(i).b_title;
			}
			else if(sold2.equals(list.get(i).b_soldnum)){
				sold_2 = list.get(i).b_title;
			}
			else if(sold3.equals(list.get(i).b_soldnum)){
				sold_3 = list.get(i).b_title;
			}
			else if(sold4.equals(list.get(i).b_soldnum)){
				sold_4 = list.get(i).b_title;
			}
			else if(sold5.equals(list.get(i).b_soldnum)){
				sold_5 = list.get(i).b_title;
			}
		}
		
		
		showtop5fr.add(new JLabel("1��"));
		showtop5fr.add(new JLabel(sold_1+"("+data2[lsize-1]+"��)"));
		showtop5fr.add(new JLabel("2��"));
		showtop5fr.add(new JLabel(sold_2+"("+data2[lsize-2]+"��)"));
		showtop5fr.add(new JLabel("3��"));
		showtop5fr.add(new JLabel(sold_3+"("+data2[lsize-3]+"��)"));
		showtop5fr.add(new JLabel("4��"));
		showtop5fr.add(new JLabel(sold_4+"("+data2[lsize-4]+"��)"));
		showtop5fr.add(new JLabel("5��"));
		showtop5fr.add(new JLabel(sold_5+"("+data2[lsize-5]+"��)"));
		
		showtop5fr.setVisible(true);
		
		
	}
	
	
	
	public void managing(){
		final JFrame managefr = new JFrame();
		managefr.setTitle("������");
		managefr.setSize(300, 200);

		managefr.setLayout(null);
		
		JLabel select = new JLabel("�޴� ����");
		String[] arr = {"�԰�","�Ǹ�","��ǰ"};
		final JComboBox box1 = new JComboBox(arr);
		select.setBounds(20, 40, 100, 20);
		box1.setBounds(150, 40, 120, 20);
		managefr.add(select);
		managefr.add(box1);
		
		
		
		ArrayList<String> list2 = new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			list2.add(list.get(i).b_title);
		}
		
		JLabel select2 = new JLabel("�ش� ���� ����");
		final JComboBox box2 = new JComboBox(list2.toArray());
		select2.setBounds(20, 10, 100, 20);
		box2.setBounds(150, 10, 120, 20);
		managefr.add(select2);
		managefr.add(box2);

		JLabel number = new JLabel("�Ǽ� �Է� ");
		number.setBounds(20, 70, 100, 20);
		final JTextField field = new JTextField();
		field.setBounds(150,70,120,20);
		managefr.add(number);
		managefr.add(field);
		
		JButton okbtn = new JButton("Ȯ��");
		okbtn.setBounds(120,100,60,30);
		managefr.add(okbtn);
		
		
		okbtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int index = box2.getSelectedIndex(); 
				
				String isbn = list.get(index).b_isbn;
				String title = list.get(index).b_title;
				String author = list.get(index).b_author;
				String genre = list.get(index).b_genre;
				String price = list.get(index).b_price;
				String publish = list.get(index).b_publish;
				String like = list.get(index).b_like;
				String havenum = list.get(index).b_havenum;
				String soldnum =  list.get(index).b_soldnum;
				
				
				
				String ch_menu = (String) box1.getSelectedItem(); 
				String value = field.getText();
				if(index == -1){
					System.out.println("�ùٸ��� ���� ���� �Դϴ�.");
				}
				else if(value.equals("")){
					System.out.println("�Է����� �����̽��ϴ�.");
				}
				else{
					
					int value_ = Integer.parseInt(value);
				
					
					if(ch_menu.equals("�԰�")){
						
						int havenum1 = Integer.parseInt(havenum);
						
						havenum1 += value_;
						
						havenum = Integer.toString(havenum1);
						
						managefr.setVisible(false);
					}
					
					else if(ch_menu.equals("�Ǹ�")){
						
						int havenum1 = Integer.parseInt(havenum);
						int soldnum1 = Integer.parseInt(soldnum);
						
						havenum1 -= value_;
						soldnum1 += value_;
						
						havenum = Integer.toString(havenum1);
						soldnum = Integer.toString(soldnum1);
						
						managefr.setVisible(false);
					}

					else if(ch_menu.equals("��ǰ")){
						
						int havenum1 = Integer.parseInt(havenum);
						
						havenum1 -= value_;
						
						havenum = Integer.toString(havenum1);
						
						managefr.setVisible(false);
					}
					
					list.set(index, new BookInfo(isbn, title, author, genre, price, 
							publish, like, havenum, soldnum));
					
					data[0] = isbn;
					data[1] = title;
					data[2] = author;
					data[3] = genre;
					data[4] = price;
					data[5] = publish;
					data[6] = like;
					data[7] = havenum;
					data[8] = soldnum;

					tableModel.insertRow(index, data);
					tableModel.removeRow(index + 1);
					
					
					
					
					
				}
			}
		});
		managefr.setVisible(true);
		
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		// �ؽ�Ʈ �ʵ尡 ���õǾ��� �� �ش� �ؽ�Ʈ �ʵ��� ���� ����
		if (e.getSource() == t_isbn)
			t_isbn.setBackground(new Color(248,255,255));
		else if (e.getSource() == t_title)
			t_title.setBackground(new Color(248,255,255));
		else if (e.getSource() == t_author)
			t_author.setBackground(new Color(248,255,255));
		else if (e.getSource() == t_genre)
			t_genre.setBackground(new Color(248,255,255));
		else if (e.getSource() == t_price)
			t_price.setBackground(new Color(248,255,255));
		else if (e.getSource() == t_publish)
			t_publish.setBackground(new Color(248,255,255));
		else if (e.getSource() == t_like)
			t_like.setBackground(new Color(248,255,255));
		else if (e.getSource() == t_havenum)
			t_havenum.setBackground(new Color(248,255,255));
		else if (e.getSource() == t_soldnum)
			t_soldnum.setBackground(new Color(248,255,255));
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		// �ؽ�Ʈ �ʵ��� ������ �����Ǿ��� �� ���� ����
		if (e.getSource() == t_isbn)
			t_isbn.setBackground(null);
		else if (e.getSource() == t_title)
			t_title.setBackground(null);
		else if (e.getSource() == t_author)
			t_author.setBackground(null);
		else if (e.getSource() == t_genre)
			t_genre.setBackground(null);
		else if (e.getSource() == t_price)
			t_price.setBackground(null);
		else if (e.getSource() == t_publish)
			t_publish.setBackground(null);
		else if (e.getSource() == t_like)
			t_like.setBackground(null);
		else if (e.getSource() == t_havenum)
			t_havenum.setBackground(null);
		else if (e.getSource() == t_soldnum)
			t_soldnum.setBackground(null);

	}
	
	class MenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			String cmd = e.getActionCommand();
			if(cmd.equals("Load")){
				System.out.println(">>> click load");
				Load load = new Load();
				load.loading(t_isbn, t_title, t_author, t_genre, t_price, t_publish, t_like,
						t_havenum, t_soldnum, data, tableModel, list);
			}
			else if(cmd.equals("Save")){
				System.out.println(">>> click save");
				Save save = new Save();
				save.saving(t_isbn, t_title, t_author, t_genre, t_price, 
						t_publish, t_like, t_havenum, t_soldnum, data,
						tableModel, list);
			}
			else if(cmd.equals("Exit")){
				System.out.println(">>>program exit");
				System.exit(0);
			}
		}
	}
	
	

	
	static class LoginView extends JFrame{
		private MainForm main;
	
		private JButton btnLogin;
		private JButton btnInit;
		private JPasswordField passText;
		private JTextField userText;
		private boolean bLoginCheck;
		
		
		
		public LoginView(){
			
			setTitle("login");
			setSize(280,150);
			setResizable(false);
			setLocation(800,450);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			
			//panel
			JPanel panel = new JPanel();
			placeLoginPanel(panel);
			
			//add
			add(panel);
			setVisible(true);
		}
			
			public void placeLoginPanel(JPanel panel){
				panel.setLayout(null);
				JLabel userLabel = new JLabel("UserID");
				userLabel.setBounds(10,10,80,25);
				panel.add(userLabel);
				
				JLabel passLabel = new JLabel("Password");
				passLabel.setBounds(10, 40, 80, 25);
				panel.add(passLabel);
				
				userText = new JTextField(20);
				userText.setBounds(100, 10, 160, 25);
				panel.add(userText);
				
				passText = new JPasswordField(20);
				passText.setBounds(100, 40, 160, 25);
				panel.add(passText);
				passText.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						isLoginCheck();
					}
				});
				
				btnLogin = new JButton("Login");
				btnLogin.setBounds(160,80,100,25);
				panel.add(btnLogin);
				btnLogin.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e){
						isLoginCheck();
					}
				});
				
			}
			
			public void isLoginCheck(){
				if(userText.getText().equals("manager") && new String(passText.getPassword()).equals("1234")){
					JOptionPane.showMessageDialog(null, "Success");
					bLoginCheck = true;
					
					//�α��� �����̶�� �Ŵ���â ���� 
					
					if(isLogin()){
						main.showFrameTest(); //����â �޼ҵ带 �̿��� â����
						
					}}else{
						JOptionPane.showMessageDialog(null, "Failed");}
					}
			
			public void setMain(MainForm main){
				this.main = main;
		
			}
			
			public boolean isLogin(){
				return bLoginCheck;
			}
			}
	
	

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainForm main = new MainForm();
		main.dispose();
		main.loginview = new LoginView(); //�α��� â ���̱�
		main.loginview.setMain(main); // �α��� â���� ���� Ŭ���� ������
	}
		public void showFrameTest(){
			loginview.dispose();
			new MainForm();
	
		}
	
}


