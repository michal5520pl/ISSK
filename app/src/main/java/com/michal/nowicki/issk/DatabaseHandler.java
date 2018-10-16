package com.michal.nowicki.issk;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Szczur on 08.01.2018. Used by ISSK.
 */

class DatabaseHandler {
    private static class DatabaseOpener extends Thread {
        @Override
        public void run(){
            setDatabase(SQLiteDatabase.openOrCreateDatabase(getFileDir() + "local_data", null));
        }
    }

    private static class DatabaseReader extends Thread {
        @Override
        public void run(){
            setResult(getDatabase().query(getArguments()[0], getMatrixArguments()[0], getArguments()[1], getMatrixArguments()[1], getArguments()[2], getArguments()[3], getArguments()[4]));
        }
    }

    static class ValuesWrapper {
        static ContentValues wrapToContentValues(String[] keys, Object[] values){
            try {
                ContentValues contentValues = new ContentValues();

                for(int i = 0; i < keys.length; ++i){
                    if(values[i] == null){
                        contentValues.putNull(keys[i]);
                    }
                    else {
                        contentValues.put(keys[i], String.valueOf(values[i]));
                    }
                }

                return contentValues;
            }
            catch(Exception e){
                return null;
            }
        }
    }

    private static SQLiteDatabase database = null;
    private static String fileDir;
    private static String[] arguments;
    private static String[][] matrixArguments;
    private static Cursor result;

    //**getters**//
    private static SQLiteDatabase getDatabase(){
        return database;
    }

    private static String getFileDir(){
        return fileDir;
    }

    private static String[] getArguments(){
        return arguments;
    }

    private static String[][] getMatrixArguments(){
        return matrixArguments;
    }

    private static Cursor getResult(){
        return result;
    }

    //**setters**//
    private static void setDatabase(SQLiteDatabase input){
        database = input;
    }

    private static void setFileDir(String input){
        fileDir = input;
    }

    private static void setArguments(String[] input){
        arguments = input;
    }

    private static void setMatrixArguments(String[][] input){
        matrixArguments = input;
    }

    private static void setResult(Cursor input){
        result = input;
    }

    //**operations**//
    static void close(){
        getDatabase().close();
        setDatabase(null);
    }

    static String readFromDatabase(String[] arguments, String[][] matrixArguments, String fileDir){
        DatabaseReader reader = new DatabaseReader();

        if(getDatabase() == null){
            setFileDir(fileDir);

            DatabaseOpener opener = new DatabaseOpener();
            opener.run();
        }
        //^ jeśli nie ma załadowanej bazy, ładuje ją

        setArguments(arguments);
        setMatrixArguments(matrixArguments);

        reader.run();

        if(getResult().getType(getResult().getColumnIndex("email")) == Cursor.FIELD_TYPE_STRING && getResult().getCount() == 1){
            getResult().moveToFirst();
            return getResult().getString(getResult().getColumnIndex("email"));
        }
        else if(getResult().getType(getResult().getColumnIndex("email")) == Cursor.FIELD_TYPE_NULL){
            return null;
        }
        else if(getResult().getCount() > 1){
            getResult().moveToFirst();
            return getResult().getString(getResult().getColumnIndex("email"));
        }
        else {
            return "Wystąpił nieznany błąd.";
        }
    }

    static void insert(){}
}
