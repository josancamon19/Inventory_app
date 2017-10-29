package com.example.santiago.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.santiago.inventoryapp.data.ProductContract.ProductEntry;
import com.example.santiago.inventoryapp.data.ProductDbHelper;

public class ProductsActivity extends AppCompatActivity {
    private ProductDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductsActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });
        mDbHelper = new ProductDbHelper(this);
        displayInfo();
    }


    private void displayInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_STOCK};

        // Perform a query on the pets table
        Cursor cursor = db.query(
                ProductEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);
        TextView displayView = (TextView) findViewById(R.id.text_view_products);

        try {
            displayView.setText("The products table contains " + cursor.getCount() + " pets.\n\n");
            displayView.append(ProductEntry._ID + " - " +
                    ProductEntry.COLUMN_PRODUCT_NAME + " - " +
                    ProductEntry.COLUMN_PRODUCT_PRICE + " - " +
                    ProductEntry.COLUMN_PRODUCT_STOCK + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int breedColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
            int genderColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_STOCK);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentBreed = cursor.getString(breedColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentBreed + " - " +
                        currentGender));
            }
        }finally{
            cursor.close();
        }
    }
    private void insertProduct() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Gaseosa");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 1124);
        values.put(ProductEntry.COLUMN_PRODUCT_STOCK, 50 );

        // Insert a new row for Toto in the database, returning the ID of that new row.
        // The first argument for db.insert() is the pets table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Toto.
        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayInfo();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.products_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.InsertDummyData:
                insertProduct();
                displayInfo();
                // Do nothing for now
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.deleteAll:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
