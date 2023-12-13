package com.example.qrclient;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

public class QrCodeScanner {

    public static String scanQrCode(Bitmap bitmap) {
        try {
            int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
            bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

            LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), pixels);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            QRCodeReader reader = new QRCodeReader();
            Result result = reader.decode(binaryBitmap);

            return result.getText();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

