package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> items;
    private Button btnAdd;
    private EditText etItem;
    private RecyclerView rvItems;
    private ItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items = new ArrayList<>();
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);
        adapter = new ItemsAdapter(items);
        rvItems.setAdapter(adapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(view -> handleAddItem());
        etItem.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                handleAddItem();
                return true;
            }
            return false;
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                items.remove(pos);
                adapter.notifyItemRemoved(pos);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                ColorDrawable swipeBackground = new ColorDrawable(Color.parseColor("#FF0000"));
                swipeBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                swipeBackground.draw(c);
                c.save();

                Drawable deleteIcon = ContextCompat.getDrawable(MainActivity.this, android.R.drawable.ic_delete);
                deleteIcon.setTint(Color.parseColor("#FFFFFF"));
                int iconMargin = (itemView.getBottom() - itemView.getTop() - deleteIcon.getIntrinsicHeight()) / 2;
                deleteIcon.setBounds(
                        itemView.getRight() - deleteIcon.getIntrinsicWidth(),
                        itemView.getTop() + iconMargin,
                        itemView.getRight(),
                        itemView.getBottom() - iconMargin
                );
                c.clipRect(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                deleteIcon.draw(c);
                c.restore();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvItems);
    }

    private void handleAddItem() {
        String item = etItem.getText().toString();
        Log.d("MainActivity", item);
        items.add(item);
        adapter.notifyItemInserted(items.size() - 1);
        etItem.setText("");
        Toast.makeText(MainActivity.this, "Item was added!", Toast.LENGTH_SHORT).show();
    }


}