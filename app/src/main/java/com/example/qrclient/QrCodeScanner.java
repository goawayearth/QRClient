package com.example.qrclient;
//import android.graphics.Bitmap;
//import com.google.zxing.BinaryBitmap;
//import com.google.zxing.DecodeHintType;
//import com.google.zxing.MultiFormatReader;
//import com.google.zxing.ReaderException;
//import com.google.zxing.Result;
//import com.google.zxing.common.HybridBinarizer;
//import com.google.zxing.qrcode.QRCodeReader;
//import com.google.zxing.RGBLuminanceSource;
//
//import java.util.EnumMap;
//import java.util.Map;
//
//public class QrCodeScanner {
//
//    public static String scanQRCode(Bitmap bitmap) {
//        if (bitmap != null) {
//            return decodeQRCode(bitmap);
//        }
//        return null;
//    }
//
//    private static String decodeQRCode(Bitmap bitmap) {
//        try {
//            int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
//            bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
//
//            RGBLuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), pixels);
//            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
//
//            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
//            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
//            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
//
//
//            QRCodeReader reader = new QRCodeReader();
//            Result result = reader.decode(binaryBitmap, hints);
//
//            return result.getText();
//        } catch (ReaderException e) {
//            // 处理解码异常
//            e.printStackTrace();
//        }
//        return null;
//    }
//}


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

