const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt');
const connection = require('../../config/db');
const jwt = require('jsonwebtoken');

// Kullanıcıları listeleme
router.get('/', (req, res, next) => {
    connection.query('SELECT * FROM kullanici', (err, results) => {
        if (err) {
            return next(err);
        }
        res.json(results);
    });
});

router.post('/login', (req, res) => {
    const { kullanici_adi, sifre } = req.body;

    if (!kullanici_adi || !sifre) {
        return res.status(400).json({ success: false, message: 'Kullanıcı adı ve şifre gereklidir' });
    }

    // Veritabanından kullanıcıyı alın
    connection.query('SELECT * FROM kullanici WHERE kullanici_adi = ?', [kullanici_adi], (err, results) => {
        if (err) {
            console.error('Veritabanı sorgu hatası:', err);
            return res.status(500).json({ success: false, message: 'Sunucu hatası' });
        }

        if (results.length === 0) {
            return res.status(401).json({ success: false, message: 'Kullanıcı bulunamadı' });
        }

        const kullanici = results[0];
        // Şifre karşılaştırması
        bcrypt.compare(sifre, kullanici.sifre, (err, result) => {
            if (err) {
                console.error('Bcrypt karşılaştırma hatası:', err);
                return res.status(500).json({ success: false, message: 'Sunucu hatası' });
            }

            if (result) {
                // Şifreler eşleşiyor, başarılı giriş
                // Erişim belirteci oluşturma
                const accessToken = jwt.sign({ user_id: kullanici.id }, 'gizliAnahtar', { expiresIn: '1h' });

                // Erişim belirtecini bir cookie'ye kaydetme
                res.cookie('access_token', accessToken, { httpOnly: true });

                return res.json({ success: true, message: 'Başarılı giriş', access_token: accessToken });
            } else {
                // Şifreler eşleşmiyor, başarısız giriş
                return res.status(401).json({ success: false, message: 'Hatalı giriş bilgileri' });
            }
        });
    });
});


// Kullanıcı kaydı
router.post('/register', (req, res) => {
    const { kullanici_adi, email, sifre } = req.body;

    // Kullanıcı adı veya email boş olamaz
    if (!kullanici_adi || !email) {
        return res.status(400).json({ success: false, message: 'Kullanıcı adı ve email gereklidir' });
    }

    // Şifreyi bcrypt ile hashleme
    bcrypt.hash(sifre, 10, (err, hash) => {
        if (err) {
            console.error('Bcrypt hashleme hatası:', err);
            return res.status(500).json({ success: false, message: 'Sunucu hatası' });
        }

        // Veritabanına yeni kullanıcı ekleme
        connection.query('INSERT INTO kullanici (kullanici_adi, email, sifre) VALUES (?, ?, ?)', [kullanici_adi, email, hash], (err, result) => {
            if (err) {
                console.error('Veritabanı ekleme hatası:', err);
                return res.status(500).json({ success: false, message: 'Kullanıcı kaydı oluşturulamadı' });
            }

            return res.json({ success: true, message: 'Kullanıcı kaydı başarıyla oluşturuldu' });
        });
    });
});

module.exports = router;
