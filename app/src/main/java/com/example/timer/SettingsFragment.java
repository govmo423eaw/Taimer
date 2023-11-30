package com.example.timer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {
    // Метод onDestroy вызывается при уничтожении фрагмента
    @Override
    public void onDestroy() {
        super.onDestroy();

        // Отменяем регистрацию слушателя изменений настроек
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    // Метод onCreate вызывается при создании фрагмента
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Регистрируем слушатель изменений настроек
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    // Метод onCreatePreferences вызывается для создания экрана настроек
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        // Устанавливаем макет для экрана настроек из файла fragment_xml.xml
        addPreferencesFromResource(R.xml.fragment_xml);

        // Получаем экземпляр SharedPreferences для текущего экрана настроек
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

        // Получаем экземпляр PreferenceScreen для текущего экрана настроек
        PreferenceScreen preferenceScreen = getPreferenceScreen();

        // Получаем количество настроек на экране
        int count = preferenceScreen.getPreferenceCount();

        // Проходим по всем настройкам на экране
        for (int i = 0; i < count; i++) {
            Preference preference = preferenceScreen.getPreference(i);

            // Если настройка является списком на выбор
            if (preference instanceof ListPreference) {
                setLabel(preference, sharedPreferences.getString(preference.getKey(), ""));
            }
            // Если настройка является полем для ввода текста
            else if (preference instanceof EditTextPreference) {
                setEditLabel(preference);
            }
        }

        // Находим настройку с ключом "default_value"
        Preference preference = findPreference("default_value");

        // Устанавливаем слушатель изменений для данной настройки
        preference.setOnPreferenceChangeListener(this);
    }

    // Метод setLabel устанавливает подпись для настройки типа список
    private void setLabel(Preference preference, String value) {
        ListPreference listPreference = (ListPreference) preference;

        // Находим индекс выбранного значения в списке настроек
        int index = listPreference.findIndexOfValue(value);

        // Если значение найдено
        if (index >= 0) {
            // Устанавливаем подпись для настройки
            listPreference.setSummary(listPreference.getEntries()[index]);
        }
    }

    // Метод setEditLabel устанавливает подпись для настройки типа поле для ввода текста
    private void setEditLabel(Preference preference) {
        EditTextPreference editTextPreference = (EditTextPreference) preference;

        // Устанавливаем подпись для настройки
        editTextPreference.setSummary(editTextPreference.getText());
    }

    // Метод onSharedPreferenceChanged вызывается при изменении настройки
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        // Находим настройку по ключу
        Preference preference = findPreference(key);

        // Если настройка является списком на выбор
        if (preference instanceof ListPreference) {
            setLabel(preference, sharedPreferences.getString(preference.getKey(), ""));
        }
        // Если настройка является полем для ввода текста
        else if (preference instanceof EditTextPreference) {
            setEditLabel(preference);
        }
    }

    // Метод onPreferenceChange вызывается при изменении значения настройки
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        // Создаем всплывающее сообщение с предупреждением
        Toast toast = Toast.makeText(getContext(), "Value is not correct", Toast.LENGTH_LONG);

        // Проверяем, была ли изменена настройка с ключом "default_value"
        if (preference.getKey().equals("default_value")) {
            try {
                // Преобразуем новое значение в целое число
                int value = Integer.parseInt(newValue.toString());

                // Проверяем, что новое значение меньше или равно 1500
                if (value > 1500) {
                    Toast.makeText(getContext(), "Value must be less than 1500 sec", Toast.LENGTH_LONG).show();

                    // Возвращаем false, чтобы отменить изменение значения настройки
                    return false;
                }
            } catch (NumberFormatException nfe) {
                toast.show();

                // Возвращаем false, чтобы отменить изменение значения настройки
                return false;
            }
        }

        // Возвращаем true, чтобы разрешить изменение значения настройки
        return true;
    }
}

