package moneyreader.virtualeye.io;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FileDownloadTask.TaskSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import ir.mahdi.mzip.zip.ZipArchive;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import moneyreader.virtualeye.io.MainActivity.Country;
import net.lingala.zip4j.util.InternalZipConstants;

public class CoutrySelectActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private TextToSpeech tts;

    /* renamed from: moneyreader.virtualeye.io.CoutrySelectActivity$1 */
    class C03081 implements OnItemClickListener {

        /* renamed from: moneyreader.virtualeye.io.CoutrySelectActivity$1$2 */
        class C03842 implements OnFailureListener {
            C03842() {
            }

            public void onFailure(@NonNull Exception exception) {
                CoutrySelectActivity.this.speakOut("Unable to download currency pack reason is : " + exception.getMessage());
            }
        }

        C03081() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String item = (String) adapterView.getItemAtPosition(i);
            Log.d("Country", item);
            final String country = item;
            if (new File(CoutrySelectActivity.this.getCacheDir().getAbsolutePath() + InternalZipConstants.ZIP_FILE_SEPARATOR + ((Country) MainActivity.currencyAbberivation.get(country)).name).isDirectory()) {
                CoutrySelectActivity.this.getSharedPreferences("setting", 0).edit().putString("country_", country).apply();
                CoutrySelectActivity.this.getSharedPreferences("setting", 0).edit().putInt("first_run_", 1).apply();
                CoutrySelectActivity.this.goBack();
                return;
            }
            final StorageReference pathReference = CoutrySelectActivity.this.storage.getReference().child(((Country) MainActivity.currencyAbberivation.get(country)).name + ".zip");
            CoutrySelectActivity.this.mProgressDialog = ProgressDialog.show(CoutrySelectActivity.this, "Downloading " + country + "'s currency feature pack", "", true, true, new OnCancelListener() {
                public void onCancel(DialogInterface dialogInterface) {
                    ((FileDownloadTask) pathReference.getActiveDownloadTasks().get(0)).cancel();
                    CoutrySelectActivity.this.speakOut("Downloading Aborted.");
                }
            });
            try {
                final File localFile = File.createTempFile(country, "zip");
                pathReference.getFile(localFile).addOnProgressListener(new OnProgressListener<TaskSnapshot>() {
                    public void onProgress(TaskSnapshot taskSnapshot) {
                        int percentage = (int) ((((double) taskSnapshot.getBytesTransferred()) / ((double) taskSnapshot.getTotalByteCount())) * 100.0d);
                        CoutrySelectActivity.this.mProgressDialog.setProgress(percentage);
                        Log.d("Downloading", percentage + "," + taskSnapshot.getBytesTransferred());
                        if (percentage >= 100) {
                            CoutrySelectActivity.this.speakOut("Downloading Completed. Extracting Currency Pack, Please wait");
                            ZipArchive zipArchive = new ZipArchive();
                            ZipArchive.unzip(localFile.getAbsolutePath(), localFile.getParent(), "");
                            CoutrySelectActivity.this.getSharedPreferences("setting", 0).edit().putString("country_", country).apply();
                            CoutrySelectActivity.this.getSharedPreferences("setting", 0).edit().putInt("first_run_", 1).apply();
                            CoutrySelectActivity.this.speakOut("Extraction Finished. Starting Camera.");
                            localFile.delete();
                            CoutrySelectActivity.this.goBack();
                        }
                    }
                }).addOnFailureListener(new C03842());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0313R.layout.activity_coutry_select);
        this.tts = new TextToSpeech(this, null);
        String[] countries = getIntent().getStringArrayExtra("countries");
        Arrays.sort(countries);
        ListView listView = (ListView) findViewById(C0313R.id.country_list);
        listView.setAdapter(new ArrayAdapter(this, 17367043, countries));
        listView.setOnItemClickListener(new C03081());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                goBack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(67108864);
        startActivity(intent);
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.tts != null) {
            this.tts.stop();
            this.tts.shutdown();
        }
    }

    private void speakOut(String text) {
        this.tts.speak(text, 1, null);
    }
}
