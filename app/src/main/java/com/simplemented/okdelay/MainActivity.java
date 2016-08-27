package com.simplemented.okdelay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView seekBarValueTextView;
    private SeekBar seekBar;
    private Button callRequestButton;
    private ProgressBar progressBar;
    private TextView responseTextView;

    private SimpleDelayProvider simpleDelayProvider;
    private GitHubService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        setupRetrofit();

        callRequestButton.setOnClickListener(view -> {
            callRequestButton.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            final long startTimeMillis = System.currentTimeMillis();

            service.listRepositories("pawel-schmidt", "updated")
                    .enqueue(new Callback<List<Repository>>() {
                        @Override
                        public void onResponse(final Call<List<Repository>> call,
                                               final Response<List<Repository>> response) {
                            callRequestButton.setEnabled(true);
                            progressBar.setVisibility(View.INVISIBLE);

                            final long elapsedMillis = System.currentTimeMillis() - startTimeMillis;
                            final String elapsedTime = getString(
                                    R.string.activity_main_seek_request_time_fmt, elapsedMillis);

                            final StringBuilder builder = new StringBuilder(elapsedTime);
                            for (final Repository repository : response.body()) {
                                builder.append(String.format("<br/><br/><a href=\"%s\">%s</a>",
                                        repository.getUrl(), repository.getName()));
                            }

                            responseTextView.setText(Html.fromHtml(builder.toString()));
                        }

                        @Override
                        public void onFailure(final Call<List<Repository>> call,
                                              final Throwable t) {
                            callRequestButton.setEnabled(true);
                            progressBar.setVisibility(View.INVISIBLE);
                            responseTextView.setText(getString(R.string.activity_main_error_fmt, t.getMessage()));
                        }
                    });
        });

        responseTextView.setMovementMethod(LinkMovementMethod.getInstance());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int i, final boolean b) {
                final long duration = i * 500L;
                simpleDelayProvider.setDelay(duration, TimeUnit.MILLISECONDS);
                seekBarValueTextView.setText(getString(R.string.activity_main_seek_bar_value_fmt, duration / 1000f));
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {

            }
        });

        seekBar.setProgress(5);
    }

    private void setupRetrofit() {
        simpleDelayProvider = new SimpleDelayProvider(1L, TimeUnit.SECONDS);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new DelayInterceptor(simpleDelayProvider))
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(GitHubService.class);
    }

    private void bindViews() {
        seekBarValueTextView = (TextView) findViewById(R.id.activity_main_seek_bar_value);
        seekBar = (SeekBar) findViewById(R.id.activity_main_seek_bar);
        callRequestButton = (Button) findViewById(R.id.activity_main_call_request);
        progressBar = (ProgressBar) findViewById(R.id.activity_main_progress_bar);
        responseTextView = (TextView) findViewById(R.id.activity_main_response);
    }
}
