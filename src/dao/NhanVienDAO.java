/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.NhanVien;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.XJdbc;

/**
 *
 * @author admin
 */
public class NhanVienDAO extends SysDAO<NhanVien, String> {

    String INSERT_SQL = "Insert KhachHang(MaNV,TenNV,MatKhau,ChucVu,Email) values(?,?,?,?,?)";
    String UPDATE_SQL = "UPDATE NhanVien SET TenNV = ?, MatKhau = ?, ChucVu = ?, Email = ? WHERE MaNV like ?";
    String DELETE_SQL = "DELETE FROM NhanVien WHERE MaNV like ?";
    String SELECT_BY_ID = "Select * from NhanVien where NhanVien like ?";
    String SELECT_ALL = "SELECT * FROM NhanVien";
    String COUNT_ROW = "SELECT COUNT(*) FROM NhanVien";

    @Override
    public void insert(NhanVien entity) {
        XJdbc.executeUpdate(INSERT_SQL,
                entity.getMaNV(),
                entity.getTenNV(),
                entity.getMatKhau(),
                entity.getChucVu(),
                entity.getEmail());
    }

    @Override
    public void update(NhanVien entity) {
        XJdbc.executeUpdate(UPDATE_SQL,
                entity.getTenNV(),
                entity.getMatKhau(),
                entity.getChucVu(),
                entity.getEmail(),
                entity.getMaNV());
    }

    @Override
    public void delete(String id) {
        XJdbc.executeUpdate(DELETE_SQL, id);
    }

    @Override
    public NhanVien selectById(String id) {
        List<NhanVien> list = this.selectBySQL(SELECT_BY_ID, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<NhanVien> selectAll() {
        return this.selectBySQL(SELECT_ALL);
    }

    @Override
    protected List<NhanVien> selectBySQL(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, args);
            while (rs.next()) {
                NhanVien entity = new NhanVien();
                entity.setMaNV(rs.getString(1));
                entity.setTenNV(rs.getString(2));
                entity.setMatKhau(rs.getString(3));
                entity.setChucVu(rs.getInt(4));
                entity.setEmail(rs.getString(5));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public int getCountRow() {
        try {
            ResultSet rs = XJdbc.executeQuery(COUNT_ROW);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
