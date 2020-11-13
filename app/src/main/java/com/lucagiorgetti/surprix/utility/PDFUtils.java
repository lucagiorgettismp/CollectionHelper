package com.lucagiorgetti.surprix.utility;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.lucagiorgetti.surprix.BuildConfig;
import com.lucagiorgetti.surprix.R;
import com.lucagiorgetti.surprix.SurprixApplication;
import com.lucagiorgetti.surprix.model.Missing;
import com.lucagiorgetti.surprix.model.MissingSurprise;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PDFUtils {
    public static void createMissingListPdfFile(FragmentActivity activity, List<MissingSurprise> missingSurprises, String path) throws IOException {
        try {
            Document document = new Document();
            //File newFile = File.createTempFile("prova.pdf", null, SurprixApplication.getSurprixContext().getCacheDir());

            File cacheFile = new File(SurprixApplication.getSurprixContext().getCacheDir(), "doc/prova.pdf");
            cacheFile.createNewFile();

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(cacheFile));

            MyEvent myEvent = new MyEvent("logo_shape.jpg");

            writer.setPageEvent(myEvent);

            document.open();

            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor(SurprixApplication.getInstance().getResources().getString(R.string.app_name));
            document.addCreator(SurprixApplication.getInstance().getResources().getString(R.string.app_name));
            HashMap<String, List<Missing>> hashMap = new HashMap<>();

            BaseColor colorAccent = new BaseColor(ContextCompat.getColor(SurprixApplication.getSurprixContext(), R.color.surprixBlue));
            float fontSize = 12.0f;

            BaseFont fontName = BaseFont.createFont();

            Font titleFont = new Font(fontName, 20.0f, Font.BOLD, colorAccent);
            String title = String.format(Locale.getDefault(), "%s %s (%d)", SurprixApplication.getInstance().getResources().getString(R.string.missings), SurprixApplication.getInstance().getCurrentUser().getUsername(), missingSurprises.size());
            addNewItem(document, title, Element.ALIGN_CENTER, titleFont);
            addLineSeparator(document, colorAccent);

            Collections.sort(missingSurprises, MissingSurprise::compareTo);
            for (MissingSurprise missing : missingSurprises) {
                Font orderNumberFont = new Font(fontName, fontSize, Font.NORMAL, BaseColor.BLACK);
                try {
                    String surpriseString = String.format(Locale.getDefault(), "%s: %s - %s, %s %s, %s", missing.getSurprise().getCode(), missing.getSurprise().getDescription(), missing.getSurprise().getSet_name(), missing.getSurprise().getSet_producer_name(), missing.getSurprise().getSet_product_name(), missing.getSurprise().getSet_year());
                    addNewItem(document, surpriseString, Element.ALIGN_LEFT, orderNumberFont);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
            document.close();

            Uri uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", cacheFile);
            String mime = SurprixApplication.getInstance().getContentResolver().getType(uri);

            // Open file with user selected app
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, mime);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(intent);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void addLineSeparator(Document document, BaseColor colorAccent) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(colorAccent);
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private static void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private static void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);

        document.add(paragraph);
    }

    protected static class MyEvent extends PdfPageEventHelper {
        Image image;

        MyEvent(String path) throws IOException, BadElementException {
            InputStream is = SurprixApplication.getInstance().getAssets().open(path);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
            Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 50, stream);

            byte[] byteArray = stream.toByteArray();
            Image jpg = Image.getInstance(byteArray);
            jpg.scaleToFit(500, 700);
            jpg.setAbsolutePosition(60, 90);
            this.image = jpg;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            super.onEndPage(writer, document);
            try {
                PdfContentByte canvas = writer.getDirectContent();
                canvas.saveState();
                PdfGState state = new PdfGState();
                state.setFillOpacity(0.2f);
                canvas.setGState(state);
                canvas.addImage(image);
                canvas.restoreState();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }
}
