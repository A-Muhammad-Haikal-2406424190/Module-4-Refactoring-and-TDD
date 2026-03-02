# Reflection

## Explain what principles you apply to your project!

> SRP

Ya, saya sudah menerapkan SRP. SRP sendiri merupakan prinsip dimana sebuah class hanya boleh memilii 1 tanggung jawab. Pada kode before-solid terdapat struktur kode yang melanggar SRP ini, dimana pada kode sebelumnya controller untuk car dan product terdapat pada file yang sama dan juga car extend product sehingga hal ini membuat tanggung jawab/fungsi dari controller product menjadi lebih dari 1 yang jelas melanggar SRP. Oleh karena itu saya melakukan perbaikan dengan memisahkan kedua controller tersebut dan menghilangkan extend pada controller car.
- Before
    ```
    @Controller
    @RequestMapping("/car")
    class CarController extends ProductController {
    // Dimana sebelumnya productController menangani request untuk product dan car
    ```
- After
    ```
    @Controller
    @RequestMapping("/car")
    public class CarController {

    ......

    @Controller
    @RequestMapping("/product")
    public class ProductController {
    ```
Dapat dilihat after-solid sudah memisahkan agar car dan product punya class untuk controllernya masing2. Ini jelas memenuhi prinsip SRP.


> OCP 

Ya, OCP merupakan prinsip dimana suatu entitas boleh dipakai/diekstensi secara bebas (terbuka), namun tidak boleh dimodifikasi.
Pada kode before-solid dapat dilihat bahwa reposity product dan car adalah sebuah konkret class, dimana ketika terdapat perubahakan kebutuhan maka harus edit langsung class dari repository ini, jelas hal tersebut melanggar OCP karena terdapat modifikasi langsung pada entitas intinya. Karena itu saya melakukan perubahan agar repository menggunakan interface
Hal ini membuat ketika terjadi perubahan kebutuhan pada repositry maka kita tinggal ubah saja classnya dan tidak mengubah interfacenya (interface disini berperan sebagai entitas inti sehingga tidak boleh diubah-ubah dan class implementasi dari repository product dan car berubah yang awalnya merupakan entitas inti menjadi bukan entitas inti lagi)

> Liskov Substitution (LSP)

Pada kode before-solid dapat kita lihat bahwa controller car mengextend controller product dimana hal ini tidak wajar dan jelas melanggar LSP, kenapa? prinsip LSP mengharuskan child dapat dipakai mengikuti parentnya tanpa mengubah ekspetasi returnnya dan kalau kita lihat jelas bahwa car extend product akan merubah ekspetasi return child dari parentnya dimana controller product jelas akan digununkan untuk model, repository, dll untuk product sedangkan car digunakan model, repository, dll untuk car, dimana ini akan merusak ekspetasi car yang harusnya dipakai untuk product. Karena itu saya melakukan perbaikan dimana menghilangkan extend pada controller prodcut
perbaikan dengan memisahkan kedua controller tersebut dan menghilangkan extend pada controller car.
- Before
    ```
    @Controller
    @RequestMapping("/car")
    class CarController extends ProductController {
    // Dimana sebelumnya productController menangani request untuk product dan car
    ```
- After
    ```
    @Controller
    @RequestMapping("/car")
    public class CarController {

    ......

    @Controller
    @RequestMapping("/product")
    public class ProductController {
    ```
Dapat dilihat after-solid sudah menghilangkan extend car ke product. Ini jelas memenuhi prinsip LSP.

> Interface Segregation (ISP)

Untuk ISP saya tidak mengubah apapun. ISP sendiri berfokus agar suatu interface tidak memiliki metode yang berlebihan/diluar dari fugsi interface tersebut. Pada before-solid dapat dilahat bahwa masing2 interface hanya melakukan CRUD dan jelas metode2 yang ada pada interface semuanya hanya berfokus pada CRUD, dimana menurut saya hal tersebut tidak melanggar ISP.


> Dependency Inversion (DIP)

Pada kode before-solid dapat dilihat bahwa pada controller car langsung menginjeksi CarServiceImpl, bukan interface service. 
```
@Controller
@RequestMapping("/car")
class CarController extends ProductController {
    @Autowired
    private CarServiceImpl carservice;
.......
```
Dimana dengan langsung menginjeksi CarServideImpl maka CarController menjadi tight coupling terhadap implementasi konkret. Ini bertentangan dengan DIP yang menekankan bahwa modul level tinggi (controller) seharusnya bergantung pada abstraksi (CarService), sedangkan detail implementasi (CarServiceImpl) cukup menjadi pihak yang mengimplementasikan abstraksi tersebut. Dengan begitu, arah ketergantungan benar: detail bergantung pada kontrak, bukan kontrak yang “terkunci” oleh detail. Karena itu saya melakukan perubahan agar controller tidak langsung inject implementasi konkretnya melakinkan interfacenya
- Before
    ```
    @Controller
    @RequestMapping("/car")
    class CarController extends ProductController {
        @Autowired
        private CarServiceImpl carservice;
.......
- After
    ```
    @Controller
    @RequestMapping("/car")
    public class CarController {

        private final CarService carservice;

    
    ```
Dapat dilihat after-solid mengubah agar controller car inject CarService interface. Ini jelas memenuhi prinsip DIP.


## xplain the advantages of applying SOLID principles to your project with examples.

Penerapan prinsip SOLID pada proyek ini membuat struktur kode jauh lebih rapi, mudah dikembangkan, dan aman saat diubah karena tanggung jawab tiap komponen dipisah jelas (misalnya controller produk dan mobil tidak lagi tercampur), perluasan fitur dilakukan lewat kontrak abstraksi seperti BaseRepository dan BaseService tanpa banyak memodifikasi kode lama, relasi kelas menjadi lebih tepat sehingga mengurangi perilaku yang tidak konsisten, serta dependensi antarlayer menjadi longgar karena controller dan service bergantung pada interface, bukan implementasi konkret, yang pada akhirnya mempermudah pengujian (mocking), menurunkan risiko bug regresi, dan meningkatkan maintainability jangka panjang.

## Explain the disadvantages of not applying SOLID principles to your project with examples.

Tanpa menerapkan SOLID, proyek akan cepat menjadi sulit dirawat karena tanggung jawab antar komponen bercampur, perubahan kecil memicu efek domino, dan kode makin sulit diuji; contohnya pada versi lama ketika CarController menempel/bergantung pada struktur controller produk dan menginjeksi implementasi konkret (CarServiceImpl) secara langsung, maka perubahan detail service atau alur produk berisiko ikut merusak fitur mobil (coupling tinggi), selain itu saat repository masih berupa kelas konkret tanpa kontrak abstraksi, penambahan atau perubahan perilaku data (misalnya operasi edit/find) cenderung memaksa modifikasi kelas lama yang sudah stabil sehingga melanggar prinsip open for extension dan meningkatkan kemungkinan regresi, duplikasi logika, serta biaya maintenance jangka panjang.