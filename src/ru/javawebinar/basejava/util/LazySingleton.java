package ru.javawebinar.basejava.util;

public class LazySingleton {
    //volatile private static LazySingleton INSTANCE;

    private LazySingleton() {
    }

    private static class LasySingletonHolder{
        private static final LazySingleton INSTANCE = new LazySingleton();
    }

    public static LazySingleton getInstance() {
        return LasySingletonHolder.INSTANCE;
    }

//    public static LazySingleton getInstance() {
//        if (INSTANCE == null) {
//            synchronized (LazySingleton.class) {
//            if (INSTANCE == null) {
//                i = 13;
//                INSTANCE = new LazySingleton();
//            }
//        }
//        return INSTANCE;
//    }

}
