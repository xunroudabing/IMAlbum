package com.opensource.multi_image_selector.adapter.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.imalbumlib.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.opensource.configure.Configure;
import com.opensource.configure.imgload.GlobalImageLoader;
import com.opensource.configure.imgload.ResizeBitmapDisplayer;
import com.opensource.multi_image_selector.adapter.bean.Image;

/**
 * 图片Adapter
 * Created by Nereo on 2015/4/7.
 */
public class ImageGridAdapter extends BaseAdapter {

	public static final int CLICK_INDEX_CHOICE = 100;//点击勾选
	public static final int CLICK_INDEX_IMAGE = 101;//点击图片
	
	
    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;

    private Context mContext;
    private Handler mHandle;//item点击事件与activity消息才传递
    private LayoutInflater mInflater;
    private boolean showCamera = true;
    private boolean showSelectIndicator = true;

    private List<Image> mImages = new ArrayList();
    private List<Image> mSelectedImages = new ArrayList();

    private int mItemSize;
    private GridView.LayoutParams mItemLayoutParams;

    public ImageGridAdapter(Context context, boolean showCamera, Handler mHandle){
        mContext = context;
        this.mHandle = mHandle;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showCamera = showCamera;
        mItemLayoutParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
    }
    /**
     * 显示选择指示器
     * @param b
     */
    public void showSelectIndicator(boolean b) {
        showSelectIndicator = b;
    }

    public void setShowCamera(boolean b){
        if(showCamera == b) return;

        showCamera = b;
        notifyDataSetChanged();
    }

    public boolean isShowCamera(){
        return showCamera;
    }

    /**
     * 选择某个图片，改变选择状态
     * @param image
     */
    public void select(Image image, GridView listView, int position) {
        if(mSelectedImages.contains(image)){
            mSelectedImages.remove(image);
        }else{
            mSelectedImages.add(image);
        }
        updateSingleRow(listView, position, image);
    }
    
    private void updateSingleRow(GridView listView, int position, Image image){
        View v = listView.getChildAt(position - 
        		listView.getFirstVisiblePosition());

        if(v == null)
           return;

        ImageView indicator = (ImageView) v.findViewById(R.id.checkmark);
        // 处理单选和多选状态
        if(showSelectIndicator){
            indicator.setVisibility(View.VISIBLE);
            if(mSelectedImages.contains(image)){
                // 设置选中状态
                indicator.setImageResource(R.drawable.btn_selected);
            }else{
                // 未选择
                indicator.setImageResource(R.drawable.btn_unselected);
            }
        }else{
            indicator.setVisibility(View.GONE);
        }
    }

  /*  private void updateSingleRow(GridView listView) { 
    	   
        if (listView != null) { 
            int start = listView.getFirstVisiblePosition(); 
            for (int i = start, j = listView.getLastVisiblePosition(); i <= j; i++) 
                if (R.id.fl_all == ((View) listView.getItemAtPosition(i)).getId()) { 
                    View view = listView.getChildAt(i - start); 
                    getView(i, view, listView); 
                    break;
                } 
        } 
    }*/
    
    /**
     * 通过图片路径设置默认选择
     * @param resultList
     */
    public void setDefaultSelected(ArrayList<String> resultList) {
    	mSelectedImages.clear();
        for(String path : resultList){
            Image image = getImageByPath(path);
            if(image != null){
                mSelectedImages.add(image);
            }
        }
        if(mSelectedImages.size() > 0){
            notifyDataSetChanged();
        }
    }

    private Image getImageByPath(String path){
        if(mImages != null && mImages.size()>0){
            for(Image image : mImages){
                if(image.path.equalsIgnoreCase(path)){
                    return image;
                }
            }
        }
        return null;
    }

    /**
     * 设置数据集
     * @param images
     */
    public void setData(List<Image> images) {
        mSelectedImages.clear();

        if(images != null && images.size()>0){
            mImages = images;
        }else{
            mImages.clear();
        }
        notifyDataSetChanged();
    }
    
