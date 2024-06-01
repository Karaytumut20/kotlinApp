// routes/kategoriler.js dosyası

const express = require('express');
const router = express.Router();
const connection = require('../../config/db');


// Kategorileri listeleme
router.get('/',(req, res, next) => {
    console.log(req.body);

    connection.query('SELECT * FROM kategori', (err, results) => {
        if (err) {
            return next(err);
        }
        res.json(results);
    });
});

router.post('/',(req, res, next) => {
    const { name } = req.body;
    console.log(req.body);

    connection.query('INSERT INTO kategori SET ?', { name }, (err, results) => {
        if (err) {
            return next(err);
        }
        res.status(201).json(results);
    });
})



module.exports = router;
