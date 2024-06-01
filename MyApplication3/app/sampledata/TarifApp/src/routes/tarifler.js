// routes/tarifler.js dosyası

const express = require('express');
const router = express.Router();
const connection = require('../../config/db');
const verifyToken = require('../middleware/guard');

// Tarifleri listeleme
router.get('/',verifyToken, (req, res, next) => {
    connection.query('SELECT * FROM tarifler', (err, results) => {
        if (err) {
            return next(err);
        }
        res.json(results);
    });
});

//tarif detayı görüntüleme
router.get('/:id',verifyToken, (req, res, next) => {
    const id = req.params.id;
    connection.query('SELECT * FROM tarifler WHERE id = ?', [id], (err, results) => {
        if (err) {
            return next(err);
        }
        if (results.length === 0) {
            return res.status(404).send('Tarif bulunamadı');
        }
        res.json(results[0]);
    });
});

router.post('/',verifyToken, (req, res, next) => {
    const { adi, hazirlanma_zamani, malzelemeler, hazirlanisi, katogori_id } = req.body;
    connection.query('INSERT INTO tarifler SET ?', { adi, hazirlanma_zamani, malzelemeler, katogori_id, hazirlanisi }, (err, results) => {
        if (err) {
            return next(err);
        }
        res.status(201).json(results);
    });
})



module.exports = router;
