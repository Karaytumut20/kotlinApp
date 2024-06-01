// routes/tarifler.js dosyası

const express = require('express');
const router = express.Router();
const connection = require('../../config/db');
const verifyToken = require('../middleware/guard');

// Tarifleri listeleme
router.get('/', (req, res, next) => {
    connection.query('SELECT * FROM tarifler', (err, results) => {
        if (err) {
            return next(err);
        }
        res.json(results);
    });
});

router.get('/kategori/:category_id', (req, res, next) => {
    const category_id = req.params.category_id 

    connection.query('SELECT * FROM tarifler WHERE kategori_id = ?', [category_id], (err, results) => {
        if (err) {
            return next(err);
        }
        if (results.length === 0) {
            return res.status(404).send('Tarif bulunamadı');
        }
        res.json(results[0]);
    });
});

//tarif detayı görüntüleme
router.get('/detay/:id', (req, res, next) => {
    const id = req.params.id;

    connection.query('SELECT * FROM tarifler JOIN kategori ON kategori.id = tarifler.kategori_id WHERE tarifler.id = ?', [id], (err, results) => {
        if (err) {
            return next(err);
        }
        if (results.length === 0) {
            return res.status(404).send('Tarif bulunamadı');
        }
        res.json(results[0]);
    });
    
});

router.post('/', (req, res, next) => {
    const {id, adi, hazirlanma_zamani, malzemeler, hazirlanisi, kategori_id } = req.body;
    connection.query('INSERT INTO tarifler SET ?', {id, adi, hazirlanma_zamani, malzemeler, kategori_id, hazirlanisi }, (err, results) => {
        if (err) {
            return next(err);
        }
        res.status(201).json(results);
    });
})



module.exports = router;
