package com.example.qescreenloader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qegame.qeutil.QeUtil;
import com.qegame.qeutil.doing.Do;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity-TAG";

    private LinearLayout linearLayout;
    private FrameLayout fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.linearLayout = findViewById(R.id.linear);
        this.fragment = findViewById(R.id.fragment);

        QeScreenLoader qeScreenLoader = new QeScreenLoader();
        qeScreenLoader.setColorElements(Color.RED);
        //qeScreenLoader.setColorBackground(Color.RED);

        setFragment(qeScreenLoader);

       // LoaderManager.getInstance(this).initLoader(myLoader, Bundle.EMPTY, new StubLoaderCallbacks());

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    public static class StubLoader extends AsyncTaskLoader<Collection<Button>> {

        private MainActivity mainActivity;

        public StubLoader(MainActivity context) {
            super(context);
            this.mainActivity = context;
        }

        @Nullable
        @Override
        protected Collection<Button> onLoadInBackground() {
            Log.e(TAG, "onLoadInBackground: ");
            return super.onLoadInBackground();
        }
        @Override
        public Collection<Button> loadInBackground() {
            Log.e(TAG, "loadInBackground: ");
            // emulate long-running operation
            //SystemClock.sleep(2000);
            Collection<Button> collection = new ArrayList<>();
            QeUtil.testLog("TAG-BACK", new Do() {
                @Override
                public void work() {


                    for (int i = 0; i < 1000; i++) {
                        collection.add(new Button(getContext()));
                    }

                }
            });

            return collection;
        }
        @Override
        protected void onStartLoading() {
            Log.e(TAG, "onStartLoading: ");
            super.onStartLoading();
            forceLoad();

            mainActivity.fragment.setVisibility(View.VISIBLE);
            mainActivity.setFragment(new QeScreenLoader());
        }
        @Override
        protected void onForceLoad() {
            Log.e(TAG, "onForceLoad: ");
            super.onForceLoad();
        }
        @Override
        protected boolean onCancelLoad() {
            Log.e(TAG, "onCancelLoad: ");
            return super.onCancelLoad();
        }
        @Override
        public void cancelLoadInBackground() {
            Log.e(TAG, "cancelLoadInBackground: ");
            super.cancelLoadInBackground();
        }

        @Override
        public void onCanceled(@Nullable Collection<Button> data) {

            super.onCanceled(data);
        }

    }

    int myLoader = 1234;

    private class StubLoaderCallbacks implements LoaderManager.LoaderCallbacks<Collection<Button>> {

        @Override
        public Loader<Collection<Button>> onCreateLoader(int id, Bundle args) {
            if (id == myLoader) {
                return new StubLoader(MainActivity.this);
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Collection<Button>> loader, Collection<Button> data) {
            if (loader.getId() == myLoader) {
                fragment.setVisibility(View.GONE);

                QeUtil.testLog("TAG-FINISH", new Do() {
                    @Override
                    public void work() {
                        for (Button button : data) {
                            linearLayout.addView(button);
                        }
                    }
                });

                Toast.makeText(MainActivity.this, "Loader FINISH", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onLoaderReset(Loader<Collection<Button>> loader) {
            // Do nothing
        }

    }

}
