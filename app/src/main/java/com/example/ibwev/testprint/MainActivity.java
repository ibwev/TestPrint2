package com.example.ibwev.testprint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TransactionAdapter ta;
    String house, firstName, lastName, rentAmount, dueDay;
    int currentOwed;
    ListView listView;
    GregorianCalendar dp1, dp2, dp3, rt1,rt2,rt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ta = new TransactionAdapter(this,R.layout.row_layout_amount);
        listView = (ListView)findViewById(R.id.paymentHistoryListView);
        listView.setAdapter(ta);

        dp1 = new GregorianCalendar(2018, 07,01);
        dp2 = new GregorianCalendar(2018,06,01);
        dp3 = new GregorianCalendar(2018,05,01);
        rt1 = new GregorianCalendar(2018, 01,01);
        rt2 = new GregorianCalendar(2018, 02,01);
        rt3 = new GregorianCalendar(2018, 03,01);

        Transaction t1 = new Transaction(dp1,1000, "rent", rt1,50,"rent");
        Transaction t2 = new Transaction(dp2,1050, "rent", rt2,0,"rent");
        Transaction t3 = new Transaction(dp3,1000, "rent", rt3,0,"rent");

        ta.add(t1);
        ta.add(t2);
        ta.add(t3);

        house = "345 Somewhere Ave";
        firstName = "Sam";
        lastName = "Lot";
        rentAmount = "1,000";
        dueDay = "1";
        currentOwed = 100;
        }

        public void printListView(View view){
            //https://developer.android.com/training/printing/custom-docs
            // Get a PrintManager instance
            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);

            // Set job name, which will be displayed in the print queue
            String jobName = getString(R.string.app_name) + " Document";

            // Start a print job, passing in a PrintDocumentAdapter implementation
            // to handle the generation of a print document
            printManager.print(jobName, new MyPrintDocumentAdapter(this), null);
        }



    private class MyPrintDocumentAdapter extends PrintDocumentAdapter {

        PrintedPdfDocument mPdfDocument;
        int pages, elementCounter, pageCounter, itemsPerPage;
        Context mContext;
        boolean isFirstPage;

        public MyPrintDocumentAdapter(Context context) {
            super();
            mContext = context;
            elementCounter = 0;
            isFirstPage = true;
        }

        @Override
        public void onStart() {
            super.onStart();
        }
        @Override
        public void onLayout(PrintAttributes oldAttributes,
                             PrintAttributes newAttributes,
                             CancellationSignal cancellationSignal,
                             LayoutResultCallback callback,
                             Bundle metadata) {
            // Create a new PdfDocument with the requested page attributes
            mPdfDocument = new PrintedPdfDocument(mContext, newAttributes);

            // Respond to cancellation request
            if (cancellationSignal.isCanceled() ) {
                callback.onLayoutCancelled();
                return;
            }

            // Compute the expected number of printed pages
            pages = computePageCount(newAttributes);

            if (pages > 0) {
                // Return print information to print framework
                PrintDocumentInfo info = new PrintDocumentInfo
                        .Builder("print_output.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(pages)
                        .build();
                // Content layout reflow is complete
                callback.onLayoutFinished(info, true);
            } else {
                // Otherwise report an error to the print framework
                callback.onLayoutFailed("Page count calculation failed.");
            }
        }

        //The following sample demonstrates the basic mechanics of this process using the PrintedPdfDocument class to create a PDF file:

        @Override
        public void onWrite(final PageRange[] pageRanges,
                            final ParcelFileDescriptor destination,
                            final CancellationSignal cancellationSignal,
                            final WriteResultCallback callback) {
            try{

                // Iterate over each page of the document,
                // check if it's in the output range.
                for (int i = 0; i < pages; i++) {
                    // Check to see if this page is in the output range.
                    if (containsPage(pageRanges, i)) {
                        // If so, add it to writtenPagesArray. writtenPagesArray.size()
                        // is used to compute the next output page index.
                        ///////////////////////////////////////////////////////////////////writtenPagesArray.append(writtenPagesArray.size(), i);

                        PdfDocument.Page page = mPdfDocument.startPage(i);

                        // check for cancellation
                        if (cancellationSignal.isCanceled()) {
                            callback.onWriteCancelled();
                            mPdfDocument.close();
                            mPdfDocument = null;
                            return;
                        }

                        // Draw page content for printing
                        drawPage(page);

                        // Rendering is complete, so page can be finalized.
                        mPdfDocument.finishPage(page);
                    }
                }

                // Write PDF document to file
                mPdfDocument.writeTo(new FileOutputStream(
                        destination.getFileDescriptor()));
            }catch(IllegalStateException e){
                return;
            }
            catch (IOException e) {
                callback.onWriteFailed(e.toString());
                return;
            } catch (Exception e) {
                callback.onWriteFailed(e.toString());
                return;
            } finally {
                mPdfDocument.close();
                mPdfDocument = null;
            }
            //////////////////////////////////////////////////////////////////PageRange[] writtenPages = computeWrittenPages();
            // Signal the print framework the document is complete
            callback.onWriteFinished(pageRanges);

        }


        @Override
        public void onFinish() {
            super.onFinish();
        }

        // Check to see if this page is in the output range.
        private boolean containsPage(PageRange[] p,int i){
            boolean b = true;
            try{
                int start = p[0].getStart();
                int end = p[0].getEnd();
                if(!(i >= start && i <= end)){
                    b = false;
                }
            }catch (IllegalArgumentException e){
                b = false;
            }catch(java.lang.IndexOutOfBoundsException e){
                b= false;
            }
            return b;
        }


        private int computePageCount(PrintAttributes printAttributes) {
            itemsPerPage = 22; // default item count for portrait mode

            PrintAttributes.MediaSize pageSize = printAttributes.getMediaSize();
            if (!pageSize.isPortrait()) {
                // Six items per page in landscape orientation
                itemsPerPage = 15;
            }

            return (int) Math.ceil((float)ta.getCount() / itemsPerPage);  //use float to round correctly
        }


        private void drawPage(PdfDocument.Page page) {
            Canvas canvas = page.getCanvas(); //I think this is one canvas object per page
            Paint paint = new Paint();
            // units are in points (1/72 of an inch)
            int titleBaseLine = 72;
            int firstColumn = 54;
            int secondColumn = 150;
            int thirdColumn =250;
            int fourthColumn = 325;
            String lineString = "";
            int y = titleBaseLine;

            canvas = page.getCanvas();
            paint = new Paint();
            paint.setColor(Color.BLACK);
            if(isFirstPage) {
                paint.setTextSize(36);
                canvas.drawText("Payment History\n", firstColumn, titleBaseLine, paint);
                isFirstPage = false;
            }
            paint.setTextSize(11);
            String tenantInfo = house;
            y = getY(paint,titleBaseLine);
            canvas.drawText(house,firstColumn, y, paint);
            y = getY(paint, y);
            canvas.drawText(firstName + " " + lastName,firstColumn, y, paint);
            y = getY(paint, y);
            canvas.drawText("Rent Amount: " + rentAmount,firstColumn, y, paint);
            y = getY(paint, y);
            canvas.drawText("Due Day: " + dueDay,firstColumn, y, paint);
            y = getY(paint, y);
            canvas.drawText("Total Amount Owed: " + Integer.toString(currentOwed),firstColumn, y, paint);
            y = getY(paint, y);
            //https://stackoverflow.com/questions/17672150/how-to-show-current-date-in-edittext-view
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
            canvas.drawText(dateFormat.format(new Date()),firstColumn,y,paint);
            y = getY(paint, y);
            y = getY(paint, y);
            canvas.drawText("Date Entered",firstColumn, y, paint);
            canvas.drawText("Rent-To-Date",secondColumn,y,paint);
            canvas.drawText("Rent Owed / Description",fourthColumn,y,paint);
            canvas.drawText("Pay/",thirdColumn,y,paint);
            paint.setColor(Color.RED);
            canvas.drawText("Charge",thirdColumn + 25,y,paint);
            paint.setColor(Color.BLACK);
            y = getY(paint, y);

            for(int i = 0; i < itemsPerPage; i++) {  //should go to itemsPerPage

                if(elementCounter < ta.getCount()) {
                    paint.setColor(Color.BLACK);
                    y=getY(paint,y);
                    Transaction t = (Transaction) ta.getItem(elementCounter);  //use elementCount in case more than one page
                    elementCounter++;
                    canvas.drawText(t.stringDatePaid(),firstColumn,y,paint);
                    if (t.isPrePay) {
                        canvas.drawText(t.getTransactionType(),secondColumn,y,paint);
                    } else if (t.getTransactionType().equals("rent")) {
                        canvas.drawText(t.stringRentToDate(),secondColumn,y,paint);
                        canvas.drawText(Integer.toString(t.getOwedToRentTo()),fourthColumn,y,paint);
                    } else {
                        canvas.drawText(t.getTransactionType(),secondColumn,y,paint);
                        canvas.drawText(t.getDescription(),fourthColumn,y,paint);
                    }

                    if (t.getTransactionType().equals("bill")) {
                        paint.setColor(Color.RED);
                    } else {
                        paint.setColor(Color.BLACK);
                    }

                    canvas.drawText(Integer.toString(t.getAmountPaid()),thirdColumn,y,paint);

                    y = getY(paint, y);
                }else{
                    break;
                }

            }

        }

    }

    private int getY(Paint p, int y){
        int yreturn = y + (int)(p.descent() - p.ascent());
        return yreturn;
    }

}