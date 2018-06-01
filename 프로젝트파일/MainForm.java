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
	String b_isbn; // 코드
	String b_title; // 책제목
	String b_author; // 저자
	String b_genre; // 장르
	String b_price; // 가격
	String b_publish; // 출판사
	String b_like; // 인기도
	String b_havenum; // 현재 보유수량
	String b_soldnum; // 판매수량 

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

		// 코드 필드가 공백일때는 입력 될 수 없음
		if (!isbn.equals("")) {
			// 중복된 코드가 존재하지 않는다면
			if (new Search(isbn, list).searchData() < 0) {
				// 리스트에 데이터를 추가시킴
				list.add(new BookInfo(isbn, title, author, genre, price, 
						publish, like,havenum,soldnum));

				// 테이블의 마지막 행에 데이터를 추가시킴
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

				// 데이터 추가가 끝난 후 텍스트 필드를 초기화
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
				JOptionPane.showMessageDialog(null, "중복된 코드가 존재합니다.");
			}
		} else {
			System.err.println(">>>not exist isbn");
			JOptionPane.showMessageDialog(null, "코드가 입력되지 않았습니다.");
		}

	}

}

class Delete {
	Delete() {
	}

	void deleting(int row, JTextField t_isbn, DefaultTableModel tableModel,
			ArrayList<BookInfo> list) {
		// 데이터 확인을 위한 부분
		BookInfo temp = list.get(row);
		System.out.printf(">>>delete data: %s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
				temp.b_isbn, temp.b_title, temp.b_author, temp.b_genre,
				temp.b_price, temp.b_publish, temp.b_like, temp.b_havenum, temp.b_soldnum);
		String isbn = t_isbn.getText();
		// 리스트 내의 코드의 존재 유무를 검색하고 인덱스를 반환
		int s_row = new Search(isbn, list).searchData();
		if (s_row != -1) {
			tableModel.removeRow(s_row);
			list.remove(row);
		} else {
			System.out.println(">>> delete error");
			JOptionPane.showMessageDialog(null, "리스트내 코드가 존재하지 않습니다.");
		}
	}
}

class Update {
	String isbn, title, author, genre, price, publish, like, havenum, soldnum; // 문자열을 저장할 변수														
	JTextField t_isbn, t_title, t_author, t_genre, t_price, t_publish,
			t_like, t_havenum, t_soldnum; // 텍스트 필드
	DefaultTableModel tableModel;
	ArrayList<BookInfo> list;
	Object data[];
	int l_row, row; // l_row: 리스트의 인덱스 값, row: 테이블의 인덱스 값

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

