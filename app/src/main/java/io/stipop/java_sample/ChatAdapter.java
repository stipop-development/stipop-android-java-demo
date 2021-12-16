package io.stipop.java_sample;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.stipop.custom.StipopImageView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<ChatItem> chatItems = new ArrayList<>();
    private static final int TYPE_INTRO = 1000;
    private static final int TYPE_MESSAGE_MINE = 1001;
    private static final int TYPE_STICKER_MINE = 1002;
    private static final String SAMPLE_STICKER = "https://img.stipop.io/2020/3/31/1585719674256_CookieArrow_size.gif";
    private final GuideDelegate guideDelegate;

    public interface GuideDelegate {
        void onStickerSearchViewClick();

        void onStickerPickerViewClick();

        void onSentStickerClick(int packageId);
    }

    public ChatAdapter(GuideDelegate guideDelegate) {
        this.guideDelegate = guideDelegate;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_INTRO:
                return new GuideItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_guide, parent, false));
            case TYPE_STICKER_MINE:
                return new StickerMessageItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_sticker, parent, false), guideDelegate);
            case TYPE_MESSAGE_MINE:
                return new SimpleMessageItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_MESSAGE_MINE:
                ((SimpleMessageItemHolder) holder).bind(chatItems.get(position - 1));
                break;
            case TYPE_STICKER_MINE:
                ((StickerMessageItemHolder) holder).bind(chatItems.get(position - 1));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chatItems.size() + 1;
    }

    public void addChatItem(ChatItem item) {
        chatItems.add(item);
    }

    @Override
    public long getItemId(int position) {
        if (position == 0) {
            return 0;
        } else {
            return chatItems.get(position - 1).hashCode();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_INTRO;
        } else if ((chatItems.get(position - 1).spSticker) == null) {
            return TYPE_MESSAGE_MINE;
        } else {
            return TYPE_STICKER_MINE;
        }
    }

    class SimpleMessageItemHolder extends RecyclerView.ViewHolder {

        private final AppCompatTextView chatTextView;

        public SimpleMessageItemHolder(View itemView) {
            super(itemView);
            chatTextView = itemView.findViewById(R.id.chat_Text);
        }

        public void bind(ChatItem chatItem) {
            chatTextView.setText(chatItem.message);
        }
    }

    static class StickerMessageItemHolder extends RecyclerView.ViewHolder {
        private final StipopImageView chatStickerImageView;
        private ChatItem chatItem;

        public StickerMessageItemHolder(View itemView, GuideDelegate guideDelegate) {
            super(itemView);
            chatStickerImageView = itemView.findViewById(R.id.my_sticker);
            chatStickerImageView.setOnClickListener(View -> {
                guideDelegate.onSentStickerClick(chatItem.spSticker.getPackageId());
            });
        }

        public void bind(ChatItem chatItem) {
            this.chatItem = chatItem;
            String url;
            if (chatItem.spSticker.getStickerImgLocalFilePath() == null || chatItem.spSticker.getStickerImgLocalFilePath().length() > 0) {
                url = chatItem.spSticker.getStickerImg();
            } else {
                url = chatItem.spSticker.getStickerImgLocalFilePath();
            }
            chatStickerImageView.loadImage(url, false);
        }
    }

    @SuppressLint("SimpleDateFormat")
    class GuideItemHolder extends RecyclerView.ViewHolder {

        private final long ANIMATION_DURATION = 500L;
        private final Animation focusAnimation;
        private final StipopImageView sampleStickerImageView;
        private final LinearLayout viewGroup1;
        private final LinearLayout viewGroup2;
        private final LinearLayout viewGroup3;
        private final AppCompatTextView guideTextView2;
        private final AppCompatTextView guideTextView4;
        private final AppCompatTextView searchViewTextView;
        private final AppCompatTextView pickerViewTextView;

        private final TransitionSet transitionSet = new TransitionSet().addTransition(new Fade()).addTransition(new Slide(Gravity.LEFT)).setDuration(ANIMATION_DURATION).addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                super.onTransitionEnd(transition);
                TransitionManager.beginDelayedTransition(viewGroup2, transitionSet2);
                guideTextView4.setVisibility(View.VISIBLE);
            }
        });

        private final TransitionSet transitionSet2 = new TransitionSet().addTransition(new Fade()).addTransition(new Slide(Gravity.LEFT)).setDuration(ANIMATION_DURATION).addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                super.onTransitionEnd(transition);
                TransitionManager.beginDelayedTransition(viewGroup3, transitionSet3);
                searchViewTextView.setVisibility(View.VISIBLE);
                pickerViewTextView.setVisibility(View.VISIBLE);
            }
        });

        private final TransitionSet transitionSet3 = new TransitionSet().addTransition(new Fade()).addTransition(new Slide(Gravity.LEFT)).setDuration(ANIMATION_DURATION).addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                super.onTransitionEnd(transition);
                searchViewTextView.startAnimation(focusAnimation);
                pickerViewTextView.startAnimation(focusAnimation);
            }
        });

        public GuideItemHolder(View itemView) {
            super(itemView);
            focusAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.shake);
            AppCompatTextView dateTimeTextView = itemView.findViewById(R.id.datetimeTextView);
            sampleStickerImageView = itemView.findViewById(R.id.guideImageView);
            viewGroup1 = itemView.findViewById(R.id.animationViewGroup1);
            viewGroup2 = itemView.findViewById(R.id.animationViewGroup2);
            viewGroup3 = itemView.findViewById(R.id.animationViewGroup3);
            guideTextView2 = itemView.findViewById(R.id.guideTextView2);
            guideTextView4 = itemView.findViewById(R.id.guideTextView4);
            searchViewTextView = itemView.findViewById(R.id.guideTextView5);
            pickerViewTextView = itemView.findViewById(R.id.guideTextView6);

            dateTimeTextView.setText(new SimpleDateFormat("a h:mm").format(new Date()));
            sampleStickerImageView.loadImage(SAMPLE_STICKER, false);

            searchViewTextView.setOnClickListener(View -> {
                guideDelegate.onStickerSearchViewClick();
            });
            pickerViewTextView.setOnClickListener(View -> {
                guideDelegate.onStickerPickerViewClick();
            });

            viewGroup1.post(() -> {
                TransitionManager.beginDelayedTransition(viewGroup1, transitionSet);
                guideTextView2.setVisibility(View.VISIBLE);
                sampleStickerImageView.setVisibility(View.VISIBLE);
            });
        }

    }
}
