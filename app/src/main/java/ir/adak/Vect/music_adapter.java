package ir.adak.Vect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class music_adapter extends RecyclerView.Adapter<music_adapter.view> {
    DATA data;
    public music_adapter(ArrayList<Music> arrayList) {
        this.arrayList = arrayList;
    }

    public void CLICK(DATA data)
    {
        this.data=data;
    }
    ArrayList<Music> arrayList;
    @NonNull
    @Override
    public view onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.vnksvsv,parent,false);
        return  new view(view);
    }

    public interface  DATA{
        public void Send(Music music,int pos);
    }
    @Override
    public void onBindViewHolder(@NonNull view holder, int position) {
    Music music=arrayList.get(position);
    holder.roundedImage.setImageResource(music.getImg());
    holder.textView12_Name_s.setText(music.getName_S());
    holder.textView13_Name.setText(music.getName());
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            data.Send(music,position);
        }
    });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class view extends RecyclerView.ViewHolder {
        ImageView roundedImage;
        TextView textView12_Name_s;
        TextView textView13_Name;
        public view(@NonNull View itemView) {
            super(itemView);

            roundedImage=itemView.findViewById(R.id.roundedImage);
            textView12_Name_s=itemView.findViewById(R.id.textView12);
            textView13_Name=itemView.findViewById(R.id.textView13);
        }
    }
}
