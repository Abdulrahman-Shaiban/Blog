package com.abood.blog;

import androidx.fragment.app.Fragment;

public class PostListActivity extends SingleFragmentActivity  {

    @Override
    protected Fragment createFragment() {

        return new PostFragment();

    }

}
