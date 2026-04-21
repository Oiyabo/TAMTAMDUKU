package model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

object NWGroup {
    val WorkType = listOf("Computer", "administration", "master of ceremony", "House Chores", "Heavy Work", "Driver / Delivery", "Tutor", "Freelance", "Other Job")

    val IndonesianCities = listOf(
        "Jakarta", "Surabaya", "Bandung", "Medan", "Semarang", "Makassar", "Palembang", "Tangerang", 
        "South Tangerang", "Bekasi", "Depok", "Batam", "Bogor", "Padang", "Bandar Lampung", "Malang", 
        "Denpasar", "Yogyakarta", "Banjarmasin", "Balikpapan", "Pontianak", "Surakarta", "Jambi", 
        "Manado", "Pekanbaru", "Mataram", "Jayapura", "Ambon", "Kupang", "Bengkulu", "Palu", 
        "Kendari", "Samarinda", "Gorontalo", "Mamuju", "Tanjung Pinang", "Pangkal Pinang", 
        "Tarakan", "Sorong", "Banjarbaru", "Palangka Raya"
    ).sorted()

    @RequiresApi(Build.VERSION_CODES.O)
    val NWG = listOf(
        NovaWorker("Front End", "Abdulilah", listOf(WorkType[0], WorkType[7]), "Saya sudah melakukan front selama 5 tahun. Project bisa anda lihat di profil.", "Jakarta", LocalDate.of(2019, 5, 10), 15000000.0, listOf("HTML", "CSS", "Javascript"), 4.5),
        NovaWorker("Back End", "Budi Santoso", listOf(WorkType[0]), "Ahli Node.js dan SQL Server. Berpengalaman dalam membangun API skala besar.", "Surabaya", LocalDate.of(2020, 3, 15), 18000000.0, listOf("Node.js", "SQL Server", "Express"), 4.8),
        NovaWorker("Mobile Developer", "Cahyo Wijaya", listOf(WorkType[0], WorkType[7]), "Fokus pada Flutter dan Android Native (Kotlin).", "Bandung", LocalDate.of(2021, 1, 20), 16000000.0, listOf("Flutter", "Kotlin", "Dart"), 4.7),
        NovaWorker("UI/UX Designer", "Dewi Lestari", listOf(WorkType[0], WorkType[7]), "Menciptakan desain yang user-friendly dan estetis.", "Yogyakarta", LocalDate.of(2022, 6, 5), 12000000.0, listOf("Figma", "Adobe XD", "Prototyping"), 4.9),
        NovaWorker("Full Stack", "Eko Prasetyo", listOf(WorkType[0]), "Bisa mengerjakan sisi client dan server dengan baik.", "Semarang", LocalDate.of(2018, 11, 12), 20000000.0, listOf("React", "Node.js", "MongoDB"), 4.6),
        NovaWorker("Data Analyst", "Fitriani", listOf(WorkType[0], WorkType[1]), "Menganalisis data untuk insight bisnis yang lebih baik.", "Medan", LocalDate.of(2021, 9, 30), 14000000.0, listOf("Python", "SQL", "Tableau"), 4.4),
        NovaWorker("DevOps Engineer", "Gita Permata", listOf(WorkType[0]), "Spesialis dalam otomasi cloud dan CI/CD.", "Makassar", LocalDate.of(2020, 7, 1), 19000000.0, listOf("Docker", "Kubernetes", "AWS"), 4.7),
        NovaWorker("QA Engineer", "Hani Nuraini", listOf(WorkType[0]), "Memastikan kualitas software dengan testing manual dan otomatis.", "Palembang", LocalDate.of(2022, 2, 14), 11000000.0, listOf("Selenium", "Appium", "JMeter"), 4.3),
        NovaWorker("Graphic Designer", "Indra Kusuma", listOf(WorkType[0], WorkType[7], WorkType[8]), "Desain kreatif untuk branding.", "Malang", LocalDate.of(2019, 12, 25), 9000000.0, listOf("Photoshop", "Illustrator", "Canva"), 4.5),
        NovaWorker("Project Manager", "Jaka Tarub", listOf(WorkType[1]), "Mengelola tim dan timeline proyek dengan efisien.", "Denpasar", LocalDate.of(2017, 8, 18), 25000000.0, listOf("Agile", "Scrum", "Trello"), 4.8),
        NovaWorker("Administrasi", "Kartika Sari", listOf(WorkType[1]), "Mengelola dokumen dan jadwal kantor.", "Bogor", LocalDate.of(2023, 1, 10), 5000000.0, listOf("Excel", "Word", "Scheduling"), 4.2),
        NovaWorker("Supir Pribadi", "Lukas", listOf(WorkType[5]), "Supir berpengalaman, hafal jalan Jakarta.", "Jakarta", LocalDate.of(2020, 5, 20), 4500000.0, listOf("Driving", "Navigation"), 4.6),
        NovaWorker("Tutor Matematika", "Maman", listOf(WorkType[6], WorkType[7]), "Mengajar matematika SMA.", "Tangerang", LocalDate.of(2021, 8, 15), 3000000.0, listOf("Math", "Teaching"), 4.4),
        NovaWorker("Asisten Rumah Tangga", "Nina", listOf(WorkType[3]), "Jujur dan rajin bersih-bersih rumah.", "Bekasi", LocalDate.of(2022, 10, 5), 3500000.0, listOf("Cleaning", "Cooking"), 4.7),
        NovaWorker("MC Acara Pernikahan", "Omar", listOf(WorkType[2], WorkType[7]), "MC profesional untuk berbagai acara formal.", "Surakarta", LocalDate.of(2018, 4, 12), 2000000.0, listOf("Public Speaking", "Hosting"), 4.5),
        NovaWorker("Kuli Bangunan", "Putra", listOf(WorkType[4]), "Tenaga kuat untuk renovasi rumah.", "Jakarta", LocalDate.of(2021, 3, 1), 5000000.0, listOf("Construction", "Lifting"), 4.9),
        NovaWorker("Game Developer", "Qori", listOf(WorkType[0]), "Membangun game Unity.", "Bandung", LocalDate.of(2020, 11, 20), 14000000.0, listOf("Unity", "C#"), 4.6),
        NovaWorker("Data Scientist", "Rani", listOf(WorkType[0]), "Model Machine Learning.", "Surabaya", LocalDate.of(2019, 7, 7), 22000000.0, listOf("Python", "Machine Learning"), 4.8),
        NovaWorker("Cloud Architect", "Surya", listOf(WorkType[0]), "Infrastruktur cloud.", "Yogyakarta", LocalDate.of(2018, 2, 28), 28000000.0, listOf("AWS", "Azure"), 4.7),
        NovaWorker("Kurir Paket", "Tono", listOf(WorkType[5]), "Pengiriman cepat dan aman.", "Medan", LocalDate.of(2023, 4, 15), 4000000.0, listOf("Driving", "Delivery"), 4.5),
        NovaWorker("Pekerja Kebun", "Udin", listOf(WorkType[3], WorkType[4]), "Merawat taman dan pohon.", "Semarang", LocalDate.of(2022, 9, 10), 3800000.0, listOf("Gardening"), 4.3),
        NovaWorker("Guru Privat Bahasa Inggris", "Vera", listOf(WorkType[6], WorkType[7]), "Native level English tutor.", "Makassar", LocalDate.of(2021, 12, 1), 4500000.0, listOf("English", "IELTS"), 4.6),
        NovaWorker("Montir Panggilan", "Wawan", listOf(WorkType[4], WorkType[8]), "Service mesin kendaraan di tempat.", "Palembang", LocalDate.of(2019, 6, 20), 7000000.0, listOf("Mechanic"), 4.7),
        NovaWorker("Tukang Masak", "Xena", listOf(WorkType[3], WorkType[7]), "Catering harian atau acara.", "Malang", LocalDate.of(2020, 2, 14), 6000000.0, listOf("Cooking", "Catering"), 4.8),
        NovaWorker("Operator Alat Berat", "Yanto", listOf(WorkType[4]), "Lisensi lengkap SIO.", "Denpasar", LocalDate.of(2017, 11, 11), 12000000.0, listOf("Excavator", "Bulldozer"), 4.4),
        NovaWorker("Content Creator", "Zaki", listOf(WorkType[0], WorkType[7]), "Video editing dan social media.", "Bogor", LocalDate.of(2022, 5, 5), 8000000.0, listOf("Premiere Pro", "TikTok"), 4.9),
        NovaWorker("Satpam", "Andi", listOf(WorkType[8]), "Keamanan ruko atau perumahan.", "Jakarta", LocalDate.of(2021, 1, 1), 4800000.0, listOf("Security", "First Aid"), 4.7),
        NovaWorker("Sekretaris", "Bela", listOf(WorkType[1]), "Manajemen administrasi kantor.", "Bandung", LocalDate.of(2023, 2, 1), 6500000.0, listOf("Organization", "Typing"), 4.1),
        NovaWorker("Teknisi Jaringan", "Chandra", listOf(WorkType[0], WorkType[4]), "Instalasi WiFi dan kabel LAN.", "Surabaya", LocalDate.of(2020, 9, 10), 7500000.0, listOf("Networking", "Cisco"), 4.5),
        NovaWorker("Cleaning Service", "Dedi", listOf(WorkType[3]), "Kebersihan gedung kantor.", "Yogyakarta", LocalDate.of(2022, 3, 15), 3200000.0, listOf("Cleaning"), 4.4),
        NovaWorker("Sales Lapangan", "Euis", listOf(WorkType[8]), "Promosi produk door to door.", "Bandung", LocalDate.of(2023, 5, 20), 4000000.0, listOf("Communication", "Marketing"), 4.6),
        NovaWorker("Staff Gudang", "Fajar", listOf(WorkType[4], WorkType[1]), "Input data dan bongkar muat barang.", "Jakarta", LocalDate.of(2021, 10, 1), 5500000.0, listOf("Inventory", "Lifting"), 4.8),
        NovaWorker("Guru Ngaji", "Guntur", listOf(WorkType[6]), "Mengajar iqro dan Al-Quran.", "Semarang", LocalDate.of(2019, 4, 1), 2500000.0, listOf("Teaching", "Religion"), 4.2),
        NovaWorker("Desainer Interior", "Hilda", listOf(WorkType[7], WorkType[0]), "Desain ruangan rumah dan kantor.", "Medan", LocalDate.of(2020, 12, 12), 15000000.0, listOf("AutoCAD", "Sketchup"), 4.7),
        NovaWorker("Resepsionis", "Irfan", listOf(WorkType[1]), "Menerima tamu dan telepon.", "Makassar", LocalDate.of(2022, 7, 1), 4500000.0, listOf("Communication", "Hospitality"), 4.5),
        NovaWorker("Staf HRD", "Joko", listOf(WorkType[1]), "Rekrutmen dan absensi karyawan.", "Surakarta", LocalDate.of(2018, 9, 1), 9000000.0, listOf("Recruitment", "Payroll"), 4.6),
        NovaWorker("Barista", "Kiki", listOf(WorkType[8]), "Ahli membuat kopi espresso.", "Palembang", LocalDate.of(2023, 8, 1), 4000000.0, listOf("Coffee", "Customer Service"), 4.4),
        NovaWorker("Fotografer", "Lulu", listOf(WorkType[7], WorkType[8]), "Foto produk dan wedding.", "Malang", LocalDate.of(2021, 5, 20), 10000000.0, listOf("Photography", "Editing"), 4.5),
        NovaWorker("Accounting", "Mulyadi", listOf(WorkType[1]), "Laporan keuangan dan perpajakan.", "Denpasar", LocalDate.of(2017, 6, 1), 12000000.0, listOf("Accounting", "Tax"), 4.3),
        NovaWorker("Baby Sitter", "Nadia", listOf(WorkType[3], WorkType[8]), "Merawat bayi dan balita.", "Bogor", LocalDate.of(2022, 1, 1), 4500000.0, listOf("Child Care"), 4.2),
        NovaWorker("Telemarketing", "Oki", listOf(WorkType[1], WorkType[8]), "Menawarkan produk via telepon.", "Depok", LocalDate.of(2023, 1, 15), 3800000.0, listOf("Persuasion", "Calling"), 4.6),
        NovaWorker("Apoteker", "Puji", listOf(WorkType[8]), "Melayani resep obat.", "Tangerang", LocalDate.of(2020, 4, 10), 7500000.0, listOf("Pharmacy", "Medicine"), 4.8),
        NovaWorker("Legal Officer", "Qomaruddin", listOf(WorkType[1]), "Mengurus perizinan dan hukum.", "Bekasi", LocalDate.of(2019, 2, 15), 14000000.0, listOf("Law", "Licensing"), 4.5),
        NovaWorker("Analis Laboratorium", "Robby", listOf(WorkType[8]), "Pemeriksaan sampel darah.", "Jakarta", LocalDate.of(2021, 8, 20), 8000000.0, listOf("Chemistry", "Research"), 4.4),
        NovaWorker("Perawat Lansia", "Siska", listOf(WorkType[3], WorkType[8]), "Merawat orang tua di rumah.", "Surabaya", LocalDate.of(2022, 11, 1), 6000000.0, listOf("Nursing", "Patience"), 4.7),
        NovaWorker("Animator 3D", "Tora", listOf(WorkType[0], WorkType[7]), "Membuat animasi untuk iklan.", "Bandung", LocalDate.of(2020, 6, 1), 13000000.0, listOf("Blender", "Maya"), 4.6),
        NovaWorker("Penerjemah Mandarin", "Uli", listOf(WorkType[7], WorkType[1]), "Translate dokumen dan interpreter.", "Yogyakarta", LocalDate.of(2021, 3, 15), 11000000.0, listOf("Mandarin", "Translation"), 4.8),
        NovaWorker("Data Entry", "Vicky", listOf(WorkType[1], WorkType[0]), "Input data cepat dan akurat.", "Jakarta", LocalDate.of(2023, 6, 1), 4200000.0, listOf("Typing", "Accuracy"), 4.5),
        NovaWorker("Tukang Kayu", "Wulan", listOf(WorkType[4], WorkType[7]), "Custom furniture dan interior.", "Denpasar", LocalDate.of(2019, 10, 10), 8500000.0, listOf("Carpentry", "Furniture"), 4.7),
        NovaWorker("System Architect", "Yayan", listOf(WorkType[0]), "Merancang sistem enterprise.", "Bandung", LocalDate.of(2016, 1, 1), 35000000.0, listOf("Architecture", "Cloud"), 4.9)
    )
}
