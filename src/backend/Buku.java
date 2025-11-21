package src.backend;
import java.util.ArrayList;
import java.sql.*;

public class Buku {
    private int idBuku;
    private Kategori idKategori = new Kategori();
    private String judul, penerbit, penulis;

    public int getIdBuku () {
        return idBuku;
    }

    public void setIdBuku (int newIdBuku) {
        this.idBuku = newIdBuku;
    }

    public Kategori getKategori () {
        return idKategori;
    }

    public void setIdKategori (Kategori newIdKategori) {
        this.idKategori = newIdKategori;
    }

    public String getJudul () {
        return judul;
    }

    public void setJudul (String newJudul) {
        this.judul = newJudul;
    }

    public String getPenerbit () {
        return penerbit;
    }

    public void setPenerbit (String newPenerbit) {
        this.penerbit = newPenerbit;
    }

    public String getPenulis () {
        return penulis;
    }

    public void setPenulis (String newPenulis) {
        this.penulis = newPenulis;
    }

    public Buku () {}
    public Buku (Kategori idKategori, String judul, String penerbit, String penulis) {
        this.idKategori = idKategori;
        this.judul = judul;
        this.penerbit = penerbit;
        this.penulis = penulis;
    }

    public Buku getById(int id) {
        Buku buku = new Buku();
        ResultSet rs = DBHelper.selectQuery("SELECT "
                + "    b.idbuku AS idbuku, "
                + "    b.judul AS judul, "
                + "    b.penerbit AS penerbit, "
                + "    b.penulis AS penulis, "
                + "    k.idkategori AS idkategori, "
                + "    k.nama AS nama, "
                + "    k.keterangan AS keterangan "
                + "    FROM buku b "
                + "    LEFT JOIN kategori k ON b.idkategori = k.idkategori "
                + "    WHERE b.idbuku = '" + id + "'");
        try {
            while (rs.next()) {
                buku = new Buku();
                buku.setIdBuku(rs.getInt("idbuku"));
                buku.getKategori().setIdKategori(rs.getInt("idkategori"));
                buku.getKategori().setNama(rs.getString("nama"));
                buku.getKategori().setKeterangan(rs.getString("keterangan"));
                buku.setJudul(rs.getString("judul"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setPenulis(rs.getString("penulis"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buku;
    }

    public ArrayList<Buku> getAll() {
        ArrayList<Buku> listBuku = new ArrayList<>();
        ResultSet rs = DBHelper.selectQuery("SELECT "
                + "    b.idbuku AS idbuku, "
                + "    b.judul AS judul, "
                + "    b.penerbit AS penerbit, "
                + "    b.penulis AS penulis, "
                + "    k.idkategori AS idkategori, "
                + "    k.nama AS nama, "
                + "    k.keterangan AS keterangan "
                + "    FROM buku b "
                + "    LEFT JOIN kategori k ON b.idkategori = k.idkategori ");
        try {
            while (rs.next()) {
                Buku buku = new Buku();
                buku.setIdBuku(rs.getInt("idbuku"));
                buku.getKategori().setIdKategori(rs.getInt("idkategori"));
                buku.getKategori().setNama(rs.getString("nama"));
                buku.getKategori().setKeterangan(rs.getString("keterangan"));
                buku.setJudul(rs.getString("judul"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setPenulis(rs.getString("penulis"));
                
                listBuku.add(buku);
            }
        } catch (Exception e) {
            e.printStackTrace();    
        } 
        return listBuku;
    }

    public ArrayList<Buku> search(String keyword) {
        ArrayList<Buku> listBuku = new ArrayList<>();
        String sql = "SELECT "
                + "    b.idbuku AS idbuku, "
                + "    b.judul AS judul, "
                + "    b.penerbit AS penerbit, "
                + "    b.penulis AS penulis, "
                + "    k.idkategori AS idkategori, "
                + "    k.nama AS nama, "
                + "    k.keterangan AS keterangan "
                + "    FROM buku b "
                + "    LEFT JOIN kategori k ON b.idkategori = k.idkategori "
                + "    WHERE b.judul ILIKE '%" + keyword + "%' "
                + "    OR b.penerbit ILIKE '%" + keyword + "%' "
                + "    OR b.penulis ILIKE '%" + keyword + "%' ";
        ResultSet rs = DBHelper.selectQuery(sql);
        try {
            while (rs.next()) {
                Buku buku = new Buku();
                buku.setIdBuku(rs.getInt("idbuku"));
                buku.getKategori().setIdKategori(rs.getInt("idkategori"));
                buku.getKategori().setNama(rs.getString("nama"));
                buku.getKategori().setKeterangan(rs.getString("keterangan"));
                buku.setJudul(rs.getString("judul"));
                buku.setPenerbit(rs.getString("penerbit"));
                buku.setPenulis(rs.getString("penulis"));
                
                listBuku.add(buku);
            }
        } catch (Exception e) {
            e.printStackTrace();    
        } 
        return listBuku;
    }

    public void save() {
        if (getById(idBuku).getIdBuku() == 0) {
            // Validation: ensure associated Kategori is set and persisted
            if (this.getKategori() == null || this.getKategori().getIdKategori() == 0) {
                throw new IllegalStateException("Cannot save Buku: associated Kategori is not set or not persisted (id=0).\n" +
                        "Create or save the Kategori first before saving Buku.");
            }
            String sql = "INSERT INTO buku (idkategori, judul, penerbit, penulis) "
                    + "VALUES ("
                    + "'" + this.getKategori().getIdKategori() + "', "
                    + "'" + this.judul + "', "
                    + "'" + this.penerbit + "', "
                    + "'" + this.penulis + "' "
                    + ")";
            // Debug: print SQL and capture returned id
            System.out.println("[DEBUG] Buku.save() - INSERT SQL: " + sql);
            int newId = DBHelper.insertQueryGetId(sql);
            System.out.println("[DEBUG] Buku.save() - insertQueryGetId returned: " + newId);
            this.idBuku = newId;
        } else {
            String sql = "UPDATE buku SET "
                    + "idkategori = '" + this.getKategori().getIdKategori() + "', "
                    + "judul = '" + this.judul + "', "
                    + "penerbit = '" + this.penerbit + "', "
                    + "penulis = '" + this.penulis + "' "
                    + "WHERE idbuku = " + this.idBuku;
            // Debug: print SQL and execution result
            System.out.println("[DEBUG] Buku.save() - UPDATE SQL: " + sql);
            boolean ok = DBHelper.executeQuery(sql);
            System.out.println("[DEBUG] Buku.save() - executeQuery returned: " + ok);
        }
    }

    public void delete() {
        String sql = "DELETE FROM buku WHERE idbuku = " + this.idBuku;
        DBHelper.executeQuery(sql);
    }
}