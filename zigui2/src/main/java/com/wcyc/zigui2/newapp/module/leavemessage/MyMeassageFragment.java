package com.wcyc.zigui2.newapp.module.leavemessage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.core.BaseRecycleAdapter;
import com.wcyc.zigui2.core.BaseRecyleviewFragment;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.greendao.db.LeaveMessageManager;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.widget.DeleteItemPop;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.DateUtils;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 章豪 on 2017/8/3.
 */

public class MyMeassageFragment extends BaseRecyleviewFragment<LeaveMessage> implements View.OnClickListener, HttpRequestAsyncTaskListener, BaseRecyleviewFragment.OnItemLongClickListener, DeleteItemPop.OnLongClick {
    private List<LeaveMessage> mdata = new ArrayList();

    LeaveMessageManager leaveMessageManager;


    @Override
    public int getAdapterLayoutId() {
        //适配器 item 布局

        return R.layout.mymessage_item;
    }

//    private LeaveMessageDao leaveMessageDao = CCApplication.getInstance().getDaoinstant().getLeaveMessageDao();
    ;

    @Override
    public void setmRecyclerViewPadding() {
        mRecyclerView.setPadding(35, 35, 35, 35);
    }

    @Override
    public void initEvents() {
        setmOnItemLongClickListener(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        leaveMessageManager=new LeaveMessageManager(CCApplication.applicationContext);
    }

    public LeaveMessageManager getLeaveMessageManager(){
        if(null==leaveMessageManager){
            leaveMessageManager=new LeaveMessageManager(CCApplication.applicationContext);
        }

        return leaveMessageManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        leaveMessageManager.closeDataBase();
        leaveMessageManager=null;

    }

    @Override
    public void onRefreshData() {
        //刷新数据
        showProgessBar();
        page = 1;
        mdata.clear();
        mRecyclerView.getAdapter().notifyDataSetChanged();
        if (DataUtil.isNetworkAvailable(CCApplication.applicationContext)) {
            isGetCache = false;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("schoolId", CCApplication.getInstance().getPresentUser().getSchoolId());
                jsonObject.put("studentId", CCApplication.getInstance().getPresentUser().getChildId());
                jsonObject.put("createUserId", CCApplication.getInstance().getPresentUser().getUserId());
                jsonObject.put("pageSize", nums);
                jsonObject.put("curPage", page);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("家长留言入参:" + jsonObject.toString());
            new HttpRequestAsyncTask(jsonObject, this, CCApplication.applicationContext).execute(Constants.GET_PARENT_MESSAGE_BOARD_LIST);
            action = REFERSH;
        } else {
            isGetCache = true;
            mdata.addAll(getLeaveMessageManager().getBdDaoSession(CCApplication.applicationContext).getLeaveMessageDao().queryBuilder().orderDesc(LeaveMessageDao.Properties.CreateTime).list());
            setDatas(mdata);
            dismissPd();
        }

    }

    int b = 0;

    @Override
    public void loadmoreData() {
        page++;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("schoolId", CCApplication.getInstance().getPresentUser().getSchoolId());
            jsonObject.put("studentId", CCApplication.getInstance().getPresentUser().getChildId());
            jsonObject.put("createUserId", CCApplication.getInstance().getPresentUser().getUserId());
            jsonObject.put("pageSize", nums);
            jsonObject.put("curPage", page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("家长留言入参:" + jsonObject.toString());
        new HttpRequestAsyncTask(jsonObject, this, CCApplication.applicationContext).execute(Constants.GET_PARENT_MESSAGE_BOARD_LIST);
        action = LOAD_MORE;


    }

    int pages = 0;
    int page = 1;
    //本地数据库 分页配置 每页 10个数据
    int nums = 10;
    boolean ishaveData = false;

    //判断服务器是否还有数据
    private boolean ishaveData(Long counts) {
        float i = (float) counts / nums;
        float b = i - (int) i;
        if (b > 0) {//说明是小数 已经到尾页了 不是整数页
            return false;
        }
        return true;
    }

    private boolean isGetCache = false;

    @Override
    public void initDatas() {
        //初始化数据
//        schoolId	String	学校id
//        createUserId	String	发布人ID（家长用户ID）
//        studentId	String	接收学生ID
//        curPage	int	当前页数，为空时默认为1
//        pageSize	int	每页数据条数，为空时默认为15
        showProgessBar();
        page = 1;
        mdata.clear();
        if (DataUtil.isNetworkAvailable(CCApplication.applicationContext)) {
            isGetCache = false;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("schoolId", CCApplication.getInstance().getPresentUser().getSchoolId());
                jsonObject.put("studentId", CCApplication.getInstance().getPresentUser().getChildId());
                jsonObject.put("createUserId", CCApplication.getInstance().getPresentUser().getUserId());
                jsonObject.put("pageSize", nums);
                jsonObject.put("curPage", page);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("家长留言入参:" + jsonObject.toString());
            new HttpRequestAsyncTask(jsonObject, this, CCApplication.applicationContext).execute(Constants.GET_PARENT_MESSAGE_BOARD_LIST);
            action = INITDATA;
        } else {
            isGetCache = true;
            mdata.addAll(getLeaveMessageManager().getBdDaoSession(CCApplication.applicationContext).getLeaveMessageDao().queryBuilder().orderDesc(LeaveMessageDao.Properties.CreateTime).list());
            setDatas(mdata);
            dismissPd();
        }


    }

    InfoParentMessage infoParentMessage;

    @Override
    public void onRequstComplete(String result) {
        //异步网络框架 返回结果 成功状态

        switch (action) {
            case INITDATA:
                System.out.println("家长留言初始化:" + result);
                infoParentMessage = JsonUtils.fromJson(result, InfoParentMessage.class);
                if (infoParentMessage.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                    pages = infoParentMessage.getPages();
                    if (pages <= 1) {
                        if (baseRecycleAdapter.view != null) {
                            baseRecycleAdapter.view.setVisibility(View.GONE);
                        }
                    } else {
                        if (baseRecycleAdapter.view != null) {
                            baseRecycleAdapter.view.setVisibility(View.VISIBLE);
                        }
                    }

                    // 数据库缓存
                    for (LeaveMessage l : infoParentMessage.getParentMessageBoardList()) {
                        try {
                            getLeaveMessageManager().getBdDaoSession(CCApplication.applicationContext).getLeaveMessageDao().insert(l);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    List<LeaveMessage> list = getLeaveMessageManager().getBdDaoSession(CCApplication.applicationContext).getLeaveMessageDao().queryBuilder().orderDesc(LeaveMessageDao.Properties.CreateTime).offset((page - 1) * nums).limit(10).list();
                    if(!list.isEmpty()){
                        mdata.addAll(getLeaveMessageManager().getBdDaoSession(CCApplication.applicationContext).getLeaveMessageDao().queryBuilder().orderDesc(LeaveMessageDao.Properties.CreateTime).offset((page - 1) * nums).limit(10).list());
                    }else{
                        mdata.addAll(infoParentMessage.getParentMessageBoardList());
                    }
                    setDatas(mdata);

                } else {
                    DataUtil.getToastShort(infoParentMessage.getServerResult().getResultMessage());
                }
                dismissPd();
                break;
            case REFERSH:
                System.out.println("刷新家长留言" + result);
                infoParentMessage = JsonUtils.fromJson(result, InfoParentMessage.class);
                if (infoParentMessage.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                    pages = infoParentMessage.getPages();
                    if (pages <= 1) {
                        if (baseRecycleAdapter.view != null) {
                            baseRecycleAdapter.view.setVisibility(View.GONE);
                        }
                    } else {
                        if (baseRecycleAdapter.view != null) {
                            baseRecycleAdapter.view.setVisibility(View.VISIBLE);
                        }
                    }

                    // 数据库缓存
                    for (LeaveMessage l : infoParentMessage.getParentMessageBoardList()) {
                        try {
                            getLeaveMessageManager().getBdDaoSession(CCApplication.applicationContext).getLeaveMessageDao().insert(l);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    mdata.addAll(infoParentMessage.getParentMessageBoardList());
                    setDatas(mdata);

                } else {
                    DataUtil.getToastShort(infoParentMessage.getServerResult().getResultMessage());
                }
                dismissPd();
                break;
            case LOAD_MORE:
                if (isGetCache) { //从无网环境进来
                    if (baseRecycleAdapter.view != null) {
                        baseRecycleAdapter.view.setVisibility(View.GONE);
                    }
                   DataUtil.getToastShort("没有更多数据了");

                } else {
                    //有网环境进来
                    //异步网络框架 返回结果 成功状态
                    if (page > pages) {
                        if (baseRecycleAdapter.view != null) {
                            baseRecycleAdapter.view.setVisibility(View.GONE);
                        }
                        DataUtil.getToastShort("没有更多数据了");
                    } else {

                        System.out.println("加载更多班牌留言:" + page + result);
                        infoParentMessage = JsonUtils.fromJson(result, InfoParentMessage.class);
                        // 数据库缓存
                        for (LeaveMessage l : infoParentMessage.getParentMessageBoardList()) {
                            try {
                                getLeaveMessageManager().getBdDaoSession(CCApplication.applicationContext).getLeaveMessageDao().insert(l);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        List<LeaveMessage> list = getLeaveMessageManager().getBdDaoSession(CCApplication.applicationContext).getLeaveMessageDao().queryBuilder().orderDesc(LeaveMessageDao.Properties.CreateTime).offset((page - 1) * nums).limit(10).list();
                        if(!list.isEmpty()){
                            mdata.addAll(getLeaveMessageManager().getBdDaoSession(CCApplication.applicationContext).getLeaveMessageDao().queryBuilder().orderDesc(LeaveMessageDao.Properties.CreateTime).offset((page - 1) * nums).limit(10).list());
                        }else{
                            mdata.addAll(infoParentMessage.getParentMessageBoardList());
                        }

                        setDatas(mdata);
                    }
                }


                break;
            case 7:
                //删除
                System.out.println("删除家长留言：" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        DataUtil.getToastShort("删除成功");
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    public void onRequstCancelled() {
        //异步网络框架 返回结果 失败状态
    }

    int position;

    @Override
    public void onItemLongClick(int position1, View childView) {
        //长按事件
        position = position1;
        DeleteItemPop pop = new DeleteItemPop(getActivity(), this);
        int[] location = {-1, -1};
        childView.getLocationOnScreen(location);
        pop.showAtLocation(childView, Gravity.NO_GRAVITY, location[0] + childView.getWidth() / 2, location[1]);
    }

    static JSONArray jsonArray = new JSONArray(); //静态
    private CustomDialog dialog;
    /**
     * 控制CustomDialog按钮事件.
     */
    Handler handler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            super.dispatchMessage(msg);

            switch (msg.what) {
                case CustomDialog.DIALOG_CANCEL:// 取消

                    dialog.dismiss();
                    break;
                case CustomDialog.DIALOG_SURE://确定删除
                    delete();
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private void delete() {
        //  DataUtil.getToastShort("删除:" + position);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", mdata.get(position).getId() + "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        //网络不能用
        if (!DataUtil.isNetworkAvailable(CCApplication.applicationContext)) {
            //将请求加入本地保存 等网络恢复
            SharedPreferences sharedPreferences = CCApplication.applicationContext.getSharedPreferences("delete", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            jsonArray.put(jsonObject);
            editor.putString(Constants.DELETE_PARENT_MESSAGE, jsonArray.toString());
            editor.commit();
        } else {
            new HttpRequestAsyncTask(jsonObject, this, CCApplication.applicationContext).execute(Constants.DELETE_PARENT_MESSAGE);
            action = 7;
        }
        getLeaveMessageManager().getBdDaoSession(CCApplication.applicationContext).getLeaveMessageDao().delete(mdata.get(position));
        mdata.remove(position);
        setDatas(mdata);
    }

    @Override
    public void deleteItem() {
        dialog = new CustomDialog(getActivity(), R.style.mystyle,
                R.layout.dialog_item, handler);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setTitle("删除留言");
        dialog.setContent("确定删除吗");
        Button negative = (Button) dialog.findViewById(R.id.cancel_btn);
        Button update = (Button) dialog.findViewById(R.id.confirm_btn);
        update.getPaint().setFakeBoldText(true);
        negative.setText("取消");
        update.setText("确定");


    }


    @Override
    public void bindAdapterData(BaseRecycleAdapter.BaseViewHolder holder, int position) {
        final LeaveMessage leaveMessage = getDatas().get(position);
        final TextView copy_message = (TextView) holder.getView(R.id.copy_message); //显示全部的TextView
        final TextView show_all = (TextView) holder.getView(R.id.show_all); //显示全部 按钮
        final TextView message = (TextView) holder.getView(R.id.message);//留言内容限制行数的
        TextView date = (TextView) holder.getView(R.id.date);//日期

        //初始化所有视图 数据
        show_all.setText("全部");
        show_all.setVisibility(View.INVISIBLE);
        message.setMaxLines(12);
        message.setVisibility(View.VISIBLE);
        copy_message.setVisibility(View.GONE);
        //执行逻辑
        copy_message.setText(leaveMessage.getContent());
        message.setText(leaveMessage.getContent());
        date.setText(DateUtils.getInstance().getDataString_3(DateUtils.getInstance().getDate(leaveMessage.getCreateTime())) + "    " + DateUtils.getInstance().getCurrentWeekOfDay(leaveMessage.getCreateTime()));
        message.post(new Runnable() {
            @Override
            public void run() {
                int count = message.getLineCount();
                if (count > 4) {
                    message.setMaxLines(4);
                    show_all.setTag(false);
                    show_all.setVisibility(View.VISIBLE);
                    show_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!(Boolean) show_all.getTag()) {
                                copy_message.setVisibility(View.VISIBLE);
                                message.setVisibility(View.GONE);
                                show_all.setText("收起");
                                show_all.setTag(true);

                            } else {
                                copy_message.setVisibility(View.GONE);
                                message.setVisibility(View.VISIBLE);
                                show_all.setText("全部");
                                show_all.setTag(false);
                            }

                        }
                    });
                }
            }
        });


    }

}
