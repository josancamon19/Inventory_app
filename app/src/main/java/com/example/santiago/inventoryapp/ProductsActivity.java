package com.example.santiago.inventoryapp;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.santiago.inventoryapp.data.ProductContract.ProductEntry;
import com.example.santiago.inventoryapp.data.ProductDbHelper;

import java.io.ByteArrayOutputStream;

public class ProductsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private ProductCursorAdapter adapter;
    private static final int PRODUCT_LOADER = 0;

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
        ListView list = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        list.setEmptyView(emptyView);

        adapter = new ProductCursorAdapter(this,null);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProductsActivity.this,EditActivity.class);
                Uri currentUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI,id);
                intent.setData(currentUri);
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(PRODUCT_LOADER, null, this);

    }
    private byte[] convertToByte (Drawable imageFromFile) {
        //Convert to bitmap
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageFromFile);
        Bitmap bitmap = bitmapDrawable .getBitmap();
        //Convert to byte to store
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] imageByte = bos.toByteArray();
        return imageByte;
    }

    private void insertProduct() {
        Drawable productImage = getDrawable(R.drawable.gaseosa);
        byte[] image =convertToByte(productImage);

        // Create a ContentValues object where column names are the keys,
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Gaseosa");
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, 2500);
        values.put(ProductEntry.COLUMN_PRODUCT_STOCK, 50 );
        values.put(ProductEntry.COLUMN_IMAGE,image);

        Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);
    }
    private void deleteAllProducts() {
        int rowsDeleted = getContentResolver().delete(ProductEntry.CONTENT_URI, null, null);
        if (rowsDeleted > 0) {
            Toast.makeText(ProductsActivity.this, "Products Deleted",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ProductsActivity.this, "You dont have products to delete",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listenersasdasdasd
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to delete all pets! ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteAllProducts();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
                return true;

            case R.id.deleteAll:

                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_STOCK};

        // Perform a query on the pets table
        return new CursorLoader(this,
                ProductEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);

    }
}
