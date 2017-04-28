//package cn.web.front.action.file;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.HashMap;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.fileupload.disk.DiskFileItem;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.commons.CommonsMultipartFile;
//
//import cn.file.service.IFileService;
//import cn.sdk.util.MsgCode;
//import cn.web.front.support.BaseAction;
//import net.sf.json.JSONObject;
//
///**
// * @ClassName: ImgUploadAction 
// * @Description: TODO(图片上传工具类) 
// * @author yangzan 
// * @date 2017年4月10日 下午3:11:26 
// *
// */
//@Controller
//@RequestMapping(value="/common")
//public class ImgUploadAction extends BaseAction{
//	private final static Logger logger = LoggerFactory.getLogger(ImgUploadAction.class);
//
//    @Autowired
//    @Qualifier("fileService")
//    private IFileService  fileService;
//    
//    /**
//     * @Title: imgUpload 
//     * @Description: TODO(图片上传) 
//     * @param @param request
//     * @param @param response
//     * @param @param file 文件流
//     * @return void 返回类型 
//     * @throws
//     */
//	@RequestMapping(value="/imgUpload.html")
//	public void imgUpload(HttpServletRequest request,HttpServletResponse response,
//			@RequestParam(value="file",required=false) MultipartFile file,@RequestParam(value="days",required=false) Integer days){
//		PrintWriter out=null;
//		HashMap<String,Object> jsonMap = new HashMap<String, Object>();
//		
//		try {
//			request.setCharacterEncoding("UTF-8");
//			response.setCharacterEncoding("UTF-8");
//			response.setContentType("text/html;charset=UTF-8");
//			out = response.getWriter();
//			
//			//验证file是否为空
//			if (file.isEmpty()) {
//				jsonMap.put("code", MsgCode.paramsError);
//				jsonMap.put("msg", "请选择上传图片！");
//				out.print(JSONObject.fromObject(jsonMap));
//				return;
//			}
//			
//			//转存文件  获取File
//			File file2 = multipartToFile(file);
//			//调用七牛上传图片  获得Url
//			String url = fileService.uploadFile(file2,days);
//			
//			jsonMap.put("code", MsgCode.success);
//			jsonMap.put("msg", "图片上传成功！");
//			jsonMap.put("imgUrl", url);
//			out.print(JSONObject.fromObject(jsonMap));
//		} catch (Exception e) {
//			logger.error("图片上传Action异常:"+e);
//			
//			jsonMap.put("success", MsgCode.exception);
//			jsonMap.put("msg", "服务器繁忙！");
//			out.print(JSONObject.fromObject(jsonMap));
//		}
//	}
//	
//	
//	/**
//	 * @Title: getUpToken 
//	 * @Description: TODO(获取七牛上传凭证) 
//	 * @param @param request
//	 * @param @param response 设定文件 
//	 * @return void 返回类型 
//	 * @throws
//	 */
//	@RequestMapping(value="/getUpToken.html")
//	public void getUpToken(HttpServletRequest request,HttpServletResponse response){
//		PrintWriter out=null;
//		HashMap<String,Object> jsonMap = new HashMap<String, Object>();
//		
//		try {
//			request.setCharacterEncoding("UTF-8");
//			response.setCharacterEncoding("UTF-8");
//			response.setContentType("text/html;charset=UTF-8");
//			out = response.getWriter();
//			
//			String token = fileService.getUpToken();
//			jsonMap.put("code", MsgCode.success);
//			jsonMap.put("msg", "获取上传凭证成功！");
//			jsonMap.put("upToken", token);
//			out.print(JSONObject.fromObject(jsonMap));
//		} catch (Exception e) {
//			logger.error("获取上传凭证异常:"+e);
//			
//			jsonMap.put("success", MsgCode.exception);
//			jsonMap.put("msg", "服务器繁忙！");
//			out.print(JSONObject.fromObject(jsonMap));
//		}
//	}
//	
//	/**
//	 * @Title: multipartToFile 
//	 * @Description: TODO(multipartToFile) 
//	 * @param @param multfile
//	 * @param @return
//	 * @param @throws IOException 设定文件 
//	 * @return File 返回类型 
//	 * @throws
//	 */
//	private File multipartToFile(MultipartFile multfile) throws IOException {  
//		File file = null;
//		try {
//			CommonsMultipartFile cf = (CommonsMultipartFile)multfile;   
//	        //这个myfile是MultipartFile的  
//	        DiskFileItem fi = (DiskFileItem) cf.getFileItem();  
//	        file = fi.getStoreLocation();  
//	        
//	        //手动创建临时文件 
////	        if(file.length() < CommonConstants.MIN_FILE_SIZE){  
////	            File tmpFile = new File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") +   
////	                    file.getName());  
////	            multfile.transferTo(tmpFile);  
////	            return tmpFile;  
////	        }  
//		} catch (Exception e) {
//			throw new IOException("文件转换异常！");
//		}
//        return file;  
//    }  
//}
