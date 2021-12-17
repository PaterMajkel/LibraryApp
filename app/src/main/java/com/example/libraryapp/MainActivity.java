package com.example.libraryapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private BookViewModel bookViewModel;
    Book bookToEdit;

    public static final int NEW_BOOK_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_BOOK_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FloatingActionButton addBookButton = findViewById(R.id.add_button);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(MainActivity.this, EditBookActivity.class)
                        ;
                startActivityForResult(intent, NEW_BOOK_ACTIVITY_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview) ;
        final BookAdapter adapter = new BookAdapter ();
        recyclerView.setAdapter (adapter);
        recyclerView.setLayoutManager (new LinearLayoutManager(this));
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        bookViewModel.findAll ().observe (this, new Observer<List<Book>>() {
            @Override
            public void onChanged (@Nullable final List<Book> books) {
                adapter.setBooks (books);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_BOOK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Book book = new Book(data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_TITLE), data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_AUTHOR));
            bookViewModel.insert(book);
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_added), Snackbar.LENGTH_LONG).show();
        } else if(requestCode == EDIT_BOOK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            bookToEdit.setTitle(data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_TITLE));
            bookToEdit.setAuthor(data.getStringExtra(EditBookActivity.EXTRA_EDIT_BOOK_AUTHOR));
            bookViewModel.update(bookToEdit);
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_updated), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(findViewById (R.id.coordinator_layout), getString(R.string.empty_not_saved), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    private class BookHolder extends RecyclerView.ViewHolder {

        private TextView bookTitleTextView;
        private TextView bookAuthorTextView;

        Book book;

        public BookHolder (LayoutInflater inflater, ViewGroup parent) {
            super (inflater.inflate(R.layout.book_list_item, parent, false));
            bookTitleTextView = itemView.findViewById (R.id.book_title);
            bookAuthorTextView = itemView.findViewById (R.id.book_author);
            itemView.findViewById(R.id.bookItem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    bookToEdit = book;
                    Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
                    intent.putExtra("EXTRA_EDIT_BOOK_TITLE", bookTitleTextView.getText());
                    intent.putExtra("EXTRA_EDIT_BOOK_AUTHOR", bookAuthorTextView.getText());
                    startActivityForResult(intent, EDIT_BOOK_ACTIVITY_REQUEST_CODE);
                }
            });
            itemView.findViewById(R.id.bookItem).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    bookViewModel.delete(book);
                    return true;
                }
            });
        }

        public void bind (Book book) {
            bookTitleTextView.setText(book.getTitle());
            bookAuthorTextView.setText(book.getAuthor());
            this.book = book;
        }
    }

    private class BookAdapter extends RecyclerView.Adapter<BookHolder>{
        private List<Book> books;

        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            return new BookHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BookHolder holder, int position){
            if(books != null){
                Book book = books.get(position);
                holder.bind(book);
            }
            else{
                Log.d("MainActivity", "No books");
            }
        }
        @Override
        public int getItemCount(){
            if(books!=null){
                return books.size();
            }
            else{
                return 0;
            }
        }
        void setBooks(List<Book> books){
            this.books=books;
            notifyDataSetChanged();
        }
    }
}