		// 텍스트 필드의 값이 변화가 없다면 그대로 진행
		if (temp_isbn.equals(isbn)) {
			update();
		} // 텍스트 필드의 값이 변화됐다면 중복체크
		else {
			// 코드값 수정시 리스트내 중복된 코드가 있는지를 체크
			if (new Search(t_isbn.getText(), list).searchData() < 0) {
				update();
			} else {
				System.err.println(">>>duplication isbn");
				JOptionPane.showMessageDialog(null, "중복된 코드가 존재합니다");
			}
		}

	}

	public void update() {
		// 리스트에서 row 위치의 데이터를 변경
		list.set(l_row, new BookInfo(isbn, title, author, genre, price, 
				publish, like, havenum, soldnum));

		// 테이블에서 row 위치에 새로운 행을 추가시키고
		// 뒤로 밀려난 기존의 행을 삭제시킴
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

		// 수정이 끝난 후 텍스트 필드를 초기화시킴
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
	// 배열 내 데이터를 저장할 변수
	String s_isbn, s_title, s_author, s_genre, s_price, s_publish,
			s_like, s_havenum, s_soldnum; // 텍스트 필드 값을 저장할 변수

	String temp_isbn; // 검색하는데 키 값으로 사용될 코드
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

		// 검색된 데이터 출력전 테이블을 초기화 시킴
		try {
			for (int i = 0; i < list.size(); i++) {
				tableModel.removeRow(0);
			}
		} catch (Exception e) {
		}

		// 리스트를 돌면서 데이터를 검색
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

			// indexOf > 해당 문자열이 존재한다면 위치, 그 이외 -1 리턴
			// 텍스트 필드가 비어있을 경우 0을 리턴함
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
				// 테이블에 조건문에 만족하는 데이터를 추가
				tableModel.addRow(data);

			}
		}
	}

	// 배열을 순회하면서 코드의 존재여부 검색
	public int searchData() {
		// 리스트가 존재하고 있을 때 검색
		if (list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				BookInfo info = list.get(i);
				if (info.b_isbn.equals(temp_isbn)) {
					// 리스트에 중복된 코드가 있다면
					return i;
				}
			}
			// 리스트에 중복된 코드가 없다면
			return -1;

		}
		// 리스트가 존재하지 않는다면
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

			// 리스트 전체를 돌면서
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
				// BookInfo 객체를 저장함

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
				// 읽어들인 객체가 null 이라면 반복문 종료
				if (info == null)
					break;
				// 읽어들인 객체를 리스트에 추가시킴
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

				// 읽어들인 객체 데이터를 Object 배열에 저장해 테이블에 추가함
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

	String titles[] = { "isbn", "제목", "저자", "장르", "가격", "출판사", "인기도", "현재수량", "판매수량" };
	Object data[] = new Object[9];
	JTable table;
	DefaultTableModel tableModel;
	JScrollPane sp;
	ArrayList<BookInfo> list = new ArrayList<BookInfo>();
	JButton b_insert, b_delete, b_update, b_save, b_load, b_search, b_clear,
			b_exit, b_item, b_top5; 
	JTextField t_isbn, t_title, t_author, t_genre, t_price, t_publish, t_like, t_havenum, t_soldnum;
	JPanel panel; // 데이터 조작 컴포넌트를 배치시킬 패널

	LoginView loginview;
	
	
	MainForm() {
		super("도서관리 프로그램");
		setLocation(150, 10); // 프레임 위치 셋팅
		
		setSize(1200, 700); // 프레임 크기 셋팅

		// JTable
		tableModel = new DefaultTableModel(titles, 0);
		table = new JTable(tableModel);
		
		sp = new JScrollPane(table);
		sp.setPreferredSize(new Dimension(600,600));
		add(sp, BorderLayout.CENTER);

		// 입력 컴포넌트 panel
		panel = new JPanel();
		panel.setLayout(new GridLayout(13,2));
		
		panel.add(new JLabel("isbn"));
		panel.add(t_isbn = new JTextField(10));
		
		
		panel.add(new JLabel("제목"));
		panel.add(t_title = new JTextField(10));
		
		
		panel.add(new JLabel("저자"));
		panel.add(t_author = new JTextField(10));
		
		
		panel.add(new JLabel("장르"));
		panel.add(t_genre = new JTextField(10));
		
		
		panel.add(new JLabel("가격"));
		panel.add(t_price = new JTextField(10));
		
		panel.add(new JLabel("출판사"));
		panel.add(t_publish = new JTextField(10));
	
		panel.add(new JLabel("인기도"));
		panel.add(t_like = new JTextField(10));
		
		
		panel.add(new JLabel("현재수량"));
		panel.add(t_havenum = new JTextField(10));
		
		panel.add(new JLabel("판매수량"));
		panel.add(t_soldnum = new JTextField(10));
		
		add(panel, BorderLayout.EAST);
		
		// 버튼들 만들기 
		panel.add(new JLabel("< Button >"));
		panel.add(b_insert = new JButton("삽입"));
		panel.add(b_delete = new JButton("삭제"));
		
		panel.add(b_update = new JButton("수정"));
		panel.add(b_search = new JButton("검색"));
		panel.add(b_clear = new JButton("필드초기화"));
		panel.add(b_item = new JButton("재고관리"));
		panel.add(b_top5 = new JButton("매출top5"));

		// 윈도우 종료 이벤트
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// 테이블 클릭 이벤트
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Select();
			}
		});
		
		

		// 버튼 이벤트 리스너
		b_insert.addActionListener(this);
		b_delete.addActionListener(this);
		b_update.addActionListener(this);
		b_clear.addActionListener(this);
		b_search.addActionListener(this);
		b_item.addActionListener(this);
		b_top5.addActionListener(this);
		

		// 텍스트 필드 포커스 리스너
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

	
	// 메뉴바 만들기 
	public void createMenu(){
		JMenuBar mb = new JMenuBar();
		//메뉴 생성
		JMenu fileMenu = new JMenu("File");
		
		//메뉴아이템 & 액션리스너 생성 
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
	
	


	// 테이블 클릭시 해당 행의 값을 텍스트 필드에 셋팅
	public void Select() {
		int row = table.getSelectedRow();
		System.out.println(">>>click position: " + row);
		// 테이블에서 (row, ?) 위치의 데이터를 텍스트 필드에 셋팅
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
		// 선택된 테이블의 행위치를 돌려줌, 선택되지 않았을 경우 -1 리턴
		int row = table.getSelectedRow();

		// 삽입 버튼
		if (e.getSource() == b_insert) {
			System.out.println(">>>click insert");
			Insert insert = new Insert();
			insert.inserting(t_isbn, t_title, t_author, t_genre, t_price, 
					t_publish, t_like, t_havenum, t_soldnum, data,
					tableModel, list);
		}
		// 삭제 버튼
		else if (e.getSource() == b_delete) {
			System.out.println(">>> click delete");
			Delete delete = new Delete();
			if (row == -1)
				return; // 행이 선택되지 않았다면 패스
			BookInfo temp = list.get(row);
			System.out.printf(">>>delete data: %s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
					temp.b_isbn, temp.b_title, temp.b_author, temp.b_genre,
					temp.b_price, temp.b_publish, temp.b_havenum, temp.b_soldnum);
			delete.deleting(row, t_isbn, tableModel, list);

		}
		// 수정버튼
		else if (e.getSource() == b_update) {
			System.out.println(">>> click update");
			if (row == -1)
				return; // 행이 선택되지 않았다면 패스
			// 텍스트 필드 변경전 데이터로 리스트 위치 검색
			String temp_isbn = (String) tableModel.getValueAt(row, 0);
			// 리스트 내 데이터 변경에 사용하기 위해서 따로 저장
			int l_row = new Search(temp_isbn, list).searchData();
			BookInfo temp = list.get(l_row); // 데이터 확인을 위한 부분
			System.out.println(">>> update isbn: " + temp.b_isbn + " -> "
					+ t_isbn.getText());
			Update update = new Update();
			update.updating(l_row, row, temp_isbn, t_isbn, t_title, t_author,
					t_genre, t_price, t_publish, t_like, t_havenum, t_soldnum,data, tableModel, list);

		}
		
		// 텍스트 필드 초기화
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
		// 검색 버튼 ( 텍스트 필드가 비어있을 경우는 전체 출력)
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
		showtop5fr.setTitle("우리서점 매출 TOP 5 확인하기");
		showtop5fr.setSize(600, 300);

		showtop5fr.setLayout(new GridLayout(5,2));
		
		int lsize = list.size(); //list의 길이를 배열크기로 정하기 위해
		int data2[] = new int[lsize]; //list길이 크기의 배열 data2를 만듬 
		
		for(int i=0;i<list.size();i++){
			int h = Integer.parseInt(list.get(i).b_soldnum); // 각 책의 판매량이 int형 변수에 저장되고
			data2[i] = h; // 그걸 data2에 담음 
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
		
		//매출 1~5위인 책을 알기위한 책 이름 변수 선언
		String sold_1 = null;
		String sold_2 = null;
		String sold_3 = null;
		String sold_4 = null; // sold_1~5는 책 이름 담을 곳임
		String sold_5 = null;
		
		//매출 1~5위인 책의 권수를 비교해주기위해 int->String 형변환해서 변수에 저장 
		String sold1 = Integer.toString(data2[lsize-1]); // sold1~5 는 책권수임
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
		
		
		showtop5fr.add(new JLabel("1위"));
		showtop5fr.add(new JLabel(sold_1+"("+data2[lsize-1]+"권)"));
		showtop5fr.add(new JLabel("2위"));
		showtop5fr.add(new JLabel(sold_2+"("+data2[lsize-2]+"권)"));
		showtop5fr.add(new JLabel("3위"));
		showtop5fr.add(new JLabel(sold_3+"("+data2[lsize-3]+"권)"));
		showtop5fr.add(new JLabel("4위"));
		showtop5fr.add(new JLabel(sold_4+"("+data2[lsize-4]+"권)"));
		showtop5fr.add(new JLabel("5위"));
		showtop5fr.add(new JLabel(sold_5+"("+data2[lsize-5]+"권)"));
		
		showtop5fr.setVisible(true);
		
		
	}
	
	
	
	public void managing(){
		final JFrame managefr = new JFrame();
		managefr.setTitle("재고관리");
		managefr.setSize(300, 200);

		managefr.setLayout(null);
		
		JLabel select = new JLabel("메뉴 선택");
		String[] arr = {"입고","판매","반품"};
		final JComboBox box1 = new JComboBox(arr);
		select.setBounds(20, 40, 100, 20);
		box1.setBounds(150, 40, 120, 20);
		managefr.add(select);
		managefr.add(box1);
		
		
		
		ArrayList<String> list2 = new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			list2.add(list.get(i).b_title);
		}
		
		JLabel select2 = new JLabel("해당 도서 선택");
		final JComboBox box2 = new JComboBox(list2.toArray());
		select2.setBounds(20, 10, 100, 20);
		box2.setBounds(150, 10, 120, 20);
		managefr.add(select2);
		managefr.add(box2);

		JLabel number = new JLabel("권수 입력 ");
		number.setBounds(20, 70, 100, 20);
		final JTextField field = new JTextField();
		field.setBounds(150,70,120,20);
		managefr.add(number);
		managefr.add(field);
		
		JButton okbtn = new JButton("확인");
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
					System.out.println("올바르지 않은 숫자 입니다.");
				}
				else if(value.equals("")){
					System.out.println("입력하지 않으셨습니다.");
				}
				else{
					
					int value_ = Integer.parseInt(value);
				
					
					if(ch_menu.equals("입고")){
						
						int havenum1 = Integer.parseInt(havenum);
						
						havenum1 += value_;
						
						havenum = Integer.toString(havenum1);
						
						managefr.setVisible(false);
					}
					
					else if(ch_menu.equals("판매")){
						
						int havenum1 = Integer.parseInt(havenum);
						int soldnum1 = Integer.parseInt(soldnum);
						
						havenum1 -= value_;
						soldnum1 += value_;
						
						havenum = Integer.toString(havenum1);
						soldnum = Integer.toString(soldnum1);
						
						managefr.setVisible(false);
					}

					else if(ch_menu.equals("반품")){
						
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
		// 텍스트 필드가 선택되었을 때 해당 텍스트 필드의 색상 변경
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
		// 텍스트 필드의 선택이 해제되었을 때 색상 해제
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
					
					//로그인 성공이라면 매니져창 띄우기 
					
					if(isLogin()){
						main.showFrameTest(); //메인창 메소드를 이용해 창띄우기
						
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
		main.loginview = new LoginView(); //로그인 창 보이기
		main.loginview.setMain(main); // 로그인 창에게 메인 클래스 보내기
	}
		public void showFrameTest(){
			loginview.dispose();
			new MainForm();
	
		}
	
}


