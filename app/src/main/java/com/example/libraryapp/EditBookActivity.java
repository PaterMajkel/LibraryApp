package com.example.libraryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditBookActivity extends AppCompatActivity {

    public static final String EXTRA_EDIT_BOOK_TITLE = "com.example.zadanie9.EDIT_BOOK_TITLE";
    public static final String EXTRA_EDIT_BOOK_AUTHOR = "com.example.zadanie9.EDIT_BOOK AUTHOR";
    private EditText editTitleEditText;
    private EditText editAuthorEditText;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_edit_book) ;

        editTitleEditText = findViewById (R.id.edit_book_title);
        editAuthorEditText = findViewById (R.id.edit_book_author);

        if(getIntent().hasExtra("EXTRA_EDIT_BOOK_TITLE") && getIntent().hasExtra("EXTRA_EDIT_BOOK_AUTHOR")) {
            editTitleEditText.setText(getIntent().getStringExtra("EXTRA_EDIT_BOOK_TITLE"));
            editAuthorEditText.setText(getIntent().getStringExtra("EXTRA_EDIT_BOOK_AUTHOR"));
        }

        final Button button = findViewById (R.id.button_save);
        button.setOnClickListener (new View.OnClickListener () {
            public void onClick (View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(editTitleEditText.getText().toString()) || TextUtils.isEmpty(editAuthorEditText.getText().toString())) {
                    setResult (RESULT_CANCELED, replyIntent);
                } else {
                    String title = editTitleEditText.getText().toString();
                    replyIntent.putExtra(EXTRA_EDIT_BOOK_TITLE, title);
                    String author = editAuthorEditText.getText().toString();
                    replyIntent.putExtra(EXTRA_EDIT_BOOK_AUTHOR, author);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}