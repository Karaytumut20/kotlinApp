-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1
-- Üretim Zamanı: 30 May 2024, 22:28:07
-- Sunucu sürümü: 10.4.32-MariaDB
-- PHP Sürümü: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `tarif_db`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `kategori`
--

CREATE TABLE `kategori` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `kategori`
--

INSERT INTO `kategori` (`id`, `name`) VALUES
(0, 'corbalar'),
(2, 'u'),
(3, 'ww'),
(4, 'yemeggg'),
(5, 's'),
(6, 'd'),
(8, 'ana yemek '),
(9, 'mama'),
(10, 'salata'),
(11, 'makarna');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `kullanici`
--

CREATE TABLE `kullanici` (
  `id` int(11) NOT NULL,
  `kullanici_adi` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `sifre` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `kullanici`
--

INSERT INTO `kullanici` (`id`, `kullanici_adi`, `email`, `sifre`) VALUES
(1, 'test', '', '$2b$10$1MpkJZO5c.69dQNhV97oS.K0w8/CSTzyBwWN9Cy/MfW31kHDKexB6'),
(1222, 'umut', '', 'umut'),
(1223, 'test', 'test@gmail.com', '$2b$10$.vTETxNlN.AtmJJvT23tB.oI8nzPZn30frz6U7ALcTRlGnvdtH4CS'),
(1224, 'test', 'test@gmail.com', '$2b$10$n16HsVDFi2i1jQJW9RMO3.ueV1YrZYZClnoGiOyEZFgRf2KX5YQGa'),
(1225, 'test', 'test@gmail.com', '$2b$10$kHQNBojYa1ksrPR5QTl7.OeGel0QLyM5wUhHT0ahuhnC5EpRV2S1.'),
(1226, 'u', 'umut@gmail.com', '$2b$10$KzPLTk9bhEQ5591Zi1Vt/OMi7UNCVNrZPfpDChtg310NHzV4XrpC.'),
(1227, 'umut', 'ka@gmail.com', '$2b$10$WGEtrQYBnJtKyycnkE5ZSudI8Z7JuKtzEMRXeEDPARBDrl0bCW.ja'),
(1228, 'jj', 'kkk@gmail.com', '$2b$10$M7KZfQjCqT3/Yu.T9LN6lutDz4qi29ebRXGDKGclJUCgKuQW43Hl.'),
(1229, 'k', 'k@gmail.com', '$2b$10$9QcouonRW.CqFIZmltjQKOYsYaZ5VBuMPHz1yCLyQU4uDobI7T22i'),
(1230, 'umutt', 'umutt@gmail.com', '$2b$10$jn17d/0j8YJtx/y1acvmwOkO3n76lSY04wS7aJpQlb5axV96KgZE6'),
(1231, 'muge', 'muge@gmail.com', '$2b$10$CaGg1rE9BWNpnlcjvu0RsezokHhU0gfL7FVwA/YIbCs1hSkaeT39O'),
(1232, 'umut', 'umut@gmail.com', '$2b$10$Z/AXosu7UWZhJOf0PhIBRuLWDmv5TCDcy213BLrVtyyp4YrJXyyd2'),
(1233, 'erkin', 'erkin@gmail.com', '$2b$10$kyUpwl3Lpg10TSDJ.mXh6OYzGnJgpPjV8l6P3wYQeZg.m6yEPHjKq'),
(1234, 'apo', 'apo@gmail.com', '$2b$10$btgC.gQ0QU1yiWtQm3LNZu/.3yH3zEuP/40DkvRLURgop/qJIYCZa'),
(1235, 'deneme', 'deneme@gmail.com', '$2b$10$pnYlOBYJsG3H0pdwljNRBOzQOLTik/G7/u8IXuNTwzo8FtxacZIzu');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `tarifler`
--

CREATE TABLE `tarifler` (
  `id` int(11) NOT NULL,
  `adi` varchar(255) NOT NULL,
  `hazirlanma_zamani` int(11) NOT NULL,
  `malzemeler` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `kategori_id` int(11) NOT NULL,
  `hazirlanisi` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Tablo döküm verisi `tarifler`
--

INSERT INTO `tarifler` (`id`, `adi`, `hazirlanma_zamani`, `malzemeler`, `kategori_id`, `hazirlanisi`) VALUES
(1, 'test', 3, 'gkfopdagkjdf gofdjapg gjofpdsajg', 2, 'gfsgfsaghj gıofjdh ıgjhfdıoagj ıoghfoıj uotghfsog'),
(4, 'yumurtaaaaaaa', 3, 'sadada', 1, 'sadadasdas'),
(9, 'patates kizartmas', 2, 'patates yag', 7, 'kansfsafasfa'),
(10, 'aaaaaaaaaaaaaaaaaaaaa', 0, 'aaaaa', 0, 'zaaaaaaaaaa\"'),
(11, 'pasta', 44, 'pastaaaaa', 4, 'sjakf asfjafjasfa fa'),
(12, 'ss', 0, 's', 8, 's'),
(13, 'sucuklu yumurta', 5, 'sucuk yumurta tava yag', 8, 'yaga dok'),
(14, 'amerkan salatas', 20, 'makarna yogurt mayonez bezelye', 10, 'hepsn br kaba koyup karstrn'),
(15, 'ss', 0, 'ssss', 11, 'ss');

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `kategori`
--
ALTER TABLE `kategori`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `kullanici`
--
ALTER TABLE `kullanici`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `tarifler`
--
ALTER TABLE `tarifler`
  ADD PRIMARY KEY (`id`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `kategori`
--
ALTER TABLE `kategori`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Tablo için AUTO_INCREMENT değeri `kullanici`
--
ALTER TABLE `kullanici`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1236;

--
-- Tablo için AUTO_INCREMENT değeri `tarifler`
--
ALTER TABLE `tarifler`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
