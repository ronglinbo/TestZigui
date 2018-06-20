
package com.wcyc.zigui2.newapp.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.wcyc.zigui2.R;

import com.wcyc.zigui2.chat.ChatActivity;
import com.wcyc.zigui2.core.CCApplication;
import com.wcyc.zigui2.newapp.bean.MemberDetailBean;
import com.wcyc.zigui2.newapp.bean.MenuItem;
import com.wcyc.zigui2.newapp.bean.NewMessageBean;
import com.wcyc.zigui2.newapp.bean.NewMessageBean.HXUser;
import com.wcyc.zigui2.newapp.bean.NewMessageListBean;

import com.wcyc.zigui2.newapp.module.mailbox.SchoolMasterMailActivity;
import com.wcyc.zigui2.newapp.module.notice.NotifyActivity;
import com.wcyc.zigui2.utils.Constants;
import com.wcyc.zigui2.utils.DataUtil;
import com.wcyc.zigui2.utils.SmileUtils;
import com.wcyc.zigui2.widget.CustomDialog;

/**
 * 消息记录adpater
 */
public class AllMessageAdapter extends ArrayAdapter<NewMessageBean> {

    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private NewMessageListBean messageList;
    private Context mContext;
    CustomDialog dialog;

    private HashMap activities = new HashMap() {
        {
            put("通知", NotifyActivity.class);
            put("校长信箱", SchoolMasterMailActivity.class);
        }
    };

    private HashMap res = new HashMap() {
        /**
         *
         */
        private static final long serialVersionUID = 4003115249358179081L;

        //服务编号:
        // 01系统消息；02通知；03资源状态改变消息；04版本更新；05续费提醒；06成绩；07点评；08作业；09校园风采；10班级动态；
        // 11考勤；12回复意见；13消费信息；14邮件；15待办事项；16工资条；17值班查询；18校长信箱；19日志；20总结；
        // 21考试 ；22请假审批结果；23学生请假单（家长端）；24维修处理结果；25文印审批结果 ；26 一卡通考勤；27 校园新闻 ;
        // 28 教育资讯 ; 29 留言板 ；30 进出校考勤 ；31 宿舍考勤 ； 32 校车考勤  33订单催缴 34缴费
        {
            put("06"/*"成绩"*/, R.drawable.home_icon_chengji);
            put("15"/*"待办事项"*/, R.drawable.home_icon_daibanshixiang);
            put("07"/*"点评"*/, R.drawable.home_icon_dianping);
            put("短信", R.drawable.home_icon_duanxin);
            put("16"/*"工资条"*/, R.drawable.home_icon_gongzitiao);
            put("11"/*"人工考勤"*/, R.drawable.home_icon_rengong);
            put("21"/*"考试"*/, R.drawable.home_icon_kaoshi);
            put("成绩表", R.drawable.home_icon_kechengbiao);
            put("01"/*"系统消息"*/, R.drawable.icon_xitongxiaoxi);
            put("19"/*"日志"*/, R.drawable.home_icon_rizhi);
            put("统计", R.drawable.home_icon_tongji);
            put("02"/*"通知"*/, R.drawable.home_icon_tongzhi);
            put("13"/*MenuItem.CONSUME*/, R.drawable.home_icon_xiaofei);
            put("校历", R.drawable.home_icon_xiaoli);
            put("校园监控", R.drawable.home_icon_xiaoyuanjiank);
            put("18"/*"校长信箱"*/, R.drawable.home_icon_xiaozhangxinxiang);
            put("业务办理", R.drawable.home_icon_yewubanli);
            put("14"/*"邮件"*/, R.drawable.home_icon_youjian);
            put("17"/*"值班查询"*/, R.drawable.home_icon_zhibanchaxun);
            put("20"/*"总结"*/, R.drawable.home_icon_zongjie);
            put("作息时间", R.drawable.home_icon_zuoxishijian);
            put("08"/*"作业"*/, R.drawable.home_icon_zuoye);
            put("23"/*"请假条"*/, R.drawable.home_icon_qingjiatiao);
            put("10"/*"班级动态"*/, R.drawable.home_icon_banjidongtai);
            put("27"/*"校园新闻"*/, R.drawable.home_icon_xiaoyuanxinwen);
            put("28"/*"教育资讯"*/, R.drawable.home_icon_jiaoyuzixun);
            //新增留言板
            put("29"/*"班牌留言"*/, R.drawable.home_icon_liuyanban);
            //新增进出校考勤
            put("30"/*"进出校考勤"*/, R.drawable.home_icon_jinchuxiao);
            //新增宿舍考勤
            put("31"/*"宿舍考勤"*/, R.drawable.home_icon_sushe);
            //新增校车考勤
            put("32"/*"校车考勤"*/, R.drawable.home_icon_xiaoche);

            //订单催缴
            put("33"/*"订单催缴"*/, R.drawable.home_icon_paynotice);
            //百川消息
            put("会话列表", R.drawable.home_icon_baichuan);
            //缴费
            put("34"/*"缴费"*/, R.drawable.home_icon_jiaofei);
        }
    };

