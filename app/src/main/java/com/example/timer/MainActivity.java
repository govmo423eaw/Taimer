package com.example.timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    SeekBar time;
    Button Start;
    TextView timer;
    CountDownTimer countDownTimer;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Инициализация элементов интерфейса
        time =  findViewById(R.id.seekBar2);
        Start = findViewById(R.id.button);
        timer = findViewById(R.id.editText);
        // Получение SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Установка максимального значения для SeekBar
        time.setMax(1500);
        // Установка значения по умолчанию из настроек
        setDefaultValueFromPreference(sharedPreferences);
        // Установка обработчика изменения значения SeekBar
        time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Обновление значения TextView в соответствии с изменением SeekBar
                seekBar.setProgress(progress);
                String firstVar = progress / 60 < 10 ? "0" + progress / 60 : String.valueOf(progress / 60) ;
                String secondVar = progress % 60 < 10 ? "0" + progress % 60 : String.valueOf(progress % 60) ;
                timer.setText(firstVar + ":" + secondVar);

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // Регистрация слушателя изменений настроек
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void setDefaultValueFromPreference(SharedPreferences sharedPreferences) {
        // Получение значения по умолчанию для SeekBar из
        int progress=0;
        try {
            progress = Integer.parseInt(sharedPreferences.getString("default_value","300"));
        }catch (NumberFormatException nfc){
            Toast.makeText(this,"Some EX",Toast.LENGTH_SHORT).show();
        }
        // Установка значения SeekBar и обновление TextView
        time.setProgress(progress);
        String firstVar = progress / 60 < 10 ? "0" + progress / 60 : String.valueOf(progress / 60) ;
        String secondVar = progress % 60 < 10 ? "0" + progress % 60 : String.valueOf(progress % 60) ;
        timer.setText(firstVar + ":" + secondVar);
    }

    public void ButtonClick(View view) {
        // При нажатии кнопки Start
        if (time.isEnabled()){
            // Отключение SeekBar и изменение текста кнопки на "Stop"
            time.setEnabled(false);
            Start.setText("Stop");
            // Создание и запуск объекта CountDownTimer
            countDownTimer = new CountDownTimer(time.getProgress()*1000,1000) {
                @Override
                public void onTick(long millisUntilFinished){
                    // Обновление значения SeekBar на каждом тике
                    time.setProgress((int)millisUntilFinished/1000);
                    Log.d("mil",millisUntilFinished+"");
                }
                @Override
                public void onFinish() {
                    // Воспроизведение звука и сброс таймера
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String sound = sharedPreferences.getString("soundOfBell","bell");;
                    // Проверка на включение звука в настройках
                    if (sharedPreferences.getBoolean("enabled_sound", true)) {
                        // Если звук включен, воспроизводится звук
                        if (sound.equals("bell")) {
                            // Создание и запуск аудиофайла "original" для воспроизведения звука
                            MediaPlayer mew = MediaPlayer.create(getApplicationContext(), R.raw.original);
                            mew.start();
                        }else {

                            MediaPlayer mew = MediaPlayer.create(getApplicationContext(), R.raw.original);
                            mew.start();
                        }
                    }
                    // Сброс таймера
                    resetTimer();
                }
            };
            countDownTimer.start();
        }else{
            // При нажатии кнопки Stop
            // Включение SeekBar, изменение текста кнопки на "Start" и сброс таймера
            resetTimer();
            countDownTimer.cancel();
        }
    }

    private void resetTimer() {
        // Включение SeekBar, изменение текста кнопки на "Start" и установка значения по умолчанию из настроек
        time.setEnabled(true);
        Start.setText("Start");
        setDefaultValueFromPreference(sharedPreferences);

    }

    // переопределенный метод, который вызывается при создании меню.
    // В нем мы используем объект MenuInflater для раздувания (inflate) XML-ресурса меню и добавления его в объект Menu.
    // Затем мы возвращаем значение true, чтобы показать, что меню было успешно создано.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }
    // Метод onOptionsItemSelected(@NonNull MenuItem item) - переопределенный метод,
    // который вызывается при выборе элемента меню. В нем мы проверяем идентификатор выбранного элемента и,
    // если это идентификатор элемента "Настройки", мы создаем и запускаем новое активити (SettingActivity).
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // Метод onDestroy() - переопределенный метод, который вызывается перед уничтожением активности. В нем мы отменяем регистрацию слушателя изменений настроек.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
    // Метод onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) - переопределенный метод,
    // который вызывается при изменении настроек приложения. В нашем случае, мы проверяем, было ли изменено значение с ключом "default_value",
    // и если да, то вызываем метод setDefaultValueFromPreference(sharedPreferences), который будет обновлять значение по умолчанию
    // в соответствии с новым значением настройки.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("default_value"))
            setDefaultValueFromPreference(sharedPreferences);
    }
}