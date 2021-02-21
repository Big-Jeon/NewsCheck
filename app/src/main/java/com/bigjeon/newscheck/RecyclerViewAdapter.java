package com.bigjeon.newscheck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<Category_Item> mcategory_items;
    private Context mcontext;
    private DBHelper mdbHelper;

    public RecyclerViewAdapter(ArrayList<Category_Item> mcategory_items, Context mcontext){
        this.mcategory_items = mcategory_items;
        this.mcontext = mcontext;
        mdbHelper = new DBHelper(mcontext);

    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list, parent, false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        holder.tv_title.setText(mcategory_items.get(position).getTitle());
        holder.tv_writeDate.setText(mcategory_items.get(position).getWriteDate());
    }

    @Override
    public int getItemCount() {
        return mcategory_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private TextView tv_writeDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.Category_title);
            tv_writeDate = itemView.findViewById(R.id.writeDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                //아이템 클릭시 해당 키워드값 넘겨주고 연결된 리스트 레이아웃으로 이동
                public void onClick(View v) {
                    int curPos = getAdapterPosition();
                    Category_Item category_item = mcategory_items.get(curPos);
                    //새로운 레이아웃 만들어 인텐트로 이동
                    Intent intent = new Intent(mcontext, Checked_list.class);
                    intent.putExtra("keyword", category_item.getTitle());
                    mcontext.startActivity(intent);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int curPos = getAdapterPosition();
                    Category_Item category_item = mcategory_items.get(curPos);

                    String[] strChoiceItems = {"수정","삭제"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                    builder.setTitle("원하는 작업을 선택 하세요.");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {

                            if (position == 0){
                                Dialog dialog = new Dialog(mcontext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.setContentView(R.layout.dialog_add_category);
                                EditText et_title = dialog.findViewById(R.id.category);
                                Button btn_ok = dialog.findViewById(R.id.add_fin_btn);

                                et_title.setText(category_item.getTitle());

                                et_title.setSelection(et_title.getText().length());

                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String title = et_title.getText().toString();
                                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                                        String beforeTime = category_item.getWriteDate();

                                        mdbHelper.UpdateCategory(title, currentTime, beforeTime);

                                        category_item.setTitle(title);
                                        category_item.setWriteDate(currentTime);
                                        notifyItemChanged(curPos, category_item);
                                        dialog.dismiss();
                                        Toast.makeText(mcontext, "수정 완료", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.show();
                            }else if (position == 1){
                                String beforeTime = category_item.getWriteDate();
                                mdbHelper.DeleteCategory(beforeTime);

                                mcategory_items.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mcontext,"제거되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    builder.show();
                    return false;
                }
            });
        }
    }
    public void addcategory(Category_Item _item){
        mcategory_items.add(0, _item);
        notifyItemInserted(0);
    }
}