    public List<Image> getData()
    {
    	return mImages;
    }

    private DisplayImageOptions options;
    /**
     * 重置每个Column的Size
     * @param columnWidth
     */
    public void setItemSize(int columnWidth) {

        if(mItemSize == columnWidth){
            return;
        }

     
        mItemSize = columnWidth;
        options = new DisplayImageOptions.Builder()
        		.resetViewBeforeLoading(true)
        		.showImageOnFail(R.drawable.default_error) // resource or drawable
    			.displayer(new ResizeBitmapDisplayer(mItemSize, mItemSize, ResizeBitmapDisplayer.fixXY))
    			.cacheInMemory(false)
    			.cacheOnDisc(false)
    			.bitmapConfig(Bitmap.Config.RGB_565)
    			.build();
        
        mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);

        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(showCamera){
            return position==0 ? TYPE_CAMERA : TYPE_NORMAL;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getCount() {
        return showCamera ? mImages.size()+1 : mImages.size();
    }

    @Override
    public Image getItem(int i) {
        if(showCamera){
            if(i == 0){
                return null;
            }
            return mImages.get(i-1);
        }else{
            return mImages.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        int type = getItemViewType(i);
        if(type == TYPE_CAMERA){
            view = mInflater.inflate(R.layout.list_item_camera, viewGroup, false);
            view.setTag(null);
        }else if(type == TYPE_NORMAL){
            ViewHolde holde;
            if(view == null){
                view = mInflater.inflate(R.layout.list_item_image, viewGroup, false);
                holde = new ViewHolde(view);
            }else{
                holde = (ViewHolde) view.getTag();
                if(holde == null){
                    view = mInflater.inflate(R.layout.list_item_image, viewGroup, false);
                    holde = new ViewHolde(view);
                }
            }
            if(holde != null) {
            	holde.setItemClickListener(i);
                holde.bindData(getItem(i));
            }
        }

        /** Fixed View Size */
        GridView.LayoutParams lp = (GridView.LayoutParams) view.getLayoutParams();
        if(lp.height != mItemSize){
            view.setLayoutParams(mItemLayoutParams);
        }

        return view;
    }

    class ViewHolde {
        ImageView image;
        ImageView indicator;
        ViewHolde(View view){
            image = (ImageView) view.findViewById(R.id.image);
            indicator = (ImageView) view.findViewById(R.id.checkmark);
            view.setTag(this);
        }

        void setItemClickListener(int position)
        {
        	image.setOnClickListener(new OnItemChildClickListener(CLICK_INDEX_IMAGE, position, mHandle));
        	indicator.setOnClickListener(new OnItemChildClickListener(CLICK_INDEX_CHOICE, position, mHandle));
        }
        void bindData(final Image data){
            if(data == null) return;
            // 处理单选和多选状态
            if(showSelectIndicator){
                indicator.setVisibility(View.VISIBLE);
                if(mSelectedImages.contains(data)){
                    // 设置选中状态
                    indicator.setImageResource(R.drawable.btn_selected);
                }else{
                    // 未选择
                    indicator.setImageResource(R.drawable.btn_unselected);
                }
            }else{
                indicator.setVisibility(View.GONE);
            }
//            File imageFile = new File(data.path);

            if(mItemSize > 0) {
                // 显示图片
            	image.setTag(Configure.getInstance().getFileUrl(data.thumbnailsPath));
            	GlobalImageLoader.getIntance().dispalyImg(image, Configure.getInstance().getFileUrl(data.thumbnailsPath), options);
            	/*
                Picasso.with(mContext)
                        .load(imageFile)
                        .placeholder(R.drawable.default_error)
                                //.error(R.drawable.default_error)
                        .resize(mItemSize, mItemSize)
                        .centerCrop()
                        .into(image);*/
            }
        }
    }

}
