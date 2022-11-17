package com.example.tuempleo;

import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import static java.nio.file.StandardCopyOption.*;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class DownloadReportsActivity extends AppCompatActivity {

    private RadioButton rbtnGeneral;
    private RadioButton rbtnUser;
    private TextView mTextViewEmailUser;
    private EditText mEditTextEmailUser;
    private Button mButtonEmailUser;
    private RadioGroup mRadioGroupReports;
    static DocumentSnapshot documentSnapshot;
    private FirebaseFirestore mFirestore;
    private PdfDocument pdfDocument;
    private Document document;
    private Bitmap pie, bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_reports);

        rbtnGeneral = findViewById(R.id.radioButtonGeneral);
        rbtnUser = findViewById(R.id.radioButtonUser);
        mTextViewEmailUser = findViewById(R.id.textViewEmailUser);
        mButtonEmailUser = findViewById(R.id.buttonDownloadPdf);
        mEditTextEmailUser = findViewById(R.id.editTextEmailUser);
        mRadioGroupReports = findViewById(R.id.radioGroupReports);

        mTextViewEmailUser.setVisibility(View.INVISIBLE);
        mEditTextEmailUser.setVisibility(View.INVISIBLE);

        mFirestore = FirebaseFirestore.getInstance();

        pie = ReporteActivity.bitmapPieChart;
        bar = ReporteActivity.bitmapBarChart;

        if(checkPermission()) {
            Toast.makeText(this, "Permiso Aceptado", Toast.LENGTH_LONG).show();
        } else {
            requestPermissions();
        }

        mRadioGroupReports.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(rbtnUser.isChecked() == true){
                    mTextViewEmailUser.setVisibility(View.VISIBLE);
                    mEditTextEmailUser.setVisibility(View.VISIBLE);
                    mEditTextEmailUser.setEnabled(true);
                }else{
                    mTextViewEmailUser.setVisibility(View.INVISIBLE);
                    mEditTextEmailUser.setVisibility(View.INVISIBLE);
                    mEditTextEmailUser.setEnabled(false);
                }
            }
        });
        mButtonEmailUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    createPdf(rbtnUser.isChecked());
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });




    }
    private void createPdf (boolean userBoolean) throws FileNotFoundException{
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = null;
        File prueba;
        int con = 0;
        String path = "Reporte.pdf";
        boolean aux = false;
        while(file == null){
            prueba = new File(pdfPath, path);
            if(!prueba.exists()){
                file = new File(pdfPath, path);
            }
            con+=1;
            path = "Reporte(" + con + ").pdf";

        }

        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        pdfDocument = new PdfDocument(writer);
        document = new Document(pdfDocument);

        pdfDocument.setDefaultPageSize(PageSize.A5);
        document.setMargins(0,0,0,0);

        Drawable drawable = getDrawable(R.drawable.fondopdf);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);


        document.add(image);

        if(userBoolean){
            getInformation();
        }
        else{
            writeGeneral();
        }
    }
    private void writeUser(String id, String names, String lastName, String phone){
        Paragraph reportUser = new Paragraph("Reporte de usuarios").setBold().setFontSize(24).setTextAlignment(TextAlignment.CENTER);
        Paragraph description = new Paragraph("Un resumen de los datos básicos del cliente\n" +
                "esto con el fin de solucionar errores producidos en la base de datos").setTextAlignment(TextAlignment.CENTER).setFontSize(10);
        Paragraph user = new Paragraph("Usuario").setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(20);

        float[] widht = {100, 100};
        Table table = new Table(widht);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addCell(new Cell().add(new Paragraph("Id")));
        table.addCell(new Cell().add(new Paragraph(id)));

        table.addCell(new Cell().add(new Paragraph("Nombres")));
        table.addCell(new Cell().add(new Paragraph(names)));

        table.addCell(new Cell().add(new Paragraph("Apellidos")));
        table.addCell(new Cell().add(new Paragraph(lastName)));

        table.addCell(new Cell().add(new Paragraph("Teléfono")));
        table.addCell(new Cell().add(new Paragraph(phone)));


        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        table.addCell(new Cell().add(new Paragraph("Fecha")));
        table.addCell(new Cell().add(new Paragraph(LocalDate.now().format(dateFormatter).toString())));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a");
        table.addCell(new Cell().add(new Paragraph("Hora")));
        table.addCell(new Cell().add(new Paragraph(LocalTime.now().format(timeFormatter))));

        BarcodeQRCode qrCode = new BarcodeQRCode(id + "\n" + names + "\n" + lastName + "\n" + phone + "\n" + LocalDate.now().format(dateFormatter).toString() +
                "\n" + LocalTime.now().format(timeFormatter).toString());
        PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
        Image qrCodeImage = new Image(qrCodeObject).setWidth(80).setHorizontalAlignment(HorizontalAlignment.CENTER);

        document.add(reportUser);
        document.add(description);
        document.add(user);
        document.add(table);
        document.add(qrCodeImage);
        document.close();

        Toast.makeText(getApplicationContext(), "Documento creado exitosamente en descargas", Toast.LENGTH_SHORT).show();

    }

    private void writeGeneral(){
        Paragraph reportUser = new Paragraph("Reporte general").setBold().setFontSize(24).setTextAlignment(TextAlignment.CENTER);
        Paragraph description = new Paragraph("Un resumen general de los datos de la aplicación\n" +
                "esto con fines estadisticos").setTextAlignment(TextAlignment.CENTER).setFontSize(10);
        Paragraph user = new Paragraph("Graficas").setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(20);

        Image imagePie = makeImage(pie);
        Image imageBar = makeImage(bar);

        imagePie.scaleAbsolute(200, 200).setHorizontalAlignment(HorizontalAlignment.CENTER);
        imageBar.scaleAbsolute(215, 250).setHorizontalAlignment(HorizontalAlignment.CENTER);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Paragraph fecha = new Paragraph("Fecha: " + LocalDate.now().format(dateFormatter)).setTextAlignment(TextAlignment.LEFT).setFontSize(10);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a");
        Paragraph hora = new Paragraph("Hora: " + LocalTime.now().format(timeFormatter)).setTextAlignment(TextAlignment.LEFT).setFontSize(10);

        BarcodeQRCode qrCode = new BarcodeQRCode(
                "www.TuEmpleo.com" + "\n" + LocalDate.now().format(dateFormatter).toString() +
                "\n" + LocalTime.now().format(timeFormatter).toString());
        PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
        Image qrCodeImage = new Image(qrCodeObject).setWidth(80).setHorizontalAlignment(HorizontalAlignment.CENTER);

        document.add(reportUser);
        document.add(description);
        document.add(user);
        document.add(imagePie);
        document.add(imageBar);
        document.add(new Paragraph("\n\n\n"));
        document.add(qrCodeImage);
        document.add(hora);
        document.add(fecha);
        document.close();

        Toast.makeText(getApplicationContext(), "Documento creado exitosamente en descargas", Toast.LENGTH_SHORT).show();

    }

    private void getInformation(){
        mFirestore.collection("Usuario").document(mEditTextEmailUser.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        Toast.makeText(getApplicationContext(), "Procesando...", Toast.LENGTH_SHORT).show();
                        String id = documentSnapshot.getString("Id").toString();
                        String names = documentSnapshot.getString("Nombres").toString();
                        String lastName = documentSnapshot.getString("Apellidos").toString();
                        String phone = documentSnapshot.getString("Teléfono").toString();
                        writeUser(id, names, lastName, phone);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Correo electronico no registrado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), MANAGE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{MANAGE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 200);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 200) {
            if(grantResults.length > 0) {
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if(writeStorage && readStorage) {
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }
    private Image makeImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();
        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        return image;
    }
}