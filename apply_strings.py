import os
import json
import xml.etree.ElementTree as ET

with open(r'd:\Semester 4\TTD\extracted_strings.json', 'r', encoding='utf-8') as f:
    id_strings = json.load(f)

# Hardcoded english strings map
en_strings = {
    "g": "G",
    "nama_chat": "Name / Chat",
    "faris_adit": "(Faris, Adit)",
    "chat": "Chat",
    "ketik_pesan": "Type a message...",
    "online": "Online",
    "pekerja_tidak_ditemukan": "Worker not found",
    "verif": "VERIFY",
    "verifikasi_otp": "OTP Verification",
    "kode_otp_telah_dikirim_ke": "OTP Code has been sent to",
    "0812xxxxxxxx": "0812-xxxx-xxxx",
    "hapus_angka": "Delete Number",
    "verifikasi": "Verify",
    "ubah_nomor_telepon": "Change Phone Number",
    "beri_rating_ulasan": "Give Rating & Review",
    "50_328": "5.0 (328)",
    "berikan_rating": "Give Rating",
    "tulis_ulasan": "Write Review",
    "ceritakan_pengalaman_anda_dengan_layanan": "Tell us your experience with the service.",
    "pekerja_sangat_profesinalnsangat_recommended": "Very professional worker\\nHighly recommended!!",
    "kirim_ulasan": "Submit Review",
    "beri_opini_anda": "Give Your Opinion",
    "detail_profil_pekerja": "Worker Profile Detail",
    "ajukan_permintaan": "Submit Request",
    "terverifikasi": "Verified",
    "notifikasi": "Notification",
    "pembayaran": "Payment",
    "bayar_sekarang": "Pay Now",
    "detail_pesanan": "Order Detail",
    "rincian_pembayaran": "Payment Details",
    "harga_layanan": "Service Price",
    "rp250000": "Rp250.000",
    "biaya_admin": "Admin Fee",
    "rp5000": "Rp5.000",
    "diskon": "Discount",
    "rp0": "-Rp0",
    "total_pembayaran": "Total Payment",
    "rp255000": "Rp255.000",
    "metode_pembayaran": "Payment Method",
    "qris": "Qris",
    "bayar_dengan_scan_qris": "Pay with Qris Scan",
    "lainnya": "Others",
    "lihat_pembayaran_lainmu": "View Your Other Payments",
    "pembayaran_berhasil": "Payment Successful",
    "terima_kasih_pembayaran_anda_telah": "Thank you, your payment has been processed successfully.",
    "transaksi_anda_telah_berhasilndan_pesanan": "Your transaction was successful\\nand the order is being processed.",
    "selesai": "Done",
    "langkah_selanjutnya": "Next Step",
    "transaksi_aman": "Secure Transaction",
    "akun_saya": "My Account",
    "keluar_akun": "Log Out",
    "edit_profil": "Edit Profile",
    "alamat_saya": "My Address",
    "lapor_masalah": "Report Issue",
    "pekerja_favorit": "Favorite Workers",
    "pengaturan": "Settings",
    "tambah_alamat": "+ Add Address",
    "laporan_masalah": "Issue Report",
    "id_re_0001": "ID RE #0001",
    "pilih_kategori_masalah": "Select Issue Category",
    "pilih_kategori": "Select Category",
    "deskripsi_masalah": "Issue Description",
    "deskripsikan_masalah_anda_disini": "Describe your issue here...",
    "upload_bukti_opsional": "Upload Evidence (Optional)",
    "upload_file_gambar": "Upload Image File",
    "kirim_laporan": "Submit Report",
    "ubah_alamat": "Change Address",
    "hapus_alamat": "Delete Address",
    "simpan": "Save",
    "informasi_profil": "Profile Information",
    "foto_profil": "Profile Photo",
    "simpan_perubahan": "Save Changes",
    "nama_lengkap": "Full Name",
    "email": "Email",
    "alamat": "Address",
    "ubah_password": "Change Password",
    "konfirmasi_password": "Confirm Password",
    "tidak_ada_pekerja_favorit": "No favorite workers",
    "detail_laporan": "Report Detail",
    "kategori_masalah": "Issue Category",
    "deskripsi": "Description",
    "tanggal": "Date",
    "laporan_tidak_ditemukan": "Report not found",
    "daftar_laporan_masalah": "Issue Report List",
    "tambah_laporan": "+ Add Report",
    "pekerja": "Worker",
    "datang_terlambat": "Arrived Late",
    "23_april_2026": "April 23, 2026",
    "detail_permintaan": "Request Detail",
    "lakukan_pembayaran": "Make Payment",
    "layanan": "Service :",
    "lokasi": "Location",
    "waktu": "Time",
    "catatan": "Notes",
    "estimasi_harga": "Estimated Price",
    "kirim_permintaan": "Submit Request",
    "reset_all": "Reset All",
    "keyword": "Keyword",
    "database_instan_delivery": "(Database, Instant Delivery)",
    "filter": "Filter",
    "terapkan": "Apply",
    "tipe_pekerjaan": "Job Type",
    "gaji": "Salary",
    "rating": "Rating",
    "search": "Search",
    "tidak_ada_hasil_yang_cocok": "No matching results",
    "reset_filter": "Reset Filter",
    "transaksi_tidak_ditemukan": "Transaction not found",
    "detail_pembatalan": "Cancellation Detail",
    "batal": "Cancel",
    "pekerjaan_dibatalkan": "Job Cancelled",
    "pekerjaan_ini_telah_dibatalkan_lihat": "This job has been cancelled. See details below for more information.",
    "alasan_pembatalan": "Cancellation Reason",
    "kesepakatan_jadwal_tidak_dapat_dipenuhi": "Schedule agreement could not be fulfilled by the worker. Funds returned.",
    "informasi_tambahan": "Additional Information",
    "jika_anda_memiliki_pertanyaan_lebih": "If you have further questions regarding the refund, you can contact our help center.",
    "hasil_kerja_pekerja": "Worker's Output",
    "beri_ulasan": "Give Review",
    "pekerja_telah_selesai": "Worker has finished",
    "silakan_tinjau_hasil_pekerjaan_sebelum": "Please review the work output before giving a review.",
    "hasil_pekerjaan": "Work Output",
    "status_pekerjaan": "Job Status",
    "fe0732001": "FE-0732-001",
    "progress_pekerjaan": "Job Progress",
    "konfirmasi_pekerjaan_selesai": "Confirm Job Finished",
    "permintaan_diterima": "Request Accepted",
    "pekerjaan_dimulai": "Job Started",
    "sedang_dikerjakan": "In Progress",
    "riwayat_transaksi": "Transaction History",
    "str_empty": "\u2191\u2193",
    "32_km": "3.2 KM",
    "tentang_saya": "About Me",
    "keahlian": "Skills",
    "pengalaman": "Experience",
    "tarif": "Rate",
    "minimal_pemesanan_2_jam": "Minimum booking 2 hours",
    "120_ulasan": "120 Reviews",
    "semua_layanan": "All Services",
    "terbaru": "Newest"
}

def escape_xml(s):
    # Need to escape single quotes so they don't break XML string values when unescaped by aapt
    s = s.replace("'", "\\'")
    # And normal xml escapes
    s = s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
    return s

def append_strings(filepath, string_dict):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # Find the closing </resources> tag
    idx = content.rfind('</resources>')
    if idx == -1:
        return
    
    # Check existing keys so we don't duplicate
    existing_keys = set()
    for line in content.split('\n'):
        if '<string name="' in line:
            start = line.find('name="') + 6
            end = line.find('"', start)
            existing_keys.add(line[start:end])
    
    # Build new strings block
    new_block = ""
    for k, v in string_dict.items():
        if k not in existing_keys:
            new_block += f'    <string name="{k}">{escape_xml(v)}</string>\n'

    if new_block:
        new_content = content[:idx] + new_block + content[idx:]
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"Appended {len(string_dict)} strings to {filepath}")

append_strings(r'd:\Semester 4\TTD\app\src\main\res\values\strings.xml', id_strings)
append_strings(r'd:\Semester 4\TTD\app\src\main\res\values-en\strings.xml', en_strings)
