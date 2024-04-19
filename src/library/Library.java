/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package library;

import java.io.BufferedReader;  
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;


import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Korb Daven
 */
public class Library extends javax.swing.JFrame {

    /**
     * Creates new form Library
     */
    public Library() {
        initComponents();
        startApp.setVisible(true);
        userLayout.setVisible(false);
        adminLayout.setVisible(false);
    }
    
    final static String MEMBER_FILE_PATH="MemberDB.csv";
    final static String SUM_FILE_PATH="sum.csv";
    
    //class for member and librarian
    class Member{
        int mId;
        String fullName;
        String email;
        private String pw;
        private String phNum;
        private String address;
        
        //Book////////
        private String book_id;
        private String book_title;
        
        public String getbook_id(){
            return book_id;
        }
        public String getbook_title(){
            return book_title;
        }
        
        //////////////////////////////
    
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getPhNum() {
		return phNum;
	}
	public void setPhNum(String phNum) {
		this.phNum = phNum;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	ArrayList<String> borrowBook = new ArrayList<>();
	String bDate; //borrow date
        ArrayList<String> returnedBook = new ArrayList<>();
        String reDate; //returned date
        ArrayList<String> booksHistory = new ArrayList<>();

        public static final int bMaxDuration=20; //max num of days books can be borrow

        public Member(int mId,String fullName, String email, String pw, String phNum, String address,ArrayList<String> borrowBook,String bDate,ArrayList<String> returnedBook,String reDate,ArrayList<String> booksHistory) {
            this.mId=mId;
            this.fullName = fullName;
            this.email = email;
            this.pw = pw;
            this.phNum = phNum;
            this.address = address;
            this.borrowBook=borrowBook;
            this.bDate=bDate;
            this.returnedBook=returnedBook;
            this.reDate=reDate;
            this.booksHistory=booksHistory;
        
        }
    }
    
     void Search(String str){
        DefaultTableModel model = (DefaultTableModel)tb_table.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        tb_table.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(str));
    }
    

    class Librarian{
        String fullName;
        String email;
        private String pw;
        private String phNum;
        private String address;
        public String getPw() {
                    return pw;
            }
            public void setPw(String pw) {
                    this.pw = pw;
            }
            public String getPhNum() {
                    return phNum;
            }
            public void setPhNum(String phNum) {
                    this.phNum = phNum;
            }
            public String getAddress() {
                    return address;
            }
            public void setAddress(String address) {
                    this.address = address;
            }

    }
    
