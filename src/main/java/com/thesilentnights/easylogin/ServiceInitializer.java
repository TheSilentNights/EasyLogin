package com.thesilentnights.easylogin

import cn.hutool.core.io.FileUtil
import com.thesilentnights.easylogin.commands.admin.ByPass
import com.thesilentnights.easylogin.commands.admin.EmailTest
import com.thesilentnights.easylogin.commands.admin.PlayerInfoCommands
import com.thesilentnights.easylogin.commands.admin.TeleportToOfflinePlayer
import com.thesilentnights.easylogin.commands.common.*
import com.thesilentnights.easylogin.configs.DataBaseType
import com.thesilentnights.easylogin.configs.EasyLoginConfig
import com.thesilentnights.easylogin.events.listener.ActionListener
import com.thesilentnights.easylogin.events.listener.Listener
import com.thesilentnights.easylogin.service.*
import com.thesilentnights.easylogin.sql.DataSource
import com.thesilentnights.easylogin.sql.DatabaseChecker
import com.thesilentnights.easylogin.sql.DatasourceConfigs
import com.thesilentnights.easylogin.utils.PathAppender
import com.zaxxer.hikari.HikariDataSource
import net.minecraftforge.common.MinecraftForge


fun initialize() {


                }
                single { DatabaseChecker(get()).checkDatabase() }

                //service
                single { AccountService(get()) }
                single { ChangePasswordService(get()) }
                single { PreLoginService(get(), get()) }
                single { LoginService() }
                single { EmailService(get(), get()) }
                single { PasswordRecoveryService(get(), get()) }
                single { CommandRejectionService() }


                //common commands
                single { Login(get()) }
                single { Registrar(get()) }
                single { Logout() }
                single { Recover(get()) }
                single { ChangePassword(get()) }
                single { Email(get()) }


                //admin commands
                single { ByPass() }
                single { EmailTest() }
                single { PlayerInfoCommands() }
                single { TeleportToOfflinePlayer() }

                //listener
                single { Listener(get(), get(), get()) }
                single { ActionListener(MinecraftForge.EVENT_BUS, get()) }
            })
    }
}