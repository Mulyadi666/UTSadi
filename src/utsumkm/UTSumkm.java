package utsumkm;

/**
 *
 * @author adimu
 */
import jaco.mp3.player.MP3Player;
import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import java.awt.*;

public class UTSumkm extends JFrame {

    private HashMap<String, BigDecimal> hargaBarang;
    private ArrayList<String> keranjang;
    private HashMap<String, Integer> jumlahBarang;

    NumberFormat nf = NumberFormat.getNumberInstance(new Locale("in", "ID"));

    public UTSumkm() {
        setTitle("Aplikasi UMKM");
        setSize(600, 450);
        hargaBarang = new HashMap<>();
        try {
            // Hapus pemisah ribuan sebelum membuat objek BigDecimal
            BigDecimal hargaKaporit = new BigDecimal(nf.parse("350000").toString());
            BigDecimal hargaHecules = new BigDecimal(nf.parse("1000000").toString());
            BigDecimal hargaMacco = new BigDecimal(nf.parse("1050000").toString());
            BigDecimal hargaJepang = new BigDecimal(nf.parse("2500000").toString());
            BigDecimal hargaPurox = new BigDecimal(nf.parse("1050000").toString());

            hargaBarang.put("Kaporit Tjiwi @15kg", hargaKaporit);
            hargaBarang.put("BPDA Hecules", hargaHecules);
            hargaBarang.put("CP Macco @20kg", hargaMacco);
            hargaBarang.put("PS Jepang @25kg", hargaJepang);
            hargaBarang.put("Purox @25kg", hargaPurox);

        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        keranjang = new ArrayList<>();
        jumlahBarang = new HashMap<>();

        // Tambahkan komponen-komponen GUI di sini
        JLabel imageLabel = new JLabel();
        add(imageLabel);
        imageLabel.setText("Cakrawala Kimia");
        imageLabel.setFont(new Font("roboto", Font.BOLD, 35));
        imageLabel.setVisible(true);

        JComboBox<String> barangComboBox = new JComboBox<>(hargaBarang.keySet().toArray(new String[0]));
//        add(barangComboBox);
        //cari text field
        JTextField cariTextField = new JTextField(10);
        add(cariTextField);
        add(barangComboBox);

        // Tambahkan teks hint ke JTextField
        cariTextField.setForeground(Color.GRAY);
        cariTextField.setText("Cari barang");

        // Tambahkan efek saat JTextField mendapatkan atau kehilangan fokus
        cariTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (cariTextField.getText().equals("Cari barang")) {
                    cariTextField.setText("");
                    cariTextField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (cariTextField.getText().isEmpty()) {
                    cariTextField.setForeground(Color.GRAY);
                    cariTextField.setText("Cari barang");
                }
            }
        });

        // Menambahkan action listener untuk JTextField pencarian
        cariTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String kataKunci = cariTextField.getText().toLowerCase(); // Mengambil kata kunci pencarian dan mengubahnya menjadi huruf kecil
                ArrayList<String> barangTersaring = new ArrayList<>(); // Membuat daftar untuk menyimpan barang yang disaring

                // Menyaring daftar barang berdasarkan kata kunci
                for (String barang : hargaBarang.keySet()) {
                    if (barang.toLowerCase().contains(kataKunci)) {
                        barangTersaring.add(barang);
                    }
                }

                // Memperbarui JComboBox dengan daftar barang yang disaring
                barangComboBox.setModel(new DefaultComboBoxModel<>(barangTersaring.toArray(new String[0])));
            }
        });
        //end cari

        JTextField jumlahTextField = new JTextField(10);
        add(jumlahTextField);

        JButton tambahButton = new JButton("Tambah ke Keranjang");
        add(tambahButton);
        tambahButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            String SONG = "C:\\Users\\adimu\\Documents\\NetBeansProjects\\UTSumkm\\src\\sounds\\cash.wav";
            MP3Player bell = new MP3Player(new File(SONG));

            bell.play ();
            }
        });

        //panel cari
//        JPanel cariPanel = new JPanel (new GridLayout(1, 2));
//        cariPanel.add(barangComboBox);
//        cariPanel.add(cariTextField);
        JTextArea keranjangTextArea = new JTextArea();
        add(new JScrollPane(keranjangTextArea));

        tambahButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedBarang = (String) barangComboBox.getSelectedItem();
                int jumlah = Integer.parseInt(jumlahTextField.getText());
                keranjang.add(selectedBarang);
                jumlahBarang.put(selectedBarang, jumlah);
                updateKeranjangText(keranjangTextArea);

            }
        });

        // Layout frame menggunakan GridLayout
        setLayout(new GridLayout(6, 1, 10, 10));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void updateKeranjangText(JTextArea keranjangTextArea) {
        keranjangTextArea.setText("");
        BigDecimal totalHarga = BigDecimal.ZERO;
        // Mendapatkan tanggal hari ini
        LocalDate tanggalHariIni = LocalDate.now();
        // Menambahkan tanggal ke dalam teks area
        keranjangTextArea.append("Tanggal: " + tanggalHariIni + "\n");

        for (String barang : keranjang) {
            int jumlah = jumlahBarang.get(barang);
            BigDecimal harga = hargaBarang.get(barang);
            BigDecimal subtotal = harga.multiply(BigDecimal.valueOf(jumlah));
            keranjangTextArea.append(barang + " (Qty: " + jumlah + ") - Rp " + nf.format(subtotal) + "\n");
            totalHarga = totalHarga.add(subtotal);
        }
        keranjangTextArea.append("Total: Rp " + nf.format(totalHarga));
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                new UTSumkm();
//            }
//        });
//    }
}
