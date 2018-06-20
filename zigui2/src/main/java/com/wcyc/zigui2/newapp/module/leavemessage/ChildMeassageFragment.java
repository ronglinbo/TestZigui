package com.wcyc.zigui2.newapp.module.leavemessage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcyc.zigui2.R;
import com.wcyc.zigui2.bean.PictureURL;
import com.wcyc.zigui2.core.BaseActivity;
import com.wcyc.zigui2.core.BaseRecycleAdapter;
import com.wcyc.zigui2.core.BaseRecyleviewFragment;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTask;
import com.wcyc.zigui2.newapp.asynctask.HttpRequestAsyncTaskListener;
import com.wcyc.zigui2.newapp.module.classdynamics.NewClassDynamicsAdapter;
import com.wcyc.zigui2.newapp.videoutils.VideoViewActivity;
import com.wcyc.zigui2.newapp.widget.DeleteItemPop;
import com.wcyc.zigui2.utils.ApiManager;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.DateUtils;
import com.wcyc.zigui2.utils.JsonUtils;
import com.wcyc.zigui2.widget.CustomDialog;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 章豪 on 2017/8/3.
 */

public class ChildMeassageFragment extends BaseRecyleviewFragment<ChildMessage> implements View.OnClickListener, HttpRequestAsyncTaskListener, BaseRecyleviewFragment.OnItemLongClickListener, DeleteItemPop.OnLongClick {

    private List<ChildMessage> mdata = new ArrayList<>();
    private ChildMessageDao childMessageDao = CCApplication.getDaoinstant().getChildMessageDao();

    @Override
    public int getAdapterLayoutId() {
        //适配器 item 布局
        return R.layout.my_child_message_item;
    }

    @Override
    public void onRefreshData() {
        page = 1;
        mdata.clear();
        mRecyclerView.getAdapter().notifyDataSetChanged();
        showProgessBar();
        if (DataUtil.isNetworkAvailable(CCApplication.applicationContext)) {
            isGetCache = false;
            //初始化数据
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("childId", CCApplication.getInstance().getPresentUser().getChildId());
                jsonObject.put("pageSize", 10);
                jsonObject.put("curPage", page);
            } catch (Exception e) {
                e.printStackTrace();
            }
            new HttpRequestAsyncTask(jsonObject, this, CCApplication.applicationContext).execute(Constants.GET_STUDENTBOARD_MESSAGE);
            action = INITDATA;
        } else {
            isGetCache = true;
            mdata.addAll(childMessageDao.queryBuilder().orderDesc(ChildMessageDao.Properties.CreateTime).list());
            setDatas(mdata);
            dismissPd();

        }


