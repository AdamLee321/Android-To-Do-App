package com.example.adaml.todolist;

import android.provider.BaseColumns;

/**
 * Created by adaml on 18/05/2018.
 */

public class ItemContract {

    private ItemContract(){}

    public static final class ItemEntry implements BaseColumns {
        public static final String DB_TABLE="Item";
        public static final String DB_COLUMN = "ItemName";
        public static final String DB_ISDONE = "ItemComplete";
    }
}
