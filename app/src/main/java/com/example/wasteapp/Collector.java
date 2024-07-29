package com.example.wasteapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wasteapp.person.Person;
import com.example.wasteapp.person.PersonAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Collector extends AppCompatActivity implements PersonAdapter.OnItemClickListener {
    private static final String TAG = "Collector";
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;
    private static final String CHANNEL_ID = "report_channel";
    private DBHelper dbHelper;
    private ScheduleDBHelper scheduleDBHelper;
    private PersonAdapter personAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Person> personList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        }

        createNotificationChannel();

        try {
            recyclerView = findViewById(R.id.recyclerView);
            if (recyclerView == null) {
                throw new NullPointerException("RecyclerView not found in layout");
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            dbHelper = new DBHelper(this);
            scheduleDBHelper = new ScheduleDBHelper(this);
            displayData();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: ", e);
            Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Button backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Collector.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        Button viewAcceptedButton = findViewById(R.id.btnViewAccepted);
        viewAcceptedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Collector.this, AcceptedRequestsActivity.class);
                startActivity(intent);
            }
        });

        Button addPriceButton = findViewById(R.id.btnAddPrice);
        addPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Collector.this, AddPriceActivity.class);
                startActivity(intent);
            }
        });

        Button addScheduleButton = findViewById(R.id.btnAddSchedule);
        addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Collector.this, AddScheduleActivity.class);
                startActivity(intent);
            }
        });

        Button generateReportButton = findViewById(R.id.btnGenerateReport);
        generateReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateReport();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Write External Storage permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Write External Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayData() {
        try {
            Cursor res = dbHelper.getAllPendingRequests();
            if (res == null) {
                throw new NullPointerException("Cursor is null");
            }
            if (res.getCount() == 0) {
                Toast.makeText(this, "No requests available", Toast.LENGTH_SHORT).show();
                return;
            }

            personList = new ArrayList<>();
            while (res.moveToNext()) {
                personList.add(new Person(
                        res.getString(0),
                        res.getString(1),
                        res.getString(2),
                        res.getString(3),
                        res.getString(4)
                ));
            }

            personAdapter = new PersonAdapter(this, personList, this, true);
            recyclerView.setAdapter(personAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error in displayData: ", e);
            Toast.makeText(this, "Error loading data: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAcceptClick(Person person) {
        try {
            acceptRequest(person);
        } catch (Exception e) {
            Log.e(TAG, "Error in onAcceptClick: ", e);
            Toast.makeText(this, "Error accepting request: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void acceptRequest(Person person) {
        boolean isUpdated = dbHelper.updateRequestStatus(person.getId(), "Accepted");
        if (isUpdated) {
            Toast.makeText(this, "Request accepted", Toast.LENGTH_SHORT).show();
            personList.remove(person);
            personAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Failed to accept request", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateReport() {
        try {
            StringBuilder reportContent = new StringBuilder();
            reportContent.append("Schedules Made:\n");
            Cursor scheduleCursor = scheduleDBHelper.getAllSchedules();
            while (scheduleCursor.moveToNext()) {
                reportContent.append("Date: ").append(scheduleCursor.getString(1))
                        .append(", Time: ").append(scheduleCursor.getString(2))
                        .append(", Location: ").append(scheduleCursor.getString(3))
                        .append(", Waste Type: ").append(scheduleCursor.getString(4))
                        .append("\n");
            }
            scheduleCursor.close();

            reportContent.append("\nRequests Accepted:\n");
            Cursor acceptedRequestsCursor = dbHelper.getAllAcceptedRequests();
            while (acceptedRequestsCursor.moveToNext()) {
                reportContent.append("Full Name: ").append(acceptedRequestsCursor.getString(1))
                        .append(", Phone Number: ").append(acceptedRequestsCursor.getString(2))
                        .append(", Location: ").append(acceptedRequestsCursor.getString(3))
                        .append(", Waste Type: ").append(acceptedRequestsCursor.getString(4))
                        .append("\n");
            }
            acceptedRequestsCursor.close();

            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setTextSize(12);

            int x = 10, y = 25;
            for (String line : reportContent.toString().split("\n")) {
                canvas.drawText(line, x, y, paint);
                y += paint.descent() - paint.ascent();
                if (y > pageInfo.getPageHeight() - 25) {
                    document.finishPage(page);
                    page = document.startPage(pageInfo);
                    canvas = page.getCanvas();
                    y = 25;
                }
            }

            document.finishPage(page);
            File reportFile = new File(getExternalFilesDir(null), "report.pdf");
            FileOutputStream outputStream = new FileOutputStream(reportFile);
            document.writeTo(outputStream);
            document.close();

            showNotification(reportFile);

            Toast.makeText(this, "PDF report generated successfully: " + reportFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, "Error in generateReport: ", e);
            Toast.makeText(this, "Error generating PDF report: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showNotification(File reportFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri reportUri = FileProvider.getUriForFile(this, "com.example.wasteapp.fileprovider", reportFile);
        intent.setDataAndType(reportUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_report)
                .setContentTitle("Report Generated")
                .setContentText("Tap to view the report")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Report Channel";
            String description = "Channel for report notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
