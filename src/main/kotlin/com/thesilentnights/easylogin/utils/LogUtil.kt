package com.thesilentnights.easylogin.utils

import com.thesilentnights.easylogin.repo.CommonStaticRepo
import kotlin.reflect.KClass

fun logInfo(clazz: KClass<*>, message: String) {
    CommonStaticRepo.log.info(clazz.simpleName + " : ", message)
}

fun logError(clazz: KClass<*>, message: String, error: Throwable) {
    CommonStaticRepo.log.error(clazz.simpleName + " : " + message, error)
}
