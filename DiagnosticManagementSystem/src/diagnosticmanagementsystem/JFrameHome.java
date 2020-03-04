/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diagnosticmanagementsystem;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Awan Ahmed
 */
public class JFrameHome extends javax.swing.JFrame {

    /**
     * Creates new form JFrameHome
     */
    DefaultTableModel modelPatient;
    DefaultTableModel modeltest;
    Connection con = null;
    PreparedStatement pstmt = null;
    boolean isNew = true;
    int c;
    private Object indexs;//my
    String patientInfo[];
    boolean testInfo[];
    JCheckBox []box;
    String []tested=new String[12];
    int costs[] = {200, 300, 350, 1000, 290, 450, 500, 340, 100, 900, 400, 350};

    public JFrameHome() {
        initComponents();
        getContentPane().setBackground(Color.DARK_GRAY);
        modelPatient = (DefaultTableModel) jTablePatient.getModel();
        modeltest = (DefaultTableModel) jTableTests.getModel();
        patientInfo = new String[7];
        testInfo = new boolean[12];
        box=new JCheckBox[12];
        initializeTestInfo();
    }

    public void initializePatientInfo() {
        patientInfo[0] = jTextFieldName.getText();
        patientInfo[1] = jTextFieldCellNo.getText();
        patientInfo[2] = jTextFieldAge.getText();
        patientInfo[3] = jTextFieldAddress.getText();
        patientInfo[4] = jComboBoxGender.getSelectedItem().toString();
        patientInfo[5] = jTextFieldDate.getText();
        patientInfo[6] = jTextFieldSingleReference.getText();
    }

    public void initializeTestInfo() 
    {
        box[0]=jCheckBoxBloodTest;
        box[1]=jCheckBoxImmunologyTests;
        box[2]=jCheckBoxKidneyBloodTests;
        box[3]=jCheckBoxInfectiousDiseaseBloodTests;
        box[4]=jCheckBoxLiverFunctionPanelsEnzymeTests;
        box[5]=jCheckBoxMineralDeficiencyTests;
        box[6]=jCheckBoxSTDBloodTests;
        box[7]=jCheckBoxStressAdrenalFatigueTests;
        box[8]=jCheckBoxWomenSpecificTests;
        box[9]=jCheckBoxDiabetesBloodTest;
        box[10]=jCheckBoxDigestiveSystemTests;
        box[11]=jCheckBoxHormoneTests;
    }
    
   ////////////////////////////////////////////////
    ////////CLEAR DATA START?///////////////////////
    //////////////////////////////////////////////////
    
    private void clearData() {
        jTextFieldName.setText(null);
        jTextFieldCellNo.setText(null);
        jTextFieldAge.setText(null);
        jTextFieldAddress.setText(null);
        jComboBoxGender.getSelectedItem().toString();
        jTextFieldDate.setText(null);
        jTextFieldSingleReference.setText(null);
        jTextFieldName.requestFocus();
    }

