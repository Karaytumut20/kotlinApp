// server.js dosyası

const express = require('express');
const app = express();
const port = 4000;

// Middleware'ler
app.use(express.json()); // JSON veri alışverişi için middleware
app.use(express.urlencoded({ extended: true })); // URL kodlu veri alışverişi için middleware

// Rota tanımları
const tariflerRouter = require('./src/routes/tarifler');
app.use('/tarif', tariflerRouter);

const kategorilerRouter = require('./src/routes/kategoriler');
app.use('/kategori', kategorilerRouter);

const kullanicilarRouter = require('./src/routes/kullanicilar');
app.use('/kullanici', kullanicilarRouter);


// 404 - Sayfa bulunamadı
app.use((req, res, next) => {
    res.status(404).send('Üzgünüz, sayfa bulunamadı!');
});

// Hata işleme middleware'i
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).send('Bir hata oluştu!');
});

// Uygulamayı belirtilen portta dinlemeye başlayın
app.listen(port, () => {
    console.log(`Uygulama ${port} portunda çalışıyor!`);
});
