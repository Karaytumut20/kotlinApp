// mysqlConnection.js dosyası

const mysql = require('mysql');

const connection = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'tarif_db'
});

connection.connect();

module.exports = connection;