    public void checkInfoAndTest()
    {
        initializePatientInfo();
        c=0;
        for(int i=0;i<6;i++)
        {
            if(patientInfo[i].equals(""))
            {
                JOptionPane.showMessageDialog(null, "please fill all..","ERROR",JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        long sum=0;
        for(int i=0;i<12;i++)
        {
            if(box[i].isSelected())
            {
                sum+=costs[i];
                tested[i]="tested";
                ++c;
            }
            else 
                tested[i]="not tested";
        }
        if(c>0)
            jTextFieldTotalCost1.setText(sum+"");
    }
    //***************************************************//
    /////INSERT DATA START/////////////////////////////////
    //**********************************************////
    public void insertData()
    {
            try {
                long a,b=0,e,d;
                a=Integer.parseInt(jTextFieldTotalCost1.getText());
                if(!jTextFieldDiscount1.getText().equals(""))
                    b=Integer.parseInt(jTextFieldDiscount1.getText());
                e=Integer.parseInt(jTextFieldPaid1.getText());
                d=a-((b*a)/100);
                if(e<=d)
                    jTextFieldDue.setText((d-e)+"");
                else
                {
                    JOptionPane.showMessageDialog(null, "invalid payment","WARNING",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                /**
                 * insert information
                 */
                con = (Connection) DriverManager.getConnection(diagnosticmanagementsystem.DB_Connection.db_url, diagnosticmanagementsystem.DB_Connection.db_userName, diagnosticmanagementsystem.DB_Connection.db_password);
                pstmt = (PreparedStatement) con.prepareStatement("insert into  patient (Patient_Name,Cell_No,Age,Address,Sex,date,Reference,Due)"
                       + "values(?,?,?,?,?,?,?,?) ");
                pstmt.setString(1, jTextFieldName.getText());
                pstmt.setString(2, jTextFieldCellNo.getText());
                pstmt.setString(3, jTextFieldAge.getText());
                pstmt.setString(4, jTextFieldAddress.getText());
                pstmt.setString(5, jComboBoxGender.getSelectedItem().toString());
                pstmt.setString(6, jTextFieldDate.getText());
                pstmt.setString(7, jTextFieldSingleReference.getText());
                pstmt.setString(8, jTextFieldDue.getText());
                /**
                 * insert tests
                 */
//                pstmt = (PreparedStatement) con.prepareStatement("insert into  test"
//                        + "values(?,?,?,?,?,?,?,?,?,?,?,?) ");
//                pstmt.setString(1, tested[0]);
//                pstmt.setString(2, tested[1]);
//                pstmt.setString(3, tested[2]);
//                pstmt.setString(4, tested[3]);
//                pstmt.setString(5, tested[4]);
//                pstmt.setString(6, tested[5]);
//                pstmt.setString(7, tested[6]);
//                pstmt.setString(8, tested[7]);
//                pstmt.setString(9, tested[8]);
//                pstmt.setString(10, tested[9]);
//                pstmt.setString(11, tested[10]);
//                pstmt.setString(12, tested[11]);
                int isOk = 0;
                isOk = pstmt.executeUpdate();
                if (isOk > 0) {
                    JOptionPane.showMessageDialog(null, "Successfully Inserted", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "NOT Inserted", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error occured due to:\n" + ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error occured due to:\n" + ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);

                }
            }
       // clearData();
    }
    
    
    ////////////////////////////////////////////////
    ////////////////// INSERT DATA END//////////////
    /////////////////////////////////////////////////
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jTextFieldCellNo = new javax.swing.JTextField();
        jTextFieldAge = new javax.swing.JTextField();
        jTextFieldDate = new javax.swing.JTextField();
        jTextFieldAddress = new javax.swing.JTextField();
        jTextFieldSingleReference = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jCheckBoxBloodTest = new javax.swing.JCheckBox();
        jCheckBoxImmunologyTests = new javax.swing.JCheckBox();
        jCheckBoxInfectiousDiseaseBloodTests = new javax.swing.JCheckBox();
        jCheckBoxKidneyBloodTests = new javax.swing.JCheckBox();
        jCheckBoxDiabetesBloodTest = new javax.swing.JCheckBox();
        jCheckBoxMineralDeficiencyTests = new javax.swing.JCheckBox();
        jCheckBoxLiverFunctionPanelsEnzymeTests = new javax.swing.JCheckBox();
        jCheckBoxWomenSpecificTests = new javax.swing.JCheckBox();
        jCheckBoxStressAdrenalFatigueTests = new javax.swing.JCheckBox();
        jCheckBoxSTDBloodTests = new javax.swing.JCheckBox();
        jCheckBoxDigestiveSystemTests = new javax.swing.JCheckBox();
        jCheckBoxHormoneTests = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jTextFieldTotalCost1 = new javax.swing.JTextField();
        jTextFieldDiscount1 = new javax.swing.JTextField();
        jTextFieldPaid1 = new javax.swing.JTextField();
        jTextFieldDue = new javax.swing.JTextField();
        jButtonTotal = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonRefresh = new javax.swing.JButton();
        jTextFieldSearchCellNo = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jButtonSearch = new javax.swing.JButton();
        jButtonUpdate = new javax.swing.JButton();
        jComboBoxGender = new javax.swing.JComboBox();
        jTextFieldDueDisable = new javax.swing.JTextField();
        jLabelDue = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePatient = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableTests = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Digonstic MAnagement System");

        jTabbedPane1.setBorder(new javax.swing.border.MatteBorder(null));
        jTabbedPane1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jPanel4.setFocusable(false);
        jPanel4.setRequestFocusEnabled(false);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setText("Reference");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Date");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Gender");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Address");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Age");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Cell No");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Name");

        jTextFieldName.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jTextFieldName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldName.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTextFieldName.setPreferredSize(new java.awt.Dimension(45, 30));
        jTextFieldName.setRequestFocusEnabled(false);

        jTextFieldCellNo.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jTextFieldCellNo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldCellNo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTextFieldCellNo.setPreferredSize(new java.awt.Dimension(45, 30));
        jTextFieldCellNo.setRequestFocusEnabled(false);

        jTextFieldAge.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jTextFieldAge.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldAge.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTextFieldAge.setPreferredSize(new java.awt.Dimension(45, 30));
        jTextFieldAge.setRequestFocusEnabled(false);

        jTextFieldDate.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jTextFieldDate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldDate.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTextFieldDate.setPreferredSize(new java.awt.Dimension(45, 30));
        jTextFieldDate.setRequestFocusEnabled(false);

        jTextFieldAddress.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jTextFieldAddress.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldAddress.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTextFieldAddress.setPreferredSize(new java.awt.Dimension(45, 30));
        jTextFieldAddress.setRequestFocusEnabled(false);

        jTextFieldSingleReference.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jTextFieldSingleReference.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldSingleReference.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTextFieldSingleReference.setPreferredSize(new java.awt.Dimension(45, 30));
        jTextFieldSingleReference.setRequestFocusEnabled(false);

        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));

