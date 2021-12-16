package io.stipop.java_sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;
import java.util.Objects;

import io.stipop.Stipop;
import io.stipop.StipopDelegate;
import io.stipop.custom.StipopImageView;
import io.stipop.models.SPPackage;
import io.stipop.models.SPSticker;

public class MainActivity extends AppCompatActivity implements StipopDelegate, ChatAdapter.GuideDelegate {

    private static String testUserId = "testUser1234";
    private Toolbar toolBar;
    private AppCompatImageView profileImageView;
    private AppCompatTextView nameTextView;
    private AppCompatTextView statusTextView;
    private RecyclerView chatRecyclerView;
    private StipopImageView stickerPickerImageView;
    private StipopImageView stickerSearchImageView;
    private AppCompatEditText chatInputEditText;
    private AppCompatImageView sendImageView;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolBar = findViewById(R.id.toolBar);
        profileImageView = findViewById(R.id.profileImageView);
        nameTextView = findViewById(R.id.nameTextView);
        statusTextView = findViewById(R.id.statusTextView);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        stickerPickerImageView = findViewById(R.id.stickerPickerImageView);
        stickerSearchImageView = findViewById(R.id.stickerSearchImageView);
        chatInputEditText = findViewById(R.id.chatInputEditText);
        sendImageView = findViewById(R.id.sendImageView);

        Stipop.Companion.connect(this, testUserId, this, stickerPickerImageView, Locale.getDefault(), null);

        stickerPickerImageView.setOnClickListener(View -> {
            Stipop.Companion.showKeyboard();
        });

        stickerSearchImageView.setOnClickListener(View -> {
            Stipop.Companion.showSearch();
        });

        initSampleUi();
    }

    @Override
    public boolean onStickerPackRequested(@NonNull SPPackage spPackage) {
        return true;
    }

    @Override
    public boolean onStickerSelected(@NonNull SPSticker spSticker) {
        sendSticker(spSticker);
        return true;
    }

    private void initSampleUi() {
        setSupportActionBar(toolBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        nameTextView.setText("Test User Id : " + testUserId);
        statusTextView.setText("Language : " + Locale.getDefault().getLanguage() + " / Country : " + Locale.getDefault().getCountry());

        profileImageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.img_profile));

        chatInputEditText.setOnClickListener(View -> {
            Stipop.Companion.hideKeyboard();
        });

        chatInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                toggleSendButton(s != null && s.length() > 0);
            }
        });

        sendImageView.setOnClickListener(View -> {
            sendMessage(null);
        });

        chatInputEditText.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage(null);
            return true;
        });

        chatAdapter = new ChatAdapter(this);
        chatRecyclerView.setAdapter(chatAdapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setHasFixedSize(true);
        chatRecyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                chatRecyclerView.postDelayed(() -> chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1), 100);
            }
        });
    }

    private void toggleSendButton(boolean isActivate) {
        if (isActivate) {
            sendImageView.setColorFilter(ContextCompat.getColor(this, R.color.primary));
        } else {
            sendImageView.setColorFilter(ContextCompat.getColor(this, R.color.deactivate));
        }
    }

    private void sendMessage(String message) {
        String chatMessage;
        if (message != null && message.length() > 0) {
            chatMessage = message;
        } else {
            chatMessage = Objects.requireNonNull(chatInputEditText.getText()).toString();
        }
        if (chatMessage != null && chatMessage.length() > 0) {
            ChatItem item = new ChatItem(chatMessage, null);
            chatAdapter.addChatItem(item);
            chatAdapter.notifyItemInserted(chatAdapter.getItemCount() - 1);
            chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            chatInputEditText.setText("");
        }
    }

    private void sendSticker(SPSticker spSticker) {
        ChatItem item = new ChatItem(null, spSticker);
        chatAdapter.addChatItem(item);
        chatAdapter.notifyItemInserted(chatAdapter.getItemCount() - 1);
        chatRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
    }

    @Override
    public void onStickerSearchViewClick() {
        sendMessage("Let me try Sticker Search View! \uD83D\uDD0D");
        Stipop.Companion.showSearch();
    }

    @Override
    public void onStickerPickerViewClick() {
        sendMessage("Let me try Sticker Keyboard Picker View! \uD83D\uDE00");
        Stipop.Companion.showKeyboard();
    }

    @Override
    public void onSentStickerClick(int packageId) {
        Stipop.Companion.showStickerPackage(getSupportFragmentManager(), packageId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
                break;
            case R.id.contactUs:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://developers.stipop.io/contact-us")));
                break;
            case R.id.goToGithub:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/stipop-development/stipop-android-sdk")));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
