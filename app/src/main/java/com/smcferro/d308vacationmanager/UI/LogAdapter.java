package com.smcferro.d308vacationmanager.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smcferro.d308vacationmanager.Entities.Log;
import com.smcferro.d308vacationmanager.R;

import java.util.ArrayList;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {

    private List<Log> logs = new ArrayList<>();
    public LogAdapter(List<Log> logList) {
        this.logs = logList;
    }
    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.log_item, parent, false);
        return new LogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        Log currentLog = logs.get(position);
        holder.textViewAction.setText(currentLog.getAction());
        holder.textViewVacationTitle.setText(currentLog.getVacationTitle());
        holder.textViewLogDate.setText(currentLog.getLogDate());
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
        notifyDataSetChanged();
    }

    static class LogViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewAction;
        private final TextView textViewVacationTitle;
        private final TextView textViewLogDate;

        public LogViewHolder(View itemView) {
            super(itemView);
            textViewAction = itemView.findViewById(R.id.textViewAction);
            textViewVacationTitle = itemView.findViewById(R.id.textViewVacationTitle);
            textViewLogDate = itemView.findViewById(R.id.textViewLogDate);
        }
    }
}
