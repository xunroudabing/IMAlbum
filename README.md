# IMAlbum
一个完整的支持单选多选图片、截图功能、可扩展业务的图片滑动预览图库

使用参数参考com.opensource.configure.IntentUtils

/**
 * 界面跳转类
 * @description：
 * @author zhangjianlin (990996641)
 * @date 2015年4月28日 上午10:52:07
 */
public class IntentUtils {

	public static final int REQUEST_IMAGE = 10000;
	public static final int REQUEST_CROPIMAGE = 10001;

	public static void jumpToSelectPics(Activity mContext, boolean isShowCamera, int selectMaxCount, int selectMode,
			String submitBtnStr, boolean isJumpToCheckPics, ArrayList<String> hasSelectList) {
		Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);

		// whether show camera
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, isShowCamera);

		// String of submit button
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SUBMIT_TITLE, submitBtnStr);

		// String of submit button
		intent.putExtra(MultiImageSelectorFragment.EXTRA_JUMPTOCHECKPICS, isJumpToCheckPics);

		// max select image amount
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, selectMaxCount);

		// select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectMode);

		// EXTRA_DEFAULT_SELECTED_LIST

		if (hasSelectList != null) {
			intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, hasSelectList);
		}
		//
		mContext.startActivityForResult(intent, REQUEST_IMAGE);
	}

	/**
	 * @param isShowCamera是否可拍照
	 * @param selectMaxCount selectMode=MultiImageSelectorActivity.MODE_MULTI,selectMaxCount表示选择的照片最大数  
	 * 		  selectMode=MultiImageSelectorActivity.MODE_SINGLE,selectMaxCount=1表示选择后的图片要剪切，否则返回选择的图片路径
	 * @param selectMode MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI
	 * @param isScaleImg 在单选模式下是否截图
	 * @description：
	 * @date 2015年4月28日 上午10:50:16
	 */

	public static void jumpToSelectPics(Activity mContext, boolean isShowCamera, int selectMaxCount, int selectMode) {
		Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);

		// whether show camera
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, isShowCamera);

		// max select image amount
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, selectMaxCount);

		// select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectMode);
		//
		mContext.startActivityForResult(intent, REQUEST_IMAGE);
	}

	/**
	 * @param isShowCamera是否可拍照
	 * @param selectMaxCount selectMode=MultiImageSelectorActivity.MODE_MULTI,selectMaxCount表示选择的照片最大数  
	 * 		  selectMode=MultiImageSelectorActivity.MODE_SINGLE,selectMaxCount=1表示选择后的图片要剪切，否则返回选择的图片路径
	 * @param selectMode MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI
	 * @param isScaleImg 在单选模式下是否截图
	 * @param hasSelectList 空间发表动态时候，已经选择的图片
	 * @description：
	 * @date 2015年4月28日 上午10:50:16
	 */

	public static void jumpToSelectPics(Activity mContext, boolean isShowCamera, int selectMaxCount, int selectMode,
			ArrayList<String> hasSelectList) {
		Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);

		// whether show camera
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, isShowCamera);

		// max select image amount
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, selectMaxCount);

		// select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectMode);
		// EXTRA_DEFAULT_SELECTED_LIST
		intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, hasSelectList);
		mContext.startActivityForResult(intent, REQUEST_IMAGE);
	}

	/**
	 
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if(requestCode == REQUEST_IMAGE){
	    if(resultCode == RESULT_OK){
	        // Get the result list of select image paths
	        List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
	        // do your logic ....
	    }
	}
	}
	 */

	/**
	 * @param mPhotoCheck选中的图片,填空或null表示从第一个开始
	 * @param mImageList 要预览的图片list
	 * @description：
	 * @author zhangjianlin (990996641)
	 * @param <T>
	 * @date 2015年5月5日 下午12:03:59
	 */

	public static <T extends ImagePagerUri> void jumpToPreViewImage(Activity mContext, ArrayList<T> mImageList,
			String mPhotoCheck) {
		Intent intent = new Intent(mContext, ImageDetailAct.class);
		intent.putExtra("imageListBean", mImageList);
		intent.putExtra("imageDetailCheck", mPhotoCheck);
		mContext.startActivity(intent);
	}

	/**
	 * @param imageDetailCheckPositon选中的图片, 默认位置
	 * @param mImageList 要预览的图片list
	 * @description：
	 * @author zhangjianlin (990996641)
	 * @param <T>
	 * @date 2015年5月5日 下午12:03:59
	 */

	public static <T extends ImagePagerUri> void jumpToPreViewImage(Activity mContext, ArrayList<T> mImageList,
			int imageDetailCheckPositon) {
		Intent intent = new Intent(mContext, ImageDetailAct.class);
		intent.putExtra("imageListBean", mImageList);
		intent.putExtra("imageDetailCheckPositon", imageDetailCheckPositon);
		mContext.startActivity(intent);
	}

	/**
	 * 图片格式jpeg
	 * @param picResultPath 图片的要保存的地址
	 * @param scaleW 截图区域的宽度   单位 dp
	 * @param scaleH 截图区域的高度   单位 dp
	 * @param limtWidth 裁剪出来的图的宽度 
	 * @param limitHight 裁剪出来的图的高度
	 * @param isScaleImg 是否按照 limtWidth、limitHight的比例进行放大缩小
	 * @description：
	 * @author zhangjianlin (990996641)
	 * @date 2015年6月11日 下午4:25:00
	 */

	public static void jumpToSelecAndCropImg(Activity mContext, String picResultPath, int scaleW, int scaleH,
			int limtWidth, int limitHight, boolean isScaleImg) {
		Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);

		// whether show camera
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);

		// max select image amount
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
		// select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
		// intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_CROP_CAMERA, isScaleImg);

		intent.putExtra(CropImageAct.RESIZEBIT, isScaleImg);
		intent.putExtra(CropImageAct.RESULT, picResultPath);
		intent.putExtra(CropImageAct.SCALEW, scaleW);
		intent.putExtra(CropImageAct.SCALEH, scaleH);
		intent.putExtra(CropImageAct.LIMITW, limtWidth);
		intent.putExtra(CropImageAct.LIMITH, limitHight);

		mContext.startActivityForResult(intent, REQUEST_CROPIMAGE);
	}

	/**
	 * 图片格式jpeg
	 * @param orgUri 图片的原始uri 的string
	 * @param savePath 图片的要保存的地址
	 * @param scaleW 截图区域的宽度   单位 dp
	 * @param scaleH 截图区域的高度   单位 dp
	 * @param limtWidth 裁剪出来的图的宽度 
	 * @param limitHight 裁剪出来的图的高度
	 * @param isEableMatrix 是否按照 limtWidth、limitHight的比例进行放大缩小
	 * @description：
	 */

	public static void jumpToCropImage(Activity mContext, String orgUri, String savePath, int scaleW, int scaleH,
			int limtWidth, int limitHight, boolean isEableMatrix) {
		Intent intent = new Intent(mContext, CropImageAct.class);
		intent.putExtra(CropImageAct.PHOTOURI, orgUri);
		intent.putExtra(CropImageAct.RESULT, savePath);
		intent.putExtra(CropImageAct.SCALEH, scaleH);
		intent.putExtra(CropImageAct.SCALEW, scaleW);
		intent.putExtra(CropImageAct.LIMITW, limtWidth);
		intent.putExtra(CropImageAct.LIMITH, limitHight);
		intent.putExtra(CropImageAct.RESIZEBIT, isEableMatrix);
		mContext.startActivityForResult(intent, REQUEST_CROPIMAGE);
	}

	/**
	 * @Description:选择单张图片不剪切
	 * 返回值  ArrayList
	 switch (requestCode) {
	 case IntentUtils.REQUEST_IMAGE:
				if (data != null) {
					ArrayList<String> resultList = data
							.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				}
				break;
	 }
	 * @param mContext
	 */
	public static void jumpToSelectNoCropImg(Activity mContext) {
		jumpToSelectPics(mContext, false, 2, MultiImageSelectorActivity.MODE_SINGLE);
	}

	public static <T extends ImagePagerUri> void jumpToPreViewImageChecked(Activity mContext, ArrayList<T> mImageList,
			int imageDetailCheckPositon, ArrayList<String> hasSelectList, int maxSelectCount, String submitStr) {
		Intent intent = new Intent(mContext, ImageDetailCheckPicsAct.class);
		// intent.putExtra("imageListBean", mImageList);
		ImageDetailCheckPicsAct.putValue("imageListBean", mImageList);
		intent.putExtra("imageDetailCheckPositon", imageDetailCheckPositon);
		intent.putExtra(ImageDetailCheckPicsAct.EXTRA_CHECKEDPICS, hasSelectList);
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxSelectCount);
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SUBMIT_TITLE, submitStr);
		mContext.startActivityForResult(intent, ImageDetailCheckPicsAct.REQUEST_CHECKFILE);
	}
}






后续扩展及内部讲解将会在  http://blog.csdn.net/lilin9105 博客上写出
