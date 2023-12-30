package com.example.bluestarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    private ListView questionListView;
    private ListView messageListView;
    private EditText editTextMessage;
    private List<String> questions;
    private ArrayAdapter<String> questionAdapter;
    private List<String> messages;
    private ArrayAdapter<String> messageAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionListView = findViewById(R.id.questionListView);
        messageListView = findViewById(R.id.messageListView);
        editTextMessage = findViewById(R.id.editTextMessage);

        // Danh sách câu hỏi để hiển thị trên màn hình
        questions = new ArrayList<>();
        questions.add("Quyền lợi khi trở thành khách hàng của BlueStar");
        questions.add("Chính sách hoàn trả vé của BlueStar");
        questions.add("Chương trình khuyến mãi thường diễn ra vào thời gian nào?");

        questionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, questions);
        questionListView.setAdapter(questionAdapter);

        messages = new ArrayList<>();
        messageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        messageListView.setAdapter(messageAdapter);

        questionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedQuestion = questions.get(position);
                String reply = getReplyForQuestion(selectedQuestion);

                // Hiển thị câu hỏi và câu trả lời trong danh sách
                messages.add(selectedQuestion);
                messages.add(reply);
                messageAdapter.notifyDataSetChanged();
                messageListView.smoothScrollToPosition(messageAdapter.getCount() - 1);
            }
        });

        editTextMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String newMessage = editTextMessage.getText().toString().trim();
                if (!newMessage.isEmpty()) {
                    // Thêm câu hỏi từ người dùng
                    messages.add(newMessage);

                    // Xử lý câu trả lời dựa trên câu hỏi
                    String reply = getReplyForQuestion(newMessage);
                    messages.add(reply);

                    // Cập nhật giao diện
                    messageAdapter.notifyDataSetChanged();
                    messageListView.smoothScrollToPosition(messageAdapter.getCount() - 1);
                    editTextMessage.getText().clear();
                }
                return true;
            }
            return false;
        });
    }

    private String getReplyForQuestion(String question) {
        // Xử lý câu hỏi và trả lời tương ứng
        switch (question) {
            case "Quyền lợi khi trở thành khách hàng của BlueStar":
                return "Câu trả lời 1.";
            case "Chính sách hoàn trả vé của BlueStar":
                return "Câu trả lời 2.";
            case "Chương trình khuyến mãi thường diễn ra vào thời gian nào?":
                return "Câu trả lời 3.";
            default:
                return "Với câu hỏi này, vui lòng liên hệ hotline của BlueStar để được tư vấn cụ thể hơn. Xin cảm ơn quý khách!";
        }
    }
}
