package com.abood.blog;

import androidx.fragment.app.Fragment;

public class PostActivity extends SingleFragmentActivity {

    private static final String EXTRA_CRIME_ID =
            "com.Abood.android.Blog.post_id";

    @Override
    protected Fragment createFragment() {

        return new PostFragment();

    }

}
