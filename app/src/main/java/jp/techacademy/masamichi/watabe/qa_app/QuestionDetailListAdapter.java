package jp.techacademy.masamichi.watabe.qa_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.view.animation.Animation;        // [課題]
import android.view.animation.AnimationUtils;   // [課題]

public class QuestionDetailListAdapter extends BaseAdapter {
    private final static int TYPE_QUESTION = 0;
    private final static int TYPE_ANSWER = 1;

    private LayoutInflater mLayoutInflater = null;
    private Question mQuestion;

    private Boolean mIsLogin;   // [課題]ログインしているか判定
    private Boolean mIsClick = false; // [課題]お気に入りボタンがクリックされたかどうか

    public QuestionDetailListAdapter(Context context, Question question, Boolean isLogin) {
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mQuestion = question;
        mIsLogin = isLogin;     // [課題]
    }

    @Override
    public int getCount() {
        return 1 + mQuestion.getAnswers().size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_QUESTION;
        } else {
            return TYPE_ANSWER;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return mQuestion;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (getItemViewType(position) == TYPE_QUESTION) {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.list_question_detail, parent, false);
            }

            final ImageView likeButton = convertView.findViewById(R.id.like_button);        // [課題]お気に入り画像(ハートを予定)を設定

            if (mIsLogin){      // [課題]　ログイン判定によって表示させるかどうか選択
                // [課題]お気に入り画像(ハートを予定)の表示
                likeButton.setVisibility(View.VISIBLE);
            } else {
                // [課題]お気に入り画像(ハートを予定)の非表示
                likeButton.setVisibility(View.INVISIBLE);
            }


            String title = mQuestion.getTitle();    // [課題]
            String body = mQuestion.getBody();
            String name = mQuestion.getName();


            TextView titleTextView = convertView.findViewById(R.id.titleTextView);
            titleTextView.setText(title);

            TextView bodyTextView = convertView.findViewById(R.id.bodyTextView);
            bodyTextView.setText(body);

            TextView nameTextView = convertView.findViewById(R.id.nameTextView);
            nameTextView.setText(name);

            byte[] bytes = mQuestion.getImageBytes();
            if (bytes.length != 0) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length).copy(Bitmap.Config.ARGB_8888, true);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
                imageView.setImageBitmap(image);
            }

            // [課題]お気に入りボタンの処理
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mIsClick){ // クリックされていたら
                        likeButton.setImageResource(R.drawable.heart_black);
                        mIsClick = !mIsClick;
                        // いいねリストから削除
                    }else{
                        likeButton.setImageResource(R.drawable.heart_red);
                        mIsClick = !mIsClick;
                        // アニメーション
                        Animation animation = AnimationUtils.loadAnimation(view.getContext(),R.anim.like_touch);
                        likeButton.startAnimation(animation);
                        // いいねリストの保持
                    }
                }
            });


        } else {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.list_answer, parent, false);
            }

            Answer answer = mQuestion.getAnswers().get(position - 1);
            String body = answer.getBody();
            String name = answer.getName();

            TextView bodyTextView = (TextView) convertView.findViewById(R.id.bodyTextView);
            bodyTextView.setText(body);

            TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            nameTextView.setText(name);
        }

        return convertView;
    }
}