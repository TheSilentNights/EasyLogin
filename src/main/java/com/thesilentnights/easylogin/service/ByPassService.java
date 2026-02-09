package com.thesilentnights.easylogin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ByPassService {

    private static final List<UUID> list = new ArrayList<>();


    public static void addBypass(UUID uuid) {
        list.add(uuid);
    }

    public static boolean isBypassed(UUID uuid) {
        return list.contains(uuid);
    }

    public void removeBypass(UUID uuid) {
        list.remove(uuid);
    }
}