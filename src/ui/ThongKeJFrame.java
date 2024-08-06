/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

import dao.LoaiMonDAO;
import dao.ThongKeDAO;
import entity.LoaiMon;
import entity.ThongKe;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLayeredPane;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RefineryUtilities;
import entity.ThongKe.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.AreaRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author ADMIN
 */
public class ThongKeJFrame extends javax.swing.JFrame {

    LoaiMonDAO LMdao = new LoaiMonDAO();
    ThongKeDAO TKdao = new ThongKeDAO();
    SimpleDateFormat fmThoiGian = new SimpleDateFormat("dd/MM");

    /**
     * Creates new form ThongKeJFrame
     */
    public ThongKeJFrame() {
        setTitle("Hệ thống quản lý nhà hàng L'ESSALE - Thống Kê");
        initComponents();
        fillComboBoxLoaiMon();
        loadBieuDoMonAn();
        loadBieuDoDoanhThu();
        loadCboNgayLap();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cboLoaiMon = new javax.swing.JComboBox<>();
        pnlThongKe = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        clrTuNgay = new com.toedter.calendar.JDateChooser();
        clrDenNgay = new com.toedter.calendar.JDateChooser();
        pnlDoanhThu = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel2.setText("LOẠI MÓN ĂN");

        cboLoaiMon.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        cboLoaiMon.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLoaiMon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoaiMonActionPerformed(evt);
            }
        });

        pnlThongKe.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        javax.swing.GroupLayout pnlThongKeLayout = new javax.swing.GroupLayout(pnlThongKe);
        pnlThongKe.setLayout(pnlThongKeLayout);
        pnlThongKeLayout.setHorizontalGroup(
            pnlThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlThongKeLayout.setVerticalGroup(
            pnlThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 666, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboLoaiMon, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 873, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cboLoaiMon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabs.addTab("DOANH SỐ MÓN ĂN", jPanel1);

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("TỪ NGÀY");

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setText("ĐẾN NGÀY");

        clrTuNgay.setDateFormatString("dd-MM-yyyy");
        clrTuNgay.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                clrTuNgayPropertyChange(evt);
            }
        });

        clrDenNgay.setDateFormatString("dd-MM-yyyy");
        clrDenNgay.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                clrDenNgayPropertyChange(evt);
            }
        });

        pnlDoanhThu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        javax.swing.GroupLayout pnlDoanhThuLayout = new javax.swing.GroupLayout(pnlDoanhThu);
        pnlDoanhThu.setLayout(pnlDoanhThuLayout);
        pnlDoanhThuLayout.setHorizontalGroup(
            pnlDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlDoanhThuLayout.setVerticalGroup(
            pnlDoanhThuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 669, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clrTuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clrDenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 727, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlDoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clrTuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clrDenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabs.addTab("DOANH THU", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cboLoaiMonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoaiMonActionPerformed
        loadBieuDoMonAn();
    }//GEN-LAST:event_cboLoaiMonActionPerformed

    private void clrTuNgayPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_clrTuNgayPropertyChange
        // TODO add your handling code here:
        loadCboNgayLap();
    }//GEN-LAST:event_clrTuNgayPropertyChange

    private void clrDenNgayPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_clrDenNgayPropertyChange
        // TODO add your handling code here:
        loadCboNgayLap();
    }//GEN-LAST:event_clrDenNgayPropertyChange

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        new TrangChuJFrame().setVisible(true);
        dispose();
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public void selectTab(int index){
        tabs.setSelectedIndex(index);
    }
    
    void fillComboBoxLoaiMon() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboLoaiMon.getModel();
        model.removeAllElements();
        model.addElement("All");
        List<LoaiMon> list = LMdao.selectAll();
        for (LoaiMon lm : list) {
            model.addElement(lm);
        }
    }

