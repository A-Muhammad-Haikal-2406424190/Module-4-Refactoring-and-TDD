# Reflection

> Reflect based on Percival (2017) proposed self-reflective questions (in “Principles and Best
Practice of Testing” submodule, chapter “Evaluating Your Testing Objectives”), whether this
TDD flow is useful enough for you or not. If not, explain things that you need to do next time
you make more tests.

Menurut saya alur TDD pada exercise ini cukup membantu karena memaksa saya mendefinisikan perilaku sistem dari awal (melalui test) sebelum menulis implementasi, sehingga arah pengembangan jadi lebih jelas, regresi lebih cepat terdeteksi, dan refactor seperti penggantian hardcoded status ke enum bisa dilakukan dengan lebih percaya diri; namun ke depan saya perlu meningkatkan kualitas objective test dengan memastikan setiap test benar-benar memetakan risiko bisnis utama, menambahkan variasi edge case yang lebih sistematis, dan melakukan review berkala apakah test yang dibuat masih relevan terhadap perubahan requirement agar effort pengujian tetap bernilai tinggi.

> You have created unit tests in Tutorial. Now reflect whether your tests have successfully
followed F.I.R.S.T. principle or not. If not, explain things that you need to do the next time you
create more tests.

Secara umum test yang saya buat sudah mendekati prinsip F.I.R.S.T. karena cukup cepat, terisolasi dengan mocking pada level service, dapat diulang, dan ditulis per skenario kecil, tetapi masih ada ruang perbaikan terutama pada aspek truly independent (mengurangi ketergantungan data setup bersama yang terlalu besar), self-validating yang lebih eksplisit (assertion lebih spesifik per perilaku), serta timeliness (menjaga disiplin menulis test sebelum implementasi pada semua bagian), jadi pada pengerjaan berikutnya saya akan membuat fixture yang lebih ringkas per test, memperjelas nama test berdasarkan Given-When-Then, dan meminimalkan asumsi implisit agar test suite lebih robust.

## Bonus 2
> Explain what you think about your partner’s code? Are there any aspects that are still lacking
from your partner’s code?
Menurut saya kode buatan andi sudah berjalan dengan baik dan fitur utamanya bisa digunakan. Struktur project juga sudah cukup jelas karena dipisah ke layer controller, service, dan repository. Namun, masih ada beberapa aspek maintainability yang kurang, terutama pada konsistensi pola coding dan kontrak API antar-layer contohnya:
- Beberapa bagian masih menggunakan pendekatan yang rawan bug jangka panjang (misalnya `null` sebagai sinyal error).
- Ada inkonsistensi implementasi antar modul (misalnya cara dependency injection dan kontrak method repository).
- Ada beberapa bagian yang masih terlalu bergantung pada string literal untuk status, sehingga rentan typo.
- Beberapa test belum mengikuti kontrak baru setelah refactor, sehingga perlu penyesuaian agar tetap valid.

> What did you do to contribute to your partner’s code?
Kontribusi saya berfokus pada refactoring bertahap dengan commit kecil agar aman direview dan mudah di-trace. Saya melakukan:
- Cleanup dependency/import yang tidak relevan di controller.
- Migrasi field injection ke constructor injection pada controller dan service.
- Perubahan interface `findById` pada repository agar menggunakan `Optional`.
- Perubahan interface `findAll` pada repository dari `Iterator` ke `List`.
- Perbaikan interface`createOrder` agar eksplisit (tidak lagi `return null` saat order duplikat).
- Refactor API status pembayaran agar berbasis enum (`PaymentStatus`) untuk menghindari stringly typed logic.

> What code smells did you find on your partner’s code?
Berikut code smell utama yang saya temukan:
- **Unused imports** di beberapa controller.
- **Field injection (`@Autowired` pada field)** yang membuat dependency kurang eksplisit dan testing kurang ideal.
- **Null-based flow** pada repository/service (`findById` atau `createOrder` mengembalikan `null`).
- **Stringly typed status** pada payment service (`"SUCCESS"`, `"REJECTED"`) yang rentan typo.
- **Kontrak koleksi kurang konsisten** (`Iterator` dipaksa diproses ulang di service, menambah boilerplate).
- **Test coupling ke kontrak lama**, sehingga saat kontrak berubah banyak test ikut pecah.

> What refactoring steps did you suggest and execute to fix those smells?
Langkah yang saya sarankan sekaligus saya eksekusi:
1. **Dependency cleanup**
   - Hapus import dan dependency yang tidak dipakai agar file lebih bersih dan fokus.
2. **Constructor injection**
   - Ubah semua dependency di controller/service menjadi `private final` + constructor injection.
3. **Optional untuk `findById`**
   - Ubah repository agar return `Optional<T>`.
   - Sesuaikan service dan test agar menangani `Optional` dengan benar.
4. **List untuk `findAll`**
   - Ubah kontrak repository `findAll` dari `Iterator<T>` menjadi `List<T>`.
   - Sederhanakan service karena tidak perlu lagi konversi iterator ke list.
5. **Kontrak eksplisit `createOrder`**
   - Ganti perilaku `return null` saat duplikat menjadi `throw IllegalStateException`.
   - Update test agar menguji exception secara eksplisit.
6. **Enum-based payment status API**
   - Ubah `setStatus` agar menerima `PaymentStatus` enum, bukan string.
   - Lakukan konversi di boundary controller dan update test/stub terkait.