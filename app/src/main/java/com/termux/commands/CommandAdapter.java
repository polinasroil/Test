package com.termux.commands;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CommandAdapter extends ArrayAdapter<Command> {

    private List<Command> originalData;
    private List<Command> filteredData;

    public CommandAdapter(Context context, List<Command> commands) {
        super(context, 0, commands);
        this.originalData = new ArrayList<>(commands);
        this.filteredData = new ArrayList<>(commands);
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Command getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Command command = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Lookup view for data population
        TextView tvCommand = (TextView) convertView.findViewById(R.id.command_name);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.command_description);

        // Populate the data into the template view using the data object
        tvCommand.setText(command.getCommand());
        tvDescription.setText(command.getDescription());

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Command> suggestions = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    suggestions.addAll(originalData);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Command item : originalData) {
                        if (item.getCommand().toLowerCase().contains(filterPattern) || item.getDescription().toLowerCase().contains(filterPattern)) {
                            suggestions.add(item);
                        }
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData.clear();
                filteredData.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
