package com.thesilentnights.configs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EasyLoginConfig {
    DataBaseType dataBaseType;
    String pathToDatabase;

}
