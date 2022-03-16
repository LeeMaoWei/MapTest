package com.example.maptest.item;



import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.maptest.R;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;

import java.util.HashMap;
import java.util.List;

public class Cardstackview extends StackAdapter<Integer> {
    List<HashMap<String,String>> datalist;
    public Cardstackview(Context context) {
        super(context);
    }

    public void updateData(List data,List<HashMap<String,String>> datalist) {
        this.datalist = datalist;
        updateData(data);
        System.out.println("Updata!");
    }

    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.space_layout,parent,false);
        CardViewHolder holder = new CardViewHolder(view);
        System.out.println("onCreateView");
        return holder;
    }

    @Override
    public void bindView(Integer data, int position, CardStackView.ViewHolder holder) {
        if(holder instanceof CardViewHolder)
        {
            CardViewHolder cardHolder = (CardViewHolder)holder;
            cardHolder.onBind(data,position,datalist);
        }
        System.out.println("bindView");
    }



    @Override
    public int getItemViewType(int position) {
        //if (position == 6) {//TODO TEST LARGER ITEM
            return R.layout.space_layout;

    }

    static class ColorItemViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextTitle;

        public ColorItemViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

    }

    static class ColorItemWithNoHeaderViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        TextView mTextTitle;

        public ColorItemWithNoHeaderViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
        }

        @Override
        public void onItemExpand(boolean b) {
        }

        public void onBind(Integer data, int position) {
            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
            mTextTitle.setText(String.valueOf(position));
        }

    }

    static class ColorItemLargeHeaderViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextTitle;

        public ColorItemLargeHeaderViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            mTextTitle = (TextView) view.findViewById(R.id.text_list_card_title);
        }

        @Override
        public void onItemExpand(boolean b) {
            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        @Override
        protected void onAnimationStateChange(int state, boolean willBeSelect) {
            super.onAnimationStateChange(state, willBeSelect);
            if (state == CardStackView.ANIMATION_STATE_START && willBeSelect) {
                onItemExpand(true);
            }
            if (state == CardStackView.ANIMATION_STATE_END && !willBeSelect) {
                onItemExpand(false);
            }
        }

        public void onBind(Integer data, int position) {
            mLayout.getBackground().setColorFilter(ContextCompat.getColor(getContext(), data), PorterDuff.Mode.SRC_IN);
            mTextTitle.setText(String.valueOf(position));

            itemView.findViewById(R.id.text_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CardStackView)itemView.getParent()).performItemClick(ColorItemLargeHeaderViewHolder.this);
                }
            });
        }

    }
    public static class CardViewHolder extends CardStackView.ViewHolder
    {
        View root;
        FrameLayout cardTitle;
        TextView textprice;
        TextView id ;
        TextView price;
        TextView freetime1;
        TextView freetime2 ;
        TextView titleText;

        LinearLayout viewlist;
        public CardViewHolder(View view)
        {
            super(view);
            root = view;
            textprice=view.findViewById(R.id.text_price);
            cardTitle = (FrameLayout)view.findViewById(R.id.frame_list_card_item);
            titleText = (TextView)view.findViewById(R.id.text_list_card_title);
            viewlist= (LinearLayout)view.findViewById(R.id.container_list_content);
             id = view.findViewById(R.id.space_id);
             price = view.findViewById(R.id.space_price);
             freetime1 = view.findViewById(R.id.space_freetime1);
             freetime2 =  view.findViewById(R.id.space_freetime2);
            System.out.println("CardViewHolder constructor");
        }

        @SuppressLint("SetTextI18n")
        public void onBind(Integer backgroundColorId, int position, List<HashMap<String,String>> datalist)
        {
            cardTitle.getBackground().setColorFilter(ContextCompat.getColor(getContext(),backgroundColorId), PorterDuff.Mode.SRC_IN);
            HashMap<String, String> map=datalist.get(position);
            textprice.setText("价格："+map.get("price"));
                titleText.setText("车位："+map.get("spaceid"));
                id.setText(map.get("spaceid"));
                price.setText(map.get("price"));
                String freetime = map.get("freetime");

                assert freetime != null;
                freetime1.setText(freetime.substring(0, 2) + ":" + freetime.substring(2, 4));
                freetime2.setText(freetime.substring(4, 6) + ":" + freetime.substring(6));

            System.out.println("holder onBind");
        }

        @Override
        public void onItemExpand(boolean b) {
            viewlist.setVisibility(b ? View.VISIBLE : View.GONE);
            System.out.println("holder onItemExpand");
        }
    }

}