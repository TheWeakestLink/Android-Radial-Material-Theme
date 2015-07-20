package com.example.mobile1.mypesoesense;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by mobile1 on 7/6/15.
 */
public class RemittanceAdapter extends RecyclerView.Adapter<RemittanceAdapter.RemittanceViewHolder> {

    private LayoutInflater inflater;
    List<RemittanceEntries> data = Collections.emptyList();
    Context context;

    // for the update dialog
    Dialog dialog;
    EditText txtDate;
    EditText txtTitle;
    EditText txtMessage;
    Button btnUpdate;

    public RemittanceAdapter(Context context, List<RemittanceEntries> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RemittanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.remittance_item, parent, false);
        RemittanceViewHolder holder = new RemittanceViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RemittanceViewHolder holder, int position) {
        RemittanceEntries current = data.get(position);
        holder.remDate.setText(current.getDate());
        holder.remType.setText(current.getType());
        holder.remContent.setText(current.getContent());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RemittanceViewHolder extends RecyclerView.ViewHolder {
        TextView remDate;
        TextView remType;
        TextView remContent;
        ImageButton btnEdit;
        ImageButton btnDelete;

        public RemittanceViewHolder(View itemView) {
            super(itemView);
            remDate = (TextView) itemView.findViewById(R.id.rem_date);
            remType = (TextView) itemView.findViewById(R.id.rem_type);
            remContent = (TextView) itemView.findViewById(R.id.rem_content);

            remContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Viewing remittance " + String.valueOf(getPosition()),
                            Toast.LENGTH_LONG).show();
                }
            });

            btnEdit = (ImageButton) itemView.findViewById(R.id.btn_edit);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   updateView(getPosition(), data.get(getPosition()).getId());
                }
            });

            btnDelete = (ImageButton) itemView.findViewById(R.id.btn_delete);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeView(getPosition(), data.get(getPosition()).getId());
                }
            });

        }

        public void removeView(final int position, final int id) {
            //prompt first if sure on delete
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(context);
            dialog = new Dialog(context);
            builder.setTitle("Delete");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //do something if yes
                    // This is the start of the actual delete
                    deleteRemittance(id);
                    data.remove(position);
                    notifyItemRemoved(position);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }

        public void deleteRemittance(int id) {
            DatabaseHelper dbHelper;
            SQLiteDatabase db;

            dbHelper = DatabaseHelper.getInstance(context);
            db = dbHelper.getWritableDatabase();

            db.delete("tbl_remittances", "rem_id = " + id, null);
            db.close();
        }

        public void updateView(final int position, final int id) {

            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_update_remittance); // add the name of the layout here
            dialog.show();

            txtDate = (EditText) dialog.findViewById(R.id.txt_udate);
            txtTitle = (EditText) dialog.findViewById(R.id.txt_utitle);
            txtMessage = (EditText) dialog.findViewById(R.id.txt_umessage);

            txtDate.setText(data.get(position).getDate());
            txtTitle.setText(data.get(position).getType());
            txtMessage.setText(data.get(position).getContent());

            btnUpdate = (Button) dialog.findViewById(R.id.btn_update);
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateRemittance(position, id, txtDate.getText().toString(), txtTitle.getText().toString(), txtMessage.getText().toString());
                    dialog.dismiss();
                }
            });

        }

        public void updateRemittance(int position, int id, String date, String title, String message) {
            // TODO: This is where the data will be delted -- Waiting for the dialog
            DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values;

            db = dbHelper.getWritableDatabase();

            values = new ContentValues();
            values.put("rem_date", date);
            values.put("rem_title", title);
            values.put("rem_message", message);

            db.update("tbl_remittances", values, "rem_id = " + id, null);
            db.close();

            data.get(position).setDate(date);
            data.get(position).setType(title);
            data.get(position).setContent(message);
            notifyItemChanged(position);
        }
    }
}
