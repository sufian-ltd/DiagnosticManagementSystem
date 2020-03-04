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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


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
    DefaultTableModel modelDoctor;
    DefaultTableModel modelDoctor1;
    Connection con = null;
    PreparedStatement pstmt = null;
    String patientInfo[];
    int c;
    long d=0;
    int costs[] = {200, 300, 350, 1000, 290, 450, 500, 340, 100, 900, 400, 350};
    JCheckBox []box;
    boolean dicountButtonPressed=false;
    String []tested=new String[12];
    String ref="";
    boolean isNew = true;
    int selectedRow = -1;
    Set<String> set=new HashSet<String>();
    public JFrameHome() {
        initComponents();
        getContentPane().setBackground(Color.BLACK);
        modelPatient = (DefaultTableModel) jTablePatient.getModel();
        modeltest = (DefaultTableModel) jTableTests.getModel();
        modelDoctor = (DefaultTableModel) jTableDoctor.getModel();
        modelDoctor1 = (DefaultTableModel) jTableDoctor1.getModel();
        patientInfo = new String[8];
        box=new JCheckBox[12];
        AutoCompleteDecorator.decorate(jComboBoxRef);
        AutoCompleteDecorator.decorate(jComboBoxDoctor);
        AutoCompleteDecorator.decorate(jComboBoxCell);
        initializeTestInfo();
        dateCalculation();
    }
    
    public void dateCalculation()
    {
        
        Calendar cal;
        cal = Calendar.getInstance();
        SimpleDateFormat sdf;   
        sdf = new SimpleDateFormat();
        jLabel3.setText(sdf.format(cal.getTime()));
        displayData();
        displayDoctorInfo();
    }
    public void initializeTestInfo() 
    {
        box[0]=jCheckBoxImmunologyTests;
        box[1]=jCheckBoxKidneyBloodTests;
        box[2]=jCheckBoxInfectiousDiseaseBloodTests;
        box[3]=jCheckBoxLiverFunctionPanelsEnzymeTests;
        box[4]=jCheckBoxMineralDeficiencyTests;
        box[5]=jCheckBoxSTDBloodTests;
        box[6]=jCheckBoxStressAdrenalFatigueTests;
        box[7]=jCheckBoxWomenSpecificTests;
        box[8]=jCheckBoxDiabetesBloodTest;
        box[9]=jCheckBoxDigestiveSystemTests;
        box[10]=jCheckBoxHormoneTests;
        box[11]=jCheckBoxBloodTest;
    }
    public void initializePatientInfo() {
        patientInfo[0] = jTextFieldName.getText();
        patientInfo[1] = jTextFieldCellNo.getText();
        patientInfo[2] = jTextFieldAge.getText();
        patientInfo[3] = jTextFieldAddress.getText();
        patientInfo[4] = jComboBoxGender.getSelectedItem().toString();
        patientInfo[5]=jLabel3.getText();
        if(ref.equals(""))
            patientInfo[6] = jComboBoxRef.getSelectedItem().toString();
        else
            patientInfo[6]=ref;
        patientInfo[7]=jTextFieldDueDis.getText();
    }
    
    public void setInfoAfterSearch(Object[] ob)
    {
        jTextFieldName.setText((String) ob[0]);
        jTextFieldCellNo.setText((String) ob[1]);
        jTextFieldAge.setText((String) ob[2]);
        jTextFieldAddress.setText((String) ob[3]);
        jComboBoxGender.setSelectedItem(ob[4]);
        jComboBoxRef.setSelectedItem((String) ob[5]);
        jTextFieldDueDis.setText((String) ob[6]);
    }
    
    public void displayData()
    {
        try
        {
            jComboBoxRef.removeAllItems();
        while(modelPatient.getRowCount()>0){
             modelPatient.removeRow(0);
         }
        while(modeltest.getRowCount()>0){
             modeltest.removeRow(0);
         }
        try
        {
         
            
            con =(Connection) DriverManager.getConnection(diagnosticmanagementsystem.DB_Connection.db_url, 
               diagnosticmanagementsystem.DB_Connection.db_userName, 
               diagnosticmanagementsystem.DB_Connection.db_password);
            pstmt=(PreparedStatement) con.prepareStatement(""
               + "select * from patient");
            ResultSet rst=pstmt.executeQuery();    
            while(rst.next()){ 
            Object[] obj=new Object[8];
            obj[0]=rst.getString("Patient_Name");
            obj[1]=rst.getString("Cell_No");
            jComboBoxCell.addItem(obj[1]);
            obj[2]=rst.getString("Age");
            obj[3]=rst.getString("Address");
            obj[4]=rst.getString("Sex");
            obj[5]=rst.getString("Date");
            obj[6]=rst.getString("Reference");
            set.add((String) obj[6]);
            //jComboBoxRef.addItem(obj[6]);
            obj[7]=rst.getString("Due");
            modelPatient.addRow(obj);
            }
            for(String s : set)
                jComboBoxRef.addItem(s);
            pstmt=(PreparedStatement) con.prepareStatement(""
               + "select * from test");
            rst=pstmt.executeQuery();
            while(rst.next()){ 
            Object[] obj=new Object[13];
            obj[0]=rst.getString("Cell_No");
            obj[1]=rst.getString("Immunology_Tests");
            obj[2]=rst.getString("Kidney_Blood_Tests");
            obj[3]=rst.getString("Infectious_Disease_Blood_Tests");
            obj[4]=rst.getString("Liver_Function_Panels_Enzyme_Tests");
            obj[5]=rst.getString("Mineral_Deficiency_Tests");
            obj[6]=rst.getString("STD_Blood_Tests");
            obj[7]=rst.getString("Stress_Adrenal_Fatigue_Tests");
            obj[8]=rst.getString("Women_Specific_Tests");
            obj[9]=rst.getString("Diabetes_Blood_Test");
            obj[10]=rst.getString("Digestive_System_Tests");
            obj[11]=rst.getString("Hormone_Tests");
            obj[12]=rst.getString("Blood_Tests");
            modeltest.addRow(obj);
            }
        }
        catch(SQLException ex){
          JOptionPane.showMessageDialog(null, "Error occured due to:\n"+ex.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
        }
        finally{
            try
            {
                if(pstmt!=null) pstmt.close();
                if(con!=null)con.close();
            }
            catch(SQLException ex)
            {
                 JOptionPane.showMessageDialog(null, "Error occured due to:\n"+ex.getMessage(),
                         "Error!",JOptionPane.ERROR_MESSAGE);
            }
        }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    public void searchData()
    {
        try
        {
         
         con =(Connection) DriverManager.getConnection(diagnosticmanagementsystem.DB_Connection.db_url,
                 diagnosticmanagementsystem.DB_Connection.db_userName,
                 diagnosticmanagementsystem.DB_Connection.db_password);
       //pstmt=(PreparedStatement) con.prepareStatement("select Patient_Name,Cell_No,Age,Address,Sex,Reference,Due where Cell_No like '%" + jTextFieldSearchCellNo.getText() + "%'");
         pstmt=(PreparedStatement) con.prepareStatement("select Patient_Name,Cell_No,Age,Address,Sex,"
                 + "Reference,Due from patient where Cell_No='"+ jComboBoxCell.getSelectedItem().toString() +"' ");
         
       ResultSet rst=pstmt.executeQuery();   
       Object[]obj=new Object[7];
      while(rst.next()){ 
      obj[0]=rst.getString("Patient_Name");
      obj[1]=rst.getString("Cell_No");
      obj[2]=rst.getString("Age");
      obj[3]=rst.getString("Address");
      obj[4]=rst.getString("Sex");
      obj[5]=rst.getString("Reference");
      obj[6]=rst.getString("Due");
      
            }
      setInfoAfterSearch(obj);
      pstmt=(PreparedStatement) con.prepareStatement("select Cell_No,Immunology_Tests,Kidney_Blood_Tests,"
              + "Infectious_Disease_Blood_Tests,Liver_Function_Panels_Enzyme_Tests,Mineral_Deficiency_Tests,"
              + "STD_Blood_Tests,Stress_Adrenal_Fatigue_Tests,Women_Specific_Tests,Diabetes_Blood_Test,"
              + "Digestive_System_Tests,Hormone_Tests,"
              + "Blood_Tests from test where Cell_No='"+ jComboBoxCell.getSelectedItem().toString() +"' ");
         
      ResultSet rst2=pstmt.executeQuery();   
      Object[] obj2=new Object[13];
      while(rst2.next()){ 
      
      obj2[0]=rst2.getString("Cell_No");
      obj2[1]=rst2.getString("Immunology_Tests");
      obj2[2]=rst2.getString("Kidney_Blood_Tests");
      obj2[3]=rst2.getString("Infectious_Disease_Blood_Tests");
      obj2[4]=rst2.getString("Liver_Function_Panels_Enzyme_Tests");
      obj2[5]=rst2.getString("Mineral_Deficiency_Tests");
      obj2[6]=rst2.getString("STD_Blood_Tests");
      obj2[7]=rst2.getString("Stress_Adrenal_Fatigue_Tests");
      obj2[8]=rst2.getString("Women_Specific_Tests");
      obj2[9]=rst2.getString("Diabetes_Blood_Test");
      obj2[10]=rst2.getString("Digestive_System_Tests");
      obj2[11]=rst2.getString("Hormone_Tests");
      obj2[12]=rst2.getString("Blood_Tests");
      
            }
      for(int i=1;i<13;i++)
      {
          if(obj2[i].equals("tested"))
          {
              box[i-1].setSelected(true);
          }
          else
              box[i-1].setSelected(false);
      }
        }
    catch(SQLException ex){
      JOptionPane.showMessageDialog(null, "Error occured due to:\n"+ex.getMessage(),"Error!",
              JOptionPane.ERROR_MESSAGE);
    }
    finally
        {
        try{
                if(pstmt!=null) pstmt.close();
                if(con!=null)con.close();
            }
        catch(SQLException ex){
             JOptionPane.showMessageDialog(null, "Error occured due to:\n"+ex.getMessage(),
                     "Error!",JOptionPane.ERROR_MESSAGE);
        
    }
    }
    }
    
    public void displayDoctorInfo()
    {
        try
        {
            jComboBoxDoctor.removeAllItems();
            jComboBoxRef.removeAllItems();
        while(modelDoctor.getRowCount()>0){
             modelDoctor.removeRow(0);
         }
        while(modelDoctor1.getRowCount()>0){
             modelDoctor1.removeRow(0);
         }
        try
        {
         
            
            con =(Connection) DriverManager.getConnection(diagnosticmanagementsystem.DB_Connection.db_url, 
               diagnosticmanagementsystem.DB_Connection.db_userName, 
               diagnosticmanagementsystem.DB_Connection.db_password);
            pstmt=(PreparedStatement) con.prepareStatement(""
               + "select * from doctor_information");
            ResultSet rst=pstmt.executeQuery();    
       
            while(rst.next()){ 
            Object[] obj=new Object[3];
            obj[0]=rst.getString("Doctor_Name");
            obj[1]=rst.getString("Qualification");
            obj[2]=rst.getString("Cell_No");
            set.add((String) obj[0]);
            jComboBoxDoctor.addItem(obj[0]);
            modelDoctor.addRow(obj);
            modelDoctor1.addRow(obj);
            }
            for(String s : set)
                jComboBoxRef.addItem(s);
        }
        catch(SQLException ex){
          JOptionPane.showMessageDialog(null, "Error occured due to:\n"+ex.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
        }
        finally{
            try
            {
                if(pstmt!=null) pstmt.close();
                if(con!=null)con.close();
            }
            catch(SQLException ex)
            {
                 JOptionPane.showMessageDialog(null, "Error occured due to:\n"+ex.getMessage(),
                         "Error!",JOptionPane.ERROR_MESSAGE);
            }
        }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    public void insertDataInDatabase()
    {
            try {
                
                /**
                 * insert information
                 */
                patientInfo[7]=jTextFieldDue.getText();
                con = (Connection) DriverManager.getConnection(diagnosticmanagementsystem.DB_Connection.db_url, diagnosticmanagementsystem.DB_Connection.db_userName, diagnosticmanagementsystem.DB_Connection.db_password);
                pstmt = (PreparedStatement) con.prepareStatement("insert into  patient (Patient_Name,"
                        + "Cell_No,Age,Address,Sex,date,Reference,Due)"
                       + "values(?,?,?,?,?,?,?,?) ");
                pstmt.setString(1, patientInfo[0]);
                pstmt.setString(2, patientInfo[1]);
                pstmt.setString(3, patientInfo[2]);
                pstmt.setString(4, patientInfo[3]);
                pstmt.setString(5, patientInfo[4]);
                pstmt.setString(6, patientInfo[5]);
                pstmt.setString(7, patientInfo[6]);
                pstmt.setString(8, patientInfo[7]);
                int isOk = 0;
                isOk = pstmt.executeUpdate();
                /**
                 * insert tests
                 */
                pstmt = (PreparedStatement) con.prepareStatement("insert into  test(Cell_No,Immunology_Tests,"
                        + "Kidney_Blood_Tests,Infectious_Disease_Blood_Tests,Liver_Function_Panels_Enzyme_Tests,"
                        + "Mineral_Deficiency_Tests,STD_Blood_Tests,Stress_Adrenal_Fatigue_Tests,Women_Specific_Tests,"
                        + "Diabetes_Blood_Test,Digestive_System_Tests,Hormone_Tests,Blood_Tests)"
                        + "values(?,?,?,?,?,?,?,?,?,?,?,?,?) ");
                pstmt.setString(1, patientInfo[1]);
                pstmt.setString(2, tested[0]);
                pstmt.setString(3, tested[1]);
                pstmt.setString(4, tested[2]);
                pstmt.setString(5, tested[3]);
                pstmt.setString(6, tested[4]);
                pstmt.setString(7, tested[5]);
                pstmt.setString(8, tested[6]);
                pstmt.setString(9, tested[7]);
                pstmt.setString(10, tested[8]);
                pstmt.setString(11, tested[9]);
                pstmt.setString(12, tested[10]);
                pstmt.setString(13, tested[11]);
                isOk = 0;
                isOk = pstmt.executeUpdate();
                if (isOk > 0) {
                    JOptionPane.showMessageDialog(null, "Successfully Inserted", "Confirmation",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "NOT Inserted", "Confirmation",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                c=0;
                dicountButtonPressed=false;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error occured due to:\n" + ex.getMessage(), 
                        "Error!", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error occured due to:\n" + ex.getMessage(), 
                            "Error!", JOptionPane.ERROR_MESSAGE);

                }
            }
            displayData();
            displayDoctorInfo();
    }
    
    public void clearData() {
        jTextFieldName.setText(null);
        jTextFieldCellNo.setText(null);
        jTextFieldAge.setText(null);
        jTextFieldAddress.setText(null);
        jComboBoxGender.getSelectedItem().toString();
        jComboBoxRef.setSelectedItem(null);
        jTextFieldName.requestFocus();
        jTextFieldDueDis.setText(null);
        for(int i=0;i<8;i++)
        {
            patientInfo[i]="";
        }
        for(int i=0;i<12;i++)
        {
            box[i].setSelected(false);
        }
        d=0;
        c=0;
        dicountButtonPressed=false;
        jTextFieldDiscount1.setText(null);
        jTextFieldTotalCost1.setText(null);
        //jTextFieldSearchCellNo.setText(null);
        jTextFieldPaid1.setText(null);
    }
    
    public void checkInfoAndTest()
    {
        initializePatientInfo();
        c=0;
        for(int i=0;i<7;i++)
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
    
             
    void updateData()
    {
        try
        {
         checkInfoAndTest();
         con =(Connection) DriverManager.getConnection(diagnosticmanagementsystem.DB_Connection.db_url,
                 diagnosticmanagementsystem.DB_Connection.db_userName,
                 diagnosticmanagementsystem.DB_Connection.db_password);
         pstmt=(PreparedStatement) con.prepareStatement("update patient set Patient_Name=?,Cell_No=?,"
                 + "Age=?,Address=?,Sex=?,"
                 + "Reference=?,Due=? where Cell_No='"+ jComboBoxCell.getSelectedItem().toString() +"' ");
         pstmt.setString(1, patientInfo[0]);
                pstmt.setString(2, jComboBoxCell.getSelectedItem().toString());
                pstmt.setString(3, patientInfo[2]);
                pstmt.setString(4, patientInfo[3]);
                pstmt.setString(5, patientInfo[4]);
                pstmt.setString(6, patientInfo[6]);
                pstmt.setString(7, patientInfo[7]);
                int isOk = 0;
                isOk = pstmt.executeUpdate();
      pstmt=(PreparedStatement) con.prepareStatement("update test set Cell_No=?,Immunology_Tests=?,Kidney_Blood_Tests=?,"
              + "Infectious_Disease_Blood_Tests=?,Liver_Function_Panels_Enzyme_Tests=?,Mineral_Deficiency_Tests=?,"
              + "STD_Blood_Tests=?,Stress_Adrenal_Fatigue_Tests=?,Women_Specific_Tests=?,Diabetes_Blood_Test=?,"
              + "Digestive_System_Tests=?,Hormone_Tests=?,Blood_Tests=? where "
              + "Cell_No='"+ jComboBoxCell.getSelectedItem().toString() +"' ");
         
      pstmt.setString(1, jComboBoxCell.getSelectedItem().toString());
        pstmt.setString(2, tested[0]);
        pstmt.setString(3, tested[1]);
        pstmt.setString(4, tested[2]);
        pstmt.setString(5, tested[3]);
        pstmt.setString(6, tested[4]);
        pstmt.setString(7, tested[5]);
        pstmt.setString(8, tested[6]);
        pstmt.setString(9, tested[7]);
        pstmt.setString(10, tested[8]);
        pstmt.setString(11, tested[9]);
        pstmt.setString(12, tested[10]);
        pstmt.setString(13, tested[11]);
        isOk = 0;
        isOk = pstmt.executeUpdate();
        JOptionPane.showMessageDialog(null, "Successfully update");
        displayData();
        }
    catch(SQLException ex){
      JOptionPane.showMessageDialog(null, "Error occured due to:\n"+ex.getMessage(),"Error!",
              JOptionPane.ERROR_MESSAGE);
    }
    finally{
        try{
                if(pstmt!=null) pstmt.close();
                if(con!=null)con.close();
            }
                   catch(SQLException ex){
                        JOptionPane.showMessageDialog(null, "Error occured due to:\n"+ex.getMessage(),
                                "Error!",JOptionPane.ERROR_MESSAGE);
        
    }
    }                       
    }
    
    void deleteData()
    {
        try{
         
       con =(Connection) DriverManager.getConnection(diagnosticmanagementsystem.DB_Connection.db_url, 
               diagnosticmanagementsystem.DB_Connection.db_userName, diagnosticmanagementsystem.DB_Connection.db_password);
       /*pstmt=(PreparedStatement) con.prepareStatement(""
               + "delete from patient where Cell_No='"+ jTextFieldSearchCellNo.getText() +"'");*/
        pstmt=(PreparedStatement) con.prepareStatement("delete from patient where Cell_No=?");
        pstmt.setString(1, jComboBoxCell.getSelectedItem().toString());
        pstmt.executeUpdate();    
        pstmt=(PreparedStatement) con.prepareStatement("delete from test where Cell_No=?");
        pstmt.setString(1, jComboBoxCell.getSelectedItem().toString());
        pstmt.executeUpdate(); 
        JOptionPane.showMessageDialog(null, "Successfully deleted","Successfull",JOptionPane.INFORMATION_MESSAGE);
        displayData();
        }
    catch(SQLException ex){
      JOptionPane.showMessageDialog(null, "Error occured due to:\n"+ex.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
    }
    finally{
        try{
                if(pstmt!=null) pstmt.close();
                if(con!=null)con.close();
            }
                   catch(SQLException ex){
                        JOptionPane.showMessageDialog(null, "Error occured due to:\n"+ex.getMessage(),"Error!",JOptionPane.ERROR_MESSAGE);
        
    }
    }

    }
    private void searchDoctor()
    {
        try
        {
    
        con =(Connection) DriverManager.getConnection(diagnosticmanagementsystem.DB_Connection.db_url, 
               diagnosticmanagementsystem.DB_Connection.db_userName, 
               diagnosticmanagementsystem.DB_Connection.db_password);
            pstmt=(PreparedStatement) con.prepareStatement(""
               + "select * from doctor_information where Doctor_Name=?");
            pstmt.setString(1,jComboBoxDoctor.getSelectedItem().toString());
            ResultSet rst=pstmt.executeQuery();    
            
            while(rst.next()){ 
            jTextFieldDoctorName.setText(rst.getString("Doctor_Name"));
            jTextFieldQuali.setText(rst.getString("Qualification"));
            jTextFieldDoctorCell.setText(rst.getString("Cell_No"));
            }
        }
        catch(Exception ex)
        {
            
        }
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

        jLabel4 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableDoctor = new javax.swing.JTable();
        jButtonLogIn2 = new javax.swing.JButton();
        jButtonLogIn = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jTextFieldCellNo = new javax.swing.JTextField();
        jTextFieldAge = new javax.swing.JTextField();
        jTextFieldAddress = new javax.swing.JTextField();
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
        jLabel25 = new javax.swing.JLabel();
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
        jButtonDiscount = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonRefresh = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jButtonSearch = new javax.swing.JButton();
        jButtonUpdate = new javax.swing.JButton();
        jComboBoxCell = new javax.swing.JComboBox();
        jComboBoxGender = new javax.swing.JComboBox();
        jLabelDueDis = new javax.swing.JLabel();
        jTextFieldDueDis = new javax.swing.JTextField();
        jComboBoxRef = new javax.swing.JComboBox();
        jButtonAddRef = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePatient = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableTests = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jButtonSave1 = new javax.swing.JButton();
        jButtonDelete1 = new javax.swing.JButton();
        jButtonRefresh1 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jButtonSearch1 = new javax.swing.JButton();
        jButtonUpdate1 = new javax.swing.JButton();
        jComboBoxDoctor = new javax.swing.JComboBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableDoctor1 = new javax.swing.JTable();
        jLabel26 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldDoctorName = new javax.swing.JTextField();
        jTextFieldQuali = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldDoctorCell = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();

        jLabel4.setText("jLabel4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Digonstic MAnagement System");
        setResizable(false);

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/Hospital-lab.png"))); // NOI18N

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/Doctor-icon.png"))); // NOI18N
        jLabel13.setText("Available Doctors");

        jTableDoctor.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTableDoctor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Doctor_Name", "Qualifications", "Cell_No"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTableDoctor);

        jButtonLogIn2.setBackground(new java.awt.Color(255, 0, 51));
        jButtonLogIn2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButtonLogIn2.setForeground(new java.awt.Color(255, 255, 255));
        jButtonLogIn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/close.png"))); // NOI18N
        jButtonLogIn2.setText("Close");
        jButtonLogIn2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonLogIn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogIn2ActionPerformed(evt);
            }
        });

        jButtonLogIn.setBackground(new java.awt.Color(0, 153, 153));
        jButtonLogIn.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButtonLogIn.setForeground(new java.awt.Color(0, 0, 0));
        jButtonLogIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/sign.png"))); // NOI18N
        jButtonLogIn.setText("Back to Log In");
        jButtonLogIn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonLogIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogInActionPerformed(evt);
            }
        });

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/logo.png"))); // NOI18N
        jLabel27.setText("jLabel27");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(656, Short.MAX_VALUE)
                .addComponent(jLabel23)
                .addGap(385, 385, 385))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButtonLogIn2, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonLogIn, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(140, 140, 140))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonLogIn)
                            .addComponent(jButtonLogIn2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(jLabel23)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Home", jPanel2);

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setText("Reference");

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

        jTextFieldName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextFieldName.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextFieldCellNo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextFieldCellNo.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextFieldAge.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextFieldAge.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextFieldAddress.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextFieldAddress.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jCheckBoxBloodTest.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBoxBloodTest.setText("1 . Blood Tests (200 BDT)");

        jCheckBoxImmunologyTests.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBoxImmunologyTests.setText("2 . Immunology Tests (300 BDT)");

        jCheckBoxInfectiousDiseaseBloodTests.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBoxInfectiousDiseaseBloodTests.setText("4. Infectious Disease Blood Tests (1000 BDT )");

        jCheckBoxKidneyBloodTests.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBoxKidneyBloodTests.setText("3. Kidney Blood Tests (350 BDT)");

        jCheckBoxDiabetesBloodTest.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBoxDiabetesBloodTest.setText("10. Diabetes Blood Test(BDT 900)");

        jCheckBoxMineralDeficiencyTests.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBoxMineralDeficiencyTests.setText("6. Mineral Deficiency Tests ( BDT 450)");

        jCheckBoxLiverFunctionPanelsEnzymeTests.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBoxLiverFunctionPanelsEnzymeTests.setText("5.  Liver Function Panels & Enzyme Tests (290 BDT)");

        jCheckBoxWomenSpecificTests.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBoxWomenSpecificTests.setText("9. Women Specific Tests (BDT 100)");

        jCheckBoxStressAdrenalFatigueTests.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBoxStressAdrenalFatigueTests.setText("8. Stress & Adrenal Fatigue Tests(BDT 340)");

        jCheckBoxSTDBloodTests.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBoxSTDBloodTests.setText("7. STD Blood Tests( BDT 500 )");

        jCheckBoxDigestiveSystemTests.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBoxDigestiveSystemTests.setText("11. Digestive System Tests(BDT 400)");

        jCheckBoxHormoneTests.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBoxHormoneTests.setText("12. Hormone Tests (BDT 350)");

        jLabel25.setFont(new java.awt.Font("Perpetua Titling MT", 1, 24)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/Search-icon.png"))); // NOI18N
        jLabel25.setText("List of Available Tests");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxBloodTest)
                            .addComponent(jCheckBoxImmunologyTests)
                            .addComponent(jCheckBoxKidneyBloodTests)
                            .addComponent(jCheckBoxInfectiousDiseaseBloodTests)
                            .addComponent(jCheckBoxLiverFunctionPanelsEnzymeTests))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jCheckBoxMineralDeficiencyTests, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jCheckBoxHormoneTests, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBoxWomenSpecificTests)
                    .addComponent(jCheckBoxDigestiveSystemTests)
                    .addComponent(jCheckBoxDiabetesBloodTest)
                    .addComponent(jCheckBoxStressAdrenalFatigueTests, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBoxSTDBloodTests))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
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

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText(" Total Cost");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Discount");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Paid");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Due");

        jTextFieldTotalCost1.setEditable(false);

        jTextFieldDiscount1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextFieldDiscount1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldDiscount1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldDiscount1KeyPressed(evt);
            }
        });

        jTextFieldPaid1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextFieldPaid1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextFieldDue.setEditable(false);

        jButtonTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonTotal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/Update.png"))); // NOI18N
        jButtonTotal.setText("Total");
        jButtonTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTotalActionPerformed(evt);
            }
        });

        jButtonDiscount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonDiscount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/AddPluse.png"))); // NOI18N
        jButtonDiscount.setText("add discount");
        jButtonDiscount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDiscountActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jTextFieldTotalCost1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jTextFieldDue)))
                .addGap(21, 21, 21)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(143, 143, 143)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextFieldDiscount1, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                        .addComponent(jTextFieldPaid1, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                    .addContainerGap(151, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldTotalCost1, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonDiscount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jButtonTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldDue, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(50, 50, 50)
                    .addComponent(jTextFieldDiscount1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTextFieldPaid1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(54, 54, 54)))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/sign.png"))); // NOI18N
        jButtonSave.setText("Save");
        jButtonSave.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jButtonDelete.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/Delete.png"))); // NOI18N
        jButtonDelete.setText("Delete");
        jButtonDelete.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jButtonRefresh.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/refresh.png"))); // NOI18N
        jButtonRefresh.setText("Refresh");
        jButtonRefresh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel15.setText("Enter Cel No :");

        jButtonSearch.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/Search-icon.png"))); // NOI18N
        jButtonSearch.setText("Search");
        jButtonSearch.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });

        jButtonUpdate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/Update.png"))); // NOI18N
        jButtonUpdate.setText("Update");
        jButtonUpdate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateActionPerformed(evt);
            }
        });

        jComboBoxCell.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap(14, Short.MAX_VALUE)
                        .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jComboBoxCell, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                    .addComponent(jButtonSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxCell, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(jButtonRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSave))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jComboBoxGender.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jComboBoxGender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Male", "Female" }));

        jLabelDueDis.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelDueDis.setText("Due");

        jTextFieldDueDis.setEditable(false);
        jTextFieldDueDis.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextFieldDueDis.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldDueDis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDueDisActionPerformed(evt);
            }
        });

        jComboBoxRef.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jButtonAddRef.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonAddRef.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/AddPluse.png"))); // NOI18N
        jButtonAddRef.setText("Add");
        jButtonAddRef.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButtonAddRef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddRefActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel14)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelDueDis))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldName, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                            .addComponent(jTextFieldAge, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                            .addComponent(jTextFieldCellNo, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                            .addComponent(jTextFieldAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                            .addComponent(jComboBoxGender, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldDueDis)
                            .addComponent(jComboBoxRef, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAddRef, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(22, 22, 22))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jTextFieldCellNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jTextFieldAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jTextFieldAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBoxGender)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jComboBoxRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonAddRef))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelDueDis)
                            .addComponent(jTextFieldDueDis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Single Patient Information", jPanel4);

        jPanel5.setBackground(new java.awt.Color(204, 204, 255));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/Update.png"))); // NOI18N
        jLabel16.setText("Lab Tests");

        jTablePatient.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
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
                "Patient Name", "Cell No", "Age", "Adress", "Sex", "Date", "Reference", "Due"
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

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/Update.png"))); // NOI18N
        jLabel17.setText("All Patient Personal Information");

        jTableTests.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
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
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Cell No", "Immonulogy", "Kidney Blood", "Infectious Disease Blood ", "Liver Function Panels & Enzyme ", "Mineral Deficiency ", "STD Blood ", "Stress & Adrenal Fatigue", "Women Specific", "Diabetes Blood", "Digestive System", "Hormone", "Blood"
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(392, 392, 392)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(386, 386, 386)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 353, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("All Patient Information", jPanel5);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonSave1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonSave1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/Save.png"))); // NOI18N
        jButtonSave1.setText("Save");
        jButtonSave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSave1ActionPerformed(evt);
            }
        });

        jButtonDelete1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonDelete1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/Delete.png"))); // NOI18N
        jButtonDelete1.setText("Delete");
        jButtonDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDelete1ActionPerformed(evt);
            }
        });

        jButtonRefresh1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonRefresh1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/refresh.png"))); // NOI18N
        jButtonRefresh1.setText("Refresh");
        jButtonRefresh1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefresh1ActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel24.setText("Enter Doctor Name");

        jButtonSearch1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonSearch1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/Search-icon.png"))); // NOI18N
        jButtonSearch1.setText("Search");
        jButtonSearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearch1ActionPerformed(evt);
            }
        });

        jButtonUpdate1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButtonUpdate1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/Update.png"))); // NOI18N
        jButtonUpdate1.setText("Update");
        jButtonUpdate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdate1ActionPerformed(evt);
            }
        });

        jComboBoxDoctor.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jButtonSave1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonUpdate1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDelete1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBoxDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonRefresh1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonSearch1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxDoctor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonUpdate1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonSave1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonDelete1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButtonRefresh1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTableDoctor1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTableDoctor1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Doctor_Name", "Qualifications", "Cell_No"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableDoctor1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDoctor1MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTableDoctor1);

        jLabel26.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/Doctor-icon.png"))); // NOI18N
        jLabel26.setText("Available Doctors");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Enter Doctor Name : ");

        jTextFieldDoctorName.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jTextFieldDoctorName.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jTextFieldQuali.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jTextFieldQuali.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Enter Qualification : ");

        jTextFieldDoctorCell.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jTextFieldDoctorCell.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Enter Cell No : ");

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/diagnosticmanagementsystem/Groups-Meeting-Light-icon.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldDoctorCell, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldQuali, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldDoctorName, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(35, 35, 35)
                        .addComponent(jLabel7)))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel26)
                        .addGap(128, 173, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel2)
                    .addComponent(jTextFieldDoctorName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(jTextFieldDoctorCell, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel7))
                        .addGap(29, 29, 29)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextFieldQuali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(21, Short.MAX_VALUE))))
        );

        jTabbedPane1.addTab("Doctor Information", jPanel1);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("date");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("DIAGONOTIC CENTER");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(151, 151, 151)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18))
                .addGap(11, 11, 11)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldDueDisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDueDisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldDueDisActionPerformed

    private void jButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateActionPerformed
        // TODO add your handling code here:
        updateData();
    }//GEN-LAST:event_jButtonUpdateActionPerformed

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
        // TODO add your handling code here:
        try
        {
            if(!jComboBoxCell.getSelectedItem().toString().equals(""))
            searchData();
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Invalid input..!!","WRONG",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshActionPerformed
        // TODO add your handling code here:
        clearData();
    }//GEN-LAST:event_jButtonRefreshActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        // TODO add your handling code here:
        if(!jComboBoxCell.getSelectedItem().toString().equals(""))
        {
            int a=JOptionPane.showConfirmDialog(null, "Are You Sure want to delete ?","Delete",1, JOptionPane.QUESTION_MESSAGE);
            if(a==0)
            {
                deleteData();
            }
        }
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        // TODO add your handling code here:
        checkInfoAndTest();
        dicountButtonPressed=false;
        jTextFieldDiscount1.setText(null);
        jTextFieldPaid1.setText(null);
        jTextFieldDue.setText(null);
        d=0;
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTotalActionPerformed
        // TODO add your handling code here:
        if(c>0 && !jTextFieldTotalCost1.getText().equals("") && !jTextFieldPaid1.getText().equals(""))
        {
            try
            {
                long e=Integer.parseInt(jTextFieldPaid1.getText());
                if(!jTextFieldDiscount1.getText().equals(""))
                {

                    if(e<=d)
                    jTextFieldDue.setText((d-e)+"");
                    else
                    {
                        JOptionPane.showMessageDialog(null, "invalid payment","WARNING",JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                insertDataInDatabase();
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }

        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please enter new data \nor Pressed refesh button\nthen input data",
                "",JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButtonTotalActionPerformed

    private void insertNewDoctor()
    {
        try {
                
            /**
             * insert information
             */
            if(!jTextFieldDoctorName.getText().equals("") && !jTextFieldQuali.getText().equals("") &&
                    !jTextFieldDoctorCell.getText().equals(""))
            {
            con = (Connection) DriverManager.getConnection(diagnosticmanagementsystem.DB_Connection.db_url, diagnosticmanagementsystem.DB_Connection.db_userName, diagnosticmanagementsystem.DB_Connection.db_password);
            pstmt = (PreparedStatement) con.prepareStatement("insert into  doctor_information (Doctor_Name,"
                    + "Qualification,Cell_No)"
                   + "values(?,?,?) ");
            pstmt.setString(1, jTextFieldDoctorName.getText());
            pstmt.setString(2, jTextFieldQuali.getText());
            pstmt.setString(3, jTextFieldDoctorCell.getText());
            int isOk = 0;
            isOk = pstmt.executeUpdate();
            if (isOk > 0) {
                JOptionPane.showMessageDialog(null, "Successfully Inserted", "Confirmation",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "NOT Inserted", "Confirmation",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
            else
            {
                JOptionPane.showMessageDialog(null, "Please enter data");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error occured due to:\n" + ex.getMessage(), 
                    "Error!", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error occured due to:\n" + ex.getMessage(), 
                        "Error!", JOptionPane.ERROR_MESSAGE);

            }
        }
        displayDoctorInfo();
    }
    
    private void jButtonAddRefActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddRefActionPerformed
        // TODO add your handling code here:
        
        ref=JOptionPane.showInputDialog("Please Enter Reference = ");
        jComboBoxRef.setSelectedItem(ref);
    }//GEN-LAST:event_jButtonAddRefActionPerformed

    private void jButtonSave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSave1ActionPerformed
        // TODO add your handling code here:
        insertNewDoctor();
    }//GEN-LAST:event_jButtonSave1ActionPerformed

    private void jButtonDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDelete1ActionPerformed
        // TODO add your handling code here:
        int isYes = JOptionPane.showConfirmDialog(null, "Are you sure?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (isYes == JOptionPane.YES_OPTION) {
        deleteDataFromDoctor();
        }
    }//GEN-LAST:event_jButtonDelete1ActionPerformed

    private void jButtonRefresh1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefresh1ActionPerformed
        // TODO add your handling code here:
        displayDoctorInfo();
        clearDoctorField();
    }//GEN-LAST:event_jButtonRefresh1ActionPerformed

    private void jButtonSearch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearch1ActionPerformed
        // TODO add your handling code here:
        searchDoctor();
    }//GEN-LAST:event_jButtonSearch1ActionPerformed

    private void jButtonUpdate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdate1ActionPerformed
        // TODO add your handling code here:
        updateDataDoctor();
    }//GEN-LAST:event_jButtonUpdate1ActionPerformed
    
    public void updateDataDoctor() {
        if (jTextFieldDoctorName.getText().equals("") && jTextFieldDoctorCell.getText().equals("") && 
                jTextFieldQuali.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please enter Data.", "ERROR", JOptionPane.WARNING_MESSAGE);
            jTextFieldName.requestFocus();
            return;
        }
        try {

            con = (Connection) DriverManager.getConnection(diagnosticmanagementsystem.DB_Connection.db_url, 
                    diagnosticmanagementsystem.DB_Connection.db_userName, diagnosticmanagementsystem.DB_Connection.db_password);
            pstmt = (PreparedStatement) con.prepareStatement("update doctor_information set Doctor_Name=?, Qualification=?,"
                    + "Cell_No=? where Doctor_Name=?");
            pstmt.setString(1, jTextFieldDoctorName.getText());
            pstmt.setString(2, jTextFieldQuali.getText());
            pstmt.setString(3, jTextFieldDoctorCell.getText());
            pstmt.setString(4, jComboBoxDoctor.getSelectedItem().toString());

            int isOk = 0;
            isOk = pstmt.executeUpdate();
            if (isOk > 0) {
                JOptionPane.showMessageDialog(null, "Successfully Update", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "NOT Update", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
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
        clearDoctorField();
    }
    void clearDoctorField()
    {
        jTextFieldDoctorName.setText(null);
        jTextFieldQuali.setText(null);
        jTextFieldDoctorCell.setText(null);
        jComboBoxDoctor.setSelectedItem(null);
    }
    public void deleteDataFromDoctor() {
        if (jComboBoxDoctor.getSelectedItem().toString().equals("")) {
            JOptionPane.showMessageDialog(null, "Please Select Doctor  Name.", "ERROR", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {

            con = (Connection) DriverManager.getConnection(diagnosticmanagementsystem.DB_Connection.db_url,
                    
                    diagnosticmanagementsystem.DB_Connection.db_userName,
                    diagnosticmanagementsystem.DB_Connection.db_password);
            pstmt = (PreparedStatement) con.prepareStatement
        ("delete from doctor_information where Doctor_Name=?");
            pstmt.setString(1, jComboBoxDoctor.getSelectedItem().toString());
            int isOk = 0;
            isOk = pstmt.executeUpdate();
            if (isOk > 0) {
                JOptionPane.showMessageDialog(null, "Successfully Delete", "Confirmation",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "NOT Delete", "Confirmation",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error occured due to:\n" + ex.getMessage(), "Error!",
                    JOptionPane.ERROR_MESSAGE);
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
        clearDoctorField();
    }
    private void jTableDoctor1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDoctor1MouseClicked
        // TODO add your handling code here:
        isNew = false;
        selectedRow = jTableDoctor1.getSelectedRow();
        if (selectedRow >= 0) {

            jTextFieldDoctorName.setText(modelDoctor1.getValueAt(selectedRow, 0).toString());
            jTextFieldQuali.setText(modelDoctor1.getValueAt(selectedRow, 1).toString());
            jTextFieldDoctorCell.setText(modelDoctor1.getValueAt(selectedRow, 2).toString());
            jComboBoxDoctor.setSelectedItem(modelDoctor1.getValueAt(selectedRow, 0).toString());
        }
    }//GEN-LAST:event_jTableDoctor1MouseClicked

    private void jTextFieldDiscount1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDiscount1KeyPressed
        // TODO add your handling code here:
        if(!dicountButtonPressed && !jTextFieldDiscount1.getText().equals(""))
        {
            try
            {
                long a,b=0;
                a=Integer.parseInt(jTextFieldTotalCost1.getText());
                b=Integer.parseInt(jTextFieldDiscount1.getText());
                d=a-((b*a)/100);
                jTextFieldTotalCost1.setText(d+"");
                dicountButtonPressed=true;
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(null, "Invalid input..!!","WRONG",JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTextFieldDiscount1KeyPressed

    private void jButtonDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDiscountActionPerformed
        // TODO add your handling code here:
        if(!dicountButtonPressed && !jTextFieldDiscount1.getText().equals(""))
        {
            try
            {
                long a,b=0;
                a=Integer.parseInt(jTextFieldTotalCost1.getText());
                b=Integer.parseInt(jTextFieldDiscount1.getText());
                d=a-((b*a)/100);
                jTextFieldTotalCost1.setText(d+"");
                dicountButtonPressed=true;
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(null, "Invalid input..!!","WRONG",JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButtonDiscountActionPerformed

    private void jButtonLogIn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogIn2ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jButtonLogIn2ActionPerformed

    private void jButtonLogInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogInActionPerformed
        // TODO add your handling code here:
        JFrameLogin frame = new JFrameLogin();
        this.setVisible(false);
        frame.setVisible(true);
    }//GEN-LAST:event_jButtonLogInActionPerformed

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
    private javax.swing.JButton jButtonAddRef;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonDelete1;
    private javax.swing.JButton jButtonDiscount;
    private javax.swing.JButton jButtonLogIn;
    private javax.swing.JButton jButtonLogIn2;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JButton jButtonRefresh1;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSave1;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JButton jButtonSearch1;
    private javax.swing.JButton jButtonTotal;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JButton jButtonUpdate1;
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
    private javax.swing.JComboBox jComboBoxCell;
    private javax.swing.JComboBox jComboBoxDoctor;
    private javax.swing.JComboBox jComboBoxGender;
    private javax.swing.JComboBox jComboBoxRef;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelDueDis;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableDoctor;
    private javax.swing.JTable jTableDoctor1;
    private javax.swing.JTable jTablePatient;
    private javax.swing.JTable jTableTests;
    private javax.swing.JTextField jTextFieldAddress;
    private javax.swing.JTextField jTextFieldAge;
    private javax.swing.JTextField jTextFieldCellNo;
    private javax.swing.JTextField jTextFieldDiscount1;
    private javax.swing.JTextField jTextFieldDoctorCell;
    private javax.swing.JTextField jTextFieldDoctorName;
    private javax.swing.JTextField jTextFieldDue;
    private javax.swing.JTextField jTextFieldDueDis;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldPaid1;
    private javax.swing.JTextField jTextFieldQuali;
    private javax.swing.JTextField jTextFieldTotalCost1;
    // End of variables declaration//GEN-END:variables
}
