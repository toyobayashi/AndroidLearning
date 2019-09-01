package com.github.toyobayashi.al;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RVActivity extends AppCompatActivity {
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        rv = findViewById(R.id.rvMain);
        rv.setLayoutManager(new LinearLayoutManager(this));
        String[] list = {
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
            "aaa", "bbb", "ccc",
        };
        rv.setAdapter(new RVAdapter(list, (v) -> {
            Toast.makeText(this, v.getText(), Toast.LENGTH_SHORT).show();
        }));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVViewHolder> {
        private String[] listItems;
        private IOnItemClick clickListener;

        public interface IOnItemClick {
            void onClick (TextView view);
        }

        private static class RVViewHolder extends RecyclerView.ViewHolder {

            private TextView tv;
            RVViewHolder(@NonNull View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.textContent);
            }
        }

        RVAdapter (String[] listItems, IOnItemClick onClickListener) {
            this.listItems = listItems;
            this.clickListener = onClickListener;
        }

        @NonNull
        @Override
        public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
            return new RVViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RVViewHolder holder, int position) {
            String display = "[" + position + "] " + listItems[position];
            holder.tv.setText(display);
            holder.tv.setOnClickListener((view) -> {
                if (clickListener != null) {
                    clickListener.onClick((TextView) view);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listItems.length;
        }
    }

}
