package com.example.santiago.inventoryapp.data;

import android.provider.BaseColumns;

/**
 * Created by Santiago on 29/10/2017.
 */

public final class ProductContract {
    private ProductContract(){

    }
    public static final class ProductEntry implements BaseColumns {
        public final static String TABLE_NAME = "products";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME ="name";
        public final static String COLUMN_PRODUCT_PRICE ="price";
        public final static String COLUMN_PRODUCT_STOCK ="stock";


    }

    }