//    void fillTableMonAn() {
//        DefaultTableModel model = (DefaultTableModel) tblMonAn.getModel();
//        model.setRowCount(0);
//        tblMonAn.setModel(model);
//        LoaiMon lm = (LoaiMon) cboLoaiMon.getSelectedItem();
//        List<Object[]> list = TKdao.getSum(Integer.parseInt(lm.getMaLoai()));
//        for (Object[] row : list) {
//            double soluong = (double) row[2];
//            model.addRow(new Object[]{
//                row[0], row[1], soluong
//            });
//        }
//    }
    
    public void loadCboNgayLap(){
        NgayLap nl = TKdao.getNgayLap();
        
        if( clrDenNgay.getDate() == null || clrTuNgay.getDate() == null){
            clrTuNgay.setSelectableDateRange(nl.getMin(), nl.getMax());
            clrDenNgay.setSelectableDateRange(nl.getMin(), nl.getMax());
            clrTuNgay.setDate(nl.getMin());
            clrDenNgay.setDate(nl.getMax());
            return;
        }
        
        
        clrTuNgay.setMaxSelectableDate(clrDenNgay.getDate());
        clrDenNgay.setMinSelectableDate(clrTuNgay.getDate());
        
        clrTuNgay.repaint();
        clrDenNgay.repaint();
        
        System.out.println("ui.ThongKeJFrame.loadCboNgayLap()");
        
        loadBieuDoDoanhThu();
    }
    
    public void loadBieuDoDoanhThu() {
        List<DoanhThu> list = TKdao.getDoanhThu(clrTuNgay.getDate(), clrDenNgay.getDate());

        double[] data = new double[]{};
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(DoanhThu dt : list){
            dataset.addValue(dt.getTongTien(), "Tổng Tiền", fmThoiGian.format(dt.getNgayLap()));
        }
        // 1. Tạo dữ liệu cho biểu đồ miền
        
        

        // 2. Tạo biểu đồ miền
        JFreeChart areaChart = ChartFactory.createAreaChart("Biểu đồ doanh thu", // Tiêu đề biểu đồ
                "Thời Gian",                        // Nhãn trục x
                "Tổng Tiền", 
                dataset,                        // Dữ liệu
                PlotOrientation.VERTICAL,       // Hướng của biểu đồ
                false,                           // Hiển thị chú thích
                true,                           // Hiển thị tooltip
                false                           // URL generation
        );

        CategoryPlot plot = (CategoryPlot) areaChart.getPlot();
        AreaRenderer renderer = new AreaRenderer();
        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator(
                "{1} - {2}đ",
                NumberFormat.getInstance()
        ));
        plot.setRenderer(renderer);
        
        // 3. Tạo ChartPanel từ JFreeChart
        ChartPanel chartPanel = new ChartPanel(areaChart);
        
        pnlDoanhThu.removeAll();
        
        // Tạo ChartPanel từ biểu đồ và thiết lập kích thước
        chartPanel.setBounds(0, 0, pnlDoanhThu.getWidth(), pnlDoanhThu.getHeight());
        pnlDoanhThu.add(chartPanel, JLayeredPane.PALETTE_LAYER);

        // Cập nhật giao diện
        pnlDoanhThu.revalidate();
        pnlDoanhThu.repaint();

    }

    public void loadBieuDoMonAn() {
        LoaiMon lm = null;
        if (cboLoaiMon.getSelectedIndex() == 0) {
            lm = new LoaiMon("%", "%");
        } else {
            lm = (LoaiMon) cboLoaiMon.getSelectedItem();
        }

        if (lm == null) {
            return;
        }
        List<DoanhThuMonAn> list = TKdao.getSum(lm.getMaLoai());

        // Tạo dataset mới cho biểu đồ
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (DoanhThuMonAn lm1 : list) {
            dataset.setValue(lm1.getTenMon(), lm1.getSoLuong());
        }

        // Tạo biểu đồ mới với dữ liệu
        JFreeChart chart = ChartFactory.createPieChart(
                "Món ăn", // Tiêu đề biểu đồ
                dataset, // Dữ liệu biểu đồ
                false,
                true,
                false

        );

        // Lấy đối tượng PiePlot từ biểu đồ
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} - {2}"));
        // {0} Tên dữ liệu
        // {1} giá trị dữ liệu
        // {2} giá trị dữ liệu theo phần trăm
        // {3} tổng các giá trị

        // Xóa các thành phần cũ trong pnlThongKe
        pnlThongKe.removeAll();

        // Tạo ChartPanel từ biểu đồ và thiết lập kích thước
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(0, 0, pnlThongKe.getWidth(), pnlThongKe.getHeight());
        pnlThongKe.add(chartPanel, JLayeredPane.PALETTE_LAYER);

        // Cập nhật giao diện
        pnlThongKe.revalidate();
        pnlThongKe.repaint();

    }

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
            java.util.logging.Logger.getLogger(ThongKeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThongKeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThongKeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThongKeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ThongKeJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboLoaiMon;
    private com.toedter.calendar.JDateChooser clrDenNgay;
    private com.toedter.calendar.JDateChooser clrTuNgay;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel pnlDoanhThu;
    private javax.swing.JPanel pnlThongKe;
    private javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables
}
