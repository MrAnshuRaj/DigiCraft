package com.anshu.digicraft;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationModel> notificationList;

    public NotificationAdapter(List<NotificationModel> notificationList) {
        this.notificationList = notificationList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, body, sender, timestamp, dataBlock;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleText);
            body = itemView.findViewById(R.id.bodyText);
            sender = itemView.findViewById(R.id.senderText);
            timestamp = itemView.findViewById(R.id.timestampText);
            dataBlock = itemView.findViewById(R.id.dataBlockText);
        }
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        NotificationModel item = notificationList.get(position);
        StringBuilder dataBuilder = new StringBuilder();
        holder.title.setText(item.getTitle());
        holder.body.setText(item.getBody());
        holder.sender.setText( item.getSender());
        holder.timestamp.setText(Utils.formatTimestamp(item.getCreatedAt()));
//        holder.dataBlock.setText(item.getData().get("data"));
        for (Map.Entry<String, String> entry : item.getData().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // Do something with the key-value pair
            dataBuilder.append(key).append(": ").append(value).append("\n");
        }
        holder.dataBlock.setText(dataBuilder.toString());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
