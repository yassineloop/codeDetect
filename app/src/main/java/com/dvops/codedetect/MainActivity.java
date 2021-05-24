/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dvops.codedetect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dvops.codedetect.ui.Main;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.snackbar.Snackbar;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;

    private TextView statusMessage;
    private TextView barcodeValue;
    private Button read_barcode2;
    private Main maindb;
    private Context context;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusMessage = (TextView)findViewById(R.id.status_message);
        barcodeValue = (TextView)findViewById(R.id.barcode_value);
        read_barcode2 = (Button)findViewById(R.id.read_barcode2);
          bar = (ProgressBar) findViewById(R.id.progressBarb);
        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        findViewById(R.id.read_barcode).setOnClickListener(this);
        context = getApplicationContext();

        maindb = new Main(context);
        //maindb.example(context);


        read_barcode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //   maindb.delete();
            maindb.select();
            }
        });

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }


    }



    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    final Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    statusMessage.setText(R.string.barcode_success);
                    assert barcode != null;
                    barcodeValue.setText(barcode.displayValue);
                    new SelectOperation().execute(barcode.displayValue);

                             /* {
                                read_barcode2.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       maindb.insert(barcode.displayValue);
                                   }
                               });
                            }else{

                                Snackbar.make(findViewById(android.R.id.content), "Existe",
                                        Snackbar.LENGTH_LONG)
                                        .show();
                            }
                            */

                 //   Log.d(TAG, "Barcode read:exist  " +s);
                } else {
                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class SelectOperation extends AsyncTask<String, Void, Boolean> {
        String displayValue;

        boolean selected;
        int count=0;
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                displayValue =  params[0];
                selected = maindb.select(params[0]); // no need for a loop
           //    bar.isIndeterminate();
//                bar.setVisibility(View.VISIBLE);
                count++;
               // Thread.sleep(5000);
                Log.e("LongOperation",  displayValue+" "+count);

            } catch (Exception e) {
                Log.e("LongOperation", "Interrupted", e);
            }
            Log.e("LongOperation", "Interrupted"+ selected);

            return selected;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
         //   bar.setVisibility(View.INVISIBLE);
            Log.e("LongOperation", "Interrupted"+ aBoolean);

            if(!aBoolean){
                maindb.insert(displayValue);
                Snackbar.make(findViewById(android.R.id.content), "Insert",
                        Snackbar.LENGTH_LONG)
                        .show();
                displayValue=null;
            }else{
                Snackbar.make(findViewById(android.R.id.content), "Existe",
                        Snackbar.LENGTH_LONG)
                       .show();
                displayValue=null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }
}