        jCheckBoxBloodTest.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jCheckBoxBloodTest.setText("1 . Blood Tests (200 BDT)");

        jCheckBoxImmunologyTests.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jCheckBoxImmunologyTests.setText("2 . Immunology Tests (300 BDT)");

        jCheckBoxInfectiousDiseaseBloodTests.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jCheckBoxInfectiousDiseaseBloodTests.setText("4. Infectious Disease Blood Tests (1000 BDT )");

        jCheckBoxKidneyBloodTests.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jCheckBoxKidneyBloodTests.setText("3. Kidney Blood Tests (350 BDT)");

        jCheckBoxDiabetesBloodTest.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jCheckBoxDiabetesBloodTest.setText("10. Diabetes Blood Test(BDT 900)");

        jCheckBoxMineralDeficiencyTests.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jCheckBoxMineralDeficiencyTests.setText("6. Mineral Deficiency Tests ( BDT 450)");

        jCheckBoxLiverFunctionPanelsEnzymeTests.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jCheckBoxLiverFunctionPanelsEnzymeTests.setText("5.  Liver Function Panels & Enzyme Tests (290 BDT)");

        jCheckBoxWomenSpecificTests.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jCheckBoxWomenSpecificTests.setText("9. Women Specific Tests (BDT 100)");

        jCheckBoxStressAdrenalFatigueTests.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jCheckBoxStressAdrenalFatigueTests.setText("8. Stress & Adrenal Fatigue Tests(BDT 340)");

        jCheckBoxSTDBloodTests.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jCheckBoxSTDBloodTests.setText("7. STD Blood Tests( BDT 500 )");

        jCheckBoxDigestiveSystemTests.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jCheckBoxDigestiveSystemTests.setText("11. Digestive System Tests(BDT 400)");

        jCheckBoxHormoneTests.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jCheckBoxHormoneTests.setText("12. Hormone Tests (BDT 350)");