    List<NewMessageBean> messageBean;

    public AllMessageAdapter(Context context, int textViewResourceId,
                             List<NewMessageBean> objects) {
        super(context, textViewResourceId, objects);
        this.messageBean = objects;
        inflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.pho_touxiang)
                .showImageOnFail(R.drawable.pho_touxiang).resetViewBeforeLoading(true).cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.pho_touxiang)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        imageLoader = ImageLoader.getInstance();
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
//		if (convertView == null) {
        convertView = inflater.inflate(R.layout.row_all_message, parent, false);

        holder = new ViewHolder();
        holder.name = (TextView) convertView.findViewById(R.id.name);
        holder.unreadLabel = (TextView) convertView
                .findViewById(R.id.unread_msg_number);
        holder.message = (TextView) convertView.findViewById(R.id.message);
        holder.time = (TextView) convertView.findViewById(R.id.time);
        holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
        holder.msgState = convertView.findViewById(R.id.msg_state);
        holder.list_item_layout = (RelativeLayout) convertView
                .findViewById(R.id.list_item_layout);
        holder.unRead = (ImageView) convertView.findViewById(R.id.unread_msg);
        convertView.setTag(holder);
//		}else{
//			holder = (ViewHolder) convertView.getTag();

//		}

        final NewMessageBean message = getItem(position);

        if ("chat".equals(message.getMessageType())) {
            MemberDetailBean member = CCApplication.getInstance().getMemberDetail();
            if (member != null) {
                String username = member.getHxUsername();
                // 获取与此用户/群组的会话
                try {
                    if (username != null) {
                        if (EMChatManager.getInstance() != null) {
                            EMConversation conversation = EMChatManager.getInstance()
                                    .getConversation(username);
                            String count = message.getCount();
                            int num = Integer.parseInt(count);

                            HXUser user = message.getHxUser();
                            if (user != null) {
                                holder.name.setText(user.getNickName());
                            }
                            if (num > 0) {
                                if (num > 99) {
                                    holder.unreadLabel.setText("99+");
                                    holder.unreadLabel.setVisibility(View.VISIBLE);
                                } else {
                                    holder.unreadLabel.setText(count);
                                    holder.unreadLabel.setVisibility(View.VISIBLE);
                                }

                            } else {
                                holder.unreadLabel.setVisibility(View.INVISIBLE);
                            }


                            holder.message
                                    .setText(
                                            SmileUtils.getSmiledText(
                                                    getContext(), message.getMessageContent()),
                                            BufferType.SPANNABLE);

                            if (conversation.getMsgCount() != 0) {
                                // 把最后一条消息的内容作为item的message内容
                                EMMessage lastMessage = conversation.getLastMessage();
                                holder.message
                                        .setText(
                                                SmileUtils.getSmiledText(
                                                        getContext(),
                                                        getMessageDigest(lastMessage,
                                                                (this.getContext()))),
                                                BufferType.SPANNABLE);
                                holder.time.setText(DateUtils.getTimestampString(new Date(
                                        lastMessage.getMsgTime())));
                                if (lastMessage.direct == EMMessage.Direct.SEND
                                        && lastMessage.status == EMMessage.Status.FAIL) {
                                    holder.msgState.setVisibility(View.VISIBLE);
                                } else {
                                    holder.msgState.setVisibility(View.GONE);
                                }

                            }
//						holder.time.setText(message.getMessageTime());
                        }
                    }
                } catch (Exception e) {
                    DataUtil.getToastShort("环信出错");
                }

            }
        } else if (message.getMessageType().equals("aLiChat")) {
            //获取到相应的未读数据.
            MemberDetailBean member = CCApplication.getInstance().getMemberDetail();

            if (member != null) {
                String username = member.getHxUsername();
                // 获取与此用户/群组的会话

                try {
                    if (username != null) {
                        if (CCApplication.getInstance().getIMKit() != null) {
                            String count = message.getCount();
                            int num = Integer.parseInt(count);
                            if (num > 0) {
                                if (num > 99) {
                                    holder.unreadLabel.setText("99+");
                                    holder.unreadLabel.setVisibility(View.VISIBLE);
                                } else {
                                    holder.unreadLabel.setText(count);
                                    holder.unreadLabel.setVisibility(View.VISIBLE);
                                }

                            } else {
                                holder.unreadLabel.setVisibility(View.INVISIBLE);
                            }

                            holder.name.setText("会话列表");

                            String messageContent = message.getMessageContent();
                            if (messageContent != null) {
                                holder.message.setText(SmileUtils.getSmiledText(getContext(), messageContent), BufferType.SPANNABLE);
                            } else {
                                holder.message.setText(SmileUtils.getSmiledText(getContext(), ""), BufferType.SPANNABLE);
                            }
                        }
                    }

                } catch (Exception e) {
                    DataUtil.getToastShort("阿里聊天出错,请联系客服.");
                }
            }


        } else {
            String name = message.getMessageTypeName();
            String type = message.getMessageType();
            if (DataUtil.isNullorEmpty(name)) {
                name = DataUtil.convertTypeToName(type);
            }
            holder.name.setText(name);
            holder.message.setText(message.getMessageContent());
            int count = DataUtil.getUnreadStatus(type);
            if ("业务办理".equals(name)) {
                count = DataUtil.getUnreadStatus(Constants.LEAVE);
                count += DataUtil.getUnreadStatus(Constants.GUARRANTEE);
                count += DataUtil.getUnreadStatus(Constants.PRINT);
            }

            message.setCount(String.valueOf(count));
            if (count > 0) {
                if (Constants.SCHOOL_NEWS.equals(type)
                        || Constants.CLASSDYN.equals(type)
                        || Constants.EDU_INFO.equals(type)) {
                    holder.unRead.setVisibility(View.VISIBLE);
                    holder.unreadLabel.setVisibility(View.INVISIBLE);
                } else {
                    if (count > 99) {
                        holder.unreadLabel.setText("99+");

                    } else {
                        holder.unreadLabel.setText(count + "");

                    }

                }
            } else {
                holder.unreadLabel.setVisibility(View.INVISIBLE);
            }

        }


        if (holder.time != null)
            holder.time.setText(formatDate(message.getMessageTime()));
        new Thread(new Runnable() {
            public void run() {
                setImage(message, holder);
            }
        }).run();

        return convertView;
    }

    private void setImage(NewMessageBean message, final ViewHolder holder) {
        if ("chat".equals(message.getMessageType())) {
            HXUser user = message.getHxUser();
            if (user != null) {
                if (user.getChatType() == ChatActivity.CHATTYPE_SINGLE) {
                    if (user.getIconUrl() != null) {
                        final String iconUrl = DataUtil.getIconURL(user.getIconUrl());
                        imageLoader.displayImage(iconUrl, holder.avatar, options);
                    }
                } else {
                    holder.avatar.setImageResource(R.drawable.iconfont_qun);
                }
            }
        } else {
            setAvatar(message, holder);
        }
    }

    private String formatDate(String date) {
        String current = DataUtil.getCurrentDate(System.currentTimeMillis());
        Date now = new Date();
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
        String today = formater.format(now);
        if (date != null) {
            String[] dateTime = date.split(" ");
            String[] YMD = dateTime[0].split("-");
            if (current.contains(dateTime[0])) {//当天的返回时间
                String serverTime = dateTime[1];
                try {
                    Date parse = formater.parse(serverTime);
                    String format = formater.format(parse);
                    System.out.println(format);
                    return format;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return today;
            } else {
                return YMD[1] + "-" + YMD[2];//不是当天的返回日期
            }
        }
        return "";
    }

    private void setAvatar(NewMessageBean message, ViewHolder holder) {
//        String type = message.getMessageTypeName();
        String type = message.getMessageType();
        if (DataUtil.isNullorEmpty(type)) {
            type = DataUtil.convertTypeToName(message.getMessageType());
        }
        if (res != null && type != null) {
            try {
                int resId = 0;
                if(!res.containsKey(type)){
                    resId=(Integer) res.get(message.getMessageTypeName());
                }else{
                    resId=(Integer) res.get(type);
                }

                if (resId != 0)
                    holder.avatar.setImageResource(resId);
            } catch (Exception e) {
                System.out.println("type:" + type);
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据消息内容和消息类型获取消息内容提示
     *
     * @param message
     * @param context
     * @return
     */
    private String getMessageDigest(EMMessage message, Context context) {
        String digest = "";
        switch (message.getType()) {
            case LOCATION: // 位置消息
//			 if (message.direct == EMMessage.Direct.RECEIVE) {
//			 //从sdk中提到了ui中，使用更简单不犯错的获取string方法
//			  digest = EasyUtils.getAppResourceString(context,
//			 "location_recv");
//			 digest = getString(context, R.string.location_recv);
//			 digest = String.format(digest, message.getFrom());
//			 return digest;
//			 } else {
//			  digest = EasyUtils.getAppResourceString(context,
//			 "location_prefix");
//			 digest = getString(context, R.string.location_prefix);
//			 }
                break;
            case IMAGE: // 图片消息
                ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
//			digest = getString(context, R.string.picture)
//					+ imageBody.getFileName();
                //在消息列表界面中，图片消息不显示图片路径
                digest = getString(context, R.string.picture);
                break;
            case VOICE:// 语音消息
                digest = getString(context, R.string.voice);
                break;
            case VIDEO: // 视频消息
                digest = getString(context, R.string.video);
                break;
            case TXT: // 文本消息
                TextMessageBody txtBody = (TextMessageBody) message.getBody();
                digest = txtBody.getMessage();
                break;
            case FILE: // 普通文件消息
                digest = getString(context, R.string.file);
                break;
            default:
                System.err.println("error, unknow type");
                return "";
        }

        return digest;
    }

    private static class ViewHolder {
        /**
         * 标题
         */
        TextView name;
        /**
         * 消息未读数
         */
        TextView unreadLabel;
        /**
         * 最后一条消息的内容
         */
        TextView message;
        /**
         * 最后一条消息的时间
         */
        TextView time;
        /**
         * 用户头像
         */
        ImageView avatar;
        /**
         * 最后一条消息的发送状态
         */
        View msgState;
        /**
         * 整个list中每一行总布局
         */
        RelativeLayout list_item_layout;
        /**
         * 消息未读图标
         */
        ImageView unRead;
    }

    String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    public void refresh(List<NewMessageBean> messageBean) {
        this.messageBean = messageBean;
    }
}
