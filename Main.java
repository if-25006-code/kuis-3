import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Mahasiswa {
    private String nim;
    private String nama;
    private String email;
    private int umur;

    public Mahasiswa(String nim, String nama, String email, int umur) {
        this.nim = nim;
        this.nama = nama;
        this.email = email;
        this.umur = umur;
    }

    public String getNim() {
        return nim;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public int getUmur() {
        return umur;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUmur(int umur) {
        this.umur = umur;
    }
}

class MataKuliah {
    private String kode;
    private String nama;
    private int jumlahSks;
    private int semester;

    public MataKuliah(String kode, String nama, int jumlahSks, int semester) {
        this.kode = kode;
        this.nama = nama;
        this.jumlahSks = jumlahSks;
        this.semester = semester;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getJumlahSks() {
        return jumlahSks;
    }

    public void setJumlahSks(int jumlahSks) {
        this.jumlahSks = jumlahSks;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}

class Ruangan {
    private String id;
    private String nama;
    private int lokasiLantai;
    private int kapasitas;

    public Ruangan(String id, String nama, int lokasiLantai, int kapasitas) {
        this.id = id;
        this.nama = nama;
        this.lokasiLantai = lokasiLantai;
        this.kapasitas = kapasitas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getLokasiLantai() {
        return lokasiLantai;
    }

    public void setLokasiLantai(int lokasiLantai) {
        this.lokasiLantai = lokasiLantai;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }
}

abstract class DataView<T> {
    private List<T> records;
    protected Scanner input;

    public DataView(Scanner input) {
        this.records = new ArrayList<>();
        this.input = input;
    }

    protected abstract String getPrimaryKeyValue(T data);

    protected abstract String[] getFields();

    protected abstract T convertValuesToModel(String[] values);

    protected abstract String[] convertModelToValues(T data);

    public void printAll() {
        if (records.isEmpty()) {
            System.out.println("Data masih kosong!");
            return;
        }

        String[] fields = getFields();

        System.out.println();
        for (String field : fields) {
            System.out.printf("%-20s", field);
        }

        System.out.println();

        for (int i = 0; i < fields.length; i++) {
            System.out.print("-");
        }

        System.out.println();

        for (T data : records) {
            String[] values = convertModelToValues(data);

            for (String value : values) {
                System.out.printf("%-20s", value);
            }

            System.out.println();
        }
    }

    public void printByPk(String primaryKey) {
        for (T data : records) {
            if (getPrimaryKeyValue(data).equalsIgnoreCase(primaryKey)) {
                System.out.println();

                String[] fields = getFields();
                String[] values = convertModelToValues(data);

                for (int i = 0; i < fields.length; i++) {
                    System.out.println(fields[i] + " : " + values[i]);
                }

                return;
            }
        }

        System.out.println("Data dengan primary key " + primaryKey + " tidak ditemukan!");
    }

    public void addRecord(T data) {
        records.add(data);
    }

    public void deleteRecord(String primaryKey) {
        for (int i = 0; i < records.size(); i++) {
            if (getPrimaryKeyValue(records.get(i)).equalsIgnoreCase(primaryKey)) {
                records.remove(i);
                return;
            }
        }

        System.out.println("Data dengan primary key " + primaryKey + " tidak ditemukan!");
    }

    public void updateRecord(String primaryKey, String[] newValues) {
        for (int i = 0; i < records.size(); i++) {
            if (getPrimaryKeyValue(records.get(i)).equalsIgnoreCase(primaryKey)) {
                records.set(i, convertValuesToModel(newValues));
                return;
            }
        }

        System.out.println("Data dengan primary key " + primaryKey + " tidak ditemukan!");
    }

    protected abstract String[] inputValues();

    public void readInputAndAdd() {
        String[] values = inputValues();
        if (values != null) {
            addRecord(convertValuesToModel(values));
        }
    }

    public void showUpdateForm() {
        System.out.print("Masukkan Primary Key yang ingin diubah: ");
        String primaryKey = input.nextLine();

        for (int i = 0; i < records.size(); i++) {
            if (getPrimaryKeyValue(records.get(i)).equalsIgnoreCase(primaryKey)) {

                String[] fields = getFields();
                String[] values = new String[fields.length];

                System.out.println("\n=== UBAH DATA ===");

                for (int j = 0; j < fields.length; j++) {
                    System.out.print("Masukkan " + fields[j] + " baru: ");
                    values[j] = input.nextLine();
                }

                T updatedData = convertValuesToModel(values);

                /* ~~ Check Duplicate Primary Key ~~ */
                for (int j = 0; j < records.size(); j++) {
                    if (j != i &&
                        getPrimaryKeyValue(records.get(j))
                        .equalsIgnoreCase(getPrimaryKeyValue(updatedData))) {
                        System.out.println("Primary Key sudah digunakan!");
                        return;
                    }
                }

                records.set(i, updatedData);
                System.out.println("Data berhasil diubah!");
                return;
            }
        }

        System.out.println("Data dengan primary key " + primaryKey + " tidak ditemukan!");
    }
}

class MahasiswaView extends DataView<Mahasiswa> {

    public MahasiswaView(Scanner input) {
        super(input);
    }

    @Override
    protected String getPrimaryKeyValue(Mahasiswa data) {
        return data.getNim();
    }

    @Override
    protected String[] getFields() {
        return new String[]{"NIM", "Nama", "Email", "Umur"};
    }

    @Override
    protected Mahasiswa convertValuesToModel(String[] values) {
        return new Mahasiswa(values[0], values[1], values[2], Integer.parseInt(values[3]));
    }

    @Override
    protected String[] convertModelToValues(Mahasiswa data) {
        return new String[]{data.getNim(), data.getNama(), data.getEmail(), String.valueOf(data.getUmur())};
    }

    @Override
    protected String[] inputValues() {
        System.out.println("\n=== TAMBAH DATA MAHASISWA ===");
        System.out.print("Masukkan NIM: ");
        String nim = input.nextLine();
        System.out.print("Masukkan Nama: ");
        String nama = input.nextLine();
        System.out.print("Masukkan Email: ");
        String email = input.nextLine();
        System.out.print("Masukkan Umur: ");
        int umur = Integer.parseInt(input.nextLine());

        return new String[]{nim, nama, email, String.valueOf(umur)};
    }

    public void menuMahasiswa() {
        while (true) {
            System.out.println("\n=== MENU MAHASISWA ===");
            System.out.println("1. Tampilkan semua data");
            System.out.println("2. Cari data berdasarkan NIM");
            System.out.println("3. Tambah data");
            System.out.println("4. Ubah data");
            System.out.println("5. Hapus data");
            System.out.println("6. Keluar");
            System.out.print("Pilih menu: ");

            int choice = Integer.parseInt(input.nextLine());

            switch (choice) {
                case 1:
                    printAll();
                    break;
                case 2:
                    System.out.print("Masukkan NIM: ");
                    String nim = input.nextLine();
                    printByPk(nim);
                    break;
                case 3:
                    readInputAndAdd();
                    break;
                case 4:
                    showUpdateForm();
                    break;
                case 5:
                    System.out.print("Masukkan NIM yang ingin dihapus: ");
                    String nimToDelete = input.nextLine();
                    deleteRecord(nimToDelete);
                    break;
                case 6:
                    System.out.println("Keluar dari menu Mahasiswa.");
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
}