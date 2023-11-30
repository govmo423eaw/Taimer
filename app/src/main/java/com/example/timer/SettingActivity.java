package com.example.timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.os.Bundle;
import android.view.MenuItem;

// Класс SettingActivity расширяет класс AppCompatActivity и представляет активность настройки приложения.

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Устанавливаем макет для активности из файла activity_setting.xml
        setContentView(R.layout.activity_setting);

        // Получаем экземпляр ActionBar для текущей активности
        ActionBar actionBar = this.getSupportActionBar();

        // Если ActionBar существует, устанавливаем отображение кнопки "Вверх"
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Получаем идентификатор выбранного пункта меню
        int id = item.getItemId();

        // Проверяем, был ли выбран пункт меню "Вверх"
        if (id == android.R.id.home) {

            // Если да, выполняем навигацию вверх по иерархии активностей
            NavUtils.navigateUpFromSameTask(this);

            // Возвращаем true, чтобы указать, что пункт меню был обработан
            return true;
        }

        // Если выбран другой пункт меню, возвращаем true, чтобы указать, что пункт меню был обработан
        return true;
    }
}