    //addMemberfunction
    private static void addMemberToFile(Member member) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEMBER_FILE_PATH, true))) {
            writer.write(member.mId+","+member.fullName + "," + member.email + "," + member.getPw() + "," +
                    member.getPhNum() + "," + member.getAddress());
            
            writer.newLine(); 
            writer.close();
            JOptionPane.showMessageDialog(null, "Data successfully added to the CSV file.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding data to the CSV file.");
        }
    }
	
	static int findLatestmId(String filename) {
		String line = "";

		String numOfMember = null;
		
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine();

            while ((line = br.readLine()) != null) {
                
            	String[] data = line.split(",");
                numOfMember= data[0];
               
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		int latestId=Integer.parseInt(numOfMember)+10001;
		
		//increase number
		String oldCount=null;
		try (BufferedReader br = new BufferedReader(new FileReader(SUM_FILE_PATH))) {
            br.readLine();

            while ((line = br.readLine()) != null) {
                
            	String[] data = line.split(",");
            	oldCount = data[0];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		incTotalInFile(SUM_FILE_PATH, 2, 1); //increase num of total member
		
		return latestId;
		
		
		
	}
	
	static void incTotalInFile(String fileName,int row,int col) {
		String originalFile = fileName;
	    String tempFile = "temp.csv";

	    int desiredRow = row;
	    int desiredCol = col;

	    try (BufferedReader br = new BufferedReader(new FileReader(originalFile));
	         BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

	        String line;
	        int currRow = 0;
	        while ((line = br.readLine()) != null) {
	            currRow++;
	            String[] cols = line.split(",");
	            if (currRow == desiredRow) {
	                if (desiredCol <= cols.length) {
	                    try {
	                    	int value=Integer.parseInt(cols[desiredCol-1]);
	                    	value++;
	                    	cols[desiredCol-1]=String.valueOf(value);
	                    	line=String.join(",",cols);
	                    	
	                    }catch(NumberFormatException e) {
	                    	System.out.println("Value is not an INT");
	                    	return;
	                    }
	                } else {
	                    System.out.println("out of bounds.");
	                }
	            }
	            bw.write(line);
	            bw.newLine();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	 // Delete the existing file coz need to replace it for new file
	    File oldFile = new File(fileName);
	    if (oldFile.exists()) {
	        if (oldFile.delete()) {
	            System.out.println("Existing file deleted successfully.");
	        } else {
	            System.out.println("Failed to delete existing file.");
	            return;  
	        }
	    }

	    // Rename tempFile to fileName
	    File tempFile1 = new File("temp.csv");
	    File newFile = new File(fileName);

	    if (tempFile1.renameTo(newFile)) {
	        System.out.println("File successfully renamed.");
	    } else {
	        System.out.println("Failed to rename file.");
	    }
	}
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startApp = new javax.swing.JPanel();
        imageCover = new javax.swing.JLabel();
        middleVertical = new javax.swing.JPanel();
        openLogin = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        createAcc = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        userEmail = new javax.swing.JTextField();
        userPassword = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        userPhone = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        userAddress = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        LibraLogin = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        adminEmail = new javax.swing.JTextField();
        sfsfsfdg = new javax.swing.JLabel();
        adminPassword = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        memberLogin = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        memberEmail = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        memberPassword = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        userLayout = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        userProfile = new javax.swing.JPanel();
        userHistory = new javax.swing.JPanel();
        userReturn = new javax.swing.JPanel();
        userBorrow = new javax.swing.JPanel();
        userCategory = new javax.swing.JPanel();
        userHome = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        adminLayout = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jPanel37 = new javax.swing.JPanel();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        adminDashboard = new javax.swing.JPanel();
        adminProfile = new javax.swing.JPanel();
        bookAddition = new javax.swing.JPanel();
        userAddition = new javax.swing.JPanel();
        recordBorrower = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        label_name = new javax.swing.JTextField();
        label_email = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_table = new javax.swing.JTable();
        search_btn = new javax.swing.JButton();
        delete_btn = new javax.swing.JButton();
        search_label = new javax.swing.JTextField();
        update_btn = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        categoryAddition = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Library Management System");
        setMinimumSize(new java.awt.Dimension(1100, 660));
        setResizable(false);

        startApp.setBackground(new java.awt.Color(204, 204, 255));
        startApp.setFocusable(false);
        startApp.setPreferredSize(new java.awt.Dimension(1100, 650));

        imageCover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library/Image/mybook.jpg"))); // NOI18N
        imageCover.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));

        middleVertical.setBackground(new java.awt.Color(255, 153, 153));
        middleVertical.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        middleVertical.setAlignmentX(0.0F);
        middleVertical.setAlignmentY(0.0F);

        javax.swing.GroupLayout middleVerticalLayout = new javax.swing.GroupLayout(middleVertical);
        middleVertical.setLayout(middleVerticalLayout);
        middleVerticalLayout.setHorizontalGroup(
            middleVerticalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 6, Short.MAX_VALUE)
        );
        middleVerticalLayout.setVerticalGroup(
            middleVerticalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        openLogin.setBackground(new java.awt.Color(255, 204, 153));

        javax.swing.GroupLayout openLoginLayout = new javax.swing.GroupLayout(openLogin);
        openLogin.setLayout(openLoginLayout);
        openLoginLayout.setHorizontalGroup(
            openLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        openLoginLayout.setVerticalGroup(
            openLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        createAcc.setBackground(new java.awt.Color(255, 255, 255));

        jButton2.setBackground(new java.awt.Color(0, 102, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Create Account");
        jButton2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        jButton2.setBorderPainted(false);
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 51, 102));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Hi, Complete Your Profile!");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 51, 153));
        jLabel20.setText("Your Fullname");

        username.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 255), 2, true));
        username.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                usernameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                usernameFocusLost(evt);
            }
        });
        username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 0, 153));
        jLabel21.setText("Email Address");

        userEmail.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 255), 2, true));
        userEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                userEmailFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                userEmailFocusLost(evt);
            }
        });
        userEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userEmailActionPerformed(evt);
            }
        });

        userPassword.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 255), 2, true));
        userPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                userPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                userPasswordFocusLost(evt);
            }
        });
        userPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userPasswordActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(51, 0, 153));
        jLabel22.setText("Create Password");

        userPhone.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 255), 2, true));
        userPhone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                userPhoneFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                userPhoneFocusLost(evt);
            }
        });
        userPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userPhoneActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 0, 153));
        jLabel23.setText("Phone Number");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 0, 153));
        jLabel24.setText("Address");

        userAddress.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 255), 2, true));
        userAddress.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                userAddressFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                userAddressFocusLost(evt);
            }
        });
        userAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userAddressActionPerformed(evt);
            }
        });

        jCheckBox1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox1.setForeground(new java.awt.Color(0, 51, 102));
        jCheckBox1.setText("I agree to terms & conditions");
        jCheckBox1.setBorder(null);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout createAccLayout = new javax.swing.GroupLayout(createAcc);
        createAcc.setLayout(createAccLayout);
        createAccLayout.setHorizontalGroup(
            createAccLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createAccLayout.createSequentialGroup()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(createAccLayout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(createAccLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(createAccLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(username)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(userEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                        .addComponent(userPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(userPhone, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(userAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(208, Short.MAX_VALUE))
        );
        createAccLayout.setVerticalGroup(
            createAccLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, createAccLayout.createSequentialGroup()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );

        jTabbedPane1.addTab("Welcome to GIC Library", createAcc);

        LibraLogin.setBackground(new java.awt.Color(255, 255, 255));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 51, 102));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Dear Master, Welcome!");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(51, 0, 153));
        jLabel27.setText("Email Address");

        adminEmail.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 255), 2, true));
        adminEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                adminEmailFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                adminEmailFocusLost(evt);
            }
        });
        adminEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminEmailActionPerformed(evt);
            }
        });

        sfsfsfdg.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        sfsfsfdg.setForeground(new java.awt.Color(51, 0, 153));
        sfsfsfdg.setText("Password");

        adminPassword.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 255), 2, true));
        adminPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                adminPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                adminPasswordFocusLost(evt);
            }
        });
        adminPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminPasswordActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(51, 102, 255));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Login");
        jButton3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout LibraLoginLayout = new javax.swing.GroupLayout(LibraLogin);
        LibraLogin.setLayout(LibraLoginLayout);
        LibraLoginLayout.setHorizontalGroup(
            LibraLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LibraLoginLayout.createSequentialGroup()
                .addGroup(LibraLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(LibraLoginLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, LibraLoginLayout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, LibraLoginLayout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addGroup(LibraLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(adminEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                            .addGroup(LibraLoginLayout.createSequentialGroup()
                                .addGroup(LibraLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sfsfsfdg, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(adminPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE))))
                .addGap(203, 203, 203))
        );
        LibraLoginLayout.setVerticalGroup(
            LibraLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LibraLoginLayout.createSequentialGroup()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adminEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(sfsfsfdg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adminPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 302, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Login as Librarian", LibraLogin);

        memberLogin.setBackground(new java.awt.Color(255, 255, 255));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 51, 102));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("Welcome Reader!");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(51, 0, 153));
        jLabel29.setText("Email Address");

        memberEmail.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 255), 2, true));
        memberEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                memberEmailFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                memberEmailFocusLost(evt);
            }
        });
        memberEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memberEmailActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(51, 0, 153));
        jLabel30.setText("Password");

        memberPassword.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 255), 2, true));
        memberPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                memberPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                memberPasswordFocusLost(evt);
            }
        });
        memberPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memberPasswordActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(51, 102, 255));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Login");
        jButton4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout memberLoginLayout = new javax.swing.GroupLayout(memberLogin);
        memberLogin.setLayout(memberLoginLayout);
        memberLoginLayout.setHorizontalGroup(
            memberLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(memberLoginLayout.createSequentialGroup()
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 313, Short.MAX_VALUE))
            .addGroup(memberLoginLayout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(memberLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(memberLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(memberPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(memberEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        memberLoginLayout.setVerticalGroup(
            memberLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(memberLoginLayout.createSequentialGroup()
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(memberEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(memberPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 288, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Login as Member", memberLogin);

        javax.swing.GroupLayout startAppLayout = new javax.swing.GroupLayout(startApp);
        startApp.setLayout(startAppLayout);
        startAppLayout.setHorizontalGroup(
            startAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startAppLayout.createSequentialGroup()
                .addComponent(imageCover, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(middleVertical, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 606, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(openLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        startAppLayout.setVerticalGroup(
            startAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(middleVertical, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(openLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(imageCover, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
        );

        userLayout.setBackground(new java.awt.Color(204, 204, 255));
        userLayout.setAutoscrolls(true);
        userLayout.setFocusable(false);
        userLayout.setPreferredSize(new java.awt.Dimension(1100, 660));

        jPanel2.setBackground(new java.awt.Color(97, 79, 221));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("_____________");
        jLabel34.setToolTipText("");
        jLabel34.setOpaque(true);

        jLabel35.setBackground(new java.awt.Color(255, 204, 204));
        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("GIC Library");

        jPanel12.setLayout(null);

        jPanel19.setBackground(new java.awt.Color(97, 79, 221));
        jPanel19.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel19.setToolTipText("");
        jPanel19.setName(""); // NOI18N
        jPanel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel19MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel19MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel19MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel19MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanel19MouseReleased(evt);
            }
        });

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library/Image/home (1).png"))); // NOI18N

        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Home");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel20.setBackground(new java.awt.Color(97, 79, 221));
        jPanel20.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel20.setToolTipText("");
        jPanel20.setName(""); // NOI18N
        jPanel20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel20MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel20MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel20MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel20MousePressed(evt);
            }
        });

        jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library/Image/categories (1).png"))); // NOI18N

        jLabel39.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Category");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel38)
                .addGap(35, 35, 35)
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addComponent(jLabel38))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel21.setBackground(new java.awt.Color(97, 79, 221));
        jPanel21.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel21.setToolTipText("");
        jPanel21.setName(""); // NOI18N
        jPanel21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel21MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel21MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel21MousePressed(evt);
            }
        });

        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library/Image/open-book (1).png"))); // NOI18N

        jLabel41.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Borrow Book");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel40)
                .addGap(37, 37, 37)
                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41)
                    .addComponent(jLabel40))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel22.setBackground(new java.awt.Color(97, 79, 221));
        jPanel22.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel22.setToolTipText("");
        jPanel22.setName(""); // NOI18N
        jPanel22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel22MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel22MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel22MousePressed(evt);
            }
        });

        jLabel42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library/Image/books-stack-of-three.png"))); // NOI18N

        jLabel43.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Return Book");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel42)
                .addGap(37, 37, 37)
                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(jLabel42))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel23.setBackground(new java.awt.Color(97, 79, 221));
        jPanel23.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel23.setToolTipText("");
        jPanel23.setName(""); // NOI18N
        jPanel23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel23MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel23MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel23MousePressed(evt);
            }
        });

        jLabel44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library/Image/history.png"))); // NOI18N

        jLabel45.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("History");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel44)
                .addGap(37, 37, 37)
                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel45)
                    .addComponent(jLabel44))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel24.setBackground(new java.awt.Color(97, 79, 221));
        jPanel24.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel24.setToolTipText("");
        jPanel24.setName(""); // NOI18N
        jPanel24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel24MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel24MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel24MousePressed(evt);
            }
        });

        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library/Image/user (1).png"))); // NOI18N

        jLabel67.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(255, 255, 255));
        jLabel67.setText("Your Profile");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel46)
                .addGap(37, 37, 37)
                .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel67)
                    .addComponent(jLabel46))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 48, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(333, Short.MAX_VALUE))
        );

        jPanel3.setLayout(null);

        userProfile.setBackground(new java.awt.Color(234, 235, 226));

        javax.swing.GroupLayout userProfileLayout = new javax.swing.GroupLayout(userProfile);
        userProfile.setLayout(userProfileLayout);
        userProfileLayout.setHorizontalGroup(
            userProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 830, Short.MAX_VALUE)
        );
        userProfileLayout.setVerticalGroup(
            userProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
        );

        jPanel3.add(userProfile);
        userProfile.setBounds(0, 0, 830, 660);

        userHistory.setBackground(new java.awt.Color(230, 239, 234));

        javax.swing.GroupLayout userHistoryLayout = new javax.swing.GroupLayout(userHistory);
        userHistory.setLayout(userHistoryLayout);
        userHistoryLayout.setHorizontalGroup(
            userHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 830, Short.MAX_VALUE)
        );
        userHistoryLayout.setVerticalGroup(
            userHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
        );

        jPanel3.add(userHistory);
        userHistory.setBounds(0, 0, 830, 660);

        userReturn.setBackground(new java.awt.Color(230, 245, 248));

        javax.swing.GroupLayout userReturnLayout = new javax.swing.GroupLayout(userReturn);
        userReturn.setLayout(userReturnLayout);
        userReturnLayout.setHorizontalGroup(
            userReturnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 830, Short.MAX_VALUE)
        );
        userReturnLayout.setVerticalGroup(
            userReturnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
        );

        jPanel3.add(userReturn);
        userReturn.setBounds(0, 0, 830, 660);

        userBorrow.setBackground(new java.awt.Color(227, 228, 245));

        javax.swing.GroupLayout userBorrowLayout = new javax.swing.GroupLayout(userBorrow);
        userBorrow.setLayout(userBorrowLayout);
        userBorrowLayout.setHorizontalGroup(
            userBorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 830, Short.MAX_VALUE)
        );
        userBorrowLayout.setVerticalGroup(
            userBorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
        );

        jPanel3.add(userBorrow);
        userBorrow.setBounds(0, 0, 830, 660);

        userCategory.setBackground(new java.awt.Color(245, 230, 245));

        javax.swing.GroupLayout userCategoryLayout = new javax.swing.GroupLayout(userCategory);
        userCategory.setLayout(userCategoryLayout);
        userCategoryLayout.setHorizontalGroup(
            userCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 830, Short.MAX_VALUE)
        );
        userCategoryLayout.setVerticalGroup(
            userCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
        );

        jPanel3.add(userCategory);
        userCategory.setBounds(0, 0, 830, 660);

        userHome.setBackground(new java.awt.Color(255, 239, 239));

        javax.swing.GroupLayout userHomeLayout = new javax.swing.GroupLayout(userHome);
        userHome.setLayout(userHomeLayout);
        userHomeLayout.setHorizontalGroup(
            userHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 830, Short.MAX_VALUE)
        );
        userHomeLayout.setVerticalGroup(
            userHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
        );

        jPanel3.add(userHome);
        userHome.setBounds(0, 0, 830, 660);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 204, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library/Image/log-out (1).png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel1MousePressed(evt);
            }
        });

        javax.swing.GroupLayout userLayoutLayout = new javax.swing.GroupLayout(userLayout);
        userLayout.setLayout(userLayoutLayout);
        userLayoutLayout.setHorizontalGroup(
            userLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userLayoutLayout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 825, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(userLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(userLayoutLayout.createSequentialGroup()
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 829, Short.MAX_VALUE)))
        );
        userLayoutLayout.setVerticalGroup(
            userLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userLayoutLayout.createSequentialGroup()
                .addContainerGap(348, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(273, 273, 273))
            .addGroup(userLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        adminLayout.setBackground(new java.awt.Color(204, 204, 255));
        adminLayout.setFocusable(false);
        adminLayout.setPreferredSize(new java.awt.Dimension(1100, 650));

        jPanel4.setBackground(new java.awt.Color(158, 79, 221));

        jLabel74.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(255, 255, 255));
        jLabel74.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel74.setText("_____________");
        jLabel74.setToolTipText("");
        jLabel74.setOpaque(true);

        jLabel75.setBackground(new java.awt.Color(255, 204, 204));
        jLabel75.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(255, 255, 255));
        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel75.setText("GIC Library");

        jPanel32.setLayout(null);

        jPanel33.setBackground(new java.awt.Color(158, 79, 221));
        jPanel33.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel33.setToolTipText("");
        jPanel33.setName(""); // NOI18N
        jPanel33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel33MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel33MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel33MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel33MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanel33MouseReleased(evt);
            }
        });

        jLabel76.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library/Image/dashboard.png"))); // NOI18N

        jLabel77.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(255, 255, 255));
        jLabel77.setText("Dashboard");

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel76)
                .addGap(38, 38, 38)
                .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel77)
                    .addComponent(jLabel76))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel34.setBackground(new java.awt.Color(158, 79, 221));
        jPanel34.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel34.setToolTipText("");
        jPanel34.setName(""); // NOI18N
        jPanel34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel34MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel34MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel34MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel34MousePressed(evt);
            }
        });

        jLabel78.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library/Image/categories (1).png"))); // NOI18N

        jLabel79.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(255, 255, 255));
        jLabel79.setText("Category Addition");

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel78)
                .addGap(37, 37, 37)
                .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel79)
                    .addComponent(jLabel78))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel35.setBackground(new java.awt.Color(158, 79, 221));
        jPanel35.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel35.setToolTipText("");
        jPanel35.setName(""); // NOI18N
        jPanel35.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel35MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel35MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel35MousePressed(evt);
            }
        });

        jLabel80.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library/Image/books-stack-of-three.png"))); // NOI18N

        jLabel81.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(255, 255, 255));
        jLabel81.setText("Book Addition");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel80)
                .addGap(37, 37, 37)
                .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel81)
                    .addComponent(jLabel80))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel36.setBackground(new java.awt.Color(158, 79, 221));
        jPanel36.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel36.setToolTipText("");
        jPanel36.setName(""); // NOI18N
        jPanel36.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel36MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel36MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel36MousePressed(evt);
            }
        });

        jLabel82.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library/Image/group.png"))); // NOI18N

        jLabel83.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel83.setForeground(new java.awt.Color(255, 255, 255));
        jLabel83.setText("User Addition");

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel82)
                .addGap(37, 37, 37)
                .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel83)
                    .addComponent(jLabel82))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel37.setBackground(new java.awt.Color(158, 79, 221));
        jPanel37.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel37.setToolTipText("");
        jPanel37.setName(""); // NOI18N
        jPanel37.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel37MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel37MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel37MousePressed(evt);
            }
        });

        jLabel84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library/Image/verify.png"))); // NOI18N

        jLabel85.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel85.setForeground(new java.awt.Color(255, 255, 255));
        jLabel85.setText("Record Borrowers");
        jLabel85.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jLabel85AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel84)
                .addGap(37, 37, 37)
                .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel85)
                    .addComponent(jLabel84))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel38.setBackground(new java.awt.Color(158, 79, 221));
        jPanel38.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel38.setToolTipText("");
        jPanel38.setName(""); // NOI18N
        jPanel38.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel38MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel38MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel38MousePressed(evt);
            }
        });

        jLabel86.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library/Image/user (1).png"))); // NOI18N

        jLabel87.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel87.setForeground(new java.awt.Color(255, 255, 255));
        jLabel87.setText("Your Profile");

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel86)
                .addGap(37, 37, 37)
                .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel87)
                    .addComponent(jLabel86))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/library/Image/log-out (1).png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel2MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(107, 107, 107)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel75)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(275, Short.MAX_VALUE))
        );

        jPanel1.setLayout(null);

        adminDashboard.setBackground(new java.awt.Color(242, 242, 226));

        javax.swing.GroupLayout adminDashboardLayout = new javax.swing.GroupLayout(adminDashboard);
        adminDashboard.setLayout(adminDashboardLayout);
        adminDashboardLayout.setHorizontalGroup(
            adminDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 830, Short.MAX_VALUE)
        );
        adminDashboardLayout.setVerticalGroup(
            adminDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );

        jPanel1.add(adminDashboard);
        adminDashboard.setBounds(0, 0, 830, 650);

        adminProfile.setBackground(new java.awt.Color(233, 226, 248));

        javax.swing.GroupLayout adminProfileLayout = new javax.swing.GroupLayout(adminProfile);
        adminProfile.setLayout(adminProfileLayout);
        adminProfileLayout.setHorizontalGroup(
            adminProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 830, Short.MAX_VALUE)
        );
        adminProfileLayout.setVerticalGroup(
            adminProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );

        jPanel1.add(adminProfile);
        adminProfile.setBounds(0, 0, 830, 650);

        bookAddition.setBackground(new java.awt.Color(239, 248, 231));

        javax.swing.GroupLayout bookAdditionLayout = new javax.swing.GroupLayout(bookAddition);
        bookAddition.setLayout(bookAdditionLayout);
        bookAdditionLayout.setHorizontalGroup(
            bookAdditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 830, Short.MAX_VALUE)
        );
        bookAdditionLayout.setVerticalGroup(
            bookAdditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );

        jPanel1.add(bookAddition);
        bookAddition.setBounds(0, 0, 830, 650);

        userAddition.setBackground(new java.awt.Color(230, 253, 252));

        javax.swing.GroupLayout userAdditionLayout = new javax.swing.GroupLayout(userAddition);
        userAddition.setLayout(userAdditionLayout);
        userAdditionLayout.setHorizontalGroup(
            userAdditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 830, Short.MAX_VALUE)
        );
        userAdditionLayout.setVerticalGroup(
            userAdditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );

        jPanel1.add(userAddition);
        userAddition.setBounds(0, 0, 830, 650);

        recordBorrower.setBackground(new java.awt.Color(244, 233, 248));
        recordBorrower.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setText("User Name");

        jLabel4.setText("Email");

        label_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                label_nameActionPerformed(evt);
            }
        });

        label_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                label_emailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_name))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_email, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_name, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_email, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        recordBorrower.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(498, 0, -1, -1));

        jPanel6.setBackground(new java.awt.Color(0, 0, 153));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("RECORDING TABLE");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(300, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(335, 335, 335))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                .addContainerGap())
        );

        recordBorrower.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 138, -1, -1));

        tb_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"001", "English", "2024-04-12", "2024-04-22"},
                {"002", "Math", "2024-04-10", "2024-04-13"},
                {"003", "Physic", "2024-04-9", "2024-04-30"},
                {null, null, null, null}
            },
            new String [] {
                "Book ID", "Book TITLE", "Borrow Date", "Return Date"
            }
        ));
        tb_table.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tb_tableAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jScrollPane1.setViewportView(tb_table);

        recordBorrower.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 225, 818, 288));

        search_btn.setText("Search");
        search_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_btnActionPerformed(evt);
            }
        });
        recordBorrower.add(search_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 160, 50));

        delete_btn.setText("Delete");
        delete_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_btnActionPerformed(evt);
            }
        });
        recordBorrower.add(delete_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 550, 160, 40));
        recordBorrower.add(search_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, 270, 50));

        update_btn.setText("Update return date");
        update_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update_btnActionPerformed(evt);
            }
        });
        recordBorrower.add(update_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 550, 160, 40));

        jButton7.setText("Check");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        recordBorrower.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, 160, 40));

        jPanel1.add(recordBorrower);
        recordBorrower.setBounds(0, 0, 830, 650);

        categoryAddition.setBackground(new java.awt.Color(253, 238, 238));

        javax.swing.GroupLayout categoryAdditionLayout = new javax.swing.GroupLayout(categoryAddition);
        categoryAddition.setLayout(categoryAdditionLayout);
        categoryAdditionLayout.setHorizontalGroup(
            categoryAdditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 830, Short.MAX_VALUE)
        );
        categoryAdditionLayout.setVerticalGroup(
            categoryAdditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );

        jPanel1.add(categoryAddition);
        categoryAddition.setBounds(0, 0, 830, 650);

        javax.swing.GroupLayout adminLayoutLayout = new javax.swing.GroupLayout(adminLayout);
        adminLayout.setLayout(adminLayoutLayout);
        adminLayoutLayout.setHorizontalGroup(
            adminLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminLayoutLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE))
        );
        adminLayoutLayout.setVerticalGroup(
            adminLayoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1100, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(startApp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(userLayout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(adminLayout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1950, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(startApp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(userLayout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(adminLayout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        userLayout.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
        //User
        String fullName=username.getText();
        String email=userEmail.getText();
        String password=userPassword.getText();
        String phoneNumber=userPhone.getText();
        String address=userAddress.getText();
        
        
        
        if(fullName.isEmpty()||email.isEmpty()||password.isEmpty()||phoneNumber.isEmpty()||address.isEmpty()){
            JOptionPane.showMessageDialog(null, "Please complete all the informations");
        }else{
            if(jCheckBox1.isSelected()){
                int mId=findLatestmId(SUM_FILE_PATH);

                ArrayList<String> borrowBook=null;
                String bDate="null";
                ArrayList<String> returnedBook=null;
                String reDate="null";
                ArrayList<String> booksHistory=null;
        
                Member newMember = new Member(mId,fullName, email, password, phoneNumber, address,borrowBook,bDate,returnedBook,reDate,booksHistory);
                addMemberToFile(newMember);
            
                startApp.setVisible(false);
                userLayout.setVisible(true);
                //user
                userHome.setVisible(true);
                userCategory.setVisible(false);
                userBorrow.setVisible(false);
                userReturn.setVisible(false);
                userHistory.setVisible(false);
                userProfile.setVisible(false);
                
                JOptionPane.showMessageDialog(null, "Welcome our new member!!");
            }else{
                JOptionPane.showMessageDialog(null, "Please check the aggreement to terms and conditions");
            }
            
        }
        
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void usernameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usernameFocusGained
        // TODO add your handling code here:
        if(username.getText().equals("Input your name ")){
            username.setText("");
        }
    }//GEN-LAST:event_usernameFocusGained

    private void usernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_usernameFocusLost
        // TODO add your handling code here:
        if(username.getText().equals("")){
            username.setText("Input your name ");
        }
    }//GEN-LAST:event_usernameFocusLost

    private void usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameActionPerformed
        // TODO add your handling code here:
        if(username.getText().equals("")){
            username.setText("Input your name ");
        }
    }//GEN-LAST:event_usernameActionPerformed

    private void userEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userEmailFocusGained
        // TODO add your handling code here:
        if(userEmail.getText().equals("Enter your email ")){
            userEmail.setText("");
        }
    }//GEN-LAST:event_userEmailFocusGained

    private void userEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userEmailFocusLost
        // TODO add your handling code here:
        if(userEmail.getText().equals("")){
            userEmail.setText("Enter your email ");
        }
    }//GEN-LAST:event_userEmailFocusLost

    private void userEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userEmailActionPerformed
        // TODO add your handling code here:
        if(userEmail.getText().equals("")){
            userEmail.setText("Enter your email ");
        }
    }//GEN-LAST:event_userEmailActionPerformed

    private void userPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userPasswordFocusGained
        // TODO add your handling code here:
        if(userPassword.getText().equals("Create your password ")){
            userPassword.setText("");
        }
    }//GEN-LAST:event_userPasswordFocusGained

    private void userPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userPasswordFocusLost
        // TODO add your handling code here:
        if(userPassword.getText().equals("")){
            userPassword.setText("Create your password ");
        }
    }//GEN-LAST:event_userPasswordFocusLost

    private void userPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userPasswordActionPerformed
        // TODO add your handling code here:
        if(userPassword.getText().equals("")){
            userPassword.setText("Create your password ");
        }
    }//GEN-LAST:event_userPasswordActionPerformed

    private void userPhoneFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userPhoneFocusGained
        // TODO add your handling code here:
        if(userPhone.getText().equals("Input phone number ")){
            userPhone.setText("");
        }

    }//GEN-LAST:event_userPhoneFocusGained

    private void userPhoneFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userPhoneFocusLost
        // TODO add your handling code here:
        if(userPhone.getText().equals("")){
            userPhone.setText("Input phone number ");
        }
    }//GEN-LAST:event_userPhoneFocusLost

    private void userPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userPhoneActionPerformed
        // TODO add your handling code here:
        if(userPhone.getText().equals("")){
            userPhone.setText("Input phone number ");
        }
    }//GEN-LAST:event_userPhoneActionPerformed

    private void userAddressFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userAddressFocusGained
        // TODO add your handling code here:
        if(userAddress.getText().equals("Enter address ")){
            userAddress.setText("");
        }
    }//GEN-LAST:event_userAddressFocusGained

    private void userAddressFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userAddressFocusLost
        // TODO add your handling code here:
        if(userAddress.getText().equals("")){
            userAddress.setText("Enter address ");
        }
    }//GEN-LAST:event_userAddressFocusLost

    private void userAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userAddressActionPerformed
        // TODO add your handling code here:
        if(userAddress.getText().equals("")){
            userAddress.setText("Enter address ");
        }
    }//GEN-LAST:event_userAddressActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void adminEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_adminEmailFocusGained
        // TODO add your handling code here:
        if(adminEmail.getText().equals("Enter your email ")){
            adminEmail.setText("");
        }
    }//GEN-LAST:event_adminEmailFocusGained

    private void adminEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_adminEmailFocusLost
        // TODO add your handling code here:
        if(adminEmail.getText().equals("")){
            adminEmail.setText("Enter your email ");
        }
    }//GEN-LAST:event_adminEmailFocusLost

    private void adminEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminEmailActionPerformed
        // TODO add your handling code here:
        if(adminEmail.getText().equals("")){
            adminEmail.setText("Enter your email ");
        }
    }//GEN-LAST:event_adminEmailActionPerformed

    private void adminPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_adminPasswordFocusGained
        // TODO add your handling code here:
        if(adminPassword.getText().equals("Input password ")){
            adminPassword.setText("");
        }
    }//GEN-LAST:event_adminPasswordFocusGained

    private void adminPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_adminPasswordFocusLost
        // TODO add your handling code here:
        if(adminPassword.getText().equals("")){
            adminPassword.setText("Input password ");
        }
    }//GEN-LAST:event_adminPasswordFocusLost

    private void adminPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminPasswordActionPerformed
        // TODO add your handling code here:
        if(adminPassword.getText().equals("")){
            adminPassword.setText("Input password ");
        }
    }//GEN-LAST:event_adminPasswordActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        startApp.setVisible(false);
        adminLayout.setVisible(true);
        //admin
        adminDashboard.setVisible(true);
        categoryAddition.setVisible(false);
        bookAddition.setVisible(false);
        userAddition.setVisible(false);
        recordBorrower.setVisible(false);
        adminProfile.setVisible(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void memberEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_memberEmailFocusGained
        // TODO add your handling code here:
        if(memberEmail.getText().equals("Enter your name ")){
            memberEmail.setText("");
        }
    }//GEN-LAST:event_memberEmailFocusGained

    private void memberEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_memberEmailFocusLost
        // TODO add your handling code here:
        if(memberEmail.getText().equals("")){
            memberEmail.setText("Enter your name ");
        }
    }//GEN-LAST:event_memberEmailFocusLost

    private void memberEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memberEmailActionPerformed
        // TODO add your handling code here:
        if(memberEmail.getText().equals("")){
            memberEmail.setText("Enter your name ");
        }
    }//GEN-LAST:event_memberEmailActionPerformed

    private void memberPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_memberPasswordFocusGained
        // TODO add your handling code here:
        if(memberPassword.getText().equals("Input password ")){
            memberPassword.setText("");
        }
    }//GEN-LAST:event_memberPasswordFocusGained

    private void memberPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_memberPasswordFocusLost
        // TODO add your handling code here:
        if(memberPassword.getText().equals("")){
            memberPassword.setText("Input password ");
        }
    }//GEN-LAST:event_memberPasswordFocusLost

    private void memberPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memberPasswordActionPerformed
        // TODO add your handling code here:
        if(memberPassword.getText().equals("")){
            memberPassword.setText("Input password ");
        }
    }//GEN-LAST:event_memberPasswordActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        startApp.setVisible(false);
        userLayout.setVisible(true);
        //User
        userHome.setVisible(true);
        userCategory.setVisible(false);
        userBorrow.setVisible(false);
        userReturn.setVisible(false);
        userHistory.setVisible(false);
        userProfile.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jPanel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel19MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel19MouseClicked

    private void jPanel19MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel19MouseEntered
        // TODO add your handling code here:

        jPanel19.setBackground(new java.awt.Color(102,153,255));
    }//GEN-LAST:event_jPanel19MouseEntered

    private void jPanel19MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel19MouseExited
        // TODO add your handling code here:
        jPanel19.setBackground(new java.awt.Color(97,79,221));
    }//GEN-LAST:event_jPanel19MouseExited

    private void jPanel19MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel19MousePressed
        // TODO add your handling code here:
        userHome.setVisible(true);
        userCategory.setVisible(false);
        userBorrow.setVisible(false);
        userReturn.setVisible(false);
        userHistory.setVisible(false);
        userProfile.setVisible(false);
    }//GEN-LAST:event_jPanel19MousePressed

    private void jPanel19MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel19MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel19MouseReleased

    private void jPanel20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel20MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel20MouseClicked

    private void jPanel20MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel20MouseEntered
        // TODO add your handling code here:
        jPanel20.setBackground(new java.awt.Color(102,153,255));
    }//GEN-LAST:event_jPanel20MouseEntered

    private void jPanel20MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel20MouseExited
        // TODO add your handling code here:
        jPanel20.setBackground(new java.awt.Color(97,79,221));
    }//GEN-LAST:event_jPanel20MouseExited

    private void jPanel20MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel20MousePressed
        // TODO add your handling code here:
        userHome.setVisible(false);
        userCategory.setVisible(true);
        userBorrow.setVisible(false);
        userReturn.setVisible(false);
        userHistory.setVisible(false);
        userProfile.setVisible(false);
    }//GEN-LAST:event_jPanel20MousePressed

    private void jPanel21MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel21MouseEntered
        // TODO add your handling code here:
        jPanel21.setBackground(new java.awt.Color(102,153,255));
    }//GEN-LAST:event_jPanel21MouseEntered

    private void jPanel21MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel21MouseExited
        // TODO add your handling code here:
        jPanel21.setBackground(new java.awt.Color(97,79,221));
    }//GEN-LAST:event_jPanel21MouseExited

    private void jPanel21MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel21MousePressed
        // TODO add your handling code here:
        userHome.setVisible(false);
        userCategory.setVisible(false);
        userBorrow.setVisible(true);
        userReturn.setVisible(false);
        userHistory.setVisible(false);
        userProfile.setVisible(false);
    }//GEN-LAST:event_jPanel21MousePressed

    private void jPanel22MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel22MouseEntered
        // TODO add your handling code here:
        jPanel22.setBackground(new java.awt.Color(102,153,255));
    }//GEN-LAST:event_jPanel22MouseEntered

    private void jPanel22MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel22MouseExited
        // TODO add your handling code here:
        jPanel22.setBackground(new java.awt.Color(97,79,221));
    }//GEN-LAST:event_jPanel22MouseExited

    private void jPanel22MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel22MousePressed
        // TODO add your handling code here:
        userHome.setVisible(false);
        userCategory.setVisible(false);
        userBorrow.setVisible(false);
        userReturn.setVisible(true);
        userHistory.setVisible(false);
        userProfile.setVisible(false);
    }//GEN-LAST:event_jPanel22MousePressed

    private void jPanel23MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel23MouseEntered
        // TODO add your handling code here:
        jPanel23.setBackground(new java.awt.Color(102,153,255));
    }//GEN-LAST:event_jPanel23MouseEntered

    private void jPanel23MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel23MouseExited
        // TODO add your handling code here:
        jPanel23.setBackground(new java.awt.Color(97,79,221));
    }//GEN-LAST:event_jPanel23MouseExited

    private void jPanel23MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel23MousePressed
        // TODO add your handling code here:
        userHome.setVisible(false);
        userCategory.setVisible(false);
        userBorrow.setVisible(false);
        userReturn.setVisible(false);
        userHistory.setVisible(true);
        userProfile.setVisible(false);
    }//GEN-LAST:event_jPanel23MousePressed

    private void jPanel24MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel24MouseEntered
        // TODO add your handling code here:
        jPanel24.setBackground(new java.awt.Color(102,153,255));
    }//GEN-LAST:event_jPanel24MouseEntered

    private void jPanel24MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel24MouseExited
        // TODO add your handling code here:
        jPanel24.setBackground(new java.awt.Color(97,79,221));
    }//GEN-LAST:event_jPanel24MouseExited

    private void jPanel24MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel24MousePressed
        // TODO add your handling code here:
        userHome.setVisible(false);
        userCategory.setVisible(false);
        userBorrow.setVisible(false);
        userReturn.setVisible(false);
        userHistory.setVisible(false);
        userProfile.setVisible(true);
    }//GEN-LAST:event_jPanel24MousePressed

    private void jPanel33MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel33MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel33MouseClicked

    private void jPanel33MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel33MouseEntered
        // TODO add your handling code here:
        jPanel33.setBackground(new java.awt.Color(204, 153, 255));
    }//GEN-LAST:event_jPanel33MouseEntered

    private void jPanel33MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel33MouseExited
        // TODO add your handling code here:
        jPanel33.setBackground(new java.awt.Color(158,79,221));
    }//GEN-LAST:event_jPanel33MouseExited

    private void jPanel33MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel33MousePressed
        // TODO add your handling code here:
        adminDashboard.setVisible(true);
        categoryAddition.setVisible(false);
        bookAddition.setVisible(false);
        userAddition.setVisible(false);
        recordBorrower.setVisible(false);
        adminProfile.setVisible(false);
    }//GEN-LAST:event_jPanel33MousePressed

    private void jPanel33MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel33MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel33MouseReleased

    private void jPanel34MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel34MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel34MouseClicked

    private void jPanel34MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel34MouseEntered
        // TODO add your handling code here:
        jPanel34.setBackground(new java.awt.Color(204, 153, 255));
    }//GEN-LAST:event_jPanel34MouseEntered

    private void jPanel34MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel34MouseExited
        // TODO add your handling code here:
        jPanel34.setBackground(new java.awt.Color(158,79,221));
    }//GEN-LAST:event_jPanel34MouseExited

    private void jPanel34MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel34MousePressed
        // TODO add your handling code here:
        adminDashboard.setVisible(false);
        categoryAddition.setVisible(true);
        bookAddition.setVisible(false);
        userAddition.setVisible(false);
        recordBorrower.setVisible(false);
        adminProfile.setVisible(false);
    }//GEN-LAST:event_jPanel34MousePressed

    private void jPanel35MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel35MouseEntered
        // TODO add your handling code here:
         jPanel35.setBackground(new java.awt.Color(204, 153, 255));
    }//GEN-LAST:event_jPanel35MouseEntered

    private void jPanel35MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel35MouseExited
        // TODO add your handling code here:
        jPanel35.setBackground(new java.awt.Color(158,79,221));
    }//GEN-LAST:event_jPanel35MouseExited

    private void jPanel35MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel35MousePressed
        // TODO add your handling code here:
        adminDashboard.setVisible(false);
        categoryAddition.setVisible(false);
        bookAddition.setVisible(true);
        userAddition.setVisible(false);
        recordBorrower.setVisible(false);
        adminProfile.setVisible(false);
    }//GEN-LAST:event_jPanel35MousePressed

    private void jPanel36MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel36MouseEntered
        // TODO add your handling code here:
         jPanel36.setBackground(new java.awt.Color(204, 153, 255));
    }//GEN-LAST:event_jPanel36MouseEntered

    private void jPanel36MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel36MouseExited
        // TODO add your handling code here:
        jPanel36.setBackground(new java.awt.Color(158,79,221));
    }//GEN-LAST:event_jPanel36MouseExited

    private void jPanel36MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel36MousePressed
        // TODO add your handling code here:
        adminDashboard.setVisible(false);
        categoryAddition.setVisible(false);
        bookAddition.setVisible(false);
        userAddition.setVisible(true);
        recordBorrower.setVisible(false);
        adminProfile.setVisible(false);
    }//GEN-LAST:event_jPanel36MousePressed

    private void jPanel37MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel37MouseEntered
        // TODO add your handling code here:
        jPanel37.setBackground(new java.awt.Color(204, 153, 255));
    }//GEN-LAST:event_jPanel37MouseEntered

    private void jPanel37MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel37MouseExited
        // TODO add your handling code here:
         jPanel37.setBackground(new java.awt.Color(158,79,221));
    }//GEN-LAST:event_jPanel37MouseExited

    private void jPanel37MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel37MousePressed
        // TODO add your handling code here:
        adminDashboard.setVisible(false);
        categoryAddition.setVisible(false);
        bookAddition.setVisible(false);
        userAddition.setVisible(false);
        recordBorrower.setVisible(true);
        adminProfile.setVisible(false);
    }//GEN-LAST:event_jPanel37MousePressed

    private void jPanel38MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel38MouseEntered
        // TODO add your handling code here:
        jPanel38.setBackground(new java.awt.Color(204, 153, 255));
    }//GEN-LAST:event_jPanel38MouseEntered

    private void jPanel38MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel38MouseExited
        // TODO add your handling code here:
        jPanel38.setBackground(new java.awt.Color(158,79,221));
    }//GEN-LAST:event_jPanel38MouseExited

    private void jPanel38MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel38MousePressed
        // TODO add your handling code here:
        adminDashboard.setVisible(false);
        categoryAddition.setVisible(false);
        bookAddition.setVisible(false);
        userAddition.setVisible(false);
        recordBorrower.setVisible(false);
        adminProfile.setVisible(true);
    }//GEN-LAST:event_jPanel38MousePressed

    private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MousePressed
        // TODO add your handling code here:
        userLayout.setVisible(false);
        startApp.setVisible(true);
    }//GEN-LAST:event_jLabel1MousePressed

    private void jLabel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MousePressed
        // TODO add your handling code here:
        adminLayout.setVisible(false);
        startApp.setVisible(true);
    }//GEN-LAST:event_jLabel2MousePressed

    private void tb_tableAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tb_tableAncestorAdded
        int row= tb_table.getRowCount();
        DefaultTableModel model= (DefaultTableModel)tb_table.getModel();
        //Set value for testing
        
    }//GEN-LAST:event_tb_tableAncestorAdded

    private void search_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_btnActionPerformed
        Search(search_label.getText());
    }//GEN-LAST:event_search_btnActionPerformed

    private void delete_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_btnActionPerformed
        int row= tb_table.getSelectedRow();
        DefaultTableModel model= (DefaultTableModel)tb_table.getModel();
        model.removeRow(row);
    }//GEN-LAST:event_delete_btnActionPerformed

    private void label_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_label_nameActionPerformed
   
    }//GEN-LAST:event_label_nameActionPerformed

    private void label_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_label_emailActionPerformed
        
    }//GEN-LAST:event_label_emailActionPerformed

    private void jLabel85AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jLabel85AncestorAdded

        
    }//GEN-LAST:event_jLabel85AncestorAdded

    private void update_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_update_btnActionPerformed
        String nreturn_date= JOptionPane.showInputDialog("Please input return date: ");
        int row= tb_table.getSelectedRow();
        DefaultTableModel model= (DefaultTableModel)tb_table.getModel();
        try {
            for(int i=0; i<tb_table.getRowCount(); i++){
            model.setValueAt(nreturn_date, row, 3);
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_update_btnActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate= LocalDate.now();
        DefaultTableModel model= (DefaultTableModel)tb_table.getModel();
        for(int i=0; i<tb_table.getRowCount(); i++){
            LocalDate return_date = LocalDate.parse(model.getValueAt(i, 3).toString(), formatter);
            if(return_date.isBefore(currentDate)){
                System.out.println(model.getValueAt(i, 3));
            }
            
            
            
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Library.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Library.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Library.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Library.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Library().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel LibraLogin;
    private javax.swing.JPanel adminDashboard;
    private javax.swing.JTextField adminEmail;
    private javax.swing.JPanel adminLayout;
    private javax.swing.JTextField adminPassword;
    private javax.swing.JPanel adminProfile;
    private javax.swing.JPanel bookAddition;
    private javax.swing.JPanel categoryAddition;
    private javax.swing.JPanel createAcc;
    private javax.swing.JButton delete_btn;
    private javax.swing.JLabel imageCover;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField label_email;
    private javax.swing.JTextField label_name;
    private javax.swing.JTextField memberEmail;
    private javax.swing.JPanel memberLogin;
    private javax.swing.JTextField memberPassword;
    private javax.swing.JPanel middleVertical;
    private javax.swing.JPanel openLogin;
    private javax.swing.JPanel recordBorrower;
    private javax.swing.JButton search_btn;
    private javax.swing.JTextField search_label;
    private javax.swing.JLabel sfsfsfdg;
    private javax.swing.JPanel startApp;
    private javax.swing.JTable tb_table;
    private javax.swing.JButton update_btn;
    private javax.swing.JPanel userAddition;
    private javax.swing.JTextField userAddress;
    private javax.swing.JPanel userBorrow;
    private javax.swing.JPanel userCategory;
    private javax.swing.JTextField userEmail;
    private javax.swing.JPanel userHistory;
    private javax.swing.JPanel userHome;
    private javax.swing.JPanel userLayout;
    private javax.swing.JTextField userPassword;
    private javax.swing.JTextField userPhone;
    private javax.swing.JPanel userProfile;
    private javax.swing.JPanel userReturn;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
