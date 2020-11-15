package com.digitaldeparturesystem.mapper;

import com.digitaldeparturesystem.pojo.Settings;

public interface SettingsMapper {

    Settings findOneByKey(String key);

    void addSetting(Settings settings);
}
