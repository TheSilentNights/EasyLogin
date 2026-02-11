package com.thesilentnights.easylogin.service.task;

public interface Loop {
    //to execute code after task is removed from TaskService
    Task regenerate();
}
