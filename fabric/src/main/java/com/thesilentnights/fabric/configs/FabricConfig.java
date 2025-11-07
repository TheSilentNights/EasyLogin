package com.thesilentnights.fabric.configs;

import com.thesilentnights.configs.SqlType;
import com.thesilentnights.repo.CommonStaticRepo;
import lombok.Data;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Data
@Config(name = CommonStaticRepo.MOD_ID)
public class FabricConfig implements ConfigData {
    SqlType sqlType = SqlType.SQLITE;
    @Comment("path starts from gameFolder")
    String customPathToDatabase = "/EasyLogin/playerAccounts.db";
}
