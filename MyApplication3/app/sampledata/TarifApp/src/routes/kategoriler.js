// routes/kategoriler.js dosyası

const express = require('express');
const router = express.Router();
const connection = require('../../config/db');
const verifyToken = require('../middleware/guard');


// Kategorileri listeleme
router.get('/', verifyToken,(req, res, next) => {
    connection.query('SELECT * FROM kategori', (err, results) => {
        if (err) {
            return next(err);
        }
        res.json(results);
    });
});

router.post('/', verifyToken,(req, res, next) => {
    const { adi } = req.body;
    connection.query('INSERT INTO kategori SET ?', { adi }, (err, results) => {
        if (err) {
            return next(err);
        }
        res.status(201).json(results);
    });
})



module.exports = router;
