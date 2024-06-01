// Middleware: Erişim belirteci kontrolü
const jwt = require('jsonwebtoken');

const verifyToken = (req, res, next) => {
    // İsteğin başlıklarından erişim belirtecini al

    const token = req.headers?.authorization?.split(' ')[1];

    // Erişim belirteci yoksa, yetkilendirme hatası dön
    if (!token) {
        return res.status(403).json({ success: false, message: 'Yetkilendirme hatası: Erişim belirteci bulunamadı' });
    }

    // Erişim belirtecini kontrol et
    jwt.verify(token, 'gizliAnahtar', (err, decoded) => {
        if (err) {
            console.error('Erişim belirteci doğrulama hatası:', err);
            return res.status(401).json({ success: false, message: 'Yetkilendirme hatası: Geçersiz erişim belirteci' });
        }

        // Erişim belirteci geçerliyse, kullanıcı kimliğini isteğe ekle ve bir sonraki middleware'e devam et
        req.user_id = decoded.user_id;
        next();
    });
};

module.exports = verifyToken;
