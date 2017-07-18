package com.cxh.mvvmart.viewmodel;

import android.app.Activity;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cxh.mvvmart.BR;
import com.cxh.mvvmart.R;
import com.cxh.mvvmart.base.BaseViewModel;
import com.cxh.mvvmart.bindingadapter.ReplyCommand;
import com.cxh.mvvmart.callback.OnItemClickListener;
import com.cxh.mvvmart.manager.RxScheduler;
import com.cxh.mvvmart.model.entity.User;
import com.cxh.mvvmart.ui.activity.DataBindingActivity;
import com.cxh.mvvmart.ui.adapter.XXXRecyclerViewAdapter;
import com.cxh.mvvmart.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.collections.MergeObservableList;
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass;

/**
 * @author Hai (haigod7[at]gmail[dot]com)
 *         2017/3/6
 */
public class DataBindingViewModel implements BaseViewModel {

    private boolean first = true;

    private OnItemClickListener listener = ToastUtils::show;

    // 单一item
    public final ItemBinding<User> itemBinding = ItemBinding.<User>of(BR.item, R.layout.list_item).bindExtra(BR.listener, listener);

    public final ObservableList<User> items0 = new ObservableArrayList<>();
    public final ObservableList<User> items = new ObservableArrayList<>();

    public final XXXRecyclerViewAdapter<Object> adapter = new XXXRecyclerViewAdapter<>();

    /**
     * Items merged with a header on top and footer on bottom.
     */
    public final MergeObservableList<Object> headerFooterItems = new MergeObservableList<>()
            .insertItem("Header")
            .insertList(items0)
            .insertItem("Footer");

    // 添加多种itemstyle，类型必须对应上
    public final OnItemBindClass<Object> multipleItems = new OnItemBindClass<>()
            .map(String.class, BR.item, R.layout.list_item_header)
            .map(User.class, BR.item, R.layout.list_item);

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(true);
    }

    private DataBindingActivity mDataBindingActivity;

    @Inject
    DataBindingViewModel(Activity activity) {
        mDataBindingActivity = (DataBindingActivity) activity;
        loadData();
    }

    @Override
    public void loadData() {
        for (int i = 0; i < 3; i++) {
            items0.add(new User("Kobe" + i, "Bryant"));
        }
        User user = new User("Kobe", "Bryant", 37);
        EventBus.getDefault().post(user);

        requestData();
    }

    private void requestData() {
        viewStyle.isRefreshing.set(true);
        items.clear();
        Observable
                .create((ObservableOnSubscribe<List<User>>) emitter -> {
                    List<User> userList = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        userList.add(new User("Kobe" + i, "Bryant"));
                    }
                    emitter.onNext(userList);
                })
                .delay(2, TimeUnit.SECONDS)
                .compose(RxScheduler.applyObservableSchedulers(mDataBindingActivity))
                .subscribe(list -> {
                    items.addAll(list);
                    viewStyle.isRefreshing.set(false);
                    if (first) {
                        first = false;
                        mDataBindingActivity.showContent();
                    }
                });

    }

    private void loadMoreData() {
        Observable
                .create((ObservableOnSubscribe<List<User>>) emitter -> {
                    List<User> userList = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        userList.add(new User("More" + i, "Bryant"));
                    }
                    emitter.onNext(userList);
                })
                .delay(2, TimeUnit.SECONDS)
                .compose(RxScheduler.applyObservableSchedulers(mDataBindingActivity))
                .subscribe(items::addAll);
    }

    public void addItem() {
        items.add(new User("New Data", "Bryant"));
    }

    public void removeItem() {
        if (items.size() > 1) {
            items.remove(items.size() - 1);
        }
    }

    public final ReplyCommand mReplyCommand = new ReplyCommand(() -> ToastUtils.show("ReplyCommand"));

    public final ReplyCommand onRefreshCommand = new ReplyCommand(DataBindingViewModel.this::loadData);

    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> loadMoreData());

    public final BindingRecyclerViewAdapter.ViewHolderFactory viewHolder = binding -> new MyAwesomeViewHolder(binding.getRoot());

    /**
     * 自定义 ViewHolder
     */
    private static class MyAwesomeViewHolder extends RecyclerView.ViewHolder {
        MyAwesomeViewHolder(View itemView) {
            super(itemView);
        }
    }
}