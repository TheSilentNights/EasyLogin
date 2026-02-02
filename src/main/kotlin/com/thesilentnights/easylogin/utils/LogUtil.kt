package com.thesilentnights.easylogin.utils

import com.thesilentnights.easylogin.repo.CommonStaticRepo
import kotlin.reflect.KClass

object LogUtil {
    fun info(clazz: KClass<*>, message: String) {
        CommonStaticRepo.log.info(clazz.simpleName + " : ", message)
    }

    fun error(clazz: KClass<*>, message: String, error: Throwable) {
        CommonStaticRepo.log.error(clazz.simpleName + " : " + message, error)
    }
}