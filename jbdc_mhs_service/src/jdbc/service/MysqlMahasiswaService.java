/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc.service;
import jdbc.model.Mahasiswa;
import jdbc.utilities.MysqlUtility;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Kayis_Hlm
 */
public class MysqlMahasiswaService {
    Connection koneksi = null;
    
    public MysqlMahasiswaService(){
        koneksi = MysqlUtility.getConnection();
        if (koneksi == null){
            System.out.println("Koneksi gagal");
        }
    }
    
    public Mahasiswa makeMhsObject(){
        return new Mahasiswa();
    }
    
    public void add(Mahasiswa mhs) {
    String sql = "INSERT INTO mahasiswa (nama) VALUES (?)";
    try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
        ps.setString(1, mhs.getNama());
        ps.executeUpdate();
        System.out.println("Data berhasil ditambahkan");
    } catch (SQLException e) {
        System.out.println("Data gagal ditambahkan : " + e.getMessage());
        }
    }

    
    public void update(Mahasiswa mhs){
        String sql = "UPDATE mahasiswa SET nama = ? WHERE id = ?";
        try (PreparedStatement ps = koneksi.prepareStatement(sql)){
            ps.setString(1, mhs.getNama());
            ps.setInt(2, mhs.getId());
            ps.executeUpdate();
            System.out.println("Data berhasil diupdate");
    } catch (SQLException e){
        System.out.println("Data gagal diupdate : " + e.getMessage());
        }
    }
    
    
    public void delete(int id){
       String sql = "DELETE FROM mahasiswa WHERE id = ?"; 
       try (PreparedStatement ps = koneksi.prepareStatement(sql)){
           ps.setInt(1, id);
           ps.executeUpdate();
           System.out.println("Data berhasil dihapus");
       } catch (SQLException e){
           System.out.println("Data gagal dihapus : " + e.getMessage());
        }
    }
    
    public Mahasiswa getById(int id){
        String sql = "SELECT * FROM mahasiswa WHERE id = ?";
        Mahasiswa mhs = null;
        try (PreparedStatement ps = koneksi.prepareStatement(sql)){
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()){
                    mhs = new Mahasiswa();
                    mhs.setId(rs.getInt("id"));
                    mhs.setNama(rs.getString("nama"));
                }
            }
        } catch (SQLException e){
            System.out.println("Data gagal diambil : " + e.getMessage());
        }
        return mhs;
    }
    
    public List<Mahasiswa> getAll() {
        String sql = "SELECT * FROM mahasiswa";
        List<Mahasiswa> listMhs = new ArrayList<>();
        try (PreparedStatement ps = koneksi.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Mahasiswa mhs = new Mahasiswa();
                mhs.setId(rs.getInt("id"));
                mhs.setNama(rs.getString("nama"));
                listMhs.add(mhs);
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil semua data: " + e.getMessage());
        }
        return listMhs;
    }
    
    public void indexReset() {
    try (Statement stmt = koneksi.createStatement()) {
        stmt.execute("DELETE FROM mahasiswa");
        stmt.execute("ALTER TABLE mahasiswa AUTO_INCREMENT = 1"); 
        System.out.println("Index reset berhasil");
    } catch (SQLException e) {
        System.out.println("Gagal reset indeks: " + e.getMessage());
    }
}

    
    public boolean isEmpty(){
        String sql = "SELECT COUNT(*) AS Total FROM mahasiswa";
        try (PreparedStatement ps = koneksi.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return rs.getInt("Total") == 0;
                    }
        } catch (SQLException e) {
            System.out.println("Cek apakah kosong gagal : " + e.getMessage());
        }
        return true;
    }
    
    public void closeConnection(){
        try{
            if (koneksi != null && !koneksi.isClosed()){
                koneksi.close();
                System.out.println("Koneksi ditutup");
            }
        } catch (SQLException e){
            System.out.println("Koneksi gagal ditutup : " + e.getMessage());
        }
    }
}
