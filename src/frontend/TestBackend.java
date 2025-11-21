package src.frontend;
import src.backend.*;

public class TestBackend {
    public static void main(String[] args) {
        Kategori novel = new Kategori().getById(27);
        if (novel.getIdKategori() == 0) {
            // If category doesn't exist, create it for testing
            novel = new Kategori("Novel", "Kategori Novel (auto-created)");
            novel.save();
        }

        Kategori referensi = new Kategori().getById(28);
        if (referensi.getIdKategori() == 0) {
            referensi = new Kategori("Referensi", "Kategori Referensi (auto-created)");
            referensi.save();
        }

        Buku buku1 = new Buku(novel, "Timun Mas", "Elex Media", "Bang Supit");
        Buku buku2 = new Buku(referensi, "Metode Linier", "Springer", "Alex Baldwin");
        Buku buku3 = new Buku(novel, "Bintang Terang", "Erlangga", "Mat Sewoot");

        // test insert
        buku1.saveForTestBackend();
        buku2.saveForTestBackend();
        // test update
        buku2.setJudul("Aljabar Linier");
        buku2.saveForTestBackend();
        // test delete
        buku3.delete();
        // test select all
        for(Buku b : new Buku().getAll()) {
            System.out.println("Kategori: " + b.getKategori().getNama() + ", Judul: " + b.getJudul());
        }
        // test search
        for(Buku b : new Buku().search("timun")) {
            System.out.println("Kategori: " + b.getKategori().getNama() + ", Judul: " + b.getJudul());
        }
    }
}