        jLabel1.setFont(new java.awt.Font("Perpetua Titling MT", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("List of Available Tests");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxBloodTest)
                            .addComponent(jCheckBoxImmunologyTests)
                            .addComponent(jCheckBoxKidneyBloodTests)
                            .addComponent(jCheckBoxInfectiousDiseaseBloodTests)
                            .addComponent(jCheckBoxLiverFunctionPanelsEnzymeTests)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jCheckBoxMineralDeficiencyTests)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxHormoneTests)
                    .addComponent(jCheckBoxWomenSpecificTests)
                    .addComponent(jCheckBoxDigestiveSystemTests)
                    .addComponent(jCheckBoxDiabetesBloodTest)
                    .addComponent(jCheckBoxStressAdrenalFatigueTests)
                    .addComponent(jCheckBoxSTDBloodTests)))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(247, 247, 247)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxBloodTest)
                    .addComponent(jCheckBoxSTDBloodTests))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxImmunologyTests)
                    .addComponent(jCheckBoxStressAdrenalFatigueTests))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxKidneyBloodTests)
                    .addComponent(jCheckBoxWomenSpecificTests))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxInfectiousDiseaseBloodTests)
                    .addComponent(jCheckBoxDiabetesBloodTest))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxLiverFunctionPanelsEnzymeTests)
                    .addComponent(jCheckBoxDigestiveSystemTests))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxMineralDeficiencyTests)
                    .addComponent(jCheckBoxHormoneTests))
                .addContainerGap())
        );

        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText(" Total Cost");

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Discount (percentage)");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Paid");

        jLabel22.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Due");

        jTextFieldTotalCost1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jTextFieldTotalCost1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldTotalCost1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTextFieldTotalCost1.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jTextFieldDiscount1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jTextFieldDiscount1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldDiscount1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        jTextFieldPaid1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jTextFieldPaid1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldPaid1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        jTextFieldDue.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jTextFieldDue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldDue.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTextFieldDue.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jButtonTotal.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jButtonTotal.setText("Total");
        jButtonTotal.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jButtonTotal.setFocusable(false);
        jButtonTotal.setRequestFocusEnabled(false);
        jButtonTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTotalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldTotalCost1, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                    .addComponent(jTextFieldPaid1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldDue))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldDiscount1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jButtonTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldTotalCost1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldDiscount1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldPaid1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldDue, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(15, 15, 15))
        );

        jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));

        jButtonSave.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButtonSave.setText("Save");
        jButtonSave.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jButtonSave.setFocusable(false);
        jButtonSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSave.setRequestFocusEnabled(false);
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jButtonDelete.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButtonDelete.setText("Delete");
        jButtonDelete.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jButtonDelete.setFocusable(false);
        jButtonDelete.setPreferredSize(new java.awt.Dimension(33, 21));
        jButtonDelete.setRequestFocusEnabled(false);

        jButtonRefresh.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButtonRefresh.setText("Refresh");
        jButtonRefresh.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jButtonRefresh.setFocusable(false);
        jButtonRefresh.setPreferredSize(new java.awt.Dimension(33, 21));
        jButtonRefresh.setRequestFocusEnabled(false);

        jTextFieldSearchCellNo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jTextFieldSearchCellNo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldSearchCellNo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTextFieldSearchCellNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchCellNoActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel15.setText("Enter Cel Nol");

        jButtonSearch.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButtonSearch.setText("Search");
        jButtonSearch.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jButtonSearch.setFocusable(false);
        jButtonSearch.setRequestFocusEnabled(false);

        jButtonUpdate.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButtonUpdate.setText("Update");
        jButtonUpdate.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jButtonUpdate.setFocusable(false);
        jButtonUpdate.setPreferredSize(new java.awt.Dimension(33, 21));
        jButtonUpdate.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldSearchCellNo, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldSearchCellNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonSave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButtonRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(17, 17, 17))
        );

        jComboBoxGender.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jComboBoxGender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Male", "Female" }));

        jTextFieldDueDisable.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jTextFieldDueDisable.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldDueDisable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jTextFieldDueDisable.setPreferredSize(new java.awt.Dimension(45, 30));
        jTextFieldDueDisable.setRequestFocusEnabled(false);

        jLabelDue.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabelDue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDue.setText("Due");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(40, 40, 40)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldDate, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                                    .addComponent(jTextFieldAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                                    .addComponent(jComboBoxGender, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldSingleReference, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldCellNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldAge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabelDue, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(jTextFieldDueDisable, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jTextFieldCellNo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jTextFieldAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jTextFieldAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBoxGender, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jTextFieldDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jTextFieldSingleReference, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelDue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldDueDisable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Single Patient Information", jPanel4);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Lab Tests");

        jTablePatient.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jTablePatient.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Patient Name", "Cell No", "Age", "Adress", "Gender", "Date", "Reference", "Due"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTablePatient);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("All Patient Personal Information");

        jTableTests.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jTableTests.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Patient", "Blood Test", "Immonulogy", "Kidney Blood", "Infectious Disease Blood", "Liver Function Panels & Enzym", "Mineral Deficiency", "STD Blood", "Stress & Adrenal Fatigue", "Women Specific", "Diabetes Blood", "Digestive System", "Hormone"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTableTests);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(392, 392, 392)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(386, 386, 386)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 554, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addContainerGap())
            .addComponent(jScrollPane2)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("All Patient Information", jPanel5);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("date");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("DIAGONOTIC CENTER");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(371, 371, 371)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        // TODO add your handling code here:
        checkInfoAndTest();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTotalActionPerformed
        // TODO add your handling code here:
        if(c>0 && !jTextFieldTotalCost1.getText().equals("") && !jTextFieldPaid1.getText().equals(""))
        {
            insertData();
        }
    }//GEN-LAST:event_jButtonTotalActionPerformed

    private void jTextFieldSearchCellNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchCellNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldSearchCellNoActionPerformed

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
            java.util.logging.Logger.getLogger(JFrameHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameHome().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JButton jButtonTotal;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JCheckBox jCheckBoxBloodTest;
    private javax.swing.JCheckBox jCheckBoxDiabetesBloodTest;
    private javax.swing.JCheckBox jCheckBoxDigestiveSystemTests;
    private javax.swing.JCheckBox jCheckBoxHormoneTests;
    private javax.swing.JCheckBox jCheckBoxImmunologyTests;
    private javax.swing.JCheckBox jCheckBoxInfectiousDiseaseBloodTests;
    private javax.swing.JCheckBox jCheckBoxKidneyBloodTests;
    private javax.swing.JCheckBox jCheckBoxLiverFunctionPanelsEnzymeTests;
    private javax.swing.JCheckBox jCheckBoxMineralDeficiencyTests;
    private javax.swing.JCheckBox jCheckBoxSTDBloodTests;
    private javax.swing.JCheckBox jCheckBoxStressAdrenalFatigueTests;
    private javax.swing.JCheckBox jCheckBoxWomenSpecificTests;
    private javax.swing.JComboBox jComboBoxGender;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelDue;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTablePatient;
    private javax.swing.JTable jTableTests;
    private javax.swing.JTextField jTextFieldAddress;
    private javax.swing.JTextField jTextFieldAge;
    private javax.swing.JTextField jTextFieldCellNo;
    private javax.swing.JTextField jTextFieldDate;
    private javax.swing.JTextField jTextFieldDiscount1;
    private javax.swing.JTextField jTextFieldDue;
    private javax.swing.JTextField jTextFieldDueDisable;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldPaid1;
    private javax.swing.JTextField jTextFieldSearchCellNo;
    private javax.swing.JTextField jTextFieldSingleReference;
    private javax.swing.JTextField jTextFieldTotalCost1;
    // End of variables declaration//GEN-END:variables
}
