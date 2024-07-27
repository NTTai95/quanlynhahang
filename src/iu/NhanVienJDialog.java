/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package iu;

import dao.BanAnDAO;
import dao.HoaDonChiTietDAO;
import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.LoaiMonDAO;
import dao.MonAnDAO;
import entity.HoaDon;
import entity.HoaDonChiTiet;
import entity.KhachHang;
import entity.LoaiMon;
import entity.MonAn;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import utils.Auth;

/**
 *
 * @author admin
 */
public class NhanVienJDialog extends javax.swing.JFrame {

    /**
     * Creates new form NhanVienJDialog
     */
    int row = -1;
    HoaDonDAO hdDAO = new HoaDonDAO();
    KhachHangDAO khDAO = new KhachHangDAO();
    HoaDonChiTietDAO hdctDAO = new HoaDonChiTietDAO();
    MonAnDAO maDAO = new MonAnDAO();
    LoaiMonDAO lmDAO = new LoaiMonDAO();
    List<HoaDon> hd = hdDAO.selectByTrangThai(String.valueOf(0));
    BanAnDAO baDAO = new BanAnDAO();
    List<LoaiMon> lm = lmDAO.selectAll();

    int rowBA = baDAO.getCountRow();
    DecimalFormat fmTien = new DecimalFormat("#,#00");
    DateTimeFormatter fmThoiGian = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    DefaultTableModel modelTableHA = new DefaultTableModel(new Object[]{"MaB", "Hình ảnh", "Thông tin"}, 0) {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 1:
                    return ImageIcon.class; // Cột Photo
                default:
                    return Object.class;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Không cho phép chỉnh sửa bất kỳ ô nào
        }
    };

    public NhanVienJDialog() {
        initComponents();
        loadThucDon();
        loadBanAn();
        loadComboxLoaiMonAn();
    }

    public void loadComboxLoaiMonAn() {
        DefaultComboBoxModel cbomodel = (DefaultComboBoxModel) cboLoaiMonAn.getModel();
        cbomodel.removeAllElements();

        cbomodel.addElement("All");
        for (LoaiMon lm : lmDAO.selectAll()) {
            cbomodel.addElement(lm);
        }

        cboLoaiMonAn.setModel(cbomodel);
    }

    public boolean checkHoaDon() {
        if (lblMaHoaDon.getText().equals("0")) {
            return false;
        } else if (lblNgayLap.getText().equals("0")) {
            return false;
        } else if (txtTimKiemKhachHang.getText().equals("")) {
            return false;
        } else if (lblTenKhachHang.getText().equals("")) {
            return false;
        } else if (lblTongTien.getText().equals("0")) {
            return false;
        }
        return true;
    }

    public HoaDon getFrom(int trangThai) {
        HoaDon hdTemp = new HoaDon();
        hdTemp.setMaHD(Integer.parseInt(lblMaHoaDon.getText()));
        hdTemp.setMaB(lblBanAn.getText());
        hdTemp.setMaKH(timKhachHangbySDT(txtTimKiemKhachHang.getText()).getMaKH());
        hdTemp.setMaNV(Auth.user.getMaNV());
        hdTemp.setNgayLap(LocalDateTime.parse(lblNgayLap.getText(), fmThoiGian));
        hdTemp.setTrangThai(trangThai);
        hdTemp.setGhiChu("");

        return hdTemp;
    }

    public void setFrom(HoaDon hd) {
        lblBanAn.setText(hd.getMaB());
        lblMaHoaDon.setText(String.valueOf(hd.getMaHD()));
        lblNgayLap.setText(hd.getNgayLap().format(fmThoiGian));
        KhachHang khTemp = khDAO.selectById(hd.getMaKH());
        System.out.println("Hóa đơn: " + hd.getMaKH());
        txtTimKiemKhachHang.setText(khTemp == null ? "" : khTemp.getSDT());
        lblTenKhachHang.setText(khTemp == null ? "" : khTemp.getTenKH());
        List<HoaDonChiTiet> hdct = hdctDAO.selectHDCT(String.valueOf(hd.getMaHD()));
        loadHoaDonChiTiet(hdct);
        lblTongTien.setText(String.valueOf(fmTien.format(tongTien())));
    }

    public void loadThucDon() {

        DefaultTableModel model = modelTableHA;
        model.setRowCount(0);

        List<MonAn> ma = maDAO.selectAll();

        String maLoaiMon = "All";
        if (cboLoaiMonAn.getSelectedItem() != null) {
            if (!cboLoaiMonAn.getSelectedItem().toString().equalsIgnoreCase("All")) {
                LoaiMon lmTemp = (LoaiMon) cboLoaiMonAn.getSelectedItem();
                maLoaiMon = lmTemp.getMaLoai();
            }
        }
        for (int i = 0; i < ma.size(); i++) {
            String tenMon = ma.get(i).getTenMon();

            if (!maLoaiMon.equals(ma.get(i).getMaLoai())
                    && !maLoaiMon.equalsIgnoreCase("All")) {
                continue;
            } else if (!tenMon.contains(txtTimKiemMonAn.getText())) {
                continue;
            }

            String gia = fmTien.format(ma.get(i).getDonGia());
            String loaiMon = lmDAO.selectById(ma.get(i).getMaLoai()).getTenLoai();
            String hinhAnh = ma.get(i).getAnh();

            model.addRow(new Object[]{ma.get(i).getMaMon(), new ImageIcon("./" + hinhAnh), "<html><h2 style=\"color: red; margin-top: 0px;\">" + tenMon + "</h2><h4 style=\"margin: 0px;\">" + gia + "</h4><i style=\"margin: 0px;\">" + loaiMon + "</i></html>"});
        }

        tblThucDon.setModel(model);

        tblThucDon.getTableHeader().setVisible(false);

        TableColumn clm = tblThucDon.getColumnModel().getColumn(1);
        clm.setMinWidth(130);
        clm.setPreferredWidth(130);
        clm.setMaxWidth(130);

        TableColumn clm2 = tblThucDon.getColumnModel().getColumn(0);
        clm2.setMinWidth(0);
        clm2.setPreferredWidth(0);
        clm2.setMaxWidth(0);
    }

    public void loadBanAn() {
        DefaultTableModel model = (DefaultTableModel) tblBanAn.getModel();
        model.setRowCount(0);

        String color = "blue";

        row = -1;
        loadHoaDon();
        
        for (int i = 1; i < rowBA; i++) {
            String maBA = "B" + (i < 10 ? "0" + i : i);
            String tenKH = "Trống";
            String trangThai = "Chờ sử dụng";
            boolean boQua = cboBATrangThai.getSelectedIndex() == 1;

            for (int j = 0; j < hd.size(); j++) {
                if (maBA.equalsIgnoreCase(hd.get(j).getMaB())) {
                    maBA = "<html><p style='color:" + color + ";'>" + maBA + "</p></html>";
                    tenKH = "<html><p style='color:" + color + ";'>" + khDAO.selectById(hd.get(j).getMaKH()).getTenKH() + "</p></html>";
                    trangThai = "<html><p style='color:" + color + ";'>Chưa Thanh Toán</p></html>";
                    boQua = cboBATrangThai.getSelectedIndex() == 2;
                    break;
                }
            }
            if (boQua) {
                continue;
            }

            model.addRow(new Object[]{maBA, tenKH, trangThai});
        }
    }

    public String getValueTable(String str) {
        if (!str.startsWith("<html>")) {
            return str;
        }
        String strNew = str.substring(6, str.length() - 7);
        int start = strNew.indexOf(">");
        int end = strNew.lastIndexOf("<");

        return strNew.substring(start + 1, end);
    }

    public void loadHoaDon() {

        if(row == -1){
            HoaDon hdNew = new HoaDon();
            hdNew.setMaHD(0);
            hdNew.setMaB("B00");
            hdNew.setMaKH("");
            hdNew.setMaNV(Auth.user.getMaNV());
            hdNew.setNgayLap(LocalDateTime.now());
            hdNew.setTrangThai(0);
            hdNew.setGhiChu("");
            setFrom(hdNew);
            return;
        }
        
        if (getValueTable(tblBanAn.getValueAt(row, 2).toString()).equalsIgnoreCase("Chưa Thanh Toán")) {
            for (int i = 0; i < hd.size(); i++) {
                if (getValueTable(tblBanAn.getValueAt(row, 0).toString()).equalsIgnoreCase(hd.get(i).getMaB())) {
                    setFrom(hd.get(i));
                    return;
                }
            }
        } else {
            HoaDon hdNew = new HoaDon();
            hdNew.setMaHD(hdDAO.getCountRow() + 1);
            hdNew.setMaB(getValueTable(tblBanAn.getValueAt(row, 0).toString()));
            hdNew.setMaKH("");
            hdNew.setMaNV(Auth.user.getMaNV());
            hdNew.setNgayLap(LocalDateTime.now());
            hdNew.setTrangThai(0);
            hdNew.setGhiChu("");
            setFrom(hdNew);
        }
    }

    public float tongTien() {
//        float tongTien = 0;
//        for (int i = 0; i < tblHoaDonChiTiet.getRowCount(); i++) {
//            tongTien += (float) tblHoaDonChiTiet.getValueAt(i, 4);
//        }
//        return tongTien;

        float tongTien = 0;
        List<HoaDonChiTiet> hdctTemp = hdctDAO.selectHDCT(lblMaHoaDon.getText());
        for (HoaDonChiTiet hdct1 : hdctTemp) {
            tongTien += hdct1.getDonGia() * hdct1.getSoLuong();
        }
        return tongTien;
    }

    public void loadHoaDonChiTiet(List<HoaDonChiTiet> hdct) {
        DefaultTableModel model = (DefaultTableModel) tblHoaDonChiTiet.getModel();
        model.setRowCount(0);
        for (int i = 0; i < hdct.size(); i++) {
            String tenMon = maDAO.selectById(hdct.get(i).getMaMon()).getTenMon();
            int soLuong = hdct.get(i).getSoLuong();
            float donGia = hdct.get(i).getDonGia();
            model.addRow(new Object[]{i + 1, tenMon, soLuong, fmTien.format(donGia), soLuong * donGia});
        }

        tblHoaDonChiTiet.setModel(model);
        lblTongTien.setText(fmTien.format(tongTien()));
    }

    public KhachHang timKhachHangbySDT(String SDT) {
        KhachHang kh = khDAO.selectBySDT(SDT);
        return kh;
    }

    public void themMonAn() {
        if (!checkHoaDon()) {
            System.out.println("Khong The Them Mon An");
            return;
        }
        DefaultTableModel model = (DefaultTableModel) tblHoaDonChiTiet.getModel();
        HoaDonChiTiet hdctNew = new HoaDonChiTiet();

        MonAn maTemp = maDAO.selectById(tblThucDon.getValueAt(tblThucDon.getSelectedRow(), 0).toString());
        int maHD = Integer.parseInt(lblMaHoaDon.getText());
        List<HoaDonChiTiet> hdctTemp = hdctDAO.selectHDCT(String.valueOf(maHD));

        for (int i = 0; i < hdctTemp.size(); i++) {
            if (hdctTemp.get(i).getMaMon().equalsIgnoreCase(maTemp.getMaMon())) {
                return;
            }
        }

        hdctNew.setMaHD(maHD);
        hdctNew.setMaMon(maTemp.getMaMon());
        hdctNew.setSoLuong(1);
        hdctNew.setDonGia(maTemp.getDonGia());
        hdctDAO.insert(hdctNew);

        hdctTemp.add(hdctNew);

        loadHoaDonChiTiet(hdctTemp);

    }

    public void capNhatHDCT() {
        DefaultTableModel model = (DefaultTableModel) tblHoaDonChiTiet.getModel();
        List<HoaDonChiTiet> hdctTemp = hdctDAO.selectHDCT(lblMaHoaDon.getText());

        for (int i = 0; i < model.getRowCount(); i++) {
            if (hdctTemp.get(i).getSoLuong() != Integer.parseInt(model.getValueAt(i, 2).toString())) {
                hdctTemp.get(i).setSoLuong(Integer.parseInt(model.getValueAt(i, 2).toString()));
                hdctDAO.update(hdctTemp.get(i));
            }
        }

        lblTongTien.setText(fmTien.format(tongTien()));
        System.out.println("Tổng tiền: " + fmTien.format(tongTien()));

    }

    public void inHoaDon(HoaDon hd) {

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBanAn = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        cboBATrangThai = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblThucDon = new javax.swing.JTable();
        cboLoaiMonAn = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtTimKiemMonAn = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblNgayLap = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        cuon = new javax.swing.JScrollPane();
        tblHoaDonChiTiet = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        btnXoaMonAn = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtTimKiemKhachHang = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnThanhToan = new javax.swing.JButton();
        btnGopBan = new javax.swing.JButton();
        lblTenKhachHang = new javax.swing.JLabel();
        btnThongBaoBep = new javax.swing.JButton();
        btnInHoaDon = new javax.swing.JButton();
        lblBanAn = new javax.swing.JLabel();
        btnLuuHD = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lblMaHoaDon = new javax.swing.JLabel();
        pnlKhachHang = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBackground(new java.awt.Color(255, 153, 153));

        jTabbedPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 51)));

        tblBanAn.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"B01", "Nguyễn Tấn Tài", "Chưa Thanh Toán"},
                {"B02", "Trống", "Chưa Sử Dụng"},
                {"B03", "Trống", "Chưa Sử Dụng"},
                {"B04", "Nguyễn Thị Ngọc Nghi", "Chưa Sử Dụng"},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã bàn", "Tên Khách hàng", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBanAn.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tblBanAn.setShowGrid(true);
        tblBanAn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBanAnMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblBanAn);

        jLabel22.setText("Trạng thái:");

        cboBATrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Chưa thanh toán", "Chờ sử dụng" }));
        cboBATrangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBATrangThaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(391, 391, 391)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboBATrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(cboBATrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Bàn ăn", jPanel2);

        tblThucDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", null}
            },
            new String [] {
                "Hình ảnh", "Thông tin"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblThucDon.setRowHeight(130);
        tblThucDon.setSelectionBackground(new java.awt.Color(102, 255, 51));
        tblThucDon.setShowGrid(true);
        tblThucDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblThucDonMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblThucDon);

        cboLoaiMonAn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        cboLoaiMonAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoaiMonAnActionPerformed(evt);
            }
        });

        jLabel23.setText("Loại món ăn:");

        jLabel24.setText("Tên món ăn:");

        txtTimKiemMonAn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTimKiemMonAnKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboLoaiMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTimKiemMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboLoaiMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24)
                    .addComponent(txtTimKiemMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Thực đơn", jPanel3);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 51)), "HÓA ĐƠN", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(255, 0, 51))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 51, 51));
        jLabel1.setText("Ngày lập:");

        lblNgayLap.setForeground(new java.awt.Color(255, 51, 51));
        lblNgayLap.setText("0");

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 51)), "HÓA ĐƠN CHI TIẾT", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(255, 0, 51))); // NOI18N

        tblHoaDonChiTiet.setAutoCreateRowSorter(true);
        tblHoaDonChiTiet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Tên món", "Số lượng", "Đơn giá", "Thành Tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDonChiTiet.setGridColor(new java.awt.Color(255, 102, 102));
        tblHoaDonChiTiet.setSelectionBackground(new java.awt.Color(255, 204, 204));
        tblHoaDonChiTiet.setShowGrid(true);
        tblHoaDonChiTiet.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tblHoaDonChiTietAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tblHoaDonChiTiet.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tblHoaDonChiTietFocusGained(evt);
            }
        });
        tblHoaDonChiTiet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonChiTietMouseClicked(evt);
            }
        });
        tblHoaDonChiTiet.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                tblHoaDonChiTietInputMethodTextChanged(evt);
            }
        });
        tblHoaDonChiTiet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblHoaDonChiTietKeyPressed(evt);
            }
        });
        cuon.setViewportView(tblHoaDonChiTiet);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 0, 51));
        jLabel7.setText("Tổng Tiền:");

        lblTongTien.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        lblTongTien.setForeground(new java.awt.Color(255, 0, 51));
        lblTongTien.setText("0");

        btnXoaMonAn.setBackground(new java.awt.Color(255, 0, 51));
        btnXoaMonAn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoaMonAn.setForeground(new java.awt.Color(255, 255, 255));
        btnXoaMonAn.setText("Xóa món ăn");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cuon, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(btnXoaMonAn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(4, 4, 4)
                        .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cuon, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblTongTien)
                    .addComponent(btnXoaMonAn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 51));
        jLabel5.setText("Khách hàng:");

        txtTimKiemKhachHang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTimKiemKhachHangKeyPressed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("02");

        btnThanhToan.setBackground(new java.awt.Color(0, 204, 0));
        btnThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThanhToan.setForeground(new java.awt.Color(255, 255, 255));
        btnThanhToan.setText("Thanh toán");

        btnGopBan.setBackground(new java.awt.Color(0, 153, 255));
        btnGopBan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGopBan.setForeground(new java.awt.Color(255, 255, 255));
        btnGopBan.setText("Gộp bàn");

        btnThongBaoBep.setBackground(new java.awt.Color(0, 153, 255));
        btnThongBaoBep.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThongBaoBep.setForeground(new java.awt.Color(255, 255, 255));
        btnThongBaoBep.setText("Thông báo bếp");
        btnThongBaoBep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongBaoBepActionPerformed(evt);
            }
        });

        btnInHoaDon.setBackground(new java.awt.Color(0, 153, 255));
        btnInHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnInHoaDon.setForeground(new java.awt.Color(255, 255, 255));
        btnInHoaDon.setText("In hóa đơn");
        btnInHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInHoaDonActionPerformed(evt);
            }
        });

        lblBanAn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblBanAn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBanAn.setText("Bàn 0");
        lblBanAn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 3));
        lblBanAn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btnLuuHD.setBackground(new java.awt.Color(0, 153, 255));
        btnLuuHD.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLuuHD.setForeground(new java.awt.Color(255, 255, 255));
        btnLuuHD.setText("Lưu");
        btnLuuHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuHDActionPerformed(evt);
            }
        });

        jLabel2.setText("Mã hóa đơn:");

        lblMaHoaDon.setText("0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnThongBaoBep, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addComponent(btnGopBan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLuuHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnInHoaDon)
                            .addComponent(btnThanhToan)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTimKiemKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                                    .addComponent(lblTenKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lblBanAn, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblMaHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(29, 29, 29)
                                        .addComponent(lblNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 125, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblBanAn, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(lblMaHoaDon))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(lblNgayLap))))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTimKiemKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTenKhachHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGopBan)
                    .addComponent(btnInHoaDon)
                    .addComponent(btnLuuHD))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThanhToan)
                    .addComponent(btnThongBaoBep))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane2))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Bán Hàng", jPanel1);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Thông tin", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        jLabel9.setText("Tên khách hàng:");

        jLabel10.setText("Số điện thoại:");

        jLabel11.setText("Mã khách hàng:");

        jTextField5.setEnabled(false);
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jButton5.setText("Thêm");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton5)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addGap(18, 18, 18))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Danh Sách", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã khách hàng", "Tên khách hàng", "Số điện thoại"
            }
        ));
        jTable2.setShowGrid(true);
        jScrollPane3.setViewportView(jTable2);

        jButton9.setText("Xóa");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("Cập nhật");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton10)
                        .addGap(5, 5, 5)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9)
                    .addComponent(jButton10))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel14.setText("Tên khách hàng");

        jLabel15.setText("Mã khách hàng");

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        jLabel16.setText("Số điện thoại");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addGap(4, 4, 4)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel14)
                .addGap(4, 4, 4)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addGap(4, 4, 4)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout pnlKhachHangLayout = new javax.swing.GroupLayout(pnlKhachHang);
        pnlKhachHang.setLayout(pnlKhachHangLayout);
        pnlKhachHangLayout.setHorizontalGroup(
            pnlKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlKhachHangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(pnlKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );
        pnlKhachHangLayout.setVerticalGroup(
            pnlKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKhachHangLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(pnlKhachHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlKhachHangLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(117, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Khác Hàng", pnlKhachHang);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void btnThongBaoBepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongBaoBepActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnThongBaoBepActionPerformed

    private void cboBATrangThaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBATrangThaiActionPerformed
        // TODO add your handling code here:
        loadBanAn();
    }//GEN-LAST:event_cboBATrangThaiActionPerformed

    private void tblBanAnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBanAnMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.row = tblBanAn.getSelectedRow();
            if (row >= 0) {
                loadHoaDon();
            }
        }
    }//GEN-LAST:event_tblBanAnMouseClicked

    private void cboLoaiMonAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoaiMonAnActionPerformed
        // TODO add your handling code here:
        loadThucDon();
    }//GEN-LAST:event_cboLoaiMonAnActionPerformed

    private void txtTimKiemMonAnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemMonAnKeyPressed
        // TODO add your handling code here:
        loadThucDon();
    }//GEN-LAST:event_txtTimKiemMonAnKeyPressed

    private void btnInHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInHoaDonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnInHoaDonActionPerformed

    private void tblThucDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblThucDonMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            if (tblThucDon.getSelectedRow() >= 0) {
                themMonAn();
            }
        }
    }//GEN-LAST:event_tblThucDonMouseClicked

    private void tblHoaDonChiTietMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonChiTietMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_tblHoaDonChiTietMouseClicked

    private void tblHoaDonChiTietAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblHoaDonChiTietAncestorAdded
        // TODO add your handling code here:

    }//GEN-LAST:event_tblHoaDonChiTietAncestorAdded

    private void tblHoaDonChiTietInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblHoaDonChiTietInputMethodTextChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_tblHoaDonChiTietInputMethodTextChanged

    private void tblHoaDonChiTietKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblHoaDonChiTietKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_tblHoaDonChiTietKeyPressed

    private void txtTimKiemKhachHangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKhachHangKeyPressed
        // TODO add your handling code here:
        KhachHang khTemp = timKhachHangbySDT(txtTimKiemKhachHang.getText());
        lblTenKhachHang.setText(khTemp != null ? khTemp.getTenKH() : "");
    }//GEN-LAST:event_txtTimKiemKhachHangKeyPressed

    private void btnLuuHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuHDActionPerformed
        // TODO add your handling code here:
        if (!checkHoaDon()) {
            System.out.println("Hoa Don Khong Duoc Luu");
            return;
        }

        hdDAO.insert(getFrom(0));

        hd = hdDAO.selectByTrangThai("0");
        rowBA = baDAO.getCountRow();
        loadBanAn();

    }//GEN-LAST:event_btnLuuHDActionPerformed

    private void tblHoaDonChiTietFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblHoaDonChiTietFocusGained
        // TODO add your handling code here:
        capNhatHDCT();
    }//GEN-LAST:event_tblHoaDonChiTietFocusGained

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
            java.util.logging.Logger.getLogger(NhanVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NhanVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NhanVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NhanVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NhanVienJDialog().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGopBan;
    private javax.swing.JButton btnInHoaDon;
    private javax.swing.JButton btnLuuHD;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnThongBaoBep;
    private javax.swing.JButton btnXoaMonAn;
    private javax.swing.JComboBox<String> cboBATrangThai;
    private javax.swing.JComboBox<String> cboLoaiMonAn;
    private javax.swing.JScrollPane cuon;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JLabel lblBanAn;
    private javax.swing.JLabel lblMaHoaDon;
    private javax.swing.JLabel lblNgayLap;
    private javax.swing.JLabel lblTenKhachHang;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JPanel pnlKhachHang;
    private javax.swing.JTable tblBanAn;
    private javax.swing.JTable tblHoaDonChiTiet;
    private javax.swing.JTable tblThucDon;
    private javax.swing.JTextField txtTimKiemKhachHang;
    private javax.swing.JTextField txtTimKiemMonAn;
    // End of variables declaration//GEN-END:variables
}