        //刷新数据
    }

    @Override
    public void loadmoreData() {

        //初始化数据
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("childId", CCApplication.getInstance().getPresentUser().getChildId());
            jsonObject.put("pageSize", 10);
            if (isGetCache) {
                //从无网环境过来
                List<ChildMessage> list = childMessageDao.queryBuilder().orderAsc(ChildMessageDao.Properties.CreateTime).build().list();
                String date = list.get(0).getCreateTime();
                System.out.println("最前面的一次时间" + date);
                jsonObject.put("minDateString", date);
            } else {
                //从有网环境过来
                if (DataUtil.isNetworkAvailable(CCApplication.applicationContext)) {
                    page++;
                    jsonObject.put("curPage", page);
                }

                ;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        new HttpRequestAsyncTask(jsonObject, this, CCApplication.applicationContext).execute(Constants.GET_STUDENTBOARD_MESSAGE);
        action = LOAD_MORE;

    }

    int page = 1;
    private boolean isGetCache = false;//是否是从缓存初始化

    @Override
    public void initDatas() {

        //防止切换Fragment 导致数据多次初始化
        mdata.clear();
        showProgessBar();

        if (DataUtil.isNetworkAvailable(CCApplication.applicationContext)) {
            isGetCache = false;
            //初始化数据
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("childId", CCApplication.getInstance().getPresentUser().getChildId());
                jsonObject.put("pageSize", 10);
                jsonObject.put("curPage", page);
            } catch (Exception e) {
                e.printStackTrace();
            }
            new HttpRequestAsyncTask(jsonObject, this, CCApplication.applicationContext).execute(Constants.GET_STUDENTBOARD_MESSAGE);
            action = INITDATA;
        } else {
            isGetCache = true;
            mdata.addAll(childMessageDao.queryBuilder().orderDesc(ChildMessageDao.Properties.CreateTime).list());
            setDatas(mdata);
            dismissPd();
        }


    }


    @Override
    public void bindAdapterData(BaseRecycleAdapter.BaseViewHolder holder, int position) {
        final ChildMessage childMessage = mdata.get(position);
        //布局id
        TextView date = (TextView) holder.getView(R.id.date);
        ImageView imageView = (ImageView) holder.getView(R.id.img);
        date.setText(DateUtils.getInstance().getDataString_3(DateUtils.getInstance().getDate(childMessage.getCreateTime())) + "    " + DateUtils.getInstance().getCurrentWeekOfDay(childMessage.getCreateTime()));
        String url = DataUtil.getDownloadURL(getContext(), childMessage.getPcitureAddress() + "&vedio=pic");
        if (!DataUtil.isNullorEmpty(url)) {
            url = url.replaceAll("&vedio=ys", "");
        }
        BaseActivity activity = (BaseActivity) getActivity();
        //Picasso.with(CCApplication.applicationContext).load(url).into(imageView);
         activity.getImageLoader().displayImage(url, imageView, activity.getImageLoaderOptions());
  //      Picasso.with(getActivity()).load(url).error(R.drawable.guide1).into(imageView);


//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //点击效果
//                PictureURL pictureURL = null;
//                List<PictureURL> datas = new ArrayList<PictureURL>();
//                pictureURL = new PictureURL();
//                String url = DataUtil.getDownloadURL(getActivity(), childMessage.getPcitureAddress());
//                pictureURL.setPictureURL(url);
//                datas.add(pictureURL);
//                Intent intent = new Intent(getActivity(),
//                        VideoViewActivity.class);
//                intent.putExtra(NewClassDynamicsAdapter.EXTRA_IMAGE_URLS,
//                        (Serializable) datas);
//                intent.putExtra(NewClassDynamicsAdapter.EXTRA_IMAGE_INDEX,
//                        0);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getActivity().startActivity(intent);
//
//            }
//        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击效果
                PictureURL pictureURL = null;
                List<PictureURL> datas = new ArrayList<PictureURL>();
                pictureURL = new PictureURL();
                current_childMessage = childMessage;
                String url = DataUtil.getRetifitDownloadURL1(getActivity(), childMessage.getPcitureAddress());
                showProgessBar();
                //判断缓存
                File cacheFile = new File(Constants.CACHE_PATH + "video-cache" + "/" + childMessage.getPcitureAddress());
                //存在缓存文件
                if (cacheFile.exists()) {
                    if (cacheFile.length() > 0) {
                        try {          //判断缓存是否全缓存了 没有缓存全部 默认为没有缓存

                            //处理下标越界
                            WhereCondition eq = ChildMessageDao.Properties.Id.eq(childMessage.getId());
                            List<ChildMessage> list = childMessageDao.queryBuilder().where(eq).list();
                            if(!list.isEmpty()){
                                ChildMessage childMessage1 = childMessageDao.queryBuilder().where(ChildMessageDao.Properties.Id.eq(childMessage.getId())).list().get(0);

                                if (childMessage1.getFilelength() == cacheFile.length()) {
                                    //已经缓存全部  从缓存取
                                    currentFile = cacheFile.getPath();
                                    gotoVideoView();
                                    dismissPd();
                                } else {
                                    //没有缓存全部 从网络取
                                    getVideoFromNet(url, childMessage.getPcitureAddress());
                                }
                            }else{
                                getVideoFromNet(url, childMessage.getPcitureAddress());
                            }

                        } catch (Exception e) {
                            getVideoFromNet(url, childMessage.getPcitureAddress());
                        }

                    } else {
                        getVideoFromNet(url, childMessage.getPcitureAddress());
                    }

                } else {
                    getVideoFromNet(url, childMessage.getPcitureAddress());


                }


            }
        });


    }

    private void getVideoFromNet(String url, String fileid) {
        //从网络获取
        if (!DataUtil.isNetworkAvailable(CCApplication.applicationContext)) {
            DataUtil.getToast(getResources().getString(R.string.no_network));// 当前网络不可用，请检查您的网络设置
            dismissPd();
            return;
        }
        //判断SD_card 是不是有500M
        if (DataUtil.isAvaiableSpace(500)) {
            downLoad(url, fileid);
        } else {
            //不足500M 直接从网络获取
            DataUtil.getToastShort("空间不足500M，无法缓存");
            currentFile = DataUtil.getDownloadURL(getActivity(), fileid);
            gotoVideoView();
        }
    }

    private void downLoad(String url, final String fileid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.DLS_URL + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(CCApplication.getInstance().initClient())
                .build();
        ApiManager apiManager = retrofit.create(ApiManager.class);
        Call<ResponseBody> call = apiManager.downloadFileWithDynamicUrlSync(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("111", "server contacted and has file");
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), fileid);
                    dismissPd();
                    if (writtenToDisk) {
                        gotoVideoView();
                    } else {
                        DataUtil.getToastShort("缓存写入失败");
                        currentFile = DataUtil.getDownloadURL(getActivity(), fileid);
                        gotoVideoView();

                    }
                    //进入视频打开界面
                } else {
                    DataUtil.getToast(getResources().getString(R.string.no_network));// 当前网络不可用，请检查您的网络设置
                    dismissPd();
                    Log.d("111", "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DataUtil.getToastShort(t.getMessage());
                Log.e("111", "error");
            }
        });
    }

    private void gotoVideoView() {
        //点击效果
        PictureURL pictureURL = null;
        List<PictureURL> datas = new ArrayList<PictureURL>();
        pictureURL = new PictureURL();

        pictureURL.setPictureURL(currentFile);
        datas.add(pictureURL);
        Intent intent = new Intent(CCApplication.applicationContext,
                VideoViewActivity.class);
        intent.putExtra(NewClassDynamicsAdapter.EXTRA_IMAGE_URLS,
                (Serializable) datas);
        intent.putExtra(NewClassDynamicsAdapter.EXTRA_IMAGE_INDEX,
                0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);

    }

    private String currentFile = "";
    private ChildMessage current_childMessage = null;

    private boolean writeResponseBodyToDisk(ResponseBody body, String fileid) {
        try {
            //视频缓存路径
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Constants.CACHE_PATH + "video-cache" + "/" + fileid);
            currentFile = futureStudioIconFile.getPath();
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                //更新数据库文件大小
                current_childMessage.setFilelength(fileSize);
                childMessageDao.update(current_childMessage);
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("111", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void initEvents() {
        setmOnItemLongClickListener(this);

    }

    com.wcyc.zigui2.newapp.module.leavemessage.infoStudentBoardMessage infoStudentBoardMessage;
    int pages = 0;

    @Override
    public void onRequstComplete(String result) {

        switch (action) {
            case INITDATA:
                //异步网络框架 返回结果 成功状态
                System.out.println("班牌留言:" + result);
                infoStudentBoardMessage = JsonUtils.fromJson(result, com.wcyc.zigui2.newapp.module.leavemessage.infoStudentBoardMessage.class);
                if (infoStudentBoardMessage.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                    pages = infoStudentBoardMessage.getTotalPageNum();
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
                    for (ChildMessage c : infoStudentBoardMessage.getInfoStudentBoardMessageList()) {
                        try {
                            childMessageDao.insert(c);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    mdata.addAll(infoStudentBoardMessage.getInfoStudentBoardMessageList());
                    setDatas(mdata);
                } else {
                    DataUtil.getToast(infoStudentBoardMessage.getServerResult().getResultMessage());
                }

                break;
            case REFERSH:
                //异步网络框架 返回结果 成功状态
                System.out.println("刷新班牌留言:" + result);
                infoStudentBoardMessage = JsonUtils.fromJson(result, com.wcyc.zigui2.newapp.module.leavemessage.infoStudentBoardMessage.class);
                if (infoStudentBoardMessage.getServerResult().getResultCode() == Constants.SUCCESS_CODE) {
                    pages = infoStudentBoardMessage.getTotalPageNum();
                    //数据库缓存
                    for (ChildMessage c : infoStudentBoardMessage.getInfoStudentBoardMessageList()) {
                        try {
                            childMessageDao.insert(c);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mdata.addAll(infoStudentBoardMessage.getInfoStudentBoardMessageList());
                    setDatas(mdata);
                } else {
                    DataUtil.getToast(infoStudentBoardMessage.getServerResult().getResultMessage());
                }

                break;
            case LOAD_MORE:
                //异步网络框架 返回结果 成功状态
                if (page > pages) {
                    if (baseRecycleAdapter.view != null) {
                        baseRecycleAdapter.view.setVisibility(View.GONE);
                    }
                    DataUtil.getToastShort("没有更多数据了");
                } else {
                    System.out.println("加载更多班牌留言:" + page + result);
                    infoStudentBoardMessage = JsonUtils.fromJson(result, com.wcyc.zigui2.newapp.module.leavemessage.infoStudentBoardMessage.class);
                    // 数据库缓存
                    for (ChildMessage c : infoStudentBoardMessage.getInfoStudentBoardMessageList()) {
                        try {
                            childMessageDao.insert(c);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mdata.addAll(infoStudentBoardMessage.getInfoStudentBoardMessageList());
                    setDatas(mdata);
                }


                break;

            case 7:

                break;
        }
        dismissPd();
    }

    @Override
    public void onRequstCancelled() {

    }


    private static JSONArray jsonArray = new JSONArray();
    private CustomDialog dialog;
    /**
     * 控制CustomDialog按钮效果.
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
            editor.putString(Constants.DELETE_STUDENTBOARD_MESSAGE, jsonArray.toString());
            editor.commit();
        } else {
            new HttpRequestAsyncTask(jsonObject, this, CCApplication.applicationContext).execute(Constants.DELETE_STUDENTBOARD_MESSAGE);
            action = 7;
        }
        childMessageDao.delete(mdata.get(position));
        deleteCacheVideo(mdata.get(position).getPcitureAddress());
        mdata.remove(position);
        setDatas(mdata);


    }

    //弹窗删除
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

    //清除缓存视频路径
    private void deleteCacheVideo(String fileid) {
        File cacheFile = new File(Constants.CACHE_PATH + "video-cache" + "/" + fileid);
        if (cacheFile.exists()) {
            if (cacheFile.delete()) {
                System.out.println("删除缓存视频成功");
            }
        }
    }

    int position = -1;

    @Override
    public void onItemLongClick(int position1, View childView) {
        //长按效果
        position = position1;
        DeleteItemPop pop = new DeleteItemPop(getActivity(), this);
        int[] location = {-1, -1};
        childView.getLocationOnScreen(location);
        pop.showAtLocation(childView, Gravity.NO_GRAVITY, location[0] + childView.getWidth() / 2, location[1]);
    }
}
