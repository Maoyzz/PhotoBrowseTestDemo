package com.myz.photobrowsetestdemo;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRvPhoto;
    FeedbackAdapter adapter;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRvPhoto = findViewById(R.id.rv_photo);
        list = new ArrayList<>();
        list.add("http://img1.imgtn.bdimg.com/it/u=4206294871,879077254&fm=26&gp=0.jpg");
        list.add("http://img1.imgtn.bdimg.com/it/u=1901690610,3955011377&fm=200&gp=0.jpg");
        list.add("http://img3.imgtn.bdimg.com/it/u=1546158593,2358526642&fm=200&gp=0.jpg");
        list.add("http://img0.imgtn.bdimg.com/it/u=3792909229,2321547963&fm=200&gp=0.jpg");
        list.add("http://img4.imgtn.bdimg.com/it/u=1621655683,865218969&fm=200&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=4286838121,1364454560&fm=26&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=551944592,1654216059&fm=26&gp=0.jpg");
        list.add("http://img1.imgtn.bdimg.com/it/u=2550323596,2167297465&fm=200&gp=0.jpg");
        list.add("http://img4.imgtn.bdimg.com/it/u=952962361,1269259737&fm=26&gp=0.jpg");

        adapter = new FeedbackAdapter(list);
        mRvPhoto.setLayoutManager(new GridLayoutManager(this, 3));
        mRvPhoto.setAdapter(adapter);
        adapter.bindToRecyclerView(mRvPhoto);


        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImageView iv = view.findViewById(R.id.image);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    share(iv, position);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void share(View view, int position) {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putStringArrayListExtra(ImageActivity.IMG_KEY, list);
        intent.putExtra(ImageActivity.IMG_POSITION, position);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this, view, "img").toBundle();
        startActivity(intent, bundle);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            int exitPos = data.getIntExtra(ImageActivity.IMG_CURRENT_POSITION, -1);
            final View exitView = getExitView(exitPos);
            if (exitView != null) {
                ActivityCompat.setExitSharedElementCallback(this, new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                        names.clear();
                        sharedElements.clear();
                        names.add(ViewCompat.getTransitionName(exitView));
                        sharedElements.put(Objects.requireNonNull(ViewCompat.getTransitionName(exitView)), exitView);
                        setExitSharedElementCallback(new SharedElementCallback() {
                        });
                    }
                });
            }
        }
    }

    private View getExitView(int position) {
        if (position == -1) {
            return null;
        }
        if (adapter != null) {
            return adapter.getViewByPosition(position, R.id.image);
        }
        return null;
    }


    class FeedbackAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public FeedbackAdapter(List<String> data) {
            super(R.layout.item_img_small, data);
        }
        @Override
        protected void convert(BaseViewHolder helper, String item) {
            int position = helper.getLayoutPosition();

            ImageView Feedbackphoto = helper.getView(R.id.image);
            Glide.with(mContext).load(item)
                    .into(Feedbackphoto);
        }
    }